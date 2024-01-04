package src.main.monfight.game.entities;

import src.main.monfight.game.ServerGame;
import src.main.monfight.game.ServerGame.Entity;

public class BulletSwordEntity extends Entity implements HorDirectionedEntity {
    private static final long serialVersionUID = 2690256651740709424L;

    private final PlayerEntity shooter;
    private int age = 0;
    private final double horVel;

    public BulletSwordEntity(final ServerGame game, final PlayerEntity shooter, final double x, final double y,
            final double horVel,
            final XAxisType xAxisType, final YAxisType yAxisType) {
        /*
         * TODO: add a rectangle class, this is becoming a problem
         */
        super(game, 2., 3., x, y);

        this.shooter = shooter;

        switch (xAxisType) {
            case LEFT:
                setX(x);
                break;
            case RIGHT:
                setX(x - this.getWidth());
                break;
            case CENTER:
                // idk
                break;
        }

        switch (yAxisType) {
            case BOTTOM:
                setY(y);
                break;
            case TOP:
                setY(y - this.getHeight());
                break;
            case CENTER:
                // idk
                break;
        }

        age = 0;
        this.horVel = horVel;
    }

    @Override
    public void tick() {
        if (age >= ServerGame.GameSettings.BULLET_SWORD_LIFESPAN) {
            getGame().removeEntity(getId());
            return;
        }

        shiftX(horVel);

        age++;
    }

    @Override
    public void handleCollision(final Entity otherEntity) {
        // death should be handled enemy side
//        if (otherEntity instanceof PlatformEntity) {
//            getGame().removeEntity(getId());
//        }
        if(otherEntity instanceof PlayerEntity) {
            getGame().removeEntity(getId());
        }
    }

    @Override
    public HorDirection getHorDirection() {
        return horVel > 0 ? HorDirection.RIGHT : HorDirection.LEFT;
    }

    public PlayerEntity getShooter() {
        return shooter;
    }

    @Override
    public void setHorDirection(final HorDirection horDirection) {
        throw new UnsupportedOperationException("Unimplemented method 'setHorDirection'");
    }
}
