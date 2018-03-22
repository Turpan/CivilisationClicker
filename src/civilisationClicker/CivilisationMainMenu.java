package civilisationClicker;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CivilisationMainMenu {
	static JLabel startGameLabel, quickGameLabel, hostGameLabel, joinGameLabel, optionsMenuLabel;
	static JPanel mainMenuPanel;
	private static CivilisationMainMenuListener listener;
	public static void createMainMenu() {
		listener = new CivilisationMainMenuListener();
		mainMenuPanel = new JPanel();
		mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.PAGE_AXIS));
		startGameLabel = new JLabel("Play Offline");
		startGameLabel.addMouseListener(listener);
		quickGameLabel = new JLabel("Quick Start");
		quickGameLabel.addMouseListener(listener);
		hostGameLabel = new JLabel("Host Game");
		hostGameLabel.addMouseListener(listener);
		joinGameLabel = new JLabel("Join Game");
		joinGameLabel.addMouseListener(listener);
		optionsMenuLabel = new JLabel("Options");
		optionsMenuLabel.addMouseListener(listener);
		mainMenuPanel.add(startGameLabel);
		mainMenuPanel.add(quickGameLabel);
		mainMenuPanel.add(hostGameLabel);
		mainMenuPanel.add(joinGameLabel);
		mainMenuPanel.add(optionsMenuLabel);
		CivilisationMainClass.mainLayeredPanel.removeAll();
		CivilisationMainClass.mainLayeredPanel.add(CivilisationMainClass.mainPanel, Integer.valueOf(1));
		CivilisationMainClass.mainPanel.removeAll();
		CivilisationMainClass.mainPanel.add(mainMenuPanel);
		CivilisationMainClass.mainPanel.revalidate();
	}
}
