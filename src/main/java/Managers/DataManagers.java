package Managers;

import VO.UserVO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//Local Data 위주 클래스
public class DataManagers {
    private static DataManagers dataManagers;
    //로드된 아이콘 맵 데이터
    private Map<String,ImageIcon> icons = new HashMap<>();
    private UserVO myUser;

    //싱글턴 생성 로직
    public static DataManagers getInstance() {
        if (dataManagers == null) {
            dataManagers = new DataManagers();
        }
        return dataManagers;
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
            Font getFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/LINESeedKR-Bd.ttf"));
            return getFont.deriveFont(Font.BOLD, size); // 크기 18로 설정
        } catch (IOException | FontFormatException e) {
            return new Font("Arial", Font.BOLD, 18); // 오류 시 기본 폰트 사용
        }
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
}
