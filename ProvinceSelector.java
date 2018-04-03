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
	ProvinceSelector(String mapName, String mapDirectory, int playerCount, Dimension viewSize, int map) {
		//this.mapName = mapName + "-selector";
		super(mapName, mapDirectory, playerCount, viewSize);
		setColorList(DataBase.mapList.get(map).provinceColors);
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
		ownerColours[player - 1] = playerColour;
		colourProvinces();
	}
	void changeProvince(int player, int newProvince) {
		for (int i=0; i<provinceColors.size(); i++) {
			if (provinceOwner[i] == player) {
				provinceOwner[i] = 0;
			}
		}
		if (newProvince != -1) {
			provinceOwner[newProvince] = player;
		}
		colourProvinces();
	}
	@Override
	public void provinceChanged(int provinceChanged) {
		if (provinceChanged != -1) {
			SoundEngine.playProvinceClickSound();
			if (provinceOwner[provinceChanged] == 0) {
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
			int province = -1;
			int provinceCount = 0;
			for (int i=0; i<provinceColors.size(); i++) {
				if (provinceOwner[i] == CivilisationMainClass.playerID) {
					province = i;
					provinceCount ++;
				}
			}
			if (provinceCount < 2) {
				CivilisationMainClass.lobbyScreen.provinceSelected(province);
			} else {
				for (int i=0; i<provinceColors.size(); i++) {
					if (provinceOwner[i] == CivilisationMainClass.playerID) {
						provinceOwner[i] = 0;
					}
				}
			}
		}
	}
	public void reloadData(Color[] playerColour, int[] playerStartProvince) {
		Arrays.fill(provinceOwner, 0);
		ownerColours = playerColour;
		for (int i=0; i<playerStartProvince.length; i++) {
			for (int j=0; j<provinceColors.size(); j++) {
				if (j == playerStartProvince[i]) {
					provinceOwner[j] = (i + 1);
				}
			}
		}
		colourProvinces();
	}
	@Override
	public void screenDragged() {
		// TODO Auto-generated method stub
		
	}
	public void mouseDragged(MouseEvent e) {
		
	}
}
