package Panel;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import DAO.SignUPDAO;
import Data.AppConstants;
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
		setBackground(Color.GRAY);
		setSize(600, 600);

		// 입력 필드 설정
		id = createPlaceholderTextField("ID", 90, 80, 200, 30);
		pw = createPlaceholderPasswordField("password", 90, 130, 260, 30);
		pwCheck = createPlaceholderPasswordField("password 확인", 90, 180, 260, 30);
		email = createPlaceholderTextField("email", 90, 230, 260, 30);
		myNum = createPlaceholderTextField("주민번호 앞자리", 90, 280, 130, 30);
		myNum7 = createMynum7Field(230, 280, 20, 30);
		phoneNum = createPlaceholderTextField("전화번호", 90, 330, 130, 30);
		nickName = createPlaceholderTextField("닉네임", 90, 430, 200, 30);

		addNationalityDropdown();

		JLabel pwErrorLabel = new JLabel("비밀번호는 8자이상의 영문, 숫자, 특수문자를 포함해야 합니다.");
		pwErrorLabel.setForeground(Color.RED);
		pwErrorLabel.setBounds(360, 130, 300, 30); // PW 필드 옆에 배치
		add(pwErrorLabel);

		myNum.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) || myNum.getText().length() >= 6) {
					e.consume(); // 입력 차단
				}
			}
		});
		// 비밀번호 입력 시 조건 검사 이벤트 추가
		pw.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String password = new String(pw.getPassword());
				if (!isValidPassword(password)) {
					pwErrorLabel.setText("비밀번호는 8자이상의 영문, 숫자, 특수문자를 포함해야 합니다.");
				} else {
					pwErrorLabel.setText(""); // 조건 만족하면 메시지 숨김
				}
			}
		});
		// ID 중복 확인 버튼
		JButton checkIDButton = new JButton("ID확인");
		checkIDButton.setBounds(300, 80, 100, 30);
		add(checkIDButton);

		checkIDButton.addActionListener(e -> {
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
		});

		// 닉네임 중복 확인 버튼 추가
		JButton checkNicknameButton = new JButton("닉네임확인");
		checkNicknameButton.setBounds(300, 430, 100, 30);
		add(checkNicknameButton);

		checkNicknameButton.addActionListener(e -> {
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
		});

		// 회원가입 버튼
		JButton finaljoinButton = new JButton("회원가입");
		finaljoinButton.setBounds(AppConstants.FRAME_WIDTH / 2, 500, 100, 30);
		add(finaljoinButton);

		finaljoinButton.addActionListener(e -> {
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
		});
	}

	private boolean isValidPassword(String password) {
		return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$");
	}

	private JTextField createPlaceholderTextField(String placeholder, int x, int y, int width, int height) {
		JTextField field = new JTextField(placeholder);
		field.setBounds(x, y, width, height);
		field.setForeground(Color.GRAY);

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
					field.setForeground(Color.GRAY);
				}
			}
		});

		add(field);
		return field;
	}

	private JPasswordField createPlaceholderPasswordField(String placeholder, int x, int y, int width, int height) {
		JPasswordField field = new JPasswordField(placeholder);
		field.setBounds(x, y, width, height);
		field.setForeground(Color.GRAY);
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
					field.setForeground(Color.GRAY);
					field.setEchoChar((char) 0);
				}
			}
		});

		add(field);
		return field;
	}

	private JTextField createMynum7Field(int x, int y, int width, int height) {
		JTextField field = new JTextField();
		field.setBounds(x, y, width, height);
		field.setDocument(new LimitDocument(1));
		add(field);

		errorLabel = new JLabel();
		errorLabel.setForeground(Color.RED);
		errorLabel.setBounds(x + 30, y, 200, 30);
		add(errorLabel);

		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = field.getText().trim();
				if (!text.isEmpty() && text.matches("[1-4]")) {
					errorLabel.setText("");
				} else {
					errorLabel.setText("잘못된 주민번호 형식입니다.");
				}
			}
		});

		return field;
	}

	private void addNationalityDropdown() {
		String[] nationalities = { "국가","대한민국", "미국", "일본", "중국", "캐나다", "영국", "프랑스", "독일", "호주", "브라질", "러시아" };
		nationalityBox = new JComboBox<>(nationalities);
		nationalityBox.setBounds(90, 380, 200, 30);
		add(nationalityBox);
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