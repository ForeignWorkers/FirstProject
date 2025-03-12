package Helper;

import javax.swing.*;
import java.awt.*;

public class SmoothLabel extends JLabel {

    public SmoothLabel(String text) {
        super(text);
        setOpaque(false); // 배경 투명
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create(); // 복사본
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        super.paintComponent(g2);
        g2.dispose(); // 메모리 누수 방지
    }
}
