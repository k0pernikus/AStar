import java.util.ArrayList;
import java.util.List;


public class AStar {
	private List <SmartButton> openList;
	private SmartButton currentTile;

	public List<SmartButton> getPath(SmartButton start, SmartButton stop){

		openList = new ArrayList <SmartButton>();
		ArrayList <SmartButton> path = new ArrayList<SmartButton>();
		openList.add(start);
		currentTile = start;
		System.out.println("openListsize: " + openList.size());

		while(true){
			//Loop through the open list to see if we've added the goal-tile.
			int pointer = 0;
			double currentF = currentTile.f;
			for (int i=0;i<openList.size();i++){
				if (openList.get(i).f < currentF){
					pointer=i;
					currentF=openList.get(i).f;
				}
				
				if(openList.get(pointer).equals(stop)){
					path.add(openList.get(i));
					currentTile = path.get(0);
					while(currentTile!=start){
						path.add(currentTile.parent);
						currentTile = currentTile.parent;
					}
					System.out.println(""+ path.size());
					return path;
					
					//The goal was not found in the openList
				}else {
					
					//Adds the currentTile to the close list, and loops through the open list for the SmartButton with the lowest f-value, selecting this as the 
					// new currentTile
					currentF=openList.get(0).f;
					for (i=0;i<openList.size();i++){
						if (currentF >openList.get(i).f && openList.get(i).isOpen()){
							currentF=openList.get(i).f;
							pointer=i;
						}
					}
					currentTile.setClosed(true);
					currentTile.setOpen(false);
					if (openList.size() >1){
						removeFromOpen(currentTile);
					}
					currentTile = openList.get(pointer);
					
					updateNeighbors(currentTile.getNeighbours());
			
				}
			}

		}

	}
	
	/*Given a list of SmartButtons representing all neighbors of some other SmartButton will update each of these neighbors */
	private void updateNeighbors(List <SmartButton> currentNeighbors){
		for (int k=0;k<currentNeighbors.size();k++){

			//if a tile is closed and the current paths g-value would be lower than its old g-value, we update the tiles g-value and sets it parent to 
			// currentTile
			if (currentNeighbors.get(k).isClosed() && currentPathIsShorter(currentNeighbors.get(k))){
				currentNeighbors.get(k).g=(currentTile.isDiagonal(currentNeighbors.get(k)) ? 
						currentTile.g+Math.sqrt(2) : currentTile.g+1);
				currentNeighbors.get(k).parent=currentTile;

				//if a tile is open and the current paths g-value would be lower than its old g-value, we update the tiles g-value and sets it parent to 
				// currentTile
			}else if (currentNeighbors.get(k).isOpen() && currentPathIsShorter(currentNeighbors.get(k))) {
				currentNeighbors.get(k).g=(currentTile.isDiagonal(currentNeighbors.get(k)) ? currentTile.g+Math.sqrt(2) : currentTile.g+1);
				currentNeighbors.get(k).parent=currentTile;

				//if a tile is neither open nor closed, we add it the openList and update the open-value accordingly.
			}else if (!currentNeighbors.get(k).isOpen() && !currentNeighbors.get(k).isClosed()){
				if (currentNeighbors.get(k).state == SmartButtonState.COLLIDABLE){
					currentNeighbors.get(k).setClosed(true);
				}else{
					currentNeighbors.get(k).setOpen(true);
					openList.add(currentNeighbors.get(k));
					currentNeighbors.get(k).parent=currentTile;
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
