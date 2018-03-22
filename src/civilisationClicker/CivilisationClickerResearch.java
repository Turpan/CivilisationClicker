package civilisationClicker;

public class CivilisationClickerResearch {
	String ID = "";
	String Name = "";
	String Building = "";
	String Effect = "";
	String Icon = "graphics/ui/researchscreen/icons/default.png";
	String Required = "";
	double value;
	int cost;
	int weight;
	boolean purchased = false;
	@Override
	public boolean equals(Object o) {
		 if (o == this) return true;
	     if (!(o instanceof CivilisationClickerResearch)) {
	    	 return false;
	     }
	     CivilisationClickerResearch research = (CivilisationClickerResearch) o;
	     return research.ID.equals(this.ID);
	}
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + ID.hashCode();
        return result;
	}
	CivilisationClickerResearch() {
		
	}
	CivilisationClickerResearch(CivilisationClickerResearch research) {
		this.ID = research.ID;
		this.Name = research.Name;
		this.Building = research.Building;
		this.Effect = research.Effect;
		this.Icon = research.Icon;
		this.Required = research.Required;
		this.value = research.value;
		this.cost = research.cost;
		this.weight = research.weight;
		this.purchased = research.purchased;
	}
	void setID(String ID) {
		this.ID = ID;
	}
	void setName(String Name) {
		this.Name = Name;
	}
	void setBuilding(String Building) {
		this.Building = Building;
	}
	void setEffect(String Effect) {
		this.Effect = Effect;
	}
	void setIcon(String Icon) {
		this.Icon = Icon;
	}
	void setValue(double Value) {
		this.value = Value;
	}
	void setCost(int cost) {
		this.cost = cost;
	}
	void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}
	void setRequired(String Required) {
		this.Required = Required;
	}
	void setWeight(int weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		String toString = this.ID + ";" + this.Building + ";" + this.Effect + ";" + this.Required + ";" + this.purchased + ";";
		return toString;
	}
}
