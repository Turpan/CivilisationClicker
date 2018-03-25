package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import paintedPanel.PaintedPanel;

public class ResourceBar implements MouseListener, MouseMotionListener, OrderListener, ActionListener{
	static final int RESOURCEBARSIZE = 32;
	boolean showMouseOver;
	int mouseOverCount;
	Point topPanelInitialPoint;
	JPanel resourceBarPanel;
	JPanel[] resourceLabelPanel, resourceCostLabelPanel;
	JLabel timerLabel, mouseoverLabel;
	JLabel[] resourceLabel, resourceCostLabel;
	JLayeredPane mainPanel;
	PaintedPanel topPanel, bottomPanel, researchButton, militaryButton, governmentButton, timerPanel, mouseoverPanel;
	PaintedPanel speed0Button, speed1Button, speed2Button, speed3Button;
	PaintedPanel[] resourcePanel;
	ImageIcon mouseoverImage, speed0Image, speed0PressedImage, speed1Image, speed1PressedImage, speed2Image, speed2PressedImage, speed3Image, speed3PressedImage;
	ImageIcon militaryImage, militaryImageHighlight, militaryImagePressed;
	ImageIcon researchImage, researchImageHighlight, researchImagePressed;
	ImageIcon governmentImage, governmentImageHighlight, governmentImagePressed;
	ResearchScreen researchScreen;
	EdictScreen edictScreen;
	ArmyScreen armyScreen;
	List<OrderTroopsScreen> orderTroopsScreens = new ArrayList<OrderTroopsScreen>();
	ResourceBar() {
		mouseoverImage = new ImageIcon("graphics/ui/resourcebar/descriptionsquare.png");
		mouseoverPanel = new PaintedPanel();
		mouseoverPanel.bgImage = mouseoverImage;
		mouseoverPanel.setLayout(new GridBagLayout());
		mouseoverPanel.setOpaque(false);
		mouseoverLabel = new JLabel();
		mouseoverLabel.setForeground(Color.WHITE);
		mouseoverPanel.add(mouseoverLabel);
		mainPanel = new JLayeredPane();
		mainPanel.setBounds(0, 0, CivilisationMainClass.gameWidth, CivilisationMainClass.gameHeight);
		mainPanel.setOpaque(false);
		createResourceBar();
		researchScreen = new ResearchScreen();
		researchScreen.mainPanel.setBounds(0, 32, 473, 360);
		EdictScreen.createProvinceIcons();
		edictScreen = new EdictScreen();
		edictScreen.mainPanel.setBounds(0, 32, 473, 632);
		armyScreen = new ArmyScreen();
		armyScreen.mainPanel.setBounds(ArmyScreen.ARMYPANELSIZE);
		mainPanel.add(resourceBarPanel, Integer.valueOf(2));
	}
	void removeMenuScreens() {
		if (edictScreen.mainPanel.getParent() == mainPanel) mainPanel.remove(edictScreen.mainPanel);
		if (researchScreen.mainPanel.getParent() == mainPanel) mainPanel.remove(researchScreen.mainPanel);
		if (armyScreen.mainPanel.getParent() == mainPanel) mainPanel.remove(armyScreen.mainPanel);
		if (mouseoverPanel.getParent() == mainPanel) mainPanel.remove(mouseoverPanel);
		armyScreen.resetScreen();
		governmentButton.bgImage = governmentImage;
		militaryButton.bgImage = militaryImage;
		researchButton.bgImage = researchImage;
	}
	void updateLabels() {
		Country country = CivilisationMainClass.getPlayer();
		for (int i=0; i<DataBase.screenTypes.size(); i++)
			resourceLabel[i].setText(MathFunctions.withSuffix(country.points[i]));
	}
	void updateCostLabel(double cost) {
		for (int i=0; i<resourceCostLabel.length; i++) {
			resourceCostLabel[i].setVisible(true);
			resourceCostLabel[i].setForeground(Color.RED);
			resourceCostLabel[i].setText("-" + MathFunctions.withSuffix(cost));
		}
	}
	void updateCostLabel(int label, double cost) {
		resourceCostLabel[label].setVisible(true);
		resourceCostLabel[label].setForeground(Color.RED);
		resourceCostLabel[label].setText("-" + MathFunctions.withSuffix(cost));
	}
	void updateRefundLabel(int label, double cost) {
		resourceCostLabel[label].setVisible(true);
		resourceCostLabel[label].setForeground(Color.GREEN);
		resourceCostLabel[label].setText("+" + MathFunctions.withSuffix(cost));
	}
	void resetCostLabel() {
		for (int i=0; i<resourceCostLabel.length; i++) {
			resourceCostLabel[i].setVisible(false);
		}
	}
	void provinceInvaded(int province) {
		OrderTroopsScreen troopsScreen = new OrderTroopsScreen(CivilisationMainClass.playerID, province, true);
		troopsScreen.addListener(this);
		orderTroopsScreens.add(troopsScreen);
		mainPanel.add(troopsScreen, Integer.valueOf(1));
	}
	void timerTick() {
		int hours = CivilisationMainClass.timeCount / 3600;
		int minutes = (CivilisationMainClass.timeCount % 3600) / 60;
		int seconds = (CivilisationMainClass.timeCount % 3600) % 60;
		String timerText = "";
		timerText += (hours < 10) ? ("0" + hours) : hours;
		timerText += ":";
		timerText += (minutes < 10) ? ("0" + minutes) : minutes;
		timerText += ":";
		timerText += (seconds < 10) ? ("0" + seconds) : seconds;
		timerLabel.setText(timerText);
		researchScreen.timerTick();
		edictScreen.timerTick();
		armyScreen.timerTick();
	}
	void screenDragged(int x, int y) {
		int windowX = CivilisationMainClass.frame.getLocation().x;
		int windowY = CivilisationMainClass.frame.getLocation().y;
		int xMove = (windowX + x) - (windowX + topPanelInitialPoint.x);
        int yMove = (windowY + y) - (windowY + topPanelInitialPoint.y);
        windowX += xMove;
        windowY += yMove;
		if (windowX < 0) windowX = 0;
		if (windowY < 0) windowY = 0;
		CivilisationMainClass.frame.setLocation(windowX, windowY);
	}
	void showMouseOverPanel(int x, int y, String text) {
		removeMouseOverPanel();
		showMouseOver = true;
		mouseOverCount = 0;
		mouseoverPanel.setAlpha(mouseOverCount);
		int width = mouseoverImage.getIconWidth();
		int height = mouseoverImage.getIconHeight();
		mouseoverPanel.setBounds(x, y, width, height);
		mouseoverLabel.setText("<html>" + text + "</html>");
		mainPanel.add(mouseoverPanel, Integer.valueOf(4));
	}
	void moveMouseOverPanel(int x, int y) {
		int width = mouseoverImage.getIconWidth();
		int height = mouseoverImage.getIconHeight();
		mouseoverPanel.setBounds(x, y, width, height);
	}
	void removeMouseOverPanel() {
		showMouseOver = false;
		mouseOverCount = 0;
		if (mouseoverPanel.getParent() == mainPanel) mainPanel.remove(mouseoverPanel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	void updateTimerButtons() {
		speed0Button.bgImage = speed0Image;
		speed1Button.bgImage = speed1Image;
		speed2Button.bgImage = speed2Image;
		speed3Button.bgImage = speed3Image;
		switch (CivilisationMainClass.timerStatus) {
		case 0:
			speed0Button.bgImage = speed0PressedImage;
			break;
		case 1:
			speed1Button.bgImage = speed1PressedImage;
			break;
		case 2:
			speed2Button.bgImage = speed2PressedImage;
			break;
		case 3:
			speed3Button.bgImage = speed3PressedImage;
			break;
		}
		speed0Button.repaint();
		speed1Button.repaint();
		speed2Button.repaint();
		speed3Button.repaint();
	}
	void createResourceBar() {
		resourceBarPanel = new JPanel();
		resourceBarPanel.setLayout(new BoxLayout(resourceBarPanel, BoxLayout.PAGE_AXIS));
		resourceBarPanel.setBounds(0, 0, CivilisationMainClass.gameWidth, 45);
		resourceBarPanel.setOpaque(false);
		createTopBar();
		createBottomBar();
		resourceBarPanel.add(topPanel);
		resourceBarPanel.add(bottomPanel);
	}
	void createBottomBar() {
		bottomPanel = new PaintedPanel(PaintedPanel.PAINTHORIZONTAL);
		bottomPanel.bgImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-bottom.png");
		bottomPanel.setPaintBufferX(CivilisationMainClass.gameWidth - 1600);
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.setMinimumSize(new Dimension(CivilisationMainClass.gameWidth, 13));
		bottomPanel.setPreferredSize(new Dimension(CivilisationMainClass.gameWidth, 13));
		bottomPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth, 13));
		speed0Button = new PaintedPanel(PaintedPanel.PAINTNONE);
		speed0Button.addMouseListener(this);
		speed0Image = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer-pause.png");
		speed0PressedImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer-pause-pressed.png");
		speed0Button.bgImage = speed0Image;
		speed0Button.setPaintBuffer(1, 2);
		speed0Button.setOpaque(false);
		speed0Button.setMinimumSize(new Dimension(10, 13));
		speed0Button.setPreferredSize(new Dimension(10, 13));
		speed0Button.setMaximumSize(new Dimension(10, 13));
		speed1Button = new PaintedPanel(PaintedPanel.PAINTNONE);
		if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT) speed1Button.addMouseListener(this);
		speed1Image = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer-speed1.png");
		speed1PressedImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer-speed1-pressed.png");
		speed1Button.bgImage = speed1Image;
		speed1Button.setPaintBuffer(1, 2);
		speed1Button.setOpaque(false);
		speed1Button.setMinimumSize(new Dimension(12, 13));
		speed1Button.setPreferredSize(new Dimension(12, 13));
		speed1Button.setMaximumSize(new Dimension(12, 13));
		speed2Button = new PaintedPanel(PaintedPanel.PAINTNONE);
		if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT) speed2Button.addMouseListener(this);
		speed2Image = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer-speed2.png");
		speed2PressedImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer-speed2-pressed.png");
		speed2Button.bgImage = speed2Image;
		speed2Button.setPaintBuffer(1, 2);
		speed2Button.setOpaque(false);
		speed2Button.setMinimumSize(new Dimension(13, 13));
		speed2Button.setPreferredSize(new Dimension(13, 13));
		speed2Button.setMaximumSize(new Dimension(13, 13));
		speed3Button = new PaintedPanel(PaintedPanel.PAINTNONE);
		if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT) speed3Button.addMouseListener(this);
		speed3Image = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer-speed3.png");
		speed3PressedImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer-speed3-pressed.png");
		speed3Button.bgImage = speed3Image;
		speed3Button.setPaintBuffer(1, 2);
		speed3Button.setOpaque(false);
		speed3Button.setMinimumSize(new Dimension(16, 13));
		speed3Button.setPreferredSize(new Dimension(16, 13));
		speed3Button.setMaximumSize(new Dimension(16, 13));
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(speed0Button);
		bottomPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		bottomPanel.add(speed1Button);
		bottomPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		bottomPanel.add(speed2Button);
		bottomPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		bottomPanel.add(speed3Button);
		bottomPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		updateTimerButtons();
	}
	void createButtonImages() {
		militaryImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-army.png");
		militaryImageHighlight = new ImageIcon("graphics/ui/resourcebar/resourcebar-army-highlighted.png");
		militaryImagePressed = new ImageIcon("graphics/ui/resourcebar/resourcebar-army-pressed.png");
		researchImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-research.png");
		researchImageHighlight = new ImageIcon("graphics/ui/resourcebar/resourcebar-research-highlighted.png");
		researchImagePressed = new ImageIcon("graphics/ui/resourcebar/resourcebar-research-pressed.png");
		governmentImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-government.png");
		governmentImageHighlight = new ImageIcon("graphics/ui/resourcebar/resourcebar-government-highlighted.png");
		governmentImagePressed = new ImageIcon("graphics/ui/resourcebar/resourcebar-government-pressed.png");
	}
	void createTopBar() {
		createButtonImages();
		topPanel = new PaintedPanel(PaintedPanel.PAINTHORIZONTAL);
		topPanel.bgImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-top.png");
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		topPanel.setMinimumSize(new Dimension(CivilisationMainClass.gameWidth, 32));
		topPanel.setPreferredSize(new Dimension(CivilisationMainClass.gameWidth, 32));
		topPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth, 32));
		topPanel.addMouseListener(this);
		topPanel.addMouseMotionListener(this);
		researchButton = new PaintedPanel(PaintedPanel.PAINTNONE);
		researchButton.bgImage = researchImage;
		researchButton.setPaintBuffer(1, 1);
		researchButton.setOpaque(false);
		researchButton.setMinimumSize(new Dimension(62, 32));
		researchButton.setPreferredSize(new Dimension(62, 32));
		researchButton.setMaximumSize(new Dimension(62, 32));
		researchButton.addMouseListener(this);
		governmentButton = new PaintedPanel(PaintedPanel.PAINTNONE);
		governmentButton.bgImage = governmentImage;
		governmentButton.setPaintBuffer(1, 1);
		governmentButton.setOpaque(false);
		governmentButton.setMinimumSize(new Dimension(62, 32));
		governmentButton.setPreferredSize(new Dimension(62, 32));
		governmentButton.setMaximumSize(new Dimension(62, 32));
		governmentButton.addMouseListener(this);
		militaryButton = new PaintedPanel(PaintedPanel.PAINTNONE);
		militaryButton.bgImage = militaryImage;
		militaryButton.setPaintBuffer(1, 1);
		militaryButton.setOpaque(false);
		militaryButton.setMinimumSize(new Dimension(62, 32));
		militaryButton.setPreferredSize(new Dimension(62, 32));
		militaryButton.setMaximumSize(new Dimension(62, 32));
		militaryButton.addMouseListener(this);
		timerPanel = new PaintedPanel(PaintedPanel.PAINTNONE);
		timerPanel.bgImage = new ImageIcon("graphics/ui/resourcebar/resourcebar-timer.png");
		timerPanel.setPaintBuffer(2, 7);
		timerPanel.setLayout(new GridBagLayout());
		timerPanel.setOpaque(false);
		timerPanel.setMinimumSize(new Dimension(66, 32));
		timerPanel.setPreferredSize(new Dimension(66, 32));
		timerPanel.setMaximumSize(new Dimension(66, 32));
		timerLabel = new JLabel("00:00:00");
		timerLabel.setForeground(Color.WHITE);
		timerPanel.add(timerLabel);
		topPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		topPanel.add(researchButton);
		topPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		topPanel.add(governmentButton);
		topPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		topPanel.add(militaryButton);
		createResourceLabels();
		topPanel.add(Box.createHorizontalGlue());
		topPanel.add(timerPanel);
	}
	void createResourceLabels() {
		initialiseArrays(DataBase.screenTypes.size());
		int i = 0;
		for (String screenType : DataBase.screenTypes) {
			resourcePanel[i] = new PaintedPanel(PaintedPanel.PAINTNONE);
			resourcePanel[i].bgImage = new ImageIcon("graphics/ui/resourcebar/resource-" + screenType + ".png");
			resourcePanel[i].setPaintBuffer(2, 7);
			resourcePanel[i].setOpaque(false);
			resourcePanel[i].setLayout(new BoxLayout(resourcePanel[i], BoxLayout.LINE_AXIS));
			resourcePanel[i].setPreferredSize(new Dimension(148, 32));
			resourcePanel[i].setMaximumSize(new Dimension(148, 32));
			resourceLabelPanel[i] = new JPanel(new GridBagLayout());
			resourceLabelPanel[i].setOpaque(false);
			resourceCostLabelPanel[i] = new JPanel(new GridBagLayout());
			resourceCostLabelPanel[i].setOpaque(false);
			resourceLabel[i] = new JLabel("0");
			resourceLabel[i].setForeground(Color.WHITE);
			resourceCostLabel[i] = new JLabel("-0");
			resourceCostLabel[i].setForeground(Color.RED);
			resourceCostLabel[i].setVisible(false);
			resourceLabelPanel[i].add(resourceLabel[i]);
			resourceCostLabelPanel[i].add(resourceCostLabel[i]);
			resourcePanel[i].add(Box.createRigidArea(new Dimension(24, 0)));
			resourcePanel[i].add(resourceLabelPanel[i]);
			resourcePanel[i].add(Box.createHorizontalGlue());
			resourcePanel[i].add(resourceCostLabelPanel[i]);
			topPanel.add(resourcePanel[i]);
			topPanel.add(Box.createRigidArea(new Dimension(2, 0)));
			i++;
		}
	}
	void initialiseArrays(int a) {
		resourcePanel = new PaintedPanel[a];
		resourceLabel = new JLabel[a];
		resourceCostLabel = new JLabel[a];
		resourceLabelPanel = new JPanel[a];
		resourceCostLabelPanel = new JPanel[a];
	}
	void openEdictScreen() {
		if (edictScreen.mainPanel.getParent() == mainPanel) {
			mainPanel.remove(edictScreen.mainPanel);
			governmentButton.bgImage = governmentImage;
		}
		else {
			removeMenuScreens();
			mainPanel.add(edictScreen.mainPanel, Integer.valueOf(1));
			governmentButton.bgImage = governmentImagePressed;
		}
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == researchButton) {
			researchButton.bgImage = researchImageHighlight;
			researchButton.repaint();
		} else if (e.getSource() == governmentButton) {
			governmentButton.bgImage = governmentImageHighlight;
			governmentButton.repaint();
		} else if (e.getSource() == militaryButton) {
			militaryButton.bgImage = militaryImageHighlight;
			militaryButton.repaint();
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == researchButton) {
			if (researchScreen.mainPanel.getParent() == mainPanel) researchButton.bgImage = researchImagePressed;
			else researchButton.bgImage = researchImage;
			researchButton.repaint();
		} else if (e.getSource() == governmentButton) {
			if (edictScreen.mainPanel.getParent() == mainPanel) governmentButton.bgImage = governmentImagePressed;
			else governmentButton.bgImage = governmentImage;
			governmentButton.repaint();
		} else if (e.getSource() == militaryButton) {
			if (armyScreen.mainPanel.getParent() == mainPanel) militaryButton.bgImage = militaryImagePressed;
			else militaryButton.bgImage = militaryImage;
			militaryButton.repaint();
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		CivilisationMainClass.soundEngine.playClickSound();
		if (e.getSource() == speed0Button) {
			CivilisationMainClass.changeGameSpeed(0);
		} else if (e.getSource() == speed1Button) {
			CivilisationMainClass.changeGameSpeed(1);
		} else if (e.getSource() == speed2Button) {
			CivilisationMainClass.changeGameSpeed(2);
		} else if (e.getSource() == speed3Button) {
			CivilisationMainClass.changeGameSpeed(3);
		} else if (e.getSource() == researchButton) {
			if (researchScreen.mainPanel.getParent() == mainPanel) {
				mainPanel.remove(researchScreen.mainPanel);
				researchButton.bgImage = researchImage;
			}
			else {
				removeMenuScreens();
				mainPanel.add(researchScreen.mainPanel, Integer.valueOf(1));
				researchButton.bgImage = researchImagePressed;
			}
			mainPanel.revalidate();
			mainPanel.repaint();
		} else if (e.getSource() == governmentButton) {
			openEdictScreen();
		} else if (e.getSource() == militaryButton) {
			if (armyScreen.mainPanel.getParent() == mainPanel) {
				mainPanel.remove(armyScreen.mainPanel);
				militaryButton.bgImage = militaryImage;
			}
			else {
				removeMenuScreens();
				mainPanel.add(armyScreen.mainPanel, Integer.valueOf(1));
				militaryButton.bgImage = militaryImagePressed;
			}
			mainPanel.revalidate();
			mainPanel.repaint();
		} else if (e.getSource() == topPanel) {
			topPanelInitialPoint = e.getPoint();
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (topPanelInitialPoint != null) {
			topPanelInitialPoint = null;
		}
	}
	@Override
	public void removeOrderScreen(OrderTroopsScreen screen) {
		orderTroopsScreens.remove(screen);
		mainPanel.remove(screen);
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if (topPanelInitialPoint != null) {
			screenDragged(e.getX(), e.getY());
		}
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (showMouseOver && mouseOverCount < 2000) mouseOverCount += 30;
		if (showMouseOver) mouseoverPanel.setAlpha((((float) mouseOverCount - 1000) / 1000));
	}
}
