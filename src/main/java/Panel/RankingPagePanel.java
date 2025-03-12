package Panel;

import javax.swing.*;

import DAO.FavoriteDAO;
import Data.AppConstants;
import Helper.GenericFinder;
import Helper.ImageHelper;
import Helper.SmoothLabel;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.FavoriteVO;
import VO.ItemVO;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RankingPagePanel extends JPanel {
    
    private JPanel rankingContentPanel; // 랭킹 콘텐츠 리스트 패널
    private JScrollPane rankingListScroll; // 스크롤 패널
    private JPanel RankingListPanel; // 스크롤 가능한 콘텐츠 패널
    private JButton selectedOttFilterButton = null; // 현재 선택된 OTT 필터 버튼 저장

    
    public RankingPagePanel() {
        setLayout(null); // 절대 위치 설정
        setBackground(Color.decode(AppConstants.UI_BACKGROUND_HEX)); // 배경색 설정
        setBounds(0, 0, 600, 600); // 패널 크기 설정

        //OTT 필터 버튼 추가 (rankingContentPanel 외부 상단에 배치)
        addOttFilterButtons();
        
        // 상단 구분선 바
        JLabel ottFillterBar = new JLabel(DataManagers.getInstance().getIcon("bar","rank_Page"));
        ottFillterBar.setBounds(30, 55, 536, 10);
        add(ottFillterBar);
         
        // 리스트 타이틀 텍스트
        JLabel ottListTitle = new JLabel("이거볼래에서 추천하는 TOP10", SwingConstants.LEFT);
        ottListTitle.setBounds(30, 70, 300, 40); // 제목 위치 설정
        ottListTitle.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
        ottListTitle.setFont(DataManagers.getInstance().getFont("regular", 20));
        add(ottListTitle);
        
        //랭킹 콘텐츠 리스트 패널 추가
        rankingContentPanel = createRankingContentPanel();
        add(rankingContentPanel);
        
    	//최초 실행 시 전체 필터 적용
        updateRankingContentPanel("전체");
        rankingContentPanel.repaint();
        rankingContentPanel.revalidate();
    }
    
    //랭킹 콘텐츠 리스트 패널 생성 메서드
    private JPanel createRankingContentPanel() {
        int rankingPagePanelWidth = 595; // RankingPagePanel의 너비
        int rankingContentPanelWidth = 535; // rankingContentPanel의 너비
        int rankingContentPanelHeight = 431; // rankingContentPanel의 높이
        int x = (rankingPagePanelWidth - rankingContentPanelWidth) / 2; // 중앙 정렬 계산
        
        RoundedPanel rankingContentPanel = new RoundedPanel(50, 50); // 모서리 둥근 패널 생성
        rankingContentPanel.setBounds(x, 110, rankingContentPanelWidth, rankingContentPanelHeight); // 크기 설정
        rankingContentPanel.setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // 패널 배경색 설정
        rankingContentPanel.setLayout(null); // null 레이아웃 유지
        
        // 내부 스크롤 가능하도록 설정
        RankingListPanel = new JPanel();
        RankingListPanel.setLayout(null); // 내부 콘텐츠도 null 레이아웃
        RankingListPanel.setPreferredSize(new Dimension(rankingContentPanelWidth - 20, 1370)); //스크롤 높이 설정
        
        //스크롤 생성 RoundedPanel 내부에 약간의 여백 남겨서 처리 
        rankingListScroll = new JScrollPane(RankingListPanel);
        int padding = 7; // 둥근 테두리를 가리지 않도록 패딩 추가
        rankingListScroll.setBounds(padding, padding, rankingContentPanelWidth - (padding * 2), rankingContentPanelHeight - (padding * 2));
        rankingListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rankingListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rankingListScroll.getVerticalScrollBar().setUnitIncrement(10);//스크롤 속도 증가
        
        //뷰포트를 불투명하게 만들고 배경색 강제 적용
        RankingListPanel.setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // 내부 콘텐츠 배경색 설정
        rankingListScroll.getViewport().setOpaque(true); // 뷰포트 불투명하게 설정
        rankingListScroll.getViewport().setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // 뷰포트 배경색 강제 설정
        rankingListScroll.setBackground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // JScrollPane 배경색 설정

        //스크롤 패널 테두리 제거
        rankingListScroll.setBorder(null); 

        //스크롤바 자체를 투명하게 설정 (숨김)
        rankingListScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // 스크롤바 크기 최소화
        rankingListScroll.getVerticalScrollBar().setOpaque(false); // 스크롤바 투명화

        //RoundedPanel 내부에 추가
        rankingContentPanel.add(rankingListScroll);

        return rankingContentPanel;
    }
    
    //OTT 필터 버튼 생성 및 추가
    private void addOttFilterButtons() {
        String[] ottNames = { "전체", "넷플릭스", "티빙", "라프텔", "왓챠" };
        String[] ottIconNames = { "allIcon", "netflexIcon", "TvingIcon", "laftelIcon", "WatchaIcon" };
        //필터 활성화 버튼을 배열로 저장
        String[] bgOnNames = { "allBGOn", "netflexBGOn", "tivingBGOn", "laftelBGOn", "WatchaBGOn" };
        // 필터 비활성화 버튼을 배열로 저장
        String[] bgOffNames = { "allBGOff", "netflexBGOff", "tivingBGOff", "laftelBGOff", "WatchaBGOff" };
        String iconAndButtonPath = "rank_Page";
        
        int buttonHeight = 60; // 버튼 높이 (고정)
        int iconSize = 24; // 버튼 아이콘 크기
        int spacing = 10; // 각 버튼 간의 간격
        int startX = 30; // 첫 버튼 시작 x 좌표
        int yPosition = 4; // y 좌표

        for (int i = 0; i < ottNames.length; i++) {
                final int index = i;
                //ott 필터링 버튼 세팅
                ImageIcon ottButtonOff = DataManagers.getInstance().getIcon(bgOffNames[i], iconAndButtonPath);
                ImageIcon ottButtonOn = DataManagers.getInstance().getIcon(bgOnNames[i], iconAndButtonPath);
                int buttonWidth = ottButtonOff.getIconWidth(); // 버튼 너비를 이미지 크기에 맞춤
                //ott 아이콘 세팅
                ImageIcon ottIcon = DataManagers.getInstance().getIcon(ottIconNames[i], iconAndButtonPath);

                JButton ottButton = new JButton(i == 0 ? ottButtonOn : ottButtonOff);
                ottButton.setBounds(startX, yPosition, buttonWidth, buttonHeight); // x 값 유지
            ottButton.setBorderPainted(false);
            ottButton.setContentAreaFilled(false);
            ottButton.setFocusPainted(false);
            ottButton.setLayout(null);
            ottButton.setActionCommand(ottNames[i]);
            
            // 무조건 전체 버튼이 디폴트 활성화 상태
            if (i == 0) {
                selectedOttFilterButton = ottButton;
            }
            
            //ott icon 위치 세팅
            JLabel ottButtonIconLabel = new JLabel(ottIcon);
            ottButtonIconLabel.setBounds(5, (buttonHeight - iconSize) / 2, iconSize, iconSize);
            ottButton.add(ottButtonIconLabel);
            
            //ott text 위치 세팅
            JLabel ottButtontextLabel = new JLabel(ottNames[i], SwingConstants.CENTER);
            ottButtontextLabel.setBounds(10, (buttonHeight - 15) / 2, buttonWidth, 20); // 버튼 내부에서 정렬
            ottButtontextLabel.setForeground(Color.WHITE);
            ottButtontextLabel.setFont(DataManagers.getInstance().getFont("bold", 11));
            ottButton.add(ottButtontextLabel);
            
            // 클릭 이벤트: 필터링 및 버튼 아이콘 변경
            ottButton.addActionListener(e -> {
                if (selectedOttFilterButton != null) {
                    int prevIndex = Arrays.asList(ottNames).indexOf(selectedOttFilterButton.getActionCommand());
                    selectedOttFilterButton.setIcon(DataManagers.getInstance().getIcon(bgOffNames[prevIndex], iconAndButtonPath));
                }
                selectedOttFilterButton = ottButton;
                ottButton.setIcon(ottButtonOn);
                updateRankingContentPanel(ottNames[index]);
            });
            
            this.add(ottButton);
            
            //다음 버튼의 x 위치를 현재 버튼 너비 + spacing 만큼 이동
            startX += buttonWidth + spacing;
        }
    }
    
    // 필터링후 랭킹 콘텐츠 업데이트
    private void updateRankingContentPanel(String selectedPlatform) {
        RankingListPanel.removeAll();

        //전체 데이터 가져오기
        Collection<ItemVO> allItems = DataManagers.getInstance().getItems().values();

        //플랫폼 필터링
        List<ItemVO> filteredItems = allItems.stream()
                .filter(item -> filterByPlatform(item, selectedPlatform)) //필터링 별도 메서드로 분리
                //리스트 데이터를 getid기준으로 오름차순 정렬
                .sorted(Comparator.comparingInt(ItemVO::getId)) // id로 정렬
                .limit(10) // 상위 10개
                //다시 리스트로 묶음
                .collect(Collectors.toList());
        
        // 콘텐츠 라벨 추가
        int yOffset = 10; //라벨 y 간격
        for (ItemVO item : filteredItems) {
            JLabel rankingItemLabel = createRankingItemLabel(item, filteredItems.indexOf(item) + 1);
            
            //RankingContentBG scrollableContent 기준 센터 정렬
            int xCenter = (RankingListPanel.getPreferredSize().width - rankingItemLabel.getPreferredSize().width) / 2;
            
            rankingItemLabel.setBounds(xCenter + 3, yOffset, 510, 126);
            RankingListPanel.add(rankingItemLabel);
            yOffset += 136;
        }

        RankingListPanel.revalidate();
        RankingListPanel.repaint();
    }
    
    //플랫폼 필터링 조건 메서드
    private boolean filterByPlatform(ItemVO item, String selectedPlatform) {
    	// 전체 버튼이면 true
        if (selectedPlatform.equals("전체")) {
        	
        	return true;
        }
        // 아니면 실행
        if (item.getCategory() == null) {
        	
        	return false;
        }
        return item.getCategory().stream()
                .anyMatch(map -> map.get("platform").equals(selectedPlatform));
    }
    
    
    private JLabel createRankingItemLabel(ItemVO item, int rank) {
    	//랭킹 콘텐츠 배경
        JLabel rankingLabel = new JLabel(DataManagers.getInstance().getIcon("RankingContentBG", "rank_Page"));
        rankingLabel.setLayout(null);
        
        //순위 라벨
        JLabel rankLabel = new JLabel(String.valueOf(rank), SwingConstants.LEFT);
        rankLabel.setBounds(17, 47, 30, 40);
        rankLabel.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
        rankLabel.setFont(DataManagers.getInstance().getFont("bold", 25));
        rankingLabel.add(rankLabel);

        //썸네일 버튼
        JButton thumbnailButton = new JButton(ImageHelper.getResizedImageIconFromUrl(item.getThumbnail(), 79, 99,item.getId(), false, true));
        thumbnailButton.setBounds(50, 14, 79, 99);
        thumbnailButton.setBorderPainted(false);
        thumbnailButton.setContentAreaFilled(false);
        thumbnailButton.setFocusPainted(false);
        thumbnailButton.addActionListener(e -> showContentDetails(item));
        rankingLabel.add(thumbnailButton);

        //장르 바
        JLabel categoryLabel = new JLabel(DataManagers.getInstance().getIcon("category", "rank_Page"));
        categoryLabel.setBounds(122, 22, 80, 18);
        rankingLabel.add(categoryLabel);
        
        // 장르 텍스트가 7글자 초과시 .. 처리
     	String genreTextlength = item.getGenres();
     	int overNum = 8;// 초과 제한 될 글자 개수
     	if (genreTextlength.length() > overNum) {
     		genreTextlength = genreTextlength.substring(0, 7) + "..";
     		}
        
        //장르 텍스트
     	SmoothLabel genreText = new SmoothLabel(genreTextlength);
     	genreText.setHorizontalAlignment(SwingConstants.CENTER);
        genreText.setBounds(0, 2, 80, 18);
        genreText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
        genreText.setFont(DataManagers.getInstance().getFont("bold", 7));
        categoryLabel.add(genreText);

        //타이틀
        JLabel titleLabel = new JLabel(item.getTitle(), SwingConstants.LEFT);
        titleLabel.setBounds(140, 50, 250, 20);
        titleLabel.setFont(DataManagers.getInstance().getFont("bold", 15));
        rankingLabel.add(titleLabel);

        //개봉일
        JLabel dateLabel = new JLabel(item.getPromotionDay(), SwingConstants.LEFT);
        dateLabel.setBounds(140, 75, 300, 15);
        dateLabel.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
        dateLabel.setFont(DataManagers.getInstance().getFont("bold", 10));
        rankingLabel.add(dateLabel);

        //평점 가져오기
        String rating = GenericFinder.findInList(DBDataManagers.getInstance().getDbRatingData(),
                findItem -> findItem.getItemId() == item.getId(),
                findItem -> Double.toString(findItem.getRatingPoint()));

        if (rating == null || rating.equals("0") || rating.equals("0.0")) {
            rating = "0.0";
        }

        //평점 아이콘
        JLabel ratingIcon = new JLabel(DataManagers.getInstance().getIcon("ratingIcon", "rank_Page"));
        ratingIcon.setBounds(450, 15, 31, 29);
        rankingLabel.add(ratingIcon);

        //평점 텍스트
        JLabel ratingText = new JLabel(rating, SwingConstants.CENTER);
        ratingText.setBounds(440, 45, 50, 20);
        ratingText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
        ratingText.setFont(DataManagers.getInstance().getFont("bold", 12));
        rankingLabel.add(ratingText);
        
        // --------------------------- ranking 찜 버튼 ---------------------------
        // 로그인 유무에 따라 찜버튼 활성화 비활성화
        boolean isLogin = DataManagers.getInstance().getMyUser() != null;
        
        if (isLogin) {
			// 우측 하단 (찜하기 버튼)
			JButton favoriteButton = new JButton(); // 버튼 생성

	        // 초기 찜 여부 확인
	        String userId = DataManagers.getInstance().getMyUser().getId();
	        int currentContentId = item.getId();
	        FavoriteVO myFavoriteVO = null;

	        for (FavoriteVO find : DBDataManagers.getInstance().getDbFavoriteData()) {
	            if (find.getUserId().equals(userId)) {
	                myFavoriteVO = find;
	                break;
	            }
	        }

	        if (myFavoriteVO == null) {
	            myFavoriteVO = new FavoriteVO();
	            myFavoriteVO.setUserId(userId);
	        }

	        boolean isContains = myFavoriteVO.getMyFavoriteList().contains(currentContentId);
	        favoriteButton.setIcon(DataManagers.getInstance().getIcon(
	                isContains ? "ggimIconOn" : "ggimIconOff", "rank_Page"));

	        // 버튼 속성 설정
	        favoriteButton.setBounds(450, 84, 30, 30);
	        favoriteButton.setBorderPainted(false);
	        favoriteButton.setContentAreaFilled(false);
	        favoriteButton.setFocusPainted(false);
	        favoriteButton.setMargin(new Insets(0, 0, 0, 0));

	        // 토글 클릭 이벤트
	        FavoriteVO finalMyFavoriteVO = myFavoriteVO;
	        favoriteButton.addActionListener(e -> {
	        	
	            boolean isNowContains = finalMyFavoriteVO.getMyFavoriteList().contains(currentContentId);
	            if (isNowContains) {
	                finalMyFavoriteVO.getMyFavoriteList().remove(Integer.valueOf(currentContentId));
	            } else {
	                finalMyFavoriteVO.getMyFavoriteList().add(currentContentId);
	            }

	            System.out.println("현재 찜 목록: " + finalMyFavoriteVO.getMyFavoriteList());

	            FavoriteDAO favoriteDAO = new FavoriteDAO();
	            favoriteDAO.setLocalFavoriteData(finalMyFavoriteVO, currentContentId);
	            try {
	                favoriteDAO.addFavoriteToJson(finalMyFavoriteVO, AppConstants.FAVORITE_FILE_NAME, AppConstants.FOLDER_ID);
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	            
	            boolean nowContains = finalMyFavoriteVO.getMyFavoriteList().contains(currentContentId);
	            favoriteButton.setIcon(DataManagers.getInstance().getIcon(
	                    nowContains ? "ggimIconOn" : "ggimIconOff", "rank_Page"));
	        });    
            // 찜 버튼 UI 배치
            rankingLabel.add(favoriteButton);
        }

        return rankingLabel;
    }
    
    // 컨텐츠 페이지 이동
 	private void showContentDetails(ItemVO content) {
 		OpenPage openPage = new OpenPage();
 		openPage.openContentPage(content);// openpage의 opencontentpage호출
 	}
}