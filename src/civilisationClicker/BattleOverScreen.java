package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BattleOverScreen extends JPanel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int province, attacker, defender;
	JPanel mainPanel, combatTitlePanel, centralPanel, attackPanel, attackTitlePanel, defendPanel, defendTitlePanel, scorePanel, buttonPanel;
	JLabel combatTitleLabel, attackerTitleLabel, defenderTitleLabel, resultLabel;
	JButton closeButton;
	List<Unit> attackerTroops;
	List<Unit> defenderTroops;
	List<BattleOverUnitPanel> attackerPanels = new ArrayList<BattleOverUnitPanel>();
	List<BattleOverUnitPanel> defenderPanels = new ArrayList<BattleOverUnitPanel>();
	boolean attackerVictor;
	List<BattleOverScreenListener> listeners = new ArrayList<BattleOverScreenListener>();
	BattleOverScreen(List<Unit> attackerTroops, List<Unit> defenderTroops, int province, int attacker, int defender, boolean attackerVictor) {
		this.province = province;
		this.attacker = attacker;
		this.defender = defender;
		this.attackerTroops = attackerTroops;
		this.defenderTroops = defenderTroops;
		this.attackerVictor = attackerVictor;
		createGameOverScreen();
		createAttackerUnitPanels();
		createDefenderUnitPanels();
	}
	void createGameOverScreen() {
		this.setBounds(350, 50, 600, 800);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		combatTitlePanel = new JPanel(new GridBagLayout());
		combatTitlePanel.setPreferredSize(new Dimension(600, 100));
		combatTitlePanel.setMaximumSize(new Dimension(600, 100));
		centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.LINE_AXIS));
		centralPanel.setPreferredSize(new Dimension(600, 600));
		centralPanel.setMaximumSize(new Dimension(600, 600));
		attackPanel = new JPanel();
		attackPanel.setLayout(new BoxLayout(attackPanel, BoxLayout.PAGE_AXIS));
		attackPanel.setPreferredSize(new Dimension(300, 600));
		attackPanel.setMaximumSize(new Dimension(300, 600));
		attackTitlePanel = new JPanel(new GridBagLayout());
		attackTitlePanel.setPreferredSize(new Dimension(300, 100));
		attackTitlePanel.setMaximumSize(new Dimension(300, 100));
		attackTitlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		defendPanel = new JPanel();
		defendPanel.setLayout(new BoxLayout(defendPanel, BoxLayout.PAGE_AXIS));
		defendPanel.setPreferredSize(new Dimension(300, 600));
		defendPanel.setMaximumSize(new Dimension(300, 600));
		defendTitlePanel = new JPanel(new GridBagLayout());
		defendTitlePanel.setPreferredSize(new Dimension(300, 100));
		defendTitlePanel.setMaximumSize(new Dimension(300, 100));
		defendTitlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		scorePanel = new JPanel();
		scorePanel.setLayout(new GridBagLayout());
		scorePanel.setPreferredSize(new Dimension(600, 50));
		scorePanel.setMaximumSize(new Dimension(600, 50));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setPreferredSize(new Dimension(600, 50));
		buttonPanel.setMaximumSize(new Dimension(600, 50));
		combatTitleLabel = new JLabel("Battle for Province: " + province);
		attackerTitleLabel = new JLabel("Attacker: " + CivilisationMainClass.playerNames[attacker - 1]);
		defenderTitleLabel = new JLabel("Defender: " + CivilisationMainClass.playerNames[defender - 1]);
		closeButton = new JButton("Close Screen");
		closeButton.addMouseListener(this);
		resultLabel = new JLabel("Uh Oh!!!!!!!!!!!!!");
		if (attackerVictor) {
			resultLabel.setText("Attacker won the battle, gaining province " + province + " for their empire.");
		} else if (defenderTroops == null) {
			resultLabel.setText("Defender did not defend, giving attacker control of " + province + " for their empire.");
		} else {
			resultLabel.setText("Defender won the battle, keeping their control over province " + province + ".");
		}
		combatTitlePanel.add(combatTitleLabel);
		attackTitlePanel.add(attackerTitleLabel);
		defendTitlePanel.add(defenderTitleLabel);
		scorePanel.add(resultLabel);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(closeButton);
		attackPanel.add(attackTitlePanel);
		defendPanel.add(defendTitlePanel);
		centralPanel.add(attackPanel);
		centralPanel.add(defendPanel);
		mainPanel.add(combatTitlePanel);
		mainPanel.add(centralPanel);
		mainPanel.add(scorePanel);
		mainPanel.add(buttonPanel);
		this.add(mainPanel);
		CivilisationMainClass.mainLayeredPanel.add(this, Integer.valueOf(3));
	}
	void createAttackerUnitPanels() {
		for (int i=0; i<attackerTroops.size(); i++) {
			if (attackerTroops.get(i).Count > 0) {
				BattleOverUnitPanel unitPanel = new BattleOverUnitPanel(attackerTroops.get(i));
				attackPanel.add(unitPanel.mainPanel);
			}
		}
		attackPanel.add(Box.createVerticalGlue());
	}
	void createDefenderUnitPanels() {
		if (defenderTroops != null) {
			for (int i=0; i<defenderTroops.size(); i++) {
				if (defenderTroops.get(i).Count > 0) {
					BattleOverUnitPanel unitPanel = new BattleOverUnitPanel(defenderTroops.get(i));
					defendPanel.add(unitPanel.mainPanel);
				}
			}
		}
		defendPanel.add(Box.createVerticalGlue());
	}
	void addListener(BattleOverScreenListener listener) {
		listeners.add(listener);
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
		SoundEngine.playClickSound();
		if (e.getSource() == closeButton) {
			for (BattleOverScreenListener listener : listeners) listener.battleOver(this);
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
interface BattleOverScreenListener {
	void battleOver(BattleOverScreen screen);
}
class BattleOverUnitPanel {
	Unit unit;
	JPanel mainPanel;
	JPanel leftTextPanel;
	JPanel rightTextPanel;
	JPanel unitNamePanels;
	JPanel unitCountPanels;
	JPanel upgradePanels;
	JLabel unitIconLabels;
	JLabel unitCountLabels;
	JLabel unitNameLabels;
	JLabel unitLostLabels;
	BattleOverUnitPanel(Unit unit) {
		this.unit = unit;
		createPanels();
	}
	void createPanels() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setPreferredSize(new Dimension(650, 100));
		mainPanel.setMaximumSize(new Dimension(650, 100));
		leftTextPanel = new JPanel();
		leftTextPanel.setLayout(new BoxLayout(leftTextPanel, BoxLayout.PAGE_AXIS));
		leftTextPanel.setPreferredSize(new Dimension(50, 100));
		leftTextPanel.setMaximumSize(new Dimension(50, 100));
		rightTextPanel = new JPanel(new GridBagLayout());
		rightTextPanel.setPreferredSize(new Dimension(50, 100));
		rightTextPanel.setMaximumSize(new Dimension(50, 100));
		unitNamePanels = new JPanel(new GridBagLayout());
		unitNamePanels.setPreferredSize(new Dimension(50, 50));
		unitNamePanels.setMaximumSize(new Dimension(50, 50));
		unitCountPanels = new JPanel(new GridBagLayout());
		unitCountPanels.setPreferredSize(new Dimension(50, 50));
		unitCountPanels.setMaximumSize(new Dimension(50, 50));
		upgradePanels = new JPanel();
		upgradePanels.setLayout(new BoxLayout(upgradePanels, BoxLayout.LINE_AXIS));
		unitIconLabels = new JLabel();
		unitIconLabels.setIcon(new ImageIcon(unit.Icon));
		unitCountLabels = new JLabel(Integer.toString(unit.Count));
		unitNameLabels = new JLabel(unit.Name);
		unitLostLabels = new JLabel((unit.Count/2) + " lost.");
		unitNamePanels.add(unitNameLabels);
		unitCountPanels.add(unitCountLabels);
		leftTextPanel.add(unitNamePanels);
		leftTextPanel.add(unitCountPanels);
		rightTextPanel.add(unitLostLabels);
		mainPanel.add(unitIconLabels);
		mainPanel.add(leftTextPanel);
		mainPanel.add(rightTextPanel);
		mainPanel.add(upgradePanels);
	}
}