package Panel;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import Managers.DataManagers;
import Managers.LoginConfrimManager;

public class BottomNavBar extends JPanel {
    private OpenPage openPage = new OpenPage(); //페이지 임시 이동 관리 객체
    private String currentPage; //현재 페이지를 나타내는 변수

    public BottomNavBar(String currentPage) {
        this.currentPage = currentPage; //현재 페이지 저장
        setLayout(null);
        setBackground(new Color(0x3d445c)); //배경색 설정
        setBounds(0, 0, 600, 80); //위치,크기 설정
        
        //버튼 크기 및 위치 설정
        int buttonSize = 50; //버튼 사이즈
        int startX = 70; //첫 번째 버튼 위치
        int gap = 130;   //버튼 간격
        
        //네비게이션 버튼 추가 매개 변수 전달
        //출력할 버튼 이름 + x,y좌표 + 크기 + 현재 페이지 + 페이지 이동 메소드 명
        add(createNavButton("main", startX, 5, buttonSize, "home", "openHomePage"));
        add(createNavButton("rank", startX + gap, 5, buttonSize, "ranking", "openRankingPage"));
        add(createNavButton("search", startX + gap * 2, 5, buttonSize, "search", "openSearchPage"));
        add(createNavButton("myPage", startX + gap * 3, 5, buttonSize, "mypage", "openMyPage"));
    }
    
    private JButton createNavButton(String baseName, int x, int y, int size, String pageName, String action) {
        boolean isActive = currentPage.equals(pageName); // 현재 페이지 여부 확인

        //버튼 활/비활성화 처리 ex) main + MenuON/MenuOff
        String iconKey = isActive ? baseName + "MenuON" : baseName + "MenuOff";
        ImageIcon icon = DataManagers.getInstance().getIcon(iconKey, "panel_DOWN");
        
        //icon을 버튼으로 생성
        JButton Navbutton = new JButton(icon);
        Navbutton.setBounds(x, y, size, size);
        
        //버튼 외곽처리
        Navbutton.setBorderPainted(false);
        Navbutton.setContentAreaFilled(false);
        Navbutton.setFocusPainted(false);
        Navbutton.setMargin(new Insets(0, 0, 0, 0));//버튼 여백 제거
        
        //mypage버튼 눌렀을 때 예외처리 마이테이지 이동 or 로그인 창 이동
        Navbutton.addActionListener(e -> {
            if (action.equals("openMyPage")) {
                if (LoginConfrimManager.loginConfrim("ABC", "123")) { //로그인 확인 id,pw
                    //존재하는 계정이면 마이페이지
                	openPage.openMyPage();
                } else {
                	//존재하지 않는 계정이면 마이페이지
                    openPage.openLoginPage();
                }
            } else {
            	//매소드에 openpage메소드 명 전달
                invokeOpenPageMethod(action);
            }
        });

        return Navbutton;
        
    }
    
    //버튼 눌렀을때 methodName에 해당하는 Openpage메소드 실행
    private void invokeOpenPageMethod(String methodName) {
    	//invoke사용 시 접근할 수 없는 메서드를 실행시도 할 수 있기때문에 예외처리
        try {
        	//해당 메서드를 실행함
            OpenPage.class.getMethod(methodName).invoke(openPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}