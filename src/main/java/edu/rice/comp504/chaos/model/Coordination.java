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
     * Get the minimum distance to another coordination in the maze.
     * @param another another coordination.
     * @return min distance.
     */
    public double minDistance(Coordination another) {
        Coordination leftMost = new Coordination(1, 9);
        Coordination rightMost = new Coordination(58, 9);
        return Math.min(Math.min(distance(another), distance(leftMost) + rightMost.distance(another)), distance(rightMost) + leftMost.distance(another));
    }

    /**
     * Test if a physical address is a regular spot in the maze.
     * @return whether a physical address is a regular spot in the maze.
     */
    public boolean isRegularSpot() {
        return ((x - 10) % 20 == 0) && ((y - 10) % 20 == 0);
    }

    /**
     * If a coordination (x, y) in the maze is space (including inner space or border).
     * @return if it is a space.
     */
    private boolean isSpace(int x, int y) {
        return Utilities.getMazeItem(x, y) == 0 || Utilities.getMazeItem(x, y) == 9;
    }

    /**
     * Test if a logical address is a crossing in the maze.
     * @return whether a logical address is a crossing in the maze.
     */
    public boolean isCrossing() {
        return (!((isSpace(x - 1, y) == isSpace(x + 1, y)) && (isSpace(x, y - 1) == isSpace(x, y + 1)))) || (isSpace(x - 1, y) && isSpace(x + 1, y) && isSpace(x, y - 1) && isSpace(x, y - 1));
    }

    /**
     * Test if a physical address is in the ghost spawn area.
     * @return if a physical address is in the ghost spawn area.
     */
    public boolean isGhostSpawnArea() {
        return x >= Settings.spawnXMin && x <=Settings.spawnXMax && y >= Settings.spawnYMin && y <= Settings.spawnYMax;
    }
}
