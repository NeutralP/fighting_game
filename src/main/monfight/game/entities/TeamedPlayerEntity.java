package src.main.monfight.game.entities;

import src.main.monfight.game.ServerGame;
import src.main.monfight.game.ServerGame.Entity;

public class TeamedPlayerEntity extends PlayerEntity {
    public enum Team {
        RED, BLUE;
    }

    private final Team team;

    public TeamedPlayerEntity(final ServerGame game, final Team team, final double x, final double y,
            final HorDirection direction, final double health, final int weapon) {
        super(game, x, y, direction, health, weapon);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public void handleCollision(final Entity otherEntity) {
        if (otherEntity instanceof final BulletEntity bulletEntity) {
            if (bulletEntity.getShooter() instanceof final TeamedPlayerEntity shooter) {
                if (shooter.team == this.team) {
                    return;
                }
            }
        }

        super.handleCollision(otherEntity);
    }
}
