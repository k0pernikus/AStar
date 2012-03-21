import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.text.Position;



/*Class for representing a gameboard of Smartbuttons.*/

public class GameBoard extends JFrame implements ActionListener{
	private int columns;
	private int rows;
	private boolean startPlaced;
	private boolean stopPlaced;
	private SmartButton start;
	private SmartButton stop;
	JPanel gridPanel;

	SmartButton [][] gameBoardState;

	//Constructor, takes two ints for parameters. Will create a main gridview of smartbuttons, the size set by the parameters rows and columns. 
	public GameBoard(int rows, int columns){
		this.columns = columns;
		this.rows=rows;
		startPlaced=false;
		stopPlaced=false;

		this.setLayout(new BorderLayout());

		//Initiates and specifies the panel holding the gameboard
		gridPanel = new JPanel();
		gridPanel.setPreferredSize(new Dimension(rows*31,columns*31));
		gridPanel.setLayout(new GridLayout(rows,columns));
		this.add(gridPanel,BorderLayout.CENTER);

		//Initiates and specifies the controlpanel.
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(rows*31,30));
		this.add(controlPanel,BorderLayout.SOUTH);

		//Creates buttons and to be placed in the control panel

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

		//Initiate matrix for storing buttons
		gameBoardState = new SmartButton[rows][columns];
		createButtons();


		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
		generateNeighbours();
	}


	//Loops through gameboard and make sure every button calculates its neighbours
	private void generateNeighbours() {
		for (int i=0;i< rows;i++){
			for(int l=0;l<columns;l++){
				calculateNeighbours(gameBoardState[i][l]);
			}
		}
	}

	//Initiate buttons and adds them to gameboard
	private void createButtons(){
		for (int i=0;i< rows;i++){
			for(int l=0;l<columns;l++){
				gameBoardState[i][l]=new SmartButton(i,l,this);
				gameBoardState[i][l].setActionCommand(i+","+l);
				gridPanel.add(gameBoardState[i][l]);
			}
		}
	}



	//Tells the specified button to calculate and add its neighbours.
	private void calculateNeighbours(SmartButton button){
		int top = button.getColumn()+1;
		int buttom = button.getColumn()-1;
		int right = button.getRow()+1;
		int left = button.getRow()-1;

		if (top<columns){
			button.addNeighbour(gameBoardState[button.getRow()][top]);
			if (right <rows){
				button.addNeighbour(gameBoardState[right][top]);
			}if (left >=0){
				button.addNeighbour(gameBoardState[left][top]);
			}
		}if (buttom >=0){
			button.addNeighbour(gameBoardState[button.getRow()][buttom]);
			if (right <rows){
				button.addNeighbour(gameBoardState[right][buttom]);
			}if(left >=0){
				button.addNeighbour(gameBoardState[left][buttom]);	
			}	
		}
		if (left >=0){
			button.addNeighbour(gameBoardState[left][button.getColumn()]);
		}if (right <rows){
			button.addNeighbour(gameBoardState[right][button.getColumn()]);
		}
	}

	public int getRows(){
		return this.rows;
	}

	public int getColumns(){
		return this.columns;
	}

	public SmartButton getSmartButton(int row, int column){
		return gameBoardState[row][column];
	}




	public static void main (String [] args){
		new GameBoard(20,20);
	}




	@Override
	public void actionPerformed(ActionEvent e) {
		//If the reset-button is pressed, the gameboard will be reset, all buttons changed to regular and white.
		if (e.getActionCommand().equals("reset")){
			for (int i=0;i<rows;i++){
				for(int l=0;l<columns;l++){
					gameBoardState[i][l].setText(null);
					gameBoardState[i][l].setBackground(Color.WHITE);
					gameBoardState[i][l].state = SmartButtonState.REGULAR;
					gameBoardState[i][l].parent=null;
					startPlaced=false;
					stopPlaced=false;
					start=null;
					stop=null;
				}
			}
		}

		else if (e.getActionCommand().equals("exit")){
			System.exit(0);
		}

		else if(e.getActionCommand().equals("findPath")){
			if (startPlaced && stopPlaced){
				for (int i=0;i<rows;i++){
					for(int l=0;l<columns;l++){
						gameBoardState[i][l].setH(start);
					}
				}

				AStar pathfinder = new AStar();
				List<SmartButton> path =pathfinder.getPath(start, stop);
				for (int i = 0;i<path.size();i++){
					System.out.println("Path: "+path.get(i).getRow() +" " + path.get(i).getColumn());
				}
				if (path.size() > 2){
					for (int i=1;i<path.size()-1;i++){
						path.get(i).setBackground(Color.PINK);
					}
				}
			}
		}

		/*Loops through entire gameboard of buttons until a button with a actioncommand matching the given actioncommand from e.
		 * when the button is found, the color and state (and possible the text) of the button is changed depending on its SmartButtonState*/
		for (int i=0;i<rows;i++){
			for(int l=0;l<columns;l++){
				if (e.getActionCommand().equals(i+","+l)){

					if (gameBoardState[i][l].state==SmartButtonState.REGULAR){
						gameBoardState[i][l].setBackground(Color.BLACK);
						gameBoardState[i][l].state = SmartButtonState.COLLIDABLE;

					}else if (gameBoardState[i][l].state == SmartButtonState.COLLIDABLE) {
						gameBoardState[i][l].state = SmartButtonState.START;
						gameBoardState[i][l].setBackground(Color.GREEN);
						gameBoardState[i][l].setText("Start");
						if (startPlaced){
							//							this.start.state = SmartButtonState.REGULAR;
							//							this.start.setBackground(Color.WHITE);
							//							this.start.setText(null);
						}
						startPlaced=true;
						this.start = gameBoardState[i][l];

					}else if (gameBoardState[i][l].state == SmartButtonState.START){
						gameBoardState[i][l].state = SmartButtonState.STOP;
						gameBoardState[i][l].setText("stop");
						gameBoardState[i][l].setBackground(Color.BLUE);
						if (stopPlaced){
							//							this.stop.state = SmartButtonState.REGULAR;
							//							this.stop.setBackground(Color.WHITE);
							//							this.stop.setText(null);
						}
						this.stop = gameBoardState[i][l];
						stopPlaced=true;

					}else if (gameBoardState[i][l].state == SmartButtonState.STOP){
						gameBoardState[i][l].setText(null);
						gameBoardState[i][l].setBackground(Color.WHITE);
						gameBoardState[i][l].state = SmartButtonState.REGULAR;
					}
				}
			}
		}
	}
}
