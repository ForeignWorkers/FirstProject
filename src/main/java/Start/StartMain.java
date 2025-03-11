package Start;

import Frame.FrameBase;
import Panel.BottomNavBar;
import Panel.MainPagePanel;
import Panel.TopNavBar;
import java.io.IOException;

public class StartMain {
    public static void main(String[] args) throws IOException {
       
    	// FrameBase 생성
        FrameBase frameBase = FrameBase.getInstance();

        //Start Panel
        //Main Panel Load!
        MainPagePanel mainPagePanel = new MainPagePanel();
        frameBase.setInnerPanel(mainPagePanel, "mid");
        
        //하단 네비 바 시작할 경로 "home"
        BottomNavBar bottomNavBar = new BottomNavBar("home");
        frameBase.setInnerPanel(bottomNavBar, "down");
        
        //상단 바
        TopNavBar topNavBar = new TopNavBar();
        frameBase.setInnerPanel(topNavBar, "up");
    }
}