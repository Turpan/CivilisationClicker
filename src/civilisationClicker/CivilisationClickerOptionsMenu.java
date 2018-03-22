package civilisationClicker;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scrollBar.ScrollBar;
import scrollBar.ScrollListener;

public class CivilisationClickerOptionsMenu implements ScrollListener, ItemListener{
	static int musicVolume, numberOfResolutions, selectedResolutionX, selectedResolutionY;
	static int resolutionSelectedIndex;
	static int[] resolutionX, resolutionY;
	boolean resolutionWarning;
	JPanel mainPanel, volumeSettingPanel, volumeContainerPanel, resolutionSettingPanel, resolutionContainerPanel;
	JLabel volumeSettingLabel, resolutionSettingLabel;
	List<String> resolutions = new ArrayList<String>();
	JComboBox<String> resolutionSelector;
	ScrollBar volumeSlider;
	CivilisationClickerOptionsMenu() {
		selectedResolutionX = CivilisationMainClass.gameWidth;
		selectedResolutionY = CivilisationMainClass.gameHeight;
		loadResolutions();
		createOptionsMenu();
	}
	void createOptionsMenu() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setPreferredSize(new Dimension(CivilisationMainClass.gameWidth - 300, CivilisationMainClass.gameHeight));
		mainPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth - 300, CivilisationMainClass.gameHeight));
		volumeContainerPanel = new JPanel(new GridBagLayout());
		volumeContainerPanel.setMinimumSize(new Dimension(CivilisationMainClass.gameWidth - 300, 200));
		volumeContainerPanel.setPreferredSize(new Dimension(CivilisationMainClass.gameWidth - 300, 200));
		volumeContainerPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth - 300, 200));
		volumeSettingPanel = new JPanel();
		volumeSettingPanel.setLayout(new BoxLayout(volumeSettingPanel, BoxLayout.PAGE_AXIS));
		volumeSettingPanel.setPreferredSize(new Dimension(200, 40));
		volumeSettingPanel.setMaximumSize(new Dimension(200, 40));
		volumeSettingLabel = new JLabel("Music Volume:");
		volumeSettingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		String volumeUpButton = "graphics/buttons/volumeleft";
		String volumeDownButton = "graphics/buttons/volumeright";
		String volumeScrollBar = "graphics/buttons/volumeslider";
		volumeSlider = new ScrollBar(ScrollBar.SCROLLHORIZONTAL, new Dimension(200, 20), volumeUpButton, volumeDownButton, volumeScrollBar);
		volumeSlider.createScrollBar(105, 5);
		volumeSlider.setScrollBarPosition(musicVolume);
		volumeSlider.setButtonViewChange(1);
		volumeSlider.setBackgroundImage("graphics/buttons/volumebackground.png");
		volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
		volumeSlider.addScrollListener(this);
		resolutionContainerPanel = new JPanel(new GridBagLayout());
		resolutionContainerPanel.setMinimumSize(new Dimension(CivilisationMainClass.gameWidth - 300, 200));
		resolutionContainerPanel.setPreferredSize(new Dimension(CivilisationMainClass.gameWidth - 300, 200));
		resolutionContainerPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth - 300, 200));
		resolutionSettingPanel = new JPanel();
		resolutionSettingPanel.setLayout(new BoxLayout(resolutionSettingPanel, BoxLayout.PAGE_AXIS));
		resolutionSettingPanel.setPreferredSize(new Dimension(200, 40));
		resolutionSettingPanel.setMaximumSize(new Dimension(200, 40));
		resolutionSettingLabel = new JLabel("Resolution: ");
		resolutionSettingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		String[] list = new String[resolutions.size()];
		resolutions.toArray(list);
		resolutionSelector = new JComboBox<String>(list);
		resolutionSelector.setMaximumSize(new Dimension(200, 40));
		resolutionSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
		resolutionSelector.setSelectedIndex(resolutionSelectedIndex);
		resolutionSelector.addItemListener(this);
		volumeContainerPanel.add(volumeSettingPanel);
		resolutionContainerPanel.add(resolutionSettingPanel);
		volumeSettingPanel.add(volumeSettingLabel);
		volumeSettingPanel.add(volumeSlider);
		resolutionSettingPanel.add(resolutionSettingLabel);
		resolutionSettingPanel.add(resolutionSelector);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(volumeContainerPanel);
		mainPanel.add(resolutionContainerPanel);
		mainPanel.add(Box.createVerticalGlue());
		double volume = (double) musicVolume / 100;
		CivilisationMainClass.musicPlayer.setVolume(volume);
	}
	void loadResolutions() {
		File resolutionsFile = new File("data/resolutions.txt");
		try {
			boolean resolutionFound = false;
			String content;
			Scanner scan = new Scanner(resolutionsFile);
			scan.useDelimiter("/Z");
			content = scan.next();
			scan.close();
			content = content.replaceAll("[\r\n]+", "");
			Scanner colourFileScanner = new Scanner(content);
			colourFileScanner.useDelimiter(";");
			numberOfResolutions = colourFileScanner.nextInt();
			resolutionX = new int[numberOfResolutions];
			resolutionY = new int[numberOfResolutions];
			for (int i=0; i<numberOfResolutions; i++) {
				colourFileScanner.next();
				resolutionX[i] = colourFileScanner.nextInt();
				resolutionY[i] = colourFileScanner.nextInt();
				resolutions.add(resolutionX[i] + "," + resolutionY[i]);
				if (resolutionX[i] == selectedResolutionX && resolutionY[i] == selectedResolutionY) {
					resolutionFound = true;
					resolutionSelectedIndex = i;
				}
			}
			if (!resolutionFound) {
				resolutions.add("Custom:" + CivilisationMainClass.gameWidth + "," + CivilisationMainClass.gameHeight);
				resolutionSelectedIndex = numberOfResolutions;
			}
			colourFileScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void saveSettings() {
		File settingsFile = new File("saved/settings.txt");
		try {
			PrintWriter fw = new PrintWriter(settingsFile);
			fw.write("MusicVolume;" + musicVolume + ";");
			fw.println();
			fw.write("Resolution;" + selectedResolutionX + ";" + selectedResolutionY + ";");
			fw.println();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resolutionWarning) {
			CivilisationMainClass.mainLayeredPanel.add(new CivilisationClickerInfoWindow("Some settings require restart to take affect."), Integer.valueOf(3));
			resolutionWarning = false;
		}
	}
	@Override
	public void viewChanged(int newValue) {
		musicVolume = newValue;
		double volume = (double) newValue / 100;
		CivilisationMainClass.musicPlayer.setVolume(volume);
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			CivilisationMainClass.soundEngine.playClickSound();
			resolutionSelectedIndex = resolutionSelector.getSelectedIndex();
			if (resolutionSelectedIndex != numberOfResolutions) {
				selectedResolutionX = resolutionX[resolutionSelectedIndex];
				selectedResolutionY = resolutionY[resolutionSelectedIndex];
			} else {
				selectedResolutionX = CivilisationMainClass.gameWidth;
				selectedResolutionY = CivilisationMainClass.gameHeight;
			}
			resolutionWarning = selectedResolutionX != CivilisationMainClass.gameWidth || selectedResolutionY != CivilisationMainClass.gameHeight;
		}
	}
}
