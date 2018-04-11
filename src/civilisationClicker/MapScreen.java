package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import paintedPanel.PaintedPanel;
import provinceGenerator.ProvinceListener;
import provinceGenerator.ProvinceLoader;

public class MapScreen implements MiniMapListener, ProvinceListener, QuickButtonListener, KeyListener{
	static final int uiwidth = 1217;
	static final int uiheight = 205;
	static final int developementImageWidth = 64;
	static final int developementImageHeight = 124;
	static int selectedProvince = -1;
	ImageIcon[] unownedDevelopementImage;
	ImageIcon[][] developementImage;
	JLayeredPane mainPanel, developementLayeredPanel;
	JPanel uiMainPanel;
	JLabel[] buildingCountLabel;
	PaintedPanel miniMapPanel, developementPanel;
	PaintedPanel[] developementImagePanel;
	MiniMap miniMap;
	QuickButton quickButton;
	ProvinceInfo provinceInfo;
	QuickBuy quickBuy;
	ChatBox chatBox;
	ChatBar chatBar;
	ProvinceLoader gameMap;
	static List<Province> provinceList;
	static Set<Dimension> adjacencyList;
	MapScreen() {
		CivilisationMainClass.frame.addKeyListener(this);
		List<Country> playerList = CivilisationMainClass.playerList;
		Map map = DataBase.getChosenMap();
		provinceList = map.provinceList;
		for (Province province : provinceList) {
			province.clearProvince();
		}
		Dimension displaySize = new Dimension(CivilisationMainClass.gameWidth, CivilisationMainClass.gameHeight);
		gameMap = new ProvinceLoader(map.map, "data/province maps/", playerList.size(), displaySize);
		gameMap.setColorList(map.provinceColors);
		gameMap.loadProvinces();
		for (int i=0; i<playerList.size(); i++) {
			Country country = playerList.get(i);
			if (country.startingProvince != -1) {
				gameMap.colourProvince(country.startingProvince, country.color);
				provinceList.get(country.startingProvince).setOwner(i+1);
				for (ProvinceDevelopement developement : provinceList.get(country.startingProvince).developementList) {
					developement.checkBuildingStage();
				}
			}
		}
		gameMap.addProvinceListener(this);
		miniMap = new MiniMap(displaySize);
		miniMap.setColorList(map.provinceColors);
		miniMap.loadProvinces();
		for (int i=0; i<playerList.size(); i++) {
			Country country = playerList.get(i);
			if (country.startingProvince != -1) {
				miniMap.colourProvince(country.startingProvince, country.color);
			}
		}
		miniMap.mapContainerPanel.setBounds(0, 7, miniMap.MAPCONTAINERWIDTH, miniMap.MAPCONTAINERHEIGHT);
		miniMap.addMiniMapListener(this);
		int x = gameMap.mainPanel.getViewport().getViewPosition().x;
		int y = gameMap.mainPanel.getViewport().getViewPosition().y;
		miniMap.updateViewWindow(x, y);
		adjacencyList = new HashSet<Dimension>(gameMap.adjacencyList);
		for (Dimension adjacency : map.adjacencyList) {
			adjacencyList.add(adjacency);
		}
		checkMapCache(map);
		getDevelopementImages();
		createUI();
	}
	void checkMapCache(Map map) {
		File mapCacheFile = new File("data/province maps/" + map.map + "-cache.xml");
		if (!mapCacheFile.exists()) {
			createMapGraphics(map);
		} else {
			MapCache mapCache = XMLLoader.loadMapCache(mapCacheFile);
			File mapImageFile = new File("data/province maps/" + map.map + "-map.png");
			File mapXMLFile = new File("data/maplist.xml");
			String mapImageHash = "";
			String mapXMLHash = "";
			try {
				mapImageHash = MathFunctions.getMD5CheckSum(mapImageFile);
				mapXMLHash = MathFunctions.getMD5CheckSum(mapXMLFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mapCache.mapImageHash.equals(mapImageHash) && mapCache.mapXMLHash.equals(mapXMLHash)) {
				loadMapGraphics(mapCache);
			} else {
				createMapGraphics(map);
			}
		}
	}
	void loadMapGraphics(MapCache mapCache) {
		for (Rectangle points : mapCache.connectionLines) {
			gameMap.createConnection(points);
		}
	}
	void createMapGraphics(Map map) {
		MapCache mapCache = new MapCache();
		File mapImageFile = new File("data/province maps/" + map.map + "-map.png");
		File mapXMLFile = new File("data/maplist.xml");
		File mapCacheFile = new File("data/province maps/" + map.map + "-cache.xml");
		try {
			mapCache.setMapImageHash(MathFunctions.getMD5CheckSum(mapImageFile));
			mapCache.setMapXMLHash(MathFunctions.getMD5CheckSum(mapXMLFile));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Dimension adjacency : map.adjacencyList) {
			Rectangle connectionLine = gameMap.addConnection(adjacency);
			if (connectionLine != null) mapCache.addConnectionLine(connectionLine);
		}
		XMLLoader.saveMapCache(mapCache, mapCacheFile);
	}
	void createUI() {
		mainPanel = new JLayeredPane();
		Dimension mainPanelSize = new Dimension(CivilisationMainClass.gameWidth, CivilisationMainClass.gameHeight);
		mainPanel.setMinimumSize(mainPanelSize);
		mainPanel.setPreferredSize(mainPanelSize);
		mainPanel.setMaximumSize(mainPanelSize);
		uiMainPanel = new JPanel();
		uiMainPanel.setLayout(new BoxLayout(uiMainPanel, BoxLayout.LINE_AXIS));
		uiMainPanel.setBounds(0, CivilisationMainClass.gameHeight - uiheight, uiwidth, uiheight);
		uiMainPanel.setOpaque(false);
		miniMapPanel = new PaintedPanel();
		miniMapPanel.setLayout(null);
		miniMapPanel.bgImage = new ImageIcon("graphics/ui/mapui-bottom/mapborder.png");
		Dimension miniMapPanelSize = new Dimension(miniMapPanel.bgImage.getIconWidth(), miniMapPanel.bgImage.getIconHeight());
		miniMapPanel.setMinimumSize(miniMapPanelSize);
		miniMapPanel.setPreferredSize(miniMapPanelSize);
		miniMapPanel.setMaximumSize(miniMapPanelSize);
		developementLayeredPanel = new JLayeredPane();
		developementLayeredPanel.setOpaque(false);
		developementPanel = new PaintedPanel();
		developementPanel.bgImage = new ImageIcon("graphics/ui/mapui-bottom/developement.png");
		developementPanel.setOpaque(false);
		Dimension developementPanelSize = new Dimension(developementPanel.bgImage.getIconWidth(), developementPanel.bgImage.getIconHeight());
		developementLayeredPanel.setMinimumSize(developementPanelSize);
		developementLayeredPanel.setPreferredSize(developementPanelSize);
		developementLayeredPanel.setMaximumSize(developementPanelSize);
		developementPanel.setBounds(0, 0, developementPanelSize.width, developementPanelSize.height);
		createDevelopementImagePanels();
		quickButton = new QuickButton();
		quickButton.selectedProvince = CivilisationMainClass.getPlayer().startingProvince;
		quickButton.addListener(this);
		provinceInfo = new ProvinceInfo();
		quickBuy = new QuickBuy();
		chatBox = new ChatBox();
		ChatBar.bounds = new Rectangle(miniMapPanel.bgImage.getIconWidth(), CivilisationMainClass.gameHeight - uiheight,
				uiwidth - miniMapPanel.bgImage.getIconWidth(), MathFunctions.getCharHeight(MathFunctions.systemFont));
		chatBar = new ChatBar();
		miniMapPanel.add(miniMap.mapContainerPanel);
		developementLayeredPanel.add(developementPanel, Integer.valueOf(2));
		uiMainPanel.add(miniMapPanel);
		uiMainPanel.add(developementLayeredPanel);
		uiMainPanel.add(provinceInfo.mainPanel);
		uiMainPanel.add(quickBuy.mainPanel);
		uiMainPanel.add(Box.createHorizontalGlue());
		mainPanel.add(gameMap.mainPanel, Integer.valueOf(1));
		mainPanel.add(uiMainPanel, Integer.valueOf(2));
		mainPanel.add(quickButton.mainPanel, Integer.valueOf(2));
		mainPanel.add(chatBox.mainPanel, Integer.valueOf(2));
		mainPanel.add(chatBar.mainPanel, Integer.valueOf(2));
	}
	void createDevelopementImagePanels() {
		developementImagePanel = new PaintedPanel[DataBase.screenTypes.size()];
		buildingCountLabel = new JLabel[DataBase.screenTypes.size()];
		Dimension imageSize = new Dimension(developementImageWidth, developementImageHeight);
		int x = 16;
		int y = 74;
		for (int i=0; i<developementImagePanel.length; i++) {
			developementImagePanel[i] = new PaintedPanel();
			developementImagePanel[i].bgImage = developementImage[i][0];
			developementImagePanel[i].setLayout(new GridBagLayout());
			developementImagePanel[i].setBounds(x, y, imageSize.width, imageSize.height);
			developementImagePanel[i].setVisible(false);
			developementLayeredPanel.add(developementImagePanel[i], Integer.valueOf(1));
			buildingCountLabel[i] = new JLabel("0");
			buildingCountLabel[i].setForeground(Color.BLACK);
			developementImagePanel[i].add(buildingCountLabel[i]);
			x += 67;
		}
	}
	void getDevelopementImages() {
		developementImage = new ImageIcon[DataBase.screenTypes.size()][SuperScreen.maxStages];
		Dimension imageSize = new Dimension(developementImageWidth, developementImageHeight);
		for (int i=0; i<DataBase.screenTypes.size(); i++) {
			for (int j=0; j<SuperScreen.maxStages; j++) {
				developementImage[i][j] = scaleImage(SuperScreen.leftBarStageImage[i][j], imageSize);
			}
		}
	}
	ImageIcon scaleImage(Image imageToScale, Dimension imageSize) {
		imageToScale = imageToScale.getScaledInstance(imageSize.width, imageSize.height, Image.SCALE_REPLICATE);
		ImageIcon scaledImage = new ImageIcon(imageToScale);
		return scaledImage;
	}
	void updateDevelopementImages(int province) {
		if (province != -1) {
			if (provinceList.get(province).owner != 0) {
				for (int i=0; i<developementImagePanel.length; i++) {
					developementImagePanel[i].setVisible(true);
					developementImagePanel[i].bgImage = developementImage[i][provinceList.get(province).developementList.get(i).currentStage - 1];
					int buildingcount = 0;
					ProvinceDevelopement developement = provinceList.get(province).developementList.get(i);
					for (int j=0; j<developement.buildingCount.length; j++) buildingcount += developement.buildingCount[j];
					buildingCountLabel[i].setText(buildingcount + "");
				}
			} else {
				for (int i=0; i<developementImagePanel.length; i++) {
					int buildingcount = 0;
					ProvinceDevelopement developement = provinceList.get(province).developementList.get(i);
					for (int j=0; j<developement.buildingCount.length; j++) buildingcount += developement.buildingCount[j];
					buildingCountLabel[i].setText(buildingcount + "");
				}
			}
		} else {
			for (int i=0; i<developementImagePanel.length; i++) {
				developementImagePanel[i].setVisible(false);
			}
		}
	}
	void colorBorder(int province, Color color) {
		//gameMap.borderColors[province] = color;
		//miniMap.borderColors[province] = color; //come back to this later
		gameMap.colourBorders();
		miniMap.colourBorders();
	}
	void changeProvinceOwner(int province, int owner) {
		if(owner > 0) {
			Country country = CivilisationMainClass.playerList.get(owner-1);
			if(owner == CivilisationMainClass.playerID || (country.isAI && CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST))
				country.findAccessableProvinces();
			gameMap.colourProvince(province, country.color);
			miniMap.colourProvince(province, country.color);
		} else {
			gameMap.colourProvince(province, Color.GRAY);
			miniMap.colourProvince(province, Color.GRAY);
		}
		provinceList.get(province).owner = owner;
		provinceList.get(province).clearProvince();
	}
	void changeProvinceOwner(int province, int owner, boolean conquered) {
		if(owner > 0) {
			Country country = CivilisationMainClass.playerList.get(owner-1);
			if(owner == CivilisationMainClass.playerID || (country.isAI && CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST))
				country.findAccessableProvinces();
			gameMap.colourProvince(province, country.color);
			miniMap.colourProvince(province, country.color);
		} else {
			gameMap.colourProvince(province, Color.GRAY);
			miniMap.colourProvince(province, Color.GRAY);
		}
		if (provinceList.get(province).owner == CivilisationMainClass.playerID && quickButton.selectedProvince == province) changeSelectedOwnProvince(); 
		provinceList.get(province).owner = owner;
		provinceList.get(province).clearProvince(conquered);
	}
	void changeSelectedOwnProvince(int province) {
		quickButton.selectedProvince = province;
	}
	void changeSelectedOwnProvince() {
		for (Province province : provinceList) if (province.owner == CivilisationMainClass.playerID) changeSelectedOwnProvince(province.ID);
	}
	void updateLabels() {
		updateDevelopementImages(selectedProvince);
		provinceInfo.updateLabels();
		quickBuy.updateLabels();
	}
	@Override
	public void miniMapClicked(int x, int y) {
		int newx = (int) (miniMap.widthScale * x);
		int newy = (int) (miniMap.heightScale * y);
		newx -= (CivilisationMainClass.gameWidth / 2);
		newy -= (CivilisationMainClass.gameHeight / 2);
		if (newx > gameMap.mapWidth - CivilisationMainClass.gameWidth) newx = gameMap.mapWidth - CivilisationMainClass.gameWidth;
		else if (newx < 0) newx = 0;
		if (newy > gameMap.mapHeight - CivilisationMainClass.gameHeight) newy = gameMap.mapHeight - CivilisationMainClass.gameHeight;
		else if (newy < 0) newy = 0;
		gameMap.mainPanel.getViewport().setViewPosition(new Point(newx, newy));
		miniMap.updateViewWindow(newx, newy);
	}
	@Override
	public void provinceChanged(int provinceChanged) {
		SoundEngine.playProvinceClickSound();
		selectedProvince = provinceChanged;
		updateLabels();
		if (selectedProvince != -1) {
			Province province = provinceList.get(selectedProvince);
			if (province.owner == CivilisationMainClass.playerID) changeSelectedOwnProvince(provinceChanged);
		} else {
			
		}
	}
	@Override
	public void screenDragged() {
		int x = gameMap.mainPanel.getViewport().getViewPosition().x;
		int y = gameMap.mainPanel.getViewport().getViewPosition().y;
		miniMap.updateViewWindow(x, y);
	}
	@Override
	public void loadingFinished() {
		// TODO Auto-generated method stub
		
	}
	public void timerTick() {
		for (Province province : provinceList) 
			if (province.owner != 0) 
				if (province.owner == CivilisationMainClass.playerID ||
				(CivilisationMainClass.playerList.get(province.owner - 1).isAI && CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT))
					province.timerTick();
		updateLabels();
	}
	static public Province getSelectedProvince() {
		if (selectedProvince != -1) return provinceList.get(selectedProvince);
		else return null;
	}
	static public Province getProvince(int province) {
		return provinceList.get(province);
	}
	@Override
	public void screenChanged(int screen) {
		quickBuy.changeScreenType(screen);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (chatBar.typing) {
				String message = chatBar.typeBox.getText().replaceAll(System.lineSeparator(), " ");
				if (message.length() > 0) {
					chatBox.addMessage(CivilisationMainClass.getPlayer(), message);
					String output = "message;" + CivilisationMainClass.playerID + ";" + message + ";";
					CivilisationMainClass.networkCommunication(output);
				}
				chatBar.typeBox.setText("");
				chatBar.stopTyping();
				CivilisationMainClass.frame.requestFocus();
			} else {
				chatBar.typeBox.setText("");
				chatBar.startTyping();
			}
		} else if (chatBar.typing && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			chatBar.backspace();
		} else if (chatBar.typing) {
			chatBar.addKey(e.getKeyChar());
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
