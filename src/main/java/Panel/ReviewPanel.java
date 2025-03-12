package Panel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import Interface.ContentDetailListener;
import java.util.ArrayList;
import java.util.List;

import Component.CustomButton;
import DAO.ReviewDAO;
import Data.AppConstants;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.ItemVO;
import VO.ReviewVO;
import VO.UserVO;

public class ReviewPanel extends JPanel {
	private JPanel contentPanel;
	private List<JLabel> createdReiveContent = new ArrayList<>();

	private JTextArea reviewInput;
	private CustomButton[] starButtons; // 별점 선택을 위한 버튼 배열
	private JLabel starCountText;
	private JLabel reviewTextCount;

	private int selectedRating = 0; // 기본 평점 0점
	private ReviewDAO reviewDAO; // ReviewDAO 추가

	private CustomButton submitButton;

	private boolean isLoggedIn = (DataManagers.getInstance().getMyUser() != null); // 로그인 여부

	private int reviewCreatedCount = 0;
	private int paddingY = 140;
	private int perHeight = 126;
	private int gapY = 10;

	public List<ContentDetailListener> eventListener = new ArrayList<>();
	
	public ReviewPanel(int contentId, boolean isMypage) {

		reviewDAO = new ReviewDAO(); // ReviewDAO 초기화
		setBackground(new Color(0, 0, 0, 0));
		setOpaque(false);
		setLayout(null);
		setBounds(isMypage ? -30 : 0, isMypage ? 0 : 15, 595, 350); // 리뷰 패널 전체 크기 설정

		// 콘텐츠 패널 (스크롤될 영역)
		contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setOpaque(false);
		contentPanel.setBackground(new Color(0, 0, 0, 0));
		//스크롤 가능 사이즈 변경 해줘야함!!!!
		contentPanel.setPreferredSize(new Dimension(595, 1200)); // 스크롤 가능한 크기 설정

		if(isLoggedIn && !isMypage){
			ItemVO findItem = DataManagers.getInstance().FindItemFromId(contentId);
			JPanel register = setInputRegisterContent(findItem);
			contentPanel.add(register);
		}
		else if(!isMypage){
			JPanel label = setNoLoginUser();
			contentPanel.add(label);
		}

		if(!isMypage) {
			//이 작품에 대한 리뷰 갯수 가져오기
			List<ReviewVO> findReview = DBDataManagers.getInstance().getContentReviewsData(contentId);

			if(findReview.isEmpty()){
				JLabel firstReview = setBaseReviewLabel("기념스러운 첫 리뷰를 작성해주세요!");
				firstReview.setBounds((595 - 510) / 2, paddingY,510,126);
				contentPanel.add(firstReview);
			}
			else {
				for (int i = 0; i < findReview.size(); i++) {
					reviewCreatedCount++;
					JLabel createReview = setReviewPerContent(findReview.get(i));
					createReview.setBounds((595 - 510) / 2, ((paddingY + ((i * perHeight) + (i * gapY)))),510,126);
					contentPanel.add(createReview);
				}
			}
		}
		else {
			//마이페이지일때
			List<ReviewVO> usersReviews = DBDataManagers.getInstance().getReviewerReviewsData(DataManagers.getInstance().getMyUser().getId());
			if(usersReviews.isEmpty()){
				JLabel firstReview = setBaseReviewLabel("기념스러운 첫 리뷰를 작성해주세요!");
				firstReview.setBounds((595 - 510) / 2, 0,510,126);
				contentPanel.add(firstReview);
			}
			else {
				for (int i = 0; i < usersReviews.size(); i++) {
					reviewCreatedCount++;
					JLabel createReview = setReviewPerContent(usersReviews.get(i));
					createReview.setBounds((595 - 510) / 2, (((i * perHeight) + (i * gapY))),510,126);
					contentPanel.add(createReview);
				}
			}
		}

		// JScrollPane 생성 및 설정
		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBounds(0, 15, 595, 350); // ReviewPanel과 동일한 크기
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setOpaque(false);

		// 스크롤 가능한 뷰포트 설정
		scrollPane.setViewportView(contentPanel);

		add(scrollPane);
	}


	private void addReview(ReviewVO review) {

		//DB 저장하기
		ReviewDAO reviewDAO = new ReviewDAO();
		// ReviewDAO에 리뷰 추가
		try {
			reviewDAO.addReviewToJson(review, AppConstants.REVIEW_FILE_NAME, AppConstants.FOLDER_ID);
		} catch (Exception e) {
			System.out.println("리뷰 등록을 실패 하였습니다." + e.getMessage());
		}

		//일단 로컬에만 저장하기
		JLabel createReview = setReviewPerContent(review);
		createReview.setBounds((595 - 510) / 2, ((paddingY + ((reviewCreatedCount * perHeight) + (reviewCreatedCount * gapY)))),510,126);
		contentPanel.add(createReview);
		reviewCreatedCount++;
	}

	private JPanel setInputRegisterContent(ItemVO itemVO) {
		int panelWidth = 510;
		int panelHeight = 126;

		// 리뷰 입력 패널
		JPanel registerContent = new JPanel();
		registerContent.setLayout(null);
		registerContent.setBounds((595 - panelWidth) / 2, 0, panelWidth, panelHeight);
		registerContent.setBackground(new Color(0, 0, 0, 0));
		registerContent.setOpaque(false);

		ImageIcon registerBG = DataManagers.getInstance().getIcon("registerBG", "detail_review_Page");
		JLabel bgBigInput = new JLabel(registerBG);
		bgBigInput.setLayout(null);
		bgBigInput.setBounds(0, 0, panelWidth, panelHeight); // 중앙 배치

		ImageIcon backgroundReviewInputImage = DataManagers.getInstance().getIcon("registerTextBox", "detail_review_Page");
		JLabel reviewBG = new JLabel(backgroundReviewInputImage);
		reviewBG.setLayout(null);
		reviewBG.setBounds(0, 0, panelWidth, 98); // 중앙 배치

		//텍스트 세팅
		//리뷰 입력란
		String setReviewText = "이 작품에 대한 내 평가를 남겨보세요!";
		reviewInput = new JTextArea(20, 40);
		reviewInput.setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		reviewInput.setFont(DataManagers.getInstance().getFont("regular", 15));
		reviewInput.setBounds(17, 17, 480, 80);
		reviewInput.setForeground(Color.decode(AppConstants.UI_TEXT_BOX_HEX));
		reviewInput.setText(setReviewText);

		reviewInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (reviewInput.getText().equals(setReviewText)) {
					reviewInput.setText("");
					reviewInput.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (reviewInput.getText().isEmpty()) {
					reviewInput.setText("이 작품에 대한 내 평가를 남겨보세요!");
					reviewInput.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
				}
			}
		});

		reviewTextCount = new JLabel(String.format("0/%d", AppConstants.MAXIMUM_REVIEW_TEXT));
		reviewTextCount.setFont(DataManagers.getInstance().getFont("bold", 15));
		reviewTextCount.setBounds(400,104,50,25);
		reviewTextCount.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));

		// 입력 감지하여 버튼 활성화
		reviewInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				toggleSubmitButton();
				updateReviewTextCount(e.getDocument().getLength());
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				toggleSubmitButton();
				updateReviewTextCount(e.getDocument().getLength());
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				toggleSubmitButton();
				updateReviewTextCount(e.getDocument().getLength());
			}
		});

		// 리뷰 내용 입력 길이 제한 (최대 120자, 두 줄)
		((AbstractDocument) reviewInput.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
				String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
				if (newText.length() <= AppConstants.MAXIMUM_REVIEW_TEXT) { // 최대 120자
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});

		registerContent.add(reviewTextCount);
		registerContent.add(reviewInput);

		//별점 세팅
		// 별점 선택을 위한 버튼 배열 초기화 #커스텀버튼화 해야함
		starButtons = new CustomButton[AppConstants.STAR_MAX_SCORE];
		int starSize = 16; // 버튼 크기
		int spacing = 4; // 간격
		int startX = 13; // 첫 번째 버튼 위치

		for (int i = 0; i < starButtons.length; i++) {
			starButtons[i] = new CustomButton("");
			final int rating = i + 1; // 별점 값 (1~5)

			registerContent.add(starButtons[i]);
			starButtons[i].setBounds(startX + (i * (starSize + spacing)), 104 , starSize, starSize); // 위치 지정

			starButtons[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					selectedRating = rating;
					updateStarButtons();
				}
			});
		}

		starCountText = new JLabel("0/5");
		starCountText.setFont(DataManagers.getInstance().getFont("bold", 15));
		starCountText.setBounds(113,104,50,25);
		starCountText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		registerContent.add(starCountText);

		updateStarButtons(); // 초기 상태 설정

		//등록 세팅
		ImageIcon submitBtn = DataManagers.getInstance().getIcon("registerBtn", "detail_review_Page");
		submitButton = new CustomButton(submitBtn);
		submitButton.setClickEffect(true,0.3f);
		submitButton.setBounds(445, 98, 65, 28);
		toggleSubmitButton();

		JLabel submitBtnText = new JLabel("등 록");
		submitBtnText.setFont(DataManagers.getInstance().getFont("bold", 17));
		submitBtnText.setBounds(460, 102, 65, 28);

		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//등록 버튼 클릭
				UserVO userVO = DataManagers.getInstance().getMyUser();
				ReviewVO reviewData = new ReviewVO(itemVO.getId(), userVO.getId(), reviewInput.getText(), selectedRating);
				reviewData.setReviewerName(userVO.getNickName());
				addReview(reviewData);


				for (int i = 0; i < eventListener.size(); i++) {
					eventListener.get(i).onReviewEvent();
				}
			}
		});

		registerContent.add(reviewBG);

		registerContent.add(submitBtnText);
		registerContent.add(submitButton);

		registerContent.add(bgBigInput);
		return registerContent;
	}

	private JLabel setReviewPerContent(ReviewVO vo){
		JLabel reviewPerContent = new JLabel();
		reviewPerContent.setLayout(null);

		JLabel bg = new JLabel(DataManagers.getInstance().getIcon("reivewContentBG", "detail_review_Page"));
		bg.setLayout(null);
		bg.setBounds(0,0, 510,126);

		//프로필
		JLabel profileLabel = new JLabel(DataManagers.getInstance().getIcon("profile01", "detail_review_Page"));
		profileLabel.setLayout(null);
		profileLabel.setBounds(33, 10, 75, 75);
		reviewPerContent.add(profileLabel);

		// 사용자명
		JLabel nameLabel = new JLabel(vo.getReviewName());
		nameLabel.setLayout(null);
		nameLabel.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		nameLabel.setBounds(144, 12, 333, 26);
		nameLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
		reviewPerContent.add(nameLabel);

		// 날짜
		JLabel dateLabel = new JLabel(vo.getReviewDate()); // 리뷰 작성 날짜 표시
		dateLabel.setLayout(null);
		dateLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		dateLabel.setBounds(144, 35, 156, 16);
		dateLabel.setFont(DataManagers.getInstance().getFont("regular", 10));
		reviewPerContent.add(dateLabel);

		// 리뷰 내용 패널
		JLabel contentLabel = new JLabel("<html>" + vo.getReviewContent() + "</html>"); // 실제 리뷰 내용 3줄까진 날짜 표기 O 4줄에서 날짜 표기가 사라짐.
		contentLabel.setLayout(null);
		contentLabel.setForeground(Color.decode(AppConstants.UI_SUB_TEXT_HEX));
		contentLabel.setBounds(144, 38, 347, 50);
		contentLabel.setFont(DataManagers.getInstance().getFont("bold", 13));
		reviewPerContent.add(contentLabel); // 리뷰내용 부착

		// 컨텐츠이름
		String findContentName = DataManagers.getInstance().FindItemFromId(vo.getContentId()).getTitle();
		JLabel contentNameLabel = new JLabel("#" + findContentName);
		contentNameLabel.setLayout(null);
		contentNameLabel.setBounds(144, 83, 270, 30);
		contentNameLabel.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		contentNameLabel.setFont(DataManagers.getInstance().getFont("bold", 11));
		reviewPerContent.add(contentNameLabel);

		// 별점 이미지
		JLabel scoreImageLabel = new JLabel(DataManagers.getInstance().getIcon("ratingIcon", "detail_review_Page"));
		scoreImageLabel.setLayout(null);
		scoreImageLabel.setBounds(58, 85, 27, 27);
		reviewPerContent.add(scoreImageLabel); // 별점 표시를 컨텐츠 패널에

		// 별점 불러와서 점수화.
		JLabel scoreTextLabel = new JLabel(String.valueOf(vo.getReviewAvgScore()));
		scoreTextLabel.setLayout(null);
		scoreTextLabel.setBounds(64, 107, 27, 27);
		scoreTextLabel.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		scoreTextLabel.setFont(DataManagers.getInstance().getFont("", 10));
		reviewPerContent.add(scoreTextLabel);

		// 좋아요 버튼
		CustomButton iineButton = new CustomButton(DataManagers.getInstance().getIcon("thumbUpOff", "detail_review_Page"));

		JLabel iineCountText = new JLabel(String.format("%d",vo.getIineCount()));
		// 좋아요 버튼에 좋아요 카운트를 텍스트로 표시
		iineCountText.setFont(DataManagers.getInstance().getFont("bold", 14));
		iineCountText.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		iineCountText.setBounds(455, 95, 71, 30);
		iineButton.addMouseListener(new MouseAdapter() {
			private int iineCount = vo.getIineCount();	//그냥 값 안됌!
			private boolean liked = false;

			@Override
			public void mouseClicked(MouseEvent e) {
				if (liked) {
					iineCount--; // 이미 좋아요가 눌렸다면 카운트 감소
					liked = false;
					iineButton.setIcon(DataManagers.getInstance().getIcon("thumbUpOff", "detail_review_Page"));
					iineCountText.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
				} else {
					iineCount++; // 좋아요를 누르면 카운트 증가
					liked = true;
					iineButton.setIcon(DataManagers.getInstance().getIcon("thumbUpOn", "detail_review_Page"));
					iineCountText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
				}

				// 좋아요 버튼에 좋아요 카운트를 텍스트로 표시
				iineCountText.setText(String.valueOf(iineCount));
			}
		});

		iineButton.setLayout(null);
		iineButton.setBounds(433, 90, 71, 30);

		reviewPerContent.add(iineButton); // 좋아요 버튼 추가
		reviewPerContent.add(iineCountText);

		reviewPerContent.add(bg);
		return reviewPerContent;
	}

	private JPanel setNoLoginUser(){
		int panelWidth = 510;
		int panelHeight = 126;

		JPanel reviewPerContent = new RoundedPanel(50,50);
		reviewPerContent.setLayout(null);
		reviewPerContent.setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		reviewPerContent.setBounds((595 - panelWidth) / 2, 0,panelWidth, panelHeight);

		JLabel reviewNameLabel = new JLabel("로그인 유저만 리뷰작성이 사용 가능해요!");
		reviewNameLabel.setLayout(null);
		reviewNameLabel.setForeground(Color.decode(AppConstants.UI_SUB_TEXT_HEX));
		reviewNameLabel.setFont(DataManagers.getInstance().getFont("bold", 19));
		reviewNameLabel.setBounds(110, 45, 333, 50);

		reviewPerContent.add(reviewNameLabel);

		return reviewPerContent;
	}

	private JLabel setBaseReviewLabel(String text){
		JLabel reviewPerContent = new JLabel();
		reviewPerContent.setLayout(null);

		JLabel bg = new JLabel(DataManagers.getInstance().getIcon("reivewContentBG", "detail_review_Page"));
		bg.setLayout(null);
		bg.setBounds(0,0, 510,126);

		JLabel reviewNameLabel = new JLabel(text);
		reviewNameLabel.setLayout(null);
		reviewNameLabel.setForeground(Color.decode(AppConstants.UI_SUB_TEXT_HEX));
		reviewNameLabel.setFont(DataManagers.getInstance().getFont("bold", 20));
		reviewNameLabel.setBounds(110, 45, 333, 50);

		reviewPerContent.add(reviewNameLabel);
		reviewPerContent.add(bg);

		return reviewPerContent;
	}

	// 별점 버튼 업데이트
	private void updateStarButtons() {
		for (int i = 0; i < starButtons.length; i++) {
			if (i < selectedRating) {
				starButtons[i].setIcon(DataManagers.getInstance().getIcon("ratingIconOn", "detail_review_Page"));
			} else {
				starButtons[i].setIcon(DataManagers.getInstance().getIcon("ratingIconOff", "detail_review_Page"));
			}
		}
		if(starCountText != null){
			starCountText.setText(String.format("%d/5", selectedRating));
		}
	}

	//텍스트 갯수 업데이트
	private void updateReviewTextCount(int count) {
		if(reviewTextCount != null){
			reviewTextCount.setText(String.format("%d/%d", count, AppConstants.MAXIMUM_REVIEW_TEXT));
		}
	}

	// 등록 버튼 활성화 조건 메서드
	private void toggleSubmitButton() {
		submitButton.setEnabled(reviewInput.getText().length() >= AppConstants.MINUMUM_REVIEW_TEXT);
	}

	// 리뷰 추가 메서드
	public void addReview(int contentId, String reviewerId, String content, double score) {

		ReviewVO vo = new ReviewVO(contentId, reviewerId, content, score);
		// ReviewDAO에 리뷰 추가
		try {
			reviewDAO.addReviewToJson(vo, AppConstants.REVIEW_FILE_NAME, AppConstants.FOLDER_ID);
		} catch (Exception e) {
			System.out.println("리뷰 등록을 실패 하였습니다." + e.getMessage());
		}
	}

	private void displayReview(String contentName, String reviewerName, String content, double score,
			String reviewDate) {
		 //리뷰 목록이 비어 있을 때 표시된 문구 제거

		int reviewCount = createdReiveContent.size(); // 현재 리뷰 개수
		int yPosition = reviewCount * 146; // 각 리뷰의 Y 위치 (간격 포함)
		
		//리뷰 리스트에서 각각의 리뷰가 담길 칸 ( 이 목표임 )
		JPanel reviewItem = new JPanel();
		reviewItem.setLayout(null);
		reviewItem.setBackground(Color.decode(AppConstants.UI_TEXT_BOX_HEX));
		reviewItem.setBounds(0, yPosition, 510, 126);

		// 프로필 이미지 담기 - OK
		ImageIcon profileIcon = (DataManagers.getInstance().getIcon("profile01", "detail_review_Page"));
		JLabel profileLabel = new JLabel(profileIcon);
		profileLabel.setLayout(null);
		profileLabel.setBounds(33, 14, 75, 75);
		reviewItem.add(profileLabel);

		// 이름과 날짜를 담을 패널
		JPanel topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setBounds(144, 9, 337, 41);

		// 사용자명
		JLabel nameLabel = new JLabel(reviewerName);
		nameLabel.setLayout(null);
		nameLabel.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		nameLabel.setBounds(2, 2, 333, 26);
		nameLabel.setFont(DataManagers.getInstance().getFont("", 14));
		topPanel.add(nameLabel);

		// 날짜
		JLabel dateLabel = new JLabel(reviewDate); // 리뷰 작성 날짜 표시
		dateLabel.setLayout(null);
		dateLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		dateLabel.setBounds(2, 24, 156, 16);
		dateLabel.setFont(DataManagers.getInstance().getFont("bold", 10));
		topPanel.add(dateLabel);

		// 리뷰 내용 패널
		JLabel contentLabel = new JLabel("<html>" + content + "</html>"); // 실제 리뷰 내용 3줄까진 날짜 표기 O 4줄에서 날짜 표기가 사라짐.
		contentLabel.setLayout(null);
		contentLabel.setForeground(Color.decode(AppConstants.UI_SUB_TEXT_HEX));
		contentLabel.setBounds(144, 42, 347, 50);
		contentLabel.setFont(DataManagers.getInstance().getFont("bold", 10));
		reviewItem.add(contentLabel); // 리뷰내용 부착

		// 컨텐츠이름
		JLabel contentNameLabel = new JLabel("#" + contentName);
		contentNameLabel.setLayout(null);
		contentNameLabel.setBounds(144, 87, 270, 30);
		contentNameLabel.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		contentNameLabel.setFont(DataManagers.getInstance().getFont("bold", 10));
		reviewItem.add(contentNameLabel);

		// 별점 이미지
		JLabel scoreImageLabel = new JLabel(DataManagers.getInstance().getIcon("ratingIcon", "detail_review_Page"));
		scoreImageLabel.setLayout(null);
		scoreImageLabel.setBounds(58, 91, 27, 27);
		reviewItem.add(scoreImageLabel); // 별점 표시를 컨텐츠 패널에

		// 별점 불러와서 점수화.
		JLabel scoreTextLabel = new JLabel(String.valueOf(score));
		scoreTextLabel.setLayout(null);
		scoreTextLabel.setBounds(63, 110, 27, 27);
		scoreTextLabel.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		scoreTextLabel.setFont(DataManagers.getInstance().getFont("", 10));
		reviewItem.add(scoreTextLabel);

		// 좋아요 버튼
		CustomButton iineButton = new CustomButton(
				DataManagers.getInstance().getIcon("thumbUpOff", "detail_review_Page"));
		iineButton.addMouseListener(new MouseAdapter() {
			private int iineCount = 0;
			private boolean liked = false;

			@Override
			public void mouseClicked(MouseEvent e) {
				if (liked) {
					iineCount--; // 이미 좋아요가 눌렸다면 카운트 감소
					liked = false;
					iineButton.setIcon(DataManagers.getInstance().getIcon("thumbUpOff", "detail_review_Page")); 
																												
				} else {
					iineCount++; // 좋아요를 누르면 카운트 증가
					liked = true;
					iineButton.setIcon(DataManagers.getInstance().getIcon("thumbUpOn", "detail_review_Page")); 
																																																	
				}

				// 좋아요 버튼에 좋아요 카운트를 텍스트로 표시
				iineButton.setFont(DataManagers.getInstance().getFont("bold", 14));
				iineButton.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
				iineButton.setText("" + iineCount);
			}
		});
		iineButton.setLayout(null);
		iineButton.setBounds(433, 95, 71, 30);
		reviewItem.add(iineButton); // 좋아요 버튼 추가

		// 리뷰 아이템에 컴포넌트 추가
		reviewItem.add(topPanel); // 이름과 날짜 패널
	}
}