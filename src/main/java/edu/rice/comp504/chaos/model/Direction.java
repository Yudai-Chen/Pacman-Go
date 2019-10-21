package edu.rice.comp504.chaos.model;

/**
 * The intended change in coordination.
 */
public class Direction {
    private Coordination dir;
    private String directionName;
    /**
     * Constructor.
     * @param directionName the name of the direction.
     */
    public Direction(String directionName) {
        this.directionName = directionName;
        switch (directionName) {
            case "left":
                dir = new Coordination(-1, 0);
                break;
            case "up":
                dir = new Coordination(0, -1);
                break;
            case "right":
                dir = new Coordination(1, 0);
                break;
            case "down":
                dir = new Coordination(0, 1);
                break;
            default:
                dir = new Coordination(0, 0);
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
}
