package Start;

import Frame.FrameBase;

import javax.swing.*;
import java.awt.*;

public class StartMain {
    public static void main(String[] args) {
        // FrameBase 생성
        FrameBase frameBase = FrameBase.getInstance();

        // 3초 후 새로운 패널로 교체하는 테스트
        new Timer(3000, e -> {
            System.out.println("3초 뒤에 upPanel에 새로운 컨텐츠 패널이 들어감!");

            //새로운 패널 만들어주기 -> 님들은 이제 이게 각자 만든 클래스 임 ㅋ
            JPanel newUpPanel = new JPanel();
            newUpPanel.setLayout(null);
            newUpPanel.setBackground(Color.YELLOW);
            newUpPanel.setSize(300, 400);
            newUpPanel.setLocation(0, 0); // upPanel 내부에서 위치 고정
            newUpPanel.setVisible(true);
            JButton btn = new JButton("로그인");
            btn.setBounds(0, 0, 100, 50);
            newUpPanel.add(btn);

            //** 패널 교체 핵심 메서드 **
            frameBase.setInnerPanel(newUpPanel, "up");
        }).start();
    }
}