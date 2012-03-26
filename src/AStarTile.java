import java.util.ArrayList;
import java.util.List;

/*Class for representing a tile in the AStar-algorithm*/
public class AStarTile {

	private double g;
	private double f;
	private double h;
	private boolean closed;
	private boolean open;

	private boolean solid;
	private int row;
	private int column;
	
	private AStarTile parent;
	private List <AStarTile> neighbors;

	public AStarTile(SmartButton toRepresent){
		this.row=toRepresent.getRow();
		this.column=toRepresent.getColumn();
		open=false;
		closed=false;
		this.neighbors=new ArrayList <AStarTile>();
		
		if(toRepresent.state==TileState.COLLIDABLE){
			solid=true;
		}
		else{
			solid=false;
		}

	}


	public int getRow(){
		return this.row;
	}
	
	public int getColumn(){
		return this.column;
	}

	public double getH(){
		return this.h;
	}

	public double getF(){
		return this.h;
	}

	public double getG(){
		return this.g;
	}
	
	public AStarTile getParent(){
		return this.parent;
	}
	public boolean isOpen(){
		return this.open;
	}
	
	public boolean isClosed(){
		return this.closed;
	}
	
	public boolean isSolid(){
		return this.solid;
	}
	
	public void setOpen(boolean value){
		this.open=value;
	}
	
	public void setClosed(boolean value){
		this.closed=value;
	}
	
	public void setParent(AStarTile parent){
		this.parent=parent;
	}

	//Since the h-value is unchanging, this method will also change the value of f (g+h)
	public void setG(double g){
		if (this.h >0){
			this.g = g;
			setF();
		}
	}

	public void setH(AStarTile stop){
		this.h = (Math.abs(this.row-stop.getRow()) + Math.abs(this.column-stop.getColumn()));
	}

	public void setF(){
		this.f=this.h+this.g;
	}
	
	public void setSolid(boolean value){
		this.solid=value;
	}


	public List<AStarTile> getNeighbors(){
		return neighbors;
	}

	public void addNeighbor(AStarTile tile){
		neighbors.add(tile);
	}

	public boolean isDiagonal(AStarTile tile){
		return (this.getRow() != tile.getRow() && this.getColumn() != tile.getColumn());
	}





}