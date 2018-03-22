package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import paintedPanel.PaintedPanel;

public class CivilisationClickerArmyScreen implements UnitListener, InvasionListener, BattleOverScreenListener{
	static final Rectangle UNITPANELSIZE = new Rectangle(137, 67, 317, 272);
	static final Rectangle STRENGTHBARSIZE = new Rectangle(80, 32, 40, 234);
	static final Rectangle STRENGTHTEXTSIZE = new Rectangle(7, 132, 59, 32);
	static final Rectangle INVASIONBOUNDS = new Rectangle(10, 283, 110, 56);
	static CivilisationClickerBattleList battleList = new CivilisationClickerBattleList();
	static Rectangle ARMYPANELSIZE;
	static ImageIcon mainBackground;
	PaintedPanel mainPanel;
	JPanel unitPanel;
	JPanel strengthTextPanel;
	JLabel strengthTextLabel;
	JProgressBar strengthBar;
	InvasionMap invasionMap;
	List<UnitPanel> unitButtons = new ArrayList<UnitPanel>();
	List<CivilisationClickerBattleOverScreen> finishedBattles = new ArrayList<CivilisationClickerBattleOverScreen>();
	CivilisationClickerArmyScreen() {
		if(mainBackground == null) initialiseValues();
		createArmyScreen();
	}
	void createArmyScreen() {
		invasionMap = new InvasionMap(INVASIONBOUNDS);
		invasionMap.containerPanel.setBounds(INVASIONBOUNDS);
		invasionMap.addListener(this);
		mainPanel = new PaintedPanel();
		mainPanel.bgImage = mainBackground;
		mainPanel.setOpaque(false);
		mainPanel.setLayout(null);
		for (int i=0; i<CivilisationClickerDataBase.unitList.size(); i++) {
			UnitPanel unit = new UnitPanel(i);
			unit.addUnitListener(this);
			unitButtons.add(unit);
		}
		strengthTextPanel = new JPanel(new GridBagLayout());
		strengthTextPanel.setOpaque(false);
		strengthTextPanel.setBounds(STRENGTHTEXTSIZE);
		unitPanel = new JPanel();
		unitPanel.setLayout(new BoxLayout(unitPanel, BoxLayout.PAGE_AXIS));
		unitPanel.setBounds(UNITPANELSIZE);
		unitPanel.setOpaque(false);
		for (UnitPanel unit : unitButtons) {
			unitPanel.add(unit.mainPanel);
		}
		strengthBar = new JProgressBar(JProgressBar.VERTICAL);
		strengthBar.setMaximum(200);
		strengthBar.setForeground(Color.RED);
		strengthBar.setBackground(Color.GREEN);
		strengthBar.setBounds(STRENGTHBARSIZE);
		strengthTextLabel = new JLabel("<html>0<br/>0</html>");
		strengthTextLabel.setForeground(Color.WHITE);
		strengthTextPanel.add(strengthTextLabel);
		mainPanel.add(unitPanel);
		mainPanel.add(strengthBar);
		mainPanel.add(strengthTextPanel);
		mainPanel.add(invasionMap.containerPanel);
	}
	void updateLabels() {
		for (UnitPanel unit : unitButtons) unit.updateLabels();
		int strength = 0;
		int ownPoints = CivilisationMainClass.getPlayer().totalUnitPoints;
		for(CivilisationClickerCountry country : CivilisationMainClass.playerList) 
			if(!country.equals(CivilisationMainClass.getPlayer())) strength += country.totalUnitPoints;
		strength = strength / (CivilisationMainClass.playerList.size() - 1);
		strengthBar.setValue(200 - (int) calculateMilitaryStrength(ownPoints, strength));
		strengthBar.repaint();
		strengthTextLabel.setText("<html>" + ownPoints + "<br/>" + strength + "</html>");
	}
	double calculateMilitaryStrength(int ownStrenght, int averageStrength) {
		double militaryStrength = 0;
		if (ownStrenght > averageStrength) {
			if (ownStrenght >= (averageStrength * 2)) {
				militaryStrength = 200;
			} else {
				double chance = ownStrenght / averageStrength;
				chance -= 1;
				chance = chance * 100;
				militaryStrength = chance + 100;
			}
		} else if (ownStrenght < averageStrength) {
			if (averageStrength >= (ownStrenght * 2)) {
				militaryStrength = 0;
			} else {
				double chance = averageStrength / ownStrenght;
				chance -= 1;
				chance = chance * 100;
				militaryStrength = 100 - chance;
			}
		} else {
			militaryStrength = 100;
		}
		return militaryStrength;
	}
	void timerTick() {
		updateLabels();
	}
	void resetScreen() {
		invasionMap.resetInvasionScreen();
	}
	static void initialiseValues() {
		mainBackground = new ImageIcon("graphics/ui/armyscreen/armybackground.png");
		ARMYPANELSIZE = new Rectangle(0, CivilisationClickerResourceBar.RESOURCEBARSIZE,
				mainBackground.getIconWidth(), mainBackground.getIconHeight());
	}
	@Override
	public void unitClicked(int ID, int amount) {
		if (CivilisationMainClass.getPlayer().buyUnit(ID, amount)) {
			updateLabels();
			CivilisationMainClass.resourceBar.updateLabels();
		}
	}
	@Override
	public void provinceSelected(int provinceid) {
		CivilisationClickerCountry country = CivilisationMainClass.getPlayer();
		if (provinceid != -1) {
			CivilisationClickerProvince province = CivilisationClickerMapScreen.provinceList.get(provinceid);
			if (country.adjacencyList.contains(Integer.valueOf(provinceid)) && province.owner != 0
					&& !province.coloniseInProgress) {
				CivilisationMainClass.resourceBar.provinceInvaded(provinceid);
			}
		}
	}
	public void createFinishBattleScreen(List<CivilisationClickerUnit> attackerTroops,
			List<CivilisationClickerUnit> defenderTroops, int province, int attacker, int defender,
			boolean attackerVictor) {
		CivilisationClickerBattleOverScreen battleFinished = new CivilisationClickerBattleOverScreen(attackerTroops, defenderTroops, province, attacker, defender, attackerVictor);
		battleFinished.addListener(this);
		finishedBattles.add(battleFinished);
		CivilisationMainClass.resourceBar.mainPanel.add(battleFinished, Integer.valueOf(3));
	}
	@Override
	public void battleOver(CivilisationClickerBattleOverScreen screen) {
		CivilisationMainClass.resourceBar.mainPanel.remove(screen);
		finishedBattles.remove(screen);
	}
}
interface UnitListener {
	void unitClicked(int ID, int amount);
}
class UnitPanel implements MouseListener{
	static final Rectangle NAMEBOUNDS = new Rectangle(63, 0, 124, 41);
	static final Rectangle COUNTBOUNDS = new Rectangle(263, 0, 54, 41);
	static ImageIcon panelImage;
	static ImageIcon seperatorImage;
	static Dimension panelSize;
	static Dimension totalSize;
	static int seperatorHeight;
	int ID;
	ImageIcon unitIcon;
	JLayeredPane mainPanel;
	PaintedPanel backgroundPanel;
	PaintedPanel seperatorPanel;
	PaintedPanel iconPanel;
	JPanel clickPanel;
	JPanel namePanel;
	JPanel countPanel;
	JLabel nameLabel;
	JLabel countLabel;
	List<UnitListener> listeners = new ArrayList<UnitListener>();
	UnitPanel(int ID) {
		this.ID = ID;
		if (panelImage == null) initialiseValues();
		createUnitPanel();
	}
	void createUnitPanel() {
		CivilisationClickerUnit unit = CivilisationMainClass.getPlayer().unitList.get(ID);
		mainPanel = new JLayeredPane();
		mainPanel.setLayout(null);
		mainPanel.setMinimumSize(totalSize);
		mainPanel.setPreferredSize(totalSize);
		mainPanel.setMaximumSize(totalSize);
		backgroundPanel = new PaintedPanel();
		backgroundPanel.bgImage = panelImage;
		backgroundPanel.setBounds(0, 0, panelSize.width, panelSize.height);
		seperatorPanel = new PaintedPanel();
		seperatorPanel.bgImage = seperatorImage;
		seperatorPanel.setBounds(0, panelSize.height, panelSize.width, seperatorHeight);
		clickPanel = new JPanel();
		clickPanel.setOpaque(false);
		clickPanel.addMouseListener(this);
		clickPanel.setBounds(0, 0, panelSize.width, panelSize.height);
		iconPanel = new PaintedPanel();
		iconPanel.bgImage = new ImageIcon(unit.Icon);
		iconPanel.setBounds(0, 0, iconPanel.bgImage.getIconWidth(), iconPanel.bgImage.getIconHeight());
		namePanel = new JPanel(new GridBagLayout());
		namePanel.setBounds(NAMEBOUNDS);
		namePanel.setOpaque(false);
		countPanel = new JPanel(new GridBagLayout());
		countPanel.setBounds(COUNTBOUNDS);
		countPanel.setOpaque(false);
		nameLabel = new JLabel(unit.Name);
		nameLabel.setForeground(Color.WHITE);
		countLabel = new JLabel(0 + "");
		countLabel.setForeground(Color.WHITE);
		namePanel.add(nameLabel);
		countPanel.add(countLabel);
		mainPanel.add(backgroundPanel, Integer.valueOf(1));
		mainPanel.add(seperatorPanel, Integer.valueOf(1));
		mainPanel.add(iconPanel, Integer.valueOf(2));
		mainPanel.add(namePanel, Integer.valueOf(2));
		mainPanel.add(countPanel, Integer.valueOf(2));
		mainPanel.add(clickPanel, Integer.valueOf(3));
	}
	void initialiseValues() {
		panelImage = new ImageIcon("graphics/ui/armyscreen/unitbackground.png");
		seperatorImage = new ImageIcon("graphics/ui/armyscreen/unitseperator.png");
		seperatorHeight = seperatorImage.getIconHeight();
		panelSize = new Dimension(panelImage.getIconWidth(), panelImage.getIconHeight());
		totalSize = new Dimension(panelSize.width, panelSize.height + seperatorHeight);
	}
	void addUnitListener(UnitListener listener) {
		listeners.add(listener);
	}
	void updateLabels() {
		CivilisationClickerUnit unit = CivilisationMainClass.getPlayer().unitList.get(ID);
		countLabel.setText(unit.Count + "");
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		CivilisationClickerUnit unit = CivilisationMainClass.getPlayer().unitList.get(ID);
		int screen = CivilisationClickerSuperScreen.militaryPointPool - 1;
		CivilisationMainClass.resourceBar.updateCostLabel(screen, unit.Cost);
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		CivilisationMainClass.resourceBar.resetCostLabel();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == clickPanel) {
			CivilisationMainClass.soundEngine.playClickSound();
			for (UnitListener listener : listeners) listener.unitClicked(ID, 1);
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
interface InvasionListener {
	void provinceSelected(int province);
}
class InvasionMap extends CivilisationClickerScaledMap{
	List<InvasionListener> listeners = new ArrayList<InvasionListener>();
	JPanel containerPanel;
	JPanel invasionPanel;
	JLabel invasionLabel;
	public InvasionMap(Rectangle viewSize) {
		super(CivilisationClickerDataBase.mapList.get(CivilisationClickerDataBase.chosenMap).map, 
				"data/province maps/", CivilisationMainClass.playerList.size(), 
				new Dimension(viewSize.width, viewSize.height));
		MAPCONTAINERWIDTH = viewSize.width;
		MAPCONTAINERHEIGHT = viewSize.height;
		loadProvinces();
	}
	void resetInvasionScreen() {
		for (int i=0; i<provinceColors.size(); i++) {
			provincePanels[i].provinceColor = Color.GRAY;
			provincePanels[i].drawRectangle();
			provinceBorders[i].provinceColor = Color.BLACK;
			provinceBorders[i].drawRectangle();
		}
		provinceSelected = -1;
		containerPanel.removeAll();
		containerPanel.add(mapContainerPanel);
	}
	public void createProvinceMap() {
		super.createProvinceMap();
		clickPanel.removeMouseMotionListener(this);
		mapContainerPanel.setBounds(0, 0, MAPCONTAINERWIDTH, MAPCONTAINERHEIGHT);
		containerPanel = new JPanel(null);
		containerPanel.setOpaque(false);
		invasionPanel = new JPanel(new GridBagLayout());
		invasionPanel.setOpaque(false);
		invasionPanel.addMouseListener(this);
		invasionPanel.setBounds(0, 0, MAPCONTAINERWIDTH, MAPCONTAINERHEIGHT);
		invasionLabel = new JLabel("Invade Province");
		invasionLabel.setForeground(Color.WHITE);
		invasionPanel.add(invasionLabel);
		containerPanel.add(invasionPanel);
	}
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == clickPanel) {
			int province = provinceSelected;
			super.mousePressed(e);
			if (province > -1) {
				provincePanels[province].provinceColor = Color.GRAY;
				provincePanels[province].drawRectangle();
				provinceBorders[province].provinceColor = Color.BLACK;
				provinceBorders[province].drawRectangle();
			}
			if (provinceSelected > -1) {
				provincePanels[provinceSelected].provinceColor = Color.RED;
				provincePanels[provinceSelected].drawRectangle();
				provinceBorders[provinceSelected].provinceColor = Color.RED;
				provinceBorders[provinceSelected].drawRectangle();
			}
			for (InvasionListener listener : listeners) listener.provinceSelected(provinceSelected);
		} else if (e.getSource() == invasionPanel) {
			containerPanel.remove(invasionPanel);
			containerPanel.add(mapContainerPanel);
		}
	}
	void addListener(InvasionListener listener) {
		listeners.add(listener);
	}
}
