package civilisationClicker;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
	static List<BuildingList> buildingList;
	static List<Map> mapList;
	static List<ResearchList> researchList;
	static List<Edict> edictList;
	static List<Unit> unitList;
	static List<String> screenTypes;
	static int chosenMap;
	static Map getChosenMap() {
		return mapList.get(chosenMap);
	}
	static List<Edict> createNewEdictList() {
		List<Edict> newList = new ArrayList<Edict>();
		for (Edict edict : edictList) {
			Edict newEdict = new Edict(edict);
			newList.add(newEdict);
		}
		return newList;
	}
	static List<ResearchList> createNewResearchList() {
		List<ResearchList> newList = new ArrayList<ResearchList>();
		for (ResearchList researchs : researchList) {
			ResearchList newResearchList = new ResearchList(researchs);
			newList.add(newResearchList);
		}
		return newList;
	}
	static List<Unit> createNewUnitList() {
		List<Unit> newList = new ArrayList<Unit>();
		for (Unit unit : unitList) {
			Unit newUnit = new Unit(unit);
			newList.add(newUnit);
		}
		return newList;
	}
}
