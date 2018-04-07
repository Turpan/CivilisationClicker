package civilisationClicker;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread{
	ServerSocket serverSocket;
	ServerThread clients[];
	boolean listening, playerSlotInUse[];
	int port, players;
	public Server(int port, int players) {
		super("CivilisationClickerServer");
		this.port = port;
		this.players = players;
	}
	public void run() {
		try {
			listening = true;
			serverSocket = new ServerSocket(port);
			clients = new ServerThread[players];
			playerSlotInUse = new boolean[players];
			while (listening) {
				for (int i=0; i<players; i++) {
					if (playerSlotInUse[i] == false) {
						clients[i] = new ServerThread(serverSocket.accept(), i);
						clients[i].start();
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
	                + port + " or listening for a connection");
			System.out.println(e.getMessage());
			MainMenu.createMainMenu();
		}
	}
	void stopListening() {
		listening = false;
	}
}
