package civilisationClicker;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BattleList {
	Set<Battle> battleList = new HashSet<Battle>();
	void createBattle(List<Unit> unitList, int province, int attackerID, int defenderID) {
		Battle battle = new Battle(unitList, province, attackerID, defenderID);
		CivilisationMainClass.mapScreen.colorBorder(province, Color.RED);
		if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPESINGLE) {
			String output = "battle;attackerjoin;" + province + ";" + attackerID + ";" + defenderID + ";";
			int i = 0;
			for (Unit unit : unitList) {
				output += i + ";" + unit.toString();
				i++;
			}
			CivilisationMainClass.networkCommunication(output);
		}
		battleList.add(battle);
	}
	void recieveBattleData(List<Unit> unitList, int province, int attackerID, int defenderID) {
		Battle battle = new Battle(unitList, province, attackerID, defenderID);
		CivilisationMainClass.mapScreen.colorBorder(province, Color.RED);
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "battle;attackerjoin;" + province + ";" + attackerID + ";" + defenderID + ";";
			int i = 0;
			for (Unit unit : unitList) {
				output += i + ";" + unit.toString();
				i++;
			}
			CivilisationMainClass.relayClientData(output, attackerID);
		}
		battleList.add(battle);
	}
	void joinBattle(List<Unit> unitList, int province, int attackerID, int defenderID) {
		Battle testbattle = new Battle(null, province, attackerID, defenderID);
		for (Battle battle : battleList) if (battle.equals(testbattle)) battle.defenderJoinedBattle(unitList);
		if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPESINGLE) {
			String output = "battle;defenderjoin;" + province + ";" + attackerID + ";" + defenderID + ";";
			int i = 0;
			for (Unit unit : unitList) {
				if (unit.Count > 0) output += i + ";" + unit.toString();
				i++;
			}
			CivilisationMainClass.relayClientData(output, attackerID);
		}
	}
	void recieveJoinBattleData(List<Unit> unitList, int province, int attackerID, int defenderID) {
		Battle testbattle = new Battle(null, province, attackerID, defenderID);
		for (Battle battle : battleList) if (battle.equals(testbattle)) battle.defenderJoinedBattle(unitList);
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "battle;defenderjoin;" + province + ";" + attackerID + ";" + defenderID + ";";
			int i = 0;
			for (Unit unit : unitList) {
				if (unit.Count > 0) output += i + ";" + unit.toString();
				i++;
			}
			CivilisationMainClass.relayClientData(output, attackerID);
		}
	}
	void recieveAttackerBonus(int province, double bonus, int attackerID, int defenderID) {
		Battle testbattle = new Battle(null, province, attackerID, defenderID);
		for (Battle battle : battleList) if(battle.equals(testbattle)) battle.setAttackerBonus(bonus);
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "battle;attackerlead;" + province + ";" + bonus + ";" + attackerID + ";" + defenderID + ";";
			CivilisationMainClass.relayClientData(output, attackerID);
		}
	}
	void recieveDefenderBonus(int province, double bonus, int attackerID, int defenderID) {
		Battle testbattle = new Battle(null, province, attackerID, defenderID);
		for (Battle battle : battleList) if(battle.equals(testbattle)) battle.setDefenderBonus(bonus);
		if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST) {
			String output = "battle;defenderlead;" + province + ";" + bonus + ";";
			CivilisationMainClass.relayClientData(output, defenderID);
		}
	}
	void finishBattle(int province, int attacker, int defender, boolean attackerVictor) {
		CivilisationMainClass.mapScreen.colorBorder(province, Color.BLACK);
		/*Battle testBattle = new Battle(null, province, attacker, defender);
		Iterator<Battle> iter = battleList.iterator();
		while (iter.hasNext()) {
			Battle battle = iter.next();
			if (battle.equals(testBattle)) {
				battle.createBattleOverScreen(attackerVictor);
				iter.remove();
				break;
			}
		}*/
	}
	boolean battleInProvince(int province) {
		Battle testBattle = new Battle(null, province, 0, 0);
		return battleList.contains(testBattle);
	}
	Battle getBattle(int province) {
		Battle testBattle = new Battle(null, province, 0, 0);
		Iterator<Battle> iter = battleList.iterator();
		while (iter.hasNext()) {
			Battle battle = iter.next();
			if (battle.equals(testBattle)) return battle;
		}
		return null;
	}
	void timerTick() {
		if (CivilisationMainClass.gameType != CivilisationMainClass.GAMETYPECLIENT) {
			Iterator<Battle> iter = battleList.iterator();
			while (iter.hasNext()) {
				Battle battle = iter.next();
				battle.timerTick();
				if (CivilisationMainClass.gameType == CivilisationMainClass.GAMETYPEHOST){
					String output = "battle;progress;" + battle.province + ";" + battle.attacker + ";"
							+ battle.defender + ";" + battle.tickCount + ";";
					CivilisationMainClass.networkCommunication(output);
				}
				if (battle.tickCount > Defines.BATTLEDURATION) iter.remove();
			}
		}
	}
	void updateBattleTick(int province, int attackerID, int defenderID, int progress) {
		Battle testBattle = new Battle(null, province, attackerID, defenderID);
		for (Battle battle : battleList) if(battle.equals(testBattle)) battle.updateTickCount(progress);
	}
	void clearProvince(int province) {
		Iterator<Battle> iter = battleList.iterator();
		while (iter.hasNext()) {
			Battle battle = iter.next();
			if (battle.province == province) iter.remove();
		}
	}
}
