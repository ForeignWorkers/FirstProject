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
	        setOpaque(false);//배경 투명 처리
	 }

	 @Override
	 protected void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 Graphics2D g2 = (Graphics2D) g.create();
	     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	     g2.setColor(getBackground());
	     g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
	     g2.dispose();
	 }
	 
}


