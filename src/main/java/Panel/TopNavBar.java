package Panel;

import javax.swing.*;
import java.awt.*;
import Managers.DataManagers;
import Managers.LoginConfrimManager;

public class TopNavBar extends JPanel {
    private OpenPage openPage = new OpenPage(); //페이지 이동 관리 메서드

    public TopNavBar() {
        setLayout(null);
        setBackground(new Color(0x404153)); // 배경색 설정
        setBounds(0, 0, 600, 200); // 패널크기 설정
        
        createLogoSection();
        
        //로그인 확인 ture? false?
        boolean isLogIn = LoginConfrimManager.loginConfrim("ABC", "123");

        if (isLogIn) {
        	createLogInView();//로그인 시 마이페이지 구성 호출
        } else {
        	createLogOutView(); //비로그인 시 로그인 구성 호출
        }
    }

    //Title 텍스트 + 아이콘
    private void createLogoSection() {
        //WatchThis 텍스트 아이콘
    	ImageIcon logotitleIcon = DataManagers.getInstance().getIcon("watchText", "panel_UP");
    	JButton logoTitleButton = new JButton(logotitleIcon);
    	logoTitleButton.setFont(DataManagers.getInstance().getFont("bold", 20));
    	logoTitleButton.setForeground(Color.WHITE);
    	logoTitleButton.setBounds(37, 25, 148, 40);
    	logoTitleButton.setBorderPainted(false);
    	logoTitleButton.setContentAreaFilled(false);
    	logoTitleButton.setFocusPainted(false);
    	//클릭 시 홈페이지 이동
    	logoTitleButton.addActionListener(e -> openPage.openHomePage());
    	
        //로고 아이콘
        ImageIcon logoIcon = DataManagers.getInstance().getIcon("logo", "panel_UP");
        JButton logoButton = new JButton(logoIcon);
        logoButton.setBounds(20, 25, 35, 40);
        logoButton.setBorderPainted(false);
        logoButton.setContentAreaFilled(false);
        logoButton.setFocusPainted(false);

        //클릭 시 홈페이지 이동
        logoButton.addActionListener(e -> openPage.openHomePage());

        //UI 추가
        add(logoTitleButton);
        add(logoButton);
    }

    //로그인 상태일 때 메소드 / 닉네임 + 마이페이지 버튼
    private void createLogInView() {
    	
    	String nickname = "민규12345"; // 닉네임 (임시)

    	// 패널 너비 가져오기 (이 값을 사용하여 중앙 정렬)
    	int panelWidth = this.getWidth(); // TopNavBar의 너비

    	// 닉네임 라벨 생성
    	JLabel welcomeLabel = new JLabel(nickname + "님 반갑습니다.", SwingConstants.CENTER);
    	welcomeLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
    	welcomeLabel.setForeground(new Color(0x78DBA6)); // 민트색
    	
    	//닉네임 중앙 정렬을 위한 X좌표 계산
    	int labelWidth = 180; // 라벨의 가로 길이 (텍스트 길이에 따라 조절 가능)
    	int labelX = (panelWidth - labelWidth) / 2; // 중앙 정렬 X 위치

    	//Y좌표 설정
    	welcomeLabel.setBounds(labelX, 30, labelWidth, 40);
        
        //마이페이지 버튼 로그인 버튼 대체
        ImageIcon myPageIcon = DataManagers.getInstance().getIcon("loginButton", "panel_UP");
        JButton myPageButton = new JButton(myPageIcon);
        myPageButton.setBounds(480, 26, 90, 40);
        myPageButton.setBorderPainted(false);
        myPageButton.setContentAreaFilled(false);
        myPageButton.setFocusPainted(false);
        
        //버튼 텍스트
        JLabel myPageTextLabel = new JLabel("마이페이지");
        myPageTextLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
        myPageTextLabel.setForeground(Color.WHITE);
        myPageTextLabel.setBounds(495, 39, 90, 20); // 버튼 아래에 배치
        
        myPageButton.addActionListener(e -> openPage.openMyPage()); // 마이페이지 이동
        	
        //UI 추가
        add(myPageTextLabel);
        add(myPageButton);
        add(welcomeLabel);
    }

    //로그아웃 상태일 때 / 로그인 버튼만 표시
    private void createLogOutView() {
        //로그인 버튼
        ImageIcon loginIcon = DataManagers.getInstance().getIcon("loginButton", "panel_UP");
        JButton loginButton = new JButton(loginIcon);
        loginButton.setBounds(480, 26, 90, 40);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        
        //버튼 텍스트
        JLabel loginTextLabel = new JLabel("로그인");
        loginTextLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
        loginTextLabel.setForeground(Color.WHITE);
        loginTextLabel.setBounds(507, 39, 90, 20); // 버튼 아래에 배치
        
        loginButton.addActionListener(e -> openPage.openLoginPage()); // 로그인 페이지 이동
        
        //UI 추가
        add(loginTextLabel);
        add(loginButton);
    }
}