package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import paintedPanel.PaintedPanel;
import provinceGenerator.ProvinceListener;
import provinceGenerator.ProvinceLoader;

public class CivilisationClickerMapScreen implements MiniMapListener, ProvinceListener, QuickButtonListener{
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
	CivilisationClickerMiniMap miniMap;
	CivilisationClickerQuickButton quickButton;
	CivilisationClickerProvinceInfo provinceInfo;
	CivilisationClickerQuickBuy quickBuy;
	ProvinceLoader gameMap;
	static List<CivilisationClickerProvince> provinceList;
	CivilisationClickerMapScreen() {
		List<CivilisationClickerCountry> playerList = CivilisationMainClass.playerList;
		CivilisationClickerMap map = CivilisationClickerDataBase.mapList.get(CivilisationClickerDataBase.chosenMap);
		provinceList = map.provinceList;
		for (CivilisationClickerProvince province : provinceList) {
			province.clearProvince();
		}
		Dimension displaySize = new Dimension(CivilisationMainClass.gameWidth, CivilisationMainClass.gameHeight);
		gameMap = new ProvinceLoader(map.map, "data/province maps/", playerList.size(), displaySize);
		gameMap.setColorList(map.provinceColors);
		gameMap.loadProvinces();
		for (int i=0; i<playerList.size(); i++) {
			CivilisationClickerCountry country = playerList.get(i);
			gameMap.ownerColours[i] = country.color;
			if (country.startingProvince != -1) {
				gameMap.provinceOwner[country.startingProvince] = i+1;
				provinceList.get(country.startingProvince).setOwner(i+1);
				for (CivilisationClickerProvinceDevelopement developement : provinceList.get(country.startingProvince).developementList) {
					developement.checkBuildingStage();
				}
			}
		}
		gameMap.colourProvinces();
		gameMap.addProvinceListener(this);
		miniMap = new CivilisationClickerMiniMap(displaySize);
		miniMap.setColorList(map.provinceColors);
		miniMap.loadProvinces();
		for (int i=0; i<playerList.size(); i++) {
			CivilisationClickerCountry country = playerList.get(i);
			miniMap.ownerColours[i] = country.color;
			if (country.startingProvince != -1) {
				miniMap.provinceOwner[country.startingProvince] = i+1;
			}
		}
		miniMap.colourProvinces();
		miniMap.mapContainerPanel.setBounds(0, 7, miniMap.MAPCONTAINERWIDTH, miniMap.MAPCONTAINERHEIGHT);
		miniMap.addMiniMapListener(this);
		int x = gameMap.mainPanel.getViewport().getViewPosition().x;
		int y = gameMap.mainPanel.getViewport().getViewPosition().y;
		miniMap.updateViewWindow(x, y);
		getDevelopementImages();
		createUI();
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
		quickButton = new CivilisationClickerQuickButton();
		quickButton.selectedProvince = CivilisationMainClass.getPlayer().startingProvince;
		quickButton.addListener(this);
		provinceInfo = new CivilisationClickerProvinceInfo();
		quickBuy = new CivilisationClickerQuickBuy();
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
	}
	void createDevelopementImagePanels() {
		developementImagePanel = new PaintedPanel[CivilisationClickerDataBase.screenTypes.size()];
		buildingCountLabel = new JLabel[CivilisationClickerDataBase.screenTypes.size()];
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
		developementImage = new ImageIcon[CivilisationClickerDataBase.screenTypes.size()][CivilisationClickerSuperScreen.maxStages];
		Dimension imageSize = new Dimension(developementImageWidth, developementImageHeight);
		for (int i=0; i<CivilisationClickerDataBase.screenTypes.size(); i++) {
			for (int j=0; j<CivilisationClickerSuperScreen.maxStages; j++) {
				developementImage[i][j] = scaleImage(CivilisationClickerSuperScreen.leftBarStageImage[i][j], imageSize);
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
					CivilisationClickerProvinceDevelopement developement = provinceList.get(province).developementList.get(i);
					for (int j=0; j<developement.buildingCount.length; j++) buildingcount += developement.buildingCount[j];
					buildingCountLabel[i].setText(buildingcount + "");
				}
			} else {
				for (int i=0; i<developementImagePanel.length; i++) {
					int buildingcount = 0;
					CivilisationClickerProvinceDevelopement developement = provinceList.get(province).developementList.get(i);
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
		gameMap.borderColors[province] = color;
		miniMap.borderColors[province] = color;
		gameMap.colourBorders();
		miniMap.colourBorders();
	}
	void changeProvinceOwner(int province, int owner) {
		if(owner > 0) {
			CivilisationClickerCountry country = CivilisationMainClass.playerList.get(owner-1);
			if(owner == CivilisationMainClass.playerID || (country.isAI && CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST))
				country.findAccessableProvinces();
		}
		provinceList.get(province).owner = owner;
		provinceList.get(province).clearProvince();
		gameMap.provinceOwner[province] = owner;
		miniMap.provinceOwner[province] = owner;
		gameMap.colourProvinces();
		miniMap.colourProvinces();
	}
	void changeProvinceOwner(int province, int owner, boolean conquered) {
		if(owner > 0) {
			CivilisationClickerCountry country = CivilisationMainClass.playerList.get(owner-1);
			if(owner == CivilisationMainClass.playerID || (country.isAI && CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST))
				country.findAccessableProvinces();
		}
		if (provinceList.get(province).owner == CivilisationMainClass.playerID && quickButton.selectedProvince == province) changeSelectedOwnProvince(); 
		provinceList.get(province).owner = owner;
		provinceList.get(province).clearProvince(conquered);
		gameMap.provinceOwner[province] = owner;
		miniMap.provinceOwner[province] = owner;
		gameMap.colourProvinces();
		miniMap.colourProvinces();
	}
	void changeSelectedOwnProvince(int province) {
		quickButton.selectedProvince = province;
	}
	void changeSelectedOwnProvince() {
		for (CivilisationClickerProvince province : provinceList) if (province.owner == CivilisationMainClass.playerID) changeSelectedOwnProvince(province.ID);
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
		CivilisationMainClass.soundEngine.playProvinceClickSound();
		selectedProvince = provinceChanged;
		updateLabels();
		if (selectedProvince != -1) {
			CivilisationClickerProvince province = provinceList.get(selectedProvince);
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
		for (CivilisationClickerProvince province : provinceList) 
			if (province.owner != 0) 
				if (province.owner == CivilisationMainClass.playerID ||
				(CivilisationMainClass.playerList.get(province.owner - 1).isAI && CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT))
					province.timerTick();
		updateLabels();
	}
	static public CivilisationClickerProvince getSelectedProvince() {
		if (selectedProvince != -1) return provinceList.get(selectedProvince);
		else return null;
	}
	static public CivilisationClickerProvince getProvince(int province) {
		return provinceList.get(province);
	}
	@Override
	public void screenChanged(int screen) {
		quickBuy.changeScreenType(screen);
	}
}
