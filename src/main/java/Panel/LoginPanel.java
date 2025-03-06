package Panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Component.CustomButton;
import DAO.SignUPDAO;
import Managers.DataManagers;

public class LoginPanel extends JPanel {
	
	public LoginPanel() {
		
		
		setBackground(Color.decode("#404153"));
		setLayout(null);
		setBounds(0,0,595,575);
		
		JLabel hiWelcome = new JLabel("안녕하세요?");
		hiWelcome.setBounds(63, 28, 461, 85);
		hiWelcome.setFont(DataManagers.getInstance().getFont("",30));
		hiWelcome.setForeground(Color.decode("#78DBA6"));
		add(hiWelcome);
		JLabel hiWelcome2 = new JLabel("다시 돌아온 것을 환영합니다.");
		hiWelcome2.setBounds(63, 57, 461, 85);
		hiWelcome2.setFont(DataManagers.getInstance().getFont("Medium",30));
		hiWelcome2.setForeground(Color.decode("#78DBA6"));
		add(hiWelcome2);
		
		
		JLabel login = new JLabel("로그인");
		login.setBounds(63, 155, 332, 42);
		login.setFont(DataManagers.getInstance().getFont("",24));
        login.setForeground(Color.decode("#CBCBCB"));
		add(login);
		
		
		JLabel idBG = new JLabel();
		idBG.setIcon(DataManagers.getInstance().getIcon("loginIdTextBox", "login_Page"));
		idBG.setBounds(63,206,461,43);
		
		JTextField idTextField = new JTextField("아이디를 입력해주세요.");
		idTextField.setBounds(20,8,350,35);
		idTextField.setFont(DataManagers.getInstance().getFont("",13));
		idTextField.setForeground(Color.decode("#CBCBCB"));
		idTextField.setOpaque(false);
		idTextField.setBorder(null);
		
		
		// 입력하면 글자 색 바꾸기 입력한 텍스트는 원래 색상 복구
		idTextField.addFocusListener(new java.awt.event.FocusListener() {
		    @Override
		    public void focusGained(java.awt.event.FocusEvent e) {
		        if (idTextField.getText().equals("아이디 또는 이메일을 입력해주세요.")) {
		            idTextField.setText("");
		            idTextField.setForeground(Color.BLACK);
		        }
		    }

		    @Override
		    public void focusLost(java.awt.event.FocusEvent e) {
		        if (idTextField.getText().isEmpty()) {
		            idTextField.setText("아이디 또는 이메일을 입력해주세요.");
		            idTextField.setForeground(Color.decode("#CBCBCB")); 
		        }
		    }
		});
		
		idBG.add(idTextField);
		

		
		JLabel pwBG = new JLabel();
		pwBG.setIcon(DataManagers.getInstance().getIcon("loginIdTextBox", "login_Page"));
		pwBG.setBounds(63,291,461,43);
		
		JTextField pwTextField = new JTextField("비밀번호를 입력해주세요.");
		pwTextField.setBounds(20,8,350,35);
		pwTextField.setFont(DataManagers.getInstance().getFont("",13));
		pwTextField.setForeground(Color.decode("#CBCBCB"));
		
		pwTextField.setOpaque(false);
		pwTextField.setBorder(null);
		
		pwTextField.addFocusListener(new java.awt.event.FocusListener() {
		    @Override
		    public void focusGained(java.awt.event.FocusEvent e) {
		        if (pwTextField.getText().equals("비밀번호를 입력해주세요.")) {
		            pwTextField.setText("");
		            pwTextField.setForeground(Color.BLACK); // 입력하면 글자 색 바꾸기
		        }
		    }

		    @Override
		    public void focusLost(java.awt.event.FocusEvent e) {
		        if (pwTextField.getText().isEmpty()) {
		            pwTextField.setText("비밀번호를 입력해주세요.");
		            pwTextField.setForeground(Color.decode("#CBCBCB")); // 원래 색상 복구
		        }
		    }
		});

		pwBG.add(pwTextField);
		
		add(pwBG);
		add(idBG);
		
		//로그인 버튼
		CustomButton loginButton = new CustomButton(DataManagers.getInstance().getIcon("loginButton", "login_Page"));
		loginButton.setClickEffect(true, 0.3f);
		loginButton.setBounds(63,423,461,43);
		
		JLabel loginButtonText = new JLabel("로그인");
		loginButtonText.setBounds(265,430,461,43);
		loginButtonText.setFont(DataManagers.getInstance().getFont("",20));
		loginButtonText.setForeground(Color.decode("#595959"));
		
		
		add(loginButtonText);
		add(loginButton);
		
		//회원가입 버튼
		JButton signupbutton = new JButton("회원가입");
		signupbutton.setFont(DataManagers.getInstance().getFont("",17));
		signupbutton.setBounds(195,481,202,39);
		signupbutton.setForeground(Color.decode("#78DBA6")); //버튼 컬러
		signupbutton.setContentAreaFilled(false); // 버튼 배경 없애기
		signupbutton.setBorderPainted(false); //버튼 테두리 없앰
		add(signupbutton);
		
		loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            		String inputID = idTextField.getText().trim();
            		
            }
		});
		
				
	}
	

	
	
//    private JTextField idField;
//    private JPasswordField pwField;
//    private JPanel parentPanel;
//    
//    public LoginPanel(JPanel parentPanel) {
//    	
//    	SignUPDAO.registerUser("min",
//    			"1234",
//    			"www",
//    			"1234",
//    			"1",
//    			"1231431",
//    			"srtta",
//    			"korean");
//    	
//    	this.parentPanel = parentPanel;
//        setLayout(null);
//        parentPanel.setLayout(null);
//        
//        parentPanel.setBackground(Color.blue);
//        parentPanel.setBounds(0, 0, 595, 550);
//        parentPanel.setVisible(true);
//
//        JTextField textf = new JTextField("id");
//        textf.setBackground(Color.cyan);
//        textf.setBounds(63, 205, 461, 43);
//        textf.setVisible(true);
//        textf.setLayout(null);
//        
//        
//        add(parentPanel);
//        setVisible(true);
//        
//         // 아이콘을 텍스트 필드 왼쪽에 배치드
//        
////        JLabel pwLabel = new JLabel("password");
////        pwLabel.setBounds(90, 110, 100, 20);
////        add(pwLabel);
////
////        pwField = new JPasswordField();
////        pwField.setBounds(90, 130, 200, 30);
////        add(pwField);
////
////        // 로그인 버튼
////        JButton loginButton = new JButton("Login");
////        loginButton.setBounds(300, 80, 100, 80);
////        add(loginButton);
////
////        // 회원가입 버튼
////        JButton signUpButton = new JButton("회원가입");
////        signUpButton.setBounds(90, 180, 100, 30);
////        add(signUpButton);
////
////        // 로그인 버튼 기능
////        loginButton.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                String inputId = idField.getText().trim();
////                String inputPassword = new String(pwField.getPassword()).trim();
////
////                if (inputId.isEmpty() || inputPassword.isEmpty()) {
////                    JOptionPane.showMessageDialog(null, "ID와 비밀번호를 입력하세요!", "오류", JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////
////                System.out.println(inputId);
////                System.out.println(inputPassword);
////                if (SignUPDAO.loginUser(inputId, inputPassword)) {
////                    JOptionPane.showMessageDialog(null, "로그인 성공!", "확인", JOptionPane.INFORMATION_MESSAGE);
////                    // 로그인 성공 시 다음 화면으로 이동 가능
////                } else {
////                    JOptionPane.showMessageDialog(null, "ID 또는 비밀번호가 잘못되었습니다.", "오류", JOptionPane.ERROR_MESSAGE);
////                }
////            }
////        });
////
////        // 회원가입 버튼 클릭 시, 회원가입 화면으로 전환
////        signUpButton.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////            	//todo 회원가입 페이지로 넘어가는 기능 추가
////            }
////        });
//    }
}
