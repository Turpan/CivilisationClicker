package civilisationClicker;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStartWindow extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final String PLAYERSTATUSLOADING = "Loading...";
	static final String PLAYERSTATUSWAITING = "Waiting...";
	static final String PLAYERSTATUSREADY = "Ready.";
	String[] playerName, playerStatus;
	int[] startProvince;
	JButton startButton;
	JPanel titlePanel, buttonPanel;
	JPanel[] playerNamePanel, playerStatusPanel;
	JLabel titleLabel;
	JLabel[] playerNameLabel, playerStatusLabel;
	GameStartWindow(int[] startProvince, String[] playerName) {
		this.playerName = playerName;
		this.startProvince = startProvince;
		initialiseVariables();
		createStartWindow();
		
	}
	void createStartWindow() {
		this.setBounds(600, 300, 400, 300);
		this.setLayout(null);
		titlePanel = new JPanel();
		titlePanel.setLayout(new GridBagLayout());
		titlePanel.setBounds(0, 0, 400, 20);
		titleLabel = new JLabel("Waiting for players...");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBounds(0, 280, 400, 20);
		startButton = new JButton("Start Game.");
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) {
			startButton.setText("Ready");
		}
		startButton.addActionListener(this);
		int panelCount = 1;
		for (int i=0; i<CivilisationMainClass.playerCount; i++) {
			playerNamePanel[i] = new JPanel(new GridBagLayout());
			playerNamePanel[i].setBounds(0, (20 * panelCount), 100, 20);
			playerStatusPanel[i] = new JPanel(new GridBagLayout());
			playerStatusPanel[i].setBounds(100, (20 * panelCount), 100, 20);
			playerNameLabel[i] = new JLabel(playerName[i]);
			playerStatusLabel[i] = new JLabel(PLAYERSTATUSLOADING);
			playerStatus[i] = PLAYERSTATUSLOADING;
			playerNamePanel[i].add(playerNameLabel[i]);
			playerStatusPanel[i].add(playerStatusLabel[i]);
			if (startProvince[i] != -1) {
				this.add(playerNamePanel[i]);
				this.add(playerStatusPanel[i]);
				panelCount += 1;
			}
		}
		titlePanel.add(titleLabel);
		buttonPanel.add(startButton);
		this.add(titlePanel);
		this.add(buttonPanel);
		
	}
	void changeStatus(int player, String status) {
		playerStatus[player - 1] = status;
		playerStatusLabel[player - 1].setText(status);
		String output = "playerwaitstatus;" + player + ";" + status + ";";
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			for (int i=0; i<CivilisationMainClass.server.players; i++) {
				if (CivilisationMainClass.server.playerSlotInUse[i] && i != (player - 2)) {
					CivilisationMainClass.server.clients[i].outPutCommand(output);
				}
			}
		}
		this.revalidate();
	}
	boolean readyCheck() {
		boolean ready = false;
		int playerCount = 0;
		int readyCount = 0;
		for (int i=0; i<CivilisationMainClass.server.players; i++) {
			if (CivilisationMainClass.server.playerSlotInUse[i]) {
				if (playerStatus[i + 1].equals(PLAYERSTATUSREADY)) {
					readyCount ++;
				}
				playerCount ++;
			}
		}
		if (readyCount >= playerCount) {
			ready = true;
		}
		return ready;
	}
	void initialiseVariables() {
		 playerNamePanel = new JPanel[CivilisationMainClass.playerCount];
		 playerStatusPanel = new JPanel[CivilisationMainClass.playerCount];
		 playerNameLabel = new JLabel[CivilisationMainClass.playerCount];
		 playerStatusLabel = new JLabel[CivilisationMainClass.playerCount];
		 playerStatus = new String[CivilisationMainClass.playerCount];
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SoundEngine.playClickSound();
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) {
			if (playerStatus[CivilisationMainClass.playerID - 1] == PLAYERSTATUSWAITING) {
				playerStatus[CivilisationMainClass.playerID - 1] = PLAYERSTATUSREADY;
				playerStatusLabel[CivilisationMainClass.playerID - 1].setText(PLAYERSTATUSREADY);
			} else if (playerStatus[CivilisationMainClass.playerID - 1] == PLAYERSTATUSREADY) {
				playerStatus[CivilisationMainClass.playerID - 1] = PLAYERSTATUSWAITING;
				playerStatusLabel[CivilisationMainClass.playerID - 1].setText(PLAYERSTATUSWAITING);
			}
			String output = "playerwaitstatus;" + playerStatus[CivilisationMainClass.playerID - 1] + ";";
			CivilisationMainClass.client.outPutCommand(output);
		} else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			if (readyCheck()) {
				CivilisationMainClass.mainLayeredPanel.remove(this);
				String output = "gametimerbegin;";
				for (int i=0; i<CivilisationMainClass.server.players; i++) {
					if (CivilisationMainClass.server.playerSlotInUse[i]) {
						CivilisationMainClass.server.clients[i].outPutCommand(output);
					}
				}
				CivilisationMainClass.mainLayeredPanel.revalidate();
				CivilisationMainClass.mainLayeredPanel.repaint();
			}
		}
	}
}
