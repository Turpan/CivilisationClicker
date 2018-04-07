package scrollBar;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;

import paintedPanel.PaintedPanel;

public class ScrollBar extends PaintedPanel implements MouseInputListener{ //remember to clean this up later, no need to constantly recalculate values that do not change
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SCROLLVERTICAL = "vertical";
	public static final String SCROLLHORIZONTAL = "horizontal";
	String ID;
	boolean upButtonPressed, downButtonPressed, scrollBarPressed;
	JLabel upButtonLabel, downButtonLabel;
	public PaintedPanel scrollBar;
	int windowSize, viewSize, viewPosition, scrollBarSize, scrollBarPosition, clickOrigin, buttonViewChange;
	private String upButtonLocation, downButtonLocation, scrollBarImageLocation, scrollDirection;
	ImageIcon upButtonImage, upButtonPressedImage, downButtonImage, downButtonPressedImage, scrollBarImage, scrollBarPressedImage, backGroundImage;
	private Dimension panelSize;
	private List<ScrollListener> listeners = new ArrayList<ScrollListener>();
	public ScrollBar(Dimension panelSize, String upButtonLocation, String downButtonLocation, String scrollBarImageLocation) {
		this.panelSize = panelSize;
		this.upButtonLocation = upButtonLocation;
		this.downButtonLocation = downButtonLocation;
		this.scrollBarImageLocation = scrollBarImageLocation;
		this.scrollDirection = SCROLLVERTICAL;
		createComponents();
	}
	public ScrollBar(String scrollDirection, Dimension panelSize, String upButtonLocation, String downButtonLocation, String scrollBarImageLocation) {
		this.panelSize = panelSize;
		this.upButtonLocation = upButtonLocation;
		this.downButtonLocation = downButtonLocation;
		this.scrollBarImageLocation = scrollBarImageLocation;
		switch (scrollDirection) {
		case SCROLLVERTICAL:
			this.scrollDirection = scrollDirection;
			break;
		case SCROLLHORIZONTAL:
			this.scrollDirection = scrollDirection;
			break;
		default:
			this.scrollDirection = SCROLLVERTICAL;
			break;
		}
		createComponents();
	}
	public void setID(String ID) {
		this.ID = ID;
	}
	private void createComponents() {
		this.setMinimumSize(panelSize);
		this.setPreferredSize(panelSize);
		this.setMaximumSize(panelSize);
		this.setLayout(null);
		this.setPaintDirection(scrollDirection);
		scrollBarImage = new ImageIcon(scrollBarImageLocation + ".png");
		scrollBarPressedImage = new ImageIcon(scrollBarImageLocation + "-pressed.png");
		upButtonImage = new ImageIcon(upButtonLocation + ".png");
		upButtonPressedImage = new ImageIcon(upButtonLocation + "-pressed.png");
		Dimension upButtonSize = new Dimension(upButtonImage.getIconWidth(), upButtonImage.getIconHeight());
		downButtonImage = new ImageIcon(downButtonLocation + ".png");
		downButtonPressedImage = new ImageIcon(downButtonLocation + "-pressed.png");
		Dimension downButtonSize = new Dimension(downButtonImage.getIconWidth(), downButtonImage.getIconHeight());
		upButtonLabel = new JLabel(upButtonImage);
		upButtonLabel.setVisible(false);
		upButtonLabel.setBounds(0, 0, upButtonSize.width, upButtonSize.height);
		upButtonLabel.addMouseListener(this);
		downButtonLabel = new JLabel(downButtonImage);
		switch(scrollDirection) {
		case SCROLLVERTICAL:
			downButtonLabel.setBounds(0, panelSize.height - downButtonSize.height, downButtonSize.width, downButtonSize.height);
			break;
		case SCROLLHORIZONTAL:
			downButtonLabel.setBounds(panelSize.width - downButtonSize.width, 0, downButtonSize.width, downButtonSize.height);
			break;
		}
		downButtonLabel.addMouseListener(this);
		scrollBar = new PaintedPanel(scrollDirection);
		scrollBar.bgImage = scrollBarImage;
		scrollBar.addMouseListener(this);
		scrollBar.addMouseMotionListener(this);
		this.add(upButtonLabel);
		this.add(scrollBar);
		this.add(downButtonLabel);
	}
	private void createScrollBar() {
		double ratio = ((double) viewSize / (double) windowSize);
		double scrollBarSpace = 0;
		switch(scrollDirection) {
		case SCROLLVERTICAL:
			scrollBarSpace =  (panelSize.height - upButtonImage.getIconHeight() - downButtonImage.getIconHeight());
			break;
		case SCROLLHORIZONTAL:
			scrollBarSpace =  (panelSize.width - upButtonImage.getIconWidth() - downButtonImage.getIconWidth());
			break;
		}
		double scrollBarSize = scrollBarSpace * ratio;
		this.scrollBarSize = (int) scrollBarSize;
		scrollBarPosition = 0;
		switch(scrollDirection) {
		case SCROLLVERTICAL:
			scrollBar.setBounds(0, scrollBarPosition + upButtonImage.getIconHeight(), panelSize.width, this.scrollBarSize);
			break;
		case SCROLLHORIZONTAL:
			scrollBar.setBounds(scrollBarPosition + upButtonImage.getIconWidth(), 0, this.scrollBarSize, panelSize.height);
			break;
		}
	}
	public void createScrollBar(int windowSize, int viewSize) {
		this.windowSize = windowSize;
		this.viewSize = viewSize;
		createScrollBar();
	}
	public void addScrollListener(ScrollListener listener) {
		listeners.add(listener);
	}
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
		if (viewSize != 0) {
			createScrollBar();
		}
	}
	public void setViewSize(int viewSize) {
		this.viewSize = viewSize;
		if (windowSize != 0) {
			createScrollBar();
		}
	}
	public void setButtonViewChange(int buttonViewChange) {
		this.buttonViewChange = buttonViewChange;
	}
	public void setBackgroundImage(String imageLocation) {
		backGroundImage = new ImageIcon(imageLocation);
		this.bgImage = backGroundImage;
	}
	private void moveScrollBar(int newPosition) {
    	int c = newPosition - clickOrigin;
    	scrollBarPosition += c;
    	if (scrollBarPosition < 0) {
    		scrollBarPosition = 0;
    	} else if (scrollBarPosition > (panelSize.height - downButtonImage.getIconHeight() - scrollBarSize - upButtonImage.getIconHeight()) && scrollDirection == SCROLLVERTICAL) {
    		scrollBarPosition = panelSize.height - downButtonImage.getIconHeight() - scrollBarSize - upButtonImage.getIconHeight();
    	} else if (scrollBarPosition > (panelSize.width - downButtonImage.getIconWidth() - scrollBarSize - upButtonImage.getIconWidth()) && scrollDirection == SCROLLHORIZONTAL) {
    		scrollBarPosition = panelSize.width - downButtonImage.getIconWidth() - scrollBarSize - upButtonImage.getIconWidth();
    	}
    	switch (scrollDirection) {
    	case SCROLLVERTICAL:
    		scrollBar.setBounds(0, scrollBarPosition + upButtonImage.getIconHeight(), panelSize.width, scrollBarSize);
    		break;
    	case SCROLLHORIZONTAL:
    		scrollBar.setBounds(scrollBarPosition + upButtonImage.getIconWidth(), 0, scrollBarSize, panelSize.height);
    		break;
    	}
    }
	public void setScrollBarPosition(int viewPosition) {
		this.viewPosition = viewPosition;
		updateScrollBar();
		updateButtons();
	}
	private int calculateWindowPosition() {
		double windowPosition = 0;
		double scrollBarSpace = 0;
		switch (scrollDirection) {
		case SCROLLVERTICAL:
			scrollBarSpace =  (panelSize.height - upButtonImage.getIconHeight() - downButtonImage.getIconHeight());
			break;
		case SCROLLHORIZONTAL:
			scrollBarSpace =  (panelSize.width - upButtonImage.getIconWidth() - downButtonImage.getIconWidth());
			break;
		}
		double ratio = ((double) scrollBarPosition / (double) scrollBarSpace);
		windowPosition = windowSize * ratio;
		return (int) windowPosition;
	}
	private void updateScrollBar() {
		double ratio = ((double) viewPosition / (double) windowSize);
		double scrollBarSpace = 0;
		switch (scrollDirection) {
		case SCROLLVERTICAL:
			scrollBarSpace =  (panelSize.height - upButtonImage.getIconHeight() - downButtonImage.getIconHeight());
			break;
		case SCROLLHORIZONTAL:
			scrollBarSpace =  (panelSize.width - upButtonImage.getIconWidth() - downButtonImage.getIconWidth());
			break;
		}
		double position = scrollBarSpace * ratio;
		if (position <= scrollBarSpace / 2) {
			scrollBarPosition = (int) Math.round(position);
		} else {
			scrollBarPosition = (int) Math.ceil(position);
		}
		switch (scrollDirection) {
    	case SCROLLVERTICAL:
    		scrollBar.setBounds(0, scrollBarPosition + upButtonImage.getIconHeight(), panelSize.width, scrollBarSize);
    		break;
    	case SCROLLHORIZONTAL:
    		scrollBar.setBounds(scrollBarPosition + upButtonImage.getIconWidth(), 0, scrollBarSize, panelSize.height);
    		break;
    	}
	}
	private void updateButtons() {
		upButtonLabel.setVisible(true);
		downButtonLabel.setVisible(true);
		if (scrollBarPosition == 0) {
			upButtonLabel.setVisible(false);
		} else if (scrollBarPosition == panelSize.height - downButtonImage.getIconHeight() - scrollBarSize - upButtonImage.getIconHeight() && scrollDirection == SCROLLVERTICAL) {
			downButtonLabel.setVisible(false);
		} else if (scrollBarPosition == panelSize.width - downButtonImage.getIconWidth() - scrollBarSize - upButtonImage.getIconWidth() && scrollDirection == SCROLLHORIZONTAL) {
			downButtonLabel.setVisible(false);
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == upButtonLabel) {
			viewPosition -= buttonViewChange;
			if (viewPosition < 0) {
				viewPosition = 0;
			}
			upButtonLabel.setIcon(upButtonPressedImage);
			upButtonPressed = true;
			updateScrollBar();
			updateButtons();
			ScrollEvent scrollEvent = new ScrollEvent(ID, viewPosition);
			for (ScrollListener sL : listeners) sL.viewChanged(scrollEvent);
		} else if (e.getSource() == downButtonLabel) {
			viewPosition += buttonViewChange;
			if (viewPosition > (windowSize - viewSize)) {
				viewPosition = windowSize - viewSize;
			}
			downButtonLabel.setIcon(downButtonPressedImage);
			downButtonPressed = true;
			updateScrollBar();
			updateButtons();
			ScrollEvent scrollEvent = new ScrollEvent(ID, viewPosition);
			for (ScrollListener sL : listeners) sL.viewChanged(scrollEvent);
		} else if (e.getSource() == scrollBar) {
			switch(scrollDirection) {
			case SCROLLVERTICAL:
				clickOrigin = e.getY();
				break;
			case SCROLLHORIZONTAL:
				clickOrigin = e.getX();
				break;
			}
			scrollBarPressed = true;
			scrollBar.bgImage = scrollBarPressedImage;
			scrollBar.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (upButtonPressed) {
			upButtonPressed = false;
			upButtonLabel.setIcon(upButtonImage);
		}
		if (downButtonPressed) {
			downButtonPressed = false;
			downButtonLabel.setIcon(downButtonImage);
		}
		if (scrollBarPressed) {
			scrollBarPressed = false;
			scrollBar.bgImage = scrollBarImage;
			scrollBar.repaint();
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if (scrollBarPressed) {
			int click = 0;
			switch(scrollDirection) {
			case SCROLLVERTICAL:
				click = e.getY();
				break;
			case SCROLLHORIZONTAL:
				click = e.getX();
				break;
			}
			moveScrollBar(click);
			updateButtons();
			ScrollEvent scrollEvent = new ScrollEvent(ID, viewPosition = calculateWindowPosition());
			for (ScrollListener sL : listeners) sL.viewChanged(scrollEvent);
		}
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
