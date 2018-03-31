package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class CivilisationMainClass{ //I hate comments. Good luck reading this nerds.
	static final String GAMESTATELOBBY = "lobby";
	static final String GAMESTATEINPROGRESS = "ingame";
	static final String GAMETYPESINGLE = "offline";
	static final String GAMETYPEHOST = "host";
	static final String GAMETYPECLIENT = "client";
	static final int GAMESPEED1 = 1000;
	static final int GAMESPEED2 = 500;
	static final int GAMESPEED3 = 100;
	static MusicPlayer musicPlayer;
	static SuperScreen clickerMaster;
	static JPanel mainPanel;
	static JPanel contentPanel;
	static Timer clickTimer;
	static Timer guiTimer;
	static String gameType, playerName;
	static TimerHandler listener;
	static Server server;
	static Client client;
	static LobbyScreen lobbyScreen;
	static MapSelector mapSelector;
	static MapScreen mapScreen;
	static ResourceBar resourceBar;
	//static CivilisationMenuBar menuBar;
	static GameStartWindow gameStartWindow;
	static CheatClass cheatListener;
	static SoundEngine soundEngine;
	static OptionsMenu optionsMenu;
	static BattleList battleList;
	static List<Country> playerList;
	static int playerID, port, lobbySize;
	static int gameHeight;
	static int gameWidth;
	static int timeCount;
	static int timerStatus;
	static int selectedPanel;
	static String checkSum;
	static String playerNames[];
	static boolean playerTicked[];
	static boolean lobbyActive;
	public static int playerCount;
	static JButton cheatButton;
	public static JFrame frame, alertFrame;
	public static JLayeredPane mainLayeredPanel;
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	private static void createAndShowGUI() {
        //Create and set up the window.
		XMLLoader.loadXMLData();
		XMLLoader.sortXMLData();
		XMLLoader.sendXMLData();
		checkSum = DataBase.getCheckSum();
		loadSettings();
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setBounds(0, 0, gameWidth, gameHeight);
		mainLayeredPanel = new JLayeredPane();
		mainLayeredPanel.setMaximumSize(new Dimension(gameWidth, gameHeight));
		mainLayeredPanel.add(mainPanel, Integer.valueOf(1));
		mainLayeredPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame = new JFrame("Civilisation Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        //frame.pack();
        //frame.setSize(gameWidth + 16, gameHeight + 39);
        frame.setSize(gameWidth, gameHeight);
        frame.setIconImage(new ImageIcon("graphics/icons/city_game.png").getImage());
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(mainLayeredPanel);
        createMusicPlayer();
        soundEngine = new SoundEngine();
        optionsMenu = new OptionsMenu();
        MainMenu.createMainMenu();
    }
	static void createHostGameScreen() {
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.PAGE_AXIS));
		JLabel serverPortLabel = new JLabel("Enter Server Port: (Default is 9898).");
		JLabel playerNameLabel = new JLabel("Enter player name: ");
		JLabel playerNumberLabel = new JLabel("Enter Number of Players: (Max is 10.)");
		JTextField serverPortField = new JTextField();
		serverPortField.setColumns(10);
		serverPortField.setMaximumSize(new Dimension(100, 20));
		serverPortField.setText("9898");
		JTextField playerNameField = new JTextField();
		playerNameField.setColumns(20);
		playerNameField.setMaximumSize(new Dimension(200, 20));
		playerNameField.setText(playerName);
		JTextField playerNumberField = new JTextField();
		playerNumberField.setColumns(2);
		playerNumberField.setMaximumSize(new Dimension(20, 20));
		playerNumberField.setText("1");
		formPanel.add(playerNameLabel);
		formPanel.add(playerNameField);
		formPanel.add(playerNumberLabel);
		formPanel.add(playerNumberField);
		formPanel.add(serverPortLabel);
		formPanel.add(serverPortField);
		int result = JOptionPane.showConfirmDialog(frame, formPanel, "Enter Server Settings", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String portText = serverPortField.getText();
			String playersText = playerNumberField.getText();
			port = 0;
			int players = 0;
			try {
				port = Integer.parseInt(portText);
				players = Integer.parseInt(playersText);
			} catch (NumberFormatException e) {
				port = 9898;
				players = 4;
			}
			playerName = playerNameField.getText();
			if (playerName == "") {
				playerName = "Host";
			}
			if (players > 10) {
				players = 10;
			} else if (players == 1) {
				playerCount = players;
				mainPanel.removeAll();
				gameType = GAMETYPESINGLE;
				playerID = 1;
				return;
			}
			lobbySize = players;
			mapSelector = new MapSelector();
			mainLayeredPanel.add(mapSelector, Integer.valueOf(2));
			//lobbyScreen = new CivilisationClickerLobbyScreen(true, players);
			//server = new CivilisationClickerServer(port, players-1);
			//server.start();
		} else {
			MainMenu.createMainMenu();
		}
	}
	static void createJoinGameScreen() {
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.PAGE_AXIS));
		JLabel serverPortLabel = new JLabel("Enter Server Port: (Default is 9898).");
		JLabel serverIPLabel = new JLabel("Enter Server IP");
		JLabel playerNameLabel = new JLabel("Enter player name: ");
		JTextField serverPortField = new JTextField();
		serverPortField.setColumns(10);
		serverPortField.setMaximumSize(new Dimension(100, 20));
		serverPortField.setText("9898");
		JTextField serverIPField = new JTextField();
		serverIPField.setColumns(20);
		serverIPField.setMaximumSize(new Dimension(200, 20));
		serverIPField.setText("");
		JTextField playerNameField = new JTextField();
		playerNameField.setColumns(20);
		playerNameField.setMaximumSize(new Dimension(200, 20));
		playerNameField.setText(playerName);
		formPanel.add(playerNameLabel);
		formPanel.add(playerNameField);
		formPanel.add(serverIPLabel);
		formPanel.add(serverIPField);
		formPanel.add(serverPortLabel);
		formPanel.add(serverPortField);
		int result = JOptionPane.showConfirmDialog(frame, formPanel, "Enter Server Settings", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String portText = serverPortField.getText();
			String ip = serverIPField.getText();
			int port = 0;
			try {
				port = Integer.parseInt(portText);
			} catch (NumberFormatException e) {
				port = 9898;
			}
			playerName = playerNameField.getText();
			if (playerName == "") {
				playerName = Integer.toString(ThreadLocalRandom.current().nextInt(1000, 500000));
			}
			client = new Client(ip, port);
			client.start();
			mainPanel.removeAll();
			JLabel connectionLabel = new JLabel("Connecting to server...");
			mainPanel.add(connectionLabel);
			mainPanel.revalidate();
		} else {
			MainMenu.createMainMenu();
		}
	}
	static void createMusicPlayer() {
		musicPlayer = new MusicPlayer();
		musicPlayer.dataFileLocation = "music/music-data.txt";
		musicPlayer.loadMusic();
		musicPlayer.play();
	}
	static void createMultiplayerGame(int players, String[] playerNames, int[] startingProvince, Color[] playerColor, int map) {
		lobbyActive = false;
		DataBase.chosenMap = map;
		cheatListener = new CheatClass();
		cheatButton = new JButton("Cheat");
		cheatButton.setBounds(gameWidth - 40, 0, 40, 40);
		cheatButton.addActionListener(cheatListener);
		mainLayeredPanel.add(cheatButton, Integer.valueOf(3));
		playerCount = players;
		CivilisationMainClass.playerNames = playerNames;
		playerList = new ArrayList<Country>();
		for (int i=0; i<players; i++) {
			if (startingProvince[i] > -1) {
				Country country = new Country(i, false, playerNames[i], playerColor[i], startingProvince[i]);
				playerList.add(country);
			}
		}
		mainPanel.removeAll();
		gameStartWindow = new GameStartWindow(startingProvince, playerNames);
		mainLayeredPanel.add(gameStartWindow, Integer.valueOf(2));
		DataBase.mapList.get(map).createDevelopementData();
		SuperScreen.loadData();
		SuperScreen.selectedBuySellButton = 1;
		mapScreen = new MapScreen();
		for (Country country : playerList) country.findAccessableProvinces();
		mainPanel.add(mapScreen.mainPanel);
		selectedPanel = 8;
		clickerMaster = new SuperScreen();
		clickerMaster.createClickerScreens();
		battleList = new BattleList();
		resourceBar = new ResourceBar();
		mainLayeredPanel.add(resourceBar.mainPanel, Integer.valueOf(3));
		guiTimer = new Timer(10, resourceBar);
		guiTimer.start();
		if (gameType == GAMETYPEHOST) {
			listener = new TimerHandler();
			clickTimer = new Timer(1000, listener);
			playerTicked = new boolean[playerCount - 1];
		}
		if (gameType == GAMETYPECLIENT) {
			gameStartWindow.changeStatus(playerID, GameStartWindow.PLAYERSTATUSWAITING);
			String output = "playerwaitstatus;" + GameStartWindow.PLAYERSTATUSWAITING + ";";
			client.outPutCommand(output);
		} else if (gameType == GAMETYPEHOST) {
			gameStartWindow.changeStatus(playerID, GameStartWindow.PLAYERSTATUSREADY);
		}
		mainPanel.revalidate();
		frame.revalidate();
		frame.repaint();
	}
	static void changeTabs(int newTab) {
		if (selectedPanel == 9) {
			optionsMenu.saveSettings();
		}
		resourceBar.resetCostLabel();
		switch (newTab) {
		case 1:
			if (MapScreen.selectedProvince != -1) {
				if (MapScreen.getSelectedProvince().owner == playerID && !MapScreen.getSelectedProvince().coloniseInProgress) {
					mainPanel.removeAll();
					clickerMaster.swapTabs(1);
					selectedPanel = 1;
				}
			}
			break;
		case 2:
			if (MapScreen.selectedProvince != -1) {
				if (MapScreen.getSelectedProvince().owner == playerID && !MapScreen.getSelectedProvince().coloniseInProgress) {
					mainPanel.removeAll();
					clickerMaster.swapTabs(2);
					selectedPanel = 2;
				}
			}
			break;
		case 3:
			if (MapScreen.selectedProvince != -1) {
				if (MapScreen.getSelectedProvince().owner == playerID && !MapScreen.getSelectedProvince().coloniseInProgress) {
					mainPanel.removeAll();
					clickerMaster.swapTabs(3);
					selectedPanel = 3;
				}
			}
			break;
		case 4:
			if (MapScreen.selectedProvince != -1) {
				if (MapScreen.getSelectedProvince().owner == playerID && !MapScreen.getSelectedProvince().coloniseInProgress) {
					mainPanel.removeAll();
					clickerMaster.swapTabs(4);
					selectedPanel = 4;
				}
			}
			break;
		case 8:
			mainPanel.removeAll();
			mainPanel.add(mapScreen.mainPanel);
			selectedPanel = 8;
			if (MapScreen.selectedProvince != -1) {
				//provinceListener.updateProvinceInfoWindow(provinceLoader.provinceSelected);
			}
			break;
		case 9:
			mainPanel.removeAll();
			mainPanel.add(optionsMenu.mainPanel);
			selectedPanel = 9;
		}
		mainPanel.repaint();
		mainPanel.revalidate();
		mainLayeredPanel.revalidate();
		mainLayeredPanel.repaint();
	}
	static void changeTabs(int newTab, int province) {
		if (selectedPanel == 9) {
			optionsMenu.saveSettings();
		}
		resourceBar.resetCostLabel();
		switch (newTab) {
		case 1:
			if (MapScreen.selectedProvince != -1) {
				if (MapScreen.getProvince(province).owner == playerID && !MapScreen.getProvince(province).coloniseInProgress) {
					mainPanel.removeAll();
					clickerMaster.swapTabs(1, province);
					selectedPanel = 1;
				}
			}
			break;
		case 2:
			if (MapScreen.selectedProvince != -1) {
				if (MapScreen.getProvince(province).owner == playerID && !MapScreen.getProvince(province).coloniseInProgress) {
					mainPanel.removeAll();
					clickerMaster.swapTabs(2, province);
					selectedPanel = 2;
				}
			}
			break;
		case 3:
			if (MapScreen.selectedProvince != -1) {
				if (MapScreen.getProvince(province).owner == playerID && !MapScreen.getProvince(province).coloniseInProgress) {
					mainPanel.removeAll();
					clickerMaster.swapTabs(3, province);
					selectedPanel = 3;
				}
			}
			break;
		case 4:
			if (MapScreen.selectedProvince != -1) {
				if (MapScreen.getProvince(province).owner == playerID && !MapScreen.getProvince(province).coloniseInProgress) {
					mainPanel.removeAll();
					clickerMaster.swapTabs(4, province);
					selectedPanel = 4;
				}
			}
			break;
		}
		mainPanel.repaint();
		mainPanel.revalidate();
		mainLayeredPanel.revalidate();
		mainLayeredPanel.repaint();
	}
	static void joinLobby(int players, int map) {
		mainPanel.removeAll();
		playerCount = players;
		lobbyScreen = new LobbyScreen(false, players, map);
		gameType = GAMETYPECLIENT;
	}
	static void connectionFailed() {
		MainMenu.createMainMenu();
	}
	static void mapSelected(int map) {
		mainLayeredPanel.remove(mapSelector);
		lobbyActive = true;
		playerID = 1;
		lobbyScreen = new LobbyScreen(true, lobbySize, map);
		gameType = GAMETYPEHOST;
		server = new Server(port, lobbySize-1);
		server.start();
	}
	static void timerSynchronise(int player) {
		playerTicked[player] = true;
		checkTimerStatus();
	}
	static void checkTimerStatus() {
		int tickCount = 0;
		int playerCount = 0;
		for (int i=0; i<playerTicked.length; i++) {
			if (server.playerSlotInUse[i]) {
				if (playerTicked[i]) {
					tickCount += 1;
				}
				playerCount += 1;
			}
		}
		if (tickCount >= playerCount) {
			clickTimer.restart();
		}
	}
	static void sendColoniseData(int i, int province) {
		// TODO Auto-generated method stub
		
	}
	static void resetProvince(int province, boolean conquered, int owner) {
		mapScreen.changeProvinceOwner(province, owner, conquered);
		clickerMaster.clearProvince(province);
		battleList.clearProvince(province);
	}
	static void sendColoniseProgressData(int player, int province) {
		String output = "provincestatus;colonise;" + player + ";" + province + ";";
		for (int i=0; i<server.players; i++) {
			if (server.playerSlotInUse[i]) {
				server.clients[i].outPutCommand(output);
			}
		}
	}
	static void networkCommunication(String output) {
		if (gameType == GAMETYPEHOST) {
			for (int i=0; i<server.players; i++) {
				if (server.playerSlotInUse[i]) {
					server.clients[i].outPutCommand(output);
				}
			}
		} else if (gameType == GAMETYPECLIENT) {
			client.outPutCommand(output);
		}
	}
	static void relayClientData(String output, int player) {
		for (int i=0; i<server.players; i++) {
			if (server.playerSlotInUse[i] && player != (i+2)) {
				server.clients[i].outPutCommand(output);
			}
		}
	}
	static void createInfoWindow(String message) {
		mainLayeredPanel.add(new InfoWindow(message), Integer.valueOf(2));
	}
	static void removePlayer(int player) {
		for (int i=0; i<MapScreen.provinceList.size(); i++) {
			if (MapScreen.provinceList.get(i).owner == player) {
				resetProvince(i, false, 0);
			}
		}
		createInfoWindow(playerNames[player - 1] + " has disconnected.");
		if (gameType == GAMETYPEHOST) {
			String output = "playerleft;" + player + ";";
			networkCommunication(output);
		}
	}
	static void timerTick() {
		timeCount += 1;
		clickerMaster.timerTick();
		resourceBar.timerTick();
		for (Country country : playerList) {
			if (country.ID == playerID - 1 || (country.isAI && gameType != GAMETYPECLIENT)) country.timerTick();
		}
		mapScreen.timerTick();
		battleList.timerTick();
		if (gameType == GAMETYPEHOST) {
			clickTimer.stop();
			checkTimerStatus();
			String output = "timertick";
			for (int i=0; i<server.players; i++) {
				if (server.playerSlotInUse[i]) {
					server.clients[i].outPutCommand(output);
				}
			}
		} else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) {
			String output = "timertick;";
			CivilisationMainClass.client.outPutCommand(output);
		}
	}
	static void changeGameSpeed(int speed) {
		timerStatus = speed;
		if (gameType != GAMETYPECLIENT) {
			switch(speed) {
			case 0:
				togglePause();
				break;
			case 1:
				clickTimer.stop();
				clickTimer.setDelay(GAMESPEED1);
				clickTimer.setInitialDelay(GAMESPEED1);
				clickTimer.start();
				break;
			case 2:
				clickTimer.stop();
				clickTimer.setDelay(GAMESPEED2);
				clickTimer.setInitialDelay(GAMESPEED2);
				clickTimer.start();
				break;
			case 3:
				clickTimer.stop();
				clickTimer.setDelay(GAMESPEED3);
				clickTimer.setInitialDelay(GAMESPEED3);
				clickTimer.start();
				break;
			}
			String output = "gamespeed;" + timerStatus + ";";
			networkCommunication(output);
		}
		resourceBar.updateTimerButtons();
	}
	static void togglePause() {
		if (gameType != GAMETYPECLIENT) {
			pauseTimer();
		} else {
			String output = "pausegame";
			client.outPutCommand(output);
		}
	}
	static void pauseTimer() {
		clickTimer.stop();
		resourceBar.updateTimerButtons();
	}
	static void recieveGameSpeedData(int speed) {
		timerStatus = speed;
		resourceBar.updateTimerButtons();
	}
	static Country getPlayer() {
		return playerList.get(playerID - 1);
	}
	static void loadSettings() {
		try {
			File settingsFile = new File("saved/settings.txt");
			FileInputStream fs;
			fs = new FileInputStream(settingsFile);
			BufferedReader settingsReader = new BufferedReader(new InputStreamReader(fs));
			String line;
			while ((line = settingsReader.readLine()) != null) {
				line = line.replaceAll("[\r\n]+", "");
				Scanner scan = new Scanner(line);
				scan.useDelimiter(";");
				String setting = scan.next();
				switch (setting) {
				case "MusicVolume":
					OptionsMenu.musicVolume = scan.nextInt();
					break;
				case "Resolution":
					gameWidth = scan.nextInt();
					gameHeight = scan.nextInt();
					break;
				}
				scan.close();
			}
			settingsReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void quickStart() {
		gameType = GAMETYPESINGLE;
		playerID = 1;
		lobbyActive = false;
		DataBase.chosenMap = 0;
		cheatListener = new CheatClass();
		cheatButton = new JButton("Cheat");
		cheatButton.setBounds(gameWidth - 340, 0, 40, 40);
		cheatButton.addActionListener(cheatListener);
		mainLayeredPanel.add(cheatButton, Integer.valueOf(3));
		playerCount = 4;
		CivilisationMainClass.playerNames = new String[] {"Amy", "Computer 1", "Computer 2", "Computer 3"};
		playerList = new ArrayList<Country>();
		Country player = new Country(0, false, playerNames[0], Color.blue, 3);
		Country ai1 = new Country(1, true, playerNames[1], Color.green, 7);
		Country ai2 = new Country(2, true, playerNames[2], Color.red, 15);
		Country ai3 = new Country(3, true, playerNames[3], Color.orange, 27);
		playerList.add(player);
		playerList.add(ai1);
		playerList.add(ai2);
		playerList.add(ai3);
		mainPanel.removeAll();
		DataBase.mapList.get(0).createDevelopementData();
		SuperScreen.loadData();
		SuperScreen.selectedBuySellButton = 1;
		mapScreen = new MapScreen();
		for (Country country : playerList) country.findAccessableProvinces();
		mainPanel.add(mapScreen.mainPanel);
		selectedPanel = 8;
		clickerMaster = new SuperScreen();
		clickerMaster.createClickerScreens();
		battleList = new BattleList();
		resourceBar = new ResourceBar();
		mainLayeredPanel.add(resourceBar.mainPanel, Integer.valueOf(3));
		listener = new TimerHandler();
		clickTimer = new Timer(1000, listener);
		guiTimer = new Timer(10, resourceBar);
		guiTimer.start();
		playerTicked = new boolean[playerCount - 1];
		clickTimer.start();
		mainPanel.revalidate();
		frame.revalidate();
		frame.repaint();
	}
}
