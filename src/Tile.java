import java.util.ArrayList;
import java.util.List;

/**
 * Class for representing a tile from some Gameboard, in order to implement A*-algorithm.
 * All variables for the algorithm is stored within the Tile
 *
 * @author jesperpersson
 */
public class Tile {

    private int g;
    private int f;
    private int h;

    private boolean isClosed;
    private boolean isOpen;

    private boolean isSolid;
    private int coordinateX;
    private int coordinateY;

    private Tile parent;
    private List<Tile> neighbors;

    private TileButton button;

    public Tile(TileButton Button) {
        this.button = Button;
        this.coordinateX = Button.getCoordinateX();
        this.coordinateY = Button.getCoordinateY();
        this.isOpen = false;
        this.isClosed = false;
        this.neighbors = new ArrayList<Tile>();
        this.isSolid = (Button.state == TileState.COLLIDABLE);
    }

    public int getCoordinateX() {
        return this.coordinateX;
    }

    public int getCoordinateY() {
        return this.coordinateY;
    }

    public int getH() {
        return this.h;
    }

    public int getF() {
        System.out.println(this.f);

        return this.f;
    }

    public int getG() {
        System.out.println(this.g);

        return this.g;
    }

    public Tile getParent() {
        return this.parent;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public boolean isSolid() {
        return this.isSolid;
    }

    public void setOpen(boolean value) {
        this.isOpen = value;
    }

    public void setClosed(boolean value) {
        this.isClosed = value;
    }

    public void setParent(Tile parent) {
        this.parent = parent;
    }


    public void setG(int g) {
        this.g = g;
        if (this.h > 0) {
            calculateF();
        }
    }

    public void setH(Tile targetTile) {
        this.h = (Math.abs(this.coordinateX - targetTile.getCoordinateX()) + Math.abs(this.coordinateY - targetTile.getCoordinateY()));
    }

    public void calculateF() {
        this.f = this.h + this.g;

        button.setText("" + f);
    }

    public void setSolid(boolean solidState) {
        this.isSolid = solidState;
    }

    public List<Tile> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Tile tile) {
        neighbors.add(tile);
    }

    public boolean isDiagonal(Tile tile) {
        return (this.getCoordinateX() != tile.getCoordinateX() && this.getCoordinateY() != tile.getCoordinateY());
    }

}
