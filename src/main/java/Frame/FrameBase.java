package Frame;

import javax.swing.*;

import Data.AppConstants;
import Panel.MainStaticPanel;

public class FrameBase extends JFrame {
    private static FrameBase instance;
    private static MainStaticPanel mainPanel;

    private FrameBase() {
        setTitle("FrameBase");
        setSize(AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // MainPanel 생성
        mainPanel = new MainStaticPanel();
        setContentPane(mainPanel);

        setVisible(true);
    }

    public static FrameBase getInstance() {
        if (instance == null) {
            instance = new FrameBase();
        }
        return instance;
    }

    // 특정 위치의 내부 패널을 변경하는 메서드
    public void setInnerPanel(JPanel panel, String position) {
        mainPanel.setInnerPanel(panel, position);
    }
}