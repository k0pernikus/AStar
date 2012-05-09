import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class LinePanel extends JPanel {

	private List <Point> path;

	public LinePanel(){
		this.path = new ArrayList<Point>();
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (path.size()>=2){
			g.setColor(Color.MAGENTA);
			for (int i = 0;i<path.size()-1;i++){
				g.drawLine(path.get(i).x, path.get(i).y, path.get(i+1).x, path.get(i+1).y);
			}
		}

	}

	public void addToPath(Point point){
		this.path.add(point);
	}

	public void clearPath(){
		this.path.clear();
	}

}
