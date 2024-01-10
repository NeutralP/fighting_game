package src.main.monfight;

import src.main.monfight.gui.client.ClientMainFrame;
import src.main.monfight.net.Server;

public class SinglePlayerRunner {
	public static void startGame() {
        String[] args = {}; // or provide actual arguments if needed
        Server.main(args);
        // Replace "ipAddress" and "port" with actual values
        String ipAddress = "127.0.0.1";
        int port = 1234;
        new ClientMainFrame(ipAddress, port);
    }
	
	public static void loadGame(final String ipAddress, int port) {
		new ClientMainFrame(ipAddress, port);
	}
	
    public static void main(final String[] args) {
//        Server.main(args);
        // Replace "ipAddress" and "port" with actual values
        String ipAddress = "127.0.0.1";
        int port = 1234;
        new ClientMainFrame(ipAddress, port);
    }
}
