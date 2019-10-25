package edu.rice.comp504.chaos.model.entities;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;
import edu.rice.comp504.chaos.model.Game;
import edu.rice.comp504.chaos.model.Utilities;

import java.awt.*;
import java.io.Serializable;

/**
 * An entity which is in the game.
 */
public abstract class AEntity implements Cloneable, Serializable {
    private Coordination loc;
    private Coordination startLoc;
    private Direction dir;
    private Direction startDir;
    private Coordination coord;
    private int speed;
    private int size;

    /**
     * Constructor.
     * @param startLoc the initial coordination of the entity.
     * @param speed the speed of the entity.
     * @param size the size of the entity.
     * @param dir the initial direction.
     */
    AEntity(Coordination startLoc, int speed, int size, Direction dir) {
        this.loc = startLoc;
        this.startLoc = startLoc;
        this.speed = speed;
        this.size = size;
        this.dir = dir;
        this.startDir = dir;
        this.coord = Utilities.loc2Coord(loc);
    }

    /**
     * Get the location.
     * @return the location.
     */
    public Coordination getLoc() {
        return loc;
    }

    /**
     * Get the initial location.
     * @return the initial location.
     */
    private Coordination getStartLoc() {
        return startLoc;
    }

    /**
     * Set the location.
     * @param loc the location.
     */
    private void setLoc(Coordination loc) {
        this.loc = loc;
    }

    /**
     * Get the coordination.
     * @return the coordination.
     */
    public Coordination getCoord() {
        return coord;
    }

    /**
     * Get the direction.
     * @return the direction.
     */
    public Direction getDir() {
        return dir;
    }

    /**
     * Set the speed of the entity.
     * @param speed the speed.
     */
    void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Set the direction.
     * @param dir the direction.
     */
    public void setDir(Direction dir) {
        this.dir = dir;
    }

    /**
     * Move to intended destination or stop.
     */
    abstract public void move();

    /**
     * Move on the regular spot. Regular spot is the center of each unit cell of the canvas.
     */
    void moveOnRegularSpot() {
        if (getItemOnIntendedDirection() == 0) {
            move(computeIntendedDestination());
        } else if (getItemOnIntendedDirection() == 9) {
            if (getCoord().x == 1) {
                moveToCoord(new Coordination(58, 9));
            } else {
                moveToCoord(new Coordination(1, 9));
            }
        }
    }

    /**
     * Move to a specific destination.
     * @param destination a specific destination.
     */
    void move(Coordination destination) {
        this.loc = destination;
        this.coord = Utilities.loc2Coord(loc);
    }

    /**
     * Move to a specific coordination.
     */
    void moveToCoord (Coordination destination) {
        this.loc = Utilities.coord2Loc(destination);
        this.coord = destination;
    }

    /**
     * Compute the intended destination according to the location, speed and direction.
     * @return the intended destination.
     */
    Coordination computeIntendedDestination() {
        int oldX = loc.x;
        int oldY = loc.y;
        int x = loc.x + speed * dir.getDirX();
        int y = loc.y + speed * dir.getDirY();
        if ((x - 10) / 20 != (oldX - 10) / 20) {
            if (x > oldX) {
                x = (x - 10) / 20 * 20 + 10;
            } else {
                if ((oldX - 10) % 20 != 0) {
                    x = ((x - 10) / 20 + 1) * 20 + 10;
                }
            }
        }
        if ((y - 10) / 20 != (oldY - 10) / 20) {
            if (y > oldY) {
                y = (y - 10) / 20 * 20 + 10;
            } else {
                if ((oldY - 10) % 20 != 0) {
                    y = ((y - 10) / 20 + 1) * 20 + 10;
                }
            }
        }
        return new Coordination(x, y);
    }

    /**
     * Set the intended direction of the entity.
     * @param dir the intended direction.
     */
    void setDirection(Direction dir) {
        this.dir = dir;
    }

    /**
     * Get the item on the next intended position.
     * @return the item.
     */
    private int getItemOnIntendedDirection() {
        return Utilities.getMazeItem(coord.x + dir.getDirX(), coord.y + dir.getDirY());
    }

    /**
     * Rest the entity to its initial location.
     */
    public void resetLoc() {
        setLoc(getStartLoc());
        setDirection(startDir);
    }
}
