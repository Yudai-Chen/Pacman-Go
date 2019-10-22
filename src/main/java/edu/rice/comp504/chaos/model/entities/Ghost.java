package edu.rice.comp504.chaos.model.entities;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;
import edu.rice.comp504.chaos.model.Utilities;
import edu.rice.comp504.chaos.model.strategies.IGhostStrategy;
import edu.rice.comp504.chaos.model.strategies.RandomStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The ghost, enemy of pac-man.
 */
public class Ghost extends AEntity {
    private String name;
    private boolean isFrightened;
    private IGhostStrategy strategy;
    private int lifetime;
    /**
     * Constructor.
     *
     * @param startLoc the initial coordination of the entity.
     * @param speed    the speed of the entity.
     * @param size     the size of the entity.
     */
    public Ghost(String name, Coordination startLoc, int speed, int size, Direction dir) {
        super(startLoc, speed, size, dir);
        this.name = name;
        this.isFrightened = false;
        this.strategy = new RandomStrategy();
        this.lifetime = 0;
    }

    /**
     * Get all available directions from current coordination.
     * @return a list of all available directions.
     */
    private List<Direction> getAvailableDirections() {
        List<Direction> result = new ArrayList<>();
        List<String> allDirections = new ArrayList<>();
        allDirections.add("left");
        allDirections.add("up");
        allDirections.add("down");
        allDirections.add("right");
        for (int i = 0; i < 4; i++) {
            if (i + allDirections.indexOf(getDir().getDirName()) != 3) {
                Direction test = new Direction(allDirections.get(i));
                int item = Utilities.getMazeItem(getCoord().x + test.getDirX(), getCoord().y + test.getDirY());
                if (item == 0 || item == 9) {
                    result.add(test);
                }
            }
        }
        //Dead end
        if (result.size() == 0) {
            result.add(new Direction(allDirections.get(3 - allDirections.indexOf(getDir().getDirName()))));
        }
        return result;
    }

    /**
     * Move to intended destination or stop.
     */
    public void move() {
        if (getLoc().isRegularSpot()) {
            if (getCoord().isCrossing()) {
                setDirection(strategy.choose(getAvailableDirections()));
            }
            moveOnRegularSpot();
        } else {
            move(computeIntendedDestination());
        }
    }
}
