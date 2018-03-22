/*package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class CivilisationClickerBattleScreen implements MouseListener{
	JLayeredPane battleInfoLayeredPanel;
	JPanel mainPanel, centralPanel, attackPanel, defendPanel, attackTitlePanel, defendTitlePanel, scorePanel, combatTitlePanel, battleInfoPanel, battleInfoClickPanel;
	JPanel[] attackerUnitPanels, attackerLeftTextPanel, attackerRightTextPanel, attackerUnitNamePanels, attackerUnitCountPanels, attackerUpgradePanels;
	JPanel[] defenderUnitPanels, defenderLeftTextPanel, defenderRightTextPanel, defenderUnitNamePanels, defenderUnitCountPanels, defenderUpgradePanels;
	JLabel combatTitleLabel, attackerTitleLabel, defenderTitleLabel, attackerLeadTroopBonusLabel, combatInfoLabel, attackerInfoLabel, defenderInfoLabel, defenderLeadTroopBonusLabel, defenderRevealLabel;
	JLabel[] attackerUnitIconLabels, attackerUnitCountLabels, attackerUnitNameLabels, attackerUnitPointsLabels;
	JLabel[] defenderUnitIconLabels, defenderUnitCountLabels, defenderUnitNameLabels, defenderUnitPointsLabels;
	JButton leadTroopsButton, joinBattleButton;
	JProgressBar winChanceBar;
	int defender, attacker, province, battleTickTimer;
	int[] attackerUnitID, attackerTroopCount, defenderUnitID, defenderTroopCount; 
	double attackerLeadTroopsBonus, defenderLeadTroopsBonus, attackerPointTotal, defenderPointTotal, attackerWinChance;
	double[] attackerUnitPointTotal, defenderUnitPointTotal;
	boolean attackerArrived;
	CivilisationClickerBattleScreen(int province, int attacker, int defender, int[] attackerUnitID, int[]attackerTroopCount) {
		this.province = province;
		this.defender = defender;
		this.attacker = attacker;
		this.attackerUnitID = attackerUnitID;
		this.attackerTroopCount = attackerTroopCount;
		createBattleListPanel();
		createBattleScreen();
		createAttackerUnitPanels();
	}
	void defenderJoinedBattle(int[] defenderUnitID, int[] defenderTroopCount) {
		this.defenderUnitID = defenderUnitID;
		this.defenderTroopCount = defenderTroopCount;
		if (defender == CivilisationMainClass.playerID) {
			createDefenderUnitPanels();
		}
	}
	void createBattleListPanel() {
		battleInfoLayeredPanel = new JLayeredPane();
		battleInfoLayeredPanel.setPreferredSize(new Dimension(467, 100));
		battleInfoLayeredPanel.setMaximumSize(new Dimension(467, 100));
		battleInfoClickPanel = new JPanel();
		battleInfoClickPanel.setOpaque(false);
		battleInfoClickPanel.addMouseListener(this);
		battleInfoClickPanel.setBounds(0, 0, 467, 100);
		battleInfoPanel = new JPanel();
		battleInfoPanel.setLayout(new BoxLayout(battleInfoPanel, BoxLayout.PAGE_AXIS));
		battleInfoPanel.setBounds(0, 0, 467, 100);
		combatInfoLabel = new JLabel("Battle for Province: " + province);
		attackerInfoLabel = new JLabel("Attacker: " + CivilisationMainClass.playerNames[attacker - 1]);
		defenderInfoLabel = new JLabel("Defender: " + CivilisationMainClass.playerNames[defender - 1]);
		battleInfoPanel.add(combatInfoLabel);
		battleInfoPanel.add(attackerInfoLabel);
		battleInfoPanel.add(defenderInfoLabel);
		battleInfoLayeredPanel.add(battleInfoPanel, Integer.valueOf(1));
		battleInfoLayeredPanel.add(battleInfoClickPanel, Integer.valueOf(2));
		CivilisationMainClass.armyScreen.armyBattlesPanel.add(battleInfoLayeredPanel);
		if (CivilisationMainClass.armyScreen.numberOfBattles == 0) {
			CivilisationMainClass.armyScreen.armyBattlesPanel.remove(CivilisationMainClass.armyScreen.noBattlesPanel);
		}
		CivilisationMainClass.armyScreen.numberOfBattles += 1;
	}
	void createBattleScreen() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		combatTitlePanel = new JPanel(new GridBagLayout());
		combatTitlePanel.setPreferredSize(new Dimension(1300, 100));
		combatTitlePanel.setMaximumSize(new Dimension(1300, 100));
		centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.LINE_AXIS));
		centralPanel.setPreferredSize(new Dimension(1300, 700));
		centralPanel.setMaximumSize(new Dimension(1300, 700));
		attackPanel = new JPanel();
		attackPanel.setLayout(new BoxLayout(attackPanel, BoxLayout.PAGE_AXIS));
		attackPanel.setPreferredSize(new Dimension(650, 700));
		attackPanel.setMaximumSize(new Dimension(650, 700));
		attackTitlePanel = new JPanel(new GridBagLayout());
		attackTitlePanel.setPreferredSize(new Dimension(650, 100));
		attackTitlePanel.setMaximumSize(new Dimension(650, 100));
		attackTitlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		defendPanel = new JPanel();
		defendPanel.setLayout(new BoxLayout(defendPanel, BoxLayout.PAGE_AXIS));
		defendPanel.setPreferredSize(new Dimension(650, 700));
		defendPanel.setMaximumSize(new Dimension(650, 700));
		defendTitlePanel = new JPanel(new GridBagLayout());
		defendTitlePanel.setPreferredSize(new Dimension(650, 100));
		defendTitlePanel.setMaximumSize(new Dimension(650, 100));
		defendTitlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		scorePanel = new JPanel();
		scorePanel.setLayout(new GridBagLayout());
		scorePanel.setPreferredSize(new Dimension(1300, 100));
		scorePanel.setMaximumSize(new Dimension(1300, 100));
		combatTitleLabel = new JLabel("Battle for Province: " + province);
		attackerTitleLabel = new JLabel("Attacker: " + CivilisationMainClass.playerNames[attacker - 1]);
		defenderTitleLabel = new JLabel("Defender: " + CivilisationMainClass.playerNames[defender - 1]);
		winChanceBar = new JProgressBar();
		winChanceBar.setMaximumSize(new Dimension(400, 40));
		winChanceBar.setPreferredSize(new Dimension(400, 40));
		winChanceBar.setMaximum(100);
		winChanceBar.setStringPainted(true);
		winChanceBar.setForeground(CivilisationMainClass.mapScreen.playerColor[attacker - 1]);
		winChanceBar.setBackground(CivilisationMainClass.mapScreen.playerColor[defender - 1]);
		combatTitlePanel.add(combatTitleLabel);
		attackTitlePanel.add(attackerTitleLabel);
		defendTitlePanel.add(defenderTitleLabel);
		scorePanel.add(winChanceBar);
		attackPanel.add(attackTitlePanel);
		defendPanel.add(defendTitlePanel);
		if (CivilisationMainClass.playerID == defender) {
			joinBattleButton = new JButton("Join Battle");
			joinBattleButton.addMouseListener(this);
			defendPanel.add(joinBattleButton);
		} else {
			defenderRevealLabel = new JLabel("Defender's units will be revealed once the battle has started.");
			defendPanel.add(defenderRevealLabel);
		}
		centralPanel.add(attackPanel);
		centralPanel.add(defendPanel);
		mainPanel.add(combatTitlePanel);
		mainPanel.add(centralPanel);
		mainPanel.add(scorePanel);
	}
	void createDefenderUnitPanels() {
		if (CivilisationMainClass.playerID == defender) {
			defendPanel.remove(joinBattleButton);
		} else {
			defendPanel.remove(defenderRevealLabel);
		}
		int a = defenderUnitID.length;
		defenderUnitPointTotal = new double[a];
		defenderUnitPanels = new JPanel[a];
		defenderLeftTextPanel = new JPanel[a];
		defenderRightTextPanel = new JPanel[a];
		defenderUnitNamePanels = new JPanel[a];
		defenderUnitCountPanels = new JPanel[a];
		defenderUpgradePanels = new JPanel[a];
		defenderUnitIconLabels = new JLabel[a];
		defenderUnitCountLabels = new JLabel[a];
		defenderUnitNameLabels = new JLabel[a];
		defenderUnitPointsLabels = new JLabel[a];
		for (int i=0; i<a; i++) {
			int b = defenderUnitID[i];
			defenderUnitPointTotal[i] = (CivilisationMainClass.armyScreen.unitPoints[b]) * defenderTroopCount[i];
			defenderUnitPanels[i] = new JPanel();
			defenderUnitPanels[i].setLayout(new BoxLayout(defenderUnitPanels[i], BoxLayout.LINE_AXIS));
			defenderUnitPanels[i].setPreferredSize(new Dimension(650, 100));
			defenderUnitPanels[i].setMaximumSize(new Dimension(650, 100));
			defenderLeftTextPanel[i] = new JPanel();
			defenderLeftTextPanel[i].setLayout(new BoxLayout(defenderLeftTextPanel[i], BoxLayout.PAGE_AXIS));
			defenderLeftTextPanel[i].setPreferredSize(new Dimension(50, 100));
			defenderLeftTextPanel[i].setMaximumSize(new Dimension(50, 100));
			defenderRightTextPanel[i] = new JPanel(new GridBagLayout());
			defenderRightTextPanel[i].setPreferredSize(new Dimension(50, 100));
			defenderRightTextPanel[i].setMaximumSize(new Dimension(50, 100));
			defenderUnitNamePanels[i] = new JPanel(new GridBagLayout());
			defenderUnitNamePanels[i].setPreferredSize(new Dimension(50, 50));
			defenderUnitNamePanels[i].setMaximumSize(new Dimension(50, 50));
			defenderUnitCountPanels[i] = new JPanel(new GridBagLayout());
			defenderUnitCountPanels[i].setPreferredSize(new Dimension(50, 50));
			defenderUnitCountPanels[i].setMaximumSize(new Dimension(50, 50));
			defenderUpgradePanels[i] = new JPanel();
			defenderUpgradePanels[i].setLayout(new BoxLayout(defenderUpgradePanels[i], BoxLayout.LINE_AXIS));
			defenderUnitIconLabels[i] = new JLabel();
			defenderUnitIconLabels[i].setIcon(new ImageIcon(CivilisationMainClass.installDirectory + CivilisationMainClass.armyScreen.unitImageLocation[b]));
			defenderUnitCountLabels[i] = new JLabel(Integer.toString(defenderTroopCount[i]));
			defenderUnitNameLabels[i] = new JLabel(CivilisationMainClass.armyScreen.unitName[b]);
			defenderUnitPointsLabels[i] = new JLabel("" + (int) defenderUnitPointTotal[i]);
			defenderUnitNamePanels[i].add(defenderUnitNameLabels[i]);
			defenderUnitCountPanels[i].add(defenderUnitCountLabels[i]);
			defenderLeftTextPanel[i].add(defenderUnitNamePanels[i]);
			defenderLeftTextPanel[i].add(defenderUnitCountPanels[i]);
			defenderRightTextPanel[i].add(defenderUnitPointsLabels[i]);
			defenderUnitPanels[i].add(defenderUnitIconLabels[i]);
			defenderUnitPanels[i].add(defenderLeftTextPanel[i]);
			defenderUnitPanels[i].add(defenderRightTextPanel[i]);
			defenderUnitPanels[i].add(defenderUpgradePanels[i]);
			defendPanel.add(defenderUnitPanels[i]);
		}
		defendPanel.add(Box.createVerticalGlue());
		if (defender == CivilisationMainClass.playerID) {
			leadTroopsButton = new JButton("Lead Troops");
			leadTroopsButton.setEnabled(false);
			leadTroopsButton.addMouseListener(this);
		}
		defenderLeadTroopBonusLabel = new JLabel("0.00");
		defendPanel.add(defenderLeadTroopBonusLabel);
		calculatePower();
	}
	void createAttackerUnitPanels() {
		int a = attackerUnitID.length;
		attackerUnitPointTotal = new double[a];
		attackerUnitPanels = new JPanel[a];
		attackerLeftTextPanel = new JPanel[a];
		attackerRightTextPanel = new JPanel[a];
		attackerUnitNamePanels = new JPanel[a];
		attackerUnitCountPanels = new JPanel[a];
		attackerUpgradePanels = new JPanel[a];
		attackerUnitIconLabels = new JLabel[a];
		attackerUnitCountLabels = new JLabel[a];
		attackerUnitNameLabels = new JLabel[a];
		attackerUnitPointsLabels = new JLabel[a];
		for (int i=0; i<a; i++) {
			int b = attackerUnitID[i];
			attackerUnitPointTotal[i] = (CivilisationMainClass.armyScreen.unitPoints[b]) * attackerTroopCount[i];
			attackerUnitPanels[i] = new JPanel();
			attackerUnitPanels[i].setLayout(new BoxLayout(attackerUnitPanels[i], BoxLayout.LINE_AXIS));
			attackerUnitPanels[i].setPreferredSize(new Dimension(650, 100));
			attackerUnitPanels[i].setMaximumSize(new Dimension(650, 100));
			attackerLeftTextPanel[i] = new JPanel();
			attackerLeftTextPanel[i].setLayout(new BoxLayout(attackerLeftTextPanel[i], BoxLayout.PAGE_AXIS));
			attackerLeftTextPanel[i].setPreferredSize(new Dimension(50, 100));
			attackerLeftTextPanel[i].setMaximumSize(new Dimension(50, 100));
			attackerRightTextPanel[i] = new JPanel(new GridBagLayout());
			attackerRightTextPanel[i].setPreferredSize(new Dimension(50, 100));
			attackerRightTextPanel[i].setMaximumSize(new Dimension(50, 100));
			attackerUnitNamePanels[i] = new JPanel(new GridBagLayout());
			attackerUnitNamePanels[i].setPreferredSize(new Dimension(50, 50));
			attackerUnitNamePanels[i].setMaximumSize(new Dimension(50, 50));
			attackerUnitCountPanels[i] = new JPanel(new GridBagLayout());
			attackerUnitCountPanels[i].setPreferredSize(new Dimension(50, 50));
			attackerUnitCountPanels[i].setMaximumSize(new Dimension(50, 50));
			attackerUpgradePanels[i] = new JPanel();
			attackerUpgradePanels[i].setLayout(new BoxLayout(attackerUpgradePanels[i], BoxLayout.LINE_AXIS));
			attackerUnitIconLabels[i] = new JLabel();
			attackerUnitIconLabels[i].setIcon(new ImageIcon(CivilisationMainClass.installDirectory + CivilisationMainClass.armyScreen.unitImageLocation[b]));
			attackerUnitCountLabels[i] = new JLabel(Integer.toString(attackerTroopCount[i]));
			attackerUnitNameLabels[i] = new JLabel(CivilisationMainClass.armyScreen.unitName[b]);
			attackerUnitPointsLabels[i] = new JLabel("" + (int) attackerUnitPointTotal[i]);
			attackerUnitNamePanels[i].add(attackerUnitNameLabels[i]);
			attackerUnitCountPanels[i].add(attackerUnitCountLabels[i]);
			attackerLeftTextPanel[i].add(attackerUnitNamePanels[i]);
			attackerLeftTextPanel[i].add(attackerUnitCountPanels[i]);
			attackerRightTextPanel[i].add(attackerUnitPointsLabels[i]);
			attackerUnitPanels[i].add(attackerUnitIconLabels[i]);
			attackerUnitPanels[i].add(attackerLeftTextPanel[i]);
			attackerUnitPanels[i].add(attackerRightTextPanel[i]);
			attackerUnitPanels[i].add(attackerUpgradePanels[i]);
			attackPanel.add(attackerUnitPanels[i]);
		}
		attackPanel.add(Box.createVerticalGlue());
		if (attacker == CivilisationMainClass.playerID) {
			leadTroopsButton = new JButton("Lead Troops");
			leadTroopsButton.setEnabled(false);
			leadTroopsButton.addMouseListener(this);
		}
		attackerLeadTroopBonusLabel = new JLabel("0.00");
		attackPanel.add(attackerLeadTroopBonusLabel);
		//calculatePower();
	}
	void updateTickTimer(int tickTimer) {
		battleTickTimer = tickTimer;
		if (!attackerArrived) {
			if (battleTickTimer >= 300) {
				attackerArrived = true;
				battleTickTimer = 0;
				if (defender != CivilisationMainClass.playerID) {
					createDefenderUnitPanels();
				}
			}
		} else {
			if (battleTickTimer >= 300) {
				if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT) {
					CivilisationMainClass.armyScreen.battle[province] = false;
					CivilisationMainClass.armyScreen.armyBattlesPanel.remove(battleInfoPanel);
					calculateVictor();
					if (CivilisationMainClass.playerID == attacker) {
						CivilisationMainClass.armyScreen.calculateLosses(attackerUnitID, attackerTroopCount);
					} else if (CivilisationMainClass.playerID == defender) {
						CivilisationMainClass.armyScreen.calculateLosses(defenderUnitID, defenderTroopCount);
					}
				}
			}
		}
	}
	void recieveBattleOverData(boolean attackerVictor) {
		CivilisationMainClass.mapScreen.colorBorder(province, Color.BLACK);
		CivilisationMainClass.armyScreen.battle[province] = false;
		CivilisationMainClass.armyScreen.createFinishBattleScreen(attackerUnitID, attackerTroopCount, defenderUnitID, defenderTroopCount, province, attacker, defender, attackerVictor);
		CivilisationMainClass.armyScreen.armyBattlesPanel.remove(battleInfoLayeredPanel);
		CivilisationMainClass.armyScreen.numberOfBattles -= 1;
		if (CivilisationMainClass.armyScreen.numberOfBattles == 0) {
			CivilisationMainClass.armyScreen.armyBattlesPanel.add(CivilisationMainClass.armyScreen.noBattlesPanel);
		}
	}
	void calculateWinChance() {
		attackerWinChance = 0;
		if (attackerPointTotal > defenderPointTotal) {
			if (attackerPointTotal >= (defenderPointTotal * 2)) {
				attackerWinChance = 100;
			} else {
				double chance = attackerPointTotal / defenderPointTotal;
				chance -= 1;
				chance = chance/2;
				chance = chance * 100;
				attackerWinChance = chance + 50;
				System.out.println(attackerWinChance);
			}
		} else if (attackerPointTotal < defenderPointTotal) {
			if (defenderPointTotal >= (attackerPointTotal * 2)) {
				attackerWinChance = 0;
			} else {
				double chance = defenderPointTotal / attackerPointTotal;
				chance -= 1;
				chance = chance/2;
				chance = chance * 100;
				attackerWinChance = 50 - chance;
				System.out.println(attackerWinChance);
			}
		} else {
			attackerWinChance = 50;
		}
		updateLabels();
	}
	void calculateVictor() {
		boolean attackerVictor = false;
		String output = "";
		calculateWinChance();
		int randomNum = ThreadLocalRandom.current().nextInt(1, 101);
		CivilisationMainClass.mapScreen.colorBorder(province, Color.BLACK);
		CivilisationMainClass.armyScreen.battle[province] = false;
		if (randomNum <= attackerWinChance) {
			CivilisationMainClass.resetProvince(province, true, attacker);
			attackerVictor = true;
			output = "provincestatus;owner;" + attacker + ";" + province + ";";
			CivilisationMainClass.networkCommunication(output);
		} else {
			attackerVictor = false;
		}
		output = "battle;finished;" + province + ";" + attackerVictor + ";";
		CivilisationMainClass.networkCommunication(output);
		CivilisationMainClass.armyScreen.armyBattlesPanel.remove(battleInfoLayeredPanel);
		CivilisationMainClass.armyScreen.numberOfBattles -= 1;
		if (CivilisationMainClass.armyScreen.numberOfBattles == 0) {
			CivilisationMainClass.armyScreen.armyBattlesPanel.add(CivilisationMainClass.armyScreen.noBattlesPanel);
		}
		CivilisationMainClass.armyScreen.createFinishBattleScreen(attackerUnitID, attackerTroopCount, defenderUnitID, defenderTroopCount, province, attacker, defender, attackerVictor);
	}
	void calculatePower() {
		double attackerpower = 0;
		double defenderpower = 0;
		for (int i=0; i<attackerUnitID.length; i++) {
			attackerpower += attackerUnitPointTotal[i];
		}
		for (int i=0; i<defenderUnitID.length; i++) {
			defenderpower += defenderUnitPointTotal[i];
		}
		attackerpower = attackerpower * (attackerLeadTroopsBonus + 1);
		defenderpower = defenderpower * (defenderLeadTroopsBonus + 1);
		attackerPointTotal = attackerpower;
		defenderPointTotal = defenderpower;
		calculateWinChance();
		updateLabels();
	}
	void updateLabels() {
		attackerLeadTroopBonusLabel.setText("" + attackerLeadTroopsBonus);
		defenderLeadTroopBonusLabel.setText("" + defenderLeadTroopsBonus);
		winChanceBar.setValue((int) attackerWinChance);
		winChanceBar.setString(attackerWinChance + "%");
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
		CivilisationMainClass.soundEngine.playClickSound();
		if (e.getSource() == battleInfoClickPanel) {
			CivilisationMainClass.mainPanel.removeAll();
			CivilisationMainClass.mainPanel.add(mainPanel);
			CivilisationMenuBar.selectedPanel = 10;
			CivilisationMainClass.frame.revalidate();
		} else if (e.getSource() == joinBattleButton) {
			CivilisationClickerOrderTroopsScreen joinBattleScreen = new CivilisationClickerOrderTroopsScreen(attacker, province, false);
			CivilisationMainClass.mainLayeredPanel.add(joinBattleScreen, Integer.valueOf(2));
			CivilisationMainClass.frame.revalidate();
			CivilisationMainClass.frame.repaint();
		} else if (e.getSource() == leadTroopsButton) {
			if (CivilisationMainClass.playerID == attacker) {
				attackerLeadTroopsBonus += 0.01;
				if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPESINGLE) {
					String output = "battle;attackerlead;" + province + ";" + attackerLeadTroopsBonus + ";";
					CivilisationMainClass.networkCommunication(output);
				}
			} else if (CivilisationMainClass.playerID == defender) {
				defenderLeadTroopsBonus += 0.01;
				if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPESINGLE) {
					String output = "battle;defenderlead;" + province + ";" + defenderLeadTroopsBonus + ";";
					CivilisationMainClass.networkCommunication(output);
				}
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
*/