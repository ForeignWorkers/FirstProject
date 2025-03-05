package Start;

import Component.CustomButton;
import Data.AppConstants;
import Frame.FrameBase;
import Managers.DataManagers;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class StartMain {
    public static void main(String[] args) {
        // FrameBase 생성
        FrameBase frameBase = FrameBase.getInstance();

        //Start Panel (재연님 추가해주세요.)
        //Main Panel Load!

        //---------------------------------
        //Test 및 사용법 숙지를 위한 테스트 코드임!
        //지울 예정임!
        JPanel testPanel = new JPanel();
        testPanel.setLayout(null);
        testPanel.setOpaque(false);
        testPanel.setBounds(0, 0, AppConstants.PANEL_UP_WIDTH, AppConstants.PANEL_UP_HEIGHT);

        //테스트를 위한 버튼 추가임
        //아이콘 별 네임은 파일 resources/ 각 파일별 이름이고, 패쓰는 폴더 이름임!
        ImageIcon getIcon = DataManagers.getInstance().getIcon("logo","panel_UP");
        CustomButton customButton = new CustomButton(getIcon);
        customButton.setClickEffect(true, 0.3f);
        //위치랑 사이즈는 피그마에서 상세 확인!
        customButton.setBounds(31, 30, 75, 36);

        //테스트를 위한 폰트 추가임!
        JLabel fontLabel = new JLabel("WatchThis");
        fontLabel.setFont(DataManagers.getInstance().getFont("",24));
        fontLabel.setForeground(Color.white);
        fontLabel.setBounds(68, 36, 148, 36);
        fontLabel.setHorizontalTextPosition(JLabel.RIGHT);
        fontLabel.setVerticalTextPosition(JLabel.CENTER);

        testPanel.add(fontLabel);
        testPanel.add(customButton);
        frameBase.setInnerPanel(testPanel,"up");
    }
}