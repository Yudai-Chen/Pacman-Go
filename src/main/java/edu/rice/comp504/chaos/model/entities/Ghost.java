package edu.rice.comp504.chaos.model.entities;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;

public class Ghost extends AEntity {
    private String name;
    /**
     * Constructor.
     *
     * @param startLoc the initial coordination of the entity.
     * @param speed    the speed of the entity.
     * @param size     the size of the entity.
     * @param dir      the initial direction.
     */
    Ghost(Coordination startLoc, int speed, int size, Direction dir) {
        super(startLoc, speed, size, dir);
    }
}
