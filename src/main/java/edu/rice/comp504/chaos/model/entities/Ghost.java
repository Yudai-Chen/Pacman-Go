package edu.rice.comp504.chaos.model.entities;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;
import edu.rice.comp504.chaos.model.Settings;
import edu.rice.comp504.chaos.model.Utilities;
import edu.rice.comp504.chaos.model.personalities.AGhostPersonality;
import edu.rice.comp504.chaos.model.strategies.IGhostStrategy;
import edu.rice.comp504.chaos.model.strategies.RandomStrategy;
import edu.rice.comp504.chaos.model.strategies.TargetStrategy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The ghost, enemy of pac-man.
 */
public class Ghost extends AEntity implements PropertyChangeListener {
    private String name;
    private boolean isFrightened;
    private int lifetime;
    private boolean isLocked;
    private boolean isEscaped;
    private Coordination home;
    private int lockingTime;
    private AGhostPersonality personality;
    //0:ready, 1:scatter, 2:chase, 3:frighten, 4:eaten
    private int state;
    /**
     * Constructor.
     *
     * @param startLoc the initial coordination of the entity.
     * @param speed    the speed of the entity.
     * @param size     the size of the entity.
     */
    public Ghost(String name, Coordination startLoc, int speed, int size, Direction dir, Coordination home, int lockingTime) {
        super(startLoc, speed, size, dir);
        this.name = name;
        this.isFrightened = false;
        this.lifetime = 0;
        this.isLocked = true;
        this.isEscaped = false;
        this.home = home;
        state = 0;
        this.lockingTime = lockingTime;
        if (lockingTime == 0) {
            unLock();
        }
    }

    /**
     * Ghost can leave the spawn area.
     */
    private void unLock() {
        isLocked = false;
        if (!getLoc().isGhostSpawnArea()) {
            isEscaped = true;
        }
    }

    /**
     * Get the home of the ghost. As known as the target in scatter mode.
     * @return the home of the ghost.
     */
    public Coordination getHome() {
        return home;
    }

    /**
     * Get the state of the ghost.
     * @return the state of the ghost. 0:ready, 1:scatter, 2:chase, 3:frighten, 4:eaten.
     */
    public int getState() {
        return this.state;
    }

    /**
     * Get all available directions from current coordination.
     * @return a list of all available directions.
     */
    private List<Direction> getAvailableDirections() {
        List<Direction> result = new ArrayList<>();
        List<String> allDirections = new ArrayList<>();
        // Do not change the order.
        allDirections.add("left");
        allDirections.add("up");
        allDirections.add("down");
        allDirections.add("right");
        if (!isLocked) {
            for (int i = 0; i < 4; i++) {
                if (i + allDirections.indexOf(getDir().getDirName()) != 3) {
                    Direction test = new Direction(allDirections.get(i));
                    int item = Utilities.getMazeItem(getCoord().x + test.getDirX(), getCoord().y + test.getDirY());
                    if (item == 0 || item == 9) {
                        result.add(test);
                    }
                }
            }
        }
        //Dead end or locked
        if (result.size() == 0) {
            result.add(new Direction(allDirections.get(3 - allDirections.indexOf(getDir().getDirName()))));
        }
        return result;
    }

    /**
     * Set the personality of the ghost.
     * @param personality the personality.
     */
    public void setPersonality(AGhostPersonality personality) {
        this.personality = personality;
    }

    /**
     * Move to intended destination or stop.
     */
    public void move() {
        lifetime++;
        if (isLocked && lifetime == lockingTime) {
            unLock();
        }
        if (!isLocked) {
            if (isEscaped) {
                if (getLoc().isRegularSpot()) {
                    if (getCoord().isCrossing()) {
                        setDirection(personality.think(this).choose(getCoord(), getAvailableDirections()));
                    }
                    moveOnRegularSpot();
                } else {
                    move(computeIntendedDestination());
                }
            } else {
                if ((getLoc().y - 10) % 20 == 0) {
                    if (getLoc().x < (Settings.spawnXMin + Settings.spawnXMax) / 2) {
                        setDirection(new Direction("right"));
                    } else if (getLoc().x > (Settings.spawnXMin + Settings.spawnXMax) / 2) {
                        setDirection(new Direction("left"));
                    } else {
                        setDirection(new Direction("up"));
                    }
                    if (getLoc().y == Settings.spawnYMin - 10) {
                        setDirection(new Direction("left"));
                        isEscaped = true;
                    }
                }
                move(computeIntendedDestination());
            }
        } else {
            if ((getLoc().y - 10) % 20 == 0) {
                // reverse direction
                setDirection(getAvailableDirections().get(0));
            }
            move(computeIntendedDestination());
        }
    }

    /**
     * React to a property change.
     * @param evt the property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("period")) {
            if ((Integer)evt.getOldValue() != 0) {
                setDirection(getDir().getReverse());
            }
            state = (Integer)evt.getNewValue();
        } else if (evt.getPropertyName().equals("frighten")) {
            setDirection(getDir().getReverse());
            state = 3;
        }
    }
}
