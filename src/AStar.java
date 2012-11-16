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
    private Tile[][] logicList;
    private Tile currentTile;
    private SmartButton[][] smartButtons;
    private final double DIAGONALCOST = 1.41421356;

    private int height;
    private int width;

    public AStar(SmartButton[][] smartButtons, int width, int height) {
        this.smartButtons = smartButtons;
        this.height = height;
        this.width = width;
        init();
    }

    private void init() {
        logicList = new Tile[width][height];
        for (int x = 0; x < smartButtons.length; x++) {
            for (int y = 0; y < smartButtons[x].length; y++) {
                logicList[x][y] = new Tile(smartButtons[x][y]);
            }
        }
        generateNeighbors();

    }

    /**
     * Main algorithm. Given a start and a goal, will return the shortest path
     * between the two. If no path is to be found, the return will be a
     * null-object
     *
     * @param start
     * @return null if no path exists between Start and Stop
     */
    public List<SmartButton> getPath(SmartButton startPoint, SmartButton endPoint) {

        Tile start = logicList[startPoint.getCoordinateX()][startPoint.getCoordinateY()];
        Tile target = logicList[endPoint.getCoordinateX()][endPoint.getCoordinateY()];

        for (int i = 0; i < width; i++) {
            for (int l = 0; l < height; l++) {
                logicList[i][l].setH(target);
            }
        }

        openList = new ArrayList<Tile>();
        ArrayList<Tile> path = new ArrayList<Tile>();
        openList.add(start);

        while (!openList.isEmpty()) {
            int pointer = findBestTile();
            currentTile = openList.get(pointer);

            // Algorithm complete and path found.
            if (currentTile.equals(target)) {
                path.add(currentTile);
                while (currentTile != start) {
                    path.add(currentTile.getParent());
                    currentTile = currentTile.getParent();
                }
                clear();
                return convertList(path);

                // The goal was not found in the openList
            } else {
                updateNeighbors(currentTile.getNeighbors());
                currentTile.setClosed(true);
                currentTile.setOpen(false);
                removeFromOpen(currentTile);
            }
        }
        clear();
        return null;
    }

    /* Returns the index of the tile in the open list with the lowest f-value */
    private int findBestTile() {
        int pointer = 0;
        double currentF = openList.get(0).getF();
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getF() < currentF) {
                pointer = i;
                currentF = openList.get(i).getF();
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
            // parent to
            // currentTile

            int g = (int) (currentTile.isDiagonal(currentNeighbor) ? currentTile.getG() + DIAGONALCOST : currentTile.getG() + 1);

            if (currentNeighbor.isClosed() && isCurrentPathShorter(currentNeighbor)) {
                currentNeighbor.setG(g);

                currentNeighbor.setParent(currentTile);

                // if a tile is open and the current paths g-value would be
                // lower than its old g-value, we update the tiles g-value and
                // sets it parent to
                // currentTile
            } else if (currentNeighbor.isOpen() && isCurrentPathShorter(currentNeighbor)) {
                currentNeighbor.setG(g);
                currentNeighbor.setParent(currentTile);

                // if a tile is neither open nor closed, we add it the openList
                // and update the open-value accordingly.
            } else if (!currentNeighbor.isOpen() && !currentNeighbor.isClosed()) {
                currentNeighbor.setOpen(true);
                openList.add(currentNeighbor);
                currentNeighbor.setParent(currentTile);
                currentNeighbor.setG(g);
            }
        }
    }

    // Given a SmartButton, will return whether or not the current path from the
    // start to the button is shorter than the currently recorded.
    private boolean isCurrentPathShorter(Tile tile) {
        return tile.getG() > (currentTile.isDiagonal(tile) ? currentTile.getG() + DIAGONALCOST : currentTile.getG() + 1);
    }

    // Loops through gameboard and make sure every button calculates its
    // neighbors
    private void generateNeighbors() {
        for (Tile[] aLogicList : logicList) {
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
            if (isRelevant(tile, logicList[tile.getCoordinateX()][top])) {
                tile.addNeighbor(logicList[tile.getCoordinateX()][top]);
            }

            if (right < width) {
                if (isRelevant(tile, logicList[right][top])) {
                    tile.addNeighbor(logicList[right][top]);
                }
            }
            if (left >= 0) {
                if (isRelevant(tile, logicList[left][top])) {
                    tile.addNeighbor(logicList[left][top]);
                }
            }
        }

        if (bottom >= 0) {
            if (isRelevant(tile, logicList[tile.getCoordinateX()][bottom])) {
                tile.addNeighbor(logicList[tile.getCoordinateX()][bottom]);
            }

            if (right < width) {
                if (isRelevant(tile, logicList[right][bottom])) {
                    tile.addNeighbor(logicList[right][bottom]);
                }
            }

            if (left >= 0) {
                if (isRelevant(tile, logicList[left][bottom])) {
                    System.out.println("Tile " + left + " " + bottom + "is relevant");
                    tile.addNeighbor(logicList[left][bottom]);
                }
            }
        }
        if (left >= 0) {
            if (isRelevant(tile, logicList[left][tile.getCoordinateY()])) {
                tile.addNeighbor(logicList[left][tile.getCoordinateY()]);
            }
        }
        if (right < width) {
            if (isRelevant(tile, logicList[right][tile.getCoordinateY()])) {
                tile.addNeighbor(logicList[right][tile.getCoordinateY()]);
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
        } else {
            int dx = consideredTile.getCoordinateX() - currentTile.getCoordinateX();
            int dy = consideredTile.getCoordinateY() - currentTile.getCoordinateY();
            return (!logicList[currentTile.getCoordinateX() + dx][currentTile.getCoordinateY()].isSolid() &&
                    !logicList[currentTile.getCoordinateX()][currentTile.getCoordinateY() + dy].isSolid());
        }
    }


    // Removes a SmartButton from the open list
    private void removeFromOpen(Tile tile) {
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i) == tile) {
                openList.remove(i);
            }
        }
    }

    public void clear() {
        for (Tile[] aLogicList : logicList) {
            for (Tile anALogicList : aLogicList) {
                anALogicList.setOpen(false);
                anALogicList.setClosed(false);
            }
        }

        clearOpen();
    }

    private void clearOpen() {
        for (int i = 0; i < openList.size(); i++) {
            openList.remove(i);
        }
    }

    private List<SmartButton> convertList(List<Tile> path) {
        List<SmartButton> returnList = new ArrayList<SmartButton>();
        for (Tile aPath : path) {
            returnList.add(smartButtons[aPath.getCoordinateX()][aPath.getCoordinateY()]);
        }
        return returnList;
    }
}
