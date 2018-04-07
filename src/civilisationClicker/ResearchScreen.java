package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import paintedPanel.PaintedPanel;

public class ResearchScreen implements ResearchListener{
	static final int researchOptions = 3;
	int researchPanelWidth, researchPanelHeight;
	PaintedPanel mainPanel;
	CivilisationClickerResearchPool[] researchPoolList;
	ResearchScreen() {
		researchPoolList = new CivilisationClickerResearchPool[DataBase.screenTypes.size()];
		createResourceScreen();
		for (int i=0; i<DataBase.screenTypes.size(); i++) {
			findNextResearch(i);
		}
	}
	void findNextResearch(int screenType) {
		List<Research> workingList = new ArrayList<Research>();
		for (Research research : 
			CivilisationMainClass.getPlayer().researchList.get(screenType).researchList) {
			workingList.add(research);
		}
		List<Research> researchPool = new ArrayList<Research>();
		for (Research research : workingList) {
			if (!research.purchased) {
				Research requirement = new Research();
				requirement.setID(research.Required);
				if (workingList.contains(requirement)) requirement = workingList.get(workingList.indexOf(requirement));
				if (requirement.purchased) {
					for (int i=0; i<research.weight; i++) {
						researchPool.add(research);
					}
				}
			}
		}
		researchPoolList[screenType] = new CivilisationClickerResearchPool(screenType);
		for (int i=0; i<researchOptions; i++) {
			if (researchPool.size() != 0) {
				Random rand = new Random();
				int index = rand.nextInt(researchPool.size() - 1);
				Research research = researchPool.get(index);
				researchPoolList[screenType].createResearchOption(research);
				researchPoolList[screenType].addResearchListener(this, i);
				while(researchPool.remove(research)) { }
			}
		}
		updateResearchGraphics();
	}
	void timerTick() {
		
	}
	void updateResearchGraphics() {
		mainPanel.removeAll();
		for (int i=0; i<DataBase.screenTypes.size(); i++) {
			if (researchPoolList[i] != null) mainPanel.add(researchPoolList[i].mainPanel);
		}
		mainPanel.repaint();
	}
	void createResourceScreen() {
		mainPanel = new PaintedPanel();
		mainPanel.bgImage = new ImageIcon("graphics/ui/researchscreen/researchbackground.png");
		mainPanel.setOpaque(false);
		mainPanel.setLayout(null);
		researchPanelWidth = mainPanel.bgImage.getIconWidth();
		researchPanelHeight = mainPanel.bgImage.getIconHeight();
	}
	@Override
	public void researchPurchased(int screenType, int researchOption, int x, int y) {
		CivilisationMainClass.resourceBar.removeMouseOverPanel();
		CivilisationMainClass.resourceBar.resetCostLabel();
		findNextResearch(screenType);
		if (researchPoolList[screenType].optionCounter -1 >= researchOption) {
			researchPoolList[screenType].researchList.get(researchOption).setPosition(x, y);
			researchPoolList[screenType].researchList.get(researchOption).showMouseOverPanel();
			researchPoolList[screenType].researchList.get(researchOption).showCost();
		}
	}
}
class CivilisationClickerResearchPool{
	static final int startingx = 102;
	static final int startingy = 27;
	static final int xoffset = 214;
	static final int yoffset = 161;
	static final int width = 129;
	static final int height = 127;
	static final int optiongap = 2;
	int x;
	int y;
	int screenType;
	int optionCounter;
	JPanel mainPanel;
	List<CivilisationClickerResearchOption> researchList = new ArrayList<CivilisationClickerResearchOption>();
	CivilisationClickerResearchPool(int screenType) {
		this.screenType = screenType;
		x = startingx;
		y = startingy;
		for (int i=0; i<screenType; i++) {
			if ((i & 1) == 0) x += xoffset;
			else {
				y += yoffset;
				x -= xoffset;
			}
		}
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBounds(x, y, width, height);
		mainPanel.setOpaque(false);
	}
	void createResearchOption(Research researchOption) {
		researchList.add(new CivilisationClickerResearchOption(researchOption, screenType, optionCounter));
		optionCounter++;
		updateGraphics();
	}
	void addResearchListener(ResearchListener listener, int option) {
		researchList.get(option).addListener(listener);
	}
	void updateGraphics() {
		mainPanel.removeAll();
		for (CivilisationClickerResearchOption option : researchList) {
			mainPanel.add(option.mainPanel);
			mainPanel.add(Box.createRigidArea(new Dimension(0, optiongap)));
		}
		mainPanel.add(Box.createVerticalGlue());
	}
}
class CivilisationClickerResearchOption implements MouseListener, MouseMotionListener{
	static final int WIDTH = 127;
	static final int HEIGHT = 41;
	int screenType;
	int option;
	int x;
	int y;
	JPanel descriptionPanel, namePanel, clickPanel;
	JLayeredPane mainPanel;
	JLabel nameLabel, costLabel;
	PaintedPanel iconPanel;
	Research researchOption;
	List<ResearchListener> listeners = new ArrayList<ResearchListener>();
	CivilisationClickerResearchOption(Research researchOption, int screenType, int option) {
		this.researchOption = researchOption;
		this.screenType = screenType;
		this.option = option;
		mainPanel = new JLayeredPane();
		mainPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setOpaque(false);
		clickPanel = new JPanel();
		clickPanel.setBounds(0, 0, WIDTH, HEIGHT);
		clickPanel.setOpaque(false);
		clickPanel.addMouseListener(this);
		clickPanel.addMouseMotionListener(this);
		iconPanel = new PaintedPanel();
		iconPanel.bgImage = new ImageIcon(researchOption.Icon);
		iconPanel.setBounds(0, 0, 42, 41);
		nameLabel = new JLabel("<html><div style='text-align: center;'>" + researchOption.Name + "</div></html>");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Serif", Font.PLAIN, 12));
		nameLabel.setBounds(44, 0, 85, 20);
		costLabel = new JLabel("<html><div style='text-align: center;'>" + researchOption.cost + "</div></html>");
		costLabel.setForeground(Color.WHITE);
		costLabel.setFont(new Font("Serif", Font.PLAIN, 12));
		costLabel.setBounds(44, 20, 85, 21);
		mainPanel.add(iconPanel, Integer.valueOf(1));
		mainPanel.add(nameLabel, Integer.valueOf(1));
		mainPanel.add(costLabel, Integer.valueOf(1));
		mainPanel.add(clickPanel, Integer.valueOf(2));
	}
	String createDescription() {
		String buildingname = researchOption.Building;
		String description = "";
		for (Building building : DataBase.buildingList.get(screenType).buildingList) {
			if (building.ID.equals(researchOption.Building)) {
				buildingname = building.Name;
				break;
			}
		}
		switch (researchOption.Effect) {
		case "enable":
			description = "Enables the construction of " + buildingname + "s throughtout your empire.";
			break;
		case "boost":
			description = "Boosts the production power of " + buildingname + "s by " + researchOption.value + "%.";
			break;
		default:
			description = "Who knows?";
			break;
		}
		return description;
	}
	void addListener(ResearchListener listener) {
		listeners.add(listener);
	}
	void showMouseOverPanel() {
		CivilisationMainClass.resourceBar.showMouseOverPanel(x, y, createDescription());
	}
	void showCost() {
		int a = Defines.RESEARCHPOINTPOOL;
		CivilisationMainClass.resourceBar.updateCostLabel(a - 1, researchOption.cost);
	}
	void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		int a = Defines.RESEARCHPOINTPOOL;
		x = e.getX() + CivilisationMainClass.resourceBar.researchScreen.researchPoolList[screenType].x;
		y = e.getY() + CivilisationMainClass.resourceBar.researchScreen.researchPoolList[screenType].y
				+ ((HEIGHT + CivilisationClickerResearchPool.optiongap) * option) + ResourceBar.RESOURCEBARSIZE;
		CivilisationMainClass.resourceBar.updateCostLabel(a - 1, researchOption.cost);
		CivilisationMainClass.resourceBar.moveMouseOverPanel(x, y);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		CivilisationMainClass.soundEngine.playClickSound();
		Country country = CivilisationMainClass.getPlayer();
		if (country.buyResearch(researchOption, screenType))
			for (ResearchListener listener : listeners) listener.researchPurchased(screenType, option, x, y);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		x = e.getX() + CivilisationMainClass.resourceBar.researchScreen.researchPoolList[screenType].x;
		y = e.getY() + CivilisationMainClass.resourceBar.researchScreen.researchPoolList[screenType].y
				+ ((HEIGHT + CivilisationClickerResearchPool.optiongap) * option) + ResourceBar.RESOURCEBARSIZE;
		showCost();
		showMouseOverPanel();
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		CivilisationMainClass.resourceBar.resetCostLabel();
		CivilisationMainClass.resourceBar.removeMouseOverPanel();
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
interface ResearchListener{
	void researchPurchased(int screenType, int researchOption, int x, int y);
}
