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
    private Tile[] tiles;
    private TileButton[][] tileButtons;
    private Tile currentTile;

    private final int DIAGONALCOST;
    private final int NORMALCOST;

    private int height;
    private int width;

    public AStar(TileButton[][] tileButtons, int width, int height) {
        DIAGONALCOST = 14;
        NORMALCOST = 10;

        this.tileButtons = tileButtons;
        this.path = new ArrayList<TileButton>();
        this.init();
    }

    private void init() {
        for (TileButton[] buttons : tileButtons) {
            for (TileButton button : buttons) {
                Tile tile = button.getTile();
                tile.findNeighbours(tileButtons);
                button.setText(button.getCoordinateX() + " " + button.getCoordinateY());
            }
        }

        TileButton tileButton  = tileButtons[0][1];
        System.out.println(tileButton.getTile().getNeighbors().size());
        for (Tile tile : tileButton.getTile().getNeighbors()) {
           tile.log();
        }
        System.out.println("__________\n");

    }



    public List<TileButton> getPath(TileButton start, TileButton target) {
        TileButton pathEntry = getTileWithLowestFScore(start, target);

        return path;
    }

    public TileButton getTileWithLowestFScore(TileButton currentTileButton, TileButton targetButton) {
        List<Tile> neighbors = currentTileButton.getTile().getNeighbors();

        for (Tile tile : neighbors) {
            if (!tile.isClosed()) {
                tile.calculateH(targetButton.getTile());
                tile.calculateGcost(currentTileButton.getTile());
                tile.calculateF();
            }
        }

        Tile lowestScore = null;

        for (Tile tile : neighbors) {
            lowestScore = tile;

            if (lowestScore.getF() > tile.getF()) {
                lowestScore = tile;
            }


            System.out.println("H = " + tile.getH() + "\n");
            System.out.println("G = " + tile.getG() + "\n");
            System.out.println("F = " + tile.getF() + "\n");
        }

        return lowestScore.getTileButton();
    }
}
