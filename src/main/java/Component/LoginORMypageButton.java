package Component;

import Panel.OpenPage;

import javax.swing.*;

import Managers.LoginConfrimManager;

import java.awt.*;

public class LoginORMypageButton extends JPanel {
    private JButton loginButton, mypageButton;
    private JLabel welcomeLabel;

    public LoginORMypageButton() {
        setLayout(new FlowLayout(FlowLayout.RIGHT)); //우측으로 버튼 정렬
         	//로그인 유무 판단 메서드 호출 id,pw 세팅
        if (LoginConfrimManager.loginConfrim("123","ABC")) {
        	//입력한 로그인 정보가 TRUE면 마이페이지 버튼 + 환영문구 추가
        	addMyPageButton();
        	   
        } else {
        	//입력한 로그인 정보가 FALSE면 로그인버튼 추가
        	addLoginButton();
        	
        }
    }
    
    OpenPage openPage = new OpenPage();
    
    //로그인 버튼 생성
    public void addLoginButton() {
    	//비로그인 상태: 로그인 버튼 추가
        loginButton = new JButton("로그인");
        loginButton.addActionListener(e -> openPage.openLoginPage());
        add(loginButton);
    }
   
    //마이페이지 버튼 생성
    public void addMyPageButton() {
    	//로그인 상태: 환영 문구 추가
    	welcomeLabel = new JLabel("nickname 데이터 추후 추가" + "님 환영합니다.");
        add(welcomeLabel);
    	
    	//로그인 상태: 마이페이지 버튼 추가
    	mypageButton = new JButton("마이페이지");
    	mypageButton.addActionListener(e -> openPage.openMyPage());
        add(mypageButton);
    }
    
   
}