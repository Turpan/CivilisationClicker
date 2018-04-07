package civilisationClicker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import paintedPanel.PaintedPanel;
import scrollBar.ScrollBar;
import scrollBar.ScrollEvent;
import scrollBar.ScrollListener;

public class ClickerScreen implements MouseListener, ScrollListener{
	static int middlePanelWidth;
	static boolean buttonPressed;
	int ID, province, middlePanelSize, scrollWindowSize;
	JPanel middlePanel, mainPanel;
	JPanel buyPanel, sellPanel, buildingListPanel, buildingCentralPanel;
	JPanel[] buildingMasterPanel, buildingTextPanel, buildingNumbersPanel, buildingNamePanel, buildingCountPanel, buildingClickPanel;
	JScrollPane buildingScrollPanel;
	JLabel pointsLabel, pointsPerSecondLabel, pointsPerClickLabel, clickLabel, industryTabLabel, scienceTabLabel, militaryTabLabel, welfareTabLabel;
	JLabel buy1Label, buy10Label, buy100Label, sell1Label, sell10Label, sell100Label, sellLabel, buyLabel;
	JLabel[] buildingIconLabel, buildingNameLabel, buildingCostLabel, buildingCountLabel;
	JLayeredPane[] buildingLayeredPanel;
	PaintedPanel leftPanel, leftBorderPanel, rightBorderPanel, buildingUIPanel[], buildingVerticalUIBar[];
	PaintedPanel industryTabPanel, scienceTabPanel, militaryTabPanel, welfareTabPanel, menuUIBar[];
	PaintedPanel buildingBuyPanel[];
	BuildingGraphicBar[] buildingGraphicPanel;
	ScrollBar buildingScrollBar;
	public ClickerScreen(int ID, int province) {
		this.ID = ID;
		this.province = province;
		mainPanel = new JPanel();
		mainPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth - 300, CivilisationMainClass.gameHeight));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		leftPanel = new PaintedPanel();
		leftPanel.bgImage = new ImageIcon(SuperScreen.leftBarStageImage[ID - 1][0]);
		leftPanel.setMaximumSize(new Dimension(467, CivilisationMainClass.gameHeight));
		leftPanel.setPreferredSize(new Dimension(467, CivilisationMainClass.gameHeight));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftBorderPanel = new PaintedPanel(PaintedPanel.PAINTVERTICAL);
		leftBorderPanel.bgImage = new ImageIcon(SuperScreen.borderPanelImage[ID - 1]);
		leftBorderPanel.setMaximumSize(new Dimension(14, CivilisationMainClass.gameHeight));
		leftBorderPanel.setPreferredSize(new Dimension(14, CivilisationMainClass.gameHeight));
		middlePanel = new JPanel();
		middlePanel.setBackground(Color.BLUE);
		middlePanel.setOpaque(true);
		middlePanel.setPreferredSize(new Dimension(middlePanelWidth, CivilisationMainClass.gameHeight));
		middlePanel.setMaximumSize(new Dimension(middlePanelWidth, CivilisationMainClass.gameHeight));
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
		rightBorderPanel = new PaintedPanel(PaintedPanel.PAINTVERTICAL);
		rightBorderPanel.setLayout(new BoxLayout(rightBorderPanel, BoxLayout.PAGE_AXIS));
		rightBorderPanel.bgImage = new ImageIcon(SuperScreen.borderPanelImage[ID - 1]);
		rightBorderPanel.setMaximumSize(new Dimension(14, CivilisationMainClass.gameHeight));
		rightBorderPanel.setPreferredSize(new Dimension(14, CivilisationMainClass.gameHeight));
		pointsLabel = new JLabel("0");
		pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		pointsLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		clickLabel = new JLabel(SuperScreen.clickImage[ID - 1]);
		clickLabel.addMouseListener(this);
		clickLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		clickLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		pointsPerClickLabel = new JLabel("1");
		pointsPerClickLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		pointsPerClickLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		pointsPerSecondLabel = new JLabel("0/s.");
		pointsPerSecondLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		pointsPerSecondLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		createBuySellButtons();
		readBuildingData();
		createBuildingPanels();
		leftPanel.add(pointsLabel);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		leftPanel.add(clickLabel);
		leftPanel.add(pointsPerClickLabel);
		leftPanel.add(pointsPerSecondLabel);
		mainPanel.add(leftPanel);
		mainPanel.add(leftBorderPanel);
		mainPanel.add(middlePanel);
		mainPanel.add(rightBorderPanel);
	}
	void updateLabels() {
		int a = (int) CivilisationMainClass.playerList.get(CivilisationMainClass.playerID - 1).points[ID - 1];
		ProvinceDevelopement developement = DataBase.mapList.
				get(DataBase.chosenMap).provinceList.get(province).developementList.get(ID - 1);
		int b = (int) developement.pointsPerSecond();
		pointsLabel.setText(MathFunctions.withSuffix(a));
		pointsPerSecondLabel.setText(MathFunctions.withSuffix(b) + "\\s.");
		for (int i=0; i<developement.numberOfBuildings; i++) {
			buildingCountLabel[i].setText(MathFunctions.withSuffix(developement.buildingCount[i]));
			int c = (int) developement.buildingCost[i];
			buildingCostLabel[i].setText(MathFunctions.withSuffix(c));
			int number = 0;
			if ((number = buildingGraphicPanel[i].getNumberOfIcons()) != developement.buildingCount[i]) {
				buildingGraphicPanel[i].addImage(0-number);
				buildingGraphicPanel[i].addImage(developement.buildingCount[i]);
			}
		}
		leftPanel.bgImage = new ImageIcon(SuperScreen.leftBarStageImage[ID - 1][developement.currentStage-1]);
	}
	void clickButton() {
		CivilisationMainClass.playerList.get(CivilisationMainClass.playerID - 1).clickButton(province, ID - 1);
		playClickSound();
		updateLabels();
		CivilisationMainClass.resourceBar.updateLabels();
	}
	void timerTick() {
		updateLabels();
	}
	void playClickSound() {
		SoundEngine.playPointClickSound(ID-1);
	}
	void buyBuilding(int building) {
		int modifier = 0;
		switch (SuperScreen.selectedBuySellButton) {
		case 1:
			modifier = 1;
			break;
		case 2:
			modifier = 10;
			break;
		case 3:
			modifier = 100;
			break;
		case 4:
			modifier = -1;
			break;
		case 5:
			modifier = -10;
			break;
		case 6:
			modifier = -100;
			break;
		}
		CivilisationMainClass.resourceBar.updateLabels();
		DataBase.mapList.get(DataBase.chosenMap).provinceList.get(province).developementList.get(ID - 1).buyBuilding(building, modifier);
		buildingGraphicPanel[building].addImage(modifier);
	}
	void showBuildingCost(int building) {
		int buyModifier = 0;
		int sellModifier = 0;
		int a = Defines.BUILDINGPOINTPOOL;
		switch (SuperScreen.selectedBuySellButton) {
		case 1:
			buyModifier = 1;
			break;
		case 2:
			buyModifier = 10;
			break;
		case 3:
			buyModifier = 100;
			break;
		case 4:
			sellModifier = 1;
			break;
		case 5:
			sellModifier = 10;
			break;
		case 6:
			sellModifier = 100;
			break;
		}
		double buildingCost = DataBase.mapList.get(DataBase.chosenMap).provinceList.get(province).developementList.get(ID - 1).buildingCost[building];
		if (buyModifier != 0) {
			double totalCost = 0;
			double potentialBuildingCost = buildingCost;
			for (int i=0; i<buyModifier; i++) {
				totalCost += potentialBuildingCost;
				potentialBuildingCost = potentialBuildingCost * Defines.BUILDINGSCALEMULTIPLIER;
			}
			CivilisationMainClass.resourceBar.updateCostLabel(a - 1, (int) totalCost);
		} else if (sellModifier != 0) {
			int totalRebate = 0;
			double potentialBuildingCost = buildingCost;
			for (int i=0; i<sellModifier; i++) {
				totalRebate += potentialBuildingCost;
				potentialBuildingCost = potentialBuildingCost / Defines.BUILDINGSCALEMULTIPLIER;
			}
			CivilisationMainClass.resourceBar.updateRefundLabel(a - 1, totalRebate);
		}
	}
	void createBuySellButtons() {
		buyPanel = new JPanel();
		buyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		buyPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		buyPanel.setLayout(new BoxLayout(buyPanel, BoxLayout.LINE_AXIS));
		buyPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
		sellPanel = new JPanel();
		sellPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		sellPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		sellPanel.setLayout(new BoxLayout(sellPanel, BoxLayout.LINE_AXIS));
		sellPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
		buyLabel = new JLabel("Buy");
		buyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		buyLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		buyLabel.setMinimumSize(new Dimension(205, 20));
		sellLabel = new JLabel("Sell");
		sellLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		sellLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		sellLabel.setMinimumSize(new Dimension(205, 20));
		buy1Label = new JLabel("1");
		buy1Label.addMouseListener(this);
		buy1Label.setAlignmentX(Component.LEFT_ALIGNMENT);
		buy1Label.setAlignmentY(Component.TOP_ALIGNMENT);
		buy1Label.setMinimumSize(new Dimension(200, 20));
		buy1Label.setBackground(Color.RED);
		buy10Label = new JLabel("10");
		buy10Label.addMouseListener(this);
		buy10Label.setAlignmentX(Component.LEFT_ALIGNMENT);
		buy10Label.setAlignmentY(Component.TOP_ALIGNMENT);
		buy10Label.setMinimumSize(new Dimension(200, 20));
		buy10Label.setBackground(Color.RED);
		buy100Label = new JLabel("100");
		buy100Label.addMouseListener(this);
		buy100Label.setAlignmentX(Component.LEFT_ALIGNMENT);
		buy100Label.setAlignmentY(Component.TOP_ALIGNMENT);
		buy100Label.setMinimumSize(new Dimension(200, 20));
		buy100Label.setBackground(Color.RED);
		sell1Label = new JLabel("1");
		sell1Label.addMouseListener(this);
		sell1Label.setAlignmentX(Component.LEFT_ALIGNMENT);
		sell1Label.setAlignmentY(Component.TOP_ALIGNMENT);
		sell1Label.setMinimumSize(new Dimension(200, 20));
		sell1Label.setBackground(Color.RED);
		sell10Label = new JLabel("10");
		sell10Label.addMouseListener(this);
		sell10Label.setAlignmentX(Component.LEFT_ALIGNMENT);
		sell10Label.setAlignmentY(Component.TOP_ALIGNMENT);
		sell10Label.setMinimumSize(new Dimension(200, 20));
		sell10Label.setBackground(Color.RED);
		sell100Label = new JLabel("100");
		sell100Label.addMouseListener(this);
		sell100Label.setAlignmentX(Component.LEFT_ALIGNMENT);
		sell100Label.setAlignmentY(Component.TOP_ALIGNMENT);
		sell100Label.setMinimumSize(new Dimension(200, 20));
		sell100Label.setBackground(Color.RED);
		buyPanel.add(buyLabel);
		buyPanel.add(buy1Label);
		buyPanel.add(buy10Label);
		buyPanel.add(buy100Label);
		sellPanel.add(sellLabel);
		sellPanel.add(sell1Label);
		sellPanel.add(sell10Label);
		sellPanel.add(sell100Label);
		//middlePanel.add(buyPanel);
		//middlePanel.add(sellPanel);
		middlePanel.add(Box.createRigidArea(new Dimension(0, 100)));
		colourBuySellLabels();
	}
	void createBuildingPanels() {
		int buildingPanelHeight = CivilisationMainClass.gameHeight - 100;
		String screenType = DataBase.screenTypes.get(ID - 1);
		buildingListPanel = new JPanel();
		buildingListPanel.setLayout(new BoxLayout(buildingListPanel, BoxLayout.PAGE_AXIS));
		buildingScrollPanel = new JScrollPane(buildingListPanel);
		buildingScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		buildingScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		buildingScrollPanel.setBorder(BorderFactory.createEmptyBorder());
		buildingScrollPanel.setMinimumSize(new Dimension(middlePanelWidth, buildingPanelHeight - 28));
		buildingScrollPanel.setPreferredSize(new Dimension(middlePanelWidth, buildingPanelHeight - 28));
		buildingScrollPanel.setMaximumSize(new Dimension(middlePanelWidth, buildingPanelHeight - 28));
		buildingCentralPanel = new JPanel();
		buildingCentralPanel.setLayout(new BoxLayout(buildingCentralPanel, BoxLayout.PAGE_AXIS));
		buildingCentralPanel.setMinimumSize(new Dimension(middlePanelWidth, buildingPanelHeight));
		buildingCentralPanel.setPreferredSize(new Dimension(middlePanelWidth, buildingPanelHeight));
		buildingCentralPanel.setMaximumSize(new Dimension(middlePanelWidth, buildingPanelHeight));
		String upImageLocation = "graphics/buttons/scrollup-" + screenType;
		String downImageLocation = "graphics/buttons/scrolldown-" + screenType;
		String scrollBarLocation = "graphics/buttons/scrollbar-" + screenType;
		buildingScrollBar = new ScrollBar(new Dimension(14, buildingPanelHeight), upImageLocation, downImageLocation, scrollBarLocation);
		buildingScrollBar.setOpaque(false);
		buildingScrollBar.scrollBar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		buildingScrollBar.addScrollListener(this);
		buildingScrollBar.setViewSize(buildingPanelHeight - 28);
		buildingScrollBar.setButtonViewChange(50);
		buildingUIPanel[0] = new PaintedPanel();
		buildingUIPanel[0].bgImage = new ImageIcon(SuperScreen.buildingUIBar[ID - 1]);
		buildingUIPanel[0].setPreferredSize(new Dimension(middlePanelWidth, 14));
		buildingUIPanel[0].setMaximumSize(new Dimension(middlePanelWidth, 14));
		buildingCentralPanel.add(buildingUIPanel[0]);
		buildingCentralPanel.add(buildingScrollPanel);
		scrollWindowSize = 0;
		int numberOfBuildings = DataBase.buildingList.get(ID - 1).buildingList.size();
		int i = 0;
		for (Building building : DataBase.buildingList.get(ID - 1).buildingList) {
			buildingLayeredPanel[i] = new JLayeredPane();
			buildingLayeredPanel[i].setPreferredSize(new Dimension(middlePanelWidth, 100));
			buildingLayeredPanel[i].setMaximumSize(new Dimension(middlePanelWidth, 100));
			buildingClickPanel[i] = new JPanel();
			buildingClickPanel[i].setBounds(0, 0, 241, 100);
			buildingClickPanel[i].setOpaque(false);
			buildingClickPanel[i].addMouseListener(this);
			buildingMasterPanel[i] = new JPanel();
			buildingMasterPanel[i].setLayout(new BoxLayout(buildingMasterPanel[i], BoxLayout.LINE_AXIS));
			buildingMasterPanel[i].setMaximumSize(new Dimension(middlePanelWidth, 100));
			buildingMasterPanel[i].setBounds(0, 0, middlePanelWidth, 100);
			buildingUIPanel[i+1] = new PaintedPanel();
			buildingUIPanel[i+1].bgImage = new ImageIcon(SuperScreen.buildingUIBar[ID - 1]);
			buildingUIPanel[i+1].setPreferredSize(new Dimension(middlePanelWidth, 14));
			buildingUIPanel[i+1].setMaximumSize(new Dimension(middlePanelWidth, 14));
			buildingBuyPanel[i] = new PaintedPanel();
			buildingBuyPanel[i].bgImage = new ImageIcon(SuperScreen.buildingButtonImage[ID - 1]);
			buildingBuyPanel[i].setPreferredSize(new Dimension(241, 100));
			buildingBuyPanel[i].setLayout(new BoxLayout(buildingBuyPanel[i], BoxLayout.LINE_AXIS));
			buildingVerticalUIBar[i] = new PaintedPanel(PaintedPanel.PAINTVERTICAL);
			buildingVerticalUIBar[i].bgImage = new ImageIcon(SuperScreen.borderPanelImage[ID - 1]);
			buildingVerticalUIBar[i].setPaintBufferY(-100);
			buildingVerticalUIBar[i].setPreferredSize(new Dimension(14, 100));
			buildingVerticalUIBar[i].setMaximumSize(new Dimension(14, 100));
			buildingTextPanel[i] = new JPanel();
			buildingTextPanel[i].setLayout(new BoxLayout(buildingTextPanel[i], BoxLayout.PAGE_AXIS));
			buildingTextPanel[i].setMaximumSize(new Dimension(68, 100));
			buildingTextPanel[i].setOpaque(false);
			buildingNamePanel[i] = new JPanel(new GridBagLayout());
			buildingNamePanel[i].setMaximumSize(new Dimension(68, 50));
			buildingNamePanel[i].setOpaque(false);
			buildingNumbersPanel[i] = new JPanel(new GridBagLayout());
			buildingNumbersPanel[i].setMaximumSize(new Dimension(68, 50));
			buildingNumbersPanel[i].setOpaque(false);
			buildingCountPanel[i] = new JPanel(new GridBagLayout());
			buildingCountPanel[i].setMaximumSize(new Dimension(73, 100));
			buildingCountPanel[i].setOpaque(false);
			String[] icon = {DataBase.buildingList.get(ID - 1).buildingList.get(i).IconImageLocation};
			buildingGraphicPanel[i] = new BuildingGraphicBar(DataBase.buildingList.get(ID - 1).buildingList.get(i).BarImageLocation, icon);
			buildingGraphicPanel[i].setMargin(2, -10);
			buildingGraphicPanel[i].setStartBounds(0, 40);
			buildingGraphicPanel[i].setVisibleArea(middlePanelWidth - 255, 100);
			buildingGraphicPanel[i].setMaximumSize(new Dimension(middlePanelWidth - 255, 100));
			buildingIconLabel[i] = new JLabel(new ImageIcon(DataBase.buildingList.get(ID - 1).buildingList.get(i).ImageLocation));
			buildingNameLabel[i] = new JLabel(DataBase.buildingList.get(ID - 1).buildingList.get(i).Name);
			buildingNameLabel[i].setForeground(Color.WHITE);
			buildingCostLabel[i] = new JLabel(Double.toString(building.Cost));
			buildingCostLabel[i].setForeground(Color.WHITE);
			buildingCountLabel[i] = new JLabel(0 + "");
			buildingCountLabel[i].setForeground(Color.WHITE);
			buildingNamePanel[i].add(buildingNameLabel[i]);
			buildingNumbersPanel[i].add(buildingCostLabel[i]);
			buildingTextPanel[i].add(buildingNamePanel[i]);
			buildingTextPanel[i].add(buildingNumbersPanel[i]);
			buildingCountPanel[i].add(buildingCountLabel[i]);
			buildingBuyPanel[i].add(buildingIconLabel[i]);
			buildingBuyPanel[i].add(buildingTextPanel[i]);
			buildingBuyPanel[i].add(buildingCountPanel[i]);
			buildingMasterPanel[i].add(buildingBuyPanel[i]);
			buildingMasterPanel[i].add(buildingVerticalUIBar[i]);
			buildingMasterPanel[i].add(buildingGraphicPanel[i]);
			buildingLayeredPanel[i].add(buildingMasterPanel[i], Integer.valueOf(1));
			buildingLayeredPanel[i].add(buildingClickPanel[i], Integer.valueOf(2));
			buildingListPanel.add(buildingLayeredPanel[i]);
			if (i != numberOfBuildings - 1) {
				buildingListPanel.add(buildingUIPanel[i + 1]);
				scrollWindowSize += 14;
			} else {
				buildingCentralPanel.add(buildingUIPanel[i + 1]);
			}
			scrollWindowSize += 100;
			i++;
		}
		if (scrollWindowSize > (CivilisationMainClass.gameHeight - 100)) {
			buildingScrollBar.setWindowSize(scrollWindowSize);
			rightBorderPanel.add(Box.createVerticalGlue());
			rightBorderPanel.add(buildingScrollBar);
			buildingListPanel.setPreferredSize(new Dimension(middlePanelWidth, scrollWindowSize));
		} else {
			buildingListPanel.setPreferredSize(new Dimension(middlePanelWidth, buildingPanelHeight));
			buildingUIPanel[numberOfBuildings + 1] = new PaintedPanel();
			buildingUIPanel[numberOfBuildings + 1].bgImage = new ImageIcon(SuperScreen.buildingUIBar[ID - 1]);
			buildingUIPanel[numberOfBuildings + 1].setPreferredSize(new Dimension(middlePanelWidth, 14));
			buildingUIPanel[numberOfBuildings + 1].setMaximumSize(new Dimension(middlePanelWidth, 14));
			buildingListPanel.add(buildingUIPanel[numberOfBuildings + 1]);
		}
		middlePanel.add(buildingCentralPanel);
	}
	void colourBuySellLabels() {
		buy1Label.setOpaque(false);
		buy10Label.setOpaque(false);
		buy100Label.setOpaque(false);
		sell1Label.setOpaque(false);
		sell10Label.setOpaque(false);
		sell100Label.setOpaque(false);
		switch (SuperScreen.selectedBuySellButton) {
		case 1:
			buy1Label.setOpaque(true);
			break;
		case 2:
			buy10Label.setOpaque(true);
			break;
		case 3:
			buy100Label.setOpaque(true);
			break;
		case 4:
			sell1Label.setOpaque(true);
			break;
		case 5:
			sell10Label.setOpaque(true);
			break;
		case 6:
			sell100Label.setOpaque(true);
			break;
		default:
			SuperScreen.selectedBuySellButton = 1;
			buy1Label.setOpaque(true);
			break;
		}
		buy1Label.repaint();
		buy10Label.repaint();
		buy100Label.repaint();
		sell1Label.repaint();
		sell10Label.repaint();
		sell100Label.repaint();
	}
	void readBuildingData() {
		int numberOfBuildings = DataBase.buildingList.get(ID - 1).getListSize();
		initialiseData(numberOfBuildings);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		SoundEngine.playClickSound();
		if (e.getSource() == buy1Label && SuperScreen.selectedBuySellButton != 1) {
			SuperScreen.selectedBuySellButton = 1;
			colourBuySellLabels();
			return;
		} else if (e.getSource() == buy10Label && SuperScreen.selectedBuySellButton != 2) {
			SuperScreen.selectedBuySellButton = 2;
			colourBuySellLabels();
			return;
		} else if (e.getSource() == buy100Label && SuperScreen.selectedBuySellButton != 3) {
			SuperScreen.selectedBuySellButton = 3;
			colourBuySellLabels();
			return;
		} else if (e.getSource() == sell1Label && SuperScreen.selectedBuySellButton != 4) {
			SuperScreen.selectedBuySellButton = 4;
			colourBuySellLabels();
			return;
		} else if (e.getSource() == sell10Label && SuperScreen.selectedBuySellButton != 5) {
			SuperScreen.selectedBuySellButton = 5;
			colourBuySellLabels();
			return;
		} else if (e.getSource() == sell100Label && SuperScreen.selectedBuySellButton != 6) {
			SuperScreen.selectedBuySellButton = 6;
			colourBuySellLabels();
			return;
		}
		for (int i=0; i<buildingClickPanel.length; i++) {
			if (e.getSource() == buildingClickPanel[i]) {
				buyBuilding(i);
				return;
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		for (int i=0; i<buildingClickPanel.length; i++) {
			if (e.getSource() == buildingClickPanel[i]) {
				showBuildingCost(i);
				return;
			}
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		CivilisationMainClass.resourceBar.resetCostLabel();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == clickLabel) {
			buttonPressed = true;
			clickLabel.setIcon(SuperScreen.clickImagePressed[ID - 1]);
			clickButton();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if (buttonPressed = true) {
			buttonPressed = false;
			clickLabel.setIcon(SuperScreen.clickImage[ID - 1]);
		}
	}
	void initialiseData(int buildings) {
		int a = buildings;
		buildingLayeredPanel = new JLayeredPane[a];
		buildingClickPanel = new JPanel[a];
		buildingMasterPanel = new JPanel[a];
		buildingUIPanel = new PaintedPanel[a+2];
		buildingBuyPanel = new PaintedPanel[a];
		buildingVerticalUIBar = new PaintedPanel[a];
		buildingNumbersPanel = new JPanel[a];
		buildingNamePanel = new JPanel[a];
		buildingCountPanel = new JPanel[a];
		buildingTextPanel = new JPanel[a];
		buildingGraphicPanel = new BuildingGraphicBar[a];
		buildingIconLabel = new JLabel[a];
		buildingNameLabel = new JLabel[a];
		buildingCostLabel = new JLabel[a];
		buildingCountLabel = new JLabel[a];
	}
	@Override
	public void viewChanged(ScrollEvent scrollEvent) {
		buildingScrollPanel.getViewport().setViewPosition(new Point(0, scrollEvent.getViewChanged()));
	}
}
