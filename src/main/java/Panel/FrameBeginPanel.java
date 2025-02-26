package Panel;

import Data.AppConstants;
import Frame.FrameBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameBeginPanel extends JPanel {
    public FrameBeginPanel() {

        //JPanel 구조
        setBackground(new Color(255,241,54));
        setLayout(null);
        setSize(831, 138);

        FrameBase.getInstance(this);

//        //하단 버튼
//        JButton btnInfo = new JButton("영화 예매");
//        JButton btnPost = new JButton("영화 평가하기");
//
//        btnInfo.setBackground(new Color(183, 240, 117));
//        btnPost.setBackground(new Color(183, 240, 117));
//
//        btnInfo.setSize(300,150);
//        btnInfo.setLocation(((int)getSize().getWidth()/2) - 310,
//                (int)getLocation().getY()/2 + 610);
//
//        btnInfo.setFont(new Font("굴림", Font.BOLD, 26));
//
//        btnPost.setSize(300,150);
//        btnPost.setLocation((int)btnInfo.getLocation().getX()+300,
//                (int)btnInfo.getLocation().getY());
//
//        btnPost.setFont(new Font("굴림", Font.BOLD, 26));
//
//        add(btnInfo);
//        add(btnPost);
//
//
//        //버튼 이벤트
//        btnInfo.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
    }
}


