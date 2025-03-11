package Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import Component.CustomButton;
import DAO.SignUPDAO;
import Data.AppConstants;
import Frame.FrameBase;
import Managers.DataManagers;
import VO.UserVO;

public class SignUPPanel extends JPanel {
	private JTextField id, email, myNum, myNum7, phoneNum, nickName;
	private JPasswordField pw, pwCheck;
	private JLabel errorLabel;
	private JComboBox<String> nationalityBox;
	private boolean isIdChecked = false;
	private boolean isNicknameChecked = false;

	// 이메일 조건
	private boolean isValidEmail(String email) {
		return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	}

	public SignUPPanel() {

		SignUPDAO signUPDAO = new SignUPDAO();
		setLayout(null);
		setBackground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
		setPreferredSize(new Dimension(700, 900));
		// addNationalityDropdown();

		JLabel SignUp = new JLabel("회원가입");
		SignUp.setBounds(83, 28, 461, 85);
		SignUp.setFont(DataManagers.getInstance().getFont("bold", 30));
		SignUp.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
		add(SignUp);

		// id입력라벨
		JLabel idBG = new JLabel();
		idBG.setBounds(83, 110, 361, 37);
		idBG.setIcon(DataManagers.getInstance().getIcon("textBox01", "signUp_Page"));

		// id텍스트필드
		id = createPlaceholderTextField("아이디를 입력해주세요.", 110, 115, 361, 37);
		id.setFont(DataManagers.getInstance().getFont("bold", 13));
		id.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		id.setOpaque(false);
		id.setBorder(null);
		add(id);
		add(idBG);

		// ID중복 확인 버튼 라벨 JLabel
		JLabel checkIDButtonBG = new JLabel("중복확인");
		checkIDButtonBG.setBounds(484, 114, 101, 37);
		checkIDButtonBG.setFont(DataManagers.getInstance().getFont("bold", 14));
		checkIDButtonBG.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));

		// ID 중복 확인 버튼
		CustomButton checkIDButton = new CustomButton(
				DataManagers.getInstance().getIcon("signUpCheckBtn", "signUp_Page"));
		checkIDButton.setBounds(455, 110, 101, 37);
		checkIDButton.setOpaque(false);
		checkIDButton.setBorder(null);

		add(checkIDButtonBG);
		add(checkIDButton);
		checkIDButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String inputID = id.getText().trim();
				if (!inputID.matches("^[a-zA-Z0-9]+$")) {
					JOptionPane.showMessageDialog(null, "ID에는 특수문자를 포함할 수 없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
					id.setText(""); // 입력 필드 초기화
					return;
				}
				if (inputID.isEmpty() || inputID.equals("ID")) {
					JOptionPane.showMessageDialog(null, "ID를 입력하세요!", "오류", JOptionPane.ERROR_MESSAGE);
				} else if (signUPDAO.isIDExists(inputID)) {
					JOptionPane.showMessageDialog(null, "중복된 ID입니다!", "경고", JOptionPane.WARNING_MESSAGE);
					id.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "사용 가능한 ID입니다!", "확인", JOptionPane.INFORMATION_MESSAGE);
					isIdChecked = true;
				}
			}
		});

		// pw입력라벨
		JLabel pwBG = new JLabel();
		pwBG.setBounds(83, 170, 361, 37);
		pwBG.setIcon(DataManagers.getInstance().getIcon("textBox01", "signUp_Page"));

		// pw텍스트 필드
		pw = createPlaceholderPasswordField("비밀번호를 입력해주세요", 110, 175, 361, 37);
		pw.setFont(DataManagers.getInstance().getFont("bold", 13));
		pw.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		pw.setOpaque(false);
		pw.setBorder(null);
		add(pw);
		add(pwBG);

		// pw체크 입력라벨
		JLabel pwCheckBG = new JLabel();
		pwCheckBG.setBounds(83, 227, 361, 37);
		pwCheckBG.setIcon(DataManagers.getInstance().getIcon("textBox01", "signUp_Page"));

		// pw체크텍스트 필드
		pwCheck = createPlaceholderPasswordField("다시 한 번 비밀번호를 입력해주세요", 110, 235, 260, 30);
		pwCheck.setFont(DataManagers.getInstance().getFont("bold", 13));
		pwCheck.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		pwCheck.setOpaque(false);
		pwCheck.setBorder(null);
		add(pwCheck);
		add(pwCheckBG);

		// pw체크 테스트 밑에 뜨는 경고문구
		JLabel pwErrorLabel = new JLabel("• 비밀번호는 8자이상의 영문, 숫자, 특수문자를 포함해야 합니다.");
		pwErrorLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		pwErrorLabel.setFont(DataManagers.getInstance().getFont("regular", 9));
		pwErrorLabel.setBounds(110, 260, 260, 30); // PW 필드 옆에 배치
		add(pwErrorLabel);
		
		

		// email입력라벨
		JLabel emailBG = new JLabel();
		emailBG.setBounds(83, 299, 361, 37);
		emailBG.setIcon(DataManagers.getInstance().getIcon("textBox01", "signUp_Page"));

		// email텍스트 필드
		email = createPlaceholderTextField("이메일을 입력해주세요", 110, 307, 260, 30);
		email.setFont(DataManagers.getInstance().getFont("bold", 13));
		email.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		email.setOpaque(false);
		email.setBorder(null);
		add(email);
		add(emailBG);

		// 주민번호 앞자리 입력라벨
		JLabel myNumBG = new JLabel();
		myNumBG.setBounds(83, 359, 161, 37);
		myNumBG.setIcon(DataManagers.getInstance().getIcon("textBox02", "signUp_Page"));

		// 주민번호 앞자리 텍스트 필드
		myNum = createPlaceholderTextField("주민번호 앞 6자리", 110, 367, 130, 30);
		myNum.setFont(DataManagers.getInstance().getFont("bold", 13));
		myNum.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		myNum.setOpaque(false);
		myNum.setBorder(null);
		add(myNum);
		add(myNumBG);

		JLabel myNumSL = new JLabel();
		myNumSL.setBounds(253, 375, 23, 7);
		myNumSL.setIcon(DataManagers.getInstance().getIcon("textBoxIntervalBar", "signUp_Page"));
		add(myNumSL);
		// 주민번호 7번째자리 입력라벨 배경
		JLabel myNum7BG = new JLabel();
		myNum7BG.setBounds(283, 359, 161, 37);
		myNum7BG.setIcon(DataManagers.getInstance().getIcon("myNum7BG", "signUp_Page"));

		// 주민번호 7번째자리 입력하는 곳 라벨 배경
		JLabel myNum7BG7 = new JLabel();
		myNum7BG7.setBounds(283, 359, 51, 37);
		myNum7BG7.setIcon(DataManagers.getInstance().getIcon("textBox03", "signUp_Page"));

		// 주민번호 7번째 자리의 뒤의 **** 라벨
		JLabel myNum7B7G7 = new JLabel();
		myNum7B7G7.setBounds(336, 359, 107, 37);
		myNum7B7G7.setIcon(DataManagers.getInstance().getIcon("textBox04", "signUp_Page"));

		// 주민번호 7번째 자리의 뒤의 **** 텍스트 라벨
		JLabel myNum7B7G77 = new JLabel("******");
		myNum7B7G77.setBounds(355, 373, 80, 30);
		myNum7B7G77.setFont(DataManagers.getInstance().getFont("bold", 22)); // 폰트 크기 조정
		myNum7B7G77.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		myNum7B7G77.setOpaque(false);

		// 주민번호 한자리 텍스트필드
		myNum7 = createMynum7Field("한자리", 290, 368, 40, 28);
		myNum7.setFont(DataManagers.getInstance().getFont("bold", 13));
		myNum7.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		myNum7.setBorder(null);
		myNum7.setVisible(true);
		myNum7.setOpaque(false);

		add(myNum7B7G77);
		add(myNum7BG7);
		add(myNum7B7G7);
		add(myNum7BG);
		add(myNum7);
		setComponentZOrder(myNum7, 0);

		// 전번 라벨
		JLabel phoneNumBG = new JLabel();
		phoneNumBG.setBounds(83, 429, 361, 37);
		phoneNumBG.setIcon(DataManagers.getInstance().getIcon("textBox01", "signUp_Page"));

		// 전번 텍스트 필드
		phoneNum = createPlaceholderTextField("전화번호를 입력해주세요", 110, 437, 130, 30);
		phoneNum.setFont(DataManagers.getInstance().getFont("bold", 13));
		phoneNum.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		phoneNum.setOpaque(false);
		phoneNum.setBorder(null);
		add(phoneNum);
		add(phoneNumBG);

		// 국적 선택 라벨
		JLabel nationalitiesBG = new JLabel();
		nationalitiesBG.setBounds(83, 489, 232, 37);
		nationalitiesBG.setIcon(DataManagers.getInstance().getIcon("textBox05", "signUp_Page"));

		JLabel nationalityLabel = new JLabel("국적 선택");
		nationalityLabel.setFont(DataManagers.getInstance().getFont("bold", 13));
		nationalityLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		nationalityLabel.setBounds(110, 493, 232, 37);

		// 국적선택 버튼
		JLabel nationalitiesDropBox = new JLabel();
		nationalitiesDropBox.setBounds((int) 275.34, 489, (int) 39.66, 37);
		nationalitiesDropBox.setIcon(DataManagers.getInstance().getIcon("dropBoxBtn", "signUp_Page"));
		add(nationalityLabel);
		add(nationalitiesDropBox);
		add(nationalitiesBG);

		JComboBox<String> nationDrop = addNationalityDropdown();

		nationDrop.addActionListener(e -> {
			// 선택된 값 가져오기
			String selectedNationality = nationalityBox.getSelectedItem().toString();
			System.out.println("선택된 국적: " + selectedNationality);
			nationalityLabel.setText(selectedNationality);
		});

		myNum.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) || myNum.getText().length() >= 6) {
					e.consume(); // 입력 차단
				}
			}
		});
		// 닉네임 라벨
		JLabel nickNameBG = new JLabel();
		nickNameBG.setBounds(83, 549, 232, 37);
		nickNameBG.setIcon(DataManagers.getInstance().getIcon("textBox05", "signUp_Page"));

		// 닉네임 텍스트 필드
		nickName = createPlaceholderTextField("닉네임을 입력해주세요", 110, 557, 200, 30);
		nickName.setFont(DataManagers.getInstance().getFont("bold", 13));
		nickName.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		nickName.setOpaque(false);
		nickName.setBorder(null);
		add(nickName);
		add(nickNameBG);

		// 닉네임 중복 확인 버튼 라벨 JLabel
		JLabel checkNicknameButtonBG = new JLabel("중복확인");
		checkNicknameButtonBG.setBounds(359, 553, 101, 37);
		checkNicknameButtonBG.setFont(DataManagers.getInstance().getFont("bold", 14));
		checkNicknameButtonBG.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));

		// 닉네임 커스텀 버튼
		CustomButton checkNicknameButton = new CustomButton(
				DataManagers.getInstance().getIcon("signUpCheckBtn", "signUp_Page"));
		checkNicknameButton.setBounds(330, 549, 101, 37);
		checkNicknameButton.setOpaque(false);
		checkNicknameButton.setBorder(null);
		add(checkNicknameButtonBG);
		add(checkNicknameButton);

		checkNicknameButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String inputNickname = nickName.getText().trim();
				if (!inputNickname.matches("^[a-zA-Z0-9가-힣]+$")) {
					JOptionPane.showMessageDialog(null, "닉네임에는 특수문자를 포함할 수 없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
					nickName.setText(""); // 입력 필드 초기화
					return;
				}
				if (inputNickname.isEmpty() || inputNickname.equals("닉네임")) {
					JOptionPane.showMessageDialog(null, "닉네임을 입력하세요!", "오류", JOptionPane.ERROR_MESSAGE);
				} else if (signUPDAO.isNicknameExists(inputNickname)) {
					JOptionPane.showMessageDialog(null, "중복된 닉네임입니다!", "경고", JOptionPane.WARNING_MESSAGE);
					nickName.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "사용 가능한 닉네임입니다!", "확인", JOptionPane.INFORMATION_MESSAGE);
					isNicknameChecked = true;
				}

			}
		});

		// 비밀번호 입력 시 조건 검사 이벤트 추가
		pw.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String password = new String(pw.getPassword());
				
				if (pwErrorLabel.getText().isEmpty()) {
		            pwErrorLabel.setForeground(Color.WHITE);  // 초기에는 하얀색
		        }
		        
		        if (!isValidPassword(password)) {
		            pwErrorLabel.setText("비밀번호는 8자이상의 영문, 숫자, 특수문자를 포함해야 합니다.");
		            pwErrorLabel.setForeground(Color.RED); // 조건 불일치 시 빨간색
		        } else {
		            pwErrorLabel.setText(""); // 조건 만족하면 메시지 숨김
		        }
		    }
		});

		// 닉네임 커스텀 버튼
		CustomButton finaljoinButton = new CustomButton(DataManagers.getInstance().getIcon("signUpBtn", "signUp_Page"));
		finaljoinButton.setBounds(83, 609, 461, 43);
		finaljoinButton.setOpaque(false);
		finaljoinButton.setBorder(null);

		JLabel finaljoinButtonLabel = new JLabel("회원가입");
		finaljoinButtonLabel.setBounds(280, 615, 461, 43);
		finaljoinButtonLabel.setFont(DataManagers.getInstance().getFont("bold", 17));
		finaljoinButtonLabel.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));

		finaljoinButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				String password = new String(pw.getPassword());

				String emailText = email.getText().trim();
				if (!isValidEmail(emailText)) {
					JOptionPane.showMessageDialog(null, "올바른 이메일 형식이 아닙니다!", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!isIdChecked || !isNicknameChecked) {
					JOptionPane.showMessageDialog(null, "ID 및 닉네임 중복 확인을 완료하세요!", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (id.getText().trim().isEmpty() || new String(pw.getPassword()).trim().isEmpty()
						|| new String(pwCheck.getPassword()).trim().isEmpty() || email.getText().strip().isEmpty()
						|| myNum.getText().trim().isEmpty() || myNum7.getText().trim().isEmpty()
						|| phoneNum.getText().trim().isEmpty() || nickName.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "모든 필드를 입력하세요!", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (email.getText().trim().isEmpty() || email.getText().equals("email")) {
					JOptionPane.showMessageDialog(null, "이메일을 입력하세요!", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (phoneNum.getText().trim().isEmpty() || phoneNum.getText().equals("전화번호")) {
					JOptionPane.showMessageDialog(null, "전화번호를 입력하세요!", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!new String(pw.getPassword()).equals(new String(pwCheck.getPassword()))) {
					JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다!", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!isValidPassword(password)) {
					JOptionPane.showMessageDialog(null, "비밀번호가 형식에 어긋납니다", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}

				UserVO user = new UserVO();
				user.setId(id.getText().trim());
				user.setPassword(new String(pw.getPassword()));
				user.setEmail(email.getText().trim());
				user.setMyNumber(Integer.parseInt(myNum.getText().trim()));
				user.setGender(Boolean.parseBoolean(myNum7.getText().trim()));
				user.setPhoneNumber(phoneNum.getText().trim());
				user.setNickName(nickName.getText().trim());
				user.setnation((String) nationalityBox.getSelectedItem());

				// 회원 정보 저장
				try {
					signUPDAO.registerUser(user);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다!", "확인", JOptionPane.INFORMATION_MESSAGE);

				// 로그인 페이지로 넘기기
				LoginPanel loginpanel = new LoginPanel();
				// 스크롤 추가
				JScrollPane loginsignUpScroll = new JScrollPane(loginpanel);
				loginsignUpScroll.setBounds(3, 3, 600, 800);
				loginsignUpScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				loginsignUpScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

				// 세로 스크롤 속도 증가 (더 빠르게)
				loginsignUpScroll.getVerticalScrollBar().setUnitIncrement(30);

				// 가로 스크롤 완전 비활성화
				loginsignUpScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
				loginsignUpScroll.getHorizontalScrollBar().setOpaque(false);

				// 🔥 스크롤을 정상적으로 작동하게 만들기 위한 추가 코드
				loginpanel.setPreferredSize(new Dimension(600, 2000)); // 패널 크기를 충분히 크게 설정
				loginpanel.revalidate();  // UI 갱신
				loginpanel.repaint();     // UI 갱신

				// 🔥 add 위치 조정 (먼저 추가한 후 프레임 설정)
				add(loginsignUpScroll);

				try {
				    FrameBase frameBase = FrameBase.getInstance();
				    frameBase.setInnerPanel(loginpanel, "mid");
				} catch (Exception t) {
				    System.out.println("로그인 페이지 로드 실패 :" + t.getMessage());
				}

				// 입력 필드 초기화
				id.setText("ID");

				pw.setText("password");
				pw.setForeground(Color.GRAY);
				pw.setEchoChar((char) 0);

				pwCheck.setText("password 확인");
				pw.setForeground(Color.GRAY);
				pw.setEchoChar((char) 0);
				email.setText("email");
				myNum.setText("주민번호 앞자리");
				myNum7.setText("");
				phoneNum.setText("전화번호");
				nickName.setText("닉네임");

				isIdChecked = false;
				isNicknameChecked = false;
			}

		});
		add(finaljoinButtonLabel);
		add(finaljoinButton);

	}

	private boolean isValidPassword(String password) {
		return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$");
	}

	private JTextField createPlaceholderTextField(String placeholder, int x, int y, int width, int height) {
		JTextField field = new JTextField(placeholder);
		field.setBounds(x, y, width, height);
		field.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));

		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (field.getText().equals(placeholder)) {
					field.setText("");
					field.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().isEmpty()) {
					field.setText(placeholder);
					field.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
				}
			}
		});

		add(field);
		return field;
	}

	private JPasswordField createPlaceholderPasswordField(String placeholder, int x, int y, int width, int height) {
		JPasswordField field = new JPasswordField(placeholder);
		field.setBounds(x, y, width, height);
		field.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		field.setEchoChar((char) 0);

		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (new String(field.getPassword()).equals(placeholder)) {
					field.setText("");
					field.setForeground(Color.BLACK);
					field.setEchoChar('●');
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (field.getPassword().length == 0) {
					field.setText(placeholder);
					field.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
					;
					field.setEchoChar((char) 0);
				}
			}
		});

		add(field);
		return field;
	}

	private JTextField createMynum7Field(String placeholder, int x, int y, int width, int height) {
		JTextField field = new JTextField(placeholder);
		field.setBounds(x, y, width, height);
		field.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // 기본 텍스트 색상을 회색으로 설정
		add(field);

		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (field.getText().equals(placeholder)) {
					field.setDocument(new LimitDocument(1));
					field.setText(""); // 포커스를 얻으면 텍스트 지우기
					field.setForeground(Color.BLACK); // 텍스트 색상 변경
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().isEmpty()) {
					field.setDocument(new PlainDocument());
					field.setText(placeholder); // 포커스를 잃으면 텍스트를 다시 기본값으로 설정
					field.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // 텍스트 색상 변경
				}
			}
		});

		// 오류 메시지 라벨
		errorLabel = new JLabel();
		errorLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
		errorLabel.setFont(DataManagers.getInstance().getFont("regular", 9));
		errorLabel.setBounds(110, 390, 200, 30);
		add(errorLabel);

		// 입력된 값 검사
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = field.getText().trim();
				if (!text.isEmpty() && text.matches("[1-4]")) {
					errorLabel.setText("");
				} else {
					errorLabel.setText("•잘못된 주민번호 형식입니다.");
				}
			}
		});

		return field;
	}

	private JComboBox<String> addNationalityDropdown() {
		String[] nationalities = { "국가", "대한민국", "미국", "일본", "중국", "캐나다", "영국", "프랑스", "독일", "호주", "브라질", "러시아" };
		nationalityBox = new JComboBox<>(nationalities);
		nationalityBox.setBounds(95, 489, 200, 31);
		nationalityBox.setOpaque(false);
		add(nationalityBox);

		return nationalityBox;
	}
}

class LimitDocument extends PlainDocument {
	private int limit;

	public LimitDocument(int limit) {
		this.limit = limit;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null || (getLength() + str.length() > limit)) {
			return;
		}
		if (!str.matches("[1-4]")) { // 1~4만 입력 가능하도록 필터링
			return;
		}
		super.insertString(offset, str, attr);
	}
}