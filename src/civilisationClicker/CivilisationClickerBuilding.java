package civilisationClicker;

public class CivilisationClickerBuilding {
	@Override
	public boolean equals(Object o) {
		 if (o == this) return true;
	     if (!(o instanceof CivilisationClickerBuilding)) {
	    	 return false;
	     }
	     CivilisationClickerBuilding building = (CivilisationClickerBuilding) o;
	     return building.ID.equals(this.ID);
	}
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + ID.hashCode();
        return result;
	}
	String ID = "";
	String Name = "";
	String ImageLocation = "";
	String BarImageLocation = "";
	String IconImageLocation = "";
	int Cost;
	int PointsPerSecond;
	int Unrest;
	boolean Unlocked = false;
	//List<CivilisationClickerResearch> researchList = new ArrayList<CivilisationClickerResearch>();
	void setID(String ID) {
		this.ID = ID;
	}
	void setName(String Name) {
		this.Name = Name;
	}
	void setImageLocation(String ImageLocation) {
		this.ImageLocation = ImageLocation;
	}
	void setBarImageLocation(String BarImageLocation) {
		this.BarImageLocation = BarImageLocation;
	}
	void setIconImageLocation(String IconImageLocation) {
		this.IconImageLocation = IconImageLocation;
	}
	void setCost(int Cost) {
		this.Cost = Cost;
	}
	void setPointsPerSecond(int PointsPerSecond) {
		this.PointsPerSecond = PointsPerSecond;
	}
	void setUnrest(int Unrest) {
		this.Unrest = Unrest;
	}
	void setUnlocked(boolean Unlocked) {
		this.Unlocked = Unlocked;
	}
	/*void addResearch(CivilisationClickerResearch research) {
		researchList.add(research);
	}
	int getListSize() {
		int listSize = 0;
		if (!researchList.isEmpty()) listSize = researchList.size();
		return listSize;
	}*/
}
