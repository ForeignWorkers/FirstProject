package Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import TESTDAO.TestConentDAO;
import TESTVO.TestContentVO;


// 컨텐츠 패널
public class MainPagePanel extends JPanel {
    private TestContentVO content; // 현재 패널이 표시하는 컨텐츠 정보
    private static TestConentDAO contentDAO = new TestConentDAO(); //DAO 객체 생성
    private JPanel contentPanel;
    
    public MainPagePanel() {
    	
        setLayout(null); // 전체 레이아웃 설정
        setBackground(Color.LIGHT_GRAY);
        setBounds(0, 0, 600, 600); // 패널 크기 설정
        
        //스크롤 적용 패널 
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.setPreferredSize(new Dimension(600,800));
        
        //큰 추천 컨텐츠 추가
        JPanel bigPanel = bigRecommendContentPanel();
        add(bigPanel);

        //작은 추천 컨텐츠 추가
        JPanel smallPanel = smallRecommendContentPanel();
        add(smallPanel);
        
        //스크롤 추가
        JScrollPane mainPageScroll = new JScrollPane(contentPanel);
        mainPageScroll.setBounds(0, 0, 600, 800);
        mainPageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(mainPageScroll);
    }
      
    //기본 생성자: 랜덤 컨텐츠를 자동으로 가져와 설정
   public JPanel bigRecommendContentPanel() {
    	JPanel bigPanel = new JPanel();
    	content = contentDAO.getRandomContent(); //랜덤 컨텐츠 가져와 저장
        System.out.println("랜덤 컨텐츠 로드 완료: " + content.getTitle());
        bigPanel.setLayout(null);
        bigPanel.setBackground(Color.PINK);
        // 패널 크기 설정
        bigPanel.setBounds(160, 10, 250, 300);
        // 버튼 설정
        JButton thumbnailButton = new JButton(new ImageIcon(content.getThumbnailFile()));
        thumbnailButton.setBounds(36, 10, 180, 250);
        thumbnailButton.addActionListener(e -> showContentDetails(content));

        //제목 라벨
        JLabel titleLabel = new JLabel(content.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        titleLabel.setBounds((250 - 200) / 2, 255, 200, 30);

        //평점 라벨
        JLabel ratingLabel = new JLabel("평점" + content.getRating(), SwingConstants.CENTER);
        ratingLabel.setBounds(25, 275, 200, 30);
        
        //UI 배치
        bigPanel.add(thumbnailButton);
        bigPanel.add(titleLabel);
        bigPanel.add(ratingLabel);    
        
        return bigPanel;
    }
   

   //작은 추천 컨텐츠 패널 (4개 가로 배치, setLayout(null) 사용)
   public JPanel smallRecommendContentPanel() {
       JPanel smallPanel = new JPanel();
       smallPanel.setLayout(null); //null 레이아웃 유지
       smallPanel.setBackground(Color.PINK);
       smallPanel.setBounds(14, 350, 550, 300); //패널 위치 및 크기 설정

       int itemWidth = 150;
       int itemHeight = 200;
       int margin = -3; // 아이템 간격
       int panelWidth = 600; // 부모 패널의 너비
       
       for (int i = 0; i < 4; i++) { //4개의 컨텐츠 추가
           TestContentVO smallContent = contentDAO.getRandomContent(); // 랜덤 컨텐츠 가져오기
           JPanel contentItem = createContentItem(smallContent);

           //X 좌표를 조정하여 간격 줄이기
           int x = (panelWidth - (4 * itemWidth + (3 * margin))) / 2 + i * (itemWidth + margin);
           contentItem.setBounds(x, 10, itemWidth, itemHeight);

           smallPanel.add(contentItem); // 패널에 추가
       }

       return smallPanel;
   }

   //개별 컨텐츠 아이템 
   private JPanel createContentItem(TestContentVO content) {
       JPanel itemPanel = new JPanel();
       itemPanel.setLayout(null); 
       itemPanel.setBackground(Color.PINK);
       itemPanel.setSize(150, 180); //크기 설정
       
       int panelWidth = 150;
       int panelHeight = 180;
       itemPanel.setSize(panelWidth, panelHeight); // 크기 설정

       // 부모 패널 너비 (smallRecommendContentPanel 크기)
       int parentWidth = 600; // smallRecommendContentPanel의 width (가로 크기)

       // 중앙 정렬 X 좌표 계산
       int centerX = (parentWidth - panelWidth) / 2;

       // 중앙에 배치
       itemPanel.setBounds(centerX, 10, panelWidth, panelHeight); 

       //썸네일 버튼
       JButton thumbnail = new JButton(new ImageIcon(content.getThumbnailFile()));
       thumbnail.setBounds(4, 10, 120, 167); // 썸네일 위치 및 크기 조정
       thumbnail.setBorderPainted(false);
       thumbnail.setContentAreaFilled(false);
       thumbnail.setFocusPainted(false);
       thumbnail.addActionListener(e -> showContentDetails(content));

       //제목 라벨
       JLabel title = new JLabel(content.getTitle(), SwingConstants.CENTER);
       title.setFont(new Font("맑은 고딕", Font.BOLD, 12));
       title.setBounds(4, 180, 130, 20); // 위치 및 크기 조정

       //평점 라벨
       JLabel rating = new JLabel("별점 " + content.getRating(), SwingConstants.CENTER);
       rating.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
       rating.setBounds(4, 195, 130, 20); // 위치 및 크기 조정

       //UI 배치
       itemPanel.add(thumbnail);
       itemPanel.add(title);
       itemPanel.add(rating);

       return itemPanel;
   }
   
    //컨텐츠 페이지 이동
   private void showContentDetails(TestContentVO content) {
       JOptionPane.showMessageDialog(null, content.getTitle() + " 상세 페이지 이동");
   }

}
