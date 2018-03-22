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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import paintedPanel.PaintedPanel;

public class CivilisationClickerResearchScreen implements ResearchListener{
	static final int researchOptions = 3;
	int researchPanelWidth, researchPanelHeight;
	PaintedPanel mainPanel;
	CivilisationClickerResearchPool[] researchPoolList;
	CivilisationClickerResearchScreen() {
		researchPoolList = new CivilisationClickerResearchPool[CivilisationClickerDataBase.screenTypes.size()];
		createResourceScreen();
		for (int i=0; i<CivilisationClickerDataBase.screenTypes.size(); i++) {
			findNextResearch(i);
		}
	}
	void findNextResearch(int screenType) {
		List<CivilisationClickerResearch> workingList = new ArrayList<CivilisationClickerResearch>();
		for (CivilisationClickerResearch research : 
			CivilisationMainClass.getPlayer().researchList.get(screenType).researchList) {
			workingList.add(research);
		}
		List<CivilisationClickerResearch> researchPool = new ArrayList<CivilisationClickerResearch>();
		for (CivilisationClickerResearch research : workingList) {
			if (!research.purchased) {
				CivilisationClickerResearch requirement = new CivilisationClickerResearch();
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
				CivilisationClickerResearch research = researchPool.get(index);
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
		for (int i=0; i<CivilisationClickerDataBase.screenTypes.size(); i++) {
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
	public void researchPurchased(int screenType, int researchOption) {
		findNextResearch(screenType);
	}
}
class CivilisationClickerResearchPool{
	static final int xoffset = 214;
	static final int yoffset = 161;
	static final int width = 127;
	static final int height = 127;
	int x;
	int y;
	int screenType;
	int optionCounter;
	JPanel mainPanel;
	List<CivilisationClickerResearchOption> researchList = new ArrayList<CivilisationClickerResearchOption>();
	CivilisationClickerResearchPool(int screenType) {
		this.screenType = screenType;
		int x = 114;
		int y = 27;
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
	void createResearchOption(CivilisationClickerResearch researchOption) {
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
		}
	}
}
class CivilisationClickerResearchOption implements MouseListener, MouseMotionListener{
	static final int WIDTH = 127;
	static final int HEIGHT = 43;
	int screenType;
	int option;
	JPanel descriptionPanel, namePanel, clickPanel;
	JLayeredPane mainPanel;
	JLabel nameLabel, costLabel;
	PaintedPanel iconPanel;
	CivilisationClickerResearch researchOption;
	List<ResearchListener> listeners = new ArrayList<ResearchListener>();
	CivilisationClickerResearchOption(CivilisationClickerResearch researchOption, int screenType, int option) {
		this.researchOption = researchOption;
		this.screenType = screenType;
		this.option = option;
		mainPanel = new JLayeredPane();
		mainPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setOpaque(false);
		clickPanel = new JPanel();
		clickPanel.setBounds(0, 0, WIDTH, 43);
		clickPanel.setOpaque(false);
		clickPanel.addMouseListener(this);
		clickPanel.addMouseMotionListener(this);
		iconPanel = new PaintedPanel();
		iconPanel.bgImage = new ImageIcon(researchOption.Icon);
		iconPanel.setBounds(0, 2, 42, 41);
		nameLabel = new JLabel("<html><div style='text-align: center;'>" + researchOption.Name + "</div></html>");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Serif", Font.PLAIN, 12));
		nameLabel.setBounds(44, 2, 85, 20);
		costLabel = new JLabel("<html><div style='text-align: center;'>" + researchOption.cost + "</div></html>");
		costLabel.setForeground(Color.WHITE);
		costLabel.setFont(new Font("Serif", Font.PLAIN, 12));
		costLabel.setBounds(44, 22, 85, 21);
		mainPanel.add(iconPanel, Integer.valueOf(1));
		mainPanel.add(nameLabel, Integer.valueOf(1));
		mainPanel.add(costLabel, Integer.valueOf(1));
		mainPanel.add(clickPanel, Integer.valueOf(2));
	}
	String createDescription() {
		String buildingname = researchOption.Building;
		String description = "";
		for (CivilisationClickerBuilding building : CivilisationClickerDataBase.buildingList.get(screenType).buildingList) {
			if (building.ID == researchOption.Building) {
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
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		CivilisationMainClass.soundEngine.playClickSound();
		CivilisationClickerCountry country = CivilisationMainClass.playerList.get(CivilisationMainClass.playerID);
		country.buyResearch(researchOption, screenType);
		for (ResearchListener listener : listeners) listener.researchPurchased(screenType, option);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		int a = CivilisationClickerSuperScreen.researchPointPool;
		int x = e.getX() + CivilisationMainClass.resourceBar.researchScreen.researchPoolList[screenType].x;
		int y = e.getY() + CivilisationMainClass.resourceBar.researchScreen.researchPoolList[screenType].y + (HEIGHT * option);
		CivilisationMainClass.resourceBar.updateCostLabel(a - 1, researchOption.cost);
		CivilisationMainClass.resourceBar.showMouseOverPanel(x, y, createDescription());
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
	void researchPurchased(int screenType, int researchOption);
}
