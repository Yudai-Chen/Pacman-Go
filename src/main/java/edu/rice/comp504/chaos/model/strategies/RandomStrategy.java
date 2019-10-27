package edu.rice.comp504.chaos.model.strategies;

import edu.rice.comp504.chaos.model.Coordinate;
import edu.rice.comp504.chaos.model.Direction;

import java.util.List;

/**
 * Choose direction randomly.
 */
public class RandomStrategy implements IGhostStrategy{

    /**
     * Ghosts choose a direction randomly.
     * @param availableDirections all available directions.
     * @return the chosen one.
     */
    @Override
    public Direction choose(Coordinate current, List<Direction> availableDirections) {
        return availableDirections.get((int)Math.floor(Math.random() * availableDirections.size()));
    }
}
