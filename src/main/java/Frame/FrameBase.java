package Frame;

import javax.swing.*;

import Data.AppConstants;
import Managers.DBDataManagers;
import Managers.DataManagers;
import Panel.MainStaticPanel;

import java.awt.*;
import java.io.IOException;

public class FrameBase extends JFrame {
    private static FrameBase instance;
    private static MainStaticPanel mainPanel;

    private FrameBase() throws IOException {
    	System.out.println("DB 초기화 실시");
    	DBDataManagers.getInstance().InitDBDataManagers();
    	
        setTitle("FrameBase");
        setSize(AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT));

        // 배경 이미지 로드
        ImageIcon backgroundIcon = DataManagers.getInstance().getIcon("mainFrame", "panel_MID");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(
                AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(backgroundImage);

        // 배경 JLabel 추가 (Z-Index: 1)
        JLabel label = new JLabel(resizedIcon);
        label.setOpaque(false);
        label.setBackground(new Color(0, 0, 0, 0));
        label.setBounds(0, 0, AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT);
        layeredPane.add(label, Integer.valueOf(1)); // 낮은 숫자가 뒤쪽에 위치함

        // 메인 패널 추가 (Z-Index: 2)
        mainPanel = new MainStaticPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBounds(0, 0, AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT);
        layeredPane.add(mainPanel, Integer.valueOf(2)); // 높은 숫자가 앞쪽에 위치함

        layeredPane.setOpaque(false);
        layeredPane.setBackground(new Color(0, 0, 0, 0));

        setContentPane(layeredPane);
        setVisible(true);
    }

    public static FrameBase getInstance() throws IOException {
        if (instance == null) {
            instance = new FrameBase();
        }
        return instance;
    }

    // 특정 위치의 내부 패널을 변경하는 메서드
    public void setInnerPanel(JComponent panel, String position) {
        mainPanel.setInnerPanel(panel, position);
    }
}