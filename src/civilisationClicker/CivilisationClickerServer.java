package civilisationClicker;

import java.io.IOException;
import java.net.ServerSocket;

public class CivilisationClickerServer extends Thread{
	ServerSocket serverSocket;
	CivilisationClickerServerThread clients[];
	boolean listening, playerSlotInUse[];
	int port, players;
	public CivilisationClickerServer(int port, int players) {
		super("CivilisationClickerServer");
		this.port = port;
		this.players = players;
	}
	public void run() {
		try {
			listening = true;
			serverSocket = new ServerSocket(port);
			clients = new CivilisationClickerServerThread[players];
			playerSlotInUse = new boolean[players];
			while (listening) {
				for (int i=0; i<players; i++) {
					if (playerSlotInUse[i] == false) {
						clients[i] = new CivilisationClickerServerThread(serverSocket.accept(), i);
						clients[i].start();
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
	                + port + " or listening for a connection");
			System.out.println(e.getMessage());
			CivilisationMainMenu.createMainMenu();
		}
	}
	void stopListening() {
		listening = false;
	}
}
