package civilisationClicker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheatClass implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == CivilisationMainClass.cheatButton) {
			Country country = CivilisationMainClass.getPlayer();
			for (int i=0; i<country.points.length; i++) country.points[i] += 1000000;
			CivilisationMainClass.resourceBar.updateLabels();
		}
	}
	
}
