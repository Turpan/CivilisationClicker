package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Country {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	static final double baseColoniseCost = 25000;
	static final double coloniseCostMulitplier = 1.5;
	static final double AICOMBATPOINTREQUIREMENT = 0.8;
	static final int HAPPINESSAMNESTYPERIOD = 600;
	static final int AIBUTTONPRESS = 100;
	static final int AICOLONISETURNREQUIREMENT = 20;
	Color color;
	int totalUnitPoints;
	int availableUnitPoints;
	int startingProvince;
	int ID;
	double coloniseCost = baseColoniseCost;
	double[] points = new double[DataBase.screenTypes.size()];
	String name;
	boolean isAI;
	boolean AIColoniseWait;
	List<ResearchList> researchList = DataBase.createNewResearchList();
	List<Unit> unitList = DataBase.createNewUnitList();
	Set<Integer> adjacencyList = new HashSet<Integer>();
	Country(int ID, boolean isAI, String name, Color color, int startingProvince) {
		this.ID = ID;
		this.isAI = isAI;
		this.color = color;
		this.name = name;
		this.startingProvince = startingProvince;
	}
	void AIAction() {
		for (Province province : MapScreen.provinceList) {
			if (province.owner == ID + 1) for (ProvinceDevelopement developement : province.developementList) {
				for (int i=0; i<AIBUTTONPRESS; i++) developement.clickButton();
			}
		}
		if (AIColoniseWait) {
			while (AIColonise());
		} else {
			while (AIBuyBuilding());
			while (AIBuyResearch());
			while (AIBuyEdict());
			while (AIBuyUnit());
			while (AIAttack());
			while (AIDefend());
			AIColoniseWait = AIColoniseCheck();
		}
	}
	boolean AIDefend() {
		for (Battle battle : CivilisationMainClass.battleList.battleList) {
			if (battle.defender == ID+1 && !battle.defenderJoined) {
				List<Unit> units = DataBase.createNewUnitList();
				int j = 0;
				double power = 0;
				for (Unit unit : unitList) {
					for (int i=0; i<unit.Available; i++) {
						if (power >= battle.attackerPointTotal) break;
						power += unit.Power;
						units.get(j).Count = i;
					}
					j++;
				}
				CivilisationMainClass.battleList.joinBattle(units, battle.province, battle.attacker, battle.defender);
				return true;
			}
		}
		return false;
	}
	boolean AIAttack() {
		boolean attack = false;
		for (Province province : MapScreen.provinceList) 
			if (province.owner != 0 && province.owner != ID+1 && adjacencyList.contains(Integer.valueOf(province.ID))) {
			Country target = CivilisationMainClass.playerList.get(province.owner-1);
			if (availableUnitPoints >= (target.availableUnitPoints * AICOMBATPOINTREQUIREMENT) && availableUnitPoints > 0 &&
					!CivilisationMainClass.battleList.battleInProvince(province.ID)) {
				List<Unit> units = DataBase.createNewUnitList();
				int j = 0;
				double power = 0;
				for (Unit unit : unitList) {
					for (int i=0; i<unit.Available; i++) {
						if (power > target.availableUnitPoints) break;
						power += unit.Power;
						units.get(j).Count = i+1;
					}
					j++;
				}
				CivilisationMainClass.battleList.createBattle(units, province.ID, ID+1, province.owner);
				return attack = true;
			}
		}
		return attack;
	}
	boolean AIColoniseCheck() {
		boolean colonisecheck = false;
		for(Integer province : adjacencyList) {
			if (MapScreen.provinceList.get(province.intValue()).owner == 0) {
				colonisecheck = true;
				break;
			}
		}
		if (!colonisecheck) return colonisecheck;
		int pointcheck = 0;
		for (int i=0; i<points.length; i++) {
			double pointspersecond = 0;
			for (Province province : MapScreen.provinceList) if (province.owner == ID+1){
				ProvinceDevelopement developement = province.developementList.get(i);
				for (int j=0; j<AIBUTTONPRESS; j++) pointspersecond += developement.pointsPerClick;
				pointspersecond += developement.pointsPerSecond();
			}
			if (pointspersecond >= (coloniseCost / AICOLONISETURNREQUIREMENT)) pointcheck++;
		}
		colonisecheck = (pointcheck == points.length);
		return colonisecheck;
	}
	boolean AIBuyUnit() {
		boolean unitbought = false;
		int unitid = 0;
		double cost = Integer.MAX_VALUE;
		int a = SuperScreen.militaryPointPool-1;
		for (Unit unit : unitList) if (unit.Cost < cost) {
			unitid = unitList.indexOf(unit);
			cost = unit.Cost;
		}
		if (unitbought = points[a] >= cost)
			buyUnit(unitid, 1);
		return unitbought;
	}
	boolean AIBuyEdict() {
		boolean edictbought = false;
		int edictid = 0;
		int provinceid = 0;
		double cost = Integer.MAX_VALUE;
		int a = SuperScreen.governmentPointPool-1;
		for (Province province : MapScreen.provinceList) {
			if (province.owner == ID + 1) for (Edict edict : province.edictList) if (edict.Cost < cost) {
				cost = edict.Cost;
				edictid = province.edictList.indexOf(edict);
				provinceid = province.ID;
				break;
			}
		}
		if (edictbought = points[a] >= cost)
			buyEdict(edictid, provinceid, 1);
		return edictbought;
	}
	boolean AIBuyResearch() {
		boolean researchbought = false;
		Research researchid = researchList.get(0).researchList.get(0);
		double cost = Integer.MAX_VALUE;
		int researchlistid = 0;
		int a = SuperScreen.researchPointPool-1;
		for (ResearchList researchs : researchList) for (Research research : researchs.researchList) {
			if (research.cost < cost && !research.purchased) {
				cost = research.cost;
				researchid = research;
				researchlistid = researchList.indexOf(researchs);
			}
		}
		if (researchbought = points[a] >= cost)
			buyResearch(researchid, researchlistid);
		return researchbought;
	}
	boolean AIBuyBuilding() {
		int a = SuperScreen.buildingPointPool-1;
		int buildingid = 0;
		double cost = Integer.MAX_VALUE;
		int provinceid = 0;
		int developementid = 0;
		boolean buildingbought = false;
		for (Province province : MapScreen.provinceList) {
			if (province.owner == ID + 1 && !province.coloniseInProgress) for (ProvinceDevelopement developement : province.developementList) {
				for (int i=0; i<developement.buildingCost.length; i++) if (developement.buildingCost[i] < cost) {
					developementid = developement.ID-1;
					provinceid = province.ID;
					cost = developement.buildingCost[i];
					buildingid = i;
				}
			}
		}
		if (buildingbought = points[a] >= cost)
			MapScreen.provinceList.get(provinceid).developementList.get(developementid).buyBuilding(buildingid, 1);
		ProvinceDevelopement developement = MapScreen.provinceList.get(provinceid).developementList.get(developementid);
		return buildingbought;
	}
	boolean AIColonise() {
		int pointcounter = 0;
		boolean colonise;
		for (int i=0; i<points.length; i++) {
			if (points[i] >= coloniseCost) pointcounter++;
		}
		if (colonise = pointcounter == points.length) {
			for(Integer province : adjacencyList) {
				if (MapScreen.provinceList.get(province.intValue()).owner == 0) {
					startColonisation(province.intValue());
					AIColoniseWait = false;
					break;
				}
			}
		}
		return colonise;
	}
	void clickButton(int province, int screen) {
		DataBase.mapList.get(DataBase.chosenMap).provinceList.get(province).developementList.get(screen).clickButton();
	}
	void timerTick() {
		if (isAI) AIAction();
		findAccessableProvinces();
	}
	void findAccessableProvinces() {
		adjacencyList = new HashSet<Integer>();
		for (Dimension provinceAdjacency : CivilisationMainClass.mapScreen.gameMap.adjacencyList) {
			int province = provinceAdjacency.width;
			int adjacent = provinceAdjacency.height;
			Province mapprovince = MapScreen.provinceList.get(province);
			if (mapprovince.owner == ID+1 && !mapprovince.coloniseInProgress) {
				adjacencyList.add(Integer.valueOf(adjacent));
			}
		}
	}
	void startColonisation(int provinceid) {
		CivilisationMainClass.mapScreen.changeProvinceOwner(provinceid, ID + 1);
		Province province = MapScreen.provinceList.get(provinceid);
		int screenCount = points.length;
		for (int i=0; i<screenCount; i++) {
			points[i] -= coloniseCost;
		}
		province.coloniseInProgress = true;
		coloniseCost = coloniseCost * coloniseCostMulitplier;
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			int ID = this.ID + 1;
			String output = "provincestatus;colonise;" + ID + ";" + province + ";";
			for (int i=0; i<CivilisationMainClass.server.players; i++) {
				if (CivilisationMainClass.server.playerSlotInUse[i]) {
					CivilisationMainClass.server.clients[i].outPutCommand(output);
				}
			}
		}
	}
	void colonisationCheck(int province) {
		int costCount = 0;
		int screenCount = points.length;
		for (int i=0; i<screenCount; i++) {
			if (points[i] >= coloniseCost) {
				costCount ++;
			}
		}
		if (costCount >= screenCount) {
			if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT) {
				startColonisation(province);
			} else {
				String output = "colonisationcheck;" + province + ";";
				CivilisationMainClass.client.outPutCommand(output);
			}
		}
	}
	boolean buyResearch(Research researchOption, int screenType) {
		boolean researchPurchased = false;
		int a = SuperScreen.researchPointPool;
		if (points[a - 1] >= researchOption.cost) {		
			for (Research research : researchList.get(screenType).researchList) {
				if (researchPurchased = research.equals(researchOption)) {
					research.setPurchased(true);
					points[a - 1] -= researchOption.cost;
					break;
				}
			}
		}
		return researchPurchased;
	}
	boolean buyEdict(int edictid, int provinceid, int count) {
		boolean edictPurchased = false;
		int a = SuperScreen.governmentPointPool;
		Province province = MapScreen.provinceList.get(provinceid);
		Edict edict = province.edictList.get(edictid);
		double potentialcost = edict.Cost;
		for (int i=1; i<count; i++) {
			potentialcost = potentialcost * Edict.EDICTCOSTMULTIPLIER;
		}
		if (edictPurchased = points[a - 1] >= potentialcost) {
			points[a - 1] -= potentialcost;
			edict.edictPurchased(count);
		}
		province.calculateUnrest();
		province.calculateHappiness();
		province.calculateProductionModifier();
		return edictPurchased;
	}
	boolean buyUnit(int unitid, int amount) {
		boolean unitBought = false;
		Unit unit = unitList.get(unitid);
		int a = SuperScreen.militaryPointPool;
		double totalCost = 0;
		double potentialUnitCost = unit.Cost;
		for (int i=0; i<amount; i++) {
			totalCost += potentialUnitCost;
			potentialUnitCost = potentialUnitCost * Unit.UNITCOSTMULTIPLIER;
		}
		if (points[a - 1] >= totalCost) {
			unitBought = true;
			points[a - 1] -= totalCost;
			unit.unitPurchased(amount);
			calculateUnitPower();
			String output = "unitpower;" + (ID+1) + ";" + totalUnitPoints + ";";
			if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) CivilisationMainClass.networkCommunication(output);
			else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) CivilisationMainClass.client.outPutCommand(output);
		}
		return unitBought;
	}
	void calculateUnitPower() {
		totalUnitPoints = 0;
		availableUnitPoints = 0;
		for (Unit unit : unitList) {
			totalUnitPoints += unit.Count * unit.Power;
			availableUnitPoints += unit.Available * unit.Power;
		}
	}
	void recieveArmyPower(int power) {
		totalUnitPoints = power;
		String output = "unitpower;" + (ID+1) + ";" + totalUnitPoints + ";";
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) CivilisationMainClass.relayClientData(output, ID + 1);
	}
}
