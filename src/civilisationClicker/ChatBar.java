package civilisationClicker;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import paintedPanel.PaintedPanel;

public class ChatBar {
	static Rectangle bounds;
	static final float alpha = (float) 0.4;
	boolean typing;
	int nameWidth;
	JLayeredPane mainLayeredPanel;
	JPanel mainPanel;
	PaintedPanel backgroundPanel;
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
		mainLayeredPanel = new JLayeredPane();
		mainLayeredPanel.setOpaque(false);
		mainLayeredPanel.setBounds(bounds);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setOpaque(false);
		//mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setBounds(0, 0, bounds.width, bounds.height);
		backgroundPanel = new PaintedPanel();
		backgroundPanel.bgImage = new ImageIcon("graphics/ui/mapui-bottom/chatbox/chatbar.png");
		backgroundPanel.setAlpha(alpha);
		backgroundPanel.setOpaque(false);
		backgroundPanel.setBounds(0, 0, bounds.width, bounds.height);
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
		mainLayeredPanel.add(backgroundPanel, Integer.valueOf(1));
		mainLayeredPanel.add(mainPanel, Integer.valueOf(2));
		stopTyping();
	}
	void changeVisibility() {
		mainLayeredPanel.setVisible(typing);
		mainLayeredPanel.revalidate();
		mainLayeredPanel.repaint();
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
		mainLayeredPanel.revalidate();
		mainLayeredPanel.repaint();
	}
	void updateViewPosition(int positionx) {
		typeScrollPanel.getViewport().setViewPosition(new Point(positionx, 0));
	}
}
