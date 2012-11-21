import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class LineDrawer extends JPanel {

    private List<Point> path;
    private BasicStroke stroke;

    public LineDrawer() {
        this.path = new ArrayList<Point>();
        this.stroke = new BasicStroke(Config.TILE_SIZE_IN_PIXEL / 3, BasicStroke.JOIN_BEVEL, BasicStroke.JOIN_BEVEL);
    }

    public void paint(Graphics g) {
        float alpha = (float) 0.1;
        Color transparentBlue = new Color(0, 0, 1, alpha);
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g2);

        if (path.size() >= 2) {
            g2.setStroke(stroke);
            g2.setPaint(transparentBlue);
            g2.setPaintMode();

            for (int i = 0; i < path.size() - 1; i++) {
                g2.drawLine(path.get(i).x, path.get(i).y, path.get(i + 1).x, path.get(i + 1).y);
            }
        }
    }

    public void addToPath(Point point) {
        this.path.add(point);
    }
}
