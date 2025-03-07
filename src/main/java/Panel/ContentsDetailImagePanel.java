package Panel;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Component.CustomButton;
import DAO.FavoriteDAO;
import Data.AppConstants;
import Helper.ImageHelper;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.FavoriteVO;
import VO.ItemVO;
import VO.ReviewVO;

public class ContentsDetailImagePanel extends JPanel {
	// 세 번째 패널 (버튼 클릭에 따라 변경)
	private ItemVO content; // 현재 패널이 표시하는 컨텐츠 정보
	private JPanel ContentDetailTabPanel; // 기본 패널 (검은색)
	private JPanel ReviewTabPanel; // 변경될 패널 (초록색)
	private JPanel AccessOTTPanel; // 변경될 패널 (초록색)
	private ReviewVO rv;

	// 콘텐츠 상세 이미지 패널 생성자
	public ContentsDetailImagePanel(ItemVO content) {

		// MID_PANEL의 공간 정의
		setLayout(null);
		setPreferredSize(new Dimension(AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT));
		setBounds(0, 0, AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT);

		// 첫 번째 패널(포스터이미지, 평점-리뷰, 평점아이콘, 타이틀, 장르, 제작년도, 찜하기버튼 위치함)
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(null);
		firstPanel.setBounds(0, 0, 595, 316);
		firstPanel.setBackground(Color.decode("#404153"));

		// 이미지 라벨
		ImageIcon thumbnailIcon = ImageHelper.getResizedImageIconFromUrl(content.getThumbnail(), 222, 271);
		Image scaledthumbnailImage = thumbnailIcon.getImage().getScaledInstance(129, 161, Image.SCALE_SMOOTH);
		ImageIcon resizedthumbnailIcon = new ImageIcon(scaledthumbnailImage);

		JLabel thumbnailPoster = new JLabel(resizedthumbnailIcon);
		thumbnailPoster.setLayout(null);
		thumbnailPoster.setBounds(405, 30, 129, 161);

		// 평점 및 리뷰 개수
		JLabel ratingLabel = new JLabel("평점");
		ratingLabel.setLayout(null);
		ratingLabel.setBounds(94, 32, 75, 18);
		ratingLabel.setFont(DataManagers.getInstance().getFont("", 15));
		ratingLabel.setForeground(Color.decode("#CBCBCB"));
		JLabel reviewLabel = new JLabel("평가자 명수");
		reviewLabel.setLayout(null);
		reviewLabel.setBounds(151, 30, 75, 18);
		reviewLabel.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 12));
		reviewLabel.setForeground(Color.decode("#CBCBCB"));
		// 평점 별 아이콘
		JLabel RatingStarIcon = new JLabel(DataManagers.getInstance().getIcon("ratingIcon", "detail_Content_Page"));
		RatingStarIcon.setLayout(null);
		RatingStarIcon.setBounds(64, 28, 25, 25);
		// 영화 제목
		JLabel titleLabel = new JLabel(content.getTitle());
		titleLabel.setLayout(null);
		titleLabel.setBounds(64, 63, 343, 48);
		titleLabel.setFont(DataManagers.getInstance().getFont("LINESeedKR-Bd", 24));
		titleLabel.setForeground(Color.decode("#78DBA6"));
		// 장르
		JLabel genreLabel = new JLabel(content.getGenres());
		genreLabel.setLayout(null);
		genreLabel.setBounds(71, 121, 67, 25);
		genreLabel.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 10));
		genreLabel.setForeground(Color.decode("#404153"));
		CustomButton genreIcon = new CustomButton(
				DataManagers.getInstance().getIcon("categoryBtn", "detail_Content_Page"));
		genreIcon.setLayout(null);
		genreIcon.setBounds(64, 121, 58, 25);
		// 제작년도
		JLabel yearLabel = new JLabel(content.getPromotionYear());
		yearLabel.setLayout(null);
		yearLabel.setBounds(67, 162, 42, 15);
		yearLabel.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 10));
		yearLabel.setForeground(Color.decode("#404153"));
		CustomButton YearIcon = new CustomButton(DataManagers.getInstance().getIcon("dateBtn", "detail_Content_Page"));
		YearIcon.setLayout(null);
		YearIcon.setBounds(64, 150, 58, 25);

		// 로그인 유무에 따라 찜버튼 활성화 비활성화
		boolean isLogin = DataManagers.getInstance().getMyUser() != null;

		// 로그인 상태라면 찜버튼을 생성 및 추가
		if (isLogin) {
			// (찜하기 버튼)
			JButton favoriteButton = new JButton(
					DataManagers.getInstance().getIcon("ggimBtnOff", "detail_Content_Page"));
			favoriteButton.setLayout(null);
			favoriteButton.setBounds(511, 168, 22, 22);
			favoriteButton.setBorderPainted(false);
			favoriteButton.setContentAreaFilled(false);
			favoriteButton.setFocusPainted(false);
			favoriteButton.setMargin(new Insets(0, 0, 0, 0)); // 버튼 여백 제거

			favoriteButton.addActionListener(e -> {
				String userId = DataManagers.getInstance().getMyUser().getId();
				int currentContentId = content.getId();
				FavoriteVO myFavoriteVO = null;

				for (FavoriteVO find : DBDataManagers.getInstance().getDbFavoriteData()) {
					if (find.getUserId() == userId) {
						myFavoriteVO = find;
					}
				}

				FavoriteDAO favoriteDAO = new FavoriteDAO();
				favoriteDAO.setLocalFavoriteData(myFavoriteVO, currentContentId);
				try {
					favoriteDAO.addFavoriteToJson(myFavoriteVO, AppConstants.FAVORITE_FILE_NAME, userId);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				boolean isContains = myFavoriteVO.getMyFavoriteList().contains(currentContentId);
				// 변경된 아이콘 세팅 -> 비주얼
				favoriteButton.setIcon(
						DataManagers.getInstance().getIcon(isContains ? "ggimBtnOn" : "ggimBtnOff", "main_Page"));
			});
			// 찜버튼 UI 배치
			firstPanel.add(favoriteButton);
		}

		// 만든 라벨들 firstPanel에 추가
		firstPanel.add(ratingLabel);
		firstPanel.add(reviewLabel);
		firstPanel.add(RatingStarIcon);
		firstPanel.add(titleLabel);
		firstPanel.add(genreLabel);
		firstPanel.add(genreIcon);
		firstPanel.add(yearLabel);
		firstPanel.add(YearIcon);
		firstPanel.add(thumbnailPoster);
		add(firstPanel);

		// 탭버튼의 문자열 객체생성 및 위치할당
		CustomButton contentTabString = new CustomButton("작품설명");
		contentTabString.setBounds(145, 279, 114, 28);
		contentTabString.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 24));
		contentTabString.setForeground(Color.decode("#78DBA6"));
		CustomButton reviewTabString = new CustomButton("리뷰");
		reviewTabString.setBounds(336, 279, 114, 28);
		reviewTabString.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 24));
		reviewTabString.setForeground(Color.decode("#CBCBCB"));
		// 탭버튼 하단의 바이미지 객체 생성 및 위치할당
		CustomButton contentTabBarOn = new CustomButton(
				DataManagers.getInstance().getIcon("barOn", "detail_Content_Page"));
		contentTabBarOn.setBounds(152, 312, 100, 3);
		CustomButton contentTabBarOff = new CustomButton(
				DataManagers.getInstance().getIcon("barOff", "detail_Content_Page"));
		contentTabBarOff.setBounds(152, 312, 100, 3);
		CustomButton reviewTabBarOn = new CustomButton(
				DataManagers.getInstance().getIcon("barOn", "detail_Content_Page"));
		reviewTabBarOn.setBounds(343, 312, 100, 3);
		CustomButton reviewTabBarOff = new CustomButton(
				DataManagers.getInstance().getIcon("barOff", "detail_Content_Page"));
		reviewTabBarOff.setBounds(343, 312, 100, 3);

		// 처음 페이지를 열었을때 나오는 default값 세팅해준 것
		contentTabBarOn.setVisible(true);
		contentTabBarOff.setVisible(false);
		reviewTabBarOn.setVisible(false);
		reviewTabBarOff.setVisible(true);

		// 작품설명탭 버튼
		CustomButton ContentTabButton = new CustomButton("");
		ContentTabButton.setLayout(null);
		ContentTabButton.setBounds(145, 279, 152, 36);
		ContentTabButton.setBackground(Color.decode("#404153"));
		ContentTabButton.setClickEffect(false, 0.3f);
		ContentTabButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					showContentPanel();
					contentTabString.setForeground(Color.decode("#78DBA6"));
					reviewTabString.setForeground(Color.decode("#CBCBCB"));
					contentTabBarOn.setVisible(true);
					contentTabBarOff.setVisible(false);
					reviewTabBarOn.setVisible(false);
					reviewTabBarOff.setVisible(true);
					System.out.println("작품설명탭버튼을 클릭함");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			};
		});
		// 리뷰탭 버튼
		CustomButton reviewTabButton = new CustomButton("");
		reviewTabButton.setLayout(null);
		reviewTabButton.setBounds(336, 279, 152, 36);
		reviewTabButton.setForeground(Color.decode("#78DBA6"));
		reviewTabButton.setClickEffect(false, 0.3f);
		reviewTabButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					showReviewPanel();
					contentTabString.setForeground(Color.decode("#CBCBCB"));
					reviewTabString.setForeground(Color.decode("#78DBA6"));
					contentTabBarOn.setVisible(false);
					contentTabBarOff.setVisible(true);
					reviewTabBarOn.setVisible(true);
					reviewTabBarOff.setVisible(false);
					System.out.println("리뷰탭버튼을 클릭함");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		firstPanel.add(contentTabString);
		firstPanel.add(reviewTabString);
		firstPanel.add(ContentTabButton);
		firstPanel.add(reviewTabButton);
		firstPanel.add(contentTabBarOn);
		firstPanel.add(contentTabBarOff);
		firstPanel.add(reviewTabBarOn);
		firstPanel.add(reviewTabBarOff);

		// 작품상세설명 패널(디폴트로 표시되는 패널)\MID_Panel의 중단에 위치함
		ContentDetailTabPanel = new JPanel();
		ContentDetailTabPanel.setLayout(null);
		ContentDetailTabPanel.setBounds(0, 316, 595, 138);
		ContentDetailTabPanel.setBackground(Color.decode("#404153"));
		add(ContentDetailTabPanel);

		// 리뷰탭버튼 클릭시 나오는 패널(클릭시 ReviewPanelLast를 MID_Panel의 중단에 보여줌)
		ReviewTabPanel = new JPanel();
		ReviewTabPanel.setLayout(null);
		ReviewTabPanel.setBounds(0, 316, 595, 337);
		ReviewTabPanel.setBackground(Color.decode("#404153"));
		add(ReviewTabPanel);

		// ReviewPanelLast을 따오기 위한 객체 RPL
		ReviewPanelLast AccessreviewPanelLast = new ReviewPanelLast();
		ReviewTabPanel.add(AccessreviewPanelLast);

		// ContentDetailTabPanel에 들어갈 [장르,개봉일,등급,러닝타임,국가,감독-출연] 라벨들 생성
		JLabel genreLabelMenu = new JLabel("장르");
		genreLabelMenu.setBounds(97, 15, 62, 30);
		genreLabelMenu.setFont(DataManagers.getInstance().getFont("", 18));
		genreLabelMenu.setForeground(Color.decode("#CBCBCB"));
		JLabel genreLabelInfo = new JLabel(content.getGenres());
		genreLabelInfo.setLayout(null);
		genreLabelInfo.setBounds(162, 15, 131, 30);
		genreLabelInfo.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 18));
		genreLabelInfo.setForeground(Color.decode("#CBCBCB"));
		// 개봉일
		JLabel PromotionDayLabelMenu = new JLabel("개봉일");
		PromotionDayLabelMenu.setBounds(297, 15, 62, 30);
		PromotionDayLabelMenu.setFont(DataManagers.getInstance().getFont("", 18));
		PromotionDayLabelMenu.setForeground(Color.decode("#CBCBCB"));
		JLabel PromotionDayLabelInfo = new JLabel(content.getPromotionDay());
		PromotionDayLabelInfo.setLayout(null);
		PromotionDayLabelInfo.setBounds(402, 15, 131, 30);
		PromotionDayLabelInfo.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 18));
		PromotionDayLabelInfo.setForeground(Color.decode("#CBCBCB"));
		// 등급
		JLabel rateLabelMenu = new JLabel("등급");
		rateLabelMenu.setBounds(97, 45, 62, 30);
		rateLabelMenu.setFont(DataManagers.getInstance().getFont("", 18));
		rateLabelMenu.setForeground(Color.decode("#CBCBCB"));
		JLabel rateLabelInfo = new JLabel(content.getAgeRating());
		rateLabelInfo.setBounds(162, 45, 131, 30);
		rateLabelInfo.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 18));
		rateLabelInfo.setForeground(Color.decode("#CBCBCB"));
		// 러닝타임
		JLabel runningTimeLabelMenu = new JLabel("러닝타임");
		runningTimeLabelMenu.setBounds(297, 45, 62, 30);
		runningTimeLabelMenu.setFont(DataManagers.getInstance().getFont("", 18));
		runningTimeLabelMenu.setForeground(Color.decode("#CBCBCB"));
		JLabel runningTimeLabelInfo = new JLabel(content.getRunningTime());
		runningTimeLabelInfo.setLayout(null);
		runningTimeLabelInfo.setBounds(402, 45, 131, 30);
		runningTimeLabelInfo.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 18));
		runningTimeLabelInfo.setForeground(Color.decode("#CBCBCB"));
		// 국가
		JLabel nationLabelMenu = new JLabel("국가");
		nationLabelMenu.setBounds(97, 75, 62, 30);
		nationLabelMenu.setFont(DataManagers.getInstance().getFont("", 18));
		nationLabelMenu.setForeground(Color.decode("#CBCBCB"));
		JLabel nationLabelInfo = new JLabel(content.getNation());
		nationLabelInfo.setLayout(null);
		nationLabelInfo.setBounds(162, 75, 131, 30);
		nationLabelInfo.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 18));
		nationLabelInfo.setForeground(Color.decode("#CBCBCB"));
		// 감독-출연
		JLabel DirectorActorMenu = new JLabel("감독/출연");
		DirectorActorMenu.setBounds(297, 75, 62, 30);
		DirectorActorMenu.setFont(DataManagers.getInstance().getFont("", 18));
		DirectorActorMenu.setForeground(Color.decode("#CBCBCB"));
		JLabel DirectorActorLabelInfo = new JLabel(content.getDirector() + "/" + content.getActor()[0]);
		DirectorActorLabelInfo.setLayout(null);
		DirectorActorLabelInfo.setBounds(402, 75, 161, 30);
		DirectorActorLabelInfo.setFont(DataManagers.getInstance().getFont("LINESeedKR-Th", 18));
		DirectorActorLabelInfo.setForeground(Color.decode("#CBCBCB"));
		ContentDetailTabPanel.add(genreLabelMenu);
		ContentDetailTabPanel.add(genreLabelInfo);
		ContentDetailTabPanel.add(PromotionDayLabelMenu);
		ContentDetailTabPanel.add(PromotionDayLabelInfo);
		ContentDetailTabPanel.add(rateLabelMenu);
		ContentDetailTabPanel.add(rateLabelInfo);
		ContentDetailTabPanel.add(runningTimeLabelMenu);
		ContentDetailTabPanel.add(runningTimeLabelInfo);
		ContentDetailTabPanel.add(nationLabelMenu);
		ContentDetailTabPanel.add(nationLabelInfo);
		ContentDetailTabPanel.add(DirectorActorMenu);
		ContentDetailTabPanel.add(DirectorActorLabelInfo);

		// OTT버튼이 위치할 패널을 생성(MID_PANEL의 하단에 위치함, ReviewPanelLast를 보여줄때에는 사라짐)
		AccessOTTPanel = new JPanel();
		AccessOTTPanel.setLayout(null);
		AccessOTTPanel.setBounds(0, 454, 595, 199);
		AccessOTTPanel.setBackground(Color.decode("#404153"));
		add(AccessOTTPanel);
		// "볼수있는OTT"라는 안내문구 생성
		JLabel showOTT = new JLabel("볼수있는 OTT");
		showOTT.setLayout(null);
		showOTT.setBounds(65, 1, 199, 30);
		showOTT.setFont(DataManagers.getInstance().getFont("LINESeedKR-Bd", 20));
		showOTT.setForeground(Color.decode("#78DBA6"));
		AccessOTTPanel.add(showOTT);

		// 각 OTT의 아이콘,문자열,재생아이콘, 해당OTT URL로 이동하는 기능을 담은 버튼 생성
		// a. 넷플릭스버튼
		CustomButton netflixButton = new CustomButton(
				DataManagers.getInstance().getIcon("ottBtnBG", "detail_Content_Page"));
		netflixButton.setLayout(null);
		netflixButton.setBounds(95, 42, 195, 43);
		netflixButton.setClickEffect(true, 0.3f);
		netflixButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("넷플릭스 클릭");
			}
		});
		JLabel netIcon = new JLabel();
		netIcon.setIcon(DataManagers.getInstance().getIcon("netflexIcon", "detail_Content_Page"));
		netIcon.setBounds(107, 49, 34, 32);
		JLabel netPlayIcon = new JLabel();
		netPlayIcon.setIcon(DataManagers.getInstance().getIcon("netflexPlay", "detail_Content_Page"));
		netPlayIcon.setBounds(253, 52, 22, 24);
		JLabel netflixString = new JLabel("넷플릭스");
		netflixString.setBounds(153, 49, 82, 30);
		netflixString.setFont(DataManagers.getInstance().getFont("", 15));
		netflixString.setForeground(Color.decode("#E40613"));
		netflixButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://www.netflix.com/kr/")); // 원하는 URL
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		// b. 티빙버튼
		CustomButton tvingButton = new CustomButton(
				DataManagers.getInstance().getIcon("ottBtnBG", "detail_Content_Page"));
		tvingButton.setLayout(null);
		tvingButton.setBounds(94, 97, 195, 43);
		tvingButton.setClickEffect(true, 0.3f);
		tvingButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("티빙 클릭");
			}
		});
		JLabel tvingIcon = new JLabel();
		tvingIcon.setIcon(DataManagers.getInstance().getIcon("tvingIcon", "detail_Content_Page"));
		tvingIcon.setBounds(114, 105, 27, 27);
		JLabel tvingPlayIcon = new JLabel();
		tvingPlayIcon.setIcon(DataManagers.getInstance().getIcon("tvingPlayIcon", "detail_Content_Page"));
		tvingPlayIcon.setBounds(253, 102, 34, 34);
		JLabel tvingString = new JLabel("티빙");
		tvingString.setBounds(152, 104, 82, 30);
		tvingString.setFont(DataManagers.getInstance().getFont("", 15));
		tvingString.setForeground(Color.decode("#FF616C"));
		tvingButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://www.tving.com/onboarding")); // 원하는 URL
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		// c. 왓챠버튼
		CustomButton watchaButton = new CustomButton(
				DataManagers.getInstance().getIcon("ottBtnBG", "detail_Content_Page"));
		watchaButton.setLayout(null);
		watchaButton.setBounds(308, 42, 195, 43);
		watchaButton.setClickEffect(true, 0.3f);
		watchaButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("왓챠 클릭");
			}
		});
		JLabel watchaIcon = new JLabel();
		watchaIcon.setIcon(DataManagers.getInstance().getIcon("watchIcon", "detail_Content_Page"));
		watchaIcon.setBounds(328, 50, 27, 27);
		JLabel watchaPlayIcon = new JLabel();
		watchaPlayIcon.setIcon(DataManagers.getInstance().getIcon("watchPlayIcon", "detail_Content_Page"));
		watchaPlayIcon.setBounds(468, 52, 22, 24);
		JLabel watchaString = new JLabel("왓챠");
		watchaString.setBounds(366, 49, 82, 30);
		watchaString.setFont(DataManagers.getInstance().getFont("", 15));
		watchaString.setForeground(Color.decode("#FF508A"));
		watchaButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://watcha.com/browse/theater")); // 원하는 URL
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		// e. 라프텔버튼
		CustomButton laftelButton = new CustomButton(
				DataManagers.getInstance().getIcon("ottBtnBG", "detail_Content_Page"));
		laftelButton.setLayout(null);
		laftelButton.setBounds(308, 97, 195, 43);
		laftelButton.setClickEffect(true, 0.3f);
		laftelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("라프텔 클릭");
			}
		});
		JLabel laftelIcon = new JLabel();
		laftelIcon.setIcon(DataManagers.getInstance().getIcon("laftelIcon", "detail_Content_Page"));
		laftelIcon.setBounds(328, 105, 27, 27);
		JLabel laftelPlayIcon = new JLabel();
		laftelPlayIcon.setIcon(DataManagers.getInstance().getIcon("laftelPlay", "detail_Content_Page"));
		laftelPlayIcon.setBounds(468, 107, 22, 24);
		JLabel laftelString = new JLabel("라프텔");
		laftelString.setBounds(366, 104, 82, 30);
		laftelString.setFont(DataManagers.getInstance().getFont("", 15));
		laftelString.setForeground(Color.decode("#816BFF"));
		laftelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://laftel.net/")); // 원하는 URL
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		AccessOTTPanel.add(netflixString);
		AccessOTTPanel.add(netPlayIcon);
		AccessOTTPanel.add(netIcon);
		AccessOTTPanel.add(netflixButton);
		AccessOTTPanel.add(tvingString);
		AccessOTTPanel.add(tvingPlayIcon);
		AccessOTTPanel.add(tvingIcon);
		AccessOTTPanel.add(tvingButton);
		AccessOTTPanel.add(watchaString);
		AccessOTTPanel.add(watchaPlayIcon);
		AccessOTTPanel.add(watchaIcon);
		AccessOTTPanel.add(watchaButton);
		AccessOTTPanel.add(laftelString);
		AccessOTTPanel.add(laftelPlayIcon);
		AccessOTTPanel.add(laftelIcon);
		AccessOTTPanel.add(laftelButton);

		// MID_PANEL안의 상중하패널 표시기본값 세팅
		ContentDetailTabPanel.setVisible(true);
		ReviewTabPanel.setVisible(false);
		AccessOTTPanel.setVisible(true);

		// 스크롤용 패널 생성
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setPreferredSize(new Dimension(AppConstants.PANEL_MID_WIDTH, 800)); // 높이를 크게 설정
		// 스크롤패널 재료 넣어주기
		contentPanel.add(firstPanel);
		contentPanel.add(ContentDetailTabPanel);
		contentPanel.add(ReviewTabPanel);
		contentPanel.add(AccessOTTPanel);
		// 스크롤 패널 껍데기 생성
		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBounds(0, 0, AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// 가시화 되어있는 스크롤 '바'를 투명화. 스크롤 기능만 남겨놓음
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollPane.getHorizontalScrollBar().setOpaque(false);
		// 구현한 스크롤 패널 추가.
		setLayout(null);
		add(scrollPane);
	}

	// 패널 교환을 위한 메서드.
	private void showContentPanel() {
		ContentDetailTabPanel.setVisible(true);
		ReviewTabPanel.setVisible(false);
		AccessOTTPanel.setVisible(true);
	}

	// 패널 교환을 위한 메서드.
	private void showReviewPanel() {
		ContentDetailTabPanel.setVisible(false);
		ReviewTabPanel.setVisible(true);
		AccessOTTPanel.setVisible(false);
	}

}// class