import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class LinePanel extends JPanel {

    private List<Point> path;

    public LinePanel() {
        this.path = new ArrayList<Point>();
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Stroke stroke = new BasicStroke(Config.TILE_SIZE_IN_PIXEL/3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        super.paint(g2);

        if (path.size() >= 2) {
            g2.setStroke(stroke);
            g2.setPaint(Color.BLUE);
            for (int i = 0; i < path.size() - 1; i++) {
                g2.drawLine(path.get(i).x, path.get(i).y, path.get(i + 1).x, path.get(i + 1).y);
            }
        }
    }

    public void addToPath(Point point) {
        this.path.add(point);
    }

    public void clearPath() {
        this.path.clear();
    }
}