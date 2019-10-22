package edu.rice.comp504.chaos.model.strategies;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;

import java.util.List;

/**
 * A strategy which can make the ghost approach the target in a shortest way.
 */
public class TargetStrategy implements IGhostStrategy {
    private Coordination current, target;

    /**
     * Constructor.
     * @param current the current coordination of the host.
     * @param target the target of the host.
     */
    public TargetStrategy(Coordination current, Coordination target) {

    }
    /**
     * Ghosts choose a direction which can make it approach the target in a shortest way.
     * @param availableDirections all available directions.
     * @return the chosen one.
     */
    @Override
    public Direction choose(List<Direction> availableDirections) {
        return null;
    }
}
