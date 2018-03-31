package civilisationClicker;

public class Unit {
	String ID;
	String Name;
	String Icon;
	int Power;
	int Cost;
	int Count;
	int Available;
	boolean Unlocked = false;
	Unit(String ID) {
		this.ID = ID;
	}
	Unit(Unit unit) {
		this.ID = unit.ID;
		this.Name = unit.Name;
		this.Icon = unit.Icon;
		this.Power = unit.Power;
		this.Cost = unit.Cost;
		this.Count = unit.Count;
		this.Available = unit.Available;
	}
	void setName(String Name) {
		this.Name = Name;
	}
	void setIcon(String Icon) {
		this.Icon = Icon;
	}
	void setPower(int Power) {
		this.Power = Power;
	}
	void setCost(int Cost) {
		this.Cost = Cost;
	}
	void unitPurchased(int count) {
		Count += count;
		Available += count;
		for (int i=0; i<count; i++) {
			Cost = (int) (Cost * Defines.UNITCOSTMULTIPLIER);
		}
	}
	@Override
	public boolean equals(Object o) {
		 if (o == this) return true;
	     if (!(o instanceof Unit)) {
	    	 return false;
	     }
	     Unit unit = (Unit) o;
	     return unit.ID.equals(this.ID);
	}
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + ID.hashCode();
        return result;
	}
	@Override
	public String toString() {
		String output = "";
		output += ID + ";";
		output += Power + ";";
		output += Count + ";";
		output += Cost + ";";
		output += Unlocked + ";";
		return output;
	}
}
