package edu.rice.comp504.chaos.model.strategies;

import edu.rice.comp504.chaos.model.Coordinate;
import edu.rice.comp504.chaos.model.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * A strategy which can make the ghost approach the target in a shortest way.
 */
public class TargetStrategy implements IGhostStrategy {
    private Coordinate current;
    private Coordinate target;

    /**
     * Constructor.
     * @param current the current Coordinate of the host.
     * @param target the target of the host.
     */
    public TargetStrategy(Coordinate current, Coordinate target) {
        this.current = current;
        this.target = target;
    }

    /**
     * Get the target.
     * @return target.
     */
    public Coordinate getTarget() {
        return target;
    }
    /**
     * Ghosts choose a direction which can make it approach the target in a shortest way.
     * @param availableDirections all available directions.
     * @return the chosen one.
     */
    @Override
    public Direction choose(Coordinate current, List<Direction> availableDirections) {
        Direction result = null;
        double currentMin = Double.MAX_VALUE;
        List<String> allDirections = new ArrayList<>();
        // Do not change the order.
        allDirections.add("up");
        allDirections.add("left");
        allDirections.add("down");
        allDirections.add("right");
        for (String dirName : allDirections) {
            for (Direction direction : availableDirections) {
                if (direction.getDirName().equals(dirName)) {
                    Coordinate coord = new Coordinate(current.x + direction.getDirX(), current.y + direction.getDirY());
                    if (coord.minDistance(target) < currentMin) {
                        currentMin = coord.minDistance(target);
                        result = direction;
                    }
                    break;
                }
            }
        }
        return result;
    }
}
