import java.util.ArrayList;
import java.util.List;


public class AStar {
	private List <SmartButton> openList;
	private SmartButton currentTile;

	
	/*Main algorithm. Given a start and a goal, will return the shortest path between the two.*/
	public List<SmartButton> getPath(SmartButton start, SmartButton stop){
		openList = new ArrayList <SmartButton>();
		ArrayList <SmartButton> path = new ArrayList<SmartButton>();
		openList.add(start);
		
		while(true){
			int pointer = findBestTile();
			currentTile=openList.get(pointer);
				if(currentTile.equals(stop)){
					path.add(currentTile);
					while(currentTile!=start){
						path.add(currentTile.parent);
						currentTile = currentTile.parent;
					}
					return path;
					
					//The goal was not found in the openList
				}else {
					updateNeighbors(currentTile.getNeighbours());
					currentTile.setClosed(true);
					currentTile.setOpen(false);
					if (openList.size() >1){
						removeFromOpen(currentTile);
					}
					currentTile = openList.get(pointer);
			
				}
			}

		}

	/*Returns the index of the tile in the open list with the lowest f-value*/
	private int findBestTile(){
		int pointer = 0;
		double currentF = openList.get(0).f;
		for (int i=0;i<openList.size();i++){
			if (openList.get(i).f < currentF){
				pointer=i;
				currentF=openList.get(i).f;
			}
		}
		return pointer;
	}
	
	/*Loop through the given list, and updates depending on its relation to its adjacent tile, the one currently selected
	 * Should a tile be in a closed or open state, the length of the path between it and the start
	 * will be compared to what it would have been had we measured the current path to it instead. Should the tile be neither closed nor open, 
	 * the path to it will be updated, and it will be added to the open list for consideration
	 * */
	private void updateNeighbors(List <SmartButton> currentNeighbors){
		for (int k=0;k<currentNeighbors.size();k++){

			//if a tile is closed and the current paths g-value would be lower than its old g-value, we update the tiles g-value and sets it parent to 
			// currentTile
			if (currentNeighbors.get(k).isClosed() && currentPathIsShorter(currentNeighbors.get(k))){
				currentNeighbors.get(k).g=(currentTile.isDiagonal(currentNeighbors.get(k)) ? 
						currentTile.g+1.4 : currentTile.g+1);
				currentNeighbors.get(k).parent=currentTile;

				//if a tile is open and the current paths g-value would be lower than its old g-value, we update the tiles g-value and sets it parent to 
				// currentTile
			}else if (currentNeighbors.get(k).isOpen() && currentPathIsShorter(currentNeighbors.get(k))) {
				currentNeighbors.get(k).g=(currentTile.isDiagonal(currentNeighbors.get(k)) ? currentTile.g+1.4: currentTile.g+1);
				currentNeighbors.get(k).parent=currentTile;

				//if a tile is neither open nor closed, we add it the openList and update the open-value accordingly.
			}else if (!currentNeighbors.get(k).isOpen() && !currentNeighbors.get(k).isClosed()){
				if (currentNeighbors.get(k).state == SmartButtonState.COLLIDABLE){
					currentNeighbors.get(k).setClosed(true);
				}else{
					currentNeighbors.get(k).setOpen(true);
					openList.add(currentNeighbors.get(k));
					currentNeighbors.get(k).parent=currentTile;
					currentNeighbors.get(k).g=(currentTile.isDiagonal(currentNeighbors.get(k)) ? currentTile.g+1.4: currentTile.g+1);
				}
			}
		}
	}
	
	//Given a SmartButton, will return whether or not the current path from the start to the button is shorter than the currently recorded.
	private boolean currentPathIsShorter(SmartButton button){
		return button.g > (currentTile.isDiagonal(button) ? currentTile.g+Math.sqrt(2) : currentTile.g+1);
	}

	//Removes a SmartButton from the open list
	private void removeFromOpen(SmartButton button){
		for (int i=0;i<openList.size();i++){
			if (openList.get(i)==button){
				openList.remove(i);
			}
		}
	}

}
