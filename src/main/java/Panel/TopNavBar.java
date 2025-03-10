package Panel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import Managers.DataManagers;

public class TopNavBar extends JPanel {
	private OpenPage openPage = new OpenPage(); // 페이지 이동 관리 메서드

	public TopNavBar() {
		setLayout(null);
		setBackground(new Color(0x404153)); // 배경색 설정
		setBounds(0, 0, 600, 200); // 패널크기 설정

		createLogoSection();

		// 로그인 확인 ture? false?
		boolean isLogIn = DataManagers.getInstance().getMyUser() != null;

		if (isLogIn) {

			createLogInView();// 로그인 시 마이페이지 구성 호출
		} else {

			createLogOutView(); // 비로그인 시 로그인 구성 호출
		}
	}

	// Title 텍스트 + 아이콘
	private void createLogoSection() {
		// WatchThis 텍스트 아이콘
		ImageIcon logotitleIcon = DataManagers.getInstance().getIcon("watchText", "panel_UP");
		JButton logoTitleButton = new JButton(logotitleIcon);
		logoTitleButton.setFont(DataManagers.getInstance().getFont("bold", 20));
		logoTitleButton.setForeground(Color.WHITE);
		logoTitleButton.setBounds(37, 25, 148, 40);
		logoTitleButton.setBorderPainted(false);
		logoTitleButton.setContentAreaFilled(false);
		logoTitleButton.setFocusPainted(false);
		// 클릭 시 홈페이지 이동
		logoTitleButton.addActionListener(e -> {
			try {
				openPage.openHomePage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		// 로고 아이콘
		ImageIcon logoIcon = DataManagers.getInstance().getIcon("logo", "panel_UP");
		JButton logoButton = new JButton(logoIcon);
		logoButton.setBounds(20, 25, 35, 40);
		logoButton.setBorderPainted(false);
		logoButton.setContentAreaFilled(false);
		logoButton.setFocusPainted(false);

		// 클릭 시 홈페이지 이동
		logoButton.addActionListener(e -> {
			try {
				openPage.openHomePage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		// UI 추가
		add(logoTitleButton);
		add(logoButton);
	}

	// 로그인 상태일 때 메소드 / 닉네임 + 마이페이지 버튼 나중에 사용

	private void createLogInView() {
		// 유저 닉네임을 불러옴
		String nickname = DataManagers.getInstance().getMyUser().getNickName();

		// 닉네임 라벨 생성
		JLabel welcomeLabel = new JLabel(nickname + "님 반갑습니다.", SwingConstants.RIGHT);
		welcomeLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
		welcomeLabel.setForeground(new Color(0x78DBA6)); // 텍스트 색상을 흰색으로 설정

		// 닉네임 라벨의 너비를 자동 계산
		int labelWidth = welcomeLabel.getPreferredSize().width; // 텍스트라벨의 동적 넓이

		int labelRightX = 485; // 마이페이지 버튼의 X 좌표
		int labelX = labelRightX - labelWidth; // 오른쪽 기준으로 X 위치 계산
		welcomeLabel.setBounds(labelX, 39, labelWidth, 20);

		// 마이페이지 버튼 로그인 버튼 대체
		ImageIcon myPageIcon = DataManagers.getInstance().getIcon("loginButton", "panel_UP");
		JButton myPageButton = new JButton(myPageIcon);
		myPageButton.setBounds(480, 26, 90, 40);
		myPageButton.setBorderPainted(false);
		myPageButton.setContentAreaFilled(false);
		myPageButton.setFocusPainted(false);

		// 버튼 텍스트
		JLabel myPageTextLabel = new JLabel("마이페이지");
		myPageTextLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
		myPageTextLabel.setForeground(Color.WHITE);
		myPageTextLabel.setBounds(495, 39, 90, 20); // 버튼 아래에 배치

		myPageButton.addActionListener(e -> {
			try {
				openPage.openMyPage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}); // 마이페이지 이동

		// UI 추가
		add(myPageTextLabel);
		add(myPageButton);
		add(welcomeLabel);
	}

	// 로그아웃 상태일 때 / 로그인 버튼만 표시
	private void createLogOutView() {
		// 로그인 버튼
		ImageIcon loginIcon = DataManagers.getInstance().getIcon("loginButton", "panel_UP");
		JButton loginButton = new JButton(loginIcon);
		loginButton.setBounds(480, 26, 90, 40);
		loginButton.setBorderPainted(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setFocusPainted(false);

		// 버튼 텍스트
		JLabel loginTextLabel = new JLabel("로그인");
		loginTextLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
		loginTextLabel.setForeground(Color.WHITE);
		loginTextLabel.setBounds(507, 39, 90, 20); // 버튼 아래에 배치

		loginButton.addActionListener(e -> {
			try {
				openPage.openLoginPage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}); // 로그인 페이지 이동

		// UI 추가
		add(loginTextLabel);
		add(loginButton);
	}
}