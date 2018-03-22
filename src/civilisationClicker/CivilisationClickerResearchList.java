package civilisationClicker;

import java.util.ArrayList;
import java.util.List;

public class CivilisationClickerResearchList {
	String screenType;
	List<CivilisationClickerResearch> researchList = new ArrayList<CivilisationClickerResearch>();
	CivilisationClickerResearchList(String screenType) {
		this.screenType = screenType;
	}
	CivilisationClickerResearchList(CivilisationClickerResearchList researchList) {
		this.screenType = researchList.screenType;
		for (CivilisationClickerResearch research : researchList.researchList) {
			CivilisationClickerResearch newResearch = new CivilisationClickerResearch(research);
			this.researchList.add(newResearch);
		}
	}
	void addResearch(CivilisationClickerResearch research) {
		researchList.add(research);
	}
	int getListSize() {
		int listSize = 0;
		if (!researchList.isEmpty()) listSize = researchList.size();
		return listSize;
	}
}
