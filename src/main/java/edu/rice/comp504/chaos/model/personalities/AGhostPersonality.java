package edu.rice.comp504.chaos.model.personalities;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;
import edu.rice.comp504.chaos.model.entities.Ghost;
import edu.rice.comp504.chaos.model.entities.Pacman;
import edu.rice.comp504.chaos.model.strategies.IGhostStrategy;

/**
 * The personality of a ghost.
 */
public abstract class AGhostPersonality {
    private Pacman pm;

    /**
     * Constructor. No matter what the concrete personality is, the ghost must know the coordination of the pacman.
     * @param pm the reference of the pacman.
     */
    AGhostPersonality(Pacman pm) {
        this.pm = pm;
    }

    /**
     * Get the coordination of the pacman.
     * @return the coordination.
     */
    Coordination getPacmanCoord() {
        return pm.getCoord();
    }

    /**
     * Get the direction of the pacman.
     * @return the direction.
     */
    Direction getPacmanDirection() {
        return pm.getDir();
    }

    /**
     * Come up with a strategy, according to different personalities.
     * @param context the ghost.
     * @return the strategy.
     */
    abstract public IGhostStrategy think(Ghost context);
}
