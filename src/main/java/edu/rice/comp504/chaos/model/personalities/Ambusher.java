package edu.rice.comp504.chaos.model.personalities;

import edu.rice.comp504.chaos.model.Coordinate;
import edu.rice.comp504.chaos.model.Settings;
import edu.rice.comp504.chaos.model.entities.Ghost;
import edu.rice.comp504.chaos.model.entities.Pacman;
import edu.rice.comp504.chaos.model.strategies.IGhostStrategy;
import edu.rice.comp504.chaos.model.strategies.RandomStrategy;
import edu.rice.comp504.chaos.model.strategies.TargetStrategy;

/**
 * A personality of the ghost.
 * The host's target tile in Chase mode is determined by looking at Pac-Man's current position and orientation, and selecting the location four tiles straight ahead of Pac-Man.
 */
public class Ambusher extends AGhostPersonality {
    /**
     * Constructor. No matter what the concrete personality is, the ghost must know the Coordinate of the pacman.
     *
     * @param pm the reference of the pacman.
     */
    public Ambusher(Pacman pm) {
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
                Coordinate target = new Coordinate(getPacmanCoord().x + getPacmanDirection().getDirX() * 4, getPacmanCoord().y + getPacmanDirection().getDirY() * 4);
                return new TargetStrategy(context.getCoord(), target);
            case 3: return new RandomStrategy();
            case 4:
                Coordinate resetTarget = new Coordinate(Settings.leftGateX, Settings.gateY);
                return new TargetStrategy(context.getCoord(), resetTarget);
            default:
                return null;
        }
    }
}
