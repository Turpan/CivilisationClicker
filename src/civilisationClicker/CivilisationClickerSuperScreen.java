package civilisationClicker;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public class CivilisationClickerSuperScreen {
	static final int maxStages = 4;
	static final int buildingPointPool = 1;
	static final int researchPointPool = 2;
	static final int militaryPointPool = 3;
	static final int governmentPointPool = 4;
	static Image[] borderPanelImage;
	static Image[] buildingUIBar;
	static Image[] buildingButtonImage;
	static Image[][] leftBarStageImage;
	static int selectedBuySellButton, provinceCount, screenCount;
	int happinessAmnestyCounter;
	static ImageIcon[] clickImage;
	static ImageIcon[] clickImagePressed;
	static Clip clickSound1[];
	static Clip clickSound2[];
	static Clip clickSound3[];
	CivilisationClickerScreen[][] provinceScreens;
	CivilisationClickerSuperScreen() {
		
	}
	static void loadData() {
		screenCount = CivilisationClickerDataBase.screenTypes.size();
		provinceCount = CivilisationClickerDataBase.mapList.get(CivilisationClickerDataBase.chosenMap).provinceList.size();
		loadSound();
		loadImages();
	}
	static void loadSound() {
		initialiseAudioVariables();
		int i = 0;
		for (String screenType : CivilisationClickerDataBase.screenTypes) {
			File audioFile1 = new File("sound/" + screenType + "-click1.wav");
			File audioFile2 = new File("sound/" + screenType + "-click2.wav");
			File audioFile3 = new File("sound/" + screenType + "-click3.wav");
			try {
				AudioInputStream stream1 = AudioSystem.getAudioInputStream(audioFile1);
				AudioInputStream stream2 = AudioSystem.getAudioInputStream(audioFile2);
				AudioInputStream stream3 = AudioSystem.getAudioInputStream(audioFile3);
				clickSound1[i] = AudioSystem.getClip();
				clickSound2[i] = AudioSystem.getClip();
				clickSound3[i] = AudioSystem.getClip();
				clickSound1[i].open(stream1);
				clickSound2[i].open(stream2);
				clickSound3[i].open(stream3);
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}
	static void initialiseAudioVariables() {
		clickSound1 = new Clip[screenCount];
		clickSound2 = new Clip[screenCount];
		clickSound3 = new Clip[screenCount];
	}
	static void loadImages() {
		leftBarStageImage = new Image[screenCount][maxStages];
		borderPanelImage = new Image[screenCount];
		clickImage = new ImageIcon[screenCount];
		clickImagePressed = new ImageIcon[screenCount];
		buildingUIBar = new Image[screenCount];
		buildingButtonImage = new Image[screenCount];
		int i = 0;
		for (String screenType : CivilisationClickerDataBase.screenTypes) {
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
		int b = CivilisationClickerMapScreen.selectedProvince;
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
			provinceScreens[i][province] = new CivilisationClickerScreen(i+1, province);
		}
	}
	void createClickerScreens() {
		CivilisationClickerScreen.middlePanelWidth = CivilisationMainClass.gameWidth - (300 + 467 + 14 + 14);
		provinceScreens = new CivilisationClickerScreen[screenCount][CivilisationClickerMapScreen.provinceList.size()];
		for (int i=0; i<screenCount; i++) {
			int j = 0;
			for (CivilisationClickerProvince province : CivilisationClickerMapScreen.provinceList) {
				if (province.owner == CivilisationMainClass.playerID) provinceScreens[i][j] = new CivilisationClickerScreen(i + 1, j);
				j++;
			}
		}
	}
	void timerTick() {
		for (int i=0; i<screenCount; i++) {
			for (int j=0; j<CivilisationClickerMapScreen.provinceList.size(); j++) {
				CivilisationClickerProvince province = CivilisationClickerMapScreen.getProvince(j);
				if (province.owner == CivilisationMainClass.playerID && !province.coloniseInProgress) {
					provinceScreens[i][j].timerTick();
				}
			}
		}
		CivilisationMainClass.resourceBar.updateLabels();
	}
}
