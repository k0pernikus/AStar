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

    protected TileButtons stop;
    protected TileButtons start;

    LinePanel gridPanel;

    TileButtons[][] gameBoardStates;

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
        gameBoardStates = new TileButtons[width][height];
        createButtons();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }

    private void initControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(this.gameboardWidth * Config.TILE_SIZE_IN_PIXEL + 1, Config.TILE_SIZE_IN_PIXEL));
        this.add(controlPanel, BorderLayout.SOUTH);
        ButtonHandler btnHandler = new ButtonHandler(this, controlPanel);
        btnHandler.addButtons();
    }

    private void initGameboardHoldingPanel() {
        gridPanel = new LinePanel();
        gridPanel.setPreferredSize(new Dimension(this.gameboardWidth * Config.TILE_SIZE_IN_PIXEL + 1, gameboardHeight * Config.TILE_SIZE_IN_PIXEL));
        gridPanel.setLayout(new GridLayout(this.gameboardWidth, this.gameboardHeight));
        this.add(gridPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new GameBoard(Config.GAMEBOARD_WIDTH, Config.GAMEBOARD_HEIGHT);
    }

    /**
     * Fills the matrix gameBoardStates with buttons, and adds each of them to
     * the gridpanel
     */
    private void createButtons() {
        for (int x = 0; x < gameboardWidth; x++) {
            for (int y = 0; y < gameboardHeight; y++) {
                gameBoardStates[x][y] = new TileButtons(x, y, this);
                gameBoardStates[x][y].setActionCommand(x + "," + y);
                gridPanel.add(gameBoardStates[x][y]);
            }
        }
    }

    public int getGameboardWidth() {
        return this.gameboardWidth;
    }

    public int getGameboardHeight() {
        return this.gameboardHeight;
    }

    public TileButtons getSmartButton(int x, int y) {
        return gameBoardStates[x][y];
    }

    /**
     * Given a list of SmartButtons, will change the background of every tile in
     * the list
     *
     * @param path
     */
    private void paintPath(List<TileButtons> path) {
        if (path.size() > 2) {
            // Starting from second element and stopping one before reaching the
            // last element in order to keep look on start/stop-buttons intact.
            for (TileButtons aPath : path) {
                //Get center point of each element, add them to path in LinePanel
                Rectangle place = aPath.getBounds();
                int x = place.x + place.width / 2;
                int y = place.y + place.height / 2;

                gridPanel.addToPath(new Point(x, y));
            }

            gridPanel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If the reset-button is pressed, the gameboard will be reset, all
        // buttons changed to regular and white, start and stop will be marked as unexisting, and any paths in the gridPanel will be erased.
        if (e.getActionCommand().equals("reset")) {
            hasStartField = false;
            hasTargetField = false;

            stop = null;
            start = null;

            gridPanel.clearPath();

            for (int i = 0; i < gameboardWidth; i++) {
                for (int l = 0; l < gameboardHeight; l++) {
                    gameBoardStates[i][l].turnIntoStandardField();
                }
            }
        } else if (e.getActionCommand().equals("exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("findPath")) {
            for (int i = 0; i < this.gameboardWidth; i++) {
                for (int l = 0; l < this.gameboardHeight; l++) {
                    if (gameBoardStates[i][l].state == TileState.REGULAR) {
                        //gameBoardStates[i][l].setBackground(Color.WHITE);
                    }
                }
            }

            if (hasStartField && hasTargetField) {
                this.findPath();
            }
        }


		/*
         * Accessed if the call is made from a button in the gridView.
		 */
        System.out.println(e.getActionCommand());

        String[] coordinates = e.getActionCommand().split(",");

        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);

        this.toggleState(x, y);
    }

    private void findPath() {
        AStar pathfinder = new AStar(this.gameBoardStates, this.gameboardWidth, this.gameboardHeight);
        List<TileButtons> path = pathfinder.getPath(this.start, this.stop);
        System.out.println("Tile");

        if (path != null) {
            paintPath(path);
        } else {
            System.out.println("No path found ");
        }
    }

    private void toggleState(int x, int y) {
        TileButtons selectedTile = this.gameBoardStates[x][y];
        new SelectedButtonToggleHandler(this, selectedTile);
    }
}