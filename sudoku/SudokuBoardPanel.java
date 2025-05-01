package sudoku;

import javax.swing.*;
import java.awt.*;

public class SudokuBoardPanel extends JPanel {
    public SudokuBoardPanel() {
        setLayout(new GridLayout(9, 9));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int cellWidth = width / 9;
        int cellHeight = height / 9;

        // 1. خطوط الخلايا العادية (رمادية وخفيفة)
        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(1));
        for (int i = 1; i < 9; i++) {
            g2.drawLine(i * cellWidth, 0, i * cellWidth, height);  // عمودي
            g2.drawLine(0, i * cellHeight, width, i * cellHeight); // أفقي
        }

        // 2. خطوط عريضة بين كل مربعات 3x3 (ذهبية أو سوداء حسب طلبك)
        g2.setColor(new Color(190, 160, 100)); // لون بيج ذهبي مثل الصورة
        g2.setStroke(new BasicStroke(4)); // سمك الخط

        for (int i = 0; i <= 9; i += 3) {
            g2.drawLine(i * cellWidth, 0, i * cellWidth, height);     // عمودي كل 3
            g2.drawLine(0, i * cellHeight, width, i * cellHeight);    // أفقي كل 3
        }
    }
}
