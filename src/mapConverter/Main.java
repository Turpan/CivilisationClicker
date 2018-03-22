package mapConverter;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {
	static String mapFile = "C:\\Users\\Amy\\eclipse-workspace\\CivilisationClicker\\data\\province maps\\europe-data.txt";
	static String xmlFile = "C:\\Users\\Amy\\eclipse-workspace\\CivilisationClicker\\data\\province maps\\europe.xml";
	static List<Color> colorList = new ArrayList<Color>();
	public static void main(String[] args) {
		loadMapData();
		parseXML();
		System.out.println("All done.");
		System.exit(0);
	}
	static void loadMapData() {
		File map = new File(mapFile);
		try {
			FileInputStream fs = new FileInputStream(map);
			BufferedReader mapReader = new BufferedReader(new InputStreamReader(fs));
			String line = mapReader.readLine();
			Scanner scan = new Scanner(line);
			scan.useDelimiter(";");
			int lineCount = scan.nextInt();
			scan.close();
			for (int i=0; i<lineCount; i++) {
				line = mapReader.readLine();
				Scanner lineReader = new Scanner(line);
				lineReader.useDelimiter(";");
				lineReader.next();
				int red = lineReader.nextInt();
				int green = lineReader.nextInt();
				int blue = lineReader.nextInt();
				lineReader.close();
				Color provinceColor = new Color(red, green, blue);
				colorList.add(provinceColor);
			}
			mapReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void parseXML() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("provinces");
			doc.appendChild(rootElement);
			int i=0;
			for (Color color : colorList) {
				Element provinceNode = doc.createElement("province");
				provinceNode.setAttribute("ID", i + "");
				Element redNode = doc.createElement("red");
				redNode.appendChild(doc.createTextNode(color.getRed() + ""));
				Element greenNode = doc.createElement("green");
				greenNode.appendChild(doc.createTextNode(color.getGreen() + ""));
				Element blueNode = doc.createElement("blue");
				blueNode.appendChild(doc.createTextNode(color.getBlue() + ""));
				provinceNode.appendChild(redNode);
				provinceNode.appendChild(greenNode);
				provinceNode.appendChild(blueNode);
				rootElement.appendChild(provinceNode);
				i++;
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", Integer.valueOf(2));
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlFile));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
