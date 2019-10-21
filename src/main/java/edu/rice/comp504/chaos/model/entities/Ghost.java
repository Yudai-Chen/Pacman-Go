package edu.rice.comp504.chaos.model.entities;

import edu.rice.comp504.chaos.model.Coordination;
import edu.rice.comp504.chaos.model.Direction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Ghost extends AEntity {
    private String name;
    private boolean isFrightened;
    /**
     * Constructor.
     *
     * @param startLoc the initial coordination of the entity.
     * @param speed    the speed of the entity.
     * @param size     the size of the entity.
     */
    public Ghost(String name, Coordination startLoc, int speed, int size, Direction dir) {
        super(startLoc, speed, size, dir);
        this.name = name;
        this.isFrightened = false;
    }
}
