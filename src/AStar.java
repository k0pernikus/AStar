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
        while (pathEntry != target) {
            pathEntry = getTileWithLowestFScore(pathEntry, target);
            if (!path.contains(pathEntry)) {
                path.add(pathEntry);
            }
        }
        return path;
    }

    public TileButton getTileWithLowestFScore(TileButton currentTileButton, TileButton targetButton) {
        System.out.println("target:" +  + targetButton.getCoordinateY());
        List<Tile> neighbors = currentTileButton.getTile().getNeighbors();

        for (Tile tile : neighbors) {
            tile.calculateH(targetButton.getTile());
            tile.calculateGcost(currentTileButton.getTile());
            tile.calculateF();

            tile.getTileButton().setText("" + tile.getF());
        }

        Tile lowestScore = null;

        for (Tile tile : neighbors) {
            lowestScore = tile;

            if (lowestScore.getF() < tile.getF()) {
                if(!tile.getTileButton().isWall()) {
                    lowestScore = tile;
                }
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
        gameboard.validate();
        gameboard.repaint();

        return lowestScore.getTileButton();
    }
}
