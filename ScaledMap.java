package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import provinceGenerator.ProvinceLoader;

public class ScaledMap extends ProvinceLoader{
	int MAPCONTAINERWIDTH;
	int MAPCONTAINERHEIGHT;
	int mapOriginalWidth;
	int mapOriginalHeight;
	double widthScale;
	double heightScale;
	Dimension miniMapSize;
	public ScaledMap(String map, String directory, int playerCount, Dimension viewSize) { 
		super(map, directory, playerCount, viewSize);
	}
	public void loadProvinces() {
		mapWidth = provinceMap.getWidth();
		mapHeight = provinceMap.getHeight();
		mapOriginalWidth = mapWidth;
		mapOriginalHeight = mapHeight;
		miniMapSize = MathFunctions.getScaledDimension(new Dimension(mapWidth, mapHeight), new Dimension(MAPCONTAINERWIDTH, MAPCONTAINERHEIGHT));
		widthScale = (double) mapWidth / miniMapSize.width;
		heightScale = (double) mapHeight / miniMapSize.height;
		provinceMap = scaleImage(provinceMap);
		super.loadProvinces();
	}
	public void createProvinceMap() {
		mapPanel = new JLayeredPane();
		mapPanel.setBounds(0, 0, mapWidth, mapHeight);
		mapPanel.setMinimumSize(new Dimension(mapWidth, mapHeight));
		mapPanel.setPreferredSize(new Dimension(mapWidth, mapHeight));
		mapPanel.setMaximumSize(new Dimension(mapWidth, mapHeight));
		mapPanel.setOpaque(false);
		mapContainerPanel = new JPanel(new GridBagLayout());
		mapContainerPanel.setBackground(new Color(120, 110, 255));
		Dimension mapContainerSize = new Dimension(MAPCONTAINERWIDTH, MAPCONTAINERHEIGHT);
		mapContainerPanel.setMinimumSize(mapContainerSize);
		mapContainerPanel.setPreferredSize(mapContainerSize);
		mapContainerPanel.setMaximumSize(mapContainerSize);
		clickPanel = new JPanel();
		clickPanel.setOpaque(false);
		clickPanel.setBounds(0, 0, mapWidth, mapHeight);
		clickPanel.addMouseListener(this);
		clickPanel.addMouseMotionListener(this);
		for (int i=0; i<provinceColors.size(); i++) {
			provincePanels[i].provinceColor = Color.GRAY;
			provinceBorders[i].provinceColor = Color.BLACK;
			borderColors[i] = Color.BLACK;
			provincePanels[i].mapWidth = mapWidth;
			provinceBorders[i].mapWidth = mapWidth;
			provincePanels[i].mapHeight = mapHeight;
			provinceBorders[i].mapHeight = mapHeight;
			provincePanels[i].drawRectangle();
			provinceBorders[i].drawRectangle();
			provincePanels[i].setBounds(provincePanels[i].getMapX(), provincePanels[i].getMapY(), provincePanels[i].provinceImage.getWidth(), provincePanels[i].provinceImage.getHeight() + 5);
			provinceBorders[i].setBounds(provinceBorders[i].getMapX(), provinceBorders[i].getMapY(), provinceBorders[i].provinceImage.getWidth(), provinceBorders[i].provinceImage.getHeight() + 5);
			mapPanel.add(provincePanels[i], Integer.valueOf(i));
			mapPanel.add(provinceBorders[i], Integer.valueOf(i + provinceColors.size()));
		}
		mapPanel.add(clickPanel,  Integer.valueOf((provinceColors.size() * 2)));
		mapContainerPanel.add(mapPanel);
		mapPositionX = 0;
		mapPositionY = 0;
	}
	BufferedImage scaleImage(BufferedImage imageToScale) {
		int width = (int) (imageToScale.getWidth() / widthScale);
		int height = (int) (imageToScale.getHeight() / heightScale);
		Image scaledImage = imageToScale.getScaledInstance(width, height, Image.SCALE_REPLICATE);
		imageToScale = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		imageToScale.getGraphics().drawImage(scaledImage, 0, 0, null);
		imageToScale.getGraphics().dispose();
		return imageToScale;
	}
}
