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

public class TileButton extends JButton {
    private TileState state;

    private Tile tile;

    private boolean hasBeenWalked;

    private int yCoordinate;
    private int xCoordinate;

    public TileButton(int xCoordinate, int yCoordinate, ActionListener listener) {
        this.setBorder(new LineBorder(Color.black, Config.LINE_WIDTH_IN_PIXEL));
        this.setOpaque(true);

        this.setPreferredSize(new Dimension(Config.TILE_SIZE_IN_PIXEL, Config.TILE_SIZE_IN_PIXEL));

        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        this.turnIntoStandardField();
        this.addActionListener(listener);
        this.setActionCommand(xCoordinate + "," + yCoordinate);

        this.setTile(new Tile(this));
    }

    public int getCoordinateY() {
        return this.yCoordinate;
    }

    public int getCoordinateX() {
        return this.xCoordinate;
    }

    public void turnIntoWall() {
        this.setBackground(Color.BLACK);
        this.setState(TileState.WALL);
    }

    public void turnIntoStandardField() {
        setState(TileState.REGULAR);
        setBackground(Color.LIGHT_GRAY);
//        this.setText("" + this.getCoordinateX() + ", " + this.getCoordinateY());
    }

    public void turnIntoStartField() {
        this.setState(TileState.START);
        this.setText("Start");
        this.setBackground(Color.GREEN);
    }

    public void turnIntoEndField() {
        this.setState(TileState.TARGET);
        this.setBackground(Color.BLUE);
        this.setText("end");
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public boolean isWall() {
        return this.getState() == TileState.WALL;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public boolean hasBeenWalked() {
        return hasBeenWalked;
    }

    public void setHasBeenWalked(boolean hasBeenWalked) {
        this.hasBeenWalked = hasBeenWalked;
    }
}
