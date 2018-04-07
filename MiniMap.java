package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MiniMap extends ScaledMap{
	int viewSquareWidth;
	int viewSquareHeight;
	JPanel viewPort;
	private List<MiniMapListener> listeners = new ArrayList<MiniMapListener>();
	public MiniMap(Dimension viewSize) {
		super(DataBase.mapList.get(DataBase.chosenMap).map, 
				"data/province maps/", CivilisationMainClass.playerList.size(), viewSize);
	}
	public void createProvinceMap() {
		super.createProvinceMap();
		calculateViewArea();
	}
	public void loadProvinces() {
		MAPCONTAINERWIDTH = 352;
		MAPCONTAINERHEIGHT = 198;
		super.loadProvinces();
	}
	void calculateViewArea() {
		mapPanel.remove(clickPanel);
		mapPanel.add(clickPanel,  Integer.valueOf((provinceColors.size() * 2) + 1));
		double widthratio = (double) mapOriginalWidth / displayWidth;
		double heightratio = (double) mapOriginalHeight / displayHeight;
		viewSquareWidth = (int) (((MAPCONTAINERWIDTH / widthratio) < MAPCONTAINERWIDTH) ? MAPCONTAINERWIDTH / widthratio : MAPCONTAINERWIDTH);
		viewSquareHeight = (int) (((MAPCONTAINERHEIGHT / heightratio) < MAPCONTAINERHEIGHT) ? MAPCONTAINERHEIGHT / heightratio : MAPCONTAINERHEIGHT);
		viewPort = new JPanel();
		viewPort.setBounds(0, 0, viewSquareWidth, viewSquareHeight);
		viewPort.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		viewPort.setOpaque(false);
		mapPanel.add(viewPort, Integer.valueOf(provinceColors.size() * 2));
	}
	void updateViewWindow(int x, int y) {
		int newx = 0;
		int newy = 0;
		if (x <= mapOriginalWidth / 2) { //makes sure there is no gap between edges
			newx = (int) Math.round(x / widthScale);
		} else {
			newx = (int) Math.ceil(x / widthScale);
		}
		if (y <= mapOriginalHeight / 2) {
			newy = (int) Math.round(y / heightScale);
		} else {
			newy = (int) Math.ceil(y / heightScale);
		}
		viewPort.setBounds(newx, newy, viewSquareWidth, viewSquareHeight);
	}
	public void addMiniMapListener(MiniMapListener listener) {
		listeners.add(listener);
	}
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == clickPanel) {
			for (MiniMapListener mL : listeners) mL.miniMapClicked(e.getX(), e.getY());
		}
	}
	public void mouseDragged(MouseEvent e) {
		if (e.getSource() == clickPanel) {
			for (MiniMapListener mL : listeners) mL.miniMapClicked(e.getX(), e.getY());
		}
	}
}
interface MiniMapListener {
	void miniMapClicked(int x, int y);
}
