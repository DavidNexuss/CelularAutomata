package nsoft.automata;

import javax.swing.JFrame;

public class Main extends JFrame {

	static final int gridSize = 5;

	static final int Width = 800;
	static final int Height = 600;
	
	static final int CWidth = Width/gridSize;
	static final int CHeight = Height/gridSize;
	
	static Cell[][] cells = new Cell[800/gridSize][600/gridSize];
	
	static Panel p = new Panel();
	static Thread Simulation = new Thread(()->{
		
		try {
			
			while(true) {
				

				update();
				//Thread.sleep(20);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	});
	static Thread Render = new Thread(()->{
		
		try {

			while (true) {
				Thread.sleep(1000/60);
				p.repaint();
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	});
	
	public Main(String name) {
		
		super(name);

		setLocationRelativeTo(null);
		setSize(800, 600);
		setContentPane(p);
		initStage();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		Simulation.start();
		Render.start();
		
	}
	
	public static void initStage() {
		
		for (int i = 0; i < CWidth; i++) {
			
			for (int j = 0; j < CHeight; j++) {
				
				cells[i][j] = new Cell();
			}
		}
	}
	public static void update() {
		
		for (int i = 0; i < CWidth; i++) {
			
			for (int j = 0; j < CHeight; j++) {
				
				int mi = i-1;
				int mj = j-1;
				
				int li = (i+1) % CWidth;
				int lj = (j+1) % CHeight;
				
				if(mi < 0) mi= CWidth -1;
				if(mj < 0) mj = CHeight -1;
				cells[i][j].act(
						
						cells[mi] [j],
						cells[i] [mj],
						cells[mi][mj],
						cells[li] [j],
						cells[i] [lj],
						cells[li] [lj]
						);
			}
		}
	}
	public static void main(String[] args) {
	
		new Main("Simulation");
	}
}
