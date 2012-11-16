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
        this.setBorder(new LineBorder(Color.black, Config.LINE_WIDTH_IN_PIXEL));
        this.setOpaque(true);

        this.setPreferredSize(new Dimension(Config.TILE_SIZE_IN_PIXEL, Config.TILE_SIZE_IN_PIXEL));

        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        this.turnIntoStandardField();

        this.addActionListener(listener);
    }

    public int getCoordinateY() {
        return this.yCoordinate;
    }

    public int getCoordinateX() {
        return this.xCoordinate;
    }

    public void turnIntoWall() {
        this.setBackground(Color.BLACK);
        this.state = TileState.COLLIDABLE;
    }

    public void turnIntoStandardField() {
        state = TileState.REGULAR;
        setBackground(Color.LIGHT_GRAY);
    }

    public void turnIntoStartField() {
        this.state = TileState.START;
        this.setBackground(Color.GREEN);
    }

    public void turnIntoEndField() {
        this.state = TileState.STOP;
        this.setBackground(Color.BLUE);
    }
}