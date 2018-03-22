package civilisationClicker;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class CivilisationClickerClient extends Thread{
	String hostName;
	int portNumber;
	PrintWriter out;
	boolean kicked;
	public CivilisationClickerClient(String hostName, int portNumber){
		super("CivilisationClickerClient");
		this.portNumber = portNumber;
		this.hostName = hostName;
	}
	public void run() {
		try {
			Socket clientSocket = new Socket(hostName, portNumber);
		    out = new PrintWriter(clientSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String fromServer;
			out.println("playerName;" + CivilisationMainClass.playerName + ";");
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
				processInput(fromServer);
			}
			clientSocket.close();
		} catch (UnknownHostException e) {
			CivilisationMainClass.connectionFailed();
		} catch (IOException e) {
			CivilisationMainClass.connectionFailed();
			String message = "";
			if (!kicked) {
				message = "You lost connection to the host.";
			} else {
				message = "You have been kicked by the host.";
				kicked = false;
			}
			CivilisationMainClass.createInfoWindow(message);
		}
	}
	void processInput(String input) {
		Scanner inputScanner = new Scanner(input);
		inputScanner.useDelimiter(";");
		String inputCommand = inputScanner.next();
		switch (inputCommand) {
		case "lobbyData":
			processLobbyData(inputScanner);
			return;
		case "playerID":
			setPlayerID(inputScanner);
			return;
		case "colour":
			processColourData(inputScanner);
			return;
		case "province":
			processStartingProvinceData(inputScanner);
			return;
		case "gamestart":
			processGameStartData(inputScanner);
			return;
		case "playerwaitstatus":
			processPlayerWaitStatusData(inputScanner);
			return;
		case "gametimerbegin":
			processGameTimerBeginData(inputScanner);
			return;
		case "timertick":
			processTimerTickData(inputScanner);
			return;
		case "colonisationcheck":
			processColonisationCheckData(inputScanner);
			return;
		case "coloniseprogress":
			processColoniseProgressData(inputScanner);
			return;
		case "provincestatus":
			processProvinceStatusData(inputScanner);
			return;
		case "battle":
			processBattleStatusData(inputScanner);
			return;
		case "lobbyplayerleft":
			processPlayerLeftLobbyData(inputScanner);
			return;
		case "playerleft":
			processPlayerLeftData(inputScanner);
			return;
		case "kicked":
			kicked = true;
			inputScanner.close();
			return;
		case "buildingcount":
			processBuildingCountData(inputScanner);
			return;
		case "gamespeed":
			processGameSpeedData(inputScanner);
			return;
		case "unitpower":
			processUnitPowerData(inputScanner);
			return;
		}
		inputScanner.close();
		return;
	}
	void processLobbyData(Scanner inputScanner) {
		int map = inputScanner.nextInt();
		int playerSlots = inputScanner.nextInt();
		CivilisationMainClass.joinLobby(playerSlots, map);
		String[] playerNames = new String[playerSlots];
		String[] playerIPs = new String[playerSlots];
		int[] playerOrder = new int[playerSlots];
		Color[] playerColor = new Color[playerSlots];
		int[] playerStartProvince = new int[playerSlots];
		boolean[] playerReady = new boolean[playerSlots];
		for (int i=0; i<playerSlots; i++) {
			playerNames[i] = inputScanner.next();
			playerIPs[i] = inputScanner.next();
			playerOrder[i] = inputScanner.nextInt();
			int red = inputScanner.nextInt();
			int green = inputScanner.nextInt();
			int blue = inputScanner.nextInt();
			playerColor[i] = new Color(red, green, blue);
			playerStartProvince[i] = inputScanner.nextInt();
			playerReady[i] = inputScanner.nextBoolean();
		}
		CivilisationMainClass.lobbyScreen.fillLobbyFromServer(playerNames, playerIPs, playerOrder, playerColor, playerStartProvince, playerReady);
		inputScanner.close();
	}
	void setPlayerID(Scanner inputScanner) {
		int playerID = inputScanner.nextInt();
		CivilisationMainClass.playerID = playerID;
	}
	void processColourData(Scanner inputScanner) {
		int playerID = inputScanner.nextInt();
		int red = inputScanner.nextInt();
		int green = inputScanner.nextInt();
		int blue = inputScanner.nextInt();
		Color playerColour = new Color(red, green, blue);
		CivilisationMainClass.lobbyScreen.recieveColourData(playerID, playerColour);
		inputScanner.close();
	}
	void processStartingProvinceData(Scanner inputScanner) {
		int playerID = inputScanner.nextInt();
		int province = inputScanner.nextInt();
		CivilisationMainClass.lobbyScreen.recieveProvinceData(playerID, province);
		inputScanner.close();
	}
	void processLobbyReadyData(Scanner inputScanner) {
		int playerID = inputScanner.nextInt();
		boolean ready = inputScanner.nextBoolean();
		CivilisationMainClass.lobbyScreen.recieveReadyData(playerID, ready);
		inputScanner.close();
	}
	void processGameStartData(Scanner inputScanner) {
		int players = inputScanner.nextInt();
		int map = inputScanner.nextInt();
		String[] playerNames = new String[players];
		int[] startProvince = new int[players];
		Color[] playerColour = new Color[players];
		for (int i=0; i<players; i++) {
			playerNames[i] = inputScanner.next();
			startProvince[i] = inputScanner.nextInt();
			int red = inputScanner.nextInt();
			int green = inputScanner.nextInt();
			int blue = inputScanner.nextInt();
			playerColour[i] = new Color(red, green, blue);
		}
		CivilisationMainClass.createMultiplayerGame(players, playerNames, startProvince, playerColour, map);
		inputScanner.close();
	}
	void processPlayerWaitStatusData(Scanner inputScanner) {
		int player = inputScanner.nextInt();
		String status = inputScanner.next();
		CivilisationMainClass.gameStartWindow.changeStatus(player, status);
		inputScanner.close();
	}
	void processGameTimerBeginData(Scanner inputScanner) {
		CivilisationMainClass.mainLayeredPanel.remove(CivilisationMainClass.gameStartWindow);
		CivilisationMainClass.mainLayeredPanel.revalidate();
		CivilisationMainClass.mainLayeredPanel.repaint();
		inputScanner.close();
	}
	void processTimerTickData(Scanner inputScanner) {
		CivilisationMainClass.timerTick();
		inputScanner.close();
	}
	void processColonisationCheckData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		boolean check = inputScanner.nextBoolean();
		if (check) CivilisationMainClass.playerList.get(CivilisationMainClass.playerID).startColonisation(province);
		inputScanner.close();
	}
	void processColoniseProgressData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		int progress = inputScanner.nextInt();
		CivilisationClickerMapScreen.provinceList.get(province).recieveColoniseProgressData(progress, 0);;
		inputScanner.close();
	}
	void processProvinceStatusData(Scanner inputScanner) {
		String status = inputScanner.next();
		int player = inputScanner.nextInt();
		int province = inputScanner.nextInt();
		switch (status) {
		case "colonise":
			CivilisationMainClass.mapScreen.changeProvinceOwner(province, player);
			CivilisationClickerMapScreen.provinceList.get(province).coloniseInProgress = true;
			CivilisationClickerMapScreen.provinceList.get(province).coloniseProgress = 0;
		case "owner":
			CivilisationMainClass.mapScreen.changeProvinceOwner(province, player);
			if (CivilisationMainClass.playerID == player) {
				CivilisationMainClass.resetProvince(province, true, CivilisationMainClass.playerID);
			}
		}
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
			inputScanner.close();
			break;
		case "progress":
			processBattleProgressData(inputScanner);
			break;
		case "finished":
			processBattleFinishedData(inputScanner);
			break;
		}
	}
	void processPlayerLeftLobbyData(Scanner inputScanner) {
		int player = inputScanner.nextInt();
		CivilisationMainClass.lobbyScreen.removePlayer(player);
		inputScanner.close();
	}
	void processPlayerLeftData(Scanner inputScanner) {
		int player = inputScanner.nextInt();
		CivilisationMainClass.removePlayer(player);
		inputScanner.close();
	}
	void processBuildingCountData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		int ID = inputScanner.nextInt();
		int building = inputScanner.nextInt();
		int count = inputScanner.nextInt();
		CivilisationClickerMapScreen.provinceList.get(province).setBuildingCount(ID, building, count, 0);
		inputScanner.close();
	}
	void processGameSpeedData(Scanner inputScanner) {
		int speed = inputScanner.nextInt();
		CivilisationMainClass.recieveGameSpeedData(speed);
		inputScanner.close();
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
	void processBattleProgressData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		int attackerID = inputScanner.nextInt();
		int defenderID = inputScanner.nextInt();
		int progress = inputScanner.nextInt();
		CivilisationMainClass.battleList.updateBattleTick(province, attackerID, defenderID, progress);
		inputScanner.close();
	}
	void processBattleFinishedData(Scanner inputScanner) {
		int province = inputScanner.nextInt();
		int attackerID = inputScanner.nextInt();
		int defenderID = inputScanner.nextInt();
		boolean attackerVictor = inputScanner.nextBoolean();
		CivilisationMainClass.battleList.finishBattle(province, attackerID, defenderID, attackerVictor);
		inputScanner.close();
	}
	void outPutCommand(String output ) {
		out.println(output);
	}
}
