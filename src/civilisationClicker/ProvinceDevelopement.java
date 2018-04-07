package civilisationClicker;

public class ProvinceDevelopement {
	double pointsPerClick = 1;
	double productionModifier = 100;
	double[] buildingCost;
	int ID;
	int province;
	int numberOfBuildings;
	int currentStage = 1;
	int[] buildingCount;
	int[] buildingPointsPerSecond;
	ProvinceDevelopement(int ID, int province) {
		this.ID = ID;
		this.province = province;
		buildingCost = new double[DataBase.buildingList.get(ID - 1).buildingList.size()];
		buildingCount = new int[DataBase.buildingList.get(ID - 1).buildingList.size()];
		buildingPointsPerSecond = new int[DataBase.buildingList.get(ID - 1).buildingList.size()];
		int i = 0;
		for (Building building : DataBase.buildingList.get(ID - 1).buildingList) {
			buildingCost[i] = building.Cost;
			buildingPointsPerSecond[i] = building.PointsPerSecond;
			i++;
		}
	}
	void clickButton() {
		int a = DataBase.mapList.get(DataBase.chosenMap).provinceList.get(province).owner;
		CivilisationMainClass.playerList.get(a - 1).points[ID - 1] += (pointsPerClick * (productionModifier/100));
	}
	void timerTick() {
		int a = DataBase.mapList.get(DataBase.chosenMap).provinceList.get(province).owner;
		CivilisationMainClass.playerList.get(a - 1).points[ID - 1] += (pointsPerSecond() * (productionModifier/100));
	}
	double pointsPerSecond() {
		double pointsPerSecond = 0;
		int owner = MapScreen.provinceList.get(province).owner - 1;
		int i = 0;
		for (Building building : DataBase.buildingList.get(ID - 1).buildingList) {
			double modifier = 0;
			for (Research research : CivilisationMainClass.playerList.get(owner).researchList.get(ID - 1).researchList) {
				if (research.Building == building.ID && research.purchased && research.Effect == "boost") modifier += research.value;
			}
			modifier = (modifier + 100) / 100;
			pointsPerSecond += (buildingCount[i] * buildingPointsPerSecond[i]) * modifier;
			i++;
		}
		return pointsPerSecond;
	}
	boolean buyBuilding(int building, int modifier) {
		int owner = DataBase.mapList.get(DataBase.chosenMap).provinceList.get(province).owner;
		int a = Defines.BUILDINGPOINTPOOL;
		boolean buildingBought = false;
		if (modifier > 0) {
			double totalCost = 0;
			double potentialBuildingCost = buildingCost[building];
			for (int i=0; i<modifier; i++) {
				totalCost += potentialBuildingCost;
				potentialBuildingCost = potentialBuildingCost * Defines.BUILDINGSCALEMULTIPLIER;
			}
			if (buildingBought = CivilisationMainClass.playerList.get(owner-1).points[a - 1] >= totalCost) {
				CivilisationMainClass.playerList.get(owner-1).points[a - 1] -= totalCost;
				buildingCost[building] = potentialBuildingCost;
				buildingCount[building] += modifier;
				checkBuildingStage();
				sendBuildingCount(building);
			}
		} else if (modifier < 0) {
			if (buildingBought = buildingCount[building] >= modifier) {
				int totalRebate = 0;
				for (int i=0; i<modifier; i++) {
					totalRebate += buildingCost[building];
					buildingCost[building] = buildingCost[building] / Defines.BUILDINGSCALEMULTIPLIER;
				}
				CivilisationMainClass.playerList.get(owner).points[a - 1] += totalRebate;
				buildingCount[building] -= modifier;
				checkBuildingStage();
				sendBuildingCount(building);
			}
		}
		DataBase.mapList.get(DataBase.chosenMap).provinceList.get(province).calculateUnrest();
		return buildingBought;
	}
	void checkBuildingStage() {
		int totalBuildings = 0;
		for (int i=0; i<buildingCount.length; i++) {
			totalBuildings += buildingCount[i];
		}
		int stage = (totalBuildings/Defines.NEXTSTAGEBUILDINGREQUIREMENT) + 1;
		if (stage > SuperScreen.maxStages) {
			stage = SuperScreen.maxStages;
		}
		currentStage = stage;
	}
	void sendBuildingCount(int building) {
		String output = "buildingcount;" + province + ";" + ID + ";" + building + ";" + buildingCount[building] + ";";
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			CivilisationMainClass.networkCommunication(output);
		} else if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPECLIENT) {
			CivilisationMainClass.client.outPutCommand(output);
		}
	}
}
