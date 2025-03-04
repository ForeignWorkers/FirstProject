package Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import DAO.ReviewDAO;
import Data.AppConstants;
import TESTVO.TestReviewVO;
import VO.ReviewVO;

public class ReviewPanelLast extends JPanel {
    private JPanel reviewListPanel;
    private JTextField reviewInput;
    private JButton submitButton;
    private JButton[] starButtons; // 별점 선택을 위한 버튼 배열
    private int selectedRating = AppConstants.STAR_MAX_SCORE; // 기본 평점 5점
    private boolean isLoggedIn = true; // 로그인 여부 (테스트용)
    private ReviewDAO reviewDAO; // ReviewDAO 추가

    public ReviewPanelLast() {
        reviewDAO = new ReviewDAO(); // ReviewDAO 초기화
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setSize(500, 200);

        // 상단 리뷰 입력 영역 (로그인한 경우만 표시)
        JPanel inputPanel = new JPanel(new BorderLayout()); // BorderLayout으로 변경
        inputPanel.setBackground(Color.WHITE);

        // 리뷰 입력 필드와 별점 패널을 담을 패널
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.WHITE);

        reviewInput = new JTextField(20); // 리뷰 입력 필드
        submitButton = new JButton("리뷰 등록"); // 등록 버튼 
        submitButton.setEnabled(false);

        // 별점 선택을 위한 버튼 배열 초기화
        starButtons = new JButton[AppConstants.STAR_MAX_SCORE];
        JPanel starPanel = new JPanel(new GridLayout(1, AppConstants.STAR_MAX_SCORE)); // 별점 버튼을 한 줄로 배치
        for (int i = 0; i < starButtons.length; i++) {
            starButtons[i] = new JButton("☆");
            starButtons[i].setFont(new Font("맑은 고딕", Font.PLAIN, 20));
            starButtons[i].setBackground(Color.WHITE);
            starButtons[i].setBorderPainted(false);
            final int rating = i + 1; // 선택된 평점 (1~5)
            starButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedRating = rating;
                    updateStarButtons(); // 별점 버튼 업데이트
                }
            });
            starPanel.add(starButtons[i]);
        }
        updateStarButtons(); // 초기 별점 버튼 상태 업데이트

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

        // leftPanel에 리뷰 입력 필드와 별점 패널 추가
        leftPanel.add(reviewInput);
        leftPanel.add(starPanel);

        // inputPanel에 leftPanel과 등록 버튼 추가
        inputPanel.add(leftPanel, BorderLayout.CENTER); // 왼쪽 패널은 중앙에 배치
        inputPanel.add(submitButton, BorderLayout.EAST); // 등록 버튼은 오른쪽에 배치

        // 입력 감지하여 버튼 활성화
        reviewInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { toggleSubmitButton(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { toggleSubmitButton(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { toggleSubmitButton(); }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addReview("사용자", reviewInput.getText(), selectedRating); // 평점 추가
                reviewInput.setText("");
                submitButton.setEnabled(false);
                checkReviewList(); // 리뷰 추가 후 리뷰 목록 상태 확인
            }
        });

        // 리뷰 목록 패널
        reviewListPanel = new JPanel();
        reviewListPanel.setLayout(new BoxLayout(reviewListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(reviewListPanel);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        
        
        if (isLoggedIn) {
            add(inputPanel, BorderLayout.NORTH);
        } else {
            add(new JLabel("로그인 후 리뷰를 작성할 수 있습니다.", SwingConstants.CENTER), BorderLayout.NORTH);
        }
        add(scrollPane, BorderLayout.CENTER);

        // 기존 리뷰 데이터 로드
        loadReviews();
        checkReviewList(); // 초기 리뷰 목록 상태 확인
    }

    // 리뷰 목록이 비어 있는지 확인하고 문구 표시
    private void checkReviewList() {
        if (reviewListPanel.getComponentCount() == 0) {
            JLabel noReviewLabel = new JLabel("첫 리뷰를 작성해주세요!", SwingConstants.CENTER);
            noReviewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            noReviewLabel.setForeground(Color.GRAY);
            reviewListPanel.add(noReviewLabel);
            reviewListPanel.revalidate();
            reviewListPanel.repaint();
        }
    }

    // 별점 버튼 업데이트
    private void updateStarButtons() {
        for (int i = 0; i < starButtons.length; i++) {
            if (i < selectedRating) {
                starButtons[i].setText("★"); // 선택된 별
                starButtons[i].setForeground(Color.ORANGE);
            } else {
                starButtons[i].setText("☆"); // 선택되지 않은 별
                starButtons[i].setForeground(Color.BLACK);
            }
        }
    }

    private void toggleSubmitButton() {
        submitButton.setEnabled(reviewInput.getText().length() >= AppConstants.MINUMUM_REVIEW_TEXT);
    }

    // 리뷰 추가 메서드
    public void addReview(String nickname, String content, double score) {
        // ReviewDAO에 리뷰 추가
        reviewDAO.addReview(nickname, content, score);

        // 화면에 리뷰 표시
        displayReview(nickname, content, score);
    }

    // 리뷰 화면에 표시
    private void displayReview(String nickname, String content, double score) {
        // 리뷰 목록이 비어 있을 때 표시된 문구 제거
        if (reviewListPanel.getComponentCount() == 1 && reviewListPanel.getComponent(0) instanceof JLabel) {
            reviewListPanel.removeAll();
        }

        JPanel reviewItem = new JPanel();
        reviewItem.setLayout(new BorderLayout()); // BorderLayout 사용
        reviewItem.setBackground(Color.LIGHT_GRAY);
        reviewItem.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        reviewItem.setSize(new Dimension(500, 150)); // 리뷰 아이템 크기 고정

        // 프로필 이미지 담기
        ImageIcon profileIcon = new ImageIcon("src/main/image/zakoprofile.png");
        JLabel profileLabel = new JLabel(profileIcon);
        profileLabel.setPreferredSize(new Dimension(50, 50));

        // 이름과 날짜를 담을 패널
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.LIGHT_GRAY);

        // 이름
        JLabel nameLabel = new JLabel(nickname);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        // 날짜
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = dateFormat.format(new Date());
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));

        JPanel profileNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profileNamePanel.setBackground(Color.LIGHT_GRAY);
        profileNamePanel.add(profileLabel);
        profileNamePanel.add(nameLabel);

        topPanel.add(profileNamePanel);
        topPanel.add(dateLabel);

        // 리뷰 내용과 별점을 담을 패널
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);

        // 리뷰 내용 (최대 30자씩 두 줄)
        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        contentArea.setLineWrap(true); // 자동 줄바꿈
        contentArea.setWrapStyleWord(true); // 단어 단위로 줄바꿈
        contentArea.setEditable(false); // 편집 불가능
        contentArea.setBackground(Color.LIGHT_GRAY);

        // 별점 표시
        JLabel scoreLabel = new JLabel("★" + score);
        scoreLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        
       

        contentPanel.add(contentArea, BorderLayout.CENTER);
        contentPanel.add(scoreLabel, BorderLayout.SOUTH); // 별점을 리뷰 내용 아래에 배치

        // 리뷰 아이템에 컴포넌트 추가
        reviewItem.add(topPanel, BorderLayout.NORTH); // 이름과 날짜는 상단
        reviewItem.add(contentPanel, BorderLayout.SOUTH); // 별점은 하단

        reviewListPanel.add(reviewItem);
        reviewListPanel.revalidate();
        reviewListPanel.repaint();
    }

    // 기존 리뷰 데이터 로드
    private void loadReviews() {
        for (ReviewVO review : reviewDAO.getReviews()) {
            displayReview(review.getReviewName(), review.getReviewContent(), review.getReviewScore());
        }
    }
    
}