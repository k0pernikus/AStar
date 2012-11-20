import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Class for representing a gameboard of Smartbuttons. Containts a AStar-object
 * for finding paths between tiles
 *
 * @author jesperpersson
 * @author k0pernikus
 */

public class GameBoard extends JFrame implements ActionListener {
    protected int gameboardHeight;
    protected int gameboardWidth;
    protected boolean hasStartField;
    protected boolean hasTargetField;
    protected TileButton target;
    protected TileButton start;
    LineDrawer lineDrawer;
    TileButton[][] tileButtons;

    // Constructor, takes two ints for parameters. Will create a main gridview
    // of smartbuttons, the size set by the parameters gameboardWidth and gameboardHeight.
    public GameBoard(int width, int height) {
        this.gameboardHeight = height;
        this.gameboardWidth = width;

        hasStartField = false;
        hasTargetField = false;

        this.setLayout(new BorderLayout());
        this.initGameboardHoldingPanel();

        // Initiates and specifies the controlpanel.
        this.initControlPanel();

        // Initiate matrix for storing buttons
        tileButtons = new TileButton[width][height];
        this.createButtons();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }

    public static void main(String[] args) {
        new GameBoard(Config.GAMEBOARD_WIDTH, Config.GAMEBOARD_HEIGHT);
    }

    private void initControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(this.gameboardWidth * Config.TILE_SIZE_IN_PIXEL + 1, Config.TILE_SIZE_IN_PIXEL));
        this.add(controlPanel, BorderLayout.SOUTH);
        ButtonHandler btnHandler = new ButtonHandler(this, controlPanel);
        btnHandler.addButtons();
    }

    private void initGameboardHoldingPanel() {
        lineDrawer = new LineDrawer();
        lineDrawer.setPreferredSize(new Dimension(this.gameboardWidth * Config.TILE_SIZE_IN_PIXEL + 1, gameboardHeight * Config.TILE_SIZE_IN_PIXEL));
        lineDrawer.setLayout(new GridLayout(this.gameboardWidth, this.gameboardHeight));
        this.add(lineDrawer, BorderLayout.CENTER);
    }

    /**
     * Fills the matrix tileButtons with buttons, and adds each of them to
     * the gridpanel
     */
    private void createButtons() {
        for (int x = 0; x < gameboardWidth; x++) {
            for (int y = 0; y < gameboardHeight; y++) {
                tileButtons[x][y] = new TileButton(x, y, this);
                lineDrawer.add(tileButtons[x][y]);
            }
        }
    }

    /**
     * Given a list of SmartButtons, will change the background of every tile in
     * the list
     *
     * @param path
     */
    private void paintPath(List<TileButton> path) {
        if (path.size() > 2) {
            // Starting from second element and stopping one before reaching the
            // last element in order to keep look on start/target-buttons intact.
            for (TileButton tileButton : path) {
                //Get center point of each element, add them to path in LineDrawer
                Rectangle place = tileButton.getBounds();
                int x = place.x + place.width / 2;
                int y = place.y + place.height / 2;

                lineDrawer.addToPath(new Point(x, y));
            }

            lineDrawer.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If the reset-button is pressed, the gameboard will be reset, all
        // buttons changed to regular and white, start and target will be marked as unexisting, and any paths in the lineDrawer will be erased.
        if (e.getActionCommand().equals("reset")) {
            this.resetPressed();
        } else if (e.getActionCommand().equals("exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("findPath")) {
            if (hasStartField && hasTargetField) {
                this.findPath();
            }
        } else {
            //System.out.println(e.getActionCommand());

            String[] coordinates = e.getActionCommand().split(",");

            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            this.toggleState(x, y);
        }
    }

    private void findPath() {
        AStar pathfinder = new AStar(this.tileButtons);
        List<TileButton> path = pathfinder.getPath(this.start, this.target);

        if (!path.isEmpty()) {
            paintPath(path);
        } else {
            System.out.println("No path found ");
        }
    }

    private void toggleState(int x, int y) {
        TileButton selectedTile = this.tileButtons[x][y];
        new SelectedButtonToggleHandler(this, selectedTile);
    }

    private void resetPressed() {
        hasStartField = false;
        hasTargetField = false;

        target = null;
        start = null;

        lineDrawer.clearPath();

        for (int i = 0; i < gameboardWidth; i++) {
            for (int l = 0; l < gameboardHeight; l++) {
                tileButtons[i][l].turnIntoStandardField();
            }
        }

        lineDrawer.validate();
        lineDrawer.repaint();
    }
}
