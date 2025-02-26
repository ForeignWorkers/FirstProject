package Panel;

import Data.AppConstants;

import javax.swing.*;
import java.awt.*;

public class MainStaticPanel extends JPanel {
    private JPanel upPanel, upContentPanel;
    private JPanel midPanel, midContentPanel;
    private JPanel downPanel, downContentPanel;

    public MainStaticPanel() {
        setLayout(new BorderLayout()); // 전체 레이아웃 설정

        // 🔴 상단 패널 (고정)
        upPanel = new JPanel(new BorderLayout());
        upPanel.setPreferredSize(new Dimension(AppConstants.PANEL_UP_WIDTH, AppConstants.PANEL_UP_HEIGHT));
        upPanel.setBackground(Color.RED);
        upContentPanel = new JPanel(new BorderLayout()); // 내부 변경 가능 패널
        upPanel.add(upContentPanel, BorderLayout.CENTER);

        // 🔵 중앙 패널 (고정)
        midPanel = new JPanel(new BorderLayout());
        midPanel.setPreferredSize(new Dimension(AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT));
        midPanel.setBackground(Color.BLUE);
        midContentPanel = new JPanel(new BorderLayout()); // 내부 변경 가능 패널
        midPanel.add(midContentPanel, BorderLayout.CENTER);

        // 🟢 하단 패널 (고정)
        downPanel = new JPanel(new BorderLayout());
        downPanel.setPreferredSize(new Dimension(AppConstants.PANEL_DOWN_WIDTH, AppConstants.PANEL_DOWN_HEIGHT));
        downPanel.setBackground(Color.GREEN);
        downContentPanel = new JPanel(new BorderLayout()); // 내부 변경 가능 패널
        downPanel.add(downContentPanel, BorderLayout.CENTER);

        // 고정 패널 추가
        add(upPanel, BorderLayout.NORTH);
        add(midPanel, BorderLayout.CENTER);
        add(downPanel, BorderLayout.SOUTH);
    }

    // 특정 위치의 내부 패널을 교체하는 메서드
    public void setInnerPanel(JPanel panel, String position) {
        switch (position.toLowerCase()) {
            case "up":
                upContentPanel.removeAll();
                upContentPanel.add(panel, BorderLayout.CENTER);
                break;
            case "mid":
                midContentPanel.removeAll();
                midContentPanel.add(panel, BorderLayout.CENTER);
                break;
            case "down":
                downContentPanel.removeAll();
                downContentPanel.add(panel, BorderLayout.CENTER);
                break;
            default:
                throw new IllegalArgumentException("Position must be 'up', 'mid', or 'down'");
        }

        // 업데이트
        revalidate();
        repaint();
    }
}
