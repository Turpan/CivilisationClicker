package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import paintedPanel.PaintedPanel;
import scrollBar.ScrollBar;
import scrollBar.ScrollListener;

public class QuickBuy implements QuickBuySlotListener{
	static final Rectangle BUILDINGSLOTBOUNDS = new Rectangle(9, 75, 254, 114);
	static final Rectangle SCROLLBARBOUNDS= new Rectangle(263, 75, 4, 114);
	static Dimension panelSize;
	int screenType;
	ImageIcon[] backgroundImages;
	PaintedPanel mainPanel;
	List<QuickBuyMain> buildingQuickBuy = new ArrayList<QuickBuyMain>();
	QuickBuy() {
		loadImages();
		createGraphics();
	}
	void createGraphics() {
		panelSize = new Dimension(backgroundImages[0].getIconWidth(), backgroundImages[0].getIconHeight());
		mainPanel = new PaintedPanel();
		mainPanel.bgImage = backgroundImages[0];
		mainPanel.setMinimumSize(panelSize);
		mainPanel.setPreferredSize(panelSize);
		mainPanel.setMaximumSize(panelSize);
		mainPanel.setLayout(null);
		mainPanel.setOpaque(false);
		for (int i=0; i<DataBase.screenTypes.size(); i++) {
			QuickBuyMain quickBuy = new QuickBuyMain(i);
			buildingQuickBuy.add(quickBuy);
			for (QuickBuySlot quickBuySlot : quickBuy.buildingSlots) {
				quickBuySlot.addListener(this);
			}
		}
	}
	void changeScreenType(int screenType) {
		this.screenType = screenType;
		if (MapScreen.selectedProvince > -1) addPanel();
	}
	void addPanel() {
		QuickBuyMain quickBuy = buildingQuickBuy.get(screenType);
		mainPanel.bgImage = backgroundImages[screenType];
		mainPanel.removeAll();
		mainPanel.add(quickBuy.mainPanel);
		if (quickBuy.scroll) mainPanel.add(quickBuy.scrollBar);
		buildingQuickBuy.get(screenType).updateLabels();
		mainPanel.repaint();
	}
	void updateLabels() {
		if (MapScreen.selectedProvince > -1) {
			Province selectedProvince = MapScreen.getSelectedProvince();
			if (selectedProvince.owner == CivilisationMainClass.playerID && !selectedProvince.coloniseInProgress) {
				addPanel();
			} else {
				mainPanel.removeAll();
			}
		} else {
			mainPanel.removeAll();
		}
		mainPanel.repaint();
	}
	void loadImages() {
		QuickBuySlot.loadImages();
		backgroundImages = new ImageIcon[DataBase.screenTypes.size()];
		for (int i=0; i<backgroundImages.length; i++) {
			String screenType = DataBase.screenTypes.get(i);
			backgroundImages[i] = new ImageIcon("graphics/ui/mapui-bottom/quickbuy/quickbuy-" + screenType + ".png");
		}
	}
	@Override
	public void buildingBought(int screenType, int building, int amount) {
		SoundEngine.playClickSound();
		ProvinceDevelopement developement = MapScreen.getSelectedProvince().developementList.get(screenType);
		if (developement.buyBuilding(building, amount)) {
			buildingQuickBuy.get(screenType).buildingSlots.get(building).updateLabels();
			buildingQuickBuy.get(screenType).buildingSlots.get(building).showCost();
		}
	}
	@Override
	public void mouseWheelScrolled(int screenType, int pixels) {
		QuickBuyMain quickBuy = buildingQuickBuy.get(screenType);
		int viewPosition = quickBuy.mainPanel.getViewport().getViewPosition().y;
		viewPosition += pixels;
		if (viewPosition < 0) viewPosition = 0;
		quickBuy.mainPanel.getViewport().setViewPosition(new Point(0, viewPosition));
		viewPosition = quickBuy.mainPanel.getViewport().getViewPosition().y;
		quickBuy.scrollBar.setScrollBarPosition(viewPosition);
	}
}
class QuickBuyMain implements ScrollListener{
	int ID;
	JScrollPane mainPanel;
	JPanel slotPanel;
	List<QuickBuySlot> buildingSlots = new ArrayList<QuickBuySlot>();
	ScrollBar scrollBar;
	boolean scroll;
	QuickBuyMain(int ID) {
		this.ID = ID;
		createGraphics();
	}
	void createGraphics() {
		slotPanel = new JPanel();
		slotPanel.setOpaque(false);
		slotPanel.setLayout(new BoxLayout(slotPanel, BoxLayout.PAGE_AXIS));
		mainPanel = new JScrollPane(slotPanel);
		mainPanel.setBounds(QuickBuy.BUILDINGSLOTBOUNDS);
		mainPanel.setBorder(null);
		mainPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		int size = 0;
		for (int i=0; i<DataBase.buildingList.get(ID).buildingList.size(); i++) {
			QuickBuySlot slot = new QuickBuySlot(i, ID);
			buildingSlots.add(slot);
			slotPanel.add(slot.mainPanel);
			size += QuickBuySlot.slotBackground[ID].getIconHeight();
		}
		if (scroll = size > QuickBuy.BUILDINGSLOTBOUNDS.height) {
			String screenType = DataBase.screenTypes.get(ID);
			Dimension scrollBarSize = new Dimension(QuickBuy.SCROLLBARBOUNDS.width, QuickBuy.SCROLLBARBOUNDS.height);
			//String upImageLocation = "graphics/ui/mapui-bottom/quickbuy/scrollup-" + screenType;
			//String downImageLocation = "graphics/ui/mapui-bottom/quickbuy/scrolldown-" + screenType;
			String scrollImageLocation = "graphics/ui/mapui-bottom/quickbuy/scrollbar-" + screenType;
			scrollBar = new ScrollBar(scrollBarSize, "", "", scrollImageLocation);
			scrollBar.setBounds(QuickBuy.SCROLLBARBOUNDS);
			scrollBar.setOpaque(false);
			scrollBar.createScrollBar(size, QuickBuy.BUILDINGSLOTBOUNDS.height);
			scrollBar.scrollBar.setBorder(BorderFactory.createLineBorder(Color.WHITE));
			scrollBar.addScrollListener(this);
		}
	}
	void updateLabels() {
		for (QuickBuySlot quickBuy : buildingSlots) quickBuy.updateLabels();
	}
	@Override
	public void viewChanged(int newValue) {
		mainPanel.getViewport().setViewPosition(new Point(0, newValue));
	}
}
interface QuickBuySlotListener {
	void buildingBought(int screenType, int building, int amount);
	void mouseWheelScrolled(int screenType, int pixels);
}
class QuickBuySlot implements MouseListener, MouseWheelListener{
	static ImageIcon[] slotBackground;
	static Dimension panelSize;
	int screenType;
	int ID;
	PaintedPanel mainPanel;
	JLabel nameLabel;
	JLabel countLabel;
	JLabel costLabel;
	QuickBuySlotListener listener;
	QuickBuySlot(int ID, int screenType) {
		this.screenType = screenType;
		this.ID = ID;
		createGraphics();
	}
	void createGraphics() {
		Building building = DataBase.buildingList.get(screenType).buildingList.get(ID);
		mainPanel = new PaintedPanel();
		mainPanel.bgImage = slotBackground[screenType];
		mainPanel.setMinimumSize(panelSize);
		mainPanel.setPreferredSize(panelSize);
		mainPanel.setMaximumSize(panelSize);
		mainPanel.addMouseListener(this);
		mainPanel.addMouseWheelListener(this);
		nameLabel = new JLabel(building.Name);
		nameLabel.setForeground(Color.WHITE);
		countLabel = new JLabel("0");
		countLabel.setForeground(Color.WHITE);
		costLabel = new JLabel(building.Cost + "");
		costLabel.setForeground(Color.WHITE);
		mainPanel.add(nameLabel);
		mainPanel.add(countLabel);
		mainPanel.add(costLabel);
	}
	void updateLabels() {
		ProvinceDevelopement developement = 
				MapScreen.getSelectedProvince().developementList.get(screenType);
		countLabel.setText(MathFunctions.withSuffix(developement.buildingCount[ID]));
		costLabel.setText(MathFunctions.withSuffix(developement.buildingCost[ID]));
	}
	void addListener(QuickBuySlotListener listener) {
		this.listener = listener;
	}
	void showCost() {
		resetCost();
		int a = SuperScreen.buildingPointPool;
		Province selectedProvince = MapScreen.getSelectedProvince();
		ProvinceDevelopement developement = selectedProvince.developementList.get(screenType);
		CivilisationMainClass.resourceBar.updateCostLabel(a-1, developement.buildingCost[ID]);
	}
	void resetCost() {
		CivilisationMainClass.resourceBar.resetCostLabel();
	}
	static void loadImages() {
		slotBackground = new ImageIcon[DataBase.screenTypes.size()];
		for (int i=0; i<slotBackground.length; i++) {
			String screenType = DataBase.screenTypes.get(i);
			slotBackground[i] = new ImageIcon("graphics/ui/mapui-bottom/quickbuy/quickbuybuildingslot-" + screenType + ".png");
		}
		panelSize = new Dimension(slotBackground[0].getIconWidth(), slotBackground[0].getIconHeight());
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
		resetCost();
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		listener.buildingBought(screenType, ID, 1);
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int pixels = e.getWheelRotation() * 5;
		listener.mouseWheelScrolled(screenType, pixels);
	}
}
