package Start;

import Frame.FrameBase;

import javax.swing.*;
import java.awt.*;

public class StartMain {
    public static void main(String[] args) {
        // FrameBase 싱글턴 생성
        FrameBase frame = FrameBase.getInstance();

        // 🔥 새로운 내부 패널 1 (upPanel 내부에 추가)
        JPanel newUpPanel = new JPanel();
        newUpPanel.setBackground(Color.YELLOW);
        newUpPanel.add(new JLabel("새로운 상단 패널"));

        // 🔥 새로운 내부 패널 2 (midPanel 내부에 추가)
        JPanel newMidPanel = new JPanel();
        newMidPanel.setBackground(Color.CYAN);
        newMidPanel.add(new JLabel("새로운 중앙 패널"));

        // 🔥 새로운 내부 패널 3 (downPanel 내부에 추가)
        JPanel newDownPanel = new JPanel();
        newDownPanel.setBackground(Color.MAGENTA);
        newDownPanel.add(new JLabel("새로운 하단 패널"));

        // 특정 위치에 새로운 내부 패널 추가
        frame.setInnerPanel(newUpPanel, "up");
        frame.setInnerPanel(newMidPanel, "mid");
        frame.setInnerPanel(newDownPanel, "down");
    }
}