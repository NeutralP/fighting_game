package src.main.monfight.game.entities;

import src.main.monfight.game.ServerGame.Entity;
import src.main.monfight.game.entities.HorDirectionedEntity.HorDirection;

public class WeaponEntity extends Entity {
    private static final long serialVersionUID = 348408736704866955L;

    private final PlayerEntity owner;
    private final int weapon; //1 is sword, other is gun
    
    public WeaponEntity(final PlayerEntity owner, final int weapon) {
    	super(owner.getGame(), 1., 2., Double.NaN, Double.NaN);
        // the 3 and 2 are dimensions of the pistol. the division by 8 is because the
        // player is 16x8 pixels and considered 2x1 in the game

        this.owner = owner;
        this.weapon = weapon;
    }

    @Override
    public void tick() {
    }

    @Override
    public void handleCollision(final Entity otherEntity) {
    }

    @Override
    public double getX() {
        return getCenterX() - getWidth() / 2;
    }

    @Override
    public double getY() {
        return owner.getBottomY() + 0.;
    }

    @Override
    public double getCenterX() {
        return owner.getCenterX() + owner.getHorDirection().getSign() * getXOffset();
    }

    private double getXOffset() {
        return owner.getWidth() / 2. + this.getWidth() / 2. + 1. / 8.;
    }

    public HorDirection getHorDirection() {
        return owner.getHorDirection();
    }
    
    public int getWeapon() {
    	return weapon;
    }
}
