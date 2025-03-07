package Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.google.common.collect.ImmutableSetMultimap;

import DAO.FavoriteDAO;
import Data.AppConstants;
import Helper.GenericFinder;
import Helper.ImageHelper;
import Managers.DBDataManagers;
import Managers.DataManagers;
import TESTDAO.TestContentDAO;
import VO.FavoriteVO;
import VO.ItemVO;

// 컨텐츠 패널
public class MainPagePanel extends JPanel {
	private ItemVO content; // 현재 패널이 표시하는 컨텐츠 정보
	private TestContentDAO contentDAO = new TestContentDAO(); // DAO 객체 생성
	private JPanel contentPanel; // 스크롤될 전체 컨텐츠 패널
	private int countContent = 4; // 작은 추천 컨텐츠 개수

	public MainPagePanel() {

		// 메인 패널 자체 크기 설정
		setLayout(null); // 전체 레이아웃 설정
		setBackground(new Color(0x404153));
		setBounds(0, 0, 600, 600); // 패널 크기 설정

		// 스크롤 적용 패널
		contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setBackground(new Color(0x404153));
		// 스크롤 적용 패널 사이즈 조정
		contentPanel.setPreferredSize(new Dimension(600, 825));

		// 스크롤 추가
		JScrollPane mainPageScroll = new JScrollPane(contentPanel);
		mainPageScroll.setBounds(0, 0, 600, 600); // 스크롤 영역 설정
		mainPageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mainPageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainPageScroll.getVerticalScrollBar().setUnitIncrement(10); // 스크롤 속도 증가

		// 스크롤바를 숨김
		mainPageScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		mainPageScroll.getVerticalScrollBar().setOpaque(false);

		// 테두리 영역 제거
		mainPageScroll.setBorder(null);

		// JScrollPane이 변경 사항을 인식하도록 설정
		mainPageScroll.setViewportView(contentPanel);
		mainPageScroll.revalidate();
		mainPageScroll.repaint();

		add(mainPageScroll);

		// 큰 추천 컨텐츠 패널 추가
		JLabel bigPanel = bigRecommendContentLabel();
		bigPanel.setBounds(150, 10, 282, 400); // 큰 패널 위치 크기 컨트롤
		contentPanel.add(bigPanel);

		// 작은 추천 컨텐츠 제목 TEXT
		JLabel smallPanelTitle = new JLabel("오늘의 추천 컨텐츠", SwingConstants.LEFT);
		smallPanelTitle.setBounds(16, 495, 250, 40); // 제목 위치 설정
		smallPanelTitle.setForeground(new Color(0x78DBA6));
		smallPanelTitle.setFont(DataManagers.getInstance().getFont("bold", 20));
		contentPanel.add(smallPanelTitle);

		// 작은 추천 컨텐츠 패널 추가
		JPanel smallPanel = smallRecommendContentPanel();
		smallPanel.setBounds(15, 537, 550, 232); // 작은 패널 위치 크기 컨트롤
		contentPanel.add(smallPanel);

		// 검색 바 추가
		JPanel searchBar = createSearchBar();
		searchBar.setBounds(16, 430, 550, 50); // 검색 바 위치 조정
		contentPanel.add(searchBar);

	}

	// 큰 추천 컨텐츠 라벨
	private JLabel bigRecommendContentLabel() {

		// bigLabel프레임을 bigPanel 크기에 맞게 리사이징
		ImageIcon bigLabelIcon = DataManagers.getInstance().getIcon("bigContentBG", "main_Page");
		Image scaledbigLabelImage = bigLabelIcon.getImage().getScaledInstance(282, 400, Image.SCALE_SMOOTH);
		ImageIcon resizedbigLabelIcon = new ImageIcon(scaledbigLabelImage);

		JLabel bigLabel = new JLabel(resizedbigLabelIcon);
		bigLabel.setLayout(null); // 내부 요소 배치 가능
		bigLabel.setBounds(140, 5, 282, 400); // 기존보다 크기를 조금 키움

		content = contentDAO.getRandomContent(); // 랜덤 컨텐츠 가져와 저장
		System.out.println("랜덤 컨텐츠 로드 완료: " + content.getTitle());

		// 컨텐츠 중앙 정렬을 위한 좌표 계산 setbound와 동일
		int bigLabelWidth = 282; // bigLabel의 너비
		int thumbWidth = 266; // 썸네일 버튼 너비 해당 사이즈로 리사이징됨
		int thumbHeight = 325;// 썸네일 버튼 높이 해당 사이즈로 리사이징됨
		int labelWidth = 200; // 제목과 장르 라벨 너비 센터 배치에 사용

		int thumbX = (bigLabelWidth - thumbWidth) / 2; // 썸네일 중앙 정렬
		int labelX = (bigLabelWidth - labelWidth) / 2; // 제목과 장르 라벨 중앙 정렬

		// 썸네일 이미지를 버튼 크기에 맞게 리사이징
		ImageIcon thumbnailIcon = ImageHelper.getResizedImageIconFromUrl(content.getThumbnail(), 222, 271);
		Image scaledthumbnailImage = thumbnailIcon.getImage().getScaledInstance(thumbWidth, thumbHeight,
				Image.SCALE_SMOOTH);
		ImageIcon resizedthumbnailIcon = new ImageIcon(scaledthumbnailImage);

		JButton thumbnailButton = new JButton(resizedthumbnailIcon);
		thumbnailButton.setBounds(thumbX, 10, thumbWidth, thumbHeight);
		thumbnailButton.setBorderPainted(false); // 테두리 없음
		thumbnailButton.setContentAreaFilled(false); // 버튼 배경색 제거
		thumbnailButton.setFocusPainted(false); // 클릭 시 테두리 없음
		thumbnailButton.addActionListener(e -> showContentDetails(content));

		// 제목 라벨
		JLabel titleLabel = new JLabel(content.getTitle(), SwingConstants.CENTER);
		titleLabel.setFont(DataManagers.getInstance().getFont("bold", 20));
		int titleLableY = 330;
		titleLabel.setBounds(labelX, titleLableY, labelWidth, 50);
		int titleStandardY = titleLableY + 30; // 제목의 아래 평점, 장르, 찜하기 높이 간격 설정

		// 좌측 하단 (평점 아이콘)
		int ratingX = labelX - 35; // 장르 라벨 기준 왼쪽
		JLabel ratingIconLabel = new JLabel(DataManagers.getInstance().getIcon("bigRatingIcon", "main_Page"));
		ratingIconLabel.setBounds(ratingX, titleStandardY, 30, 30);
		
		// 평점 데이터 가져옴
		String rating = GenericFinder.findInList(DBDataManagers.getInstance().getDbRatingData(),
				item -> item.getItemId() == content.getId(), // 조건: contentId가 일치하는지 확인
				item -> Double.toString(item.getRatingPoint()) // 변환: double -> String
		);
		
		//평점 정보가 없거나 0 또는 0.0 일 경우 평점 라벨 텍스트 0.0으로 처리
		if (rating == null || rating.equals("0") || rating.equals("0.0")) {
			rating = "0.0";
		}
		
		JLabel ratingTextLabel = new JLabel(rating, SwingConstants.LEFT);
		ratingTextLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
		ratingTextLabel.setForeground(new Color(0x78DBA6));
		// ratingX + n 평점 아이콘 기준으로 x값 + 좌측으로 이동
		ratingTextLabel.setBounds(ratingX + 30, titleStandardY + 5, 50, 30);

		// 장르 텍스트가 7글자 초과시 .. 처리
		int overNum = 7;// 제한될 글자 수
		String bigGenreText = content.getGenres();
		if (bigGenreText.length() > overNum) {
			bigGenreText = bigGenreText.substring(0, 7) + "..";
		}

		// 중앙 하단 (장르 텍스트 데이터)
		int genreX = labelX;
		JLabel genreTextLabel = new JLabel(bigGenreText, SwingConstants.CENTER);
		genreTextLabel.setFont(DataManagers.getInstance().getFont("regular", 11));
		genreTextLabel.setForeground(new Color(0x78DBA6));
		genreTextLabel.setBounds(genreX, titleStandardY + 4, labelWidth, 35);

		// 장르 라벨 아이콘 리사이징
		ImageIcon bigGenreLabelIcon = DataManagers.getInstance().getIcon("bigCategory", "main_Page");
		Image scaledBigGenreLabelImage = bigGenreLabelIcon.getImage().getScaledInstance(72, 23, Image.SCALE_SMOOTH);
		ImageIcon resizedBigGenreLabelIcon = new ImageIcon(scaledBigGenreLabelImage);

		// 중앙 하단 (장르 이미지 데이터)
		JLabel genreIconLabel = new JLabel(resizedBigGenreLabelIcon);
		genreIconLabel.setBounds(genreX, titleStandardY, labelWidth, 35);

		// 로그인 상태 확인
		//boolean isLoggedIn = (DataManagers.getInstance().getMyUser() != null);

		//로그인 유무에 따라 찜버튼 활성화 비활성화
		boolean isLogin = DataManagers.getInstance().getMyUser() != null;
		
		
		// 로그인 상태라면 찜버튼을 생성 및 추가
		if (isLogin) {
			// 우측 하단 (찜하기 버튼)
			int favoriteX = labelX + 203; // 장르 라벨 기준 오른쪽 +
			JButton favoriteButton = new JButton(DataManagers.getInstance().getIcon("bigWishBtnOff", "main_Page"));
			favoriteButton.setBounds(favoriteX, 362, 30, 30);
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
						DataManagers.getInstance().getIcon(isContains ? "bigWishBtnOn" : "bigWishBtnOff", "main_Page"));
			});
			//찜버튼 UI 배치
			bigLabel.add(favoriteButton);
		}

		// UI 배치
		bigLabel.add(thumbnailButton);
		bigLabel.add(titleLabel);
		bigLabel.add(ratingIconLabel);
		bigLabel.add(ratingTextLabel);
		bigLabel.add(genreTextLabel);
		bigLabel.add(genreIconLabel);

		return bigLabel;
	}

	// 작은 추천 컨텐츠 패널 (4개 가로 배치, setLayout(null) 사용)
	private JPanel smallRecommendContentPanel() {

		ItemVO item = null;
		JPanel smallPanel = new RoundedPanel(20, 20); // 라운드 모서리 적용

		// 변경이 잦을 수 있는 위치,크기는 mainpage쪽으로 이동
		smallPanel.setLayout(null); // null 레이아웃 유지
		smallPanel.setBackground(new Color(0xCBCBCB));

		int itemWidth = 150; // 아이템 넓이
		int margin = 10; // 아이템 간격
		int y = -10; // 아이템의 기본 Y 좌표

		int leftPadding = 10; // 좌측 아이템 패널과의 여백 설정

		// 첫 번째 아이템의 x 간격을 맞추기 위해 시작 위치 조정

		for (int i = 0; i < countContent; i++) { // 4개의 컨텐츠 추가
			item = contentDAO.getRandomContent(); // 랜덤 컨텐츠 가져오기

			// 각 아이템 x 좌표를 조정하여 가로 정렬
			int x = leftPadding + i * (itemWidth + margin);

			// 컨텐츠 아이템 매개변수 전달
			createContentItem(item, smallPanel, x, y);
		}

		return smallPanel;
	}

	// 기존 방식 유지: createContentItem()에 JPanel을 반환하지 않음
	private void createContentItem(ItemVO item, JPanel smallPanel, int x, int y) {

		int itemWidth = 148; // 개별 아이템이 들어갈 넓이 무조건 thumbWidth,labelWidth 보다 커야함
		int thumbWidth = 148; // 썸네일 넓이
		int labelWidth = 148; // 제목 및 장르 라벨의 넓이

		int thumbX = (itemWidth - thumbWidth) / 2;
		// int labelX = (itemWidth - labelWidth) / 2;

		// 프레임 이미지를 라벨로 설정
		ImageIcon smallIcon = DataManagers.getInstance().getIcon("smallContentBG", "main_Page");
		JLabel smallLabel = new JLabel(smallIcon);
		smallLabel.setLayout(null); // 내부 요소 배치 가능
		smallLabel.setBounds(x, y, itemWidth, 250); // 프레임 크기 조정

		boolean isLogin = DataManagers.getInstance().getMyUser() != null;
		
		if (isLogin) {
			// 찜하기 버튼 (썸네일 위에 배치)
			JButton favoriteButton = new JButton(DataManagers.getInstance().getIcon("smallWishBtnOff", "main_Page"));
			favoriteButton.setBounds(x + thumbX + 120, y + 205, 30, 30);
			favoriteButton.setBorderPainted(false);
			favoriteButton.setContentAreaFilled(false);
			favoriteButton.setFocusPainted(false);
			favoriteButton.setMargin(new Insets(0, 0, 0, 0)); // 버튼 여백 제거
			favoriteButton.addActionListener(e -> {
				
				// 새로운 로직
				String userId = DataManagers.getInstance().getMyUser().getId();
				int currentContentId = item.getId();
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
				favoriteButton.setIcon(DataManagers.getInstance()
						.getIcon(isContains ? "smallWishBtnOn" : "smallWishBtnOff", "main_Page"));
			});
			smallPanel.add(favoriteButton);
		}
		
		JButton thumbnail = new JButton(ImageHelper.getResizedImageIconFromUrl(item.getThumbnail(), 134, 164));
		thumbnail.setBounds(x, y + 20, thumbWidth, 181);
		thumbnail.setBorderPainted(false);
		thumbnail.setContentAreaFilled(false);
		thumbnail.setFocusPainted(false);
		thumbnail.addActionListener(e -> showContentDetails(item));

		// 제목 라벨
		JLabel titleLabel = new JLabel(item.getTitle(), SwingConstants.CENTER);
		titleLabel.setFont(DataManagers.getInstance().getFont("bold", 13));
		int titleLabelY = y + 188;
		titleLabel.setBounds(x, titleLabelY + 3, labelWidth, 30);

		// 장르 텍스트가 7글자 초과시 .. 처리
		String smallGenreText = item.getGenres();
		int overNum = 7;// 초과 제한 될 글자 개수
		if (smallGenreText.length() > overNum) {
			smallGenreText = smallGenreText.substring(0, 7) + "..";
		}

		// 장르 텍스트
		int genreY = titleLabelY + 22; // 타이틀 라벨 기준 y+
		JLabel genreTextLabel = new JLabel(smallGenreText, SwingConstants.CENTER);
		genreTextLabel.setFont(DataManagers.getInstance().getFont("bold", 7));
		genreTextLabel.setForeground(new Color(0x78DBA6));
		genreTextLabel.setBounds(x, genreY + 2, labelWidth, 20);

		ImageIcon smallGenreLabelIcon = DataManagers.getInstance().getIcon("smallCategory", "main_Page");
		Image scaledsmallGenreLabelImage = smallGenreLabelIcon.getImage().getScaledInstance(46, 13, Image.SCALE_SMOOTH);
		ImageIcon resizedsmallGenreLabelIcon = new ImageIcon(scaledsmallGenreLabelImage);

		// 장르 이미지
		JLabel genreIconLabel = new JLabel(resizedsmallGenreLabelIcon);

		int genreIconWidth = 160; // 장르 아이콘 크기
		int genreIconX = x + (itemWidth - genreIconWidth) / 2; // 장르 이미지 중앙 정렬
		genreIconLabel.setBounds(genreIconX, genreY, genreIconWidth, 20);

		// 평점 아이콘
		int ratingY = genreY; // 목적에 맞게 변수이름 변경
		JLabel ratingIconLabel = new JLabel(DataManagers.getInstance().getIcon("smallRatingIcon", "main_Page"));
		int ratingIconWidth = 100; // 별점 아이콘 크기
		int ratingIconX = x + (itemWidth - ratingIconWidth - 30) / 2; // 별점 아이콘 중앙 정렬
		ratingIconLabel.setBounds(ratingIconX - 47, ratingY, ratingIconWidth, 20);

		String rating = GenericFinder.findInList(DBDataManagers.getInstance().getDbRatingData(),
				finditem -> finditem.getItemId() == item.getId(), // 조건: contentId가 일치하는지 확인
				finditem -> Double.toString(finditem.getRatingPoint()) // 변환: double -> String
		);
		// 평점 정보가 없을 때 0.0으로 처리
		if (rating == null || rating.equals("0") || rating.equals("0.0")) {
			rating = "0.0";
		}

		// 평점 텍스트
		JLabel ratingValueLabel = new JLabel(rating, SwingConstants.LEFT);
		ratingValueLabel.setFont(DataManagers.getInstance().getFont("bold", 8));
		ratingValueLabel.setForeground(new Color(0x78DBA6));
		ratingValueLabel.setBounds(ratingIconX + 10, ratingY + 3, 40, 20); // 별점 아이콘 오른쪽 정렬

		// UI 배치
		smallPanel.add(thumbnail);
		smallPanel.add(titleLabel);
		smallPanel.add(genreTextLabel);
		smallPanel.add(genreIconLabel);
		smallPanel.add(ratingIconLabel);
		smallPanel.add(ratingValueLabel);
		smallPanel.add(smallLabel);
	}

	private JPanel createSearchBar() {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(null);
		searchPanel.setOpaque(false); // 배경 투명 처리

		// 검색 바 배경 이미지 (검색 버튼 역할)
		ImageIcon searchBarIcon = DataManagers.getInstance().getIcon("searchBarBG", "main_Page");
		JButton searchButton = new JButton(searchBarIcon);
		searchButton.setBounds(0, 0, 550, 50);
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.setFocusPainted(false);

		// 검색 버튼 클릭 시 검색 페이지 이동
		searchButton.addActionListener(e -> {
			OpenPage openPage = new OpenPage();
			openPage.openSearchPage();
		});

		// 입력 필드 (기본 안내 문구)
		JLabel searchField = new JLabel("보고 싶은 작품을 검색해보세요!", SwingConstants.LEFT);
		searchField.setBounds(40, 15, 250, 30); // 검색 바 내부 위치 설정
		searchField.setForeground(new Color(0xA4A4A4));
		searchField.setFont(DataManagers.getInstance().getFont("regular", 16));

		// 돋보기 아이콘(검색 바 내부에 배치)
		ImageIcon searchIcon = DataManagers.getInstance().getIcon("searchIcon", "main_Page");
		JLabel searchIconLabel = new JLabel(searchIcon);
		searchIconLabel.setBounds(480, 8, 30, 30); // 돋보기 아이콘 위치 설정

		// UI 배치
		searchPanel.add(searchField);
		searchPanel.add(searchIconLabel);
		searchPanel.add(searchButton);

		return searchPanel;
	}

	// 컨텐츠 페이지 이동
	private void showContentDetails(ItemVO content) {
		OpenPage openPage = new OpenPage();
		openPage.openContentPage(content);// openpage의 opencontentpage호출
	}


}