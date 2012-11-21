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
        if (selectedButton.getState() == TileState.REGULAR) {
            selectedButton.turnIntoWall();
        } else if (selectedButton.isWall()) {
            if (!gameBoard.hasStartField) {
                selectedButton.turnIntoStartField();
                gameBoard.hasStartField = true;
                gameBoard.start = selectedButton;
            } else if (!gameBoard.hasTargetField) {
                selectedButton.turnIntoEndField();
                gameBoard.hasTargetField = true;
                gameBoard.target = selectedButton;
            } else {
                selectedButton.turnIntoStandardField();
            }
        } else {
            if (selectedButton.getState() == TileState.START) {
                if (!gameBoard.hasTargetField) {
                    selectedButton.turnIntoEndField();

                    gameBoard.target = selectedButton;
                    gameBoard.hasTargetField = true;
                    gameBoard.hasStartField = false;
                } else {
                    selectedButton.turnIntoStandardField();
                    gameBoard.hasStartField = false;
                }
            } else if (selectedButton.getState() == TileState.TARGET) {
                selectedButton.turnIntoStandardField();
                gameBoard.hasTargetField = false;
            }
        }
    }
}
