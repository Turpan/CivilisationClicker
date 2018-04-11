package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatBar {
	static Rectangle bounds;
	boolean typing;
	int nameWidth;
	JPanel mainPanel;
	JLabel nameLabel;
	JLabel typeBox;
	JPanel typePanel;
	JScrollPane typeScrollPanel;
	String typed;
	ChatBar() {
		createGraphics();
	}
	void createGraphics() {
		Country player = CivilisationMainClass.getPlayer();
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setOpaque(false);
		//mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setBounds(bounds);
		nameLabel = new JLabel(player.name + ": ");
		nameLabel.setForeground(player.color);
		nameWidth = MathFunctions.getStringWidth(nameLabel.getText());
		typeBox = new JLabel();
		typeBox.setForeground(player.color);
		typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.LINE_AXIS));
		typePanel.setOpaque(false);
		Dimension scrollSize = new Dimension(bounds.width - nameWidth, bounds.height);
		typeScrollPanel = new JScrollPane(typePanel);
		typeScrollPanel.setOpaque(false);
		typeScrollPanel.getViewport().setOpaque(false);
		typeScrollPanel.setMinimumSize(scrollSize);
		typeScrollPanel.setPreferredSize(scrollSize);
		typeScrollPanel.setMaximumSize(scrollSize);
		typeScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		typeScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		typeScrollPanel.setBorder(null);
		typePanel.add(typeBox);
		mainPanel.add(nameLabel);
		mainPanel.add(typeScrollPanel);
		mainPanel.add(Box.createHorizontalGlue());
		stopTyping();
	}
	void changeVisibility() {
		mainPanel.setVisible(typing);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	void startTyping() {
		typing = true;
		updateViewPosition(0);
		changeVisibility();
	}
	void stopTyping() {
		typing = false;
		typed = "";
		changeVisibility();
	}
	void addKey(char typed) {
		if (MathFunctions.canDisplay(typed)) {
			this.typed += String.valueOf(typed);
		}
		updateLabels();
	}
	void backspace() {
		typed = typed.substring(0, typed.length() - 1);
		updateLabels();
	}
	void updateLabels() {
		typeBox.setText(typed);
		int positionx = MathFunctions.getStringWidth(typed) - (bounds.width - nameWidth);
		if (positionx < 0) positionx = 0;
		updateViewPosition(positionx);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	void updateViewPosition(int positionx) {
		typeScrollPanel.getViewport().setViewPosition(new Point(positionx, 0));
	}
}
