package Helper;

import Managers.DataManagers;
import net.coobird.thumbnailator.Thumbnails; 
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    private static Map<String, ImageIcon> imageCache = new HashMap<>();
    
    public static ImageIcon getResizedImageIconFromUrl(String urlString, int width, int height, int itemID, boolean topRounded, boolean fullRounded) {
    	String cacheKey = itemID + "_" + (topRounded ? "TOPROUND" : (fullRounded ? "FULLROUND" : "NORMAL"));
    	
    	//아이템 아이디 체크
    	if (DataManagers.getInstance().getTempThumbnail().containsKey(cacheKey)) {
            return DataManagers.getInstance().getTempThumbnail().get(cacheKey);
        }

    	BufferedImage image = getImageFromUrl(urlString);
        if (image != null) {
        	//Thumbnailator 이용해서 고품질 리사이징
            BufferedImage resizedImage = resizeImage(image, width, height);
            
            // 라운드 옵션 체크
            if (topRounded) {
                resizedImage = applyTopRoundCorners(resizedImage, 14, 14); // 네 귀퉁이 14px 라운드
            } else if (fullRounded) {
                resizedImage = applyFullRoundCorners(resizedImage, 14, 14); // 상단만 14px 라운드
            }

            ImageIcon icon = new ImageIcon(resizedImage);
            DataManagers.getInstance().getTempThumbnail().put(cacheKey, icon); // 캐시 저장
            return icon;
        } else {
            return getDefaultImageIcon(width, height); // 기본 아이콘 반환
        }
    }
    
    public static void clearImageCache() {
        imageCache.clear(); // 기존 내부 캐시 초기화
        DataManagers.getInstance().getTempThumbnail().clear(); // ⭐ DataManagers 캐시도 함께 초기화
    }
    
    /**
     * 상단 좌우만 라운드 처리된 BufferedImage 반환 메서드 (Path2D로 직접 구현)
     */
    public static BufferedImage applyTopRoundCorners(BufferedImage image, int arcWidth, int arcHeight) {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage roundedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();

        // 고품질 렌더링 옵션
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 투명 배경
        g2.setComposite(AlphaComposite.Src);
        g2.setColor(new Color(0, 0, 0, 0)); // 완전 투명
        g2.fillRect(0, 0, w, h);

        // 상단 라운드 경로 생성
        Path2D.Float path = new Path2D.Float();
        path.moveTo(0, arcHeight);
        path.quadTo(0, 0, arcWidth, 0); // 왼쪽 상단 라운드
        path.lineTo(w - arcWidth, 0);
        path.quadTo(w, 0, w, arcHeight); // 오른쪽 상단 라운드
        path.lineTo(w, h); // 우측 하단
        path.lineTo(0, h); // 좌측 하단
        path.closePath();

        // 클리핑 적용
        g2.setClip(path);

        // 이미지 그리기
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return roundedImage;
    }
    
    /**
     * 이미지 4개 귀퉁이를 모두 라운드 처리하는 메서드
     */
    public static BufferedImage applyFullRoundCorners(BufferedImage image, int arcWidth, int arcHeight) {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage roundedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();

        // 고품질 렌더링 옵션
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 투명 배경
        g2.setComposite(AlphaComposite.Src);
        g2.setColor(new Color(0, 0, 0, 0)); // 완전 투명
        g2.fillRect(0, 0, w, h);

        // 라운드된 사각형 clip 설정
        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, arcWidth, arcHeight));
        
        // 이미지 그리기
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return roundedImage;
    }
    
    /**
     * BufferedImage를 지정한 크기로 리사이징하는 메서드
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
    	try {
            return Thumbnails.of(originalImage)
                    .size(targetWidth, targetHeight) // 크기 지정
                    .outputQuality(1.0) // 100% 품질
                    .keepAspectRatio(false) // ⭐ 비율 유지 OFF (딱 지정한 크기로)
                    .asBufferedImage(); // BufferedImage로 반환
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    	/*
    	BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // 🔥 고품질 리사이징 옵션 적용
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resizedImage;
        */
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