package provinceGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ProvinceLoader implements MouseListener, MouseMotionListener{
	File provinceMapFile;
	String mapDirectory;
	int clickX, clickY;
	protected int mapPositionX;
	protected int mapPositionY;
	public int mapWidth;
	public int mapHeight;
	protected int displayWidth;
	protected int displayHeight;
	public int playerCount, provinceOwner[], provinceSelected;
	public Color ownerColours[];
	public ProvincePanel provincePanels[];
	protected ProvincePanel provinceBorders[];
	public Color borderColors[];
	protected BufferedImage provinceMap;
	public Set<Dimension> adjacencyList = new HashSet<Dimension>();
	public JLayeredPane mapPanel;
	public JScrollPane mainPanel;
	private List<ProvinceListener> listeners = new ArrayList<ProvinceListener>();
	public List<Color> provinceColors = new ArrayList<Color>();
	public List<ProvincePanel> connectionPanels = new ArrayList<ProvincePanel>();
	protected JPanel clickPanel;
	public JPanel mapContainerPanel;
	public ProvinceLoader(String mapName, String mapDirectory, int playerCount, Dimension screenSize) {
		this.playerCount = playerCount;
		this.mapDirectory = mapDirectory;
		displayWidth = screenSize.width;
		displayHeight = screenSize.height;
		ownerColours = new Color[playerCount];
		provinceSelected = -1;
		provinceMapFile = new File(mapDirectory + mapName + "-map.png");
		try {
			provinceMap = ImageIO.read(provinceMapFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Program could not find the map image, and must close.");
			System.exit(1);
		}
	}
	public void setColorList(List<Color> provinceColors) {
		this.provinceColors = new ArrayList<Color>(provinceColors);
	}
	public void loadProvinces() {
		mapWidth = provinceMap.getWidth();
		mapHeight = provinceMap.getHeight();
		initialiseData(provinceColors.size());
		for (int i=0; i<provinceColors.size(); i++) {
			provincePanels[i] = new ProvincePanel();
			provinceBorders[i] = new ProvincePanel();
		}
		for (int x=0; x<mapWidth; x++) {
			for (int y=0; y<mapHeight; y++) {
				Color testedColour = new Color(provinceMap.getRGB(x, y));
				for (int i=0; i<provinceColors.size(); i++) {
					if (testedColour.getRGB() == provinceColors.get(i).getRGB()) {
						provincePanels[i].addPixelToList(x, y);
						if (checkBorderPixel(x, y, provinceColors.get(i))) {
							provinceBorders[i].addPixelToList(x, y);
							checkAdjacency(x, y, provinceColors.get(i), i);
						}
						break;
					}
				}
			}
		}
		createProvinceMap();
	}
	protected void createProvinceMap() {
		mapContainerPanel = new JPanel(new GridBagLayout());
		mapContainerPanel.setBackground(new Color(120, 110, 255));
		int containerx = (mapWidth > displayWidth) ? mapWidth : displayWidth;
		int containery = (mapHeight > displayHeight) ? mapHeight : displayHeight;
		Dimension containersize = new Dimension(containerx, containery);
		mapContainerPanel.setMinimumSize(containersize);
		mapContainerPanel.setPreferredSize(containersize);
		mapContainerPanel.setMaximumSize(containersize);
		mainPanel = new JScrollPane(mapContainerPanel);
		mainPanel.setBorder(null);
		mainPanel.setBounds(0, 0, displayWidth, displayHeight);
		mainPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		mainPanel.setPreferredSize(new Dimension(displayWidth, displayHeight));
		mainPanel.setMaximumSize(new Dimension(displayWidth, displayHeight));
		mapPanel = new JLayeredPane();
		mapPanel.setBounds(0, 0, mapWidth, mapHeight);
		mapPanel.setMinimumSize(new Dimension(mapWidth, mapHeight));
		mapPanel.setPreferredSize(new Dimension(mapWidth, mapHeight));
		mapPanel.setMaximumSize(new Dimension(mapWidth, mapHeight));
		mapPanel.setOpaque(false);
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
			provincePanels[i].setBounds(provincePanels[i].X, provincePanels[i].Y, provincePanels[i].provinceImage.getWidth(), provincePanels[i].provinceImage.getHeight() + 5); // this has to be 5 for some reason
			provinceBorders[i].setBounds(provinceBorders[i].X, provinceBorders[i].Y, provinceBorders[i].provinceImage.getWidth(), provinceBorders[i].provinceImage.getHeight() + 5); // this too
			mapPanel.add(provincePanels[i], Integer.valueOf(i));
			mapPanel.add(provinceBorders[i], Integer.valueOf(i + provinceColors.size()));
		}
		mapPanel.add(clickPanel,  Integer.valueOf((provinceColors.size() * 2)));
		mapContainerPanel.add(mapPanel);
		mapPositionX = 0;
		mapPositionY = 0;
		mainPanel.getViewport().setViewPosition(new Point(mapPositionX, mapPositionY));
		for (ProvinceListener pL : listeners) pL.loadingFinished();
	}
	private void initialiseData(int a) {
		provincePanels = new ProvincePanel[a];
		provinceBorders = new ProvincePanel[a];
		provinceOwner = new int[a];
		ownerColours = new Color[playerCount];
		borderColors = new Color[a];
	}
	private boolean checkBorderPixel(int x, int y, Color provinceColor) {
		boolean border = false;
		Color[] testedColors = new Color[4];
		if (x == 0 || y == 0) {
			border = true;
			return border;
		} else if (x == mapWidth || y == mapHeight) {
			border = true;
			return border;
		}
		testedColors[0] = new Color(provinceMap.getRGB(x-1, y));
		testedColors[1] = new Color(provinceMap.getRGB(x+1, y));
		testedColors[2] = new Color(provinceMap.getRGB(x, y-1));
		testedColors[3] = new Color(provinceMap.getRGB(x, y+1));
		if (testedColors[0].getRGB() != provinceColor.getRGB()) {
			border = true;
			return border;
		} else if (testedColors[1].getRGB() != provinceColor.getRGB()) {
			border = true;
			return border;
		} else if (testedColors[2].getRGB() != provinceColor.getRGB()) {
			border = true;
			return border;
		} else if (testedColors[3].getRGB() != provinceColor.getRGB()) {
			border = true;
			return border;
		}
		return border;
	}
	private void checkAdjacency(int x, int y, Color provinceColor, int testedProvince) {
		if (x > 0) {
			Color testedColor = new Color(provinceMap.getRGB(x-1, y));
			if (testedColor.getRGB() != provinceColor.getRGB()) {
				for (int i=0; i<provinceColors.size(); i++) {
					if (provinceColors.get(i).getRGB() == testedColor.getRGB()) {
						adjacencyList.add(new Dimension(testedProvince, i));
					}
				}
			}
		}
		if (x < mapWidth - 1) {
			Color testedColor = new Color(provinceMap.getRGB(x+1, y));
			if (testedColor.getRGB() != provinceColor.getRGB()) {
				for (int i=0; i<provinceColors.size(); i++) {
					if (provinceColors.get(i).getRGB() == testedColor.getRGB()) {
						adjacencyList.add(new Dimension(testedProvince, i));
					}
				}
			}
		}
		if (y > 0) {
			Color testedColor = new Color(provinceMap.getRGB(x, y-1));
			if (testedColor.getRGB() != provinceColor.getRGB()) {
				for (int i=0; i<provinceColors.size(); i++) {
					if (provinceColors.get(i).getRGB() == testedColor.getRGB()) {
						adjacencyList.add(new Dimension(testedProvince, i));
					}
				}
			}
		}
		if (y < mapHeight - 1) {
			Color testedColor = new Color(provinceMap.getRGB(x, y+1));
			if (testedColor.getRGB() != provinceColor.getRGB()) {
				for (int i=0; i<provinceColors.size(); i++) {
					if (provinceColors.get(i).getRGB() == testedColor.getRGB()) {
						adjacencyList.add(new Dimension(testedProvince, i));
					}
				}
			}	
		}
	}
	public void colourProvinces() {
		for (int i=0; i<provinceColors.size(); i++) {
			Color newColor = Color.GRAY;
			if (provinceOwner[i] == 0) {
				newColor = Color.GRAY;
			} else {
				newColor = ownerColours[provinceOwner[i] - 1];
			}
			if (provincePanels[i].provinceColor != newColor) {
				provincePanels[i].provinceColor = newColor;
				provincePanels[i].drawRectangle();
			}
		}
		mapPanel.revalidate();
		mapPanel.repaint();
	}
	public void colourBorders() {
		for (int i=0; i<provinceColors.size(); i++) {
			Color newColor = Color.BLACK;
			if (provinceSelected == i) {
				newColor = Color.YELLOW;
			} else {
				newColor = borderColors[i];
			}
			if (provinceBorders[i].provinceColor != newColor) {
				provinceBorders[i].provinceColor = newColor;
				provinceBorders[i].drawRectangle();
			}
		}
		mapPanel.revalidate();
		mapPanel.repaint();
	}
	public void addProvinceListener(ProvinceListener listener) {
		listeners.add(listener);
	}
	public void addConnection(Dimension connection) {
		int distance = Integer.MAX_VALUE;
		int loopCount = 0;
		Dimension point1 = new Dimension();
		Dimension point2 = new Dimension();
		for (Dimension pixel1 : provinceBorders[connection.width].pixelList) {
			for (Dimension pixel2 : provinceBorders[connection.height].pixelList) {
				int hypot = (int) Math.hypot(pixel1.width - pixel2.width, pixel1.height - pixel2.height);
				if (hypot < distance) {
					distance = hypot;
					point1 = pixel1;
					point2 = pixel2;
				}
				loopCount ++;
				System.out.println(loopCount);
			}
		}
		if (point2.width < point1.width) {
			Dimension spare = point2;
			point2 = point1;
			point1 = spare;
		}
		ProvincePanel connectionLine = new ProvincePanel();
		int width = point2.width - point1.width;
		int height = point2.height - point1.height;
		int absoluteheight = height;
		if (absoluteheight < 0) absoluteheight = 0 - absoluteheight;
		if (width >= absoluteheight) {
			int heightOffset = width / absoluteheight;
			int y = 0;
			for (int x=0; x<width; x++) {
				if (x % heightOffset == 0) y -= (height < 0) ? 1 : -1;
				connectionLine.addPixelToList(point1.width + x, point1.height + y);
				connectionLine.addPixelToList(point1.width + x, point1.height + y + 1);
			}
		} else {
			int widthOffSet = absoluteheight / width;
			int x = 0;
			int y = 0;
			for (int i=0; i<absoluteheight; i++) {
				y -= (height < 0) ? 1 : -1;
				if (y % widthOffSet == 0) x++;
				connectionLine.addPixelToList(point1.width + x, point1.height + y);
				connectionLine.addPixelToList(point1.width + x, point1.height + y + 1);
			}
		}
		connectionLine.provinceColor = Color.YELLOW;
		connectionLine.drawRectangle();
		connectionLine.setBounds(connectionLine.X, connectionLine.Y,
				connectionLine.provinceImage.getWidth(), connectionLine.provinceImage.getHeight() + 5);
		mapPanel.add(connectionLine, Integer.valueOf(2));
		mapPanel.repaint();
		connectionPanels.add(connectionLine);
	}
	private void moveMap(int newX, int newY) {
    	int c = newX - clickX;
    	int d = newY - clickY;
    	mapPositionX -= c;
    	mapPositionY -= d;
    	if (mapPositionX < 0) {
    		mapPositionX = 0;
    	} else if (mapPositionX > (mapWidth - displayWidth)) {
    		mapPositionX = mapWidth - displayWidth;
    	}
    	if (mapPositionY < 0) {
    		mapPositionY = 0;
    	} else if (mapPositionY > (mapHeight - displayHeight)) {
    		mapPositionY = mapHeight - displayHeight;
    	}
    	mainPanel.getViewport().setViewPosition(new Point(mapPositionX, mapPositionY));
    	for (ProvinceListener pL : listeners) pL.screenDragged();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == clickPanel) {
			int x = e.getX();
			int y = e.getY();
			boolean provinceClicked = false;
			Color clickLocation = new Color(provinceMap.getRGB(x, y));
			for (int i=0; i<provinceColors.size(); i++) {
				if (clickLocation.getRGB() == provinceColors.get(i).getRGB()) {
					if (provinceSelected != -1) {
						provinceBorders[provinceSelected].provinceColor = borderColors[provinceSelected];
						provinceBorders[provinceSelected].drawRectangle();
						provinceBorders[provinceSelected].repaint();
					}
					provinceSelected = i;
					provinceBorders[i].provinceColor = Color.YELLOW;
					provinceBorders[i].drawRectangle();
					provinceBorders[i].repaint();
					mapPanel.revalidate();
					for (ProvinceListener pL : listeners) pL.provinceChanged(provinceSelected);
					provinceClicked = true;
					break;
				}
			}
			if (!provinceClicked) {
				if (provinceSelected != -1) {
					provinceBorders[provinceSelected].provinceColor = borderColors[provinceSelected];
					provinceBorders[provinceSelected].drawRectangle();
					provinceBorders[provinceSelected].repaint();
				}
				provinceSelected = -1;
				mapPanel.revalidate();
				for (ProvinceListener pL : listeners) pL.provinceChanged(provinceSelected);
			}
			clickX = x;
			clickY = y;
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getSource() == clickPanel) {
			moveMap(e.getX(), e.getY());
			for (ProvinceListener pL : listeners) pL.screenDragged();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
