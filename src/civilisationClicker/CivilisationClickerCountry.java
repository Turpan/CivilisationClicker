package civilisationClicker;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CivilisationClickerCountry {
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
		CivilisationClickerCountry other = (CivilisationClickerCountry) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	static final double baseColoniseCost = 5000;
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
	double[] points = new double[CivilisationClickerDataBase.screenTypes.size()];
	String name;
	boolean isAI;
	boolean AIColoniseWait;
	List<CivilisationClickerResearchList> researchList = CivilisationClickerDataBase.createNewResearchList();
	List<CivilisationClickerUnit> unitList = CivilisationClickerDataBase.createNewUnitList();
	Set<Integer> adjacencyList = new HashSet<Integer>();
	CivilisationClickerCountry(int ID, boolean isAI, String name, Color color, int startingProvince) {
		this.ID = ID;
		this.isAI = isAI;
		this.color = color;
		this.name = name;
		this.startingProvince = startingProvince;
	}
	void AIAction() {
		for (CivilisationClickerProvince province : CivilisationClickerMapScreen.provinceList) {
			if (province.owner == ID + 1) for (CivilisationClickerProvinceDevelopement developement : province.developementList) {
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
		for (CivilisationClickerBattle battle : CivilisationMainClass.battleList.battleList) {
			if (battle.defender == ID+1 && !battle.defenderJoined) {
				List<CivilisationClickerUnit> units = CivilisationClickerDataBase.createNewUnitList();
				int j = 0;
				double power = 0;
				for (CivilisationClickerUnit unit : unitList) {
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
		for (CivilisationClickerProvince province : CivilisationClickerMapScreen.provinceList) 
			if (province.owner != 0 && province.owner != ID+1 && adjacencyList.contains(Integer.valueOf(province.ID))) {
			CivilisationClickerCountry target = CivilisationMainClass.playerList.get(province.owner-1);
			if (availableUnitPoints >= (target.availableUnitPoints * AICOMBATPOINTREQUIREMENT) && availableUnitPoints > 0 &&
					!CivilisationMainClass.battleList.battleInProvince(province.ID)) {
				List<CivilisationClickerUnit> units = CivilisationClickerDataBase.createNewUnitList();
				int j = 0;
				double power = 0;
				for (CivilisationClickerUnit unit : unitList) {
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
			if (CivilisationClickerMapScreen.provinceList.get(province.intValue()).owner == 0) {
				colonisecheck = true;
				break;
			}
		}
		if (!colonisecheck) return colonisecheck;
		int pointcheck = 0;
		for (int i=0; i<points.length; i++) {
			double pointspersecond = 0;
			for (CivilisationClickerProvince province : CivilisationClickerMapScreen.provinceList) if (province.owner == ID+1){
				CivilisationClickerProvinceDevelopement developement = province.developementList.get(i);
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
		int a = CivilisationClickerSuperScreen.militaryPointPool-1;
		for (CivilisationClickerUnit unit : unitList) if (unit.Cost < cost) {
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
		int a = CivilisationClickerSuperScreen.governmentPointPool-1;
		for (CivilisationClickerProvince province : CivilisationClickerMapScreen.provinceList) {
			if (province.owner == ID + 1) for (CivilisationClickerEdict edict : province.edictList) if (edict.Cost < cost) {
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
		CivilisationClickerResearch researchid = researchList.get(0).researchList.get(0);
		double cost = Integer.MAX_VALUE;
		int researchlistid = 0;
		int a = CivilisationClickerSuperScreen.researchPointPool-1;
		for (CivilisationClickerResearchList researchs : researchList) for (CivilisationClickerResearch research : researchs.researchList) {
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
		int a = CivilisationClickerSuperScreen.buildingPointPool-1;
		int buildingid = 0;
		double cost = Integer.MAX_VALUE;
		int provinceid = 0;
		int developementid = 0;
		boolean buildingbought = false;
		for (CivilisationClickerProvince province : CivilisationClickerMapScreen.provinceList) {
			if (province.owner == ID + 1 && !province.coloniseInProgress) for (CivilisationClickerProvinceDevelopement developement : province.developementList) {
				for (int i=0; i<developement.buildingCost.length; i++) if (developement.buildingCost[i] < cost) {
					developementid = developement.ID-1;
					provinceid = province.ID;
					cost = developement.buildingCost[i];
					buildingid = i;
				}
			}
		}
		if (buildingbought = points[a] >= cost)
			CivilisationClickerMapScreen.provinceList.get(provinceid).developementList.get(developementid).buyBuilding(buildingid, 1);
		CivilisationClickerProvinceDevelopement developement = CivilisationClickerMapScreen.provinceList.get(provinceid).developementList.get(developementid);
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
				if (CivilisationClickerMapScreen.provinceList.get(province.intValue()).owner == 0) {
					startColonisation(province.intValue());
					AIColoniseWait = false;
					break;
				}
			}
		}
		return colonise;
	}
	void clickButton(int province, int screen) {
		CivilisationClickerDataBase.mapList.get(CivilisationClickerDataBase.chosenMap).provinceList.get(province).developementList.get(screen).clickButton();
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
			CivilisationClickerProvince mapprovince = CivilisationClickerMapScreen.provinceList.get(province);
			if (mapprovince.owner == ID+1 && !mapprovince.coloniseInProgress) {
				adjacencyList.add(Integer.valueOf(adjacent));
			}
		}
	}
	void startColonisation(int provinceid) {
		CivilisationMainClass.mapScreen.changeProvinceOwner(provinceid, ID + 1);
		CivilisationClickerProvince province = CivilisationClickerMapScreen.provinceList.get(provinceid);
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
	void buyResearch(CivilisationClickerResearch researchOption, int screenType) {
		int a = CivilisationClickerSuperScreen.researchPointPool;
		if (points[a - 1] >= researchOption.cost) {		
			for (CivilisationClickerResearch research : researchList.get(screenType).researchList) {
				if (research == researchOption) {
					research.setPurchased(true);
					points[a - 1] -= researchOption.cost;
					break;
				}
			}
		}
	}
	boolean buyEdict(int edictid, int provinceid, int count) {
		boolean edictPurchased = false;
		int a = CivilisationClickerSuperScreen.governmentPointPool;
		CivilisationClickerProvince province = CivilisationClickerMapScreen.provinceList.get(provinceid);
		CivilisationClickerEdict edict = province.edictList.get(edictid);
		double potentialcost = edict.Cost;
		for (int i=1; i<count; i++) {
			potentialcost = potentialcost * CivilisationClickerEdict.EDICTCOSTMULTIPLIER;
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
		CivilisationClickerUnit unit = unitList.get(unitid);
		int a = CivilisationClickerSuperScreen.militaryPointPool;
		double totalCost = 0;
		double potentialUnitCost = unit.Cost;
		for (int i=0; i<amount; i++) {
			totalCost += potentialUnitCost;
			potentialUnitCost = potentialUnitCost * CivilisationClickerUnit.UNITCOSTMULTIPLIER;
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
		for (CivilisationClickerUnit unit : unitList) {
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
