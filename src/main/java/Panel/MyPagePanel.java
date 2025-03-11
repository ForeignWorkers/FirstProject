package Panel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import Component.CustomButton;
import DAO.FavoriteDAO;
import DAO.SignUPDAO;
import Data.AppConstants;
import Helper.GenericFinder;
import Helper.ImageHelper;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.FavoriteVO;
import VO.ItemVO;
import VO.ReviewVO;
import VO.UserVO;

public class MyPagePanel extends JPanel {

	private CustomButton myReviewHistory;
	private CustomButton myReviewHistoryBar;
	private CustomButton myReviewBtn;
	private CustomButton myiineHistory;
	private CustomButton myiineHistoryBar;
	private CustomButton myiineBtn;

	private JPanel myPageContentPanel; // 콘텐츠 리스트 패널
	private JScrollPane myPageListScroll; // 스크롤 패널
	private JPanel myPageListPanel; // 스크롤 가능한 패널

	//리뷰 
	private MyPageReviewPanel myPageReviewPanel; // MyPageReviewPanel 객체
	
	// 위에 //버튼 //밑에
	public MyPagePanel() {

		// MID_PANEL의 공간 정의

		setLayout(null);
		setBounds(0, 0, AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT);
		setOpaque(false);

		// 리뷰 패널 객체 생성 + 보이지 않게 처리
		myPageReviewPanel = new MyPageReviewPanel();
	    myPageReviewPanel.setVisible(false);
	     
		// 위쪽 패널 닉네임 변경하는 쪽
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(null);
		firstPanel.setBackground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
		firstPanel.setBounds(0, 0, 595, 217);

		// 마이페이지 프로필 이미지
		JLabel mypageProfileimage = new JLabel();
		mypageProfileimage.setBounds((int) 31.35, 44, (int) 98.65, (int) 98.65);
		mypageProfileimage.setIcon(DataManagers.getInstance().getIcon("profile01", "myPage_Page"));

		// 마이페이지 닉네임 변경 칸
		JLabel mypageNicknameBox = new JLabel();
		mypageNicknameBox.setBounds(149, 74, 277, 37);
		mypageNicknameBox.setIcon(DataManagers.getInstance().getIcon("nickNameBox", "myPage_Page"));

		// 마이페이지 닉네임 변경 텍스트 필드
		JTextField mypageNicknameEdit = new JTextField();
		mypageNicknameEdit.setBounds(163, 79, 277, 37);
		mypageNicknameEdit.setFont(DataManagers.getInstance().getFont("bold", 17));
		mypageNicknameEdit.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		mypageNicknameEdit.setOpaque(false);
		mypageNicknameEdit.setBorder(null);

		// 현재 로그인된 사용자의 닉네임을 표시
		UserVO loggedInUser = DataManagers.getInstance().getMyUser(); // myUser에서 로그인된 사용자 정보 가져오기
		if (loggedInUser != null) {
			mypageNicknameEdit.setText(loggedInUser.getNickName()); // 닉네임 텍스트 필드에 설정
		}

		// 마이페이지 닉네임 수정 커스텀 버튼
		CustomButton mypageNicknameEditBtn = new CustomButton(
				DataManagers.getInstance().getIcon("nickNameEditBtn", "myPage_Page"));
		mypageNicknameEditBtn.setBounds(439, 74, 37, 37);
		mypageNicknameEditBtn.setOpaque(false);
		mypageNicknameEditBtn.setBorder(null);

		mypageNicknameEditBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 새로운 닉네임을 텍스트 필드에서 가져오기
				String newNickname = mypageNicknameEdit.getText().trim();

				// 닉네임이 비어있지 않은지 체크
				if (!newNickname.isEmpty() && !newNickname.equals(loggedInUser.getNickName())) {
					// 닉네임을 수정된 값으로 업데이트
					loggedInUser.setNickName(newNickname);

					// 변경된 닉네임을 DB나 파일에 저장
					try {
						// 사용자의 닉네임을 업데이트한 후 파일에 저장
						SignUPDAO signUpDAO = new SignUPDAO();
						signUpDAO.updateUserNick(loggedInUser, true);
						System.out.println("닉네임이 수정되었습니다: " + newNickname);

						// ⭐ 닉네임 수정 후 상단 바와 마이페이지 재갱신
						OpenPage openPage = new OpenPage();
						openPage.openMyPage(); // 페이지 재호출로 자동 반영

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					System.out.println("변경할 닉네임이 없습니다.");
				}
			}
		});

		// 마이페이지 제일 긴 초록색 바
		JLabel mypageGreenBarnagai = new JLabel();
		mypageGreenBarnagai.setBounds(30, 206, 534, 5);
		mypageGreenBarnagai.setIcon(DataManagers.getInstance().getIcon("bar", "myPage_Page"));

		firstPanel.add(mypageGreenBarnagai);
		firstPanel.add(mypageNicknameEdit);
		firstPanel.add(mypageNicknameEditBtn);
		firstPanel.add(mypageNicknameBox);
		firstPanel.add(mypageProfileimage);
		add(firstPanel);

		// 중간 패널 버튼 누르는 쪽
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(null);
		btnPanel.setBackground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
		btnPanel.setBounds(0, 217, 595, 71);
		btnPanel.setOpaque(false);

		// 해야하는거 리뷰내역이랑 리뷰내역 밑에 바를 같은 버튼으로 만들기

		// 리뷰내역 버튼
		myReviewHistory = new CustomButton("리뷰내역");
		myReviewHistory.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		myReviewHistory.setFont(DataManagers.getInstance().getFont("bold", 24));
		myReviewHistory.setBounds(342, 37, 114, 35);
		myReviewHistory.setVerticalAlignment(SwingConstants.CENTER);

		// 리뷰내역 텍스트 밑 버튼 줄
		myReviewHistoryBar = new CustomButton(DataManagers.getInstance().getIcon("barOff", "detail_Content_Page"));
		myReviewHistoryBar.setBounds(336, 68, 100, 3);
		myReviewHistoryBar.setOpaque(false);
		myReviewHistoryBar.setBorder(null);

		// 리뷰내역과 바를 가리는 투명 버튼
		myReviewBtn = new CustomButton("");
		myReviewBtn.setBounds(335, 0, 114, 71);
		myReviewBtn.setBackground(new Color(0, 0, 0, 0));
		myReviewBtn.setOpaque(true);

		myReviewBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ChangeButtonColor(myReviewHistory, myReviewHistoryBar, true);
				ChangeButtonColor(myiineHistory, myiineHistoryBar, false);
				
				myPageContentPanel.setVisible(false);
				myPageReviewPanel.setVisible(true); // MyPageReviewPanel 보이게 설정
                add(myPageReviewPanel); // MyPageReviewPanel을 패널에 추가
                revalidate(); // 레이아웃을 다시 갱신
                repaint(); // 다시 그리기
			}
		});
		
		// 찜 리스트 버튼
		myiineHistory = new CustomButton("찜 리스트");
		myiineHistory.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		myiineHistory.setFont(DataManagers.getInstance().getFont("bold", 24));
		myiineHistory.setBounds(154, 37, 114, 35);
		myiineHistory.setVerticalAlignment(SwingConstants.CENTER);

		// 찜 리스트 텍스트 밑 버튼 줄
		myiineHistoryBar = new CustomButton(DataManagers.getInstance().getIcon("barOn", "detail_Content_Page"));
		myiineHistoryBar.setBounds(149, 68, 100, 3);
		myiineHistoryBar.setOpaque(false);
		myiineHistoryBar.setBorder(null);

		// 찜 리스트와 바를 가리는 투명 버튼
		myiineBtn = new CustomButton("");
		myiineBtn.setBounds(130, 0, 114, 71);
		myiineBtn.setBackground(new Color(0, 0, 0, 0));
		myiineBtn.setOpaque(false);

		myiineBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ChangeButtonColor(myReviewHistory, myReviewHistoryBar, false);
				ChangeButtonColor(myiineHistory, myiineHistoryBar, true);

				myPageContentPanel.setVisible(true);
				myPageReviewPanel.setVisible(false); // MyPageReviewPanel 안보이게 설정
				setMyWishList();
				revalidate();
                repaint();
			}
		});

		// WishList Set
		setMyWishList();

		btnPanel.add(myiineBtn);
		btnPanel.add(myReviewBtn);
		btnPanel.add(myiineHistoryBar);
		btnPanel.add(myReviewHistoryBar);
		btnPanel.add(myiineHistory);
		btnPanel.add(myReviewHistory);
		add(btnPanel);

	}

	private void setMyWishList() {

		// 기존 랭킹 콘텐츠 패널이 존재하면 제거
		if (myPageContentPanel != null) {
			this.remove(myPageContentPanel);
			myPageContentPanel = null; // 반드시 null 처리
		}

		List<Integer> favoriteList = new ArrayList<Integer>();

		for (FavoriteVO favoriteVO : (DBDataManagers.getInstance().getDbFavoriteData())) {
			if (favoriteVO.getUserId().equals(DataManagers.getInstance().getMyUser().getId())) {
				System.out.println(favoriteVO.getMyFavoriteList());
				favoriteList = favoriteVO.getMyFavoriteList();
			}
		}

		// 패널 재생성 및 추가 (기존 패널 제거 후)
		InsertScrollPanel(favoriteList.size());

		if (!favoriteList.isEmpty()) {
			// 콘텐츠 라벨 추가
			int yOffset = 10; // 라벨 y 간격
			for (int i = 0; i < favoriteList.size(); i++) {
				Integer favoriteId = favoriteList.get(i);
				ItemVO findItem = DataManagers.getInstance().FindItemFromId(favoriteId);
				JLabel rankingItemLabel = createRankingItemLabel(findItem);

				// RankingContentBG scrollableContent 기준 센터 정렬
				int xCenter = (myPageListPanel.getPreferredSize().width - rankingItemLabel.getPreferredSize().width)
						/ 2;

				rankingItemLabel.setBounds(xCenter + 3, yOffset, 510, 126);
				myPageListPanel.add(rankingItemLabel);
				yOffset += 136;
			}
		} else {
			// 위시 리스트 없을 때
			System.out.println("위시 리스트가 없습니다!");
			JLabel noWish = new JLabel("아직 찜리스트가 없습니다!", SwingConstants.CENTER);
			noWish.setBorder(null);
			noWish.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
			noWish.setFont(DataManagers.getInstance().getFont("bold", 24));
			noWish.setBounds(0, 0, 535, 200);

			myPageListPanel.add(noWish);
		}

		// 패널 새로고침
		myPageListPanel.repaint();
		myPageListPanel.revalidate();
		this.revalidate();
		this.repaint();
		updatePanel();
	}

	private void InsertScrollPanel(int count) {
		// 랭킹 콘텐츠 리스트 패널 추가
		myPageContentPanel = createRankingContentPanel(count);
		this.add(myPageContentPanel);
	}

	private void updatePanel() {
		this.revalidate();
		this.repaint();
	}

	private JPanel createRankingContentPanel(int createCount) {
		int myPagePanelWidth = 595; // RankingPagePanel의 너비
		int myPageContentPanelWidth = 535; // rankingContentPanel의 너비
		int myPageContentPanelHeight = 230; // rankingContentPanel의 높이
		int x = (myPagePanelWidth - myPageContentPanelWidth) / 2; // 중앙 정렬 계산

		RoundedPanel myPageContentPanel = new RoundedPanel(50, 50); // 둥근 모서리 적용
		myPageContentPanel.setBounds(x, 310, 535, 230); // 크기 설정
		myPageContentPanel.setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // 배경색 적용
		myPageContentPanel.setLayout(null);

		// 내부 스크롤 가능하도록 설정
		myPageListPanel = new JPanel();
		myPageListPanel.setLayout(null); // 내부 콘텐츠도 null 레이아웃
		myPageListPanel.setOpaque(false);
		myPageListPanel.setBackground(new Color(0, 0, 0, 0));

		// 리스트 스크롤 동적으로 개수에 맞게 높이 설정
		int contentHeight = 136; // 한 개 콘텐츠 높이 + 간격
		int minHeight = 230; // 최소 높이 (패널이 너무 작지 않게)
		int totalHeight = Math.max(contentHeight * createCount, minHeight); // 최소 높이 보장
		myPageListPanel.setPreferredSize(new Dimension(new Dimension(myPageContentPanelWidth - 20, totalHeight))); // 스크롤
																													// 높이
																													// 설정

		// 스크롤 생성 RoundedPanel 내부에 약간의 여백 남겨서 처리
		myPageListScroll = new JScrollPane(myPageListPanel);
		int padding = 7; // 둥근 테두리를 가리지 않도록 패딩 추가
		myPageListScroll.setBounds(padding, padding, myPageContentPanelWidth - (padding * 2),
				myPageContentPanelHeight - (padding * 2));
		myPageListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		myPageListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		myPageListScroll.getVerticalScrollBar().setUnitIncrement(10);// 스크롤 속도 증가
		myPageListScroll.setOpaque(false);

		// 뷰포트를 불투명하게 만들고 배경색 강제 적용
		myPageListScroll.getViewport().setOpaque(false); // 뷰포트 불투명하게 설정

		// 스크롤 패널 테두리 제거
		myPageListScroll.setBorder(null);

		// 스크롤바 자체를 투명하게 설정 (숨김)
		myPageListScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // 스크롤바 크기 최소화
		myPageListScroll.getVerticalScrollBar().setOpaque(false); // 스크롤바 투명화

		// RoundedPanel 내부에 추가
		myPageContentPanel.add(myPageListScroll);

		return myPageContentPanel;
	}

	private JLabel createRankingItemLabel(ItemVO item) {
		// 랭킹 콘텐츠 배경
		JLabel rankingLabel = new JLabel(DataManagers.getInstance().getIcon("RankingContentBG", "rank_Page"));
		rankingLabel.setLayout(null);

		// 썸네일 버튼
		JButton thumbnailButton = new JButton(ImageHelper.getResizedImageIconFromUrl(item.getThumbnail(), 79, 99, item.getId()));
		thumbnailButton.setBounds(50, 14, 79, 99);
		thumbnailButton.setBorderPainted(false);
		thumbnailButton.setContentAreaFilled(false);
		thumbnailButton.setFocusPainted(false);
		thumbnailButton.addActionListener(e -> showContentDetails(item));
		rankingLabel.add(thumbnailButton);

		// 장르 바
		JLabel categoryLabel = new JLabel(DataManagers.getInstance().getIcon("category", "rank_Page"));
		categoryLabel.setBounds(122, 22, 80, 18);
		rankingLabel.add(categoryLabel);

		// 장르 텍스트가 7글자 초과시 .. 처리
		String genreTextlength = item.getGenres();
		int overNum = 8;// 초과 제한 될 글자 개수
		if (genreTextlength.length() > overNum) {
			genreTextlength = genreTextlength.substring(0, 7) + "..";
		}

		// 장르 텍스트
		JLabel genreText = new JLabel(genreTextlength, SwingConstants.CENTER);
		genreText.setBounds(0, 2, 80, 18);
		genreText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		genreText.setFont(DataManagers.getInstance().getFont("bold", 6));
		categoryLabel.add(genreText);

		// 타이틀
		JLabel titleLabel = new JLabel(item.getTitle(), SwingConstants.LEFT);
		titleLabel.setBounds(140, 50, 250, 20);
		titleLabel.setFont(DataManagers.getInstance().getFont("bold", 15));
		rankingLabel.add(titleLabel);

		// 개봉일
		JLabel dateLabel = new JLabel(item.getPromotionDay(), SwingConstants.LEFT);
		dateLabel.setBounds(140, 75, 300, 15);
		dateLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		dateLabel.setFont(DataManagers.getInstance().getFont("bold", 10));
		rankingLabel.add(dateLabel);

		// 평점 가져오기
		String rating = GenericFinder.findInList(DBDataManagers.getInstance().getDbRatingData(),
				findItem -> findItem.getItemId() == item.getId(),
				findItem -> Double.toString(findItem.getRatingPoint()));

		if (rating == null || rating.equals("0") || rating.equals("0.0")) {
			rating = "0.0";
		}

		// 평점 아이콘
		JLabel ratingIcon = new JLabel(DataManagers.getInstance().getIcon("ratingIcon", "rank_Page"));
		ratingIcon.setBounds(450, 15, 31, 29);
		rankingLabel.add(ratingIcon);

		// 평점 텍스트
		JLabel ratingText = new JLabel(rating, SwingConstants.CENTER);
		ratingText.setBounds(440, 45, 50, 15);
		ratingText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		ratingText.setFont(DataManagers.getInstance().getFont("bold", 12));
		rankingLabel.add(ratingText);

		// --------------------------- 찜 버튼 ---------------------------
		// 로그인 유무에 따라 찜버튼 활성화 비활성화
		boolean isLogin = DataManagers.getInstance().getMyUser() != null;

		// 로그인 상태라면 찜버튼을 생성 및 추가
		if (isLogin) {
			// 우측 하단 (찜하기 버튼)
			JButton favoriteButton = new JButton(); // 버튼 생성

			// 초기 찜 여부 확인
			String userId = DataManagers.getInstance().getMyUser().getId();
			int currentContentId = item.getId();
			FavoriteVO myFavoriteVO = null;

			for (FavoriteVO find : DBDataManagers.getInstance().getDbFavoriteData()) {
				if (find.getUserId().equals(userId)) {
					myFavoriteVO = find;
					break;
				}
			}

			if (myFavoriteVO == null) {
				myFavoriteVO = new FavoriteVO();
				myFavoriteVO.setUserId(userId);
			}

			boolean isContains = myFavoriteVO.getMyFavoriteList().contains(currentContentId);
			favoriteButton.setIcon(
					DataManagers.getInstance().getIcon(isContains ? "ggimIconOn" : "ggimIconOff", "rank_Page"));

			// 버튼 속성 설정
			favoriteButton.setBounds(450, 84, 30, 30);
			favoriteButton.setBorderPainted(false);
			favoriteButton.setContentAreaFilled(false);
			favoriteButton.setFocusPainted(false);
			favoriteButton.setMargin(new Insets(0, 0, 0, 0));

			// 찜 토글 클릭 이벤트
			FavoriteVO finalMyFavoriteVO = myFavoriteVO;
			favoriteButton.addActionListener(e -> {

				boolean isNowContains = finalMyFavoriteVO.getMyFavoriteList().contains(currentContentId);
				if (isNowContains) {
					finalMyFavoriteVO.getMyFavoriteList().remove(Integer.valueOf(currentContentId)); // 찜 해제
				} else {
					finalMyFavoriteVO.getMyFavoriteList().add(currentContentId); // 찜 추가
				}

				System.out.println("현재 찜 목록: " + finalMyFavoriteVO.getMyFavoriteList());

				FavoriteDAO favoriteDAO = new FavoriteDAO();
				favoriteDAO.setLocalFavoriteData(finalMyFavoriteVO, currentContentId);
				try {
					favoriteDAO.addFavoriteToJson(finalMyFavoriteVO, AppConstants.FAVORITE_FILE_NAME,
							AppConstants.FOLDER_ID);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				boolean nowContains = finalMyFavoriteVO.getMyFavoriteList().contains(currentContentId);
				favoriteButton.setIcon(
						DataManagers.getInstance().getIcon(nowContains ? "ggimIconOn" : "ggimIconOff", "rank_Page"));

				// 찜 리스트 즉시 갱신(이 메서드로 전체 리스트를 다시 그려줌)
				setMyWishList();
			});

			// 찜 버튼 UI 배치
			rankingLabel.add(favoriteButton);
		}
		return rankingLabel;
	}

	// 컨텐츠 페이지 이동
	private void showContentDetails(ItemVO content) {
		OpenPage openPage = new OpenPage();
		openPage.openContentPage(content);// openpage의 opencontentpage호출
	}

	private void ChangeButtonColor(CustomButton textButton, CustomButton bar, boolean isOn) {
		textButton.setForeground(isOn == true ? Color.decode(AppConstants.UI_POINT_COLOR_HEX)
				: Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		bar.setIcon((DataManagers.getInstance().getIcon(isOn == true ? "barOn" : "barOff", "detail_Content_Page")));
	}

}
