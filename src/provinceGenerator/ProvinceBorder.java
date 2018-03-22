package provinceGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProvinceBorder extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BufferedImage provinceImage;
	Set<Dimension> pixelList = new HashSet<Dimension>();
	public JLabel provinceLabel;
	public Color provinceColor;
	public int mapWidth;
	public int mapHeight;
	int X = 1800;
	int Y = 900;
	int width = 0;
	int height = 0;
	public ImageIcon provinceLabelImage;
	public void drawRectangle() {
		removeAll();
		provinceImage = new BufferedImage((width - X) + 1, (height - Y) + 1, BufferedImage.TYPE_INT_ARGB);
		int a = pixelList.size();
		Dimension[] pixelsToPaint = new Dimension[a];
		java.util.Iterator<Dimension> iterator = pixelList.iterator();
		int b = 0;
		while (iterator.hasNext()) {
			pixelsToPaint[b] = iterator.next();
			b += 1;
		}
		for (int i=0; i<a; i++) {
			int x = pixelsToPaint[i].width;
			int y = pixelsToPaint[i].height;
			provinceImage.setRGB(x - X, y - Y, provinceColor.getRGB());
		}
		provinceLabelImage = new ImageIcon(provinceImage);
		setOpaque(false);
		provinceLabel = new JLabel(provinceLabelImage);
		add(provinceLabel);
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
	public int getMapX() {
		return X;
	}
	public int getMapY() {
		return Y;
	}
}
