package Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import Component.CustomButton;
import DAO.ReviewDAO;
import Data.AppConstants;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.ReviewVO;

public class ReviewPanel extends JPanel {
	private JPanel reviewListPanel;
	private JTextArea reviewInput;
	private JButton[] starButtons; // 별점 선택을 위한 버튼 배열
	private int selectedRating = 0; // 기본 평점 0점
	private ReviewDAO reviewDAO; // ReviewDAO 추가
	private String reviewDate;
	private CustomButton submitButton;
	private JScrollPane scrollPane;

	// 테스트용 필드 연결 완료 후 해당하는 컨텐츠명, 로그인 판별식, 유저명으로 연결.
	private String contentName = "아는 영화"; // 영화 제목 테스트용.
	private String reviewerName = "테스트 유저"; // 테스트용 유저 이름
	private int contentId = 12343; // 테스트용 컨텐츠 ID
	private String reviewerId = "5555"; // 테스트용 리뷰어 ID
	private boolean isLoggedIn = true; // 로그인 여부 (테스트용)

	public ReviewPanel() {

		reviewDAO = new ReviewDAO(); // ReviewDAO 초기화
		setLayout(null);
		setBackground(new Color(0x404153));
		setBounds(0, 316, AppConstants.PANEL_MID_WIDTH, 337); // 리뷰 패널 전체 위치 조정할 때는 여기로.

		// 상단 리뷰 입력 영역 (로그인한 경우만 표시)
		JPanel inputPanel = new JPanel();
		ImageIcon backgroundReviewInputImage = DataManagers.getInstance().getIcon("registerTextBox", "detail_review_Page");
		inputPanel.setLayout(null);
		
		inputPanel.setBounds(36, 10, 510, 126);
		JLabel backgroundReviewLabel = new JLabel(backgroundReviewInputImage); // #백그라운드 이미지
		backgroundReviewLabel.setBounds(0, -20, 510, 126);
		inputPanel.setBorder(new LineBorder(Color.black,1,true));
		inputPanel.setOpaque(true);
		inputPanel.add(backgroundReviewLabel);

		// 리뷰 입력 버튼과 별점 선택을 담을 패널
		JPanel reviewButtonStarPanel = new JPanel();
		reviewButtonStarPanel.setLayout(null);
		reviewButtonStarPanel.setBounds(0, 91, 510, 35);

		//리뷰 입력란
		reviewInput = new JTextArea(20, 40);
		reviewInput.setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		reviewInput.setFont(DataManagers.getInstance().getFont("regular", 12));
		reviewInput.setBounds(0, 0, 510, 92);

		// 등록 버튼
		ImageIcon submitIcon = DataManagers.getInstance().getIcon("registerBtn", "detail_review_Page");
		submitButton = new CustomButton(submitIcon);
		submitButton.setFont(DataManagers.getInstance().getFont("", 10));
		submitButton.setBounds(440, 4, 65, 28);
		submitButton.setEnabled(false);
		submitButton.setClickEffect(true, 0.3f);

		// 등록 버튼의 등록 텍스트

		JLabel submitText = new JLabel("등 록");
		submitText.setFont(DataManagers.getInstance().getFont("bold", 12));
		submitText.setLayout(null);
		submitText.setBounds(20, 7, 49, 21);
		submitButton.add(submitText);

		// 별점 선택을 위한 버튼 배열 초기화 #커스텀버튼화 해야함
		starButtons = new JButton[AppConstants.STAR_MAX_SCORE];
		JLabel starLabel = new JLabel(); // 별점 버튼을 한 줄로 배치
		setPreferredSize(new Dimension(150, 30));
		starLabel.setLayout(null);
		int starSize = 25; // 버튼 크기
		int spacing = 1; // 간격
		int startX = 46; // 첫 번째 버튼 위치

		starButtons = new JButton[5]; // 5점 만점

		for (int i = 0; i < starButtons.length; i++) {
			starButtons[i] = new JButton();
			starButtons[i].setFont(DataManagers.getInstance().getFont("", 10));
			starButtons[i].setBackground(Color.decode(AppConstants.UI_TEXT_BOX_HEX));
			starButtons[i].setBorderPainted(false);

			final int rating = i + 1; // 별점 값 (1~5)

			starButtons[i].setBounds(startX + (i * (starSize + spacing)), 106, starSize, starSize); // 위치 지정

			starButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedRating = rating;
					updateStarButtons();
				}
			});
			add(starButtons[i]); // 패널에 추가
		}

		updateStarButtons(); // 초기 상태 설정

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

		// reviewButtonStarPanel에 리뷰 입력 버튼과 별점 라벨 추가
		reviewButtonStarPanel.add(starLabel);// 별점
		reviewButtonStarPanel.add(submitButton); // 리뷰 등록버튼

		// inputPanel에 reviewButtonStarPanel과 reviewInput 추가
		inputPanel.add(reviewButtonStarPanel);
		inputPanel.add(reviewInput);

		// 입력 감지하여 버튼 활성화
		reviewInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				toggleSubmitButton();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				toggleSubmitButton();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				toggleSubmitButton();
			}
		}); // 등록 버튼 상호 작용 ( 작품명 / 사용자명 / 리뷰 내용 / 등록 평점 입력됨 )
		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addReview(contentId, reviewerId, reviewInput.getText(), selectedRating); //

				reviewInput.setText("");
				submitButton.setEnabled(false);
				
				
				checkReviewList(); // 리뷰 추가 후 리뷰 목록 상태 확인
			}
		});

		reviewListPanel = new JPanel();
		reviewListPanel.setLayout(null);
		reviewListPanel.setBounds(43, 300, 510, 300);
		reviewListPanel.setPreferredSize(new Dimension(510, 0)); // 크기 명시

		// JScrollPane 생성 및 reviewListPanel 부착 ##스크롤 수정해야함 .. 전체 패널에 좀 붙어라 제발..
		JScrollPane scrollPane = new JScrollPane(reviewListPanel);
		scrollPane.setBounds(26, 138, 540, 80); // 크기 조정
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JLabel pleaseLogIn = new JLabel("로그인 후 리뷰를 작성할 수 있습니다.");
		pleaseLogIn.setFont(DataManagers.getInstance().getFont("bold", 16));

		if (isLoggedIn) { // 로그인 여부 체크
			add(inputPanel);
		} else {
			add(pleaseLogIn);
			pleaseLogIn.setBounds(36, 10, 510, 161);
		}

		setStarButtonsVisibility(); // 로그인 상태에 따른 별점 버튼 표시 및 비활성화 처리

		add(scrollPane);

		// 기존 리뷰 데이터 로드
		loadReviews(contentId); // 영화 이름에 따라 리뷰 목록 받아오기.
		checkReviewList(); // 초기 리뷰 목록 상태 확인
	}

	// 리뷰 목록이 비어 있는지 확인하고 문구 표시
	public void checkReviewList() {
		if (reviewListPanel.getComponentCount() == 0) {
			
			JLabel noReviewLabel = new JLabel("기념스러운 첫 리뷰를 작성해주세요!", SwingConstants.CENTER);
			noReviewLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
			noReviewLabel.setBounds(0, 0, 250, 50);
			noReviewLabel.setForeground(Color.GRAY);
			reviewListPanel.add(noReviewLabel);
			reviewListPanel.revalidate();
			reviewListPanel.repaint();	
		}else {
			reviewListPanel.removeAll();
			loadReviews(contentId);
		}
		reviewListPanel.revalidate();
	    reviewListPanel.repaint();
	}

	// 별점 버튼 업데이트

	private void updateStarButtons() {
		for (int i = 0; i < starButtons.length; i++) {
			if (i < selectedRating) {
				starButtons[i].setIcon(DataManagers.getInstance().getIcon("ratingIcon", "detail_review_Page")); // 선택된
																												// 별점
			} else {
				starButtons[i].setIcon(DataManagers.getInstance().getIcon("ratingIconOff", "detail_review_Page")); // 미선택된
																													// 별점
			}
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

		// 화면에 리뷰 표시
		displayReview(contentName, reviewerName, content, score, reviewDate);
	}

	private void displayReview(String contentName, String reviewerName, String content, double score,
			String reviewDate) {
		// 리뷰 목록이 비어 있을 때 표시된 문구 제거
		if (reviewListPanel.getComponentCount() == 1 && reviewListPanel.getComponent(0) instanceof JLabel) {
			reviewListPanel.removeAll();
			checkReviewList();
		}

		int reviewCount = reviewListPanel.getComponentCount(); // 현재 리뷰 개수
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

		// 리뷰 목록에 추가
		reviewListPanel.add(reviewItem);

		// 리뷰 목록 크기 업데이트 (스크롤 활성화)
		int newHeight = (reviewCount + 1) * 146; // 추가된 리뷰 고려
		reviewListPanel.setPreferredSize(new Dimension(510, newHeight));

		reviewListPanel.revalidate();
		reviewListPanel.repaint();
	}

	// 별점 버튼의 상태를 설정하는 메서드
	private void setStarButtonsVisibility() {
		for (JButton starButton : starButtons) {
			starButton.setVisible(isLoggedIn); // 로그인된 경우 별점 버튼을 보이게 함
			starButton.setEnabled(isLoggedIn); // 로그인된 경우 클릭 가능하게 활성화
		}
	}

	// 리뷰 불러오기 (해당하는 영화 이름)
	private void loadReviews(int contentId) {
		reviewListPanel.removeAll(); // 기존 리뷰 초기화

		if(DBDataManagers.getInstance().getContentReviewsData(contentId) == null) return;

		// DBDataManagers에서 해당 영화에 대한 리뷰를 불러옴
		for (ReviewVO review : DBDataManagers.getInstance().getContentReviewsData(contentId)) {
			// 각 리뷰 항목을 화면에 표시
			displayReview(review.getContentName(), review.getReviewName(), review.getReviewContent(),
					review.getReviewScore(), review.getReviewDate());
		}

		reviewListPanel.revalidate();
		reviewListPanel.repaint();
	}
}