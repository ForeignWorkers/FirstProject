package Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CustomButton extends JLabel {
    private MouseAdapter mouseAdapter;
    private ImageIcon imageIcon;

    public CustomButton(String text) {
        setText(text);
    }

    public CustomButton(ImageIcon icon) {
        setIcon(icon);
        this.imageIcon = icon;
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        if(mouseAdapter != null) {

        }
    }

    //외부에서 끄고 / 켜줄때
    public void setClickEffect(boolean clickEffect, float... hoveredValue) {
        if (clickEffect) {
            setButtonClickEffect(hoveredValue);
        }
        else {
            if(mouseAdapter != null) {
                removeMouseListener(mouseAdapter);
            }
        }
    }

    private void setButtonClickEffect(float... hoveredVaule){
        if (mouseAdapter == null) {
            float value = (hoveredVaule.length > 0) ? hoveredVaule[0] : 0.9f; // 기본값 1.0f

            mouseAdapter = new MouseAdapter()
            {
                BufferedImage originalImage = toBufferedImage(imageIcon.getImage());

                // 어두운 이미지 생성 (아이콘 픽셀만 조절)
                BufferedImage darkenedImage = darkenIconPixels(originalImage, 0.7f);
                ImageIcon darkenedIcon = new ImageIcon(darkenedImage);

                @Override
                public void mouseEntered(MouseEvent e) {
                    setIcon(darkenedIcon); // 클릭하면 어둡게
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setIcon(imageIcon); // 마우스가 나가면 원래대로
                    setCursor(Cursor.getDefaultCursor()); // 커서 기본값으로 복구
                }
            };

            addMouseListener(mouseAdapter);
        }
        else {
            System.out.println("이미 마우스 아답타가 들어가 있습니다. 기존 아답터를 지우고 시도 해주세요.");
        }
    }
    
    // 🔹 BufferedImage로 변환하는 메서드
    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimage.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return bimage;
    }

    // 🔹 아이콘 픽셀만 어둡게 조절하는 메서드 (배경 영향 없음)
    private static BufferedImage darkenIconPixels(BufferedImage image, float brightnessFactor) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgba = image.getRGB(x, y);
                Color color = new Color(rgba, true);

                // 배경은 변경하지 않음 (투명 픽셀 제외)
                if (color.getAlpha() > 0) {
                    int r = (int) (color.getRed() * brightnessFactor);
                    int g = (int) (color.getGreen() * brightnessFactor);
                    int b = (int) (color.getBlue() * brightnessFactor);

                    Color newColor = new Color(r, g, b, color.getAlpha());
                    newImage.setRGB(x, y, newColor.getRGB());
                } else {
                    newImage.setRGB(x, y, rgba); // 배경은 그대로 유지
                }
            }
        }
        return newImage;
    }
}
