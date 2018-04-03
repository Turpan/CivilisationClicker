package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LobbyScreen implements ActionListener, MouseListener{
	static final String ONSCREENCOLOURSELECTOR = "colour";
	static final String ONSCREENPROVINCESELECTOR = "province";
	static final String ONSCREENNONE = "none";
	int playerSlots, playerOrder[], numberOfPlayers, playerStartProvince[];
	String itemOnScreen;
	int map;
	String[] playerNames;
	boolean isHost, playerInLobby[], playerReady[];
	JPanel lobbyMasterPanel, playerPanel, playerSlotPanel[], buttonPanel, kickButtonPanel[], playerNamePanel[], playerIPPanel[], playerColourPanel[];
	JLabel playerSlotsLabel, gameNameLabel, playerNameLabel[], playerIPLabel[], viewMapLabel, readyIconLabel[];
	JButton kickButton[], startGameButton, exitLobbyButton;
	ImageIcon readyIcon, notReadyIcon;
	Color playerColour[];
	ColourSelector colourSelector;
	ProvinceSelector startSelector;
	public LobbyScreen(boolean host, int players, int map) {
		playerSlots = players;
		isHost = host;
		this.map = map;
		loadImages();
		createLobbyScreen();
	}
	void createLobbyScreen() {
		lobbyMasterPanel = new JPanel();
		lobbyMasterPanel.setLayout(new BoxLayout(lobbyMasterPanel, BoxLayout.PAGE_AXIS));
		lobbyMasterPanel.setPreferredSize(new Dimension(CivilisationMainClass.gameWidth, CivilisationMainClass.gameHeight));
		lobbyMasterPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth, CivilisationMainClass.gameHeight));
		playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.PAGE_AXIS));
		gameNameLabel = new JLabel("Civilisation Clicker Lobby");
		playerSlotsLabel = new JLabel("Player Slots: " + playerSlots + ".");
		Dimension selectorSize = new Dimension(ProvinceSelector.WIDTH, ProvinceSelector.HEIGHT - 20);
		String mapname = DataBase.mapList.get(map).map;
		String directory = "data/province maps/";
		startSelector = new ProvinceSelector(mapname, directory, playerSlots, selectorSize, map);
		viewMapLabel = new JLabel("View Map...");
		viewMapLabel.addMouseListener(this);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setMaximumSize(new Dimension(CivilisationMainClass.gameWidth, 100));
		colourSelector = new ColourSelector();
		startGameButton = new JButton("Start Game");
		startGameButton.addActionListener(this);
		if (!isHost) {
			startGameButton.setText("Ready");
		}
		exitLobbyButton = new JButton("Exit Lobby");
		numberOfPlayers = 1;
		playerNamePanel = new JPanel[playerSlots];
		playerIPPanel = new JPanel[playerSlots];
		kickButtonPanel = new JPanel[playerSlots];
		playerSlotPanel = new JPanel[playerSlots];
		playerColourPanel = new JPanel[playerSlots];
		playerNameLabel = new JLabel[playerSlots];
		playerIPLabel = new JLabel[playerSlots];
		readyIconLabel = new JLabel[playerSlots];
		kickButton = new JButton[playerSlots];
		playerOrder = new int[playerSlots];
		playerColour = new Color[playerSlots];
		playerStartProvince = new int[playerSlots];
		playerInLobby = new boolean[playerSlots];
		playerReady = new boolean[playerSlots];
		playerNames = new String[playerSlots];
		for (int i=0; i<playerSlots; i++) {
			playerSlotPanel[i] = new JPanel();
			playerSlotPanel[i].setLayout(new BoxLayout(playerSlotPanel[i], BoxLayout.LINE_AXIS));
			playerSlotPanel[i].setMaximumSize(new Dimension(CivilisationMainClass.gameWidth, 100));
			playerNamePanel[i] = new JPanel();
			playerNamePanel[i].setLayout(new GridBagLayout());
			playerNamePanel[i].setMaximumSize(new Dimension(100, 100));
			playerIPPanel[i] = new JPanel();
			playerIPPanel[i].setLayout(new GridBagLayout());
			playerIPPanel[i].setMaximumSize(new Dimension(100, 100));
			playerColourPanel[i] = new JPanel();
			playerColourPanel[i].setPreferredSize(new Dimension(100, 100));
			playerColourPanel[i].setMaximumSize(new Dimension(100, 100));
			playerColourPanel[i].setBackground(Color.GRAY);
			playerColourPanel[i].setOpaque(true);
			playerColourPanel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			playerColourPanel[i].addMouseListener(this);
			playerColour[i] = Color.GRAY;
			startSelector.addPlayerColour(i + 1, playerColour[i]);
			playerStartProvince[i] = -1;
			playerReady[i] = false;
			kickButtonPanel[i] = new JPanel();
			kickButtonPanel[i].setLayout(new GridBagLayout());
			kickButtonPanel[i].setMaximumSize(new Dimension(200, 100));
			playerNameLabel[i] = new JLabel("Empty Slot");
			playerIPLabel[i] = new JLabel("Empty Slot");
			readyIconLabel[i] = new JLabel(notReadyIcon);
			kickButton[i] = new JButton("Kick Player");
			kickButton[i].setPreferredSize(new Dimension(200, 100));
			kickButton[i].addActionListener(this);
			if (!isHost) {
				kickButton[i].setEnabled(false);
				kickButton[i].setToolTipText("Only host may kick.");
			}
			playerNamePanel[i].add(playerNameLabel[i]);
			playerIPPanel[i].add(playerIPLabel[i]);
			kickButtonPanel[i].add(kickButton[i]);
			playerSlotPanel[i].add(playerNamePanel[i]);
			playerSlotPanel[i].add(playerIPPanel[i]);
			playerSlotPanel[i].add(playerColourPanel[i]);
			playerSlotPanel[i].add(readyIconLabel[i]);
			playerSlotPanel[i].add(Box.createHorizontalGlue());
			playerSlotPanel[i].add(kickButtonPanel[i]);
			playerPanel.add(playerSlotPanel[i]);
		}
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(exitLobbyButton);
		buttonPanel.add(startGameButton);
		lobbyMasterPanel.add(gameNameLabel);
		lobbyMasterPanel.add(playerSlotsLabel);
		lobbyMasterPanel.add(viewMapLabel);
		lobbyMasterPanel.add(playerPanel);
		lobbyMasterPanel.add(Box.createVerticalGlue());
		lobbyMasterPanel.add(buttonPanel);
		if (isHost) {
			playerNames[0] = CivilisationMainClass.playerName;
			playerNameLabel[0].setText(CivilisationMainClass.playerName);
			playerIPLabel[0].setText(findOwnIP());
			kickButton[0].setEnabled(false);
			kickButton[0].setToolTipText("Cannot kick self. Exit lobby through the exit lobby button.");
			playerInLobby[0] = true;
			playerOrder[0] = 1;
			playerReady[0] = true;
			readyIconLabel[0].setIcon(readyIcon);
		}
		CivilisationMainClass.mainPanel.removeAll();
		CivilisationMainClass.mainPanel.add(lobbyMasterPanel);
		CivilisationMainClass.mainPanel.revalidate();
	}
	void fillLobbyFromServer(String playerNames[], String playerIP[], int playerOrder[], Color playerColor[], int[] playerStartProvince, boolean[] playerReady) { 
		for (int i=0; i<playerSlots; i++) {
			playerNameLabel[i].setText(playerNames[i]);
			playerIPLabel[i].setText(playerIP[i]);
			this.playerOrder[i] = playerOrder[i];
			playerColour[i] = playerColor[i];
			playerColourPanel[i].setBackground(playerColour[i]);
			this.playerStartProvince[i] = playerStartProvince[i];
			this.playerReady[i] = playerReady[i];
			if (playerReady[i]) {
				readyIconLabel[i].setIcon(readyIcon);
			} else {
				readyIconLabel[i].setIcon(notReadyIcon);
			}
		}
		restructureLobbySlots();
		startSelector.reloadData(playerColour, this.playerStartProvince);
	}
	void addPlayer(String address, int playerID) {
		playerInLobby[playerID - 1] = true;
		numberOfPlayers += 1;
		playerOrder[playerID - 1] = numberOfPlayers;
		playerIPLabel[playerID - 1].setText(address);
		restructureLobbySlots();
	}
	void addPlayerName(String name, int playerID) {
		playerNames[playerID - 1] = name;
		playerNameLabel[playerID - 1].setText(name);
		lobbyMasterPanel.revalidate();
	}
	void restructureLobbySlots() {
		playerPanel.removeAll();
		int a = 1;
		boolean slotAdded[] = new boolean[playerSlots];
		for (int i=0; i<playerSlots; i++) {
			if (a == playerOrder[i]) {
				a += 1;
				playerPanel.add(playerSlotPanel[i]);
				slotAdded[i] = true;
			}
		}
		for (int i=0; i<playerSlots; i++) {
			if (slotAdded[i] == false) {
				playerPanel.add(playerSlotPanel[i]);
			}
		}
		lobbyMasterPanel.revalidate();
	}
	void removePlayer(int player) {
		String playerName = playerNames[player];
		playerNames[player] = null;
		playerNameLabel[player].setText("Empty Slot");
		playerIPLabel[player].setText("Empty Slot");
		playerOrder[player] = 0;
		playerInLobby[player] = false;
		numberOfPlayers -= 1;
		playerColour[player] = Color.GRAY;
		playerColourPanel[player].setBackground(playerColour[player]);
		this.playerStartProvince[player] = -1;
		this.playerReady[player] = false;
		readyIconLabel[player].setIcon(notReadyIcon);
		restructureLobbySlots();
		startSelector.reloadData(playerColour, this.playerStartProvince);
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "lobbyplayerleft;" + player + ";";
			CivilisationMainClass.networkCommunication(output);
			CivilisationMainClass.createInfoWindow(playerName + " has disconnected.");
		}
	}
	boolean checkReadyStatus() {
		boolean ready = false;
		int players = 0;
		int readys = 0;
		for (int i=0; i<playerSlots; i++) {
			if (playerInLobby[i]) {
				players++;
				if (playerReady[i]) {
					readys++;
				}
			}
		}
		if (players == readys) {
			ready = true;
		}
		return ready;
	}
	void streamGameData() {
		String output = "gamestart;" + playerSlots + ";" + map + ";";
		for (int i=0; i<playerSlots; i++) {
			output += playerNames[i] + ";";
			output += playerStartProvince[i] + ";";
			output += playerColour[i].getRed() + ";" + playerColour[i].getGreen() + ";" + playerColour[i].getBlue() + ";";
		}
		for (int i=0; i<CivilisationMainClass.server.players; i++) {
			if (CivilisationMainClass.server.playerSlotInUse[i]) {
				CivilisationMainClass.server.clients[i].outPutCommand(output);
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startGameButton) {
			if (isHost && checkReadyStatus()) {
				CivilisationMainClass.server.stopListening();
				streamGameData();
				CivilisationMainClass.createMultiplayerGame(playerSlots, playerNames, playerStartProvince, playerColour, map);
			} else {
				playerReady[CivilisationMainClass.playerID - 1] = !playerReady[CivilisationMainClass.playerID - 1];
				if (playerReady[CivilisationMainClass.playerID - 1]) {
					readyIconLabel[CivilisationMainClass.playerID - 1].setIcon(readyIcon);
				} else {
					readyIconLabel[CivilisationMainClass.playerID - 1].setIcon(notReadyIcon);
				}
				String output = "lobbyready;" + playerReady[CivilisationMainClass.playerID - 1] + ";";
				CivilisationMainClass.client.outPutCommand(output);
			}
		}
		for (int i=0; i<playerSlots; i++) {
			if (e.getSource() == kickButton[i]) {
				removePlayer(i);
				String output = "kicked;";
				CivilisationMainClass.server.clients[i - 1].outPutCommand(output);
				try {
					CivilisationMainClass.server.clients[i - 1].socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CivilisationMainClass.server.playerSlotInUse[i - 1] = false;
			}
		}
	}
	String findOwnIP() {
		String IP = "";
		try {
			URL ipServerURL = new URL("http://ipv4.icanhazip.com/");
			BufferedReader in = new BufferedReader(new InputStreamReader(ipServerURL.openStream()));
			IP = in.readLine();
			System.out.println(IP);
		} catch (MalformedURLException e) {
			IP = "Could not find your IP. Are you online?";
			e.printStackTrace();
		} catch (IOException e) {
			IP = "Could not find your IP. Are you online?";
			e.printStackTrace();
		}
		return IP;
	}
	void provinceSelected(int province) {
		boolean provinceInUse = false;
		for (int i=0; i<playerSlots; i++) {
			if (playerStartProvince[i] == province) {
				provinceInUse = true;
			}
		}
		if (!provinceInUse) {
			playerStartProvince[CivilisationMainClass.playerID - 1] = province;
			if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
				for (int i=0; i<CivilisationMainClass.server.players; i++) {
					if (CivilisationMainClass.server.playerSlotInUse[i]) {
						String output = "province;1;" + province + ";";
						CivilisationMainClass.server.clients[i].outPutCommand(output);
					}
				}
			} else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) {
				String output = "province;" + province + ";";
				CivilisationMainClass.client.outPutCommand(output);
			}
			CivilisationMainClass.mainLayeredPanel.remove(startSelector.selectorPanel);
			lobbyMasterPanel.repaint();
			lobbyMasterPanel.revalidate();
			itemOnScreen = ONSCREENNONE;
		} else {
			startSelector.reloadData(playerColour, playerStartProvince);
		}
	}
	void recieveProvinceData(int playerID, int province) {
		playerStartProvince[playerID - 1] = province;
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			for (int i=0; i<CivilisationMainClass.server.players; i++) {
				if (CivilisationMainClass.server.playerSlotInUse[i] && i != (playerID - 2)) {
					String output = "province;" + playerID + ";" + province + ";";
					CivilisationMainClass.server.clients[i].outPutCommand(output);
				}
			}
		}
		startSelector.reloadData(playerColour, playerStartProvince);
	}
	void colourSelected(int red, int green, int blue) {
		Color colourSelected = new Color(red, green, blue);
		boolean colourInUse = false;
		for (int i=0; i<playerSlots; i++) {
			if (playerColour[i] == colourSelected) {
				colourInUse = true;
			}
		}
		if (!colourInUse) {
			playerColour[CivilisationMainClass.playerID - 1] = colourSelected;
			playerColourPanel[CivilisationMainClass.playerID - 1].setBackground(playerColour[CivilisationMainClass.playerID - 1]);
			startSelector.addPlayerColour(CivilisationMainClass.playerID, colourSelected);
			if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
				for (int i=0; i<CivilisationMainClass.server.players; i++) {
					if (CivilisationMainClass.server.playerSlotInUse[i]) {
						String output = "colour;1;" + red + ";" + green + ";" + blue + ";";
						CivilisationMainClass.server.clients[i].outPutCommand(output);
					}
				}
			} else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) {
				String output = "colour;" + red + ";" + green + ";" + blue + ";";
				CivilisationMainClass.client.outPutCommand(output);
			}
			CivilisationMainClass.mainLayeredPanel.remove(colourSelector);
			lobbyMasterPanel.repaint();
			lobbyMasterPanel.revalidate();
			itemOnScreen = ONSCREENNONE;
		}
	}
	void recieveColourData(int playerID, Color playerColor) {
		playerColour[playerID-1] = playerColor;
		playerColourPanel[playerID-1].setBackground(playerColor);
		startSelector.addPlayerColour(playerID, playerColor);
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			for (int i=0; i<CivilisationMainClass.server.players; i++) {
				if (CivilisationMainClass.server.playerSlotInUse[i] && i != (playerID - 2)) {
					String output = "colour;" + playerID + ";" + playerColor.getRed() + ";" + playerColor.getGreen() + ";" + playerColor.getBlue() + ";";
					CivilisationMainClass.server.clients[i].outPutCommand(output);
				}
			}
		}
	}
	void recieveReadyData(int playerID, boolean ready) {
		playerReady[playerID - 1] = ready;
		if (playerReady[playerID - 1]) {
			readyIconLabel[playerID - 1].setIcon(readyIcon);
		} else {
			readyIconLabel[playerID - 1].setIcon(notReadyIcon);
		}
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			for (int i=0; i<CivilisationMainClass.server.players; i++) {
				if (CivilisationMainClass.server.playerSlotInUse[i] && i != (playerID - 2)) {
					String output = "lobbyready;" + playerID + ";" + playerReady[playerID - 1] + ";";
					CivilisationMainClass.server.clients[i].outPutCommand(output);
				}
			}
		}
	}
	void loadImages() {
		readyIcon = new ImageIcon("graphics/ui/ready-icon.png");
		notReadyIcon = new ImageIcon("graphics/ui/notready-icon.png");
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
		SoundEngine.playClickSound();
		if (e.getSource() == viewMapLabel) {
			if (itemOnScreen == ONSCREENCOLOURSELECTOR) {
				CivilisationMainClass.mainLayeredPanel.remove(colourSelector);
				lobbyMasterPanel.repaint();
				lobbyMasterPanel.revalidate();
			}
			CivilisationMainClass.mainLayeredPanel.add(startSelector.selectorPanel, Integer.valueOf(2));
			itemOnScreen = ONSCREENPROVINCESELECTOR;
			return;
		}
		for (int i=0; i<playerSlots; i++) {
			if (e.getSource() == playerColourPanel[i] && CivilisationMainClass.playerID == (i+1)) {
				if (itemOnScreen == ONSCREENPROVINCESELECTOR) {
					CivilisationMainClass.mainLayeredPanel.remove(startSelector.selectorPanel);
					lobbyMasterPanel.repaint();
					lobbyMasterPanel.revalidate();
				}
				CivilisationMainClass.mainLayeredPanel.add(colourSelector,  Integer.valueOf(2));
				itemOnScreen = ONSCREENCOLOURSELECTOR;
				return;
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
