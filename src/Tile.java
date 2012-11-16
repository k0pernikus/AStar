import java.util.ArrayList;
import java.util.List;

/**
 * Class for representing a tile from some Gameboard, in order to implement A*-algorithm.
 * All variables for the algorithm is stored within the Tile, only the.
 *
 * @author jesperpersson
 */
public class Tile {

    private double g;
    private double f;
    private double h;

    private boolean isClosed;
    private boolean isOpen;

    private boolean isSolid;
    private int coordinateX;
    private int coordinateY;

    private Tile parent;
    private List<Tile> neighbors;

    public Tile(SmartButton toRepresent) {
        this.coordinateX = toRepresent.getCoordinateX();
        this.coordinateY = toRepresent.getCoordinateY();
        this.isOpen = false;
        this.isClosed = false;
        this.neighbors = new ArrayList<Tile>();
        this.isSolid = (toRepresent.state == TileState.COLLIDABLE);
    }

    public int getCoordinateX() {
        return this.coordinateX;
    }

    public int getCoordinateY() {
        return this.coordinateY;
    }

    public double getH() {
        return this.h;
    }

    public double getF() {
        return this.f;
    }

    public double getG() {
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


    public void setG(double g) {
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
