package Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Component.CustomButton;
import DAO.ReviewDAO;
import Data.AppConstants;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.ReviewVO;

public class MyPageReviewPanel extends JPanel {
	
		private JPanel reviewListPanel;
		private ReviewDAO reviewDAO; // ReviewDAO 추가
		private String reviewDate;
		private JScrollPane scrollPane;
		// 테스트용 필드 연결 완료 후 해당하는 컨텐츠명, 로그인 판별식, 유저명으로 연결.
		private String contentName = "아는 영화"; // 영화 제목 테스트용.
		private String reviewName = "테스트 유저"; // 테스트용 유저 이름
		private int contentId = 1234; // 테스트용 컨텐츠 ID
		private String reviewerId = "5555"; // 테스트용 리뷰어 ID

		public MyPageReviewPanel() {
			
			reviewDAO = new ReviewDAO(); // ReviewDAO 초기화
			setLayout(null);
			setBackground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
			setBounds(0, 316, AppConstants.PANEL_MID_WIDTH, 337); // 리뷰 패널 전체 위치 조정할 때는 여기로.

			reviewListPanel = new JPanel();
			reviewListPanel.setLayout(null);
			reviewListPanel.setBounds(37, 30, 510, 300);
			reviewListPanel.setBackground(new Color(0xD9D9D9));
			reviewListPanel.setPreferredSize(new Dimension(510, 0)); // 크기 명시

			
			JScrollPane scrollPane = new JScrollPane(reviewListPanel);
			scrollPane.setBounds(26, 20, 540, 200); // 크기 조정
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.getVerticalScrollBar().setUnitIncrement(20);
			scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
			scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
			
			 add(scrollPane);
			
			// 기존 리뷰 데이터 로드
			loadReviews(reviewerId); // 리뷰어명에 따라 리뷰 목록 받아오기.
			checkReviewList(); // 초기 리뷰 목록 상태 확인
		}

		// 리뷰 목록이 비어 있는지 확인하고 문구 표시
		private void checkReviewList() {
			if (reviewListPanel.getComponentCount() == 0) {
				JLabel noReviewLabel = new JLabel("기념스러운 첫 리뷰를 작성해주세요!");
				noReviewLabel.setLayout(null);
				noReviewLabel.setBounds(153, 62, 300, 100);
				noReviewLabel.setFont(DataManagers.getInstance().getFont("bold", 14));
				noReviewLabel.setBackground(Color.decode(AppConstants.UI_TEXT_BOX_HEX));
				reviewListPanel.add(noReviewLabel);
				reviewListPanel.revalidate();
				reviewListPanel.repaint();

			}
		}

		//리뷰 출력 메서드
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
			reviewItem.setBounds(10, 8 + yPosition, 510, 126);

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
			JLabel nameLabel = new JLabel(DBDataManagers.getInstance().getReviewerNameById(reviewerId));
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
			dateLabel.setFont(DataManagers.getInstance().getFont("", 10));
			topPanel.add(dateLabel);

			// 리뷰 내용 패널
			JLabel contentLabel = new JLabel("<html>" + content + "</html>"); // 실제 리뷰 내용 3줄까진 날짜 표기 O 4줄에서 날짜 표기가 사라짐.
			contentLabel.setLayout(null);
			contentLabel.setForeground(Color.decode(AppConstants.UI_SUB_TEXT_HEX));
			contentLabel.setBounds(144, 40, 347, 50);
			reviewItem.add(contentLabel); // 리뷰내용 부착

			// 컨텐츠이름
			JLabel contentNameLabel = new JLabel("#" + contentName);
			contentNameLabel.setLayout(null);
			contentNameLabel.setBounds(144, 87, 270, 30);
			contentNameLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
			contentNameLabel.setFont(DataManagers.getInstance().getFont("", 10));
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
			scoreTextLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
			scoreTextLabel.setFont(DataManagers.getInstance().getFont("", 10));
			reviewItem.add(scoreTextLabel);

			// 좋아요 버튼
			CustomButton iineButton = new CustomButton(
					DataManagers.getInstance().getIcon("thumbUpOff", "detail_review_Page"));
			iineButton.addMouseListener(new MouseAdapter() {
				private int iineCount = 0;
				private boolean liked = false;

				@Override
				public void mouseClicked (MouseEvent e) {
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
					iineButton.setFont(DataManagers.getInstance().getFont(" ", 14));
					iineButton.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
					iineButton.setText("" + iineCount);
				}
			});
			iineButton.setLayout(null);
			iineButton.setBounds(433, 95, 71, 30);
			reviewItem.add(iineButton); // 좋아요 버튼 추가

			// 리뷰 아이템에 컴포넌트 추가
			reviewItem.add(topPanel); // 이름과 날짜 패널

			reviewListPanel.add(reviewItem);

			// 리뷰 목록 크기 업데이트 (스크롤 활성화)
			int newHeight = (reviewCount + 1) * 146; // 추가된 리뷰 고려
			reviewListPanel.setPreferredSize(new Dimension(510, newHeight));

			reviewListPanel.revalidate();
			reviewListPanel.repaint();
		}

		
		// 리뷰 불러오기 (현재 접속중인 유저 ID로 받아오기)
		private void loadReviews(String reviewerId) {
			reviewListPanel.removeAll(); // 기존 리뷰 초기화

			if(DBDataManagers.getInstance().getReviewerReviewsData(reviewerId) != null) {
				for (ReviewVO review : DBDataManagers.getInstance().getReviewerReviewsData(reviewerId)) {
					System.out.println("review : " + review.getContentName());
					
					displayReview(review.getContentName(), review.getReviewName(), review.getReviewContent(),
							review.getReviewScore(), review.getReviewDate());
				}
			}
			
			
			
			reviewListPanel.revalidate();
			reviewListPanel.repaint();
			//add(reviewListPanel);
		}
	}
