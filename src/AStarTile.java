
public class AStarTile {
	
	private double g;
	private double f;
	private double h;
	private boolean open;
	private boolean closed;
	private TileState state;
	private int row;
	private int column;
	
	public AStarTile(SmartButton toRepresent, SmartButton start){
		this.row=toRepresent.getRow();
		this.column=toRepresent.getColumn();
		h = (Math.abs(this.row-start.getRow()) + Math.abs(this.column-start.getColumn()));
		open=false;
		closed=false;
	}
	
	//Since the h-value is unchanging, this method will also change the value of f (g+h)
		public void setG(double g){
			if (this.h >0){
			this.g = g;
			this.f=g+h;
			}
		}
		
		public void setClosed(boolean value){
			this.closed=value;
		}
		
		public void setOpen(boolean value){
			this.open=value;
		}

		public boolean isClosed(){
			return this.closed;
		}
		
		public boolean isOpen(){
			return this.open;
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

	
		
	
	
}
