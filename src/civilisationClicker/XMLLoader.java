package civilisationClicker;

import java.awt.Dimension;
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

public class XMLLoader {
	static List<BuildingList> buildingList = new ArrayList<BuildingList>();
	static List<ResearchList> researchList = new ArrayList<ResearchList>();
	static List<Edict> edictList = new ArrayList<Edict>();
	static List<Unit> unitList = new ArrayList<Unit>();
	static List<Map> mapList = new ArrayList<Map>();
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
		        	BuildingList buildingList = new BuildingList(attribute.getTextContent());
		        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		        		Element elem = (Element) nNode;
		        		NodeList buildingNodes = elem.getElementsByTagName("building");
		        		for (int j=0; j<buildingNodes.getLength(); j++) {
		        			NodeList buildingData = buildingNodes.item(j).getChildNodes();
		        			NamedNodeMap buildingIDs = buildingNodes.item(j).getAttributes();
		        			Building building = new Building();
		        			Node ID = buildingIDs.item(0);
		        			building.setID(ID.getTextContent());
		        			int a = 0;
		        			int b = 0;
		        			boolean buildingFound = false;
		        			for (BuildingList buildingsList : XMLLoader.buildingList) {
		        				if (buildingsList.buildingList.contains(building)) {
		        					buildingFound = true;
		        					for (Building retrievedbuilding : buildingsList.buildingList) {
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
		        					else XMLLoader.buildingList.get(a).buildingList.get(b).setName(data.getTextContent());
		        					break;
		        				case "cost":
		        					if (!buildingFound) building.setCost(MathFunctions.parseInt(data.getTextContent()));
		        					else XMLLoader.buildingList.get(a).buildingList.get(b).setCost(MathFunctions.parseInt(data.getTextContent()));
		        					break;
		        				case "per-second":
		        					if (!buildingFound) building.setPointsPerSecond(MathFunctions.parseInt(data.getTextContent()));
		        					else XMLLoader.buildingList.get(a).buildingList.get(b).setPointsPerSecond(MathFunctions.parseInt(data.getTextContent()));
		        					break;
		        				case "unrest":
		        					if (!buildingFound) building.setUnrest(MathFunctions.parseInt(data.getTextContent()));
		        					else XMLLoader.buildingList.get(a).buildingList.get(b).setUnrest(MathFunctions.parseInt(data.getTextContent()));
		        					break;
		        				case "image":
		        					if (!buildingFound) building.setImageLocation(data.getTextContent());
		        					else XMLLoader.buildingList.get(a).buildingList.get(b).setImageLocation(data.getTextContent());
		        					break;
		        				case "bar":
		        					if (!buildingFound) building.setBarImageLocation(data.getTextContent());
		        					else XMLLoader.buildingList.get(a).buildingList.get(b).setBarImageLocation(data.getTextContent());
		        					break;
		        				case "icon":
		        					if (!buildingFound) building.setIconImageLocation(data.getTextContent());
		        					else XMLLoader.buildingList.get(a).buildingList.get(b).setIconImageLocation(data.getTextContent());
		        					break;
		        				}
		        			}
		        			if (!buildingFound) buildingList.addBuilding(building);
		        		}
		        	}
		        	if (buildingList.getListSize() > 0) {
		        		XMLLoader.buildingList.add(buildingList);
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
		        		ResearchList workingResearchList = new ResearchList(researchListIDs.item(0).getTextContent());
		        		for (int j=0; j<researchNodes.getLength(); j++) {
				        	Node researchData = researchNodes.item(j);
				        	if (researchData.getNodeType() == Node.ELEMENT_NODE && researchData.hasAttributes()) {
				        		NodeList researchDataNodes = researchData.getChildNodes();
			        			NamedNodeMap researchIDs = researchData.getAttributes();
			        			Research research = new Research();
			        			Node ID = researchIDs.item(0);
			        			research.setID(ID.getTextContent());
			        			int a = 0;
			        			int b = 0;
			        			boolean researchFound = false;
			        			for (ResearchList researchsList : XMLLoader.researchList) {
			        				if (researchsList.researchList.contains(research)) {
			        					researchFound = true;
			        					for (Research retrievedresearch : researchsList.researchList) {
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
			        					if (!researchFound) research.setCost(MathFunctions.parseInt(data.getTextContent()));
			        					else researchList.get(a).researchList.get(b).setCost(MathFunctions.parseInt(data.getTextContent()));
			        					break;
			        				case "effect":
			        					if (!researchFound) research.setEffect(data.getTextContent());
			        					else researchList.get(a).researchList.get(b).setEffect(data.getTextContent());
			        					break;
			        				case "value":
			        					if (!researchFound) research.setValue(MathFunctions.parseDouble(data.getTextContent()));
			        					else researchList.get(a).researchList.get(b).setValue(MathFunctions.parseDouble(data.getTextContent()));
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
			        					if (!researchFound) research.setWeight(MathFunctions.parseInt(data.getTextContent()));
			        					else researchList.get(a).researchList.get(b).setWeight(MathFunctions.parseInt(data.getTextContent()));
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
		        			Map map = new Map(mapIDs.item(0).getTextContent());
		        			for (int j=0; j<mapData.getLength(); j++) {
		        				Node data = mapData.item(j);
		        				if (data.getNodeType() == Node.ELEMENT_NODE) {
		        					Element elem = (Element) data;
			        				String datatype = data.getNodeName();
			        				switch (datatype) {
			        				case "mapfile":
			        					map.setMapFile(data.getTextContent());
			        					break;
			        				case "provinces":
			        					NodeList provinceData = elem.getElementsByTagName("province");
			        					for (int k=0; k<provinceData.getLength(); k++) {
			        						Node provinceNode = provinceData.item(k);
			        						NodeList provinceNodeData = provinceNode.getChildNodes();
			        						NamedNodeMap provinceIDs = provinceNode.getAttributes();
			        						Province province = new Province(MathFunctions.parseInt(provinceIDs.item(0).getTextContent()));
			        						for (int h=0; h<provinceNodeData.getLength(); h++) {
			        							Node provinceNodeNode = provinceNodeData.item(h);
			        							String provincedatatype = provinceNodeNode.getNodeName();
			        							switch (provincedatatype) {
			        							case "red":
			        								province.setRed(MathFunctions.parseInt(provinceNodeNode.getTextContent()));
			        								break;
			        							case "green":
			        								province.setGreen(MathFunctions.parseInt(provinceNodeNode.getTextContent()));
			        								break;
			        							case "blue":
			        								province.setBlue(MathFunctions.parseInt(provinceNodeNode.getTextContent()));
			        								break;
			        							}
			        						}
			        						map.addProvince(province);
			        					}
			        					break;
			        				case "straits":
			        					NodeList straitData = elem.getElementsByTagName("strait");
			        					for (int k=0; k<straitData.getLength(); k++) {
			        						Node straitNode = straitData.item(k);
			        						NodeList straitNodeData = straitNode.getChildNodes();
			        						int a = -1;
		        							int b = -1;
			        						for (int h=0; h<straitNodeData.getLength(); h++) {
			        							Node straitNodeNode = straitNodeData.item(h);
			        							String straitdatatype = straitNodeNode.getNodeName();
			        							switch (straitdatatype) {
			        							case "side-a":
			        								a = MathFunctions.parseInt(straitNodeNode.getTextContent());
			        								break;
			        							case "side-b":
			        								b = MathFunctions.parseInt(straitNodeNode.getTextContent());
			        								break;
			        							}
			        						}
			        						if (a > -1 && b > -1) {
		        								Dimension adjacency1 = new Dimension(a, b);
		        								Dimension adjacency2 = new Dimension(b, a);
		        								map.addAdjacency(adjacency1);
		        								map.addAdjacency(adjacency2);
		        							}
			        					}
			        					break;
			        				}
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
	        			Edict edict = new Edict(edictIDs.item(0).getTextContent());
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
	        					if (!edictFound) edict.setCost(MathFunctions.parseInt(data.getTextContent()));
	        					else edictList.get(a).setCost(MathFunctions.parseInt(data.getTextContent()));
	        					break;
	        				case "happiness":
	        					if (!edictFound) edict.setHappiness(MathFunctions.parseInt(data.getTextContent()));
	        					else edictList.get(a).setHappiness(MathFunctions.parseInt(data.getTextContent()));
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
	        			Unit unit = new Unit(unitIDs.item(0).getTextContent());
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
	        					if(!unitFound) unit.setCost(MathFunctions.parseInt(data.getTextContent()));
	        					else unitList.get(a).setCost(MathFunctions.parseInt(data.getTextContent()));
	        					break;
	        				case "power":
	        					if(!unitFound) unit.setPower(MathFunctions.parseInt(data.getTextContent()));
	        					else unitList.get(a).setPower(MathFunctions.parseInt(data.getTextContent()));
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
		List<BuildingList> workingBuildingMasterList = new ArrayList<BuildingList>();
		for (String screenType : screenTypes) {
			BuildingList workingBuildingList = new BuildingList(screenType);
			for (BuildingList buildings : buildingList) {
				if (buildings.screenType.equals(screenType)) {
					for(Building building : buildings.buildingList) {
						workingBuildingList.addBuilding(building);
					}
				}
			}
			workingBuildingMasterList.add(workingBuildingList);
		}
		buildingList = new ArrayList<BuildingList>(workingBuildingMasterList);
		workingBuildingMasterList = new ArrayList<BuildingList>();
		for (BuildingList buildings : buildingList) {
			int buildingListLength = buildings.getListSize();
			int[] listOrder = new int[buildingListLength];
			for (int i=0; i<listOrder.length; i++) {
				listOrder[i] = i+1;
			}
			for (int i=0; i<listOrder.length; i++) {
				for (int j=i; j>0; j--) {
					Building building1 = buildings.buildingList.get(listOrder[j] - 1);
					Building building2 = buildings.buildingList.get(listOrder[j-1] - 1);
					if (building2.Cost > building1.Cost) {
						int order = listOrder[j];
						listOrder[j] = listOrder[j-1];
						listOrder[j-1] = order;
					} else {
						break;
					}
				}
			}
			BuildingList workingList = new BuildingList(buildings.screenType);
			for (int i=0; i<listOrder.length; i++) {
				workingList.addBuilding(buildings.buildingList.get(listOrder[i] - 1));
			}
			workingBuildingMasterList.add(workingList);
		}
		buildingList = new ArrayList<BuildingList>(workingBuildingMasterList);
		List<ResearchList> workingResearchMasterList = new ArrayList<ResearchList>();
		for (String screenType : screenTypes) {
			ResearchList workingResearchList = new ResearchList(screenType);
			for (ResearchList researchs : researchList) {
				if (researchs.screenType.equals(screenType)) {
					for(Research research : researchs.researchList) {
						workingResearchList.addResearch(research);
					}
				}
			}
			workingResearchMasterList.add(workingResearchList);
		}
		researchList = new ArrayList<ResearchList>(workingResearchMasterList);
		workingResearchMasterList = new ArrayList<ResearchList>();
		for (ResearchList researchs : researchList) {
			int researchListLength = researchs.getListSize();
			int[] listOrder = new int[researchListLength];
			for (int i=0; i<listOrder.length; i++) {
				listOrder[i] = i+1;
			}
			for (int i=0; i<listOrder.length; i++) {
				for (int j=i; j>0; j--) {
					Research research1 = researchs.researchList.get(listOrder[j] - 1);
					Research research2 = researchs.researchList.get(listOrder[j-1] - 1);
					if (research2.cost > research1.cost) {
						int order = listOrder[j];
						listOrder[j] = listOrder[j-1];
						listOrder[j-1] = order;
					} else {
						break;
					}
				}
			}
			ResearchList workingList = new ResearchList(researchs.screenType);
			for (int i=0; i<listOrder.length; i++) {
				workingList.addResearch(researchs.researchList.get(listOrder[i] - 1));
			}
			workingResearchMasterList.add(workingList);
		}
		researchList = new ArrayList<ResearchList>(workingResearchMasterList);
		List<Edict> workingEdictList = new ArrayList<Edict>();
		int edictListLength = edictList.size();
		int[] listOrder = new int[edictListLength];
		for (int i=0; i<listOrder.length; i++) {
			listOrder[i] = i+1;
		}
		for (int i=0; i<listOrder.length; i++) {
			for (int j=i; j>0; j--) {
				Edict edict1 = edictList.get(listOrder[j] - 1);
				Edict edict2 = edictList.get(listOrder[j-1] - 1);
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
		edictList = new ArrayList<Edict>(workingEdictList);
		List<Unit> workingUnitList = new ArrayList<Unit>();
		int unitListLength = unitList.size();
		listOrder = new int[unitListLength];
		for (int i=0; i<listOrder.length; i++) {
			listOrder[i] = i+1;
		}
		for (int i=0; i<listOrder.length; i++) {
			for (int j=i; j>0; j--) {
				Unit unit1 = unitList.get(listOrder[j] - 1);
				Unit unit2 = unitList.get(listOrder[j-1] - 1);
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
		unitList = new ArrayList<Unit>(workingUnitList);
		for (Map map : mapList) map.compileColors();
	}
	static void sendXMLData() {
		DataBase.screenTypes = new ArrayList<String>(screenTypes);
		DataBase.buildingList = new ArrayList<BuildingList>(buildingList);
		DataBase.researchList = new ArrayList<ResearchList>(researchList);
		DataBase.edictList = new ArrayList<Edict>(edictList);
		DataBase.unitList = new ArrayList<Unit>(unitList);
		DataBase.mapList = new ArrayList<Map>(mapList);
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
