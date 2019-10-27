package edu.rice.comp504.chaos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The intended change in Coordinate.
 */
public class Direction {
    private Coordinate dir;
    private String directionName;
    /**
     * Constructor.
     * @param directionName the name of the direction.
     */
    public Direction(String directionName) {
        this.directionName = directionName;
        switch (directionName) {
            case "left":
                dir = new Coordinate(-1, 0);
                break;
            case "up":
                dir = new Coordinate(0, -1);
                break;
            case "right":
                dir = new Coordinate(1, 0);
                break;
            case "down":
                dir = new Coordinate(0, 1);
                break;
            default:
                dir = new Coordinate(0, 0);
                break;
        }
    }

    /**
     * Get the X-dimension intend.
     * @return the X-dimension intend.
     */
    public int getDirX() {return dir.x;}

    /**
     * Get the Y-dimension intend.
     * @return the Y-dimension intend.
     */
    public int getDirY() {return dir.y;}

    /**
     * Get the name of the direction.
     * @return the name of the direction.
     */
    public String getDirName() {return directionName;}

    /**
     * Get the reverse of the direction.
     * @return the reverse direction.
     */
    public Direction getReverse() {
        List<String> allDirections = new ArrayList<>();
        // Do not change the order.
        allDirections.add("left");
        allDirections.add("up");
        allDirections.add("down");
        allDirections.add("right");

        return new Direction(allDirections.get(3 - allDirections.indexOf(getDirName())));
    }
}
