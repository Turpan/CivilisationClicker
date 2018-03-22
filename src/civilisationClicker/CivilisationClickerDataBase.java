package civilisationClicker;

import java.util.ArrayList;
import java.util.List;

public class CivilisationClickerDataBase {
	static List<CivilisationClickerBuildingList> buildingList;
	static List<CivilisationClickerMap> mapList;
	static List<CivilisationClickerResearchList> researchList;
	static List<CivilisationClickerEdict> edictList;
	static List<CivilisationClickerUnit> unitList;
	static List<String> screenTypes;
	static int chosenMap;
	static List<CivilisationClickerEdict> createNewEdictList() {
		List<CivilisationClickerEdict> newList = new ArrayList<CivilisationClickerEdict>();
		for (CivilisationClickerEdict edict : edictList) {
			CivilisationClickerEdict newEdict = new CivilisationClickerEdict(edict);
			newList.add(newEdict);
		}
		return newList;
	}
	static List<CivilisationClickerResearchList> createNewResearchList() {
		List<CivilisationClickerResearchList> newList = new ArrayList<CivilisationClickerResearchList>();
		for (CivilisationClickerResearchList researchs : researchList) {
			CivilisationClickerResearchList newResearchList = new CivilisationClickerResearchList(researchs);
			newList.add(newResearchList);
		}
		return newList;
	}
	static List<CivilisationClickerUnit> createNewUnitList() {
		List<CivilisationClickerUnit> newList = new ArrayList<CivilisationClickerUnit>();
		for (CivilisationClickerUnit unit : unitList) {
			CivilisationClickerUnit newUnit = new CivilisationClickerUnit(unit);
			newList.add(newUnit);
		}
		return newList;
	}
}
