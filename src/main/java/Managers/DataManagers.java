package Managers;

import Data.AppConstants;
import VO.ItemVO;
import VO.UserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Local Data 위주 클래스
public class DataManagers {
    private static DataManagers dataManagers;

    private boolean isInitialized = false;
    //로드된 아이콘 맵 데이터
    private Map<String,ImageIcon> icons = new HashMap<>();
    //로드된 영화 데이터
    private Map<Integer, ItemVO> items = new HashMap<>();
    //로드된 썸네일 데이터
    private Map<String, ImageIcon> tempThumbnail = new HashMap<>();

    private UserVO myUser;

    //싱글턴 생성 로직
    public static DataManagers getInstance() {
        if (dataManagers == null) {
            dataManagers = new DataManagers();
            
        }
        return dataManagers;
    }

    public void InitDataManagers() {
        if(isInitialized)
        {
            System.out.println("Data Manager is already initialized");
            return;
        }

        loadItemsFromJson(AppConstants.ITEM_FILE_PATH);

        isInitialized = true;
    }

    public Map<String, ImageIcon> getTempThumbnail() {
        return tempThumbnail;
    }

    public UserVO getMyUser() {
		return myUser;
	}
    
    public void setMyUser(UserVO myUser) {
        this.myUser = myUser;
    }

	public ImageIcon getIcon(String iconName, String iconPath) {
        if(!isLoadedIcon(iconName))
            setLoadedIcon(iconName, iconPath);

        return icons.get(iconName);
    }

    //추 후 볼드. 레귤러, 작은 글씨 탕비을 나눌 예정임
    public Font getFont(String thinType, int size) {
        try {
            Font getFont = null;
            switch(thinType)
            {
                case "bold":
                    getFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/LINESeedKR-Bd.ttf"));
                    break;
                case "regular":
                    getFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/LINESeedKR-Rg.ttf"));
                    break;
                case "thin":
                    getFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/LINESeedKR-Th.ttf"));
                    break;
                default:
                    getFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/LINESeedKR-Rg.ttf"));
                    break;
            }
            return getFont.deriveFont(Font.BOLD, size); // 크기 18로 설정
        } catch (IOException | FontFormatException e) {
            return new Font("Arial", Font.BOLD, 18); // 오류 시 기본 폰트 사용
        }
    }

    public Map<Integer, ItemVO> getItems() {
        return items;
    }

    public ItemVO FindItemFromTitle(String itemName) {
        for(ItemVO item : items.values()) {
            if(item.getTitle().equals(itemName)) {
                System.out.println("\uD83D\uDC4D 아이템을 찾았습니다 : " + itemName);
                return item;
            }
        }

        System.out.println("\uD83E\uDD72 아이템을 못 찾았습니다 : " + itemName);
        return null;
    }

    public ItemVO FindItemFromId(Integer itemId) {
        for(ItemVO item : items.values()) {
            if(item.getId() == itemId) {
                System.out.println("\uD83D\uDC4D 아이템을 찾았습니다 : " + itemId);
                return item;
            }
        }

        System.out.println("\uD83E\uDD72 아이템을 못 찾았습니다 : " + itemId);
        return null;
    }

    //아이콘, 페이지(패스) 매개변수로 받음
    private void setLoadedIcon(String iconName, String iconPath) {
        // 원본 이미지 로드 (PNG가 알파 채널을 포함할 수 있음)
        BufferedImage originalImage = null;

        try {
            originalImage = ImageIO.read(new File("resources/" + iconPath + "/" + iconName + ".png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // 4배 축소한 크기 계산
        int newWidth = originalImage.getWidth() / 4;
        int newHeight = originalImage.getHeight() / 4;

        // 🔥 고품질 리사이징 적용 (안티앨리어싱 포함)
        BufferedImage resizedImage = getSmoothScaledImage(originalImage, newWidth, newHeight);

        // 새로운 ImageIcon 생성
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        icons.put(iconName, resizedIcon);
    }

    private static BufferedImage getSmoothScaledImage(BufferedImage image, int targetWidth, int targetHeight) {
        int currentWidth = image.getWidth();
        int currentHeight = image.getHeight();

        BufferedImage scaledImage = image;

        while (currentWidth / 2 >= targetWidth && currentHeight / 2 >= targetHeight) {
            currentWidth /= 2;
            currentHeight /= 2;
            scaledImage = scaleImage(scaledImage, currentWidth, currentHeight);
        }

        // 최종적으로 정확한 크기로 조정 (만약 정밀한 크기가 필요하면)
        if (currentWidth != targetWidth || currentHeight != targetHeight) {
            scaledImage = scaleImage(scaledImage, targetWidth, targetHeight);
        }

        return scaledImage;
    }

    // 고품질 리사이징을 수행하는 함수 (BICUBIC + 안티앨리어싱 적용)
    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();

        return resizedImage;
    }

    private Boolean isLoadedIcon(String iconName) {
        return icons.containsKey(iconName);
    }

    private void loadItemsFromJson(String filePath) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(filePath);

            // JSON을 List<ItemVO>로 변환
            Type listType = new TypeToken<List<ItemVO>>() {}.getType();
            List<ItemVO> itemList = gson.fromJson(reader, listType);
            reader.close();

            // items Map에 저장 (id를 키로 저장)
            items.clear();
            for (ItemVO item : itemList) {
                items.put(item.getId(), item);
            }

            System.out.println("✅ JSON 데이터 로드 완료! 총 " + items.size() + "개의 항목이 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("❌ JSON 파일 읽기 오류: " + e.getMessage());
        }
    }

    public List<ItemVO> getSearchItemKeyword(String keyword)
    {
        // 검색어와 두 글자 이상 일치하는 요소 찾기
        List<ItemVO> result = items.values().stream()
                .filter(item -> containsTwoCharSubstring(item.getTitle(), keyword))
                .collect(Collectors.toList());

        System.out.println(result);

        return result;
    }

    public static boolean containsTwoCharSubstring(String item, String query) {
        if (query.length() < 2) return false;

        for (int i = 0; i < item.length() - 1; i++) {
            String sub = item.substring(i, i + 2);
            if (query.contains(sub)) {
                return true;
            }
        }
        return false;
    }
    
    //아이콘 캐시초기화 
    public void clearIconCache() {
        this.icons.clear(); // 캐시 초기화
    }
}
