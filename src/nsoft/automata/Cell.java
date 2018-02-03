package nsoft.automata;

import java.awt.Color;
import java.util.ArrayList;

public class Cell {

	public static int virusMaxPwr = 1;
	public static int maxLifeTime = 1;
	
	private boolean needUp = true;
	public State current = State.NONE;
	
	int lifeTime = 0;
	int strenght = 0;
	static boolean start = true;
	int act = 1;
	
	public void act(Cell ... cells) {

		lifeTime++;
		int Lvirus = 0;
		int Lfood = 0;
		int SF = 0;
		int SV = 0;
		int Svirus = 0;
		int Sfood = 0;
		
		for (int i = 0; i < cells.length; i++) {
			

			Cell cell = cells[i];
			if(cell.current == State.FOOD) {
				
				Sfood++ ;
				Lfood += cell.lifeTime;
				SF += cell.strenght;
			
				if(Lvirus > Lfood && current == State.VIRUS) {
					
					cell.lifeTime-=10;
				}
			}
			
			else if(cell.current == State.VIRUS) { 
				
				Svirus++;
				Lvirus+= cell.lifeTime;
				SV += cell.strenght;
				}
			
		}
		
		if(current == State.NONE) {
			
			if(lifeTime > 20 && Sfood < 5) {
				
				if(Math.random() > .999f) { set(State.FOOD); lifeTime = -10000;}
				else set(State.NONE);
				
			}
			else if(Sfood > 5 && start) {
					
				set(State.VIRUS);
				start = false;
			}
			else if(Lvirus > 400*Svirus) {
				
				set(State.VIRUS);
			}
		}else if(current == State.FOOD) {
			
			if(Svirus> 3 && SV > SF) set(State.VIRUS);
			if(Svirus> 4 ) { set(State.VIRUS); strenght+= Svirus + SV/7 + SF/8;}
			else if(Sfood > 3) set(State.NONE);
			//else if(Sfood == 3 && lifeTime < 1) set(State.NONE);
			else if(Sfood < 1 && lifeTime > 0) set(State.NONE);
			if(lifeTime < -2000000) set(State.NONE);
			if(lifeTime > maxLifeTime) maxLifeTime = lifeTime;
			
		}else if(current == State.VIRUS) {
			
			//if(Snone == 7) set(State.NONE);
			//if(lifeTime > 80 && Svirus > 4) set(State.NONE);
			//if(lifeTime > 10 && Svirus > 4 && Sfood < 1) set(State.NONE);
			
			
			if(lifeTime > 50 && Sfood > 0) {
				
				strenght = Sfood*3/2 + (strenght + SV/7)/2;
				if(strenght > virusMaxPwr) virusMaxPwr = strenght;
			}
			
			if(lifeTime > 5000*act) {
				
				act+=1;
				strenght = (strenght + SV/8)/2;
			}
		}
	}
	
	public Cell() {
		
	}
	public void set(State a) {
		
		
		lifeTime = 0;
		if(a == State.VIRUS) lifeTime -= -10;
		strenght = 0;
		current = a;
		needUp = true;
	}
	public void update() {needUp = true;}
	public boolean isUpdated() {return needUp;}
	
	public enum State{
		
		VIRUS(new float[] {1,0,0})
		,NONE(new float[] {0,0,0})
		,FOOD(new float[] {0,1,0});
		
		public float[] color;
		private State(float[] c) {
			
			color = c;
		}
	}
}
