package paintedPanel;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PaintedPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PAINTHORIZONTAL = "horizontal";
	public static final String PAINTVERTICAL = "vertical";
	public static final String PAINTNONE = "none";
	int paintBufferX;
	int paintBufferY;
	float alpha = 1;
	public ImageIcon bgImage = new ImageIcon();
	String paintDirection;
	public PaintedPanel(String direction) {
		paintDirection = direction;
	}
	public PaintedPanel() {
		paintDirection = PAINTHORIZONTAL;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2d.drawImage(bgImage.getImage(), paintBufferX, paintBufferY, null);
	    int repeat = 0;
	    switch (paintDirection) {
	    case PAINTHORIZONTAL:
	    	repeat = ((int) this.getSize().getWidth() / bgImage.getIconWidth());
	    	if (repeat > 0) {
	    		for (int i = 0; i<repeat; i++) {
	    			int x = (bgImage.getIconWidth() * (i + 1));
	    			g.drawImage(bgImage.getImage(), x + paintBufferX, paintBufferY, null);
	    		}
	    	}
	    	break;
	    case PAINTVERTICAL:
	    	repeat = ((int) this.getSize().getHeight() / bgImage.getIconHeight());
	    	if (repeat > 0) {
	    		for (int i = 0; i<repeat; i++) {
	    			int y = (bgImage.getIconHeight() * (i + 1));
	    			g.drawImage(bgImage.getImage(), paintBufferX, y + paintBufferY, null);
	    		}
	    	}
	    	break;
	    case PAINTNONE:
	    	break;
	    }
	}
	public void setPaintDirection(String direction) {
		paintDirection = direction;
	}
	public void setPaintBufferX(int x) {
		paintBufferX = x;
	}
	public void setPaintBufferY(int y) {
		paintBufferY = y;
	}
	public void setPaintBuffer(int x, int y) {
		paintBufferX = x;
		paintBufferY = y;
	}
	public void setAlpha(float alpha) {
		if (alpha < 0) alpha = 0;
		else if (alpha > 1) alpha = 1;
		this.alpha = alpha;
		this.repaint();
	}
}
