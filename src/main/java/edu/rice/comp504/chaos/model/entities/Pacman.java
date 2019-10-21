package edu.rice.comp504.chaos.model.entities;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;
import edu.rice.comp504.chaos.model.Settings;
import edu.rice.comp504.chaos.model.Utilities;


/**
 * The pacman which is controlled by the player.
 */
public class Pacman extends AEntity {
    private int credit;
    private Direction playerAction;

    /**
     * Constructor.
     * @param startLoc the initial coordination of pacman.
     * @param speed the speed of pacman.
     * @param size the size of pacman.
     */
    public Pacman(Coordination startLoc, int speed, int size) {
        super(startLoc, speed, size, new Direction(Settings.pacmanStartDir));
        credit = 0;
        playerAction = new Direction(Settings.pacmanStartDir);
    }

    /**
     * Move to intended destination or stop.
     */
    public void move() {
        if (getLoc().isRegularSpot()) {
            if (isPlayerActionValid()) {
                setDirection(playerAction);
                move(computeIntendedDestination());
            } else {
                if (isIntendedDirectionValid()) {
                    move(computeIntendedDestination());
                } else {
                    setDirection(new Direction("stop"));
                }
            }
        } else {
            move(computeIntendedDestination());
        }
    }

    /**
     * Record the most recent player action.
     * @param dir the most recent player action.
     */
    public void setPlayerAction(Direction dir) {
        this.playerAction = dir;
    }

    /**
     * Test whether the intended direction is a valid direction.
     * @return whether the intended direction is a valid direction.
     */
    private boolean isPlayerActionValid() {
        int item = Utilities.getMazeItem(getCoord().x + playerAction.getDirX(), getCoord().y + playerAction.getDirY());
        return item == 0;
    }
}
