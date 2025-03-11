package Panel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInput;
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

	private JPanel rankingContentPanel; // 랭킹 콘텐츠 리스트 패널
	private JScrollPane rankingListScroll; // 스크롤 패널
	private JPanel RankingListPanel; // 스크롤 가능한 콘텐츠 패널

	// 위에 //버튼 //밑에
	public MyPagePanel() {

		// MID_PANEL의 공간 정의
		setLayout(null);
		setBounds(0, 0, AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT);
		setOpaque(false);

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

				List<Integer> reviewlist = new ArrayList<Integer>();

				for (ReviewVO reviewVO : (DBDataManagers.getInstance().getDbReviewsData())) {
					if (reviewVO.getReviewName() == (DataManagers.getInstance().getMyUser().getNickName())) {
						reviewlist.add(reviewVO.getReviewid());
					}
				}

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

				setMyWishList();
			}
		});

		//WishList Set
		setMyWishList();

		btnPanel.add(myiineBtn);
		btnPanel.add(myReviewBtn);
		btnPanel.add(myiineHistoryBar);
		btnPanel.add(myReviewHistoryBar);
		btnPanel.add(myiineHistory);
		btnPanel.add(myReviewHistory);
		add(btnPanel);
	}

	private void setMyWishList(){
		//기존 데이터 지우기
		if(RankingListPanel != null) {
			RankingListPanel.removeAll();
		}

		List<Integer> favoriteList = new ArrayList<Integer>();

		for (FavoriteVO favoriteVO : (DBDataManagers.getInstance().getDbFavoriteData())) {
			if (favoriteVO.getUserId() == DataManagers.getInstance().getMyUser().getId()) {
				System.out.println(favoriteVO.getMyFavoriteList());
				favoriteList = favoriteVO.getMyFavoriteList();
			}
		}

		InsertScrollPanel(favoriteList.size());

		if(!favoriteList.isEmpty()) {
			// 콘텐츠 라벨 추가
			int yOffset = 10; //라벨 y 간격
			for (int i = 0; i < favoriteList.size(); i++) {
				Integer favoriteId = favoriteList.get(i);
				ItemVO findItem = DataManagers.getInstance().FindItemFromId(favoriteId);
				JLabel rankingItemLabel = createRankingItemLabel(findItem);

				//RankingContentBG scrollableContent 기준 센터 정렬
				int xCenter = (RankingListPanel.getPreferredSize().width - rankingItemLabel.getPreferredSize().width) / 2;

				rankingItemLabel.setBounds(xCenter + 3, yOffset, 510, 126);
				RankingListPanel.add(rankingItemLabel);
				yOffset += 136;
			}
		}
		else {
			//위시 리스트 없을 때
			System.out.println("위시 리스트가 없습니다!");
			JLabel noWish = new JLabel("아직 찜리스트가 없습니다!", SwingConstants.CENTER);
			noWish.setBorder(null);
			noWish.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
			noWish.setFont(DataManagers.getInstance().getFont("bold", 24));
			noWish.setBounds(0,0,535,200);

			RankingListPanel.add(noWish);
		}

		RankingListPanel.repaint();
		RankingListPanel.revalidate();

		updatePanel();
	}

	private void InsertScrollPanel(int count){
		//랭킹 콘텐츠 리스트 패널 추가
		rankingContentPanel = createRankingContentPanel(count);
		this.add(rankingContentPanel);
	}

	private void updatePanel(){
		this.revalidate();
		this.repaint();
	}

	private JPanel createRankingContentPanel(int createCount){
		int rankingPagePanelWidth = 595; // RankingPagePanel의 너비
		int rankingContentPanelWidth = 535; // rankingContentPanel의 너비
		int rankingContentPanelHeight = 230; // rankingContentPanel의 높이
		int x = (rankingPagePanelWidth - rankingContentPanelWidth) / 2; // 중앙 정렬 계산

		RoundedPanel rankingContentPanel = new RoundedPanel(50, 50); // 둥근 모서리 적용
		rankingContentPanel.setBounds(x, 310, 535, 230); // 크기 설정
		rankingContentPanel.setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // 배경색 적용
		rankingContentPanel.setLayout(null);

		// 내부 스크롤 가능하도록 설정
		RankingListPanel = new JPanel();
		RankingListPanel.setLayout(null); // 내부 콘텐츠도 null 레이아웃
		RankingListPanel.setOpaque(false);
		RankingListPanel.setBackground(new Color(0, 0, 0, 0));
		RankingListPanel.setPreferredSize(new Dimension(rankingContentPanelWidth - 20, (rankingContentPanelHeight * createCount))); //스크롤 높이 설정

		//스크롤 생성 RoundedPanel 내부에 약간의 여백 남겨서 처리
		rankingListScroll = new JScrollPane(RankingListPanel);
		int padding = 7; // 둥근 테두리를 가리지 않도록 패딩 추가
		rankingListScroll.setBounds(padding, padding, rankingContentPanelWidth - (padding * 2), rankingContentPanelHeight - (padding * 2));
		rankingListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rankingListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		rankingListScroll.getVerticalScrollBar().setUnitIncrement(10);//스크롤 속도 증가
		rankingListScroll.setOpaque(false);

		//뷰포트를 불투명하게 만들고 배경색 강제 적용
		rankingListScroll.getViewport().setOpaque(false); // 뷰포트 불투명하게 설정

		//스크롤 패널 테두리 제거
		rankingListScroll.setBorder(null);

		//스크롤바 자체를 투명하게 설정 (숨김)
		rankingListScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // 스크롤바 크기 최소화
		rankingListScroll.getVerticalScrollBar().setOpaque(false); // 스크롤바 투명화

		//RoundedPanel 내부에 추가
		rankingContentPanel.add(rankingListScroll);

		return rankingContentPanel;
	}

	private JLabel createRankingItemLabel(ItemVO item) {
		//랭킹 콘텐츠 배경
		JLabel rankingLabel = new JLabel(DataManagers.getInstance().getIcon("RankingContentBG", "rank_Page"));
		rankingLabel.setLayout(null);

		//썸네일 버튼
		JButton thumbnailButton = new JButton(ImageHelper.getResizedImageIconFromUrl(item.getThumbnail(), 79, 99));
		thumbnailButton.setBounds(50, 14, 79, 99);
		thumbnailButton.setBorderPainted(false);
		thumbnailButton.setContentAreaFilled(false);
		thumbnailButton.setFocusPainted(false);
		thumbnailButton.addActionListener(e -> showContentDetails(item));
		rankingLabel.add(thumbnailButton);

		//장르 바
		JLabel categoryLabel = new JLabel(DataManagers.getInstance().getIcon("category", "rank_Page"));
		categoryLabel.setBounds(122, 22, 80, 18);
		rankingLabel.add(categoryLabel);

		// 장르 텍스트가 7글자 초과시 .. 처리
		String genreTextlength = item.getGenres();
		int overNum = 8;// 초과 제한 될 글자 개수
		if (genreTextlength.length() > overNum) {
			genreTextlength = genreTextlength.substring(0, 7) + "..";
		}

		//장르 텍스트
		JLabel genreText = new JLabel(genreTextlength, SwingConstants.CENTER);
		genreText.setBounds(0, 2, 80, 18);
		genreText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		genreText.setFont(DataManagers.getInstance().getFont("bold", 6));
		categoryLabel.add(genreText);

		//타이틀
		JLabel titleLabel = new JLabel(item.getTitle(), SwingConstants.LEFT);
		titleLabel.setBounds(140, 50, 250, 20);
		titleLabel.setFont(DataManagers.getInstance().getFont("bold", 15));
		rankingLabel.add(titleLabel);

		//개봉일
		JLabel dateLabel = new JLabel(item.getPromotionDay(), SwingConstants.LEFT);
		dateLabel.setBounds(140, 75, 300, 15);
		dateLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		dateLabel.setFont(DataManagers.getInstance().getFont("bold", 10));
		rankingLabel.add(dateLabel);

		//평점 가져오기
		String rating = GenericFinder.findInList(DBDataManagers.getInstance().getDbRatingData(),
				findItem -> findItem.getItemId() == item.getId(),
				findItem -> Double.toString(findItem.getRatingPoint()));

		if (rating == null || rating.equals("0") || rating.equals("0.0")) {
			rating = "0.0";
		}

		//평점 아이콘
		JLabel ratingIcon = new JLabel(DataManagers.getInstance().getIcon("ratingIcon", "rank_Page"));
		ratingIcon.setBounds(450, 15, 31, 29);
		rankingLabel.add(ratingIcon);

		//평점 텍스트
		JLabel ratingText = new JLabel(rating, SwingConstants.CENTER);
		ratingText.setBounds(440, 45, 50, 15);
		ratingText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		ratingText.setFont(DataManagers.getInstance().getFont("bold", 12));
		rankingLabel.add(ratingText);

		// 로그인 유무에 따라 찜버튼 활성화 비활성화
		boolean isLogin = DataManagers.getInstance().getMyUser() != null;

		// 로그인 상태라면 찜버튼을 생성 및 추가
		if (isLogin) {
			JButton favoriteButton = new JButton(DataManagers.getInstance().getIcon("ggimIconOff", "rank_Page"));
			favoriteButton.setBounds(450, 84, 30, 30);
			favoriteButton.setBorderPainted(false);
			favoriteButton.setContentAreaFilled(false);
			favoriteButton.setFocusPainted(false);
			favoriteButton.setMargin(new Insets(0, 0, 0, 0)); // 버튼 여백 제거

			favoriteButton.addActionListener(e -> {
				// 현재 로그인한 유저 ID 가져오기
				String userId = DataManagers.getInstance().getMyUser().getId();
				int currentContentId = item.getId();
				FavoriteVO myFavoriteVO = null;

				// 기존 찜 리스트에서 현재 유저의 찜 데이터를 가져오기
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
						DataManagers.getInstance().getIcon(isContains ? "ggimIconOn" : "ggimIconOff", "rank_Page"));
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
		textButton.setForeground(isOn == true ? Color.decode(AppConstants.UI_POINT_COLOR_HEX) : Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		bar.setIcon((DataManagers.getInstance().getIcon(isOn == true ? "barOn" : "barOff", "detail_Content_Page")));
	}
}
