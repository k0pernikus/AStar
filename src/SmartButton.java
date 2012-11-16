import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 * A Button class capable of representing different kinds of "states", Start, Stop, Regular or Collidable.
 *
 * @author jesperpersson
 */

public class SmartButton extends JButton {
    public TileState state;
    //The tiles place in the grid
    private int yCoordinate;
    private int xCoordinate;

    public SmartButton(int xCoordinate, int yCoordinate, ActionListener listener) {
        state = TileState.REGULAR;
        setBackground(Color.WHITE);
        this.setBorder(new LineBorder(Color.RED, 1));
        this.setOpaque(true);
        this.addActionListener(listener);
        this.setPreferredSize(new Dimension(30, 30));
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

    }

    public int getCoordinateY() {
        return this.yCoordinate;
    }

    public int getCoordinateX() {
        return this.xCoordinate;
    }
}
