import java.util.ArrayList;
import java.util.List;

/**
 * Class for representing a grid field of tiles, capable of finding paths
 * between tiles in the field.
 * 
 * @author jesperpersson
 * 
 */

public class AStar {
	private List<AStarTile> openList;
	private AStarTile[][] logicList;
	private AStarTile currentTile;
	private SmartButton[][] gameBoard;

	private int columns;
	private int rows;

	public AStar(SmartButton[][] GameBoard, int rows, int columns) {
		this.gameBoard = GameBoard;
		this.columns = columns;
		this.rows = rows;
		logicList = new AStarTile[rows][columns];
		for (int i = 0; i < GameBoard.length; i++) {
			for (int l = 0; l < GameBoard[i].length; l++) {
				logicList[i][l] = new AStarTile(GameBoard[i][l]);
			}
		}

		init();
	}

	private void init() {
		generateNeighbors();
	}

	/**
	 * Main algorithm. Given a start and a goal, will return the shortest path
	 * between the two. If no path is to be found, the return will be a
	 * null-object
	 * 
	 * @param start
	 * @return SmartButton [][] Shortest path between Start and Stop.
	 * @return null if no path exists between Start and Stop
	 */
	public List<SmartButton> getPath(SmartButton startPoint,
			SmartButton endPoint) {
		AStarTile start = logicList[startPoint.getRow()][startPoint.getColumn()];
		AStarTile stop = logicList[endPoint.getRow()][endPoint.getColumn()];
		for (int i = 0; i < rows; i++) {
			for (int l = 0; l < columns; l++) {
				logicList[i][l].setH(stop);
				logicList[i][l]
						.setSolid(gameBoard[i][l].state == TileState.COLLIDABLE);
			}
		}
		openList = new ArrayList<AStarTile>();
		ArrayList<AStarTile> path = new ArrayList<AStarTile>();
		openList.add(start);

		while (!openList.isEmpty()) {
			int pointer = findBestTile();
			currentTile = openList.get(pointer);

			// Algorithm complete and path found.
			if (currentTile.equals(stop)) {
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
	private void updateNeighbors(List<AStarTile> currentNeighbors) {
		for (int k = 0; k < currentNeighbors.size(); k++) {

			// if a tile is closed and the current paths g-value would be lower
			// than its old g-value, we update the tiles g-value and sets it
			// parent to
			// currentTile
			if (currentNeighbors.get(k).isClosed()
					&& currentPathIsShorter(currentNeighbors.get(k))) {
				currentNeighbors
						.get(k)
						.setG(currentTile.isDiagonal(currentNeighbors.get(k)) ? currentTile
								.getG() + 1.4 : currentTile.getG() + 1);
				currentNeighbors.get(k).setParent(currentTile);

				// if a tile is open and the current paths g-value would be
				// lower than its old g-value, we update the tiles g-value and
				// sets it parent to
				// currentTile
			} else if (currentNeighbors.get(k).isOpen()
					&& currentPathIsShorter(currentNeighbors.get(k))) {
				currentNeighbors
						.get(k)
						.setG(currentTile.isDiagonal(currentNeighbors.get(k)) ? currentTile
								.getG() + 1.4 : currentTile.getG() + 1);
				currentNeighbors.get(k).setParent(currentTile);

				// if a tile is neither open nor closed, we add it the openList
				// and update the open-value accordingly.
			} else if (!currentNeighbors.get(k).isOpen()
					&& !currentNeighbors.get(k).isClosed()) {
				if (currentNeighbors.get(k).isSolid()) {
					currentNeighbors.get(k).setClosed(true);
				} else {
					currentNeighbors.get(k).setOpen(true);
					openList.add(currentNeighbors.get(k));
					currentNeighbors.get(k).setParent(currentTile);
					currentNeighbors.get(k)
							.setG(currentTile.isDiagonal(currentNeighbors
									.get(k)) ? currentTile.getG() + 1.4
									: currentTile.getG() + 1);
				}
			}
		}
	}

	// Given a SmartButton, will return whether or not the current path from the
	// start to the button is shorter than the currently recorded.
	private boolean currentPathIsShorter(AStarTile tile) {
		return tile.getG() > (currentTile.isDiagonal(tile) ? currentTile.getG() + 1.4
				: currentTile.getG() + 1);
	}

	// Loops through gameboard and make sure every button calculates its
	// neighbors
	private void generateNeighbors() {
		for (int i = 0; i < logicList.length; i++) {
			for (int l = 0; l < logicList[i].length; l++) {
				calculateNeighbors(logicList[i][l]);
			}
		}
	}

	// Tells the specified button to calculate and add its neighbors.
	private void calculateNeighbors(AStarTile tile) {
		int top = tile.getColumn() + 1;
		int buttom = tile.getColumn() - 1;
		int right = tile.getRow() + 1;
		int left = tile.getRow() - 1;

		if (top < columns) {
			tile.addNeighbor(logicList[tile.getRow()][top]);
			if (right < rows) {
				tile.addNeighbor(logicList[right][top]);
			}
			if (left >= 0) {
				tile.addNeighbor(logicList[left][top]);
			}
		}
		if (buttom >= 0) {
			tile.addNeighbor(logicList[tile.getRow()][buttom]);
			if (right < rows) {
				tile.addNeighbor(logicList[right][buttom]);
			}
			if (left >= 0) {
				tile.addNeighbor(logicList[left][buttom]);
			}
		}
		if (left >= 0) {
			tile.addNeighbor(logicList[left][tile.getColumn()]);
		}
		if (right < rows) {
			tile.addNeighbor(logicList[right][tile.getColumn()]);
		}
	}

	// Removes a SmartButton from the open list
	private void removeFromOpen(AStarTile tile) {
		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i) == tile) {
				openList.remove(i);
			}
		}
	}

	public void clear() {
		for (int i=0;i<logicList.length;i++){
			for (int l=0;l<logicList[i].length;l++){
				logicList[i][l].setOpen(false);
				logicList[i][l].setClosed(false);
			}
		}
		clearOpen();
	}

	private void clearOpen() {
		for (int i = 0; i < openList.size(); i++) {
			openList.remove(i);
		}
		System.out.println("Size: " + openList.size());
	}

	private List<SmartButton> convertList(List<AStarTile> path) {
		List<SmartButton> returnList = new ArrayList<SmartButton>();
		for (int i = 0; i < path.size(); i++) {
			returnList.add(gameBoard[path.get(i).getRow()][path.get(i)
					.getColumn()]);
		}
		return returnList;
	}

}
