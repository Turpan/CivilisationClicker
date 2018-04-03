package civilisationClicker;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainMenuListener implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == MainMenu.startGameLabel) {
			SoundEngine.playClickSound();
			CivilisationMainClass.mainPanel.removeAll();
			CivilisationMainClass.gameType = CivilisationMainClass.GAMETYPESINGLE;
			CivilisationMainClass.playerCount = 1;
			CivilisationMainClass.playerID = 1;
		} else if (e.getSource() == MainMenu.quickGameLabel) {
			SoundEngine.playClickSound();
			CivilisationMainClass.mainPanel.removeAll();
			CivilisationMainClass.quickStart();
		} else if (e.getSource() == MainMenu.hostGameLabel) {
			SoundEngine.playClickSound();
			CivilisationMainClass.mainPanel.removeAll();
			CivilisationMainClass.createHostGameScreen();
		} else if (e.getSource() == MainMenu.joinGameLabel) {
			SoundEngine.playClickSound();
			CivilisationMainClass.mainPanel.removeAll();
			CivilisationMainClass.createJoinGameScreen();
		} else if (e.getSource() == MainMenu.optionsMenuLabel) {
			SoundEngine.playClickSound();
			CivilisationMainClass.mainPanel.removeAll();
			CivilisationMainClass.mainPanel.add(CivilisationMainClass.optionsMenu.mainPanel);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
