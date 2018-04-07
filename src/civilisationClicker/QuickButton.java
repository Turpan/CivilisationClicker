package civilisationClicker;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

import paintedPanel.PaintedPanel;

public class QuickButton implements MouseListener{
	static final Rectangle PANELBOUNDS = new Rectangle(0, 0, 281, 237);
	static final Rectangle BUTTONBOUNDS = new Rectangle(23, 0, 234, 234);
	static final Dimension[] BUTTONPOSITIONS = {
		new Dimension(11, 199),
		new Dimension(39, 199),
		new Dimension(216, 199),
		new Dimension(244, 199)
	};
	int selectedScreen;
	int selectedProvince;
	JLayeredPane mainPanel;
	PaintedPanel backgroundPanel;
	PaintedPanel buttonPanel;
	PaintedPanel[] screenPanels;
	ImageIcon[] buttonImages;
	ImageIcon[] buttonPressedImages;
	ImageIcon[] screenImages;
	ImageIcon[] screenPressedImages;
	List<QuickButtonListener> listeners = new ArrayList<QuickButtonListener>();
	QuickButton() {
		initialiseArrays();
		createGraphics();
		for (int i=0; i<BUTTONPOSITIONS.length; i++) {
			System.out.println(BUTTONPOSITIONS[i]);
		}
	}
	void createGraphics() {
		Rectangle mainPanelBounds = new Rectangle(CivilisationMainClass.gameWidth - PANELBOUNDS.width,
				CivilisationMainClass.gameHeight - PANELBOUNDS.height,
				PANELBOUNDS.width, PANELBOUNDS.height);
		mainPanel = new JLayeredPane();
		mainPanel.setOpaque(false);
		mainPanel.setBounds(mainPanelBounds);
		backgroundPanel = new PaintedPanel();
		backgroundPanel.bgImage = new ImageIcon("graphics/ui/mapui-bottom/buttonui.png");
		backgroundPanel.setBounds(PANELBOUNDS);
		backgroundPanel.setOpaque(false);
		buttonPanel = new PaintedPanel();
		buttonPanel.setBounds(BUTTONBOUNDS);
		buttonPanel.setOpaque(false);
		buttonPanel.addMouseListener(this);
		for (int i=0; i<screenPanels.length; i++) {
			String screentype = DataBase.screenTypes.get(i);
			String directory = "graphics/ui/mapui-bottom/";
			buttonImages[i] = new ImageIcon(directory + screentype + ".png");
			buttonPressedImages[i] = new ImageIcon(directory + screentype + "-pressed.png");
			screenImages[i] = new ImageIcon(directory + "button" + screentype + ".png");
			screenPressedImages[i] = new ImageIcon(directory + "button" + screentype + "-pressed.png");
			Dimension screenButtonSize = new Dimension(screenImages[i].getIconWidth(), screenImages[i].getIconHeight());
			screenPanels[i] = new PaintedPanel();
			screenPanels[i].bgImage = screenImages[i];
			screenPanels[i].setBounds(BUTTONPOSITIONS[i].width, BUTTONPOSITIONS[i].height, screenButtonSize.width, screenButtonSize.height);
			screenPanels[i].setOpaque(false);
			screenPanels[i].addMouseListener(this);
			mainPanel.add(screenPanels[i], Integer.valueOf(2));
		}
		buttonPanel.bgImage = buttonImages[0];
		mainPanel.add(backgroundPanel, Integer.valueOf(1));
		mainPanel.add(buttonPanel, Integer.valueOf(2));
	}
	void initialiseArrays() {
		int a = DataBase.screenTypes.size();
		screenPanels = new PaintedPanel[a];
		buttonImages = new ImageIcon[a];
		buttonPressedImages = new ImageIcon[a];
		screenImages = new ImageIcon[a];
		screenPressedImages = new ImageIcon[a];
	}
	void addListener(QuickButtonListener listener) {
		listeners.add(listener);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == buttonPanel) {
			double points = MapScreen.getProvince(selectedProvince).developementList.get(selectedScreen).pointsPerClick;
			CivilisationMainClass.resourceBar.updateRefundLabel(selectedScreen, points);
		}
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		CivilisationMainClass.resourceBar.resetCostLabel();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == buttonPanel) {
			MapScreen.provinceList.get(selectedProvince).developementList.get(selectedScreen).clickButton();
			CivilisationMainClass.resourceBar.updateLabels();
			CivilisationMainClass.clickerMaster.provinceScreens[selectedScreen][selectedProvince].playClickSound();
			buttonPanel.bgImage = buttonPressedImages[selectedScreen];
		} else {
			for (int i=0; i<screenPanels.length; i++) if (e.getSource() == screenPanels[i]){
				selectedScreen = i;
				buttonPanel.bgImage = buttonImages[i];
				screenPanels[i].bgImage = screenPressedImages[i];
				SoundEngine.playPointClickSound(i);
				if (e.getClickCount() >= 2) CivilisationMainClass.changeTabs(selectedScreen + 1, selectedProvince);
				for(QuickButtonListener listener : listeners) listener.screenChanged(i);
				break;
			}
		}
		mainPanel.repaint();
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		buttonPanel.bgImage = buttonImages[selectedScreen];
		for (int i=0; i<screenPanels.length; i++) screenPanels[i].bgImage = screenImages[i];
		mainPanel.repaint();
	}
}
interface QuickButtonListener {
	void screenChanged(int screen);
}
