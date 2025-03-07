package Panel;

import javax.swing.*;

import Managers.DataManagers;

import java.awt.*;

public class RankingPagePanel extends JPanel {
    
    private JPanel rankingContentPanel; // 랭킹 콘텐츠 리스트 패널
    private JScrollPane scrollPane; // 스크롤 패널

    public RankingPagePanel() {
        setLayout(null); // 절대 위치 설정
        setBackground(Color.GRAY); // 배경색 설정
        setBounds(0, 0, 600, 600); // 패널 크기 설정

        //OTT 필터 버튼 추가 (rankingContentPanel 외부 상단에 배치)
        addOttFilterButtons();
        
        //OTT 필터와 랭킹 컨텐츠 리스트 패널 중앙에 위치한 바
        JLabel ottFillterbar = new JLabel(DataManagers.getInstance().getIcon("bar","rank_Page"));
        ottFillterbar.setBounds(30, 55, 536, 10);
        add(ottFillterbar);
         
        JLabel ottFillterTitle = new JLabel("이거볼래에서 추천하는 TOP10", SwingConstants.LEFT);
        ottFillterTitle.setBounds(30, 70, 250, 40); // 제목 위치 설정
        ottFillterTitle.setForeground(new Color(0x78DBA6));
        ottFillterTitle.setFont(DataManagers.getInstance().getFont("regular", 20));
        add(ottFillterTitle);
        
        //595 - 536 /2
        //랭킹 콘텐츠 리스트 패널 추가
        rankingContentPanel = createRankingContentPanel();
        add(rankingContentPanel);
        
    }
    
    //랭킹 콘텐츠 리스트 패널 생성 메서드
    private JPanel createRankingContentPanel() {
        int panelWidth = 595; // RankingPagePanel의 너비
        int contentPanelWidth = 535; // rankingContentPanel의 너비
        int contentPanelHeight = 431; // rankingContentPanel의 높이
        int x = (panelWidth - contentPanelWidth) / 2; // 중앙 정렬 계산
        
        RoundedPanel rankingContentPanel = new RoundedPanel(50, 50); // 모서리 둥근 패널 생성
        rankingContentPanel.setBounds(x, 110, contentPanelWidth, contentPanelHeight); // 크기 설정
        rankingContentPanel.setBackground(new Color(0xCBCBCB)); // 패널 배경색 설정
        rankingContentPanel.setLayout(null); // null 레이아웃 유지
        
        // 내부 스크롤 가능하도록 설정
        JPanel scrollableContent = new JPanel();
        scrollableContent.setLayout(null); // 내부 콘텐츠도 null 레이아웃
        scrollableContent.setPreferredSize(new Dimension(contentPanelWidth - 20, 800)); //스크롤 높이 설정
        
        //임시 리스트 텍스트 데이터가 스크롤 되는지 확인
        for (int i = 0; i < 20; i++) { // 20개의 더미 레이블 추가
            JLabel label = new JLabel("랭킹 콘텐츠 " + (i + 1));
            label.setBounds(20, i * 35, 200, 30); // 각 레이블 위치 설정
            label.setForeground(Color.BLACK);
            scrollableContent.add(label);
        }
        
        //스크롤 패널 생성 RoundedPanel 내부에 약간의 여백 남겨서 처리 
        scrollPane = new JScrollPane(scrollableContent);
        int padding = 7; // 둥근 테두리를 가리지 않도록 패딩 추가
        scrollPane.setBounds(padding, padding, contentPanelWidth - (padding * 2), contentPanelHeight - (padding * 2));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);//스크롤 속도 증가
        
        //중요: 뷰포트를 불투명하게 만들고 배경색 강제 적용
        scrollableContent.setBackground(new Color(0xCBCBCB)); // 내부 콘텐츠 배경색 설정
        scrollPane.getViewport().setOpaque(true); // 뷰포트 불투명하게 설정
        scrollPane.getViewport().setBackground(new Color(0xCBCBCB)); // 뷰포트 배경색 강제 설정
        scrollPane.setBackground(new Color(0xCBCBCB)); // JScrollPane 배경색 설정

        //스크롤 패널 테두리 제거
        scrollPane.setBorder(null); 

        //스크롤바 자체를 투명하게 설정 (숨김)
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // 스크롤바 크기 최소화
        scrollPane.getVerticalScrollBar().setOpaque(false); // 스크롤바 투명화

        //RoundedPanel 내부에 추가
        rankingContentPanel.add(scrollPane);

        return rankingContentPanel;
    }
    
    private JButton selectedButton = null; // 현재 활성화된 버튼 저장

    private void addOttFilterButtons() {

        String[] ottNames = { "전체", "넷플릭스", "티빙", "라프텔", "왓챠" };
        String[] ottIconNames = { "allIcon", "netflexIcon", "TvingIcon", "laftelIcon", "WatchaIcon" };
        //필터 활성화 버튼을 배열로 저장
        String[] bgOnNames = { "allBGOn", "netflexBGOn", "tivingBGOn", "laftelBGOn", "WatchaBGOn" };
        // 필터 비활성화 버튼을 배열로 저장
        String[] bgOffNames = { "allBGOff", "netflexBGOff", "tivingBGOff", "laftelBGOff", "WatchaBGOff" };
        String iconAndButtonPath = "rank_Page";
        
        int buttonWidth = 80; //버튼 넓이
        int buttonHeight = 60; //버튼 높이 
        int iconSize = 24; //버튼 아이콘 사이즈
        int spacing = 3; //각 버튼 간의 간격
        int startX = 20; //첫 버튼 시작 x 좌표
        int yPosition = 4; //y값

        for (int i = 0; i < ottNames.length; i++) {
            final int index = i;
            
            ImageIcon buttonBgOff = DataManagers.getInstance().getIcon(bgOffNames[i], iconAndButtonPath);
            ImageIcon buttonBgOn = DataManagers.getInstance().getIcon(bgOnNames[i], iconAndButtonPath);
            ImageIcon ottIcon = DataManagers.getInstance().getIcon(ottIconNames[i], iconAndButtonPath);
            
            JButton ottButton = new JButton(i == 0 ? buttonBgOn : buttonBgOff);
            ottButton.setBounds(startX + (i * (buttonWidth + spacing)), yPosition, buttonWidth, buttonHeight);
            ottButton.setBorderPainted(false);
            ottButton.setContentAreaFilled(false);
            ottButton.setFocusPainted(false);
            ottButton.setLayout(null);
            ottButton.setActionCommand(ottNames[i]);
            
            //무조건 전체 버튼이 디폴트 활성화 상태
            if (i == 0) {
                selectedButton = ottButton;
            }

            JLabel iconLabel = new JLabel(ottIcon);
            iconLabel.setBounds(10, (buttonHeight - iconSize) / 2, iconSize, iconSize);
            ottButton.add(iconLabel);

            JLabel textLabel = new JLabel(ottNames[i]);
            textLabel.setBounds(40, (buttonHeight - 20) / 2, 60, 20);
            textLabel.setForeground(Color.WHITE);
            textLabel.setFont(DataManagers.getInstance().getFont("bold", 11));
            ottButton.add(textLabel);
            
            ottButton.addActionListener(e -> {
                if (selectedButton != null) {
                    int prevIndex = java.util.Arrays.asList(ottNames).indexOf(selectedButton.getActionCommand());
                    // index 계산
                    selectedButton.setIcon(DataManagers.getInstance().getIcon(bgOffNames[prevIndex], iconAndButtonPath));
                }
                selectedButton = ottButton;
                ottButton.setIcon(buttonBgOn);
                
                System.out.println(ottNames[index] + " 필터 선택됨");
            });
            
            this.add(ottButton);
        }
    }
}