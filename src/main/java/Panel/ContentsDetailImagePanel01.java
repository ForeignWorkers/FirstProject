package Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Data.AppConstants;
import TESTDAO.TestContentDetailPanelDAO;
import TESTVO.TestContentDetailPanelVO;

public class ContentsDetailImagePanel01 extends JPanel {
    // 세 번째 패널 (버튼 클릭에 따라 변경)
    private JPanel thirdPanel1; // 기본 패널 (검은색)
    private JPanel thirdPanel2; // 변경될 패널 (초록색)

    /**
     * 콘텐츠 상세 이미지 패널 생성자
     * @param testVO 영화 정보를 담고 있는 VO 객체
     * @param posterPath 영화 포스터 이미지 경로
     */
    public ContentsDetailImagePanel01() {
    	
        String posterPath = "src/main/java/TESTVO/TokyoCheaters.png"; // 포스터 이미지 경로
                
        TestContentDetailPanelDAO dataDAO = new TestContentDetailPanelDAO();
        
        //VO에서 가져온 데이터를 DAO에 넣고, 그DAO에서  설정해주는 모습
        dataDAO.addDetailDatas(new TestContentDetailPanelVO(1,"도쿄 사기꾼들", "범죄", 2025, 5.0, 150, 100));
        TestContentDetailPanelVO testVO = dataDAO.getItemUseID(1);
        setLayout(null);
        setPreferredSize(new Dimension(AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT));
        setBounds(0, 0, AppConstants.PANEL_MID_WIDTH, AppConstants.PANEL_MID_HEIGHT);

        // 첫 번째 패널 (상단: 영화 정보 및 포스터)
        JPanel firstPanel = new JPanel();
        firstPanel.setLayout(null);
        firstPanel.setBounds(0, 0, AppConstants.PANEL_MID_WIDTH, 183);
        
        // 영화 정보를 표시하는 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBounds(10, 10, 400, 163);

        // 평점 및 리뷰 개수
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel ratingLabel = new JLabel("평점: " + testVO.getRating() + "  |  ");
        JLabel reviewLabel = new JLabel("리뷰 개수: " + testVO.getPersonNum());
        ratingPanel.add(ratingLabel);
        ratingPanel.add(reviewLabel);

        // 영화 제목
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("타이틀: " + testVO.getTitle());
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // 장르 및 제작년도
        JPanel infoSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel genreLabel = new JLabel("장르: " + testVO.getGenre() + "  |  ");
        JLabel yearLabel = new JLabel("제작년도: " + testVO.getPromotionYear());
        infoSubPanel.add(genreLabel);
        infoSubPanel.add(yearLabel);

        // 영화 정보 패널 구성
        infoPanel.add(ratingPanel);
        infoPanel.add(titlePanel);
        infoPanel.add(infoSubPanel);
        firstPanel.add(infoPanel);

        // 포스터 표시 패널
        JPanel posterPanel = new JPanel();
        posterPanel.setBounds(420, 10, 150, 150);
        posterPanel.setLayout(null);

        JLabel posterLabel = new JLabel();
        posterLabel.setBounds(0, 0, 150, 150);

        // 포스터 이미지 설정
        if (posterPath != null && !posterPath.isEmpty()) {
            ImageIcon posterImage = new ImageIcon(posterPath);
            Image img = posterImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(img));
        } else {
            posterLabel.setText("이미지 없음");
            posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
            posterLabel.setVerticalAlignment(SwingConstants.CENTER);
        }

        posterPanel.add(posterLabel);
        firstPanel.add(posterPanel);

        add(firstPanel);

        // 두 번째 패널 (작품설명/리뷰 버튼)
        JPanel secondPanel = new JPanel();
        secondPanel.setBackground(Color.LIGHT_GRAY);
        secondPanel.setLayout(new BorderLayout());
        secondPanel.setBounds(0, 183, AppConstants.PANEL_MID_WIDTH, 63);

        JButton leftButton = new JButton("작품설명");
        leftButton.setPreferredSize(new Dimension(297, 50));

        JButton rightButton = new JButton("리뷰");
        rightButton.setPreferredSize(new Dimension(298, 50));

        secondPanel.add(leftButton, BorderLayout.WEST);
        secondPanel.add(rightButton, BorderLayout.EAST);
        add(secondPanel);

        // 세 번째 패널 1 (작품설명 상세걔)
        thirdPanel1 = new JPanel();
        thirdPanel1.setBackground(Color.BLACK);
        thirdPanel1.setLayout(null);
        thirdPanel1.setBounds(0, 246, AppConstants.PANEL_MID_WIDTH, 303);
        add(thirdPanel1);

        // 세 번째 패널 2 (버튼 클릭 시 변경)
        thirdPanel2 = new JPanel();
        thirdPanel2.setBackground(Color.GREEN);
        thirdPanel2.setLayout(null);
        thirdPanel2.setBounds(0, 246, AppConstants.PANEL_MID_WIDTH, 303);
        thirdPanel2.setVisible(false);
        add(thirdPanel2);

        // 버튼 클릭 시 패널 전환
        leftButton.addActionListener(e -> showPanel(thirdPanel1));
        rightButton.addActionListener(e -> showPanel(thirdPanel2));
    }
    
    //패널 교환을 
    private void showPanel(JPanel panelToShow) {
        thirdPanel1.setVisible(panelToShow == thirdPanel1);
        thirdPanel2.setVisible(panelToShow == thirdPanel2);
    }
    
}