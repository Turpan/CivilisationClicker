package civilisationClicker;

import java.util.ArrayList;
import java.util.List;

public class CivilisationClickerBuildingList {
	String screenType;
	List<CivilisationClickerBuilding> buildingList = new ArrayList<CivilisationClickerBuilding>();
	CivilisationClickerBuildingList(String screenType) {
		this.screenType = screenType;
	}
	void addBuilding(CivilisationClickerBuilding building) {
		buildingList.add(building);
	}
	int getListSize() {
		int listSize = 0;
		if (!buildingList.isEmpty()) listSize = buildingList.size();
		return listSize;
	}
}
