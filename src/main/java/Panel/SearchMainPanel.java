package Panel;

import Data.AppConstants;
import Data.EachContentData;
import Helper.ImageHelper;
import Managers.DBDataManagers;
import Managers.DataManagers;
import Component.CustomButton;
import DAO.FavoriteDAO;
import VO.FavoriteVO;
import VO.ItemVO;
import VO.RatingVO;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.stream.Collectors;

public class SearchMainPanel extends JPanel {

    private JTextField searchField;
    private JLabel searchedText;
    private JPanel bottomPanel;
    private JPanel itemPanel;
    private JLabel itemBG;
    private JScrollPane scrollPane;

    //생성자
    public SearchMainPanel() {

        //제일 큰 서칭 메인 패널 세팅
        setLayout(null); // 전체 레이아웃 설정
        setOpaque(false);
        setBounds(0, 0, 595, 550); // 패널 크기 설정
        setBackground(Color.decode(AppConstants.UI_BACKGROUND_HEX));

        //상단패널
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setOpaque(false);
        topPanel.setBounds(0, 0, 595, 117); // 패널 크기 설정

        // 검색 바 추가
        JLabel searchBar = createSearchBar();
        searchBar.setBounds(44, 22, 550, 50); // 검색 바 위치 조정
        topPanel.add(searchBar);

        // 검색 바 가로지는 바 추가
        JLabel bar = new JLabel(DataManagers.getInstance().getIcon("searchLongBar", "search_Main_Page"));
        bar.setBounds(31,98,534,6);
        topPanel.add(bar);

        //상단 패널 추가
        add(topPanel);

        //하단 컨텐츠 패널
        bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        bottomPanel.setOpaque(false);
        bottomPanel.setBounds(0, 117, 595, 433); // 패널 크기 설정
        bottomPanel.setBackground(Color.decode(AppConstants.UI_BACKGROUND_HEX));

        bottomPanel.add(searchedText = SetResultText("이거볼래”에서 추천하는 TOP 10"));

        itemBG = new JLabel(DataManagers.getInstance().getIcon("searchItemsBG", "search_Main_Page"));
        itemBG.setLayout(null);
        itemBG.setOpaque(false);
        itemBG.setBounds(31,47,536,367);

        itemPanel = new JPanel();
        itemPanel.setOpaque(false);
        itemPanel.setLayout(null);
        itemPanel.setBounds(31,47,536,367);

        EachContentData eachContentData = new EachContentData(
                DataManagers.getInstance().getIcon("itemBG", "search_Main_Page"),
                new Rectangle(0,0,113,162),
                null,
                new Rectangle(7,6,99,121),
                SwingConstants.CENTER,
                new Rectangle(0,125, 0, 30),
                14,
                DataManagers.getInstance().getIcon("smallRatingIcon", "search_Main_Page"),
                new Rectangle(9,146,8,8),
                new Rectangle(18,142,30,20),
                6,
                new Rectangle(0,143, 0, 20),
                5,
                new Rectangle(35,146,41,10)
        );

        List<ItemVO> subList = getFirstNItems(10);
        bottomPanel.add(setScrollLayoutNull(itemPanel, eachContentData, subList,113,162,4,4,10,10,10,false));
        bottomPanel.add(itemBG);

        //하단 패널 추가
        add(bottomPanel);
    }

    public List<ItemVO> getFirstNItems(int n) {
        return DataManagers.getInstance().getItems().values().stream()
                .limit(n) // 처음 n개만 선택
                .collect(Collectors.toList());
    }

    //기능함수들
    private JLabel createSearchBar() {
        String watchWantText = "보고싶은 작품을 검색해 보세요!";

        JLabel searchBG = new JLabel();
        searchBG.setIcon(DataManagers.getInstance().getIcon("searchBar", "search_Main_Page"));

        searchField = new JTextField(watchWantText);
        searchField.setBounds(26, 17, 419, 30);
        searchField.setFont(DataManagers.getInstance().getFont("", 16));
        searchField.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
        searchField.setOpaque(false);
        searchField.setBorder(null);

        // 입력하면 글자 색 바꾸기 입력한 텍스트는 원래 색상 복구
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                // 기본 텍스트가 있는지 확인하고 삭제
                if (searchField.getText().equals(watchWantText)) {
                    searchField.setText(""); // 기본 텍스트 지우기
                    searchField.setForeground(Color.BLACK); // 텍스트 색상 검은색으로 변경
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                // 텍스트 필드가 비어 있으면 기본 텍스트를 복원
                if (searchField.getText().isEmpty()) {
                    searchField.setText(watchWantText); // 기본 텍스트 설정
                    searchField.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX)); // 텍스트 색상 회색으로 변경
                }
            }
        });

        CustomButton searchBtn = new CustomButton(DataManagers.getInstance().getIcon("searchIcon", "search_Main_Page"));
        searchBtn.setBounds(445, 5, 39, 39);
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Search button clicked");
                //검색 기능 호출
                System.out.println(searchField.getText());
                EditResultText(String.format("'%s'에 대한 검색 결과", searchField.getText()), searchedText);
                SearchItems(searchField.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });

        searchBG.add(searchField);
        searchBG.add(searchBtn);

        return searchBG;
    }

    private void SearchItems(String searchText) {
        //검색 페이지로 넘기기
        List<ItemVO> findList= new ArrayList<>();
        findList = DataManagers.getInstance().getSearchItemKeyword(searchText);
        for (ItemVO itemVO : findList) {
            System.out.println(itemVO.getTitle());
        }

        bottomPanel.remove(itemBG);
        bottomPanel.remove(itemPanel);
        bottomPanel.remove(scrollPane);

        itemPanel.setOpaque(false);
        itemPanel.setBackground(new Color(0, 0, 0, 0)); // 🔥 완전 투명 배경 설정
        itemPanel.setLayout(null);
        itemPanel.setBounds(31,47,536,367);

        //검색 아이콘 세팅
        EachContentData eachContentData = new EachContentData(
                DataManagers.getInstance().getIcon("searchItemBG", "search_Result_Page"),
                new Rectangle(12,13,510,126),
                null,
                new Rectangle(35,12,79,99),
                SwingConstants.LEFT,
                new Rectangle(128,40, 288, 30),
                18,
                DataManagers.getInstance().getIcon("ratingIcon", "search_Result_Page"),
                new Rectangle(450,15,28,28),
                new Rectangle(456,53,31,14),
                12,
                new Rectangle(-105,24, 47, 18),
                6,
                new Rectangle(128,23,47,17)
        );

        bottomPanel.add(setScrollLayoutNull(itemPanel, eachContentData, findList,510,126, findList.size(), 1,10,10,10,true));

        if(findList.isEmpty()){
            JLabel noResultText =new JLabel(String.format("'%s'에 대한 검색 결과가 없습니다!", searchText));
            noResultText.setFont(DataManagers.getInstance().getFont("bold", 20));
            noResultText.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
            noResultText.setBounds(165,140,536,200);
            bottomPanel.add(noResultText);
        }

        bottomPanel.add(itemBG);
    }

    private JLabel SetResultText(String searchText) {
        JLabel recommendText = new JLabel(searchText);
        recommendText.setBounds(31,12,434,28);
        recommendText.setFont(DataManagers.getInstance().getFont("regular", 16));
        recommendText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
        return recommendText;
    }

    private void EditResultText(String searchText, JLabel recommendText) {
        recommendText.setText(searchText);
    }

    private void showContentDetails(ItemVO content) {
        OpenPage openPage = new OpenPage();
        openPage.openContentPage(content);
    }

    private JLabel createItem(ItemVO itemVO, EachContentData eachContentData, boolean isHorizontal) {
        //기본 배경 세팅
        CustomButton itemLabel = new CustomButton(eachContentData.getBgImage());
        itemLabel.setBorder(null);
        itemLabel.setBounds(eachContentData.getBgRect());

        itemLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    showContentDetails(itemVO);
                }
        });

        //썸네일 세팅
        JLabel thumbNail = new JLabel(eachContentData.getThumbImage());
        thumbNail.setBounds(eachContentData.getThumbRect());
        itemLabel.add(thumbNail);

        //타이틀 세팅
        JLabel titleText = new JLabel(itemVO.getTitle(),eachContentData.getConstants());
        titleText.setHorizontalAlignment(eachContentData.getConstants());
        titleText.setBorder(null);
        titleText.setBounds(eachContentData.getTitleRect().x,eachContentData.getTitleRect().y,
                itemLabel.getWidth(), eachContentData.getTitleRect().height);
        titleText.setFont(DataManagers.getInstance().getFont("bold",eachContentData.getTitleSize()));
        titleText.setForeground(Color.decode(AppConstants.UI_BACKGROUND_HEX));
        itemLabel.add(titleText);

        // 장르 텍스트가 7글자 초과시 .. 처리
        String title = titleText.getText();
        int titleOver = 21;// 초과 제한 될 글자 개수
        if (titleText.getText().length() > titleOver) {
            titleText.setText(title.substring(0, 21) + "..");
        }

        //평점 세팅
        JLabel ratingIcon = new JLabel(eachContentData.getRatingIcon());
        ratingIcon.setBounds(eachContentData.getRatingRect());
        itemLabel.add(ratingIcon);

        RatingVO findRating = null;
        for (RatingVO ratingVO : DBDataManagers.getInstance().getDbRatingData()) {
            if(ratingVO.getItemId()==itemVO.getId()){
                findRating = ratingVO;
            }
        }
        String rating = ( findRating == null || findRating.getRatingPoint() == 0 || findRating.getRatingPoint() == 0.0 )
                ? "0.0" : String.valueOf(findRating.getRatingPoint());

        JLabel ratingText = new JLabel(rating);
        ratingText.setBounds(eachContentData.getRatingTextRect());
        ratingText.setFont(DataManagers.getInstance().getFont("bold", eachContentData.getRatingTextSize()));
        ratingText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
        ratingText.setHorizontalAlignment(SwingConstants.LEFT); // 왼쪽 정렬 유지
        ratingText.setVerticalAlignment(SwingConstants.CENTER); // 세로 중앙 정렬
        itemLabel.add(ratingText);

        // 장르 텍스트가 7글자 초과시 .. 처리
        String genresText = itemVO.getGenres();
        int overNum = 7;// 초과 제한 될 글자 개수
        if (genresText.length() > overNum) {
            genresText = genresText.substring(0, 7) + "..";
        }

        //장르 세팅
        JLabel genreText = new JLabel(genresText, SwingConstants.CENTER);
        genreText.setHorizontalAlignment(SwingConstants.CENTER);
        genreText.setBorder(null);
        genreText.setBounds(eachContentData.getCategoryTextRect().x,eachContentData.getCategoryTextRect().y, itemLabel.getWidth(), eachContentData.getCategoryTextRect().height);
        genreText.setFont(DataManagers.getInstance().getFont("bold", eachContentData.getCategoryTextSize()));
        genreText.setForeground(Color.decode(AppConstants.UI_POINT_COLOR_HEX));
        itemLabel.add(genreText);

        JLabel genreBG = new JLabel(DataManagers.getInstance().getIcon("longCategoryBG", "search_Main_Page"));
        genreBG.setBounds(eachContentData.getCategoryBgRect());
        itemLabel.add(genreBG);

     // --------------------------- Serch 찜 버튼 ---------------------------
     		// 로그인 유무에 따라 찜버튼 활성화 비활성화
     		boolean isLogin = DataManagers.getInstance().getMyUser() != null;

     		// 로그인 상태라면 찜버튼을 생성 및 추가
     		if (isLogin) {
     			// 우측 하단 (찜하기 버튼)
     			JButton favoriteButton = new JButton(); // 버튼 생성

     	        // 초기 찜 여부 확인
     	        String userId = DataManagers.getInstance().getMyUser().getId();
     	        int currentContentId = itemVO.getId();
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
     	                isContains ? "ggimOn" : "ggimOff", "search_Main_Page"));

     	        // 버튼 속성 설정
     	        favoriteButton.setBounds(eachContentData.getCategoryTextRect().x + 40,eachContentData.getCategoryTextRect().y - 1, itemLabel.getWidth(), eachContentData.getCategoryTextRect().height);
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
     	                    nowContains ? "ggimOn" : "ggimOff", "search_Main_Page"));
     	        });

     			// 찜버튼 UI 배치
     	       itemLabel.add(favoriteButton);
     		}
/*
        //찜하기 세팅
        Icon on = DataManagers.getInstance().getIcon("ggimOn", "search_Main_Page");
        Icon off = DataManagers.getInstance().getIcon("ggimOff", "search_Main_Page");

//        if(DataManagers.getInstance().getMyUser() != null){
//            itemLabel.add(createFavoritesButton(itemVO, on, off, 93, 147, 8));
//        }
*/
        //개봉 연도 세팅
        if(isHorizontal)
        {
            JLabel promotionData = new JLabel(itemVO.getPromotionYear());
            promotionData.setFont(DataManagers.getInstance().getFont("regular", 12));
            promotionData.setForeground(Color.decode(AppConstants.UI_MAIN_TEXT_HEX));
            promotionData.setBounds(130,70,51,20);
            itemLabel.add(promotionData);
        }
        return itemLabel;
    }

    private JScrollPane setScrollLayoutNull(JPanel itemPanel, EachContentData data, List<ItemVO> items,
                                            int perWidth, int perHeight, int rows, int cols,
                                            int hGap, int vGap, int padding, boolean isRowOnly) {
        int lastItemX = 0;
        int lastItemY = 0;

        // 🌟 가로 스크롤 모드일 경우: 1행만 사용
        if (isRowOnly) {
            rows = items.size() / cols + (items.size() % cols == 0 ? 0 : 1); // 필요한 행 개수 계산
        }
        itemPanel.removeAll(); // 기존 아이템 제거
        itemPanel.revalidate();
        itemPanel.repaint();

        for (int i = 0; i < items.size(); i++) {
            int row = i / cols; // 🟢 줄바꿈 적용
            int col = i % cols;

            int x = padding + col * (perWidth + hGap);
            int y = padding + row * (perHeight + vGap); // 🔥 row 증가 반영

            lastItemX = x + perWidth;
            lastItemY = y + perHeight; // 🔥 마지막 Y값 업데이트
            ItemVO itemVO = items.get(i);

            data.setThumbImage(ImageHelper.getResizedImageIconFromUrl(itemVO.getThumbnail(), data.getThumbRect().width, data.getThumbRect().height));
            JLabel item = createItem(itemVO, data, isRowOnly);
            item.setOpaque(false);
            item.setBounds(isRowOnly ? 0 : x, y, perWidth, perHeight);
            itemPanel.add(item);
        }

        // 🚀 패널 크기 강제 설정 (세로 크기 고려)
        int panelWidth = isRowOnly ? lastItemX + padding : 536; // 🔥 고정된 너비 사용
        int panelHeight = lastItemY + padding; // 🔥 아이템 끝 + 여유 공간 추가
        itemPanel.setPreferredSize(new Dimension(panelWidth, (panelHeight - 20))); // **스크롤 가능하도록 설정**

        // 🚀 **스크롤 패널 설정**
        scrollPane = new JScrollPane(itemPanel);
        scrollPane.setBounds(46, 53, 536, (367 - 20)); // **JScrollPane 크기 고정**
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 🌟 세로 스크롤 활성화
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 🌟 가로 스크롤 제거
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        // 🚀 Viewport도 완전히 투명하게 설정
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));

        // 🚀 JScrollPane 자체도 투명하게 설정
        scrollPane.setOpaque(false);
        scrollPane.setBackground(new Color(0, 0, 0, 0));
        scrollPane.setBorder(null);

        // 🚀 bottomPanel도 투명하게 설정
        bottomPanel.setOpaque(false);
        bottomPanel.setBackground(new Color(0, 0, 0, 0));

        // 🚀 itemBG를 맨 뒤로 배치하여 배경이 유지되도록 설정
        bottomPanel.add(itemBG, 0);
        bottomPanel.revalidate();
        bottomPanel.repaint();

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        return scrollPane;
    }
}