package civilisationClicker;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class CivilisationClickerServerThread extends Thread{
	Socket socket;
	PrintWriter serverOut;
	BufferedReader serverIn;
	int playerSlot;
	String address;
	public CivilisationClickerServerThread(Socket socket, int i) {
		super("CivilisationClickerServerThread");
		this.socket = socket;
		this.playerSlot = i;
	}
	public void run() {
		try {
			serverOut = new PrintWriter(socket.getOutputStream(), true);
			serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String input;
			address = socket.getRemoteSocketAddress().toString();
			CivilisationMainClass.lobbyScreen.addPlayer(address, (playerSlot + 2));
			CivilisationMainClass.server.playerSlotInUse[playerSlot] = true;
			serverOut.println("playerID;" + (playerSlot + 2) + ";");
			while ((input = serverIn.readLine()) != null) {
				System.out.println("Client " + playerSlot + ": " + input);
				processInput(input);
			}
		} catch (IOException e) {
			if (CivilisationMainClass.lobbyActive) {
				CivilisationMainClass.lobbyScreen.removePlayer(playerSlot + 2);
				CivilisationMainClass.server.playerSlotInUse[playerSlot] = false;
			} else {
				CivilisationMainClass.server.playerSlotInUse[playerSlot] = false;
				CivilisationMainClass.removePlayer(playerSlot + 2);
			}
		}
	}
	void processInput(String input) {
		Scanner inputScanner = new Scanner(input);
		inputScanner.useDelimiter(";");
		String inputCommand = inputScanner.next();
		switch (inputCommand) {
		case "playerName":
			String name = inputScanner.next();
			CivilisationMainClass.lobbyScreen.addPlayerName(name, playerSlot + 2);
			sendLobbyData();
			inputScanner.close();
			return;
		case "colour":
			processColourData(inputScanner);
			inputScanner.close();
			return;
		case "province":
			processStartingProvinceData(inputScanner);
			inputScanner.close();
			return;
		case "lobbyready":
			processLobbyReadyData(inputScanner);
			inputScanner.close();
			return;
		case "playerwaitstatus":
			processPlayerWaitStatusData(inputScanner);
			inputScanner.close();
			return;
		case "timertick":
			processTimerTickData(inputScanner);
			inputScanner.close();
			return;
		case "colonisationcheck":
			processColonisationCheckData(inputScanner);
			inputScanner.close();
			return;
		case "provincestatus":
			processProvinceStatusData(inputScanner);
			inputScanner.close();
			return;
		case "coloniseprogress":
			processColoniseProgressData(inputScanner);
			inputScanner.close();
			return;
		case "battle":
			processBattleStatusData(inputScanner);
			inputScanner.close();
			return;
		case "provincelost":
			processProvinceLostData(inputScanner);
			inputScanner.close();
			return;
		case "buildingcount":
			processBuildingCountData(inputScanner);
			inputScanner.close();
			return;
		case "gamepause":
			processPauseData(inputScanner);
			inputScanner.close();
			return;
		case "unitpower":
			processUnitPowerData(inputScanner);
			inputScanner.close();
			return;
		}
		inputScanner.close();
		return;
	}
	void sendLobbyData() {
		int players = CivilisationMainClass.lobbyScreen.playerSlots;
		String output = "lobbyData;";
		output += CivilisationMainClass.lobbyScreen.map + ";";
		output += players + ";";
		for (int i=0; i<players; i++) {
			output += CivilisationMainClass.lobbyScreen.playerNameLabel[i].getText() + ";";
			output += CivilisationMainClass.lobbyScreen.playerIPLabel[i].getText() + ";";
			output += CivilisationMainClass.lobbyScreen.playerOrder[i] + ";";
			output += CivilisationMainClass.lobbyScreen.playerColour[i].getRed() + ";";
			output += CivilisationMainClass.lobbyScreen.playerColour[i].getGreen() + ";";
			output += CivilisationMainClass.lobbyScreen.playerColour[i].getBlue() + ";";
			output += CivilisationMainClass.lobbyScreen.playerStartProvince[i] + ";";
			output += CivilisationMainClass.lobbyScreen.playerReady[i] + ";";
		}
		outPutCommand(output);
	}
	void processColourData(Scanner inputScanner) {
		int red = inputScanner.nextInt();
		int green = inputScanner.nextInt();
		int blue = inputScanner.nextInt();
		Color playerColour = new Color(red, green, blue);
		CivilisationMainClass.lobbyScreen.recieveColourData(playerSlot + 2, playerColour);
		inputScanner.close();
	}
	void processStartingProvinceData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		CivilisationMainClass.lobbyScreen.recieveProvinceData(playerSlot + 2, province);
		inputScanner.close();
	}
	void processLobbyReadyData(Scanner inputScanner) {
		boolean ready = inputScanner.nextBoolean();
		CivilisationMainClass.lobbyScreen.recieveReadyData(playerSlot + 2, ready);
		inputScanner.close();
	}
	void processPlayerWaitStatusData(Scanner inputScanner) {
		String status = inputScanner.next();
		CivilisationMainClass.gameStartWindow.changeStatus(playerSlot + 2, status);
		inputScanner.close();
	}
	void processTimerTickData(Scanner inputScanner) {
		CivilisationMainClass.timerSynchronise(playerSlot);
		inputScanner.close();
	}
	void processColonisationCheckData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		String output = "colonisationcheck;" + province + ";";
		if (CivilisationClickerMapScreen.provinceList.get(province).owner == 0) {
			CivilisationMainClass.mapScreen.changeProvinceOwner(province, playerSlot + 2);
			CivilisationClickerMapScreen.provinceList.get(province).coloniseInProgress = true;
			CivilisationClickerMapScreen.provinceList.get(province).coloniseProgress = 0;
			CivilisationMainClass.sendColoniseData((playerSlot + 2), province);
			output += "true;";
		} else {
			output += "false;";
		}
		outPutCommand(output);
		inputScanner.close();
	}
	void processProvinceStatusData(Scanner inputScanner) {
		String status = inputScanner.next();
		int province = inputScanner.nextInt();
		switch (status) {
		case "colonise":
			CivilisationMainClass.mapScreen.changeProvinceOwner(province, playerSlot + 2);
			CivilisationClickerMapScreen.provinceList.get(province).coloniseInProgress = true;
			CivilisationClickerMapScreen.provinceList.get(province).coloniseProgress = 0;
		}
		inputScanner.close();
	}
	void processColoniseProgressData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		int progress = inputScanner.nextInt();
		CivilisationClickerMapScreen.provinceList.get(province).recieveColoniseProgressData(progress, (playerSlot));
		inputScanner.close();
	}
	void processBattleStatusData(Scanner inputScanner) {
		String battleStatus = inputScanner.next();
		switch (battleStatus) {
		case "attackerjoin":
			processBattleAttackerJoinData(inputScanner);
			break;
		case "defenderjoin":
			processBattleDefenderJoinData(inputScanner);
			break;
		case "attackerlead":
			processBattleAttackerLeadData(inputScanner);
			break;
		case "defenderlead":
			processBattleDefenderLeadData(inputScanner);
			break;
		}
	}
	void processProvinceLostData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		CivilisationMainClass.resetProvince(province, false, 0);
		String output = "provincestatus;owner;0;" + province + ";";
		CivilisationMainClass.relayClientData(output, playerSlot + 2);
		inputScanner.close();
	}
	void processBuildingCountData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		int ID = inputScanner.nextInt();
		int building = inputScanner.nextInt();
		int count = inputScanner.nextInt();
		CivilisationClickerMapScreen.provinceList.get(province).setBuildingCount(ID, building, count, playerSlot + 2);
	}
	void processPauseData(Scanner inputScanner) {
		CivilisationMainClass.pauseTimer();
		String output = "gamespeed;0;";
		CivilisationMainClass.relayClientData(output, playerSlot + 2);
	}
	void processUnitPowerData(Scanner inputScanner) {
		int ID = inputScanner.nextInt();
		int power = inputScanner.nextInt();
		CivilisationMainClass.playerList.get(ID - 1).recieveArmyPower(power);
		inputScanner.close();
	}
	void processBattleAttackerJoinData(Scanner inputScanner) {
		List<CivilisationClickerUnit> attackerUnits
			= CivilisationClickerDataBase.createNewUnitList();
		int province = inputScanner.nextInt();
		int attackerID = inputScanner.nextInt();
		int defenderID = inputScanner.nextInt();
		for (CivilisationClickerUnit unit : attackerUnits) {
			inputScanner.next();
			unit.setPower(inputScanner.nextInt());
			unit.Count = inputScanner.nextInt();
		}
		CivilisationMainClass.battleList.recieveBattleData(attackerUnits, province, attackerID, defenderID);
		inputScanner.close();
	}
	void processBattleDefenderJoinData(Scanner inputScanner) {
		List<CivilisationClickerUnit> defenderUnits
			= CivilisationClickerDataBase.createNewUnitList();
		int province = inputScanner.nextInt();
		int attackerID = inputScanner.nextInt();
		int defenderID = inputScanner.nextInt();
		for (CivilisationClickerUnit unit : defenderUnits) {
			inputScanner.next();
			unit.setPower(inputScanner.nextInt());
			unit.Count = inputScanner.nextInt();
		}
		CivilisationMainClass.battleList.recieveJoinBattleData(defenderUnits, province, attackerID, defenderID);
		inputScanner.close();
	}
	void processBattleAttackerLeadData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		double defenderBonus = inputScanner.nextDouble();
		int attackerID = inputScanner.nextInt();
		int defenderID = inputScanner.nextInt();
		CivilisationMainClass.battleList.recieveAttackerBonus(province, defenderBonus, attackerID, defenderID);
		inputScanner.close();
	}
	void processBattleDefenderLeadData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		double defenderBonus = inputScanner.nextDouble();
		int attackerID = inputScanner.nextInt();
		int defenderID = inputScanner.nextInt();
		CivilisationMainClass.battleList.recieveDefenderBonus(province, defenderBonus, attackerID, defenderID);
		inputScanner.close();
	}
	void outPutCommand(String output ) {
		serverOut.println(output);
	}
}
