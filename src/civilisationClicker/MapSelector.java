package civilisationClicker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MapSelector extends JPanel implements ActionListener, ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	int numberOfMaps;
	String[] mapNames;
	JLabel mapLabel;
	JPanel selectorPanel;
	JComboBox<String> mapSelector;
	JButton selectButton;
	ImageIcon[] mapImages;
	MapSelector() {
		loadMaps();
		createMapSelector();
	}
	void createMapSelector() {
		int positionx = (CivilisationMainClass.gameWidth - WIDTH) / 2;
		int positiony = (CivilisationMainClass.gameHeight - HEIGHT) / 2;
		this.setBounds(positionx, positiony, WIDTH, HEIGHT);
		this.setLayout(null);
		mapLabel = new JLabel();
		mapLabel.setBounds(0, 20, WIDTH, HEIGHT - 20);
		mapLabel.setIcon(mapImages[0]);
		selectorPanel = new JPanel();
		selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.LINE_AXIS));
		selectorPanel.setBounds(0, 0, WIDTH, 20);
		mapSelector = new JComboBox<String>(mapNames);
		mapSelector.setSize(200, 20);
		mapSelector.setSelectedIndex(0);
		mapSelector.addItemListener(this);
		selectButton = new JButton("Select Map");
		selectButton.setActionCommand("selectMap");
		selectButton.addActionListener(this);
		selectorPanel.add(mapSelector);
		selectorPanel.add(Box.createHorizontalGlue());
		selectorPanel.add(selectButton);
		this.add(selectorPanel);
		this.add(mapLabel);
	}
	void loadMaps() {
		File mapFile = new File("data/province maps/maplist.txt");
		try {
			String content;
			Scanner scan = new Scanner(mapFile);
			scan.useDelimiter("/Z");
			content = scan.next();
			scan.close();
			content = content.replaceAll("[\r\n]+", "");
			Scanner mapFileScanner = new Scanner(content);
			mapFileScanner.useDelimiter(";");
			numberOfMaps = mapFileScanner.nextInt();
			initialiseVariables();
			for (int i=0; i<numberOfMaps; i++) {
				mapNames[i] = mapFileScanner.next();
				mapImages[i] = new ImageIcon("data/province maps/" + mapNames[i] + "-mini.png");
			}
			mapFileScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void initialiseVariables() {
		mapNames = new String[numberOfMaps];
		mapImages = new ImageIcon[numberOfMaps];
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "selectMap") {
			SoundEngine.playClickSound();
			CivilisationMainClass.mapSelected(mapSelector.getSelectedIndex());
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getStateChange() == ItemEvent.SELECTED) {
			SoundEngine.playClickSound();
			mapLabel.setIcon(mapImages[mapSelector.getSelectedIndex()]);
			this.revalidate();
		}
	}
}
