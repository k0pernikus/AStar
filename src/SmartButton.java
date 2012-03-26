import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 *A Button class capable of representing a node a  A*-algorithm.
 * @author jesperpersson
 *
 */

public class SmartButton extends JButton{
	//The tiles place in the grid
	private int column;
	private int row;

	//The cost to reach this tile from the starting point.
	private double g;

	//the estimated (just a motivated guess) cost to reach the goal from this tile.
	private double h;

	//h+g, a estimated guess of the total cost to reach the goal from this tile.
	private double f;
	//The tile from which we reached this tile, using the A* algorithm.
	public SmartButton parent;
	private boolean closed;
	private boolean open;
	
	private List<SmartButton> neighbours;

	public TileState state;

	public SmartButton(int row, int column,ActionListener listener){
		state = TileState.REGULAR;
		setBackground(Color.WHITE);
		this.setBorder(new LineBorder(Color.RED, 1));
		this.setOpaque(true);
		this.addActionListener(listener);
		this.setPreferredSize(new Dimension(30,30));
		this.row=row;
		this.column=column;
		this.closed=false;
		this.open=false;
		h=0;
		neighbours = new ArrayList<SmartButton>();
	}

	public int getColumn(){
		return this.column;
	}

	public int getRow(){
		return this.row;
	}
	
	public List<SmartButton> getNeighbours(){
		return neighbours;
	}
	
	public void addNeighbour(SmartButton button){
		neighbours.add(button);
	}

	//Since the h-value is unchanging, this method will also change the value of f (g+h)
	public void setG(double g){
		if (this.h >0){
		this.g = g;
		this.f=g+h;
		}
	}
	
	public void setH(SmartButton stop){
		h = (Math.abs(this.row-stop.getRow()) + Math.abs(this.column-stop.getColumn()));

	}
	public void setClosed(boolean value){
		this.closed=value;
	}
	
	public void setOpen(boolean value){
		this.open=value;
	}

	public boolean isClosed(){
		return this.closed;
	}
	
	public boolean isOpen(){
		return this.open;
	}
	
	public double getH(){
		return this.h;
	}
	
	public double getF(){
		return this.h;
	}
	
	public double getG(){
		return this.g;
	}
	
	public boolean isDiagonal(SmartButton button){
		return (this.getRow() != button.getRow() && this.getColumn() != button.getColumn());
	}

}
