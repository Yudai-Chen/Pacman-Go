package edu.rice.comp504.chaos.model.personalities;

import edu.rice.comp504.chaos.model.Coordinate;
import edu.rice.comp504.chaos.model.Settings;
import edu.rice.comp504.chaos.model.entities.Ghost;
import edu.rice.comp504.chaos.model.entities.Pacman;
import edu.rice.comp504.chaos.model.strategies.IGhostStrategy;
import edu.rice.comp504.chaos.model.strategies.RandomStrategy;
import edu.rice.comp504.chaos.model.strategies.TargetStrategy;

/**
 * A personality of a ghost. Chase the pacman.
 */
public class Chaser extends AGhostPersonality{

    /**
     * Constructor. No matter what the concrete personality is, the ghost must know the Coordinate of the pacman.
     * @param pm the reference of the pacman.
     */
    public Chaser(Pacman pm) {
        super(pm);
    }

    /**
     * Come up with a strategy, according to different personalities.
     * @param context the ghost.
     * @return the strategy.
     */
    @Override
    public IGhostStrategy think(Ghost context) {
        switch (context.getState()) {
            case 1: return new TargetStrategy(context.getCoord(), context.getHome());
            case 2: return new TargetStrategy(context.getCoord(), getPacmanCoord());
            case 3: return new RandomStrategy();
            default:
                Coordinate resetTarget = new Coordinate(Settings.leftGateX, Settings.gateY);
                return new TargetStrategy(context.getCoord(), resetTarget);
        }

    }
}
