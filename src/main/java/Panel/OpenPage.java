package Panel;

import java.io.IOException;

import javax.swing.JOptionPane;

import Frame.FrameBase;
import Helper.ImageHelper;
import Managers.DataManagers;
import VO.ItemVO;

public class OpenPage {

	// 마이페이지 이동 확인
	public void openMyPage() throws IOException {
		ImageHelper.clearImageCache(); //캐시 초기화
		FrameBase.getInstance().setInnerPanel(new TopNavBar(), "up"); // 상단바 재로딩
		FrameBase.getInstance().setInnerPanel(new MyPagePanel(), "mid");
		FrameBase.getInstance().setInnerPanel(new BottomNavBar("mypage"), "down");
	}

	// 로그인 페이지 이동 확인
	public void openLoginPage() throws IOException {
		FrameBase.getInstance().setInnerPanel(new LoginPanel(), "mid");
		FrameBase.getInstance().setInnerPanel(new BottomNavBar("mypage"), "down");
	}

	// 회원 가입 페이지 이동
	public void openSignupPage() throws IOException {
		FrameBase.getInstance().setInnerPanel(new SignUPPanel(), "mid");
		// FrameBase.getInstance().setInnerPanel(new BottomNavBar("mypage"), "down");
	}

	// 홈 페이지 이동 확인
	public void openHomePage() throws IOException {
		ImageHelper.clearImageCache(); //캐시 초기화
		FrameBase.getInstance().setInnerPanel(new MainPagePanel(), "mid");
		FrameBase.getInstance().setInnerPanel(new BottomNavBar("home"), "down");
	}

	// 랭킹 페이지 이동 확인
	public void openRankingPage() throws IOException {
        DataManagers.getInstance().clearIconCache();//아이콘 캐시 초기화
		ImageHelper.clearImageCache(); //이미지 캐시 초기화
		FrameBase.getInstance().setInnerPanel(new RankingPagePanel(), "mid");
		FrameBase.getInstance().setInnerPanel(new BottomNavBar("ranking"), "down");
	}

	// 검색 페이지 이동 확인
	public void openSearchPage() throws IOException {
		ImageHelper.clearImageCache(); //캐시 초기화
		FrameBase.getInstance().setInnerPanel(new SearchMainPanel(), "mid");
		FrameBase.getInstance().setInnerPanel(new BottomNavBar("search"), "down");
	}

	// 메인 컨텐츠 세부 페이지 이동 확인
	public void openContentPage(ItemVO content) {
		DataManagers.getInstance().clearIconCache();//아이콘 캐시 초기화
		ImageHelper.clearImageCache(); //이미지 캐시 초기화
		try {
			// 클릭한 콘텐츠 정보를 ContentsDetailImagePanel에 전달
			FrameBase.getInstance().setInnerPanel(new ContentsDetailImagePanel(content), "mid");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			if (DataManagers.getInstance().getMyUser() != null) { // 무조건 false 로그인으로 이동
				openMyPage();
			} else {
				openLoginPage();
			}
			break;
		}
	}
}