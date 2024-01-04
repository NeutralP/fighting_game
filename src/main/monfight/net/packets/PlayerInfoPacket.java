package src.main.monfight.net.packets;

import src.main.monfight.game.ServerGame.Entity;
import src.main.monfight.game.action.ActionSet;

public class PlayerInfoPacket extends ClientPacket {
    private static final long serialVersionUID = -1234567890123456789L;

    private final String playerName;
    private final int weapon;

    public PlayerInfoPacket(String playerName, int weapon) {
        this.playerName = playerName;
        this.weapon = weapon;
    }

    public String getPlayerName() {
        return playerName;
    }
    public int getWeapon() {
        return weapon;
    }
}
