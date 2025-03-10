package Panel;

import Data.AppConstants;
import Managers.DataManagers;
import Component.CustomButton;

import javax.swing.*;
import java.awt.*;

public class MainStaticPanel extends JPanel {
    private JPanel upPanel;
    private JPanel midPanel;
    private JPanel downPanel;

    public MainStaticPanel() {
        setLayout(null); // 전체 레이아웃 설정

        // 🔴 상단 패널 (고정)
        upPanel = new JPanel();
        upPanel.setLayout(null);
        upPanel.setOpaque(false);
        upPanel.setSize(AppConstants.PANEL_UP_WIDTH,AppConstants.PANEL_UP_HEIGHT);
        upPanel.setLocation(0,0);

        // 🔵 중앙 패널 (고정)
        midPanel = new JPanel();
        midPanel.setLayout(null);
        midPanel.setOpaque(false);
        midPanel.setSize(AppConstants.PANEL_MID_WIDTH,AppConstants.PANEL_MID_HEIGHT);
        midPanel.setLocation(0,83);

        // 🟢 하단 패널 (고정)
        downPanel = new JPanel();
        downPanel.setLayout(null);
        downPanel.setOpaque(false);
        downPanel.setSize(AppConstants.PANEL_DOWN_WIDTH,AppConstants.PANEL_DOWN_HEIGHT);
        downPanel.setLocation(0,632);

        add(upPanel);
        add(midPanel);
        add(downPanel);
    }

    // 특정 위치의 내부 패널을 교체하는 메서드 - TO DO : position -> enum으로 변경
    public void setInnerPanel(JComponent panel, String position) {
        switch (position.toLowerCase()) {
            case "up":
                System.out.println("여기도 탐!");
                upPanel.removeAll();
                upPanel.add(panel);
                break;
            case "mid":
                midPanel.removeAll();
                midPanel.add(panel);
                break;
            case "down":
                downPanel.removeAll();
                downPanel.add(panel);
                break;
            default:
                throw new IllegalArgumentException("Position must be 'up', 'mid', or 'down'");
        }

        // 🔥 UI 업데이트
        updateMainPanel();
    }


    //업데이트
    public void updateMainPanel(){
        revalidate();
        repaint();
    }
}
