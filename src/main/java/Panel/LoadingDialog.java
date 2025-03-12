package Panel;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog {
    public LoadingDialog(JFrame parent) {
        super(parent, "로딩 중...", true);
        setUndecorated(true);
        setSize(200, 100);
        setLocationRelativeTo(parent);

        JLabel label = new JLabel("처리 중...", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }
}