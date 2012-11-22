import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author k0pernikus
 */

public class ButtonHandler {
    private GameBoard gameBoard;
    private JPanel controlPanel;

    public ButtonHandler(GameBoard gameBoard, JPanel controlPanel) {
        this.gameBoard = gameBoard;
        this.controlPanel = controlPanel;
    }

    public void addButtons() {
        JButton findPath = new JButton("Find Path");
        findPath.setActionCommand("findPath");
        findPath.addActionListener(this.gameBoard);
        this.controlPanel.add(findPath);

        JButton reset = new JButton("Reset");
        reset.setActionCommand("reset");
        reset.addActionListener(this.gameBoard);
        this.controlPanel.add(reset);

        JButton exit = new JButton("Exit");
        exit.setActionCommand("exit");
        exit.addActionListener(this.gameBoard);
        this.controlPanel.add(exit);
    }
}
