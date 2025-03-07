package Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Component.CustomButton;
import DAO.SignUPDAO;
import Data.AppConstants;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.FavoriteVO;
import VO.UserVO;

public class MyPagePanel extends JPanel {

	 private CustomButton myReviewHistory;
	 private CustomButton myReviewHistoryBar;
	 private CustomButton myReviewBtn;
	 private CustomButton myiineHistory;
	 private CustomButton myiineHistoryBar;
	 private CustomButton myiineBtn;
	// 위에 //버튼 //밑에
	public MyPagePanel() {

		// MID_PANEL의 공간 정의
		setLayout(null);
		setPreferredSize(new Dimension(AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT));
		setBounds(0, 0, AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT);
		
		// 위쪽 패널 닉네임 변경하는 쪽
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(null);
		firstPanel.setBackground(Color.decode("#404153"));
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
		mypageNicknameEdit.setFont(DataManagers.getInstance().getFont("", 17));
		mypageNicknameEdit.setForeground(Color.decode("#78DBA6"));
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
						signUpDAO.addUserToJson(loggedInUser, AppConstants.USER_FILE_NAME, AppConstants.FOLDER_ID);
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
		btnPanel.setBackground(Color.decode("#404153"));
		btnPanel.setBounds(0, 217, 595, 71);

		//해야하는거 리뷰내역이랑 리뷰내역 밑에 바를 같은 버튼으로 만들기

		// 리뷰내역 버튼
		myReviewHistory = new CustomButton("리뷰내역");
		myReviewHistory.setForeground(Color.decode("#BABABC"));
		myReviewHistory.setFont(DataManagers.getInstance().getFont("", 24));
		myReviewHistory.setBounds(342, 37, 114, 35);
		myReviewHistory.setVerticalAlignment(SwingConstants.CENTER);

		// 리뷰내역 텍스트 밑 버튼 줄 
		myReviewHistoryBar = new CustomButton(DataManagers.getInstance().getIcon("barOff", "detail_Content_Page"));
		myReviewHistoryBar.setBounds(336, 68, 100, 3);
		myReviewHistoryBar.setOpaque(false);
		myReviewHistoryBar.setBorder(null);
		
		//리뷰내역과 바를 가리는 투명 버튼
		myReviewBtn = new CustomButton("");
		myReviewBtn.setBounds(335, 0, 114, 71);
		myReviewBtn.setBackground(new Color(0, 0, 0, 0));
		myReviewBtn.setOpaque(true);
		
		myReviewBtn.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	ChangeButtonColor(myReviewHistory, myReviewHistoryBar, true);
		    	ChangeButtonColor(myiineHistory, myiineHistoryBar, false);
		    }
		});
        
		// 찜 리스트 버튼
		myiineHistory = new CustomButton("찜 리스트");
		myiineHistory.setForeground(Color.decode("#78DBA6"));
		myiineHistory.setFont(DataManagers.getInstance().getFont("", 24));
		myiineHistory.setBounds(154, 37, 114, 35);
		myiineHistory.setVerticalAlignment(SwingConstants.CENTER);

		//
		//찜 리스트 텍스트 밑 버튼 줄 
		myiineHistoryBar = new CustomButton(DataManagers.getInstance().getIcon("barOn", "detail_Content_Page"));
		myiineHistoryBar.setBounds(129, 68, 100, 3);
		myiineHistoryBar.setOpaque(false);
		myiineHistoryBar.setBorder(null);
		
		//찜 리스트와 바를 가리는 투명 버튼
		myiineBtn = new CustomButton("");
		myiineBtn.setBounds(130, 0, 114, 71);
		myiineBtn.setBackground(new Color(0, 0, 0, 0));
		myiineBtn.setOpaque(true);
		
		myiineBtn.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	ChangeButtonColor(myReviewHistory, myReviewHistoryBar, false);
		    	ChangeButtonColor(myiineHistory, myiineHistoryBar, true);
		    	
//		    	DataManagers.getInstance().getMyUser().getId().(DBDataManagers.getInstance().getDbFavoriteData())
		    	for(FavoriteVO favoriteVO : (DBDataManagers.getInstance().getDbFavoriteData())) {
		    		if(favoriteVO.getUserId() == DataManagers.getInstance().getMyUser().getId()) {
		    			System.out.println(favoriteVO.getMyFavoriteList());
		    		}
		    	}
		    }
		});
		btnPanel.add(myiineBtn);
		btnPanel.add(myReviewBtn);
		btnPanel.add(myiineHistoryBar);
		btnPanel.add(myReviewHistoryBar);
		btnPanel.add(myiineHistory);
		btnPanel.add(myReviewHistory);
		add(btnPanel);
		
		//맨 밑 기본 패널
		JPanel listPanel = new JPanel();
		listPanel.setLayout(null);
		listPanel.setBackground(Color.decode("#BABABC"));
		listPanel.setBounds(0,231,595,318);
		
		
		mypageGreenBarnagai.setIcon(DataManagers.getInstance().getIcon("bar", "myPage_Page"));
		//맨 밑 찜 리스트 패널
		JLabel myiineListPanel = new JLabel();
		myiineListPanel.setIcon(DataManagers.getInstance().getIcon("ggimContentBG", "myPage_Page"));
		myiineListPanel.setBounds(30, 80, 565, 230);
	//30
	
		listPanel.add(myiineListPanel);
		add(listPanel);
	}

	private void ChangeButtonColor(CustomButton textButton, CustomButton bar, boolean isOn) {
		textButton.setForeground(isOn == true ? Color.decode("#78DBA6") : Color.decode("#BABABC"));
		bar.setIcon((DataManagers.getInstance().getIcon( isOn == true ? "barOn" : "barOff", "detail_Content_Page")));
	}

}
