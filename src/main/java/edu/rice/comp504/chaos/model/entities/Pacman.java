package edu.rice.comp504.chaos.model.entities;

import edu.rice.comp504.chaos.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * The pacman which is controlled by the player.
 */
public class Pacman extends AEntity {
    private int credit;
    private int eatenDots;
    private Direction playerAction;
    private static PropertyChangeSupport pcs;

    /**
     * Constructor.
     * @param startLoc the initial Coordinate of pacman.
     * @param speed the speed of pacman.
     * @param size the size of pacman.
     */
    public Pacman(Game game, Coordinate startLoc, int speed, int size, Direction startDir) {
        super(startLoc, speed, size, startDir);
        credit = 0;
        eatenDots = 0;
        playerAction = startDir;
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener("timerPause", game);
    }

    /**
     * Move to intended destination or stop.
     */
    public void move() {
        if (getLoc().isRegularSpot()) {
            if (getItemOnPlayerAction() == 0) {
                setDirection(playerAction);
                move(computeIntendedDestination());
            } else if (getItemOnPlayerAction() == 9) {
                if (getCoord().x == 1) {
                    moveToCoord(new Coordinate(58, 9));
                } else {
                    moveToCoord(new Coordinate(1, 9));
                }
            } else {
                moveOnRegularSpot();
            }
        } else {
            move(computeIntendedDestination());
        }
        earnCredit();
    }

    /**
     * Get the amount of dots that the pac-man has already eaten.
     * @return the amount of dots that the pac-man has already eaten.
     */
    public int getEatenDots() {
        return eatenDots;
    }

    /**
     * Take food and earn credit.
     */
    private void earnCredit() {
        if (Utilities.getFoodMapItem(getCoord().x, getCoord().y) == 1) {
            credit += 10;
            eatenDots++;
            Utilities.setFoodMapItem(getCoord().x, getCoord().y, 0);
        } else if (Utilities.getFoodMapItem(getCoord().x, getCoord().y) == 2) {
            pcs.firePropertyChange("frighten", 0, Settings.frightenedLast);
            pcs.firePropertyChange("timerPause", 0, Settings.frightenedLast);
            setSpeed(Settings.pacmanEnergizedSpeed);
            credit += 50;
            Utilities.setFoodMapItem(getCoord().x, getCoord().y, 3);
        } else if (Utilities.getFoodMapItem(getCoord().x, getCoord().y) == 3) {
            credit += 100;
            Utilities.setFoodMapItem(getCoord().x, getCoord().y, 0);
        }
    }

    /**
     * Add some credit to the pac-man.
     * @param credit the added credit.
     */
    public void addCredit(int credit) {
        this.credit += credit;
    }

    /**
     * Record the most recent player action.
     * @param dir the most recent player action.
     */
    public void setPlayerAction(Direction dir) {
        this.playerAction = dir;
    }

    /**
     * Get the item on the next intended position.
     * @return the item.
     */
    private int getItemOnPlayerAction() {
        return Utilities.getMazeItem(getCoord().x + playerAction.getDirX(), getCoord().y + playerAction.getDirY());
    }

    /**
     * Add ghosts that will listen for a property change (i.e. energizer frighten).
     * @param pcls The ghosts.
     */
    public void addListeners(PropertyChangeListener[] pcls) {
        for (PropertyChangeListener pcl : pcls) {
            pcs.addPropertyChangeListener("frighten", pcl);
        }
    }

    /**
     * Rest the pac-man to its initial location.
     */
    public void resetLoc() {
        super.resetLoc();
        playerAction = getStartDir();
    }

    /**
     * For unit tests.
     * @return Pacman's credit.
     */
    public int getCredit() {
        return this.credit;
    }
}
