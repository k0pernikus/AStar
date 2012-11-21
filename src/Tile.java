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

    private List<Tile> neighbors;

    private TileButton tileButton;

    public Tile(TileButton tileButton) {
        this.neighbors = new ArrayList<Tile>();
        this.tileButton = tileButton;

        this.setIsOpen(false);
        this.setIsClosed(false);
        this.setIsSolid(tileButton.isWall());
    }

    public int getCoordinateX() {
        return this.tileButton.getCoordinateX();
    }

    public int getCoordinateY() {
        return this.tileButton.getCoordinateY();
    }

    public int getH() {
        return this.h;
    }

    public int getF() {
        return this.f;
    }

    public int getG() {
        return this.g;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setIsOpen(boolean value) {
        this.isOpen = value;
    }

    public void setIsClosed(boolean value) {
        this.isClosed = value;
    }

    private void setG(int g) {
        this.g = g;
    }

    public void calculateH(Tile targetTile) {
        this.h = (Math.abs(this.getCoordinateX() - targetTile.getCoordinateX()) + Math.abs(this.getCoordinateY() - targetTile.getCoordinateY())) * 10;
    }

    public void calculateF() {
        this.f = this.getH() + this.getG();
    }

    public void setIsSolid(boolean solidState) {
        boolean solid = solidState;
    }

    public List<Tile> getNeighbors() {
        return neighbors;
    }

    public boolean isDiagonal(Tile compareTo) {
        return (this.getCoordinateX() != compareTo.getCoordinateX() && this.getCoordinateY() != compareTo.getCoordinateY());
    }

    public void calculateGcost(Tile target) {
        if (isDiagonal(target)) {
            this.setG(14);
        } else {
            this.setG(10);
        }
    }

    public void findNeighbours(TileButton[][] tileButtons) {
        this.getCoordinateY();
        this.getCoordinateX();

        int counter = 0;

        for (int xNeighbor = -1; xNeighbor <= 1; xNeighbor++) {
            for (int yNeighbor = -1; yNeighbor <= 1; yNeighbor++) {
                if ((xNeighbor == 0) && (yNeighbor == 0)) {
                    continue;
                }

                int xdiff = this.getCoordinateX() + xNeighbor;
                int ydiff = this.getCoordinateY() + yNeighbor;

                try {
                    TileButton neighbor = tileButtons[xdiff][ydiff];
                    if (!neighbor.isWall()) {
                        this.neighbors.add(neighbor.getTile());
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
//                    System.out.println("That is the end of the gameboard.");
                }
            }
        }

        System.out.println(counter);
    }

    public void log() {
        System.out.print("-------------------" + "\n" +
                "x =" + this.getCoordinateX() + "\n" +
                "y =" + this.getCoordinateY() + "\n");
//                "G =" + this.getG() + "\n" +
//                "H =" + this.getH() + "\n" +
//                "F =" + this.getF() + "\n\n
//
    }

    public void setTileButton(TileButton button) {
        this.tileButton = button;
    }

    public TileButton getTileButton() {
        return this.tileButton;
    }
}
