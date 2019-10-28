package edu.rice.comp504.chaos.model;

import java.awt.*;
import java.io.Serializable;

/**
 * The 2-dimension Coordinate. A pair of int. Can be logical or physical.
 */
public class Coordinate implements Cloneable, Serializable {
    public int x;
    public int y;

    /**
     * Constructor.
     * @param x x-Coordinate.
     * @param y y-Coordinate.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the distance to another Coordinate.
     * @param another another Coordinate.
     * @return distance.
     */
    public double distance(Coordinate another) {
        return new Point(x, y).distance(new Point(another.x, another.y));
    }

    /**
     * Get the minimum distance to another Coordinate in the maze.
     * @param another another Coordinate.
     * @return min distance.
     */
    public double minDistance(Coordinate another) {
        Coordinate leftMost = new Coordinate(1, 9);
        Coordinate rightMost = new Coordinate(58, 9);
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
     * If a Coordinate (x, y) in the maze is space (including inner space or border).
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
        return x >= Settings.spawnXMin && x <= Settings.spawnXMax && y >= Settings.spawnYMin && y <= Settings.spawnYMax;
    }
}
