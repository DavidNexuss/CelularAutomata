package nsoft.automata;

import java.awt.Color;
import java.util.ArrayList;

public class Cell {

	private static ArrayList<Cell> Fnone = new ArrayList<>();
	private static ArrayList<Cell> Ffood = new ArrayList<>();
	private static ArrayList<Cell> Fvirus = new ArrayList<>();
	
	
	private boolean needUp = true;
	public State current = State.NONE;
	
	int lifeTime = 0;
	int strenght = 0;
	public void act(Cell ... cells) {

		lifeTime++;
		int Lnone = 0;
		int Lvirus = 0;
		int Lfood = 0;
		int SF = 0;
		int Snone = 0;
		int Svirus = 0;
		int Sfood = 0;
		
		for (Cell cell : cells) {
			
			if(cell.current == State.FOOD) {
				
				Sfood++ ; Ffood.add(cell); 
				Lfood += cell.lifeTime;
				SF += strenght;}
			
			else if(cell.current == State.VIRUS) { 
				
				Svirus++; Fvirus.add(cell); 
				Lvirus+= cell.lifeTime;
				}
			
			else if(cell.current == State.NONE) {
				
				Snone++; Fnone.add(cell); 
				Lnone += cell.lifeTime;
				}
		}
		
		if(current == State.NONE) {
			
			if(lifeTime > 20 && Sfood < 5) {
				
				if(Math.random() > .999f) { set(State.FOOD); lifeTime = -10000;}
				else set(State.NONE);
				
			}
			else if(lifeTime > 700 && Sfood < 6) {
				
				set(State.VIRUS);
			}
			else if(Lvirus > 700) {
				
				set(State.VIRUS);
			}
		}else if(current == State.FOOD) {
			
			if(Svirus> 3 && Lvirus > Lfood*(strenght * 10)) set(State.VIRUS);
			else if(Sfood > 3) set(State.NONE);
			else if(Sfood < 1 && lifeTime > 0) set(State.NONE);
			
			strenght += Svirus;
			
		}else if(current == State.VIRUS) {
			
			if(Snone > 6) set(State.NONE);
			if(lifeTime*SF > 100) set(State.NONE);
			if(lifeTime > 40 && Svirus > 4) set(State.NONE);
			if(lifeTime > 10 && Svirus > 3 && Sfood < 1) set(State.NONE);
			if(SF > 20)set(State.FOOD);
			if(Sfood == 0) { set(State.NONE);}
			if(Lvirus > Lfood) {
				
				for (Cell cell : Ffood) {
					
					cell.lifeTime -=1;
				}
			}
		}
		
		Fnone.clear();
		Ffood.clear();
		Fvirus.clear();
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
		
		VIRUS(Color.RED)
		,NONE(Color.BLACK)
		,FOOD(Color.GREEN);
		
		public Color color;
		private State(Color c) {
			
			color = c;
		}
	}
}
