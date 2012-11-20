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

    private Tile parent;
    private List<Tile> neighbors;

    private TileButton button;

    public Tile(TileButton Button) {
        this.neighbors = new ArrayList<Tile>();
        this.button = Button;

        this.setIsOpen(false);
        this.setIsClosed(false);
        this.setIsSolid(Button.state == TileState.WALL);
    }

    public int getCoordinateX() {
        return this.button.getCoordinateX();
    }

    public int getCoordinateY() {
        return this.button.getCoordinateY();
    }

    public int getH() {
        return this.h;
    }

    public int getF() {
        System.out.println("F =" + this.f);

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

    public void setIsOpen(boolean value) {
        this.isOpen = value;
    }

    public void setIsClosed(boolean value) {
        this.isClosed = value;
    }

    public void setParent(Tile parent) {
        this.parent = parent;
    }


    public void setG(int g) {
        this.g = g;
        calculateF();
    }

    public void calculateH(Tile targetTile) {
        this.h = (Math.abs(this.getCoordinateX() - targetTile.getCoordinateX()) + Math.abs(this.getCoordinateY() - targetTile.getCoordinateY()));
    }

    public void calculateF() {
        this.f = this.getH() + this.getG();
        this.button.setText("" +f);
    }

    public void setIsSolid(boolean solidState) {
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

    public void log() {
//        System.out.print("-------------------" + "\n" +
//                "G =" + this.getG() + "\n" +
//                "H =" + this.getH() + "\n" +
//                "F =" + this.getF() + "\n\n"
//        );
    }
}
