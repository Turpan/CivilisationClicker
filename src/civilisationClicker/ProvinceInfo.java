package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import paintedPanel.PaintedPanel;

public class ProvinceInfo implements ColoniseListener, ProvinceInfoMilitaryListener,
	provinceInfoHappinessListener{
	static final Rectangle TITLEBOUNDS = new Rectangle(31, 86, 221, 24);
	static final Rectangle BARBOUNDS = new Rectangle(70, 112, 143, 17);
	static final Rectangle BUTTONBOUNDS = new Rectangle(53, 153, 179, 24);
	static Dimension panelSize;
	PaintedPanel mainPanel;
	ImageIcon backgroundColonised;
	ImageIcon backgroundUncolonised;
	ImageIcon backgroundNoProvince;
	ProvinceInfoColonise coloniseInfoPanel;
	ProvinceInfoMilitary militaryInfoPanel;
	ProvinceInfoHappiness happinessInfoPanel;
	ProvinceInfo() {
		backgroundColonised = new ImageIcon("graphics/ui/mapui-bottom/provinceinfo-owned.png");
		backgroundUncolonised = new ImageIcon("graphics/ui/mapui-bottom/provinceinfo-unowned.png");
		backgroundNoProvince = new ImageIcon("graphics/ui/mapui-bottom/provinceinfo-noprovince.png");
		createGraphics();
	}
	void createGraphics() {
		panelSize = new Dimension(backgroundColonised.getIconWidth(), backgroundColonised.getIconHeight());
		mainPanel = new PaintedPanel();
		mainPanel.bgImage = backgroundColonised;
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setMinimumSize(panelSize);
		mainPanel.setPreferredSize(panelSize);
		mainPanel.setMaximumSize(panelSize);
		mainPanel.setOpaque(false);
		coloniseInfoPanel = new ProvinceInfoColonise();
		coloniseInfoPanel.addListener(this);
		militaryInfoPanel = new ProvinceInfoMilitary();
		militaryInfoPanel.addListener(this);
		happinessInfoPanel = new ProvinceInfoHappiness();
		happinessInfoPanel.addListener(this);
	}
	void updateLabels() {
		if (MapScreen.selectedProvince > -1) {
			Province selectedProvince = MapScreen.getSelectedProvince();
			if (selectedProvince.owner > 0) {
				mainPanel.bgImage = backgroundColonised;
				if (selectedProvince.coloniseInProgress) {
					if (coloniseInfoPanel.mainPanel.getParent() != mainPanel) {
						mainPanel.removeAll();
						mainPanel.add(coloniseInfoPanel.mainPanel);
					}
					coloniseInfoPanel.updateLabels();
				} else if (CivilisationMainClass.battleList.battleInProvince(selectedProvince.ID)) {
					if (militaryInfoPanel.mainPanel.getParent() != mainPanel) {
						mainPanel.removeAll();
						mainPanel.add(militaryInfoPanel.mainPanel);
					}
					militaryInfoPanel.updateLabels();
				} else if (selectedProvince.owner != CivilisationMainClass.playerID) {
					if (militaryInfoPanel.mainPanel.getParent() != mainPanel) {
						mainPanel.removeAll();
						mainPanel.add(militaryInfoPanel.mainPanel);
					}
					militaryInfoPanel.updateLabels();
				} else {
					if (happinessInfoPanel.mainPanel.getParent() != mainPanel) {
						mainPanel.removeAll();
						mainPanel.add(happinessInfoPanel.mainPanel);
					}
					happinessInfoPanel.updateLabels();
				}
			} else {
				mainPanel.bgImage = backgroundUncolonised;
				if (coloniseInfoPanel.mainPanel.getParent() != mainPanel) {
					mainPanel.removeAll();
					mainPanel.add(coloniseInfoPanel.mainPanel);
				}
				coloniseInfoPanel.updateLabels();
			}
		} else {
			mainPanel.removeAll();
			mainPanel.bgImage = backgroundNoProvince;
		}
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	@Override
	public void colonisePressed() {
		Province selectedProvince = MapScreen.getSelectedProvince();
		Country playerCountry = CivilisationMainClass.getPlayer();
		if (selectedProvince.coloniseInProgress && selectedProvince.owner == CivilisationMainClass.playerID) {
			selectedProvince.coloniseButtonPressed();
		} else if (playerCountry.adjacencyList.contains(Integer.valueOf(selectedProvince.ID))){
			playerCountry.colonisationCheck(selectedProvince.ID);
		}
	}
	@Override
	public void invadeButtonPressed() {
		CivilisationMainClass.resourceBar.provinceInvaded(MapScreen.selectedProvince);
	}
	@Override
	public void openBattlePressed() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void happinessButtonPressed() {
		CivilisationMainClass.resourceBar.openEdictScreen();
		CivilisationMainClass.resourceBar.edictScreen.provinceSelected(MapScreen.selectedProvince);
	}
}
abstract class InfoTemplate {
	JPanel mainPanel;
	JPanel titlePanel;
	JLabel titleLabel;
	JLabel buttonLabel;
	JProgressBar infoBar;
	PaintedPanel infoButton;
	ColoniseListener listener;
	InfoTemplate() {
		Dimension panelSize = ProvinceInfo.panelSize;
		mainPanel = new JPanel();
		mainPanel.setMinimumSize(panelSize);
		mainPanel.setPreferredSize(panelSize);
		mainPanel.setMaximumSize(panelSize);
		mainPanel.setLayout(null);
		mainPanel.setOpaque(false);
		titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setBounds(ProvinceInfo.TITLEBOUNDS);
		titlePanel.setOpaque(false);
		titleLabel = new JLabel();
		titleLabel.setForeground(Color.white);
		infoBar = new JProgressBar();
		infoBar.setMaximum(Province.COLONISEDURATION);
		infoBar.setBounds(ProvinceInfo.BARBOUNDS);
		infoButton = new PaintedPanel();
		infoButton.setLayout(new GridBagLayout());
		infoButton.setOpaque(false);
		infoButton.setBounds(ProvinceInfo.BUTTONBOUNDS);
		buttonLabel = new JLabel("Colonise");
		buttonLabel.setForeground(Color.white);
		titlePanel.add(titleLabel);
		infoButton.add(buttonLabel);
		mainPanel.add(titlePanel);
		mainPanel.add(infoBar);
		mainPanel.add(infoButton);
	}
}
interface ColoniseListener {
	void colonisePressed();
}
class ProvinceInfoColonise extends InfoTemplate implements MouseListener{
	ProvinceInfoColonise() {
		super();
		infoButton.addMouseListener(this);
	}
	void updateLabels() {
		Province selectedProvince = MapScreen.getSelectedProvince();
		Country playerCountry = CivilisationMainClass.getPlayer();
		if (selectedProvince.coloniseInProgress) {
			titleLabel.setText("Colonisation in progress.");
			infoBar.setValue(selectedProvince.coloniseProgress);
		} else if (selectedProvince.owner == 0) {
			if (playerCountry.adjacencyList.contains(Integer.valueOf(selectedProvince.ID))) {
				titleLabel.setText("Empty Province.");
			} else {
				titleLabel.setText("Non-Accessible Province.");
			}
			infoBar.setValue(0);
		}
	}
	void addListener(ColoniseListener listener) {
		this.listener = listener;
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		Province selectedProvince = MapScreen.getSelectedProvince();
		if (selectedProvince.owner == 0 || selectedProvince.owner == CivilisationMainClass.playerID) {
			listener.colonisePressed();
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		Province selectedProvince = MapScreen.getSelectedProvince();
		Country playerCountry = CivilisationMainClass.getPlayer();
		if (selectedProvince.owner == 0) {
			CivilisationMainClass.resourceBar.updateCostLabel(playerCountry.coloniseCost);
		}
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		CivilisationMainClass.resourceBar.resetCostLabel();
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
interface ProvinceInfoMilitaryListener {
	void invadeButtonPressed();
	void openBattlePressed();
}
class ProvinceInfoMilitary extends InfoTemplate implements MouseListener{
	ProvinceInfoMilitaryListener listener;
	ProvinceInfoMilitary() {
		super();
		infoButton.addMouseListener(this);
		infoBar.setMaximum(Battle.BATTLEDURATION);
	}
	void updateLabels() {
		Province province = MapScreen.getSelectedProvince();
		Country playerCountry = CivilisationMainClass.getPlayer();
		if (province.owner == CivilisationMainClass.playerID) {
			Battle battle = CivilisationMainClass.battleList.getBattle(province.ID);
			titleLabel.setText("Battle in Progress.");
			infoBar.setValue(battle.tickCount);
			buttonLabel.setText("Open Battle");
		} else if (CivilisationMainClass.battleList.battleInProvince(province.ID)){
			Battle battle = CivilisationMainClass.battleList.getBattle(province.ID);
			titleLabel.setText("Battle in Progress.");
			infoBar.setValue(battle.tickCount);
			buttonLabel.setText("Open Battle");
		} else if (playerCountry.adjacencyList.contains(province.ID)) {
			titleLabel.setText("Accessible Province.");
			infoBar.setValue(0);
			buttonLabel.setText("Invade Province");
		} else {
			titleLabel.setText("Non-accessible Province.");
			infoBar.setValue(0);
			buttonLabel.setText("Cannot Invade");
		}
	}
	void addListener(ProvinceInfoMilitaryListener listener) {
		this.listener = listener;
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
		if (e.getSource() == infoButton) {
			Province province = MapScreen.getSelectedProvince();
			if (CivilisationMainClass.battleList.battleInProvince(province.ID)) {
				listener.openBattlePressed();
			} else {
				listener.invadeButtonPressed();
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
interface provinceInfoHappinessListener {
	void happinessButtonPressed();
}
class ProvinceInfoHappiness extends InfoTemplate implements MouseListener {
	provinceInfoHappinessListener listener;
	ProvinceInfoHappiness() {
		super();
		infoButton.addMouseListener(this);
		buttonLabel.setText("Open Edicts");
	}
	void updateLabels() {
		Province selectedProvince = MapScreen.getSelectedProvince();
		if (selectedProvince.revoltRisk) {
			titleLabel.setText("Revolt Brewing.");
			infoBar.setMaximum(Province.REVOLTTIME);
			infoBar.setValue(selectedProvince.revoltRiskCounter);
			infoBar.setBackground(Color.WHITE);
			infoBar.setForeground(Color.BLACK);
		} else {
			titleLabel.setText(selectedProvince.developementList.get(0).productionModifier + "% production.");
			infoBar.setMaximum(200);
			infoBar.setValue(200 - (int) selectedProvince.developementList.get(0).productionModifier);
			infoBar.setForeground(Color.RED);
			infoBar.setBackground(Color.GREEN);
		}
	}
	void addListener(provinceInfoHappinessListener listener) {
		this.listener = listener;
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
		if (e.getSource() == infoButton) {
			listener.happinessButtonPressed();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}