package provinceGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProvincePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BufferedImage provinceImage;
	Set<Dimension> pixelList = new HashSet<Dimension>();
	private Color provinceColor;
	private BufferedImage backgroundImage;
	public int mapWidth;
	public int mapHeight;
	int X = Integer.MAX_VALUE;
	int Y = Integer.MAX_VALUE;
	int width = 0;
	int height = 0;
	public ImageIcon provinceLabelImage;
	public JLabel provinceLabel;
	public void drawRectangle() {
		removeAll();
		provinceImage = new BufferedImage((width - X) + 1, (height - Y) + 1, BufferedImage.TYPE_INT_ARGB);
		if (backgroundImage == null) {
			drawColor();
		} else {
			drawImage();
		}
		provinceLabelImage = new ImageIcon(provinceImage);
		setOpaque(false);
		provinceLabel = new JLabel(provinceLabelImage);
		add(provinceLabel);
	}
	void drawImage() {
		for (Dimension pixel : pixelList) {
			int x = pixel.width;
			int y = pixel.height;
			int imagex = x;
			int imagey = y;
			if (imagex >= backgroundImage.getWidth()) {
				while (imagex >= backgroundImage.getWidth()) {
					imagex -= backgroundImage.getWidth();
				}
			}
			if (imagey >= backgroundImage.getHeight()) {
				while (imagey >= backgroundImage.getHeight()) {
					imagey -= backgroundImage.getHeight();
				}
			}
			int rgb = backgroundImage.getRGB(imagex, imagey);
			provinceImage.setRGB(x - X, y - Y, rgb);
		}
	}
	void drawColor() {
		for (Dimension pixel : pixelList) {
			int x = pixel.width;
			int y = pixel.height;
			provinceImage.setRGB(x - X, y - Y, provinceColor.getRGB());
		}
	}
	void addPixelToList(int x, int y) {
		pixelList.add(new Dimension(x, y));
		if (x < X) {
			X = x;
		}
		if (y < Y) {
			Y = y;
		}
		if (x > width) {
			width = x;
		}
		if (y > height) {
			height = y;
		}
	}
	public void setColor(Color provinceColor) {
		this.provinceColor = provinceColor;
		backgroundImage = null;
	}
	public Color getColor() {
		return provinceColor;
	}
	void setImage(BufferedImage backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	BufferedImage getImage() {
		return backgroundImage;
	}
	public int getMapX() {
		return X;
	}
	public int getMapY() {
		return Y;
	}
}
