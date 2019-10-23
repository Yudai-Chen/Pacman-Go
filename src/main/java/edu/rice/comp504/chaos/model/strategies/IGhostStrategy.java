package edu.rice.comp504.chaos.model.strategies;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;

import java.util.List;

/**
 * The strategy determine how ghosts will take actions.
 */
public interface IGhostStrategy {

    /**
     * Ghosts choose a direction from all available directions, according to different strategy.
     * @param availableDirections all available directions.
     * @return the chosen one.
     */
    Direction choose(Coordination current, List<Direction> availableDirections);
}
