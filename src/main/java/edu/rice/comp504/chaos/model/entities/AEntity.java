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
    private Direction dir;
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
        this.speed = speed;
        this.size = size;
        this.dir = dir;
        this.coord = Utilities.loc2Coord(loc);
    }

    Coordination getLoc() {
        return loc;
    }

    Coordination getCoord() {
        return coord;
    }

    public Direction getDir() {
        return dir;
    }

    /**
     * Move to intended destination or stop.
     */
    public void move() {
        if (loc.isRegularSpot()) {
            if (isIntendedDirectionValid()) {
                move(computeIntendedDestination());
            } else {
                dir = new Direction("stop");
            }
        } else {
            move(computeIntendedDestination());
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
     * Compute the intended destination according to the location, speed and direction.
     * @return the intended destination.
     */
    Coordination computeIntendedDestination() {
        return new Coordination(loc.x + speed * dir.getDirX(), loc.y + speed * dir.getDirY());
    }

    /**
     * Set the intended direction of the entity.
     * @param dir the intended direction.
     */
    void setDirection(Direction dir) {
        this.dir = dir;
    }

    /**
     * Test whether the intended direction is a valid direction.
     * @return whether the intended direction is a valid direction.
     */
    boolean isIntendedDirectionValid() {
        int item = Utilities.getMazeItem(coord.x + dir.getDirX(), coord.y + dir.getDirY());
        return item == 0;
    }
}
