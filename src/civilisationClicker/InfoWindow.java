package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoWindow extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8690778137967587205L;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	JPanel messagePanel, buttonPanel;
	JLabel messageLabel;
	JButton closeButton;
	InfoWindow(String message) {
		int positionx = (CivilisationMainClass.gameWidth - WIDTH - 300) / 2;
		int positiony = (CivilisationMainClass.gameHeight - HEIGHT) / 2;
		this.setBounds(positionx, positiony, WIDTH, HEIGHT);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		messagePanel = new JPanel(new GridBagLayout());
		messagePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT - 20));
		messagePanel.setMaximumSize(new Dimension(WIDTH, HEIGHT - 20));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setPreferredSize(new Dimension(WIDTH, 20));
		buttonPanel.setMaximumSize(new Dimension(WIDTH, 20));
		messageLabel = new JLabel(message);
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		messagePanel.add(messageLabel);
		buttonPanel.add(closeButton);
		this.add(messagePanel);
		this.add(buttonPanel);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeButton) {
			CivilisationMainClass.mainLayeredPanel.remove(this);
			CivilisationMainClass.mainLayeredPanel.revalidate();
			CivilisationMainClass.mainLayeredPanel.repaint();
		}
	}
}
