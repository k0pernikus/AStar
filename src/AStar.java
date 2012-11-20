import java.util.ArrayList;
import java.util.List;

/**
 * Class for representing a grid field of tiles, capable of finding paths
 * between tiles in the field.
 *
 * @author jesperpersson
 * @author k0pernikus
 */
public class AStar {
    private List<Tile> openList;
    private Tile[][] tiles;
    private Tile currentTile;
    private TileButton[][] tileButtons;

    private final int DIAGONALCOST;
    private final int NORMALCOST;

    private int height;
    private int width;

    public AStar(TileButton[][] tileButtons, int width, int height) {
        DIAGONALCOST = 14;
        NORMALCOST = 10;

        this.tileButtons = tileButtons;
        this.height = height;
        this.width = width;

        this.init();
    }

    /*
    create Tiles, inject needed TileButton
    * */

    private void init() {
        this.tiles = new Tile[width][height];
        for (int x = 0; x < this.tileButtons.length; x++) {
            for (int y = 0; y < this.tileButtons[x].length; y++) {
                this.tiles[x][y] = new Tile(tileButtons[x][y]);
            }
        }

        this.generateNeighbors();
    }

    /**
     * Main algorithm. Given a start and a goal, will return the shortest path
     * between the two. If no path is to be found, the return will be a
     * null-object
     */
    public List<TileButton> getPath(TileButton startPoint, TileButton endPoint) {
        Tile start = this.tiles[startPoint.getCoordinateX()][startPoint.getCoordinateY()];
        Tile target = this.tiles[endPoint.getCoordinateX()][endPoint.getCoordinateY()];


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile thatTile = this.tiles[x][y];
                thatTile.calculateH(target);
            }
        }

        this.openList = new ArrayList<Tile>();
        ArrayList<Tile> paths = new ArrayList<Tile>();

        this.openList.add(start);

        while (!this.openList.isEmpty()) {
            int pointer = this.findBestTile();
            this.currentTile = this.openList.get(pointer);

            // Algorithm complete and path found.
            if (this.currentTile.equals(target)) {
                paths.add(this.currentTile);
                while (this.currentTile != start) {
                    paths.add(this.currentTile.getParent());
                    this.currentTile = this.currentTile.getParent();
                }
                clear();
                return convertList(paths);

                // The goal was not found in the openList
            } else {
                updateNeighbors(this.currentTile.getNeighbors());
                this.currentTile.setIsClosed(true);
                this.currentTile.setIsOpen(false);
                removeFromOpen(this.currentTile);
            }
        }
        clear();
        return null;

    }

    /* Returns the index of the tile in the open list with the lowest f-value */
    private int findBestTile() {
        int pointer = 0;
        int currentF = this.openList.get(0).getF();
        for (int i = 0; i < this.openList.size(); i++) {
            if (this.openList.get(i).getF() < currentF) {
                pointer = i;
                currentF = this.openList.get(i).getF();
            }
        }

        return pointer;
    }

    /*
     * Loop through the given list, and updates depending on its relation to its
     * adjacent tile, the one currently selected Should a tile be in a closed or
     * open state, the length of the path between it and the start will be
     * compared to what it would have been had we measured the current path to
     * it instead. Should the tile be neither closed nor open, the path to it
     * will be updated, and it will be added to the open list for consideration
     */
    private void updateNeighbors(List<Tile> currentNeighbors) {
        for (Tile currentNeighbor : currentNeighbors) {

            // if a tile is closed and the current paths g-value would be lower
            // than its old g-value, we update the tiles g-value and sets it
            // parent to currentTile

            int g = this.currentTile.isDiagonal(currentNeighbor) ? (this.currentTile.getG() + DIAGONALCOST) : (currentTile.getG() + NORMALCOST);

            if (currentNeighbor.isClosed() && isCurrentPathShorter(currentNeighbor)) {
                currentNeighbor.setG(g);
                currentNeighbor.setParent(this.currentTile);

                // if a tile is open and the current paths g-value would be
                // lower than its old g-value, we update the tiles g-value and
                // sets it parent to currentTile
            } else if (currentNeighbor.isOpen() && isCurrentPathShorter(currentNeighbor)) {
                currentNeighbor.setG(g);
                currentNeighbor.setParent(this.currentTile);

                // if a tile is neither open nor closed, we add it the openList
                // and update the open-value accordingly.
            } else if (!currentNeighbor.isOpen() && !currentNeighbor.isClosed()) {
                currentNeighbor.setIsOpen(true);
                this.openList.add(currentNeighbor);
                currentNeighbor.setParent(this.currentTile);
                currentNeighbor.setG(g);
            }
        }
    }

    // Given a TileButton, will return whether or not the current path from the
    // start to the button is shorter than the currently recorded.
    private boolean isCurrentPathShorter(Tile tile) {
        return tile.getG() > (this.currentTile.isDiagonal(tile) ? this.currentTile.getG() + this.DIAGONALCOST : this.currentTile.getG() + this.NORMALCOST);
    }

    // Loops through gameboard and make sure every button calculates its
    // neighbors
    private void generateNeighbors() {
        for (Tile[] aLogicList : this.tiles) {
            for (Tile anALogicList : aLogicList) {
                if (!anALogicList.isSolid()) {
                    calculateNeighbors(anALogicList);
                }
            }
        }
    }

    // Tells the specified button to calculate and add its neighbors.
    private void calculateNeighbors(Tile tile) {
        int top = tile.getCoordinateY() + 1;
        int bottom = tile.getCoordinateY() - 1;
        int right = tile.getCoordinateX() + 1;
        int left = tile.getCoordinateX() - 1;

        if (top < height) {
            if (isRelevant(tile, this.tiles[tile.getCoordinateX()][top])) {
                tile.addNeighbor(this.tiles[tile.getCoordinateX()][top]);
            }

            if (right < width) {
                if (isRelevant(tile, this.tiles[right][top])) {
                    tile.addNeighbor(this.tiles[right][top]);
                }
            }
            if (left >= 0) {
                if (isRelevant(tile, this.tiles[left][top])) {
                    tile.addNeighbor(this.tiles[left][top]);
                }
            }
        }

        if (bottom >= 0) {
            if (isRelevant(tile, this.tiles[tile.getCoordinateX()][bottom])) {
                tile.addNeighbor(this.tiles[tile.getCoordinateX()][bottom]);
            }

            if (right < width) {
                if (isRelevant(tile, this.tiles[right][bottom])) {
                    tile.addNeighbor(this.tiles[right][bottom]);
                }
            }

            if (left >= 0) {
                if (isRelevant(tile, this.tiles[left][bottom])) {
                    System.out.println("Tile " + left + " " + bottom + "is relevant");
                    tile.addNeighbor(this.tiles[left][bottom]);
                }
            }
        }
        if (left >= 0) {
            if (isRelevant(tile, this.tiles[left][tile.getCoordinateY()])) {
                tile.addNeighbor(this.tiles[left][tile.getCoordinateY()]);
            }
        }
        if (right < width) {
            if (isRelevant(tile, this.tiles[right][tile.getCoordinateY()])) {
                tile.addNeighbor(this.tiles[right][tile.getCoordinateY()]);
            }
        }
    }


    /*
     * Checks if a certain tile should be added to the current Tiles list of neighbors
     * @return True if the considered tile is not a solid, and the path between the two tiles will not go through a wall.
     */
    private boolean isRelevant(Tile currentTile, Tile consideredTile) {
        if (consideredTile.isSolid()) {
            return false;
        }

        int dx = consideredTile.getCoordinateX() - currentTile.getCoordinateX();
        int dy = consideredTile.getCoordinateY() - currentTile.getCoordinateY();
        return (!tiles[currentTile.getCoordinateX() + dx][currentTile.getCoordinateY()].isSolid() &&
                !tiles[currentTile.getCoordinateX()][currentTile.getCoordinateY() + dy].isSolid());
    }


    // Removes a TileButton from the open list
    private void removeFromOpen(Tile tile) {
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i) == tile) {
                openList.remove(i);
            }
        }
    }

    public void clear() {
        for (Tile[] aLogicList : this.tiles) {
            for (Tile anALogicList : aLogicList) {
                anALogicList.setIsOpen(false);
                anALogicList.setIsClosed(false);
            }
        }

        clearOpen();
    }

    private void clearOpen() {
        for (int i = 0; i < this.openList.size(); i++) {
            this.openList.remove(i);
        }
    }

    private List<TileButton> convertList(List<Tile> paths) {
        List<TileButton> returnList = new ArrayList<TileButton>();

        for (Tile path : paths) {
            returnList.add(this.tileButtons[path.getCoordinateX()][path.getCoordinateY()]);
        }

        return returnList;
    }
}
