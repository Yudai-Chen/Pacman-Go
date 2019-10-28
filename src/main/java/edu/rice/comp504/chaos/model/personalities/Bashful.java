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
 * To locate the target in Chase period, we first start by selecting the position two tiles in front of Pac-Man in his current direction of travel.
 * From there, imagine drawing a vector from a ghost friend's position to this tile, and then doubling the length of the vector.
 * The tile that this new, extended vector ends on will be the host's actual target.
 */
public class Bashful extends AGhostPersonality {
    private Ghost friend;
    /**
     * Constructor. No matter what the concrete personality is, the ghost must know the Coordinate of the pacman.
     *
     * @param pm the reference of the pacman.
     */
    public Bashful(Pacman pm, Ghost friend) {
        super(pm);
        this.friend = friend;
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
                Coordinate vStart = new Coordinate(getPacmanCoord().x + getPacmanDirection().getDirX() * 2, getPacmanCoord().y + getPacmanDirection().getDirY() * 2);
                Coordinate vector =  new Coordinate(vStart.x - friend.getCoord().x, vStart.y - friend.getCoord().y);
                Coordinate vEnd = new Coordinate(friend.getCoord().x + vector.x * 2, friend.getCoord().y + vector.y * 2);
                return new TargetStrategy(context.getCoord(), vEnd);
            case 3:
                return new RandomStrategy();
            case 4:
                Coordinate resetTarget = new Coordinate(Settings.leftGateX, Settings.gateY);
                return new TargetStrategy(context.getCoord(), resetTarget);
            default:
                return null;
        }
    }
}
