package civilisationClicker;

import java.util.ArrayList;
import java.util.List;

public class BuildingList {
	String screenType;
	List<Building> buildingList = new ArrayList<Building>();
	BuildingList(String screenType) {
		this.screenType = screenType;
	}
	void addBuilding(Building building) {
		buildingList.add(building);
	}
	int getListSize() {
		int listSize = 0;
		if (!buildingList.isEmpty()) listSize = buildingList.size();
		return listSize;
	}
}
