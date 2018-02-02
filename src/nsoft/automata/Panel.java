package nsoft.automata;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Panel extends JPanel {

	@Override
	public void paint(Graphics paint) {
		

		super.paint(paint);
		Graphics2D g2d = (Graphics2D)paint;
		for (int i = 0; i < Main.CWidth; i++) {
			
			for (int j = 0; j < Main.CHeight; j++) {
				
				if(Main.cells[i][j].isUpdated()) {
					
					g2d.setColor(Main.cells[i][j].current.color);
					g2d.fillRect(i*Main.gridSize, j*Main.gridSize, (i + 1)*Main.gridSize, (j + 1)*Main.gridSize);
					Main.cells[i][j].update();
				}
			}
		}
	}
}
