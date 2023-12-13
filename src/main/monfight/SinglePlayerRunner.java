package src.main.monfight;

import src.main.monfight.gui.client.ClientMainFrame;
import src.main.monfight.net.Server;

public class SinglePlayerRunner {
    public static void main(final String[] args) {
        Server.main(args);
        ClientMainFrame.main(args);
    }
}
