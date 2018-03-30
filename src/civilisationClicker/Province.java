package civilisationClicker;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Province {
	int ID;
	int owner;
	int unrest;
	int happiness;
	int red;
	int green;
	int blue;
	int coloniseProgress;
	int newlyConqueredCounter;
	int revoltRiskCounter;
	Color provinceColor;
	boolean coloniseInProgress;
	boolean newlyConquered;
	boolean revoltRisk;
	List<ProvinceDevelopement> developementList = new ArrayList<ProvinceDevelopement>();
	List<Edict> edictList = new ArrayList<Edict>();
	@Override
	public boolean equals(Object o) {
		 if (o == this) return true;
	     if (!(o instanceof Province)) {
	    	 return false;
	     }
	     Province province = (Province) o;
	     return province.ID == this.ID;
	}
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + ID;
        return result;
	}
	Province(int ID) {
		this.ID = ID;
	}
	void setOwner(int owner) {
		this.owner = owner;
	}
	void setRed(int red) {
		this.red = red;
	}
	void setGreen(int green) {
		this.green = green;
	}
	void setBlue(int blue) {
		this.blue = blue;
	}
	void createColor() {
		provinceColor = new Color(red, green, blue);
	}
	void createProvinceDevelopement() {
		for (int i=0; i<DataBase.screenTypes.size(); i++) {
			developementList.add(new ProvinceDevelopement(i + 1, ID));
		}
	}
	void coloniseButtonPressed() {
		if (coloniseInProgress) {
			coloniseProgress += Defines.COLONISEBUTTONVALUE;
			if (coloniseProgress >= 6000) {
				coloniseProgress = 0;
				coloniseInProgress = false;
				CivilisationMainClass.resetProvince(ID, false, owner);
			}
			String output = "coloniseprogress;" + ID + ";" + coloniseProgress + ";";
			if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST)
				CivilisationMainClass.networkCommunication(output);
			else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) 
				CivilisationMainClass.client.outPutCommand(output);
		}
	}
	void timerTick() {
		if (owner > 0) {
			calculateUnrest();
			calculateHappiness();
			calculateProductionModifier();
			if (owner == CivilisationMainClass.playerID || (CivilisationMainClass.playerList.get(owner-1).isAI
					&& CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT))
				for (ProvinceDevelopement developement : developementList) developement.timerTick();
			if (coloniseInProgress) coloniseTick();
			else coloniseProgress = 0;
			if (newlyConquered) newlyConqueredTick();
			else newlyConqueredCounter = 0;
			if (revoltRisk) revoltRiskTick();
			else revoltRiskCounter = 0;
		}
	}
	void coloniseTick() {
		coloniseProgress += Defines.COLONISETICKVALUE;
		if (coloniseProgress >= 6000) {
			coloniseProgress = 0;
			coloniseInProgress = false;
			CivilisationMainClass.resetProvince(ID, false, owner);
		}
		String output = "coloniseprogress;" + ID + ";" + coloniseProgress + ";";
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST)
			CivilisationMainClass.networkCommunication(output);
		else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) 
			CivilisationMainClass.client.outPutCommand(output);
	}
	void newlyConqueredTick() {
		newlyConqueredCounter += 1;
		if (newlyConqueredCounter >= 600) {
			newlyConquered = false;
		}
	}
	void revoltRiskTick() {
		revoltRiskCounter += 1;
		if (revoltRiskCounter >= Defines.REVOLTTIME) {
			CivilisationMainClass.resetProvince(ID, false, 0);
			if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
				String output = "provincestatus;owner;0;" + ID + ";";
				CivilisationMainClass.networkCommunication(output);
			} else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) {
				String output = "provincelost;" + ID + ";";
				CivilisationMainClass.client.outPutCommand(output);
			}
		}
	}
	void updateColonisation(int i) {
		coloniseProgress += i;
		if (coloniseProgress >= 6000) {
			coloniseProgress = 0;
			coloniseInProgress = false;
			CivilisationMainClass.resetProvince(ID, false, owner);
		}
	}
	void calculateUnrest() {
		unrest = 0;
		int i = 0;
		for (ProvinceDevelopement developement : developementList) {
			int j = 0;
			for (Building building : DataBase.buildingList.get(i).buildingList) {
				unrest += (building.Unrest * developement.buildingCount[j]);
				j++;
			}
			i++;
		}
	}
	void calculateHappiness() {
		happiness = 0;
		for (Edict edict : edictList) {
			happiness += edict.Happiness * edict.Count;
		}
	}
	void calculateProductionModifier() {
		double productionModifier = 0;
		if (CivilisationMainClass.timeCount < Defines.HAPPINESSAMNESTYPERIOD) {
			productionModifier = 100;
		} else {
			if (happiness > unrest) {
				if (happiness >= (unrest * 2)) {
					productionModifier = 200;
				} else {
					double chance = happiness / unrest;
					chance -= 1;
					chance = chance * 100;
					productionModifier = chance + 100;
				}
			} else if (happiness < unrest) {
				if (unrest >= (happiness * 2)) {
					productionModifier = 0;
				} else {
					double chance = unrest / happiness;
					chance -= 1;
					chance = chance * 100;
					productionModifier = 100 - chance;
				}
			} else {
				productionModifier = 100;
			}
			if (happiness <= (unrest * Defines.REVOLTRISKTHRESHOLD) && unrest != 0) {
				revoltRisk = true;
			} else {
				revoltRisk = false;
				revoltRiskCounter = 0;
			}
		}
		for (ProvinceDevelopement developement : developementList) {
			developement.productionModifier = productionModifier;
		}
	}
	void recieveColoniseProgressData(int progress, int player) {
		coloniseProgress = progress;
		if (coloniseProgress >= Defines.COLONISEDURATION) {
			coloniseProgress = 0;
			coloniseInProgress = false;
		}
		String output = "coloniseprogress;" + ID + ";" + coloniseProgress + ";";
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST)	
			CivilisationMainClass.relayClientData(output, player);
	}
	void setBuildingCount(int ID, int building, int count, int player) {
		developementList.get(ID - 1).buildingCount[building] = count;
		String output = "buildingcount;" + this.ID + ";" + ID + ";" + building + ";" + count + ";";
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST)
			CivilisationMainClass.relayClientData(output, player);
	}
	void clearProvince() {
		coloniseProgress = 0;
		newlyConqueredCounter = 0;
		revoltRiskCounter = 0;
		coloniseInProgress = false;
		newlyConquered = false;
		revoltRisk = false;
		developementList = new ArrayList<ProvinceDevelopement>();
		edictList = DataBase.createNewEdictList();
		createProvinceDevelopement();
	}
	void clearProvince(boolean conquered) {
		coloniseProgress = 0;
		newlyConqueredCounter = 0;
		revoltRiskCounter = 0;
		coloniseInProgress = false;
		newlyConquered = true;
		revoltRisk = false;
		developementList = new ArrayList<ProvinceDevelopement>();
		edictList = DataBase.createNewEdictList();
		createProvinceDevelopement();
	}
}
