package modManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ManagerModFinder {
	static List<File> modFinder() {
		List<File> fileList = new ArrayList<File>();
		File modListFile = new File(ManagerMain.installDirectory + "\\data\\modlist.xml");
		if (modListFile.exists() && !modListFile.isDirectory()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder dBuilder;
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(modListFile);
			    doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("mod");
		        for (int i=0; i<nList.getLength(); i++) {
		        	Node nNode = nList.item(i);
		        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		        		File modFile = new File(ManagerMain.installDirectory + "\\mods\\" + nNode.getTextContent());
		        		if (modFile.exists()) {
		        			fileList.add(modFile);
		        			ManagerMain.savedModList.add(modFile);
		        		}
		        	}
		        }
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		File[] files = new File(ManagerMain.installDirectory + "\\mods\\").listFiles();
		for (int i=0; i<files.length; i++) {
			if (getFileExtension(files[i]).equals("xml") && !fileList.contains(files[i])) fileList.add(files[i]);
		}
		return fileList;
	}
	static String getFileExtension(File file) {
		String extension = "";
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		return extension;
	}
}
