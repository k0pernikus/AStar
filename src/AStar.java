import java.awt.*;
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
    private List<TileButton> path;
    private TileButton[][] tileButtons;
    private GameBoard gameboard;

    public AStar(GameBoard gameBoard, TileButton[][] tileButtons) {
        this.gameboard = gameBoard;
        this.tileButtons = tileButtons;
        this.path = new ArrayList<TileButton>();
        this.init();
    }

    private void init() {
        for (TileButton[] buttons : tileButtons) {
            for (TileButton button : buttons) {
                Tile tile = button.getTile();
                if (button.isWall()) {
                    tile.setIsSolid(true);
                }
            }
        }

        for (TileButton[] buttons : tileButtons) {
            for (TileButton button : buttons) {
                Tile tile = button.getTile();
                tile.findNeighbours(tileButtons);
            }
        }
    }

    public List<TileButton> getPath(TileButton startButton, TileButton target) {
        TileButton pathEntry = startButton;
        path.add(pathEntry);
        pathEntry.getTile().setIsOpen(false);

        int counter = 0;
        while (pathEntry != target) {
            pathEntry = getTileWithLowestFScore(pathEntry, target);
            path.add(pathEntry);
        }

        return path;
    }

    public TileButton getTileWithLowestFScore(TileButton currentTileButton, TileButton targetButton) {
        currentTileButton.getTile().setIsOpen(false);
        System.out.println("target:" + targetButton.getCoordinateX() + targetButton.getCoordinateY());
        List<Tile> neighbors = currentTileButton.getTile().getNeighbors();
        for (Tile neighbor : neighbors) {
            neighbor.calculateH(targetButton.getTile());
            neighbor.calculateGcost(currentTileButton.getTile());
            neighbor.calculateF();
            neighbor.getTileButton().setText("" + neighbor.getF());
            System.out.println("F: " + neighbor.getF());
        }

        Tile lowestScore = null;
        for (Tile neighbor : neighbors) {
            if (neighbor.isOpen()) {
                lowestScore = neighbor;
            }

        }

        for (Tile tile : neighbors) {
            assert lowestScore != null;
            if (lowestScore.getF() > tile.getF() && tile.isOpen()) {
                lowestScore = tile;
            }
//            System.out.println("------");
//            System.out.println("" + tile.getCoordinateX() + "," + tile.getCoordinateY());
//            System.out.println("isWall " + tile.getTileButton().isWall());
//            System.out.println("isSolid" + tile.isSolid());
//            System.out.println("H = " + tile.getH());
//            System.out.println("G = " + tile.getG());
//            System.out.println("F = " + tile.getF());
//            System.out.println("------");
        }

        System.out.println("Lowest Score:" + lowestScore.getCoordinateX() + lowestScore.getCoordinateY() + lowestScore.getF());

        assert null != lowestScore;
        lowestScore.getTileButton().setBackground(Color.cyan);
        gameboard.lineDrawer.validate();
        gameboard.lineDrawer.repaint();

        return lowestScore.getTileButton();
    }
}
