package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import provinceGenerator.ProvinceLoader;
//Consider adding a timer, to make the color change say half a second after typing stops.
public class ColourSelector extends JPanel implements ActionListener, ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	int numberOfColours, selectedRed, selectedGreen, selectedBlue;
	int[] colourRed, colourGreen, colourBlue;
	String[] colourNames;
	JPanel selectorPanel, textFieldPanel;
	JButton selectButton;
	JTextField redField, greenField, blueField;
	JComboBox<String> colourSelector;
	ProvinceLoader previewMap;
	ColourSelector() {
		loadColours();
		createColourSelector();
	}
	void createColourSelector() {
		int positionx = (CivilisationMainClass.gameWidth - WIDTH) / 2;
		int positiony = (CivilisationMainClass.gameHeight - HEIGHT) / 2;
		this.setBounds(positionx, positiony, WIDTH, HEIGHT);
		this.setLayout(null);
		previewMap = new ProvinceLoader("preview", "data/province maps/", 1, new Dimension(400, 260));
		List<Color> colorList = new ArrayList<Color>();
		colorList.add(new Color(255, 0, 220));
		colorList.add(new Color(0, 0, 0));
		previewMap.setColorList(colorList);
		previewMap.loadProvinces();
		previewMap.mainPanel.setBounds(0, 40, WIDTH, HEIGHT - 40);
		selectorPanel = new JPanel();
		selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.LINE_AXIS));
		selectorPanel.setBounds(0, 0, WIDTH, 20);
		textFieldPanel = new JPanel();
		textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.LINE_AXIS));
		textFieldPanel.setBounds(0, 20, WIDTH, 20);
		colourSelector = new JComboBox<String>(colourNames);
		colourSelector.setSize(new Dimension(200, 20));
		colourSelector.addItem("Custom");
		colourSelector.setSelectedIndex(0);
		colourSelector.addItemListener(this);
		selectButton = new JButton("Confirm");
		selectButton.addActionListener(this);
		selectButton.setActionCommand("selectcolour");
		redField = new JTextField(Integer.toString(colourRed[0]));
		redField.setColumns(4);
		redField.setMaximumSize(new Dimension(40, 20));
		redField.addActionListener(this);
		greenField = new JTextField(Integer.toString(colourGreen[0]));
		greenField.setColumns(4);
		greenField.setMaximumSize(new Dimension(40, 20));
		greenField.addActionListener(this);
		blueField = new JTextField(Integer.toString(colourBlue[0]));
		blueField.setColumns(4);
		blueField.setMaximumSize(new Dimension(40, 20));
		blueField.addActionListener(this);
		selectorPanel.add(colourSelector);
		selectorPanel.add(Box.createHorizontalGlue());
		selectorPanel.add(selectButton);
		textFieldPanel.add(redField);
		textFieldPanel.add(greenField);
		textFieldPanel.add(blueField);
		textFieldPanel.add(Box.createHorizontalGlue());
		this.add(selectorPanel);
		this.add(textFieldPanel);
		this.add(previewMap.mainPanel);
	}
	void loadColours() {
		File colourFile = new File("data/colours.txt");
		try {
			String content;
			Scanner scan = new Scanner(colourFile);
			scan.useDelimiter("\\Z");
			content = scan.next();
			scan.close();
			content = content.replaceAll("[\r\n]+", "");
			Scanner colourFileScanner = new Scanner(content);
			colourFileScanner.useDelimiter(";");
			numberOfColours = colourFileScanner.nextInt();
			initialiseVariables();
			for (int i=0; i<numberOfColours; i++) {
				colourFileScanner.next();
				colourNames[i] = colourFileScanner.next();
				colourRed[i] = colourFileScanner.nextInt();
				colourGreen[i] = colourFileScanner.nextInt();
				colourBlue[i] = colourFileScanner.nextInt();
			}
			colourFileScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void initialiseVariables() {
		colourNames = new String[numberOfColours];
		colourRed = new int[numberOfColours];
		colourGreen = new int[numberOfColours];
		colourBlue = new int[numberOfColours];
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == redField || e.getSource() == greenField || e.getSource() == blueField) {
			colourSelector.setSelectedIndex(numberOfColours);
			int red = 0;
			int green = 0;
			int blue = 0;
			String redString = redField.getText();
			String greenString = greenField.getText();
			String blueString = blueField.getText();
			try {
				red = Integer.parseInt(redString);
			} catch (NumberFormatException f) {
				red = 0;
			} catch (NullPointerException f) {
				red = 0;
			}
			try {
				green = Integer.parseInt(greenString);
			} catch (NumberFormatException f) {
				green = 0;
			} catch (NullPointerException f) {
				green = 0;
			}
			try {
				blue = Integer.parseInt(blueString);
			} catch (NumberFormatException f) {
				blue = 0;
			} catch (NullPointerException f) {
				blue = 0;
			}
			if (red > 255) {
				red = 255;
			}
			if (green > 255) {
				green = 255;
			}
			if (blue > 255) {
				blue = 255;
			}
			selectedRed = red;
			selectedGreen = green;
			selectedBlue = blue;
			previewMap.colourProvince(0, new Color(selectedRed, selectedGreen, selectedBlue));
			this.revalidate();
		} else if (e.getSource() == selectButton) {
			SoundEngine.playClickSound();
			CivilisationMainClass.lobbyScreen.colourSelected(selectedRed, selectedGreen, selectedBlue);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			SoundEngine.playClickSound();
			if (colourSelector.getSelectedIndex() != numberOfColours) {
				int a = colourSelector.getSelectedIndex();
				redField.setText(Integer.toString(colourRed[a]));
				greenField.setText(Integer.toString(colourGreen[a]));
				blueField.setText(Integer.toString(colourBlue[a]));
				selectedRed = colourRed[a];
				selectedGreen = colourGreen[a];
				selectedBlue = colourBlue[a];
				previewMap.colourProvince(0, new Color(colourRed[a], colourGreen[a], colourBlue[a]));
				this.revalidate();
			}
		}
	}

}
