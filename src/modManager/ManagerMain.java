package modManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import civilisationClicker.CivilisationMainClass;
import directoryFinder.DirectoryFinder;

public class ManagerMain {
	static JFrame frame;
	static JPanel mainPanel;
	static String installDirectory;
	static ManagerModList modListUI;
	static List<File> modList = new ArrayList<File>();
	static List<File> savedModList = new ArrayList<File>();
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	private static void createAndShowGUI() {
        //Create and set up the window.
		DirectoryFinder directoryFinder = new DirectoryFinder();
		installDirectory = directoryFinder.findOwnDirectory();
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBounds(0, 0, 400, 800);
		modListUI = new ManagerModList(ManagerModFinder.modFinder());
		modListUI.checkSavedMods(savedModList);
		savedModList = null;
		mainPanel.add(modListUI.mainPanel);
        frame = new JFrame("Civilisation Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 800 + 39);
        frame.setIconImage(new ImageIcon(installDirectory + "\\graphics\\icons\\city_game.png").getImage());
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(mainPanel);
    }
	static void saveModData() {
		ManagerXMLSaver.saveModData(modList);
		CivilisationMainClass.main(new String[] {});
		frame.dispose();
	}
}
