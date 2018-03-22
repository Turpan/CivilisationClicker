package modManager;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import paintedPanel.PaintedPanel;

public class ManagerModList implements MouseListener, ItemListener{

	JPanel mainPanel, modMainPanel, titlePanel, buttonPanel;
	JPanel[] modPanels, modButtonPanel;
	JLabel titleLabel;
	JLabel[] modTitleLabels;
	JCheckBox[] modCheckBox;
	JButton launchButton;
	PaintedPanel[] modUpButton, modDownButton;
	ImageIcon upButtonImage, downButtonImage, upButtonPressedImage, downButtonPressedImage;
	int[] modOrder;
	boolean[] modSelected, modUpPressed, modDownPressed;
	List<File> fileList;
	ManagerModList(List<File> fileList) {
		this.fileList = fileList;
		initialiseArrays(fileList.size());
		createInterface();
	}
	void createInterface() {
		upButtonImage = new ImageIcon(ManagerMain.installDirectory + "\\graphics\\buttons\\modmanagerup.png");
		upButtonPressedImage = new ImageIcon(ManagerMain.installDirectory + "\\graphics\\buttons\\modmanageruppressed.png");
		downButtonImage = new ImageIcon(ManagerMain.installDirectory + "\\graphics\\buttons\\modmanagerdown.png");
		downButtonPressedImage = new ImageIcon(ManagerMain.installDirectory + "\\graphics\\buttons\\modmanagerdownpressed.png");
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setPreferredSize(new Dimension(400, 800));
		mainPanel.setMaximumSize(new Dimension(400, 800));
		titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setPreferredSize(new Dimension(400, 20));
		titlePanel.setMaximumSize(new Dimension(400, 20));
		modMainPanel = new JPanel();
		modMainPanel.setLayout(new BoxLayout(modMainPanel, BoxLayout.PAGE_AXIS));
		modMainPanel.setPreferredSize(new Dimension(400, 760));
		modMainPanel.setMaximumSize(new Dimension(400, 760));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setPreferredSize(new Dimension(400, 20));
		buttonPanel.setMaximumSize(new Dimension(400, 20));
		titleLabel = new JLabel("Mod List");
		launchButton = new JButton("Launch Game");
		launchButton.addMouseListener(this);
		titlePanel.add(titleLabel);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(launchButton);
		mainPanel.add(titlePanel);
		mainPanel.add(modMainPanel);
		mainPanel.add(buttonPanel);
		for (int i=0; i<fileList.size(); i++) {
			modPanels[i] = new JPanel();
			modPanels[i].setLayout(new BoxLayout(modPanels[i], BoxLayout.LINE_AXIS));
			modPanels[i].setPreferredSize(new Dimension(400, 20));
			modPanels[i].setMaximumSize(new Dimension(400, 20));
			modButtonPanel[i] = new JPanel();
			modButtonPanel[i].setLayout(new BoxLayout(modButtonPanel[i], BoxLayout.PAGE_AXIS));
			modButtonPanel[i].setPreferredSize(new Dimension(10, 20));
			modButtonPanel[i].setMaximumSize(new Dimension(10, 20));
			modTitleLabels[i] = new JLabel(fileList.get(i).getName());
			modCheckBox[i] = new JCheckBox();
			modCheckBox[i].addItemListener(this);
			modOrder[i] = i+1;
			modUpButton[i] = new PaintedPanel();
			modUpButton[i].bgImage = upButtonImage;
			modUpButton[i].addMouseListener(this);
			modUpButton[i].setPreferredSize(new Dimension(10, 10));
			modUpButton[i].setMaximumSize(new Dimension(10, 10));
			modDownButton[i] = new PaintedPanel();
			modDownButton[i].bgImage = downButtonImage;
			modDownButton[i].addMouseListener(this);
			modDownButton[i].setPreferredSize(new Dimension(10, 10));
			modDownButton[i].setMaximumSize(new Dimension(10, 10));
			modButtonPanel[i].add(modUpButton[i]);
			modButtonPanel[i].add(modDownButton[i]);
			modPanels[i].add(modTitleLabels[i]);
			modPanels[i].add(Box.createHorizontalGlue());
			modPanels[i].add(modButtonPanel[i]);
			modPanels[i].add(modCheckBox[i]);
			modMainPanel.add(modPanels[i]);
		}
	}
	void initialiseArrays(int a) {
		modPanels = new JPanel[a];
		modButtonPanel = new JPanel[a];
		modTitleLabels = new JLabel[a];
		modCheckBox = new JCheckBox[a];
		modUpButton = new PaintedPanel[a];
		modDownButton = new PaintedPanel[a];
		modOrder = new int[a];
		modSelected = new boolean[a];
		modUpPressed = new boolean[a];
		modDownPressed = new boolean[a];
	}
	void checkSavedMods(List<File> savedModList) {
		for (int i=0; i<savedModList.size(); i++) {
			modCheckBox[i].setSelected(true);
			modCheckBox[i].repaint();
			modSelected[i] = true;
		}
	}
	void saveModData() {
		int a = 1;
		for (int i=0; i<fileList.size(); i++) {
			if (modSelected[i] && modOrder[i] == a) {
				ManagerMain.modList.add(fileList.get(i));
				a += 1;
				i = -1;
			}
		}
		ManagerMain.saveModData();
	}
	void restructureModList() {
		modMainPanel.removeAll();
		int a = 1;
		for (int i=0; i<fileList.size(); i++) {
			if (modOrder[i] == a) {
				modMainPanel.add(modPanels[i]);
				a += 1;
				i = -1;
			}
		}
		modMainPanel.revalidate();
		modMainPanel.repaint();
	}
	public void modUp(int mod) {
		if (modOrder[mod] != 1) {
			int a = modOrder[mod];
			int b = 0;
			for (int i=0; i<fileList.size(); i++) {
				if (modOrder[i] == a-1) {
					b = i;
					i = fileList.size();
				}
			}
			modOrder[b] = a;
			modOrder[mod] = a-1;
			restructureModList();
		}
	}
	public void modDown(int mod) {
		if (modOrder[mod] != fileList.size()) {
			int a = modOrder[mod];
			int b = 0;
			for (int i=0; i<fileList.size(); i++) {
				if (modOrder[i] == a+1) {
					b = i;
					i = fileList.size();
				}
			}
			modOrder[b] = a;
			modOrder[mod] = a+1;
			restructureModList();
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
		for (int i=0; i<fileList.size(); i++) {
			if (e.getSource() == modUpButton[i]) {
				modUpPressed[i] = true;
				modUpButton[i].bgImage = upButtonPressedImage;
				modUpButton[i].repaint();
				i = fileList.size();
			} else if (e.getSource() == modDownButton[i]) {
				modDownPressed[i] = true;
				modDownButton[i].bgImage = downButtonPressedImage;
				modDownButton[i].repaint();
				i = fileList.size();
			}
		}
		if (e.getSource() == launchButton) {
			saveModData();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		for (int i=0; i<fileList.size(); i++) {
			if (modUpPressed[i]) {
				modUpPressed[i] = false;
				modUpButton[i].bgImage = upButtonImage;
				modUpButton[i].repaint();
				modUp(i);
			}
			if (modDownPressed[i]) {
				modDownPressed[i] = false;
				modDownButton[i].bgImage = downButtonImage;
				modDownButton[i].repaint();
				modDown(i);
			}
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		for (int i=0; i<fileList.size(); i++) {
			if (e.getSource() == modCheckBox[i]) {
				modSelected[i] = !modSelected[i];
			}
		}
	}
}
