/**
 * @author k0pernikus
 */
public class SelectedButtonToggleHandler {
    private GameBoard gameBoard;
    private TileButton selectedButton;
    
    public SelectedButtonToggleHandler(GameBoard gameBoard, TileButton selectedButton) {
        this.gameBoard = gameBoard;
        this.selectedButton = selectedButton;
        this.handleClick();
    }
    
    private void handleClick() {
        if (selectedButton.state == TileState.REGULAR) {
            selectedButton.turnIntoWall();
        } else if (selectedButton.state == TileState.COLLIDABLE) {
            if (!gameBoard.hasStartField) {
                selectedButton.turnIntoStartField();
                gameBoard.hasStartField = true;
                gameBoard.start = selectedButton;
            } else if (!gameBoard.hasTargetField) {
                selectedButton.turnIntoEndField();
                gameBoard.hasTargetField = true;
                gameBoard.stop = selectedButton;
            } else {
                selectedButton.turnIntoStandardField();
            }
        } else {
            if (selectedButton.state == TileState.START) {
                if (!gameBoard.hasTargetField) {
                    selectedButton.turnIntoEndField();

                    gameBoard.stop = selectedButton;
                    gameBoard.hasTargetField = true;
                    gameBoard.hasStartField = false;
                } else {
                    selectedButton.turnIntoStandardField();
                    gameBoard.hasStartField = false;
                }
            } else if (selectedButton.state == TileState.STOP) {
                selectedButton.turnIntoStandardField();
                gameBoard.hasTargetField = false;
            }
        }
    }      
}
