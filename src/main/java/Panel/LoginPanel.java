package Panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Component.CustomButton;
import Frame.FrameBase;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.UserVO;

public class LoginPanel extends JPanel {

	protected static final JOptionPane OptionPane = null;

	public LoginPanel() {

		setBackground(Color.decode("#404153"));
		setLayout(null);
		setBounds(0, 0, 595, 575);

		JLabel hiWelcome = new JLabel("안녕하세요?");
		hiWelcome.setBounds(63, 28, 461, 85);
		hiWelcome.setFont(DataManagers.getInstance().getFont("", 30));
		hiWelcome.setForeground(Color.decode("#78DBA6"));
		add(hiWelcome);
		JLabel hiWelcome2 = new JLabel("다시 돌아온 것을 환영합니다.");
		hiWelcome2.setBounds(63, 57, 461, 85);
		hiWelcome2.setFont(DataManagers.getInstance().getFont("Medium", 30));
		hiWelcome2.setForeground(Color.decode("#78DBA6"));
		add(hiWelcome2);

		JLabel login = new JLabel("로그인");
		login.setBounds(63, 155, 332, 42);
		login.setFont(DataManagers.getInstance().getFont("", 24));
		login.setForeground(Color.decode("#CBCBCB"));
		add(login);

		JLabel idBG = new JLabel();
		idBG.setIcon(DataManagers.getInstance().getIcon("loginIdTextBox", "login_Page"));
		idBG.setBounds(63, 206, 461, 43);

		JTextField idTextField = new JTextField("아이디를 입력해주세요.");
		idTextField.setBounds(20, 8, 350, 35);
		idTextField.setFont(DataManagers.getInstance().getFont("", 13));
		idTextField.setForeground(Color.decode("#CBCBCB"));
		idTextField.setOpaque(false);
		idTextField.setBorder(null);

		// 입력하면 글자 색 바꾸기 입력한 텍스트는 원래 색상 복구
		idTextField.addFocusListener(new java.awt.event.FocusListener() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				// 기본 텍스트가 있는지 확인하고 삭제
				if (idTextField.getText().equals("아이디를 입력해주세요.")) {
					idTextField.setText(""); // 기본 텍스트 지우기
					idTextField.setForeground(Color.BLACK); // 텍스트 색상 검은색으로 변경
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				// 텍스트 필드가 비어 있으면 기본 텍스트를 복원
				if (idTextField.getText().isEmpty()) {
					idTextField.setText("아이디를 입력해주세요."); // 기본 텍스트 설정
					idTextField.setForeground(Color.decode("#CBCBCB")); // 텍스트 색상 회색으로 변경
				}
			}
		});

		idBG.add(idTextField);

		JLabel pwBG = new JLabel();
		pwBG.setIcon(DataManagers.getInstance().getIcon("loginIdTextBox", "login_Page"));
		pwBG.setBounds(63, 291, 461, 43);

		JPasswordField pwTextField = new JPasswordField();
		pwTextField.setBounds(20, 8, 350, 35);
		pwTextField.setFont(DataManagers.getInstance().getFont("", 13));
		pwTextField.setForeground(Color.decode("#CBCBCB"));

		// ✅ 플레이스홀더 상태를 위해 별도 변수 추가
		boolean[] isPlaceholder = { true }; // 배열을 사용해 final처럼 활용

		pwTextField.setText("비밀번호를 입력해주세요."); // 기본 플레이스홀더 설정
		pwTextField.setEchoChar((char) 0); // 텍스트 그대로 보이게 설정

		pwTextField.setOpaque(false);
		pwTextField.setBorder(null);

		pwTextField.addFocusListener(new java.awt.event.FocusListener() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (isPlaceholder[0]) { // 플레이스홀더 상태인지 확인
					pwTextField.setText(""); // 플레이스홀더 제거
					pwTextField.setEchoChar('●'); // 입력 시 ● 표시
					pwTextField.setForeground(Color.BLACK);
					isPlaceholder[0] = false;
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (pwTextField.getPassword().length == 0) { // 비밀번호가 없으면
					pwTextField.setText("비밀번호를 입력해주세요."); // 플레이스홀더 복구
					pwTextField.setEchoChar((char) 0); // 텍스트 그대로 보이게 설정
					pwTextField.setForeground(Color.decode("#CBCBCB"));
					isPlaceholder[0] = true;
				}
			}
		});

		pwBG.add(pwTextField);

		add(pwBG);
		add(idBG);

		// 로그인 버튼
		CustomButton loginButton = new CustomButton(
				DataManagers.getInstance().getIcon("longLoginButton", "login_Page"));
		loginButton.setClickEffect(true, 0.3f);
		loginButton.setBounds(63, 423, 461, 43);

		JLabel loginButtonText = new JLabel("로그인");
		loginButtonText.setBounds(265, 430, 461, 43);
		loginButtonText.setFont(DataManagers.getInstance().getFont("", 20));
		loginButtonText.setForeground(Color.decode("#595959"));

		add(loginButtonText);
		add(loginButton);

		// 회원가입 버튼
		JButton signUpButton = new JButton("회원가입");
		signUpButton.setFont(DataManagers.getInstance().getFont("", 17));
		signUpButton.setBounds(195, 481, 202, 39);
		signUpButton.setForeground(Color.decode("#78DBA6")); // 버튼 컬러
		signUpButton.setContentAreaFilled(false); // 버튼 배경 없애기
		signUpButton.setBorderPainted(false); // 버튼 테두리 없앰
		add(signUpButton);

		// 로그인 버튼 클릭 이벤트
		loginButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// ID 및 비밀번호 존재 여부 확인
				boolean isIdExist = isIDExists(idTextField.getText().trim());
				// boolean isPwExist = isPWExists(pwTextField.getPassword());

				if (isIdExist) {
					String findId = idTextField.getText().trim();
					String inputPassword = new String(pwTextField.getPassword()); // 입력된 비밀번호를 String으로 변환
					for (UserVO user : DBDataManagers.getInstance().getDbUsersData()) {
						// null 체크 추가함
						if (user != null && user.getId() != null && user.getPassword() != null
								&& user.getId().equals(findId) && user.getPassword().equals(inputPassword)) {
							System.out.println("로그인 성공");

							// 로그인된 사용자 정보 저장
							DataManagers.getInstance().setMyUser(user);

							// 로그인 성공 후 텍스트 필드 초기화
							idTextField.setText("");
							pwTextField.setText("");

							// To Do 로그인 성공 로직 구현
							// 모든 상,중,하 패널 로그인 상태로 최신화
							FrameBase frameBase;
							try {
								frameBase = FrameBase.getInstance();
								frameBase.setInnerPanel(new TopNavBar(), "up"); // 상단 바 갱신
								frameBase.setInnerPanel(new MainPagePanel(), "mid"); // 메인 패널 갱신
								frameBase.setInnerPanel(new BottomNavBar("home"), "down"); // 하단 바 갱신
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							return;
						}
					}
				}

				JOptionPane.showMessageDialog(null, "입력된 정보가 올바르지 않습니다");
			}
		});

		// 회원가입 버튼 클릭 시, 회원가입 화면으로 전환
		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// SignUp
				SignUPPanel panel = new SignUPPanel();
				// 스크롤 추가
				JScrollPane signUpScroll = new JScrollPane(panel);
				signUpScroll.setBounds(0, 0, 600, 800);
				signUpScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				signUpScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

				try {
					FrameBase frameBase = FrameBase.getInstance();
					frameBase.setInnerPanel(signUpScroll, "mid");
				} catch (Exception t) {
					System.out.println("로그인 페이지 로드 실패 :" + t.getMessage());
				}
			}
		});

	}

	//
	public boolean isIDExists(String Id) {
		boolean isExis = false;
		for (UserVO iDvon : DBDataManagers.getInstance().getDbUsersData()) {
			// null 체크 추가함
			if (iDvon != null && iDvon.getId() != null && iDvon.getId().equals(Id))
				isExis = true;
		}
		return isExis;
	}

	@SuppressWarnings("unlikely-arg-type")
	public boolean isPWExists(char[] cs) {
		boolean isPWis = false;
		for (UserVO PWvon : DBDataManagers.getInstance().getDbUsersData()) {
			// null 체크 추가함
			if (PWvon != null && PWvon.getPassword() != null && PWvon.getPassword().equals(new String(cs)))
				isPWis = true;
		}
		return isPWis;
	}
}
