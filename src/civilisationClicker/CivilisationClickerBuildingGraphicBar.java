package civilisationClicker;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import paintedPanel.PaintedPanel;

public class CivilisationClickerBuildingGraphicBar extends PaintedPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String IMAGESTYLEGRID = "grid";
	public static final String IMAGESTYLERANDOM = "random";
	int imageMarginX;
	int imageMarginY;
	int startX;
	int startY;
	int width;
	int height;
	String imageStyle;
	ImageIcon[] buildingIcon;
	List<CivilisationClickerBuildingGraphicBarIcon> iconList = new ArrayList<CivilisationClickerBuildingGraphicBarIcon>();
	CivilisationClickerBuildingGraphicBar (String buildingBar, String[] buildingIcon) {
		this.bgImage = new ImageIcon(buildingBar);
		this.buildingIcon = new ImageIcon[buildingIcon.length];
		for (int i=0; i<buildingIcon.length; i++) {
			this.buildingIcon[i] = new ImageIcon(buildingIcon[i]);
		}
		imageStyle = IMAGESTYLEGRID;
	}
	CivilisationClickerBuildingGraphicBar (String buildingBar, String[] buildingIcon, String imageStyle) {
		this.bgImage = new ImageIcon(buildingBar);
		this.buildingIcon = new ImageIcon[buildingIcon.length];
		for (int i=0; i<buildingIcon.length; i++) {
			this.buildingIcon[i] = new ImageIcon(buildingIcon[i]);
		}
		switch (imageStyle) {
		case IMAGESTYLEGRID:
			this.imageStyle = IMAGESTYLEGRID;
			break;
		case IMAGESTYLERANDOM:
			this.imageStyle = IMAGESTYLERANDOM;
			break;
		default:
			this.imageStyle = IMAGESTYLEGRID;
			break;
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (CivilisationClickerBuildingGraphicBarIcon icon : iconList) {
			g.drawImage(buildingIcon[icon.id].getImage(), icon.x, icon.y, null);
		}
	}
	void addImage(int image) {
		if (image > 0) {
			int currentx = 0;
			int currenty = 0;
			if (!iconList.isEmpty()) {
				CivilisationClickerBuildingGraphicBarIcon icon = iconList.get(iconList.size() - 1);
				currentx = icon.x;
				currenty = icon.y;
			}
			switch (imageStyle) {
			case IMAGESTYLEGRID:
				for (int i=0; i<image; i++) {
					int id = 0;
					if (buildingIcon.length > 1) {
						id = new Random().nextInt(buildingIcon.length - 1);
					}
					if (currentx == 0 && currenty == 0) {
						currentx = imageMarginX + startX;
						currenty = imageMarginY + startY;
					} else if (currenty + imageMarginY + (2 * buildingIcon[id].getIconHeight()) > height) {
						currenty = imageMarginY + startY;
						currentx += (buildingIcon[id].getIconWidth() + imageMarginX);
					} else {
						currenty += (buildingIcon[id].getIconHeight() + imageMarginY);
					}
					iconList.add(new CivilisationClickerBuildingGraphicBarIcon(currentx, currenty, id));
				}
				break;
			case IMAGESTYLERANDOM:
				break;
			}
		} else {
			for (int i=0; i<image; i++) {
				iconList.remove(iconList.size() - 1);
			}
		}
	}
	void setMarginX (int x) {
		imageMarginX = x;
	}
	void setMarginY (int y) {
		imageMarginY = y;
	}
	void setMargin(int x, int y) {
		imageMarginX = x;
		imageMarginY = y;
	}
	void setStartBounds(int x, int y) {
		startX = x;
		startY = y;
	}
	void setVisibleArea(int x, int y) {
		width = x;
		height = y;
	}
	int getNumberOfIcons() {
		return iconList.size();
	}
}
class CivilisationClickerBuildingGraphicBarIcon {
	int x;
	int y;
	int id;
	CivilisationClickerBuildingGraphicBarIcon(int x, int y, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}
}
