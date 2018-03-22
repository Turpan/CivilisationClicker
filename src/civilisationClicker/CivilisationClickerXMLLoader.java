package civilisationClicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CivilisationClickerXMLLoader {
	static List<CivilisationClickerBuildingList> buildingList = new ArrayList<CivilisationClickerBuildingList>();
	static List<CivilisationClickerResearchList> researchList = new ArrayList<CivilisationClickerResearchList>();
	static List<CivilisationClickerEdict> edictList = new ArrayList<CivilisationClickerEdict>();
	static List<CivilisationClickerUnit> unitList = new ArrayList<CivilisationClickerUnit>();
	static List<CivilisationClickerMap> mapList = new ArrayList<CivilisationClickerMap>();
	static Set<String> screenTypes = new LinkedHashSet<String>();
	static void loadXMLData() {
		loadXMLFileData(findGameFiles());
		loadXMLFileData(findModFiles());
	}
	static List<File> findModFiles() {
		List<File> fileList = new ArrayList<File>();
		File modListFile = new File("data/modlist.xml");
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
		        		File modFile = new File("mods/" + nNode.getTextContent());
		        		if (modFile.exists()) {
		        			fileList.add(modFile);
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
		return fileList;
	}
	static List<File> findGameFiles() {
		List<File> results = new ArrayList<File>();
		try {
			File inputFile = new File("data/developerfiles.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
		    doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("file");
	        for (int i=0; i<nList.getLength(); i++) {
	        	Node nNode = nList.item(i);
	        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        		File devFile = new File("data/" + nNode.getTextContent());
	        		results.add(devFile);
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
		return results;
	}
	static void loadXMLFileData(List<File> files) {
		for (File inputFile : files) {
			try {
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);
		        doc.getDocumentElement().normalize();
		        NodeList nList = doc.getElementsByTagName("buildings");
		        for (int i=0; i<nList.getLength(); i++) {
		        	Node nNode = nList.item(i);
		        	NamedNodeMap attributes = nNode.getAttributes();
		        	Node attribute = attributes.item(0);
		        	CivilisationClickerBuildingList buildingList = new CivilisationClickerBuildingList(attribute.getTextContent());
		        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		        		Element elem = (Element) nNode;
		        		NodeList buildingNodes = elem.getElementsByTagName("building");
		        		for (int j=0; j<buildingNodes.getLength(); j++) {
		        			NodeList buildingData = buildingNodes.item(j).getChildNodes();
		        			NamedNodeMap buildingIDs = buildingNodes.item(j).getAttributes();
		        			CivilisationClickerBuilding building = new CivilisationClickerBuilding();
		        			Node ID = buildingIDs.item(0);
		        			building.setID(ID.getTextContent());
		        			int a = 0;
		        			int b = 0;
		        			boolean buildingFound = false;
		        			for (CivilisationClickerBuildingList buildingsList : CivilisationClickerXMLLoader.buildingList) {
		        				if (buildingsList.buildingList.contains(building)) {
		        					buildingFound = true;
		        					for (CivilisationClickerBuilding retrievedbuilding : buildingsList.buildingList) {
		        						if (retrievedbuilding.equals(building)) break;
		        						b += 1;
		        					}
		        					break;
		        				}
		        				a += 1;
		        			}
		        			for (int k=0; k<buildingData.getLength(); k++) {
		        				Node data = buildingData.item(k);
		        				String dataType = data.getNodeName();
		        				switch (dataType) {
		        				case "name":
		        					if (!buildingFound) building.setName(data.getTextContent());
		        					else CivilisationClickerXMLLoader.buildingList.get(a).buildingList.get(b).setName(data.getTextContent());
		        					break;
		        				case "cost":
		        					if (!buildingFound) building.setCost(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
		        					else CivilisationClickerXMLLoader.buildingList.get(a).buildingList.get(b).setCost(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
		        					break;
		        				case "per-second":
		        					if (!buildingFound) building.setPointsPerSecond(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
		        					else CivilisationClickerXMLLoader.buildingList.get(a).buildingList.get(b).setPointsPerSecond(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
		        					break;
		        				case "unrest":
		        					if (!buildingFound) building.setUnrest(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
		        					else CivilisationClickerXMLLoader.buildingList.get(a).buildingList.get(b).setUnrest(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
		        					break;
		        				case "image":
		        					if (!buildingFound) building.setImageLocation(data.getTextContent());
		        					else CivilisationClickerXMLLoader.buildingList.get(a).buildingList.get(b).setImageLocation(data.getTextContent());
		        					break;
		        				case "bar":
		        					if (!buildingFound) building.setBarImageLocation(data.getTextContent());
		        					else CivilisationClickerXMLLoader.buildingList.get(a).buildingList.get(b).setBarImageLocation(data.getTextContent());
		        					break;
		        				case "icon":
		        					if (!buildingFound) building.setIconImageLocation(data.getTextContent());
		        					else CivilisationClickerXMLLoader.buildingList.get(a).buildingList.get(b).setIconImageLocation(data.getTextContent());
		        					break;
		        				}
		        			}
		        			if (!buildingFound) buildingList.addBuilding(building);
		        		}
		        	}
		        	if (buildingList.getListSize() > 0) {
		        		CivilisationClickerXMLLoader.buildingList.add(buildingList);
		        	}
		        }
		        nList = doc.getElementsByTagName("screentypes");
		        for (int i=0; i<nList.getLength(); i++) {
		        	Node nNode = nList.item(i);
		        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		        		Element elem = (Element) nNode;
		        		NodeList screenTypeNodes = elem.getElementsByTagName("screentype");
		        		for (int j=0; j<screenTypeNodes.getLength(); j++) {
		        			NodeList screenData = screenTypeNodes.item(j).getChildNodes();
		        			for (int k=0; k<screenData.getLength(); k++) {
		        				Node data = screenData.item(k);
		        				String dataType = data.getNodeName();
		        				switch (dataType) {
		        				case "name":
		        					screenTypes.add(data.getTextContent());
		        					break;
		        				}
		        			}
		        		}
		        	}
		        }
		        nList = doc.getElementsByTagName("upgrades");
		        for (int i=0; i<nList.getLength(); i++) {
		        	Node nNode = nList.item(i);
		        	if (nNode.hasAttributes() && nNode.getNodeType() == Node.ELEMENT_NODE) {
		        		Element elem = (Element) nNode;
		        		NodeList researchNodes = elem.getElementsByTagName("research");
		        		NamedNodeMap researchListIDs = nNode.getAttributes();
		        		CivilisationClickerResearchList workingResearchList = new CivilisationClickerResearchList(researchListIDs.item(0).getTextContent());
		        		for (int j=0; j<researchNodes.getLength(); j++) {
				        	Node researchData = researchNodes.item(j);
				        	if (researchData.getNodeType() == Node.ELEMENT_NODE && researchData.hasAttributes()) {
				        		NodeList researchDataNodes = researchData.getChildNodes();
			        			NamedNodeMap researchIDs = researchData.getAttributes();
			        			CivilisationClickerResearch research = new CivilisationClickerResearch();
			        			Node ID = researchIDs.item(0);
			        			research.setID(ID.getTextContent());
			        			int a = 0;
			        			int b = 0;
			        			boolean researchFound = false;
			        			for (CivilisationClickerResearchList researchsList : CivilisationClickerXMLLoader.researchList) {
			        				if (researchsList.researchList.contains(research)) {
			        					researchFound = true;
			        					for (CivilisationClickerResearch retrievedresearch : researchsList.researchList) {
			        						if (retrievedresearch.equals(research)) break;
			        						b += 1;
			        					}
			        					break;
			        				}
			        				a += 1;
			        			}
			        			for (int k=0; k<researchDataNodes.getLength(); k++) {
			        				Node data = researchDataNodes.item(k);
			        				String dataType = data.getNodeName();
			        				switch (dataType) {
			        				case "building":
			        					if (!researchFound) research.setBuilding(data.getTextContent());
			        					else researchList.get(a).researchList.get(b).setBuilding(data.getTextContent());
			        					break;
			        				case "name":
			        					if (!researchFound) research.setName(data.getTextContent());
			        					else researchList.get(a).researchList.get(b).setName(data.getTextContent());
			        					break;
			        				case "cost":
			        					if (!researchFound) research.setCost(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
			        					else researchList.get(a).researchList.get(b).setCost(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
			        					break;
			        				case "effect":
			        					if (!researchFound) research.setEffect(data.getTextContent());
			        					else researchList.get(a).researchList.get(b).setEffect(data.getTextContent());
			        					break;
			        				case "value":
			        					if (!researchFound) research.setValue(CivilisationClickerMathFunctions.parseDouble(data.getTextContent()));
			        					else researchList.get(a).researchList.get(b).setValue(CivilisationClickerMathFunctions.parseDouble(data.getTextContent()));
			        					break;
			        				case "purchased":
			        					if (!researchFound) research.setPurchased(Boolean.parseBoolean(data.getTextContent()));
			        					else researchList.get(a).researchList.get(b).setPurchased(Boolean.parseBoolean(data.getTextContent()));
			        					break;
			        				case "required":
			        					if (!researchFound) research.setRequired(data.getTextContent());
			        					else researchList.get(a).researchList.get(b).setRequired(data.getTextContent());
			        					break;
			        				case "weight":
			        					if (!researchFound) research.setWeight(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
			        					else researchList.get(a).researchList.get(b).setWeight(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
			        					break;
			        				}
			        			}
			        			if (!researchFound) workingResearchList.addResearch(research);
				        	}
				        }
		        		if (workingResearchList.getListSize() > 0) researchList.add(workingResearchList);
		        	}
		        }
		        nList = doc.getElementsByTagName("map");
	        	for (int i=0; i<nList.getLength(); i++) {
	        		Node nNode = nList.item(i);
	        		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        			if (nNode.hasAttributes()) {
		        			NodeList mapData = nNode.getChildNodes();
		        			NamedNodeMap mapIDs = nNode.getAttributes();
		        			CivilisationClickerMap map = new CivilisationClickerMap(mapIDs.item(0).getTextContent());
		        			for (int j=0; j<mapData.getLength(); j++) {
		        				Node data = mapData.item(j);
		        				String datatype = data.getNodeName();
		        				switch (datatype) {
		        				case "mapfile":
		        					map.setMapFile(data.getTextContent());
		        					break;
		        				case "provinces":
		        					Element elem = (Element) data;
		        					NodeList provinceData = elem.getElementsByTagName("province");
		        					for (int k=0; k<provinceData.getLength(); k++) {
		        						Node provinceNode = provinceData.item(k);
		        						NodeList provinceNodeData = provinceNode.getChildNodes();
		        						NamedNodeMap provinceIDs = provinceNode.getAttributes();
		        						CivilisationClickerProvince province = new CivilisationClickerProvince(CivilisationClickerMathFunctions.parseInt(provinceIDs.item(0).getTextContent()));
		        						for (int h=0; h<provinceNodeData.getLength(); h++) {
		        							Node provinceNodeNode = provinceNodeData.item(h);
		        							String provincedatatype = provinceNodeNode.getNodeName();
		        							switch (provincedatatype) {
		        							case "red":
		        								province.setRed(CivilisationClickerMathFunctions.parseInt(provinceNodeNode.getTextContent()));
		        								break;
		        							case "green":
		        								province.setGreen(CivilisationClickerMathFunctions.parseInt(provinceNodeNode.getTextContent()));
		        								break;
		        							case "blue":
		        								province.setBlue(CivilisationClickerMathFunctions.parseInt(provinceNodeNode.getTextContent()));
		        								break;
		        							}
		        						}
		        						map.addProvince(province);
		        					}
		        					break;
		        				}
		        			}
		        			mapList.add(map);
	        			}
	        		}
	        	}
	        	nList = doc.getElementsByTagName("edict");
	        	for (int i=0; i<nList.getLength(); i++) {
	        		Node nNode = nList.item(i);
	        		if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.hasAttributes()) {
	        			NodeList edictData = nNode.getChildNodes();
	        			NamedNodeMap edictIDs = nNode.getAttributes();
	        			CivilisationClickerEdict edict = new CivilisationClickerEdict(edictIDs.item(0).getTextContent());
	        			boolean edictFound = false;
	        			int a = 0;
	        			if (edictList.contains(edict)) {
	        				a = edictList.indexOf(edict);
	        				edictFound = true;
	        			}
	        			for (int j=0; j<edictData.getLength(); j++) {
	        				Node data = edictData.item(j);
	        				String datatype = data.getNodeName();
	        				switch (datatype) {
	        				case "name":
	        					if (!edictFound) edict.setName(data.getTextContent());
	        					else edictList.get(a).setName(data.getTextContent());
	        					break;
	        				case "price":
	        					if (!edictFound) edict.setCost(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
	        					else edictList.get(a).setCost(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
	        					break;
	        				case "happiness":
	        					if (!edictFound) edict.setHappiness(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
	        					else edictList.get(a).setHappiness(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
	        					break;
	        				}
	        			}
	        			if (!edictFound) edictList.add(edict);
	        		}
	        	}
	        	nList = doc.getElementsByTagName("unit");
	        	for (int i=0; i<nList.getLength(); i++) {
	        		Node nNode = nList.item(i);
	        		if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.hasAttributes()) {
	        			NodeList unitData = nNode.getChildNodes();
	        			NamedNodeMap unitIDs = nNode.getAttributes();
	        			CivilisationClickerUnit unit = new CivilisationClickerUnit(unitIDs.item(0).getTextContent());
	        			boolean unitFound = false;
	        			int a = 0;
	        			if (unitFound = unitList.contains(unit)) a = unitList.indexOf(unit);
	        			for (int j=0; j<unitData.getLength(); j++) {
	        				Node data = unitData.item(j);
	        				String datatype = data.getNodeName();
	        				switch (datatype) {
	        				case "name":
	        					if(!unitFound) unit.setName(data.getTextContent());
	        					else unitList.get(a).setName(data.getTextContent());
	        					break;
	        				case "icon":
	        					if(!unitFound) unit.setIcon(data.getTextContent());
	        					else unitList.get(a).setIcon(data.getTextContent());
	        					break;
	        				case "cost":
	        					if(!unitFound) unit.setCost(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
	        					else unitList.get(a).setCost(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
	        					break;
	        				case "power":
	        					if(!unitFound) unit.setPower(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
	        					else unitList.get(a).setPower(CivilisationClickerMathFunctions.parseInt(data.getTextContent()));
	        					break;
	        				}
	        			}
	        			if(!unitFound) unitList.add(unit);
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
	}
	static void sortXMLData() {
		List<CivilisationClickerBuildingList> workingBuildingMasterList = new ArrayList<CivilisationClickerBuildingList>();
		for (String screenType : screenTypes) {
			CivilisationClickerBuildingList workingBuildingList = new CivilisationClickerBuildingList(screenType);
			for (CivilisationClickerBuildingList buildings : buildingList) {
				if (buildings.screenType.equals(screenType)) {
					for(CivilisationClickerBuilding building : buildings.buildingList) {
						workingBuildingList.addBuilding(building);
					}
				}
			}
			workingBuildingMasterList.add(workingBuildingList);
		}
		buildingList = new ArrayList<CivilisationClickerBuildingList>(workingBuildingMasterList);
		workingBuildingMasterList = new ArrayList<CivilisationClickerBuildingList>();
		for (CivilisationClickerBuildingList buildings : buildingList) {
			int buildingListLength = buildings.getListSize();
			int[] listOrder = new int[buildingListLength];
			for (int i=0; i<listOrder.length; i++) {
				listOrder[i] = i+1;
			}
			for (int i=0; i<listOrder.length; i++) {
				for (int j=i; j>0; j--) {
					CivilisationClickerBuilding building1 = buildings.buildingList.get(listOrder[j] - 1);
					CivilisationClickerBuilding building2 = buildings.buildingList.get(listOrder[j-1] - 1);
					if (building2.Cost > building1.Cost) {
						int order = listOrder[j];
						listOrder[j] = listOrder[j-1];
						listOrder[j-1] = order;
					} else {
						break;
					}
				}
			}
			CivilisationClickerBuildingList workingList = new CivilisationClickerBuildingList(buildings.screenType);
			for (int i=0; i<listOrder.length; i++) {
				workingList.addBuilding(buildings.buildingList.get(listOrder[i] - 1));
			}
			workingBuildingMasterList.add(workingList);
		}
		buildingList = new ArrayList<CivilisationClickerBuildingList>(workingBuildingMasterList);
		List<CivilisationClickerResearchList> workingResearchMasterList = new ArrayList<CivilisationClickerResearchList>();
		for (String screenType : screenTypes) {
			CivilisationClickerResearchList workingResearchList = new CivilisationClickerResearchList(screenType);
			for (CivilisationClickerResearchList researchs : researchList) {
				if (researchs.screenType.equals(screenType)) {
					for(CivilisationClickerResearch research : researchs.researchList) {
						workingResearchList.addResearch(research);
					}
				}
			}
			workingResearchMasterList.add(workingResearchList);
		}
		researchList = new ArrayList<CivilisationClickerResearchList>(workingResearchMasterList);
		workingResearchMasterList = new ArrayList<CivilisationClickerResearchList>();
		for (CivilisationClickerResearchList researchs : researchList) {
			int researchListLength = researchs.getListSize();
			int[] listOrder = new int[researchListLength];
			for (int i=0; i<listOrder.length; i++) {
				listOrder[i] = i+1;
			}
			for (int i=0; i<listOrder.length; i++) {
				for (int j=i; j>0; j--) {
					CivilisationClickerResearch research1 = researchs.researchList.get(listOrder[j] - 1);
					CivilisationClickerResearch research2 = researchs.researchList.get(listOrder[j-1] - 1);
					if (research2.cost > research1.cost) {
						int order = listOrder[j];
						listOrder[j] = listOrder[j-1];
						listOrder[j-1] = order;
					} else {
						break;
					}
				}
			}
			CivilisationClickerResearchList workingList = new CivilisationClickerResearchList(researchs.screenType);
			for (int i=0; i<listOrder.length; i++) {
				workingList.addResearch(researchs.researchList.get(listOrder[i] - 1));
			}
			workingResearchMasterList.add(workingList);
		}
		researchList = new ArrayList<CivilisationClickerResearchList>(workingResearchMasterList);
		List<CivilisationClickerEdict> workingEdictList = new ArrayList<CivilisationClickerEdict>();
		int edictListLength = edictList.size();
		int[] listOrder = new int[edictListLength];
		for (int i=0; i<listOrder.length; i++) {
			listOrder[i] = i+1;
		}
		for (int i=0; i<listOrder.length; i++) {
			for (int j=i; j>0; j--) {
				CivilisationClickerEdict edict1 = edictList.get(listOrder[j] - 1);
				CivilisationClickerEdict edict2 = edictList.get(listOrder[j-1] - 1);
				if (edict2.Cost > edict1.Cost) {
					int order = listOrder[j];
					listOrder[j] = listOrder[j-1];
					listOrder[j-1] = order;
				} else {
					break;
				}
			}
		}
		for (int i=0; i<listOrder.length; i++) {
			workingEdictList.add(edictList.get(listOrder[i] - 1));
		}
		edictList = new ArrayList<CivilisationClickerEdict>(workingEdictList);
		List<CivilisationClickerUnit> workingUnitList = new ArrayList<CivilisationClickerUnit>();
		int unitListLength = unitList.size();
		listOrder = new int[unitListLength];
		for (int i=0; i<listOrder.length; i++) {
			listOrder[i] = i+1;
		}
		for (int i=0; i<listOrder.length; i++) {
			for (int j=i; j>0; j--) {
				CivilisationClickerUnit unit1 = unitList.get(listOrder[j] - 1);
				CivilisationClickerUnit unit2 = unitList.get(listOrder[j-1] - 1);
				if (unit2.Cost > unit1.Cost) {
					int order = listOrder[j];
					listOrder[j] = listOrder[j-1];
					listOrder[j-1] = order;
				} else {
					break;
				}
			}
		}
		for (int i=0; i<listOrder.length; i++) {
			workingUnitList.add(unitList.get(listOrder[i] - 1));
		}
		unitList = new ArrayList<CivilisationClickerUnit>(workingUnitList);
		for (CivilisationClickerMap map : mapList) map.compileColors();
	}
	static void sendXMLData() {
		CivilisationClickerDataBase.screenTypes = new ArrayList<String>(screenTypes);
		CivilisationClickerDataBase.buildingList = new ArrayList<CivilisationClickerBuildingList>(buildingList);
		CivilisationClickerDataBase.researchList = new ArrayList<CivilisationClickerResearchList>(researchList);
		CivilisationClickerDataBase.edictList = new ArrayList<CivilisationClickerEdict>(edictList);
		CivilisationClickerDataBase.unitList = new ArrayList<CivilisationClickerUnit>(unitList);
		CivilisationClickerDataBase.mapList = new ArrayList<CivilisationClickerMap>(mapList);
		screenTypes = null;
		buildingList = null;
		researchList = null;
		edictList = null;
		unitList = null;
		mapList = null;
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
