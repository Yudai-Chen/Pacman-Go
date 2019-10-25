package edu.rice.comp504.chaos.model.personalities;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.entities.Ghost;
import edu.rice.comp504.chaos.model.entities.Pacman;
import edu.rice.comp504.chaos.model.strategies.IGhostStrategy;
import edu.rice.comp504.chaos.model.strategies.RandomStrategy;
import edu.rice.comp504.chaos.model.strategies.TargetStrategy;

/**
 * A personality of the ghost.
 * If he is farther than eight tiles away, his targeting is the Pac-Man's current tile.
 * However, as soon as his distance to Pac-Man becomes less than eight tiles, the host's target is set to its home.
 */
public class Pokey extends AGhostPersonality{

    /**
     * Constructor. No matter what the concrete personality is, the ghost must know the coordination of the pacman.
     * @param pm the reference of the pacman.
     */
    public Pokey(Pacman pm) {
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
            case 2:
                if (context.getCoord().distance(getPacmanCoord()) >= 8) {
                    return new TargetStrategy(context.getCoord(), getPacmanCoord());
                }
                return new TargetStrategy(context.getCoord(), context.getHome());
            case 3: return new RandomStrategy();
            case 4:
                //TODO
                Coordination resetTarget = new Coordination(36, 5);
                return new TargetStrategy(context.getCoord(), resetTarget);
            default:return null;
        }

    }
}
