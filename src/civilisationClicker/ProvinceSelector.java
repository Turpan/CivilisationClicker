package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import provinceGenerator.ProvinceListener;

public class ProvinceSelector extends ScaledMap implements ActionListener, ProvinceListener{

	static final int WIDTH = 400;
	static final int HEIGHT = 300;
	JPanel buttonPanel;
	JPanel selectorPanel;
	JButton selectButton;
	Color[] playerColor;
	int[] playerProvince;
	ProvinceSelector(String mapName, String mapDirectory, int playerCount, Dimension viewSize, int map) {
		//this.mapName = mapName + "-selector";
		super(mapName, mapDirectory, playerCount, viewSize);
		setColorList(DataBase.mapList.get(map).provinceColors);
		playerColor = new Color[playerCount];
		playerProvince = new int[playerCount];
		Arrays.fill(playerProvince, -1);
		loadProvinces();
		createProvinceSelector();
	}
	public void loadProvinces() {
		MAPCONTAINERWIDTH = WIDTH;
		MAPCONTAINERHEIGHT = HEIGHT - 20;
		super.loadProvinces();
	}
	void createProvinceSelector() {
		addProvinceListener(this);
		int positionx = (CivilisationMainClass.gameWidth - WIDTH) / 2;
		int positiony = (CivilisationMainClass.gameHeight - HEIGHT) / 2;
		mapContainerPanel.setBounds(0, 20, WIDTH, HEIGHT - 20);
		selectorPanel = new JPanel();
		selectorPanel.setBounds(positionx, positiony, WIDTH, HEIGHT);
		selectorPanel.setLayout(null);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBounds(0, 0, WIDTH, 20);
		selectButton = new JButton("Confirm");
		selectButton.addActionListener(this);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(selectButton);
		selectorPanel.add(buttonPanel);
		selectorPanel.add(mapContainerPanel);
	}
	void addPlayerColour(int player, Color playerColour) {
		playerColor[player - 1] = playerColour;
		colourProvinces();
	}
	void changeProvince(int player, int newProvince) {
		playerProvince[player - 1] = newProvince;
		colourProvinces();
	}
	void colourProvinces() {
		for (int i=0; i<provinceColors.size(); i++) {
			colourProvince(i, Color.GRAY);
		}
		for (int i=0; i<playerProvince.length; i++) {
			if (playerProvince[i] > -1) colourProvince(playerProvince[i], playerColor[i]);
		}
	}
	@Override
	public void provinceChanged(int provinceChanged) {
		if (provinceChanged != -1) {
			CivilisationMainClass.soundEngine.playProvinceClickSound();
			boolean provinceInUse = false;
			for (int i=0; i<playerProvince.length; i++) {
				if (playerProvince[i] == provinceChanged) {
					provinceInUse = true;
					break;
				}
			}
			if (!provinceInUse) {
				changeProvince(CivilisationMainClass.playerID, provinceChanged);
			}
		} else {
			changeProvince(CivilisationMainClass.playerID, provinceChanged);
		}
	}
	@Override
	public void loadingFinished() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectButton) {
			boolean provinceInUse = false;
			int ID = CivilisationMainClass.playerID - 1;
			for (int i=0; i<playerProvince.length; i++) {
				if (playerProvince[i] == playerProvince[ID] &&
						i != ID) {
					provinceInUse = true;
					break;
				}
			}
			if (provinceInUse) {
				playerProvince[ID] = -1;
			} else {
				CivilisationMainClass.lobbyScreen.provinceSelected(playerProvince[ID]);
			}
		}
	}
	public void reloadData(Color[] playerColor, int[] playerProvince) {
		this.playerColor = playerColor;
		this.playerProvince = playerProvince;
		colourProvinces();
	}
	@Override
	public void screenDragged() {
		// TODO Auto-generated method stub
		
	}
	public void mouseDragged(MouseEvent e) {
		
	}
}
