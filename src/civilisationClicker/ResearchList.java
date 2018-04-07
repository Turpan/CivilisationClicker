package civilisationClicker;

import java.util.ArrayList;
import java.util.List;

public class ResearchList {
	String screenType;
	List<Research> researchList = new ArrayList<Research>();
	ResearchList(String screenType) {
		this.screenType = screenType;
	}
	ResearchList(ResearchList researchList) {
		this.screenType = researchList.screenType;
		for (Research research : researchList.researchList) {
			Research newResearch = new Research(research);
			this.researchList.add(newResearch);
		}
	}
	void addResearch(Research research) {
		researchList.add(research);
	}
	int getListSize() {
		int listSize = 0;
		if (!researchList.isEmpty()) listSize = researchList.size();
		return listSize;
	}
	@Override
	public String toString() {
		String string = "";
		string += screenType;
		for (Research research : researchList) {
			string += research.toString();
		}
		return string;
	}
}
