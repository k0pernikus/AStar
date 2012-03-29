import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Class for representing a gameboard of Smartbuttons. Containts a AStar-object
 * for finding paths between tiles
 * 
 * @author jesperpersson
 * 
 */

public class GameBoard extends JFrame implements ActionListener {
	private int columns;
	private int rows;
	private boolean startPlaced;
	private boolean stopPlaced;
	private SmartButton stop;
	private SmartButton start;
	JPanel gridPanel;
	private AStar pathfinder;

	SmartButton[][] gameBoardState;

	// Constructor, takes two ints for parameters. Will create a main gridview
	// of smartbuttons, the size set by the parameters rows and columns.
	public GameBoard(int rows, int columns) {
		this.columns = columns;
		this.rows = rows;
		startPlaced = false;
		stopPlaced = false;

		this.setLayout(new BorderLayout());

		// Initiates and specifies the panel holding the gameboard
		gridPanel = new JPanel();
		gridPanel.setPreferredSize(new Dimension(rows * 31, columns * 31));
		gridPanel.setLayout(new GridLayout(rows, columns));
		this.add(gridPanel, BorderLayout.CENTER);

		// Initiates and specifies the controlpanel.
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(rows * 31, 30));
		this.add(controlPanel, BorderLayout.SOUTH);

		// Creates buttons and to be placed in the control panel

		JButton findPath = new JButton("Find Path");
		findPath.setActionCommand("findPath");
		findPath.addActionListener(this);
		controlPanel.add(findPath);

		JButton reset = new JButton("Reset");
		reset.setActionCommand("reset");
		reset.addActionListener(this);
		controlPanel.add(reset);

		JButton exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		controlPanel.add(exit);

		// Initiate matrix for storing buttons
		gameBoardState = new SmartButton[rows][columns];
		createButtons();

		pathfinder = new AStar(gameBoardState, rows, columns);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	
	}

	public static void main(String[] args) {
		new GameBoard(20, 20);
	}

	/**
	 * Fills the matrix gameBoardState with buttons, and adds each of them to
	 * the gridpanel
	 */
	private void createButtons() {
		for (int i = 0; i < rows; i++) {
			for (int l = 0; l < columns; l++) {
				gameBoardState[i][l] = new SmartButton(i, l, this);
				gameBoardState[i][l].setActionCommand(i + "," + l);
				gridPanel.add(gameBoardState[i][l]);
			}
		}
	}

	public int getRows() {
		return this.rows;
	}

	public int getColumns() {
		return this.columns;
	}

	public SmartButton getSmartButton(int row, int column) {
		return gameBoardState[row][column];
	}

	/**
	 * Given a list of SmartButtons, will change the background of every tile in
	 * the list
	 * 
	 * @param path
	 */
	private void paintPath(List<SmartButton> path) {
		for (int i = 0; i < path.size(); i++) {
			System.out.println("Path: " + path.get(i).getRow() + " "
					+ path.get(i).getColumn());

		}
		if (path.size() > 2) {
			// Starting from second element and stopping one before reaching the
			// last element in order to keep look on start/stop-buttons intact.
			for (int i = 1; i < path.size() - 1; i++) {
				path.get(i).setBackground(Color.PINK);
			}
		}
	}
	
	private Point convertPosToTile(Point point){
		 int y = point.y/this.rows;
		 int x= point.x/this.columns;
		return new Point(x,y);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If the reset-button is pressed, the gameboard will be reset, all
		// buttons changed to regular and white.
		if (e.getActionCommand().equals("reset")) {
			startPlaced = false;
			stopPlaced = false;
			stop = null;
			start = null;

			for (int i = 0; i < rows; i++) {
				for (int l = 0; l < columns; l++) {
					gameBoardState[i][l].setText(null);
					gameBoardState[i][l].setBackground(Color.WHITE);
					gameBoardState[i][l].state = TileState.REGULAR;

				}

			}
		}

		else if (e.getActionCommand().equals("exit")) {
			System.exit(0);
		}

		else if (e.getActionCommand().equals("findPath")) {
			for (int i = 0; i < rows; i++) {
				for (int l = 0; l < columns; l++) {
					if (gameBoardState[i][l].state == TileState.REGULAR) {
						gameBoardState[i][l].setBackground(Color.WHITE);
						gameBoardState[i][l].state = TileState.REGULAR;
					}

				}
			}
			if (startPlaced && stopPlaced) {

				List<SmartButton> path = pathfinder.getPath(start, stop);
				// If a path exists
				if (path != null) {
					paintPath(path);
				}
				// If no possible path was found
				else {
					System.out.println("No path found ");
				}
			}
		}

		/*
		 * Accessed if the call is made from a button in the gridView.
		 */
		for (int i = 0; i < rows; i++) {
			for (int l = 0; l < columns; l++) {
				if (e.getActionCommand().equals(i + "," + l)) {
					if (gameBoardState[i][l].state == TileState.REGULAR) {
						gameBoardState[i][l].setBackground(Color.BLACK);
						gameBoardState[i][l].state = TileState.COLLIDABLE;

					} else if (gameBoardState[i][l].state == TileState.COLLIDABLE) {
						if (!startPlaced) {
							gameBoardState[i][l].state = TileState.START;
							gameBoardState[i][l].setBackground(Color.GREEN);
							gameBoardState[i][l].setText("Start");
							startPlaced = true;
							start = gameBoardState[i][l];
						} else {
							gameBoardState[i][l].setBackground(Color.WHITE);
							gameBoardState[i][l].state = TileState.REGULAR;
						}

					} else if (gameBoardState[i][l].state == TileState.START) {
						if (!stopPlaced) {
							gameBoardState[i][l].state = TileState.STOP;
							gameBoardState[i][l].setText("stop");
							gameBoardState[i][l].setBackground(Color.BLUE);
							this.stop = gameBoardState[i][l];
							stopPlaced = true;
							startPlaced = false;
						} else {
							gameBoardState[i][l].setBackground(Color.WHITE);
							gameBoardState[i][l].state = TileState.REGULAR;
							gameBoardState[i][l].setText(null);
							startPlaced = false;
						}

					} else if (gameBoardState[i][l].state == TileState.STOP) {
						gameBoardState[i][l].setText(null);
						gameBoardState[i][l].setBackground(Color.WHITE);
						gameBoardState[i][l].state = TileState.REGULAR;
						stopPlaced = false;
					}
				}
			}
		}
	}
}
