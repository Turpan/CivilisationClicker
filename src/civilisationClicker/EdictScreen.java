package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import paintedPanel.PaintedPanel;

public class EdictScreen implements ProvinceIconListener, EdictListener{
	static final int PROVINCEICONWIDTH = 177;
	static final int PROVINCEICONHEIGHT = 74;
	static final Rectangle PROVINCECONTAINERDIMENSIONS = new Rectangle(12, 82, 177, 530);
	static final Rectangle HAPPINESSBARDIMENSIONS = new Rectangle(252, 314, 204, 22);
	static final Rectangle EDICTCONTAINERDIMENSIONS = new Rectangle(219, 52, 225, 246);
	static final Rectangle MODIFIERLABELDIMENSIONS = new Rectangle(215, 314, 29, 22);
	static ImageIcon[] provinceIcons;
	PaintedPanel mainPanel;
	JPanel provinceIconContainer;
	JPanel edictContainer;
	JPanel modifierPanel;
	JLabel modifierLabel;
	JProgressBar happinessBar;
	List<ProvinceIcon> provinceButtons = new ArrayList<ProvinceIcon>();
	List<EdictPanel> edictButtons = new ArrayList<EdictPanel>();
	static int selectedProvince;
	EdictScreen() {
		createEdictScreen();
		restructureProvinceIcons();
	}
	void createEdictScreen() {
		mainPanel = new PaintedPanel();
		mainPanel.bgImage = new ImageIcon("graphics/ui/edictscreen/edictbackground.png");
		mainPanel.setLayout(null);
		mainPanel.setOpaque(false);
		for (int i=0; i<MapScreen.provinceList.size(); i++) {
			ProvinceIcon province = new ProvinceIcon(i);
			province.addProvinceListener(this);
			provinceButtons.add(province);
		}
		for (int i=0; i<DataBase.edictList.size(); i++) {
			EdictPanel edict = new EdictPanel(i);
			edict.addEdictListener(this);
			edictButtons.add(edict);
		}
		provinceIconContainer = new JPanel();
		provinceIconContainer.setLayout(new BoxLayout(provinceIconContainer, BoxLayout.PAGE_AXIS));
		provinceIconContainer.setOpaque(false);
		provinceIconContainer.setBounds(PROVINCECONTAINERDIMENSIONS);
		happinessBar = new JProgressBar();
		happinessBar.setMaximum(200);
		happinessBar.setForeground(Color.RED);
		happinessBar.setBackground(Color.GREEN);
		happinessBar.setBounds(HAPPINESSBARDIMENSIONS);
		modifierPanel = new JPanel(new GridBagLayout());
		modifierPanel.setBounds(MODIFIERLABELDIMENSIONS);
		modifierPanel.setOpaque(false);
		edictContainer = new JPanel();
		edictContainer.setLayout(new BoxLayout(edictContainer, BoxLayout.PAGE_AXIS));
		edictContainer.setBounds(EDICTCONTAINERDIMENSIONS);
		edictContainer.setOpaque(false);
		modifierLabel = new JLabel();
		modifierLabel.setForeground(Color.WHITE);
		for (EdictPanel edict : edictButtons) {
			edictContainer.add(edict.mainPanel);
		}
		modifierPanel.add(modifierLabel);
		mainPanel.add(provinceIconContainer);
		mainPanel.add(happinessBar);
		mainPanel.add(modifierPanel);
		mainPanel.add(edictContainer);
	}
	void restructureProvinceIcons() {
		provinceIconContainer.removeAll();
		for (Province province : MapScreen.provinceList) {
			if (province.owner == CivilisationMainClass.playerID) {
				provinceIconContainer.add(provinceButtons.get(province.ID).mainPanel);
			}
		}
		provinceIconContainer.add(Box.createVerticalGlue());
	}
	void updateLabels() {
		edictContainer.removeAll();
		if (selectedProvince > -1) {
			if (MapScreen.provinceList.get(selectedProvince).owner != CivilisationMainClass.playerID)
				selectedProvince = findNextProvince();
			Province province = MapScreen.provinceList.get(selectedProvince);
			happinessBar.setValue(200 - (int) province.developementList.get(0).productionModifier);
			for (EdictPanel edict : edictButtons) {
				edict.updateLabels();
				edictContainer.add(edict.mainPanel);
			}
		}
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	int findNextProvince() {
		int nextProvince = 0;
		for (Province province : MapScreen.provinceList) {
			if (province.owner == CivilisationMainClass.playerID) {
				nextProvince = province.ID;
				break;
			}
		}
		return nextProvince;
	}
	void timerTick() {
		restructureProvinceIcons();
		updateLabels();
	}
	static void createProvinceIcons() {
		provinceIcons = new ImageIcon[MapScreen.provinceList.size()];
		for (int i=0; i<MapScreen.provinceList.size(); i++) {
			boolean redrawAtEnd = false;
			Color provinceColor = Color.GRAY;
			if (CivilisationMainClass.mapScreen.gameMap.provincePanels[i].provinceColor != CivilisationMainClass.mapScreen.gameMap.ownerColours[CivilisationMainClass.playerID - 1]) {
				provinceColor = CivilisationMainClass.mapScreen.gameMap.provincePanels[i].provinceColor;
				CivilisationMainClass.mapScreen.gameMap.provincePanels[i].provinceColor = CivilisationMainClass.mapScreen.gameMap.ownerColours[CivilisationMainClass.playerID - 1];
				CivilisationMainClass.mapScreen.gameMap.provincePanels[i].drawRectangle();
				redrawAtEnd = true;
			}
			Image imageToScale = CivilisationMainClass.mapScreen.gameMap.provincePanels[i].provinceImage;
			Dimension provinceSize = new Dimension(CivilisationMainClass.mapScreen.gameMap.provincePanels[i].provinceImage.getWidth(),
					CivilisationMainClass.mapScreen.gameMap.provincePanels[i].provinceImage.getHeight());
			Dimension containerSize = new Dimension(PROVINCEICONWIDTH, PROVINCEICONHEIGHT);
			Dimension imageDimension = MathFunctions.getScaledDimension(provinceSize, containerSize);
			imageToScale = imageToScale.getScaledInstance(imageDimension.width, imageDimension.height, Image.SCALE_REPLICATE);
			provinceIcons[i] = new ImageIcon(imageToScale);
			if (redrawAtEnd) {
				CivilisationMainClass.mapScreen.gameMap.provincePanels[i].provinceColor = provinceColor;
				CivilisationMainClass.mapScreen.gameMap.provincePanels[i].drawRectangle();
			}
		}
	}
	@Override
	public void provinceSelected(int province) {
		selectedProvince = province;
		updateLabels();
	}
	@Override
	public void edictClicked(int edict) {
		if (CivilisationMainClass.getPlayer().buyEdict(edict, selectedProvince, 1)) {
			CivilisationMainClass.soundEngine.playClickSound();
			edictButtons.get(edict).showCost();
			updateLabels();
		}
	}
}
interface ProvinceIconListener {
	void provinceSelected(int province);
}
class ProvinceIcon implements MouseListener{
	int province;
	JLayeredPane mainPanel;
	JPanel clickPanel;
	JLabel provinceLabel;
	PaintedPanel seperatorBar;
	PaintedPanel provincePanel;
	static ImageIcon seperatorImage;
	static ImageIcon backgroundImage;
	static int seperatorHeight;
	List<ProvinceIconListener> listeners = new ArrayList<ProvinceIconListener>();
	ProvinceIcon(int province) {
		this.province = province;
		if (seperatorImage == null) {
			seperatorImage = new ImageIcon("graphics/ui/edictscreen/provinceseperator.png");
			backgroundImage = new ImageIcon("graphics/ui/edictscreen/provincebackground.png");
			seperatorHeight = seperatorImage.getIconHeight();
		}
		createVisuals();
	}
	void createVisuals() {
		Dimension panelSize = new Dimension(EdictScreen.PROVINCEICONWIDTH,
				EdictScreen.PROVINCEICONHEIGHT);
		Dimension totalSize = new Dimension(panelSize.width, panelSize.height + seperatorHeight);
		mainPanel = new JLayeredPane();
		mainPanel.setMinimumSize(totalSize);
		mainPanel.setPreferredSize(totalSize);
		mainPanel.setMaximumSize(totalSize);
		clickPanel = new JPanel();
		clickPanel.setOpaque(false);
		clickPanel.setBounds(0, 0, panelSize.width, panelSize.height);
		clickPanel.addMouseListener(this);
		provincePanel = new PaintedPanel();
		provincePanel.bgImage = backgroundImage;
		provincePanel.setLayout(new GridBagLayout());
		provincePanel.setBounds(0, 0, panelSize.width, panelSize.height);
		seperatorBar = new PaintedPanel(PaintedPanel.PAINTHORIZONTAL);
		seperatorBar.bgImage = seperatorImage;
		seperatorBar.setBounds(0, panelSize.height, panelSize.width, seperatorHeight);
		provinceLabel = new JLabel(EdictScreen.provinceIcons[province]);
		provincePanel.add(provinceLabel);
		mainPanel.add(provincePanel, Integer.valueOf(1));
		mainPanel.add(seperatorBar, Integer.valueOf(1));
		mainPanel.add(clickPanel, Integer.valueOf(2));
	}
	void addProvinceListener(ProvinceIconListener listener) {
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
		if (e.getSource() == clickPanel) {
			for (ProvinceIconListener listener : listeners) listener.provinceSelected(province);
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
interface EdictListener {
	void edictClicked(int edict);
}
class EdictPanel implements MouseListener{
	static final Rectangle COUNTPOSITION = new Rectangle(24, 0, 24, 24);
	static final Rectangle NAMEPOSITION = new Rectangle(48, 0, 176, 24);
	int edict;
	JLayeredPane mainPanel;
	PaintedPanel backgroundPanel;
	PaintedPanel seperatorPanel;
	JPanel countPanel;
	JPanel namePanel;
	JPanel clickPanel;
	JLabel countLabel;
	JLabel nameLabel;
	static ImageIcon backgroundImage;
	static ImageIcon seperatorImage;
	static int seperatorHeight;
	static Dimension panelSize;
	List<EdictListener> listeners = new ArrayList<EdictListener>();
	EdictPanel(int edict) {
		this.edict = edict;
		if (seperatorImage == null) {
			seperatorImage = new ImageIcon("graphics/ui/edictscreen/edictseperator.png");
			backgroundImage = new ImageIcon("graphics/ui/edictscreen/edictpanel.png");
			seperatorHeight = seperatorImage.getIconHeight();
			panelSize = new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
		}
		createVisuals();
		updateLabels();
	}
	void createVisuals() {
		Dimension totalSize = new Dimension(panelSize.width, panelSize.height + seperatorHeight);
		System.out.println(totalSize.toString());
		mainPanel = new JLayeredPane();
		mainPanel.setMinimumSize(totalSize);
		mainPanel.setPreferredSize(totalSize);
		mainPanel.setMaximumSize(totalSize);
		backgroundPanel = new PaintedPanel();
		backgroundPanel.bgImage = backgroundImage;
		backgroundPanel.setBounds(0, 0, panelSize.width, panelSize.height);
		seperatorPanel = new PaintedPanel(PaintedPanel.PAINTHORIZONTAL);
		seperatorPanel.bgImage = seperatorImage;
		seperatorPanel.setBounds(0, panelSize.height, panelSize.width, seperatorHeight);
		countPanel = new JPanel(new GridBagLayout());
		countPanel.setOpaque(false);
		countPanel.setBounds(COUNTPOSITION);
		namePanel = new JPanel(new GridBagLayout());
		namePanel.setOpaque(false);
		namePanel.setBounds(NAMEPOSITION);
		clickPanel = new JPanel();
		clickPanel.setOpaque(false);
		clickPanel.setBounds(0, 0, panelSize.width, panelSize.height);
		clickPanel.addMouseListener(this);
		countLabel = new JLabel("0");
		countLabel.setForeground(Color.WHITE);
		nameLabel = new JLabel(DataBase.edictList.get(edict).Name);
		nameLabel.setForeground(Color.WHITE);
		countPanel.add(countLabel);
		namePanel.add(nameLabel);
		mainPanel.add(backgroundPanel, Integer.valueOf(1));
		mainPanel.add(seperatorPanel, Integer.valueOf(1));
		mainPanel.add(countPanel, Integer.valueOf(2));
		mainPanel.add(namePanel, Integer.valueOf(2));
		mainPanel.add(clickPanel, Integer.valueOf(3));
	}
	void updateLabels() {
		int count = MapScreen.provinceList.get
				(EdictScreen.selectedProvince).edictList.get(edict).Count;
		countLabel.setText(MathFunctions.withSuffix(count));
	}
	void addEdictListener(EdictListener listener) {
		listeners.add(listener);
	}
	void showCost() {
		Edict edict = MapScreen.provinceList
				.get(EdictScreen.selectedProvince).edictList.get(this.edict);
		int screen = SuperScreen.governmentPointPool - 1;
		CivilisationMainClass.resourceBar.updateCostLabel(screen, edict.Cost);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		showCost();
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		CivilisationMainClass.resourceBar.resetCostLabel();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == clickPanel) {
			for (EdictListener listener : listeners) listener.edictClicked(edict);
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
