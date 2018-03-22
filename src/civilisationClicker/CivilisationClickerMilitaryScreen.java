/*package civilisationClicker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
import scrollBar.ScrollListener;

public class CivilisationClickerMilitaryScreen implements MouseListener, ScrollListener{
	static final double UNITCOSTMULTIPLIER = 1.1;
	static int buyModifier = 1;
	int numberOfUnits, totalPoints, availablePoints, numberOfBattles, middlePanelWidth, scrollWindowSize, scrollWindowPosition;
	int[] unitPoints, unitBaseCost, unitCount, unitAvailableCount;
	boolean[] battle;
	String[] unitName, unitImageLocation;
	double[] unitCurrentCost;
	JLabel titleLabel, totalPointsLabel, availablePointsLabel, battleTitleLabel, noBattlesLabel;
	JLabel[] unitIconLabel, unitNameLabel, unitCountLabel, unitPointsLabel, unitCostLabel;
	JLayeredPane[] unitLayeredPanel;
	JPanel mainPanel, leftPanel, middlePanel, armyBattlesPanel, noBattlesPanel, unitListPanel, unitCentralPanel, armyTitlePanel, armyLabelPanel;
	JPanel[] unitMasterPanel, unitClickPanel, unitLeftTextPanel, unitRightTextPanel, unitNamePanel, unitCountPanel, unitPointsPanel, unitCostPanel;
	JScrollPane unitScrollPanel;
	Set<CivilisationClickerBattleOverScreen> battleOverScreens = new HashSet<CivilisationClickerBattleOverScreen>();
	PaintedPanel leftBorderPanel, middleBorderPanel, rightBorderPanel, battleUIPanel, armyInfoPanel;
	PaintedPanel[] unitUIBar, unitBuyPanel, unitGraphicPanel;
	ScrollBar unitScrollBar;
	CivilisationClickerBattleScreen[] battles;
	CivilisationClickerMilitaryScreen() {
		battles = new CivilisationClickerBattleScreen[CivilisationMainClass.mapScreen.provinceList.size()];
		battle = new boolean[CivilisationMainClass.mapScreen.provinceList.size()];
		middlePanelWidth = CivilisationMainClass.gameWidth - (300 + 467 + 14 + 14);
		createMilitaryScreen();
		loadUnitData();
		createUnitPanels();
	}
	void createMilitaryScreen() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth - 300, CivilisationMainClass.gameHeight));
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.setPreferredSize(new Dimension(453, CivilisationMainClass.gameHeight));
		leftPanel.setMaximumSize(new Dimension(453, CivilisationMainClass.gameHeight));
		leftBorderPanel = new PaintedPanel(PaintedPanel.PAINTVERTICAL);
		leftBorderPanel.bgImage = new ImageIcon(CivilisationMainClass.installDirectory + "\\graphics\\ui\\army.png");
		leftBorderPanel.setMinimumSize(new Dimension(14, CivilisationMainClass.gameHeight));
		leftBorderPanel.setPreferredSize(new Dimension(14, CivilisationMainClass.gameHeight));
		leftBorderPanel.setMaximumSize(new Dimension(14, CivilisationMainClass.gameHeight));
		middleBorderPanel = new PaintedPanel(PaintedPanel.PAINTVERTICAL);
		middleBorderPanel.bgImage = new ImageIcon(CivilisationMainClass.installDirectory + "\\graphics\\ui\\army.png");
		middleBorderPanel.setMinimumSize(new Dimension(14, CivilisationMainClass.gameHeight));
		middleBorderPanel.setPreferredSize(new Dimension(14, CivilisationMainClass.gameHeight));
		middleBorderPanel.setMaximumSize(new Dimension(14, CivilisationMainClass.gameHeight));
		middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
		middlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, CivilisationMainClass.gameHeight));
		rightBorderPanel = new PaintedPanel(PaintedPanel.PAINTVERTICAL);
		rightBorderPanel.setLayout(new BoxLayout(rightBorderPanel, BoxLayout.PAGE_AXIS));
		rightBorderPanel.bgImage = new ImageIcon(CivilisationMainClass.installDirectory + "\\graphics\\ui\\army.png");
		rightBorderPanel.setMinimumSize(new Dimension(14, CivilisationMainClass.gameHeight));
		rightBorderPanel.setPreferredSize(new Dimension(14, CivilisationMainClass.gameHeight));
		rightBorderPanel.setMaximumSize(new Dimension(14, CivilisationMainClass.gameHeight));
		armyInfoPanel = new PaintedPanel();
		armyInfoPanel.bgImage = new ImageIcon(CivilisationMainClass.installDirectory + "\\graphics\\ui\\army-overview.png");
		armyInfoPanel.setLayout(new BoxLayout(armyInfoPanel, BoxLayout.PAGE_AXIS));
		armyInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		armyInfoPanel.setMinimumSize(new Dimension(453, 286));
		armyInfoPanel.setMaximumSize(new Dimension(453, 286));
		armyInfoPanel.setPreferredSize(new Dimension(453, 286));
		armyBattlesPanel = new JPanel();
		armyBattlesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		armyBattlesPanel.setLayout(new BoxLayout(armyBattlesPanel, BoxLayout.PAGE_AXIS));
		armyBattlesPanel.setMinimumSize(new Dimension(453, CivilisationMainClass.gameHeight - 320));
		armyBattlesPanel.setPreferredSize(new Dimension(453, CivilisationMainClass.gameHeight - 320));
		armyBattlesPanel.setMaximumSize(new Dimension(453, CivilisationMainClass.gameHeight - 320));
		armyTitlePanel = new JPanel(new GridBagLayout());
		armyTitlePanel.setOpaque(false);
		armyTitlePanel.setMinimumSize(new Dimension(453, 30));
		armyTitlePanel.setPreferredSize(new Dimension(453, 30));
		armyTitlePanel.setMaximumSize(new Dimension(453, 30));
		armyLabelPanel = new JPanel();
		armyLabelPanel.setOpaque(false);
		armyLabelPanel.setLayout(new BoxLayout(armyLabelPanel, BoxLayout.PAGE_AXIS));
		armyLabelPanel.setMinimumSize(new Dimension(453, 113));
		armyLabelPanel.setPreferredSize(new Dimension(453, 113));
		armyLabelPanel.setMaximumSize(new Dimension(453, 113));
		battleUIPanel = new PaintedPanel();
		battleUIPanel.bgImage = new ImageIcon(CivilisationMainClass.installDirectory + "\\graphics\\ui\\army-side.png");
		battleUIPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		battleUIPanel.setMinimumSize(new Dimension(453, 14));
		battleUIPanel.setPreferredSize(new Dimension(453, 14));
		battleUIPanel.setMaximumSize(new Dimension(453, 14));
		noBattlesPanel = new JPanel();
		noBattlesPanel.setLayout(new BoxLayout(noBattlesPanel, BoxLayout.PAGE_AXIS));
		titleLabel = new JLabel("Army");
		titleLabel.setForeground(Color.WHITE);
		totalPointsLabel = new JLabel("Total Points: 0");
		totalPointsLabel.setForeground(Color.WHITE);
		availablePointsLabel = new JLabel("Avaible Points: 0");
		availablePointsLabel.setForeground(Color.WHITE);
		battleTitleLabel = new JLabel("Battles:");
		noBattlesLabel = new JLabel("There are no currently active battles.");
		armyTitlePanel.add(titleLabel);
		armyLabelPanel.add(totalPointsLabel);
		armyLabelPanel.add(availablePointsLabel);
		armyInfoPanel.add(armyTitlePanel);
		armyInfoPanel.add(armyLabelPanel);
		noBattlesPanel.add(noBattlesLabel);
		armyBattlesPanel.add(battleTitleLabel);
		armyBattlesPanel.add(noBattlesPanel);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		leftPanel.add(armyInfoPanel);
		leftPanel.add(battleUIPanel);
		leftPanel.add(armyBattlesPanel);
		middlePanel.add(Box.createRigidArea(new Dimension(0, 100)));
		mainPanel.add(leftBorderPanel);
		mainPanel.add(leftPanel);
		mainPanel.add(middleBorderPanel);
		mainPanel.add(middlePanel);
		mainPanel.add(rightBorderPanel);
	}
	void createUnitPanels() {
		int unitPanelHeight = CivilisationMainClass.gameHeight - 100;
		unitListPanel = new JPanel();
		unitListPanel.setLayout(new BoxLayout(unitListPanel, BoxLayout.PAGE_AXIS));
		unitScrollPanel = new JScrollPane(unitListPanel);
		unitScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		unitScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		unitScrollPanel.setBorder(BorderFactory.createEmptyBorder());
		unitScrollPanel.setMinimumSize(new Dimension(middlePanelWidth, unitPanelHeight - 28));
		unitScrollPanel.setPreferredSize(new Dimension(middlePanelWidth, unitPanelHeight - 28));
		unitScrollPanel.setMaximumSize(new Dimension(middlePanelWidth, unitPanelHeight - 28));
		unitCentralPanel = new JPanel();
		unitCentralPanel.setLayout(new BoxLayout(unitCentralPanel, BoxLayout.PAGE_AXIS));
		unitCentralPanel.setMinimumSize(new Dimension(middlePanelWidth, unitPanelHeight));
		unitCentralPanel.setPreferredSize(new Dimension(middlePanelWidth, unitPanelHeight));
		unitCentralPanel.setMaximumSize(new Dimension(middlePanelWidth, unitPanelHeight));
		String upImageLocation = CivilisationMainClass.installDirectory + "\\graphics\\buttons\\scrollup-industry";
		String downImageLocation = CivilisationMainClass.installDirectory + "\\graphics\\buttons\\scrolldown-industry";
		String scrollBarLocation = CivilisationMainClass.installDirectory + "\\graphics\\buttons\\scrollbar-industry";
		unitScrollBar = new ScrollBar(new Dimension(14, CivilisationMainClass.gameHeight - 100), upImageLocation, downImageLocation, scrollBarLocation);
		unitScrollBar.scrollBar.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		unitScrollBar.setOpaque(false);
		unitScrollBar.addScrollListener(this);
		unitScrollBar.setViewSize(unitPanelHeight - 28);
		unitScrollBar.setButtonViewChange(50);
		unitUIBar[0] = new PaintedPanel();
		unitUIBar[0].bgImage = new ImageIcon(CivilisationMainClass.installDirectory + "\\graphics\\ui\\army-side.png");
		unitUIBar[0].setPreferredSize(new Dimension(middlePanelWidth, 14));
		unitUIBar[0].setMaximumSize(new Dimension(middlePanelWidth, 14));
		unitCentralPanel.add(unitUIBar[0]);
		unitCentralPanel.add(unitScrollPanel);
		scrollWindowSize = 0;
		for (int i=0; i<numberOfUnits; i++) {
			unitLayeredPanel[i] = new JLayeredPane();
			unitLayeredPanel[i].setMaximumSize(new Dimension(middlePanelWidth, 100));
			unitClickPanel[i] = new JPanel();
			unitClickPanel[i].setBounds(0, 0, 241, 100);
			unitClickPanel[i].setOpaque(false);
			unitClickPanel[i].addMouseListener(this);
			unitMasterPanel[i] = new JPanel();
			unitMasterPanel[i].setLayout(new BoxLayout(unitMasterPanel[i], BoxLayout.LINE_AXIS));
			unitMasterPanel[i].setMaximumSize(new Dimension(middlePanelWidth, 100));
			unitMasterPanel[i].setBounds(0, 0, middlePanelWidth, 100);
			unitUIBar[i + 1] = new PaintedPanel();
			unitUIBar[i + 1].bgImage = new ImageIcon(CivilisationMainClass.installDirectory + "\\graphics\\ui\\army-side.png");
			unitUIBar[i + 1].setPreferredSize(new Dimension(middlePanelWidth, 14));
			unitUIBar[i + 1].setMaximumSize(new Dimension(middlePanelWidth, 14));
			unitBuyPanel[i] = new PaintedPanel();
			unitBuyPanel[i].setPreferredSize(new Dimension(241, 100));
			unitBuyPanel[i].setLayout(new BoxLayout(unitBuyPanel[i], BoxLayout.LINE_AXIS));
			unitLeftTextPanel[i] = new JPanel();
			unitLeftTextPanel[i].setLayout(new BoxLayout(unitLeftTextPanel[i], BoxLayout.PAGE_AXIS));
			unitLeftTextPanel[i].setMaximumSize(new Dimension(68, 100));
			unitLeftTextPanel[i].setOpaque(false);
			unitRightTextPanel[i] = new JPanel();
			unitRightTextPanel[i].setLayout(new BoxLayout(unitRightTextPanel[i], BoxLayout.PAGE_AXIS));
			unitRightTextPanel[i].setMaximumSize(new Dimension(73, 100));
			unitRightTextPanel[i].setOpaque(false);
			unitNamePanel[i] = new JPanel(new GridBagLayout());
			unitNamePanel[i].setMaximumSize(new Dimension(68, 50));
			unitNamePanel[i].setOpaque(false);
			unitCountPanel[i] = new JPanel(new GridBagLayout());
			unitCountPanel[i].setMaximumSize(new Dimension(68, 50));
			unitCountPanel[i].setOpaque(false);
			unitPointsPanel[i] = new JPanel(new GridBagLayout());
			unitPointsPanel[i].setMaximumSize(new Dimension(73, 50));
			unitPointsPanel[i].setOpaque(false);
			unitCostPanel[i] = new JPanel(new GridBagLayout());
			unitCostPanel[i].setMaximumSize(new Dimension(73, 50));
			unitCostPanel[i].setOpaque(false);
			unitGraphicPanel[i] = new PaintedPanel();
			unitGraphicPanel[i].setBackground(Color.BLUE);
			unitGraphicPanel[i].setMaximumSize(new Dimension(middlePanelWidth - 255, 100));
			unitIconLabel[i] = new JLabel();
			unitIconLabel[i].setIcon(new ImageIcon(CivilisationMainClass.installDirectory + unitImageLocation[i]));
			unitNameLabel[i] = new JLabel(unitName[i]);
			unitCountLabel[i] = new JLabel("0 (0)");
			unitPointsLabel[i] = new JLabel(Integer.toString(unitPoints[i]));
			unitCostLabel[i] = new JLabel(Integer.toString(unitBaseCost[i]));
			unitNamePanel[i].add(unitNameLabel[i]);
			unitCountPanel[i].add(unitCountLabel[i]);
			unitPointsPanel[i].add(unitPointsLabel[i]);
			unitCostPanel[i].add(unitCostLabel[i]);
			unitLeftTextPanel[i].add(unitNamePanel[i]);
			unitLeftTextPanel[i].add(unitCountPanel[i]);
			unitRightTextPanel[i].add(unitPointsPanel[i]);
			unitRightTextPanel[i].add(unitCostPanel[i]);
			unitBuyPanel[i].add(unitIconLabel[i]);
			unitBuyPanel[i].add(unitLeftTextPanel[i]);
			unitBuyPanel[i].add(unitRightTextPanel[i]);
			unitMasterPanel[i].add(unitBuyPanel[i]);
			unitMasterPanel[i].add(unitGraphicPanel[i]);
			unitLayeredPanel[i].add(unitMasterPanel[i], Integer.valueOf(1));
			unitLayeredPanel[i].add(unitClickPanel[i], Integer.valueOf(2));
			unitListPanel.add(unitLayeredPanel[i]);
			unitListPanel.add(unitUIBar[i+1]);
			if (i != numberOfUnits - 1) {
				unitListPanel.add(unitUIBar[i + 1]);
				scrollWindowSize += 14;
			} else {
				unitCentralPanel.add(unitUIBar[i + 1]);
			}
			scrollWindowSize += 100;
		}
		if (scrollWindowSize > (CivilisationMainClass.gameHeight - 100)) {
			rightBorderPanel.add(Box.createVerticalGlue());
			rightBorderPanel.add(unitScrollBar);
			unitListPanel.setPreferredSize(new Dimension(middlePanelWidth, scrollWindowSize));
		} else {
			unitListPanel.setPreferredSize(new Dimension(middlePanelWidth, unitPanelHeight));
			unitUIBar[numberOfUnits + 1] = new PaintedPanel();
			unitUIBar[numberOfUnits + 1].bgImage = new ImageIcon(CivilisationMainClass.installDirectory + "\\graphics\\ui\\army-side.png");
			unitUIBar[numberOfUnits + 1].setPreferredSize(new Dimension(middlePanelWidth, 14));
			unitUIBar[numberOfUnits + 1].setMaximumSize(new Dimension(middlePanelWidth, 14));
		}
		middlePanel.add(unitCentralPanel);
	}
	void buyUnit(int unit) {
		int a = CivilisationClickerSuperScreen.militaryPointPool;
		double totalCost = 0;
		double potentialBuildingCost = unitCurrentCost[unit];
		for (int i=0; i<buyModifier; i++) {
			totalCost += potentialBuildingCost;
			potentialBuildingCost = potentialBuildingCost * UNITCOSTMULTIPLIER;
		}
		if (CivilisationMainClass.clickerMaster.points[a - 1] >= totalCost) {
			CivilisationMainClass.clickerMaster.points[a - 1] -= totalCost;
			unitCurrentCost[unit] = potentialBuildingCost;
			unitCount[unit] += buyModifier;
			unitAvailableCount[unit] += buyModifier;
			totalPoints += unitPoints[unit];
			availablePoints += unitPoints[unit];
		}
		updateLabels();
		CivilisationMainClass.resourceBar.updateLabels();
	}
	void showUnitCost(int unit) {
		int a = CivilisationClickerSuperScreen.militaryPointPool;
		double totalCost = 0;
		double potentialBuildingCost = unitCurrentCost[unit];
		for (int i=0; i<buyModifier; i++) {
			totalCost += potentialBuildingCost;
			potentialBuildingCost = potentialBuildingCost * UNITCOSTMULTIPLIER;
		}
		CivilisationMainClass.resourceBar.updateCostLabel(a - 1, (int) totalCost);
	}
	void updateLabels() {
		for (int i=0; i<numberOfUnits; i++) {
			unitCountLabel[i].setText(unitCount[i] + " (" + unitAvailableCount[i] + ")");
			int cost = (int) unitCurrentCost[i];
			unitCostLabel[i].setText(Integer.toString(cost));
		}
		totalPointsLabel.setText("Total Points: " + totalPoints);
		availablePointsLabel.setText("Available Points: " + availablePoints);
	}
	void openBattle(int province) {
		CivilisationMainClass.mainPanel.removeAll();
		CivilisationMainClass.mainPanel.add(battles[province].mainPanel);
		CivilisationMenuBar.selectedPanel = 10;
		CivilisationMainClass.frame.revalidate();
		CivilisationMainClass.frame.repaint();
	}
	void createBattle(int[] unitID, int unitCount[], int province, int attackerID, int defenderID) {
		battles[province] = new CivilisationClickerBattleScreen(province, attackerID, defenderID, unitID, unitCount);
		battle[province] = true;
		CivilisationMainClass.mapScreen.colorBorder(province, Color.RED);
		if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPESINGLE) {
			String output = "battle;attackerjoin;" + province + ";" + attackerID + ";" + defenderID + ";" + unitID.length + ";";
			for (int i=0; i<unitID.length; i++) {
				output += (unitID[i] + ";" + unitCount[i] + ";");
			}
			CivilisationMainClass.networkCommunication(output);
		}
	}
	void recieveBattleData(int[] unitID, int unitCount[], int  province, int attackerID, int defenderID) {
		battles[province] = new CivilisationClickerBattleScreen(province, attackerID, defenderID, unitID, unitCount);
		battle[province] = true;
		CivilisationMainClass.mapScreen.colorBorder(province, Color.RED);
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "battle;attackerjoin;" + province + ";" + attackerID + ";" + defenderID + ";" + unitID.length + ";";
			for (int i=0; i<unitID.length; i++) {
				output += (unitID[i] + ";" + unitCount[i] + ";");
			}
			CivilisationMainClass.relayClientData(output, attackerID);
		}
	}
	void joinBattle(int[] unitID, int unitCount[], int province, int attackerID, int defenderID) {
		battles[province].defenderJoinedBattle(unitID, unitCount);
		if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPESINGLE) {
			String output = "battle;defenderjoin;" + province + ";" + attackerID + ";" + defenderID + ";" + unitID.length + ";";
			for (int i=0; i<unitID.length; i++) {
				output += (unitID[i] + ";" + unitCount[i] + ";");
			}
			CivilisationMainClass.networkCommunication(output);
		}
	}
	void recieveJoinBattleData(int[] unitID, int unitCount[], int province, int attackerID, int defenderID) {
		battles[province].defenderJoinedBattle(unitID, unitCount);
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "battle;defenderjoin;" + province + ";" + attackerID + ";" + defenderID + ";" + unitID.length + ";";
			for (int i=0; i<unitID.length; i++) {
				output += (unitID[i] + ";" + unitCount[i] + ";");
			}
			CivilisationMainClass.relayClientData(output, defenderID);
		}
	}
	void recieveAttackerBonus(int province, double bonus, int playerID) {
		battles[province].attackerLeadTroopsBonus = bonus;
		battles[province].calculatePower();
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "battle;attackerlead;" + province + ";" + bonus + ";";
			CivilisationMainClass.relayClientData(output, playerID);
		}
	}
	void recieveDefenderBonus(int province, double bonus, int playerID) {
		battles[province].defenderLeadTroopsBonus = bonus;
		battles[province].calculatePower();
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "battle;defenderlead;" + province + ";" + bonus + ";";
			CivilisationMainClass.relayClientData(output, playerID);
		}
	}
	void calculateLosses(int[] unitID, int[] unitTroopCount) {
		for (int i=0; i<unitID.length; i++) {
			int unitsLost = unitTroopCount[i] / 2;
			int unitsKept = unitTroopCount[i] -= unitsLost;
			unitCount[unitID[i]] -= unitsLost;
			unitAvailableCount[unitID[i]] += unitsKept;
		}
		updateLabels();
	}
	void recieveBattleOverData(int province, boolean attackerVictor) {
		if (battle[province]) {
			battles[province].recieveBattleOverData(attackerVictor);
		}
	}
	void timerTick() {
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			for (int i=0; i<CivilisationMainClass.mapScreen.provinceList.size(); i++) {
				if (battle[i]) {
					battles[i].battleTickTimer += 1;
					battles[i].updateTickTimer(battles[i].battleTickTimer);
					String output = "battle;progress;" + i + ";" + battles[i].battleTickTimer + ";";
					CivilisationMainClass.networkCommunication(output);
				}
			}	
		}
	}
	void updateBattleTick(int province, int progress) {
		battles[province].updateTickTimer(progress);
	}
	void createFinishBattleScreen(int[] attackerUnitID, int[] attackerTroopCount, int[] defenderUnitID, int[] defenderTroopCount, int province, int attacker, int defender, boolean attackerVictor) {
		battleOverScreens.add(new CivilisationClickerBattleOverScreen(attackerUnitID, attackerTroopCount, defenderUnitID, defenderTroopCount, province, attacker, defender, attackerVictor));
	}
	void loadUnitData() {
		File buildingFile = new File(CivilisationMainClass.installDirectory + "\\data\\army-units.txt");
		FileInputStream fs;
		try {
			fs = new FileInputStream(buildingFile);
			BufferedReader buildingReader = new BufferedReader(new InputStreamReader(fs));
			String line = buildingReader.readLine();
			Scanner scan = new Scanner(line);
			scan.useDelimiter(";");
			numberOfUnits = scan.nextInt();
			scan.close();
			initialiseUnitArrays(numberOfUnits);
			for (int i=0; i<numberOfUnits; i++) {
				line = buildingReader.readLine();
				Scanner lineReader = new Scanner(line);
				lineReader.useDelimiter(";");
				lineReader.next();
				unitName[i] = lineReader.next();
				unitImageLocation[i] = lineReader.next();
				unitBaseCost[i] = lineReader.nextInt();
				unitCurrentCost[i] = unitBaseCost[i];
				unitPoints[i] = lineReader.nextInt();
				lineReader.close();
			}
			buildingReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void initialiseUnitArrays(int a) {
		unitPoints = new int[a];
		unitBaseCost = new int[a];
		unitCurrentCost = new double[a];
		unitCount = new int[a];
		unitAvailableCount = new int[a];
		unitName = new String[a];
		unitImageLocation = new String[a];
		unitLayeredPanel = new JLayeredPane[a];
		unitMasterPanel = new JPanel[a];
		unitClickPanel = new JPanel[a];
		unitBuyPanel = new PaintedPanel[a];
		unitLeftTextPanel = new JPanel[a];
		unitRightTextPanel = new JPanel[a];
		unitNamePanel = new JPanel[a];
		unitCountPanel = new JPanel[a];
		unitPointsPanel = new JPanel[a];
		unitCostPanel = new JPanel[a];
		unitGraphicPanel = new PaintedPanel[a];
		unitUIBar = new PaintedPanel[a + 2];
		unitIconLabel = new JLabel[a];
		unitNameLabel = new JLabel[a];
		unitCountLabel = new JLabel[a];
		unitPointsLabel = new JLabel[a];
		unitCostLabel = new JLabel[a];
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		for (int i=0; i<numberOfUnits; i++) {
			if (e.getSource() == unitClickPanel[i]) {
				showUnitCost(i);
				return;
			}
		}
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		CivilisationMainClass.resourceBar.resetCostLabel();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		CivilisationMainClass.soundEngine.playClickSound();
		for (int i=0; i<numberOfUnits; i++) {
			if (e.getSource() == unitClickPanel[i]) {
				buyUnit(i);
				return;
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void viewChanged(int newValue) {
		unitScrollPanel.getViewport().setViewPosition(new Point(0, scrollWindowPosition));
	}
}
*/