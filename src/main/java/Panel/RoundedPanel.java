package Panel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

//패널에 라운드 줄 수 있도록 함
//호출해서 사용
class RoundedPanel extends JPanel {
	private int arcWidth;
	private int arcHeight;

	public RoundedPanel(int arcWidth, int arcHeight) {
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
		setOpaque(false); // 배경을 투명하게 설정하여 둥근 모서리가 적용되도록 함
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();

		// 안티앨리어싱 적용 (부드럽게)
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 배경색 적용
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

		g2.dispose();
	}
}


