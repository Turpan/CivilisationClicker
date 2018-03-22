package civilisationClicker;

public class CivilisationClickerEdict {
	static final double EDICTCOSTMULTIPLIER = 1.1;
	String ID;
	String Name;
	int Cost;
	int Happiness;
	int Count;
	CivilisationClickerEdict(String ID) {
		this.ID = ID;
	}
	CivilisationClickerEdict(CivilisationClickerEdict edict) {
		this.ID = edict.ID;
		this.Name = edict.Name;
		this.Cost = edict.Cost;
		this.Happiness = edict.Happiness;
		this.Count = edict.Count;
	}
	void setName(String Name) {
		this.Name = Name;
	}
	void setCost(int Cost) {
		this.Cost = Cost;
	}
	void setHappiness(int Happiness) {
		this.Happiness = Happiness;
	}
	void edictPurchased(int count) {
		Count += count;
		for (int i=0; i<count; i++) {
			Cost = (int) (Cost * EDICTCOSTMULTIPLIER);
		}
	}
	@Override
	public boolean equals(Object o) {
		 if (o == this) return true;
	     if (!(o instanceof CivilisationClickerEdict)) {
	    	 return false;
	     }
	     CivilisationClickerEdict edict = (CivilisationClickerEdict) o;
	     return edict.ID.equals(this.ID);
	}
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + ID.hashCode();
        return result;
	}
	@Override
	public String toString() {
		String edict = ID + ";";
		edict += Name + ";";
		edict += Count + ";";
		return edict;
	}
}
