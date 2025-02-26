package Frame;

import Data.AppConstants;

import javax.swing.*;
import java.awt.*;

public class FrameBase extends JFrame {
    private static FrameBase instance;

    public FrameBase(JPanel e) {

        //화면크기을 얻기 위한 툴 킷
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        int frameWidth = AppConstants.FRAME_WIDTH;
        int frameHeight = AppConstants.FRAME_HEIGHT;

        //기본 JfFrame프레임 구조
        setTitle("");
        setLayout(null);
        setBounds(
                (screenSize.width - frameWidth) / 2,
                (screenSize.height - frameHeight) / 2,
                frameWidth,
                frameHeight);

        add(e);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void getInstance(JPanel e) {

        instance = new FrameBase(e);
        instance.getContentPane().removeAll();
        instance.getContentPane().add(e);
        instance.revalidate();
        instance.repaint();
    }
}
