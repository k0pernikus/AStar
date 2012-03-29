import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 *A Button class capable of representing different kinds of "states", Start, Stop, Regular or Collidable.
 * @author jesperpersson
 *
 */

public class SmartButton extends JButton{
	//The tiles place in the grid
	private int column;
	private int row;


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

	}

	public int getColumn(){
		return this.column;
	}

	public int getRow(){
		return this.row;
	}
	


	

}
