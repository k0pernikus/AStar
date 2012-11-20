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
        return path;
    }
}
