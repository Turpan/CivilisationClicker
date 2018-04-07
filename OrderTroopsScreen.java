package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import paintedPanel.PaintedPanel;

public class OrderTroopsScreen extends PaintedPanel implements ActionListener, MouseListener, OrderUnitPanelListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean isAttacker;
	int playerID, province;
	int[] unitCount;
	JButton cancelButton, confirmButton;
	JPanel titlePanel, unitsPanel, buttonPanel, centralPanel, contentsPanel;
	JLabel titleLabel, armyPowerLabel, remainingPowerLabel;
	PaintedPanel northBorderPanel, southBorderPanel, westBorderPanel, eastBorderPanel;
	OrderListener listener;
	List<OrderUnitPanel> unitPanels = new ArrayList<OrderUnitPanel>();
	OrderTroopsScreen(int playerID, int province, boolean isAttacker) {
		this.playerID = playerID;
		this.province = province;
		this.isAttacker = isAttacker;
		createOrderTroopsScreen();
	}
	void createOrderTroopsScreen() {
		int positionX = (CivilisationMainClass.gameWidth - 900) / 2;
		int positionY = (CivilisationMainClass.gameHeight - 800) / 2;
		this.setBounds(positionX, positionY, 600, 800);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.bgImage = new ImageIcon("graphics/ui/order-background.png");
		centralPanel = new JPanel();
		centralPanel.setOpaque(false);
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.LINE_AXIS));
		centralPanel.setPreferredSize(new Dimension(600, 760));
		centralPanel.setMaximumSize(new Dimension(600, 760));
		northBorderPanel = new PaintedPanel();
		northBorderPanel.setOpaque(false);
		northBorderPanel.setPreferredSize(new Dimension(600, 20));
		northBorderPanel.setMaximumSize(new Dimension(600, 20));
		southBorderPanel = new PaintedPanel();
		southBorderPanel.setOpaque(false);
		southBorderPanel.setPreferredSize(new Dimension(600, 20));
		southBorderPanel.setMaximumSize(new Dimension(600, 20));
		westBorderPanel = new PaintedPanel();
		westBorderPanel.setOpaque(false);
		westBorderPanel.setPreferredSize(new Dimension(20, 760));
		westBorderPanel.setMaximumSize(new Dimension(20, 760));
		eastBorderPanel = new PaintedPanel();
		eastBorderPanel.setOpaque(false);
		eastBorderPanel.setPreferredSize(new Dimension(20, 760));
		eastBorderPanel.setMaximumSize(new Dimension(20, 760));
		contentsPanel = new JPanel();
		contentsPanel.setOpaque(false);
		contentsPanel.setLayout(new BoxLayout(contentsPanel, BoxLayout.PAGE_AXIS));
		contentsPanel.setPreferredSize(new Dimension(560, 760));
		contentsPanel.setMaximumSize(new Dimension(560, 760));
		titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.PAGE_AXIS));
		titlePanel.setPreferredSize(new Dimension(560, 50));
		unitsPanel = new JPanel();
		unitsPanel.setOpaque(false);
		unitsPanel.setLayout(new BoxLayout(unitsPanel, BoxLayout.PAGE_AXIS));
		unitsPanel.setPreferredSize(new Dimension(560, 700));
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setPreferredSize(new Dimension(560, 50));
		if (isAttacker) {
			titleLabel = new JLabel("Attacking Player: " + CivilisationMainClass.playerNames[playerID - 1]);
		} else {
			titleLabel = new JLabel("Defending Against: " + CivilisationMainClass.playerNames[playerID - 1]);
		}
		armyPowerLabel = new JLabel("Total Power: 0");
		remainingPowerLabel = new JLabel("Power Remaining: " + CivilisationMainClass.getPlayer().availableUnitPoints);
		titlePanel.add(titleLabel);
		titlePanel.add(armyPowerLabel);
		titlePanel.add(remainingPowerLabel);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		buttonPanel.add(confirmButton);
		contentsPanel.add(titlePanel);
		contentsPanel.add(unitsPanel);
		contentsPanel.add(buttonPanel);
		centralPanel.add(westBorderPanel);
		centralPanel.add(contentsPanel);
		centralPanel.add(eastBorderPanel);
		this.add(northBorderPanel);
		this.add(centralPanel);
		this.add(southBorderPanel);
		createUnitPanels();
	}
	void createUnitPanels() {
		initialiseArrays();
		int a = DataBase.unitList.size();
		for (int i=0; i<a; i++) {
			OrderUnitPanel unitPanel = new OrderUnitPanel(i);
			unitPanel.addListener(this);
			unitPanels.add(unitPanel);
			unitsPanel.add(unitPanel.unitPanel);
		}
		unitsPanel.add(Box.createVerticalGlue());
	}
	void updateLabels() {
		int power = 0;
		for (int i=0; i<DataBase.unitList.size(); i++) {
			unitPanels.get(i).updateLabels(unitCount[i]);
			power += unitCount[i] * CivilisationMainClass.getPlayer().unitList.get(i).Power;
		}
		armyPowerLabel.setText("Total Power: " + power);
		remainingPowerLabel.setText("Remaining Power: " + (CivilisationMainClass.getPlayer().availableUnitPoints - power));
	}
	void initialiseArrays() {
		int a = DataBase.unitList.size();
		unitCount = new int[a];
	}
	boolean checkEmpty() {
		int fullCount = 0;
		for (int i=0; i<DataBase.unitList.size(); i++) {
			if (unitCount[i] != 0) {
				fullCount += 1;
			}
		}
		return fullCount > 0;
	}
	void createBattle() {
		List<Unit> unitList = DataBase.createNewUnitList();
		for (int i=0; i<unitList.size(); i++) unitList.get(i).Count = unitCount[i];
		if (isAttacker) {
			CivilisationMainClass.battleList.createBattle(unitList, province, CivilisationMainClass.playerID, playerID);
		} else {
			CivilisationMainClass.battleList.joinBattle(unitList, province, playerID, CivilisationMainClass.playerID);
		}
	}
	void addListener(OrderListener listener) {
		this.listener = listener;
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
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		SoundEngine.playClickSound();
		if (e.getSource() == cancelButton) {
			listener.removeOrderScreen(this);
		} else if (e.getSource() == confirmButton) {
			if (!checkEmpty()) {
				listener.removeOrderScreen(this);
				createBattle();
			}
		}
	}
	@Override
	public void upButton(int ID) {
		unitCount[ID] += 1;
		if (unitCount[ID] > CivilisationMainClass.getPlayer().unitList.get(ID).Available) {
			unitCount[ID] = CivilisationMainClass.getPlayer().unitList.get(ID).Available;
		}
		updateLabels();
	}
	@Override
	public void downButton(int ID) {
		unitCount[ID] -= 1;
		if (unitCount[ID] < 0) {
			unitCount[ID] = 0;
		}
		updateLabels();
	}
	@Override
	public void textField(int ID, int count) {
		unitCount[ID] = count;
		if (unitCount[ID] > CivilisationMainClass.getPlayer().unitList.get(ID).Available) {
			unitCount[ID] = CivilisationMainClass.getPlayer().unitList.get(ID).Available;
		} else if (unitCount[ID] < 0) {
			unitCount[ID] = 0;
		}
		updateLabels();
		return;
	}
}
interface OrderListener {
	void removeOrderScreen(OrderTroopsScreen screen);
}
interface OrderUnitPanelListener {
	void upButton(int ID);
	void downButton(int ID);
	void textField(int ID, int count);
}
class OrderUnitPanel implements ActionListener, MouseListener{
	int ID;
	PaintedPanel unitGridPanel;
	JPanel unitPanel;
	JPanel unitButtonPanel;
	JPanel unitTextFieldPanel;
	JPanel unitTextPanel;
	JPanel unitNamePanel;
	JLabel unitNameLabel;
	JLabel unitIconLabel;
	JLabel unitUpButton;
	JLabel unitDownButton;
	JTextField unitCountField;
	List<OrderUnitPanelListener> listeners = new ArrayList<OrderUnitPanelListener>();
	OrderUnitPanel(int ID) {
		this.ID = ID;
		createGraphics();
	}
	void createGraphics() {
		Unit unit = DataBase.unitList.get(ID);
		unitPanel = new JPanel();
		unitPanel.setOpaque(false);
		unitPanel.setLayout(new BoxLayout(unitPanel, BoxLayout.LINE_AXIS));
		unitPanel.setPreferredSize(new Dimension(560, 100));
		unitPanel.setMaximumSize(new Dimension(560, 100));
		unitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		unitGridPanel = new PaintedPanel();
		unitGridPanel.bgImage = new ImageIcon("graphics/ui/order-square.png");
		unitGridPanel.setLayout(new BoxLayout(unitGridPanel, BoxLayout.LINE_AXIS));
		unitGridPanel.setPreferredSize(new Dimension(100, 100));
		unitGridPanel.setMaximumSize(new Dimension(100, 100));
		unitButtonPanel = new JPanel();
		unitButtonPanel.setOpaque(false);
		unitButtonPanel.setLayout(new BoxLayout(unitButtonPanel, BoxLayout.PAGE_AXIS));
		unitTextFieldPanel = new JPanel();
		unitTextFieldPanel.setOpaque(false);
		unitTextFieldPanel.setLayout(new GridBagLayout());
		unitTextFieldPanel.setPreferredSize(new Dimension(50, 50));
		unitTextFieldPanel.setMaximumSize(new Dimension(50, 50));
		unitTextPanel = new JPanel();
		unitTextPanel.setOpaque(false);
		unitTextPanel.setLayout(new BoxLayout(unitTextPanel, BoxLayout.PAGE_AXIS));
		unitNamePanel = new JPanel(new GridBagLayout());
		unitNamePanel.setOpaque(false);
		unitNamePanel.setPreferredSize(new Dimension(50, 50));
		unitNamePanel.setMaximumSize(new Dimension(50, 50));
		unitNameLabel = new JLabel(unit.Name);
		ImageIcon icon = new ImageIcon(unit.Icon);
		unitIconLabel = new JLabel(icon);
		unitUpButton = new JLabel(new ImageIcon("graphics/ui/arrow-up.png"));
		unitUpButton.addMouseListener(this);
		unitDownButton = new JLabel(new ImageIcon("graphics/ui/arrow-down.png"));
		unitDownButton.addMouseListener(this);
		unitCountField = new JTextField("0");
		unitCountField.setOpaque(false);
		unitCountField.setColumns(5);
		unitCountField.setMinimumSize(new Dimension(50, 50));
		unitCountField.addActionListener(this);
		unitNamePanel.add(unitNameLabel);
		unitTextFieldPanel.add(unitCountField);
		unitTextPanel.add(unitNamePanel);
		unitTextPanel.add(unitTextFieldPanel);
		unitButtonPanel.add(unitUpButton);
		unitButtonPanel.add(unitDownButton);
		unitGridPanel.add(unitTextPanel);
		unitGridPanel.add(unitButtonPanel);
		unitPanel.add(unitIconLabel);
		unitPanel.add(Box.createHorizontalGlue());
		unitPanel.add(unitGridPanel);
	}
	void updateLabels(int unitCount) {
		unitCountField.setText(Integer.toString(unitCount));
	}
	void addListener(OrderUnitPanelListener listener) {
		listeners.add(listener);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		SoundEngine.playClickSound();
		if (e.getSource() == unitUpButton) {
			for (OrderUnitPanelListener listener : listeners) listener.upButton(ID);
		} else if (e.getSource() == unitDownButton) {
			for (OrderUnitPanelListener listener : listeners) listener.downButton(ID);
		}
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
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == unitCountField) {
			String field = unitCountField.getText();
			int count = MathFunctions.parseInt(field);
			for (OrderUnitPanelListener listener : listeners) listener.textField(ID, count);
		}
	}
}
