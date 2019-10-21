package edu.rice.comp504.chaos.model;

import java.awt.*;
import java.io.Serializable;

/**
 * The 2-dimension coordination. A pair of int. Can be logical or physical.
 */
public class Coordination implements Cloneable, Serializable {
    public int x;
    public int y;

    /**
     * Constructor.
     * @param x x-coordination.
     * @param y y-coordination.
     */
    public Coordination(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the distance to another coordination.
     * @param another another coordination.
     * @return distance.
     */
    public double distance(Coordination another) {
        return new Point(x, y).distance(new Point(another.x, another.y));
    }

    /**
     * Test if a physical address is a regular spot in the maze.
     * @return whether a physical address is a regular spot in the maze.
     */
    public boolean isRegularSpot() {
        return ((x - 10) % 20 == 0) && ((y - 10) % 20 == 0);
    }

    /**
     * Test if a logical address is a crossing in the maze.
     * @return whether a physical address is a regular spot in the maze.
     */
    public boolean isCrossing() {
        return false;
    }
}
