package Panel;

import java.io.IOException;

import javax.swing.JOptionPane;

import Frame.FrameBase;
import Managers.LoginConfrimManager;

public class OpenPage {

	// 임시 마이페이지 이동 확인
	public void openMyPage() {
		JOptionPane.showMessageDialog(null, "마이페이지로 이동");
	}

	// 임시 로그인 페이지 이동 확인
	public void openLoginPage() throws IOException {
		FrameBase.getInstance().setInnerPanel(new LoginPanel(), "mid");
	}

	// 임시 홈 페이지 이동 확인
	public void openHomePage() throws IOException {
		FrameBase.getInstance().setInnerPanel(new MainPagePanel(), "mid");
		FrameBase.getInstance().setInnerPanel(new BottomNavBar("home"), "down");
	}

	// 임시 랭킹 페이지 이동 확인
	public void openRankingPage() {
		JOptionPane.showMessageDialog(null, "랭킹 페이지로 이동");
	}

	// 임시 검색 페이지 이동 확인
	public void openSearchPage() {
		JOptionPane.showMessageDialog(null, "검색 페이지로 이동");
	}

	// 임시 메인 컨텐츠 세부 페이지 이동 확인
	public void openMainContentPage() {
		JOptionPane.showMessageDialog(null, "메인 컨텐츠 세부 페이지 이동 확인");
	}

	// 임시 메인 컨텐츠 세부 페이지 이동 확인
	public void openContentPage(String contentTitle) {
		JOptionPane.showMessageDialog(null, contentTitle + " 상세 페이지 이동");
	}

	// 하단 네비를 위한 공통 메서드
	public void navigateToPage(String pageName) throws IOException {
		switch (pageName) {
		case "home":
			openHomePage();
			break;
		case "ranking":
			openRankingPage();
			break;
		case "search":
			openSearchPage();
			break;
		case "mypage":
			if (LoginConfrimManager.loginConfrim("ddd", "ddd")) { // 무조건 false 로그인으로 이동
				openMyPage();
			} else {
				openLoginPage();
			}
			break;
		}
	}
}