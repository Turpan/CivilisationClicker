package civilisationClicker;

import java.awt.Image;

import javax.swing.ImageIcon;

public class SuperScreen {
	static final int maxStages = 4;
	static Image[] borderPanelImage;
	static Image[] buildingUIBar;
	static Image[] buildingButtonImage;
	static Image[][] leftBarStageImage;
	static int selectedBuySellButton, provinceCount, screenCount;
	int happinessAmnestyCounter;
	static ImageIcon[] clickImage;
	static ImageIcon[] clickImagePressed;
	ClickerScreen[][] provinceScreens;
	SuperScreen() {
		
	}
	static void loadData() {
		screenCount = DataBase.screenTypes.size();
		provinceCount = DataBase.mapList.get(DataBase.chosenMap).provinceList.size();
		loadImages();
	}
	static void loadImages() {
		leftBarStageImage = new Image[screenCount][maxStages];
		borderPanelImage = new Image[screenCount];
		clickImage = new ImageIcon[screenCount];
		clickImagePressed = new ImageIcon[screenCount];
		buildingUIBar = new Image[screenCount];
		buildingButtonImage = new Image[screenCount];
		int i = 0;
		for (String screenType : DataBase.screenTypes) {
			for (int j=0; j<maxStages; j++) {
				leftBarStageImage[i][j] = new ImageIcon("graphics/sidebars/" + screenType + "-stage" + Integer.toString(j+1) + ".png").getImage();
			}
			borderPanelImage[i] = new ImageIcon("graphics/ui/" + screenType + ".png").getImage();
			clickImage[i] = new ImageIcon("graphics/buttons/" + screenType + "-button.png");
			clickImagePressed[i] = new ImageIcon("graphics/buttons/" + screenType + "-button-pressed.png");
			buildingUIBar[i] = new ImageIcon("graphics/ui/" + screenType + "-side.png").getImage();
			buildingButtonImage[i] = new ImageIcon("graphics/ui/" + screenType + "-building-square.png").getImage();
			i++;
		}
	}
	void swapTabs(int newTab) {
		int a = newTab;
		int b = MapScreen.selectedProvince;
		if (b == -1) {
			CivilisationMainClass.mainPanel.add(CivilisationMainClass.mapScreen.mainPanel);
			CivilisationMainClass.selectedPanel = 7;
		} else {
			CivilisationMainClass.mainPanel.add(provinceScreens[a - 1][b].mainPanel);
		}
	}
	void swapTabs(int newTab, int province) {
		int a = newTab;
		if (province == -1) {
			CivilisationMainClass.mainPanel.add(CivilisationMainClass.mapScreen.mainPanel);
			CivilisationMainClass.selectedPanel = 7;
		} else {
			CivilisationMainClass.mainPanel.add(provinceScreens[a - 1][province].mainPanel);
		}
	}
	void clearProvince(int province) {
		for (int i=0; i<screenCount; i++) {
			provinceScreens[i][province] = new ClickerScreen(i+1, province);
		}
	}
	void createClickerScreens() {
		ClickerScreen.middlePanelWidth = CivilisationMainClass.gameWidth - (300 + 467 + 14 + 14);
		provinceScreens = new ClickerScreen[screenCount][MapScreen.provinceList.size()];
		for (int i=0; i<screenCount; i++) {
			int j = 0;
			for (Province province : MapScreen.provinceList) {
				if (province.owner == CivilisationMainClass.playerID) provinceScreens[i][j] = new ClickerScreen(i + 1, j);
				j++;
			}
		}
	}
	void timerTick() {
		for (int i=0; i<screenCount; i++) {
			for (int j=0; j<MapScreen.provinceList.size(); j++) {
				Province province = MapScreen.getProvince(j);
				if (province.owner == CivilisationMainClass.playerID && !province.coloniseInProgress) {
					provinceScreens[i][j].timerTick();
				}
			}
		}
		CivilisationMainClass.resourceBar.updateLabels();
	}
}
