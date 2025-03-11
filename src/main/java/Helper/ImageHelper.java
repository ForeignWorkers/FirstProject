package Helper;

import Managers.DataManagers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageHelper {

    /**
     * URL에서 이미지를 가져오는 메서드 (User-Agent 및 Referer 추가, WebP 지원)
     */
    public static BufferedImage getImageFromUrl(String urlString) {
        try {
            System.out.println("🔍 이미지 URL 요청: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // User-Agent 추가
            connection.setRequestProperty("Referer", "https://kinolights.com"); // Referer 추가
            connection.setRequestProperty("Accept", "image/webp,image/png,image/jpeg,image/*");
            connection.setConnectTimeout(3000); // 연결 타임아웃 (5초)
            connection.setReadTimeout(3000);    // 읽기 타임아웃 (5초)
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.connect();

            // HTTP 응답 코드 체크
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("⚠️ 이미지 로드 실패! 응답 코드: " + responseCode);
                return null;
            }

            // 🚀 데이터를 메모리에 저장 후 변환 (서버 응답 지연 대응)
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            inputStream.close();

            // 🔥 빈 데이터 확인
            byte[] imageBytes = buffer.toByteArray();
            if (imageBytes.length == 0) {
                System.out.println("⚠️ 빈 이미지 데이터 수신! URL: " + urlString);
                return null;
            }

            // 🚀 WebP 지원을 위한 ImageIO 사용 (TwelveMonkeys 라이브러리 필요)
            return ImageIO.read(new ByteArrayInputStream(imageBytes));

        } catch (Exception e) {
            System.out.println("❌ 이미지 로드 오류: " + e.getMessage());
            return null;
        }
    }

    /**
     * 주어진 URL 문자열을 사용하여 지정한 크기의 ImageIcon을 반환하는 메서드
     */
    public static ImageIcon getResizedImageIconFromUrl(String urlString, int width, int height, int itemID) {
        //아이템 아이디 체크
        if(DataManagers.getInstance().getTempThumbnail().containsKey(itemID)){
            return DataManagers.getInstance().getTempThumbnail().get(itemID);
        }

        BufferedImage image = getImageFromUrl(urlString);
        System.out.println(image);
        if (image != null) {
            BufferedImage resizedImage = resizeImage(image, width, height);
            ImageIcon icon = new ImageIcon(resizedImage);
            DataManagers.getInstance().getTempThumbnail().put(itemID,icon);
            return icon;
        } else {
            return getDefaultImageIcon(width, height); // 기본 아이콘 반환
        }
    }

    /**
     * BufferedImage를 지정한 크기로 리사이징하는 메서드
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // 🔥 고품질 리사이징 옵션 적용
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resizedImage;
    }

    /**
     * 기본 아이콘을 지정된 크기로 생성하여 반환하는 메서드 (이미지 로드 실패 시 대비)
     */
    private static ImageIcon getDefaultImageIcon(int width, int height) {
        BufferedImage defaultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = defaultImage.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("No Image", width / 4, height / 2);
        g2d.dispose();
        return new ImageIcon(defaultImage);
    }
}