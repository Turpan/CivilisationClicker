package civilisationClicker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerHandler implements ActionListener{ //an entirely useless class, but one i can't be bothered to remove

	@Override
	public void actionPerformed(ActionEvent e) {
		CivilisationMainClass.timerTick();
	}

}
