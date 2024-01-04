package src.main.monfight.game.entities;

import src.main.monfight.game.ServerGame;
import src.main.monfight.game.ServerGame.Entity;
import src.main.monfight.game.ServerGame.GameSettings;
import src.main.monfight.game.action.Action;

public class PlayerEntity extends Entity implements HorDirectionedEntity, GravitationalEntity {
    private static final long serialVersionUID = -3022640676588904126L;


    private HorDirection horDirection;
    private double xVel, yVel;
    private double health;
    private WeaponEntity weaponEntity;
    private int weapon = 1;

    public PlayerEntity(final ServerGame game, final double x, final double y,
            final HorDirection direction, final double h) {
        super(game, 1, 2, x, y);

        this.horDirection = direction;

        xVel = 0;
        yVel = 0;
        health = h;
        this.weaponEntity = new WeaponEntity(this, weapon);
    }

    @Override
    public double getYVel() {
        return yVel;
    }

    @Override
    public void setYVel(final double yVel) {
        this.yVel = yVel;
    }

    @Override
    public double getXVel() {
        return xVel;
    }

    @Override
    public void setXVel(final double xVel) {
        this.xVel = xVel;
    }
    
    public double getHealth() {
        return health;
    }
    
    public void updateHealth(double amount) {
        health += amount;

        // Đảm bảo máu không vượt quá giới hạn
        health = Math.min(GameSettings.MAX_HEALTH, health);
        health = Math.max(0, health);
    }

    
    @Override
    public HorDirection getHorDirection() {
        return horDirection;
    }

    @Override
    public void setHorDirection(final HorDirection horDirection) {
        this.horDirection = horDirection;
    }

    @Override
    public void tick() {
        for (final Action action : getActionSet().getInstantActions()) {
            switch (action) {
                case JUMP: {
                    if (getYVel() != 0) {
                        break;
                    }
                    shiftYVel(GameSettings.JUMP_VEL);
                    break;
                }

                case SHOOT: {
                	if (weapon == 1) {
                		sword_shoot();
                	}
                	else {
                		gun_shoot();
                	}
                    break;
                }
                
                case SWITCH: {
                	getGame().removeEntity(weaponEntity.getId());
                	if (weapon == 1) {
                		weapon = 0;
                		weaponEntity = new WeaponEntity(this, 0);
                	}
                	else {
                		weapon = 1;
                		weaponEntity = new WeaponEntity(this, 1);
                	}
                    break;
                }

                default:
                    ServerGame.getLogger().warning("Unknown action \"" + action + "\" in instant actions.");
                    break;
            }
        }
        getActionSet().getInstantActions().clear();

        for (final Action action : getActionSet().getLongActions()) {
            switch (action) {
                case LEFT_WALK: {
                    setHorDirection(HorDirection.LEFT);
                    shiftX(-GameSettings.WALK_SPEED);
                    break;
                }

                case RIGHT_WALK: {
                    setHorDirection(HorDirection.RIGHT);
                    shiftX(GameSettings.WALK_SPEED);
                    break;
                }

                default:
                    ServerGame.getLogger().warning("Unknown action \"" + action + "\" in long actions.");
                    break;
            }
        }

        // physics
        applyGravity();
        applyVelocity();
    }

    @Override
    public void handleCollision(final Entity otherEntity) {
        if (otherEntity instanceof PlatformEntity) {
            final Vector2D collisionNormal = getCollisionNormal(otherEntity);

            if (collisionNormal.getX() > 0) {
                // set right of this to the left of other
                this.setX(otherEntity.getX() - this.getWidth());
                this.setXVel(0);
            } else if (collisionNormal.getX() < 0) {
                // set left of this to the right of other
                this.setX(otherEntity.getX() + otherEntity.getWidth());
                this.setXVel(0);
            }

            if (collisionNormal.getY() > 0) {
                // set top of this to the bottom of other
                this.setY(otherEntity.getY() - this.getHeight());
                this.setYVel(0);
            } else if (collisionNormal.getY() < 0) {
                // set bottom of this to the top of other
                this.setY(otherEntity.getY() + otherEntity.getHeight());
                this.setYVel(0);
            }
            
        } else if (otherEntity instanceof BulletEntity) {
        	updateHealth(-10);
        	getGame().removeEntity(otherEntity.getId());
        	if (this.health <= 0) {
        		die();
            }
        }
	    else if (otherEntity instanceof BulletSwordEntity) {
	    	updateHealth(-30);
	    	getGame().removeEntity(otherEntity.getId());
	    	if (this.health <= 0) {
	    		die();
	        }
	    }
    }
    
    public void gun_shoot() {
    	if (weaponEntity.getHorDirection() == HorDirection.LEFT) {
            new BulletEntity(getGame(), this, weaponEntity.getLeftX()-0.2, weaponEntity.getTopY()-0.35,
                    -GameSettings.BULLET_SPEED,
                    XAxisType.LEFT, YAxisType.TOP);
        } else if (weaponEntity.getHorDirection() == HorDirection.RIGHT) {
            new BulletEntity(getGame(), this, weaponEntity.getRightX()+0.2, weaponEntity.getTopY()-0.35,
                    GameSettings.BULLET_SPEED,
                    XAxisType.RIGHT, YAxisType.TOP);
        } else {
            ServerGame.getLogger().warning("Unknown direction \"" + weaponEntity.getHorDirection() + "\".");
        }
    }
    
    public void sword_shoot() {
    	updateHealth(-0.5);
    	if (weaponEntity.getHorDirection() == HorDirection.LEFT) {
            new BulletSwordEntity(getGame(), this, weaponEntity.getLeftX()-1, weaponEntity.getTopY()+1,
                    -GameSettings.SWORD_BULLET_SPEED,
                    XAxisType.LEFT, YAxisType.TOP);
        } else if (weaponEntity.getHorDirection() == HorDirection.RIGHT) {
            new BulletSwordEntity(getGame(), this, weaponEntity.getRightX()+1, weaponEntity.getTopY()+1,
                    GameSettings.SWORD_BULLET_SPEED,
                    XAxisType.RIGHT, YAxisType.TOP);
        } else {
            ServerGame.getLogger().warning("Unknown direction \"" + weaponEntity.getHorDirection() + "\".");
        }
    }
    
    private void die() {
      getGame().removeEntity(getId());
      getGame().removeEntity(this.weaponEntity.getId());
    }
}

// TODO: add tree like structure to entities so that players can officially own
// guns