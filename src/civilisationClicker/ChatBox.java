package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import scrollBar.ScrollBar;
import scrollBar.ScrollEvent;
import scrollBar.ScrollListener;

public class ChatBox implements ScrollListener, MouseWheelListener, MouseListener, ActionListener{
	static final int WIDTH = 400;
	static final int HEIGHT = 200;
	static final int SCROLLWIDTH = 4;
	static final int VERTICALPAD = 20;
	static final int HORIZONTALPAD = 10;
	static final int STARTINGALPHA = 10000;
	boolean scrollBarCreated;
	boolean stayOnScreen;
	int size;
	int alphaCount = 1000;
	JPanel mainPanel;
	JPanel chatPanel;
	JScrollPane chatScrollPane;
	ScrollBar scrollBar;
	List<ChatMessage> messages = new ArrayList<ChatMessage>();
	ChatBox() {
		createGraphics();
	}
	void createGraphics() {
		int x = CivilisationMainClass.gameWidth - WIDTH - HORIZONTALPAD;
		int y = ResourceBar.RESOURCEBARSIZE + VERTICALPAD;
		Dimension chatPanelSize = new Dimension(WIDTH - SCROLLWIDTH, HEIGHT);
		Dimension scrollBarSize = new Dimension(SCROLLWIDTH, HEIGHT);
		String scrollImageLocation = "graphics/ui/mapui-bottom/chatbox/scrollbar";
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setOpaque(false);
		//mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setBounds(x, y, WIDTH, HEIGHT);
		chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.PAGE_AXIS));
		chatPanel.setOpaque(false);
		chatPanel.setMaximumSize(new Dimension(WIDTH - SCROLLWIDTH, Integer.MAX_VALUE));
		chatPanel.addMouseListener(this);
		chatScrollPane = new JScrollPane(chatPanel);
		chatScrollPane.setOpaque(false);
		chatScrollPane.getViewport().setOpaque(false);
		chatScrollPane.setMinimumSize(chatPanelSize);
		chatScrollPane.setPreferredSize(chatPanelSize);
		chatScrollPane.setMaximumSize(chatPanelSize);
		chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		chatScrollPane.setBorder(null);
		scrollBar = new ScrollBar(scrollBarSize, "", "", scrollImageLocation);
		scrollBar.setOpaque(false);
		scrollBar.scrollBar.setOpaque(false);
		scrollBar.addScrollListener(this);
		mainPanel.add(chatScrollPane);
		mainPanel.add(scrollBar);
	}
	void addMessage(Country player, String message) {
		ChatMessage chatMessage = new ChatMessage(player.color, player.name, message);
		messages.add(chatMessage);
		updateMessages();
		if (size > HEIGHT && !scrollBarCreated) {
			createScrollBar();
		} else if (size > HEIGHT) {
			updateScrollBar();
		}
		setVisible();
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	void updateMessages() {
		size = 0;
		chatPanel.removeAll();
		for (ChatMessage chatMessage : messages) {
			chatPanel.add(chatMessage.mainPanel);
			chatPanel.revalidate();
			chatMessage.mainPanel.revalidate();
			size += chatMessage.height;
		}
		chatPanel.add(Box.createVerticalGlue());
		if (size < HEIGHT) {
			chatPanel.add(Box.createRigidArea(new Dimension(0, HEIGHT - size)));
		}
	}
	void createScrollBar() {
		chatPanel.addMouseWheelListener(this);
		scrollBar.createScrollBar(size, HEIGHT);
		scrollBarCreated = true;
		updateScrollBar();
	}
	void updateScrollBar() {
		scrollBar.setWindowSize(size);
		scrollBar.setScrollBarPosition(size - HEIGHT);
		chatScrollPane.getViewport().setViewPosition(new Point(0, size - HEIGHT));
	}
	void updateAlpha() {
		double alpha = 255;
		float alphaFloat = 1;
		if (alphaCount <= 1000) {
			alphaFloat = (float) alphaCount / 1000;
			alpha = 255.0 * alphaFloat;
		}
		for (ChatMessage chatMessage : messages) {
			chatMessage.updateAlpha((int) alpha);
		}
		scrollBar.updateAlpha(alphaFloat);
		//mainPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, (int) alpha)));
	}
	void setVisible() {
		alphaCount = STARTINGALPHA;
		updateAlpha();
	}
	@Override
	public void viewChanged(ScrollEvent scrollEvent) {
		chatScrollPane.getViewport().setViewPosition(new Point(0, scrollEvent.getViewChanged()));
		setVisible();
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int pixels = e.getWheelRotation() * 5;
		int viewPosition = chatScrollPane.getViewport().getViewPosition().y;
		viewPosition += pixels;
		if (viewPosition < 0) viewPosition = 0;
		chatScrollPane.getViewport().setViewPosition(new Point(0, viewPosition));
		viewPosition = chatScrollPane.getViewport().getViewPosition().y;
		scrollBar.setScrollBarPosition(viewPosition);
		setVisible();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (alphaCount > 0 && !stayOnScreen) {
			alphaCount -= 10;
			if (alphaCount < 0) alphaCount = 0;
			updateAlpha();
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		setVisible();
		stayOnScreen = true;
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		stayOnScreen = false;
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
class ChatMessage {
	static final int HORIZONTALGAP = 100;
	static final int WIDTH = ChatBox.WIDTH - HORIZONTALGAP;
	int height;
	Color color;
	String name;
	String[] message;
	JPanel mainPanel;
	JPanel namePanel;
	JPanel messagePanel;
	JLabel nameLabel;
	JLabel[] messageLabel;
	ChatMessage(Color color, String name, String message) {
		this.color = color;
		this.name = name;
		this.message = MathFunctions.splitString(message, ChatBox.WIDTH - HORIZONTALGAP);
		createGraphics();
	}
	void createGraphics() {
		int nameWidth = MathFunctions.getStringWidth(name);
		height = message.length * MathFunctions.getCharHeight(MathFunctions.systemFont);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setOpaque(false);
		mainPanel.setMaximumSize(new Dimension(ChatBox.WIDTH, Integer.MAX_VALUE));
		namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.PAGE_AXIS));
		namePanel.setOpaque(false);
		namePanel.setMinimumSize(new Dimension((HORIZONTALGAP > nameWidth) ? nameWidth : HORIZONTALGAP, height));
		namePanel.setMaximumSize(new Dimension((HORIZONTALGAP > nameWidth) ? nameWidth : HORIZONTALGAP, height));
		messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
		messagePanel.setOpaque(false);
		messagePanel.setMinimumSize(new Dimension(WIDTH, height));
		messagePanel.setPreferredSize(new Dimension(WIDTH, height));
		messagePanel.setMaximumSize(new Dimension(WIDTH, height));
		nameLabel = new JLabel(name + ": ");
		nameLabel.setForeground(color);
		messageLabel = new JLabel[message.length];
		for (int i=0; i<message.length; i++) {
			messageLabel[i] = new JLabel(message[i]);
			messageLabel[i].setForeground(color);
		}
		namePanel.add(nameLabel);
		namePanel.add(Box.createVerticalGlue());
		for (JLabel label : messageLabel) {
			messagePanel.add(label);
		}
		mainPanel.add(Box.createHorizontalGlue());
		mainPanel.add(namePanel);
		mainPanel.add(messagePanel);
	}
	void updateAlpha(int alpha) {
		Color alphaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		nameLabel.setForeground(alphaColor);
		for (int i=0; i<messageLabel.length; i++) {
			messageLabel[i].setForeground(alphaColor);
		}
	}
}