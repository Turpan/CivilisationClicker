package civilisationClicker;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Battle {
	static final int BATTLEDURATION = 600;
	List<Unit> attackerTroops;
	List<Unit> defenderTroops;
	int attacker;
	int defender;
	int province;
	int tickCount;
	double attackerWinChance;
	double defenderBonus;
	double attackerBonus;
	double attackerPointTotal;
	double defenderPointTotal;
	boolean defenderJoined;
	Battle(List<Unit> attackerTroops, int province, int attacker, int defender) {
		this.attackerTroops = attackerTroops;
		this.province = province;
		this.attacker = attacker;
		this.defender = defender;
	}
	void defenderJoinedBattle(List<Unit> defenderTroops) {
		this.defenderTroops = defenderTroops;
		defenderJoined = true;
	}
	void setDefenderBonus(double defenderBonus) {
		this.defenderBonus = defenderBonus;
		calculatePower();
	}
	void setAttackerBonus(double attackerBonus) {
		this.attackerBonus = attackerBonus;
		calculatePower();
	}
	void calculatePower() {
		double attackerpower = 0;
		double defenderpower = 0;
		for (Unit unit : attackerTroops) attackerpower += (unit.Power * unit.Count);
		attackerpower = attackerpower * (attackerBonus + 1);
		attackerPointTotal = attackerpower;
		if (defenderJoined) {
			for (Unit unit : defenderTroops) defenderpower += (unit.Power * unit.Count);
			defenderpower = defenderpower * (defenderBonus + 1);
			defenderPointTotal = defenderpower;
		}
		calculateWinChance();
	}
	void calculateWinChance() {
		attackerWinChance = 0;
		if (attackerPointTotal > defenderPointTotal) {
			if (attackerPointTotal >= (defenderPointTotal * 2)) {
				attackerWinChance = 100;
			} else {
				double chance = attackerPointTotal / defenderPointTotal;
				chance -= 1;
				chance = chance/2;
				chance = chance * 100;
				attackerWinChance = chance + 50;
			}
		} else if (attackerPointTotal < defenderPointTotal) {
			if (defenderPointTotal >= (attackerPointTotal * 2)) {
				attackerWinChance = 0;
			} else {
				double chance = defenderPointTotal / attackerPointTotal;
				chance -= 1;
				chance = chance/2;
				chance = chance * 100;
				attackerWinChance = 50 - chance;
			}
		} else {
			attackerWinChance = 50;
		}
	}
	void calculateVictor() {
		boolean attackerVictor = false;
		String output = "";
		int randomNum = ThreadLocalRandom.current().nextInt(1, 101);
		CivilisationMainClass.mapScreen.colorBorder(province, Color.BLACK);
		if (randomNum <= attackerWinChance) {
			CivilisationMainClass.resetProvince(province, true, attacker);
			attackerVictor = true;
			output = "provincestatus;owner;" + attacker + ";" + province + ";";
			CivilisationMainClass.networkCommunication(output);
		} else {
			attackerVictor = false;
		}
		output = "battle;finished;" + province + ";" + attacker + ";" + defender + ";" + attackerVictor + ";";
		CivilisationMainClass.networkCommunication(output);
		CivilisationMainClass.battleList.finishBattle(province, attacker, defender, attackerVictor);
	}
	void createBattleOverScreen(boolean attackerVictor) {
		CivilisationMainClass.resourceBar.armyScreen.createFinishBattleScreen(attackerTroops, defenderTroops, province, attacker, defender, attackerVictor);
	}
	void timerTick() {
		tickCount += 1;
		calculatePower();
		if (tickCount >= BATTLEDURATION) calculateVictor();
	}
	public void updateTickCount(int progress) {
		tickCount = progress;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + province;
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
		Battle other = (Battle) obj;
		if (province != other.province)
			return false;
		return true;
	}
}
