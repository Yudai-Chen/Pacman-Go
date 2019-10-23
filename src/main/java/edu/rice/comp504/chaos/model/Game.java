package edu.rice.comp504.chaos.model;

import edu.rice.comp504.chaos.model.entities.Ghost;
import edu.rice.comp504.chaos.model.entities.Pacman;
import edu.rice.comp504.chaos.model.personalities.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

/**
 * The pac-man game.
 */
public class Game implements java.io.Serializable{
    private int[][] maze;
    private int[][] foodMap;
    private int timer;
    //0:ready, 1:scatter, 2:chase
    private static int period;
    private Pacman pacman;
    private static PropertyChangeSupport pcs;
    private Ghost[] ghosts;
    /**
     * Constructor. Initialize the map.
     */
    public Game() {
        try {
            Utilities.loadStaticMaze(Settings.mazeFileLocation);
            Utilities.loadStaticFoodMap(Settings.foodMapFileLocation);
            maze = Utilities.getsMaze();
            foodMap = Utilities.getsFoodMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pcs = new PropertyChangeSupport(this);
        pacman = new Pacman(new Coordination(Settings.pacmanStartLocX, Settings.pacmanStartLocY), Settings.pacmanSpeed, Settings.pacmanSize);
        ghosts = new Ghost[4];
        ghosts[0] = new Ghost("red", new Coordination(Settings.redStartLocX, Settings.redStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.redGhostStartDir), new Coordination(Settings.redHomeCoordX, Settings.redHomeCoordY), Settings.redLockingTime);
        AGhostPersonality chase = new Chaser(pacman);
        ghosts[0].setPersonality(chase);
        pcs.addPropertyChangeListener("period", ghosts[0]);
        pcs.addPropertyChangeListener("fright", ghosts[0]);
        ghosts[1] = new Ghost("pink", new Coordination(Settings.pinkStartLocX, Settings.pinkStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.pinkGhostStartDir), new Coordination(Settings.pinkHomeCoordX, Settings.pinkHomeCoordY), Settings.pinkLockingTime);
        AGhostPersonality ambusher = new Ambusher(pacman);
        ghosts[1].setPersonality(ambusher);
        pcs.addPropertyChangeListener("period", ghosts[1]);
        pcs.addPropertyChangeListener("fright", ghosts[1]);
        ghosts[2] = new Ghost("blue", new Coordination(Settings.blueStartLocX, Settings.blueStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.blueGhostStartDir), new Coordination(Settings.blueHomeCoordX, Settings.blueHomeCoordY), Settings.blueLockingTime);
        AGhostPersonality bashful = new Bashful(pacman, ghosts[0]);
        ghosts[2].setPersonality(bashful);
        pcs.addPropertyChangeListener("period", ghosts[2]);
        pcs.addPropertyChangeListener("fright", ghosts[2]);
        ghosts[3] = new Ghost("yellow", new Coordination(Settings.yellowStartLocX, Settings.yellowStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.yellowGhostStartDir), new Coordination(Settings.yellowHomeCoordX, Settings.yellowHomeCoordY), Settings.yellowLockingTime);
        AGhostPersonality pokey = new Pokey(pacman);
        ghosts[3].setPersonality(pokey);
        pcs.addPropertyChangeListener("period", ghosts[3]);
        pcs.addPropertyChangeListener("fright", ghosts[3]);
        pacman.addListeners(ghosts);
        timer = 0;
        period = 0;
    }

    /**
     * Update the positions of all entities in the game.
     */
    public void update() {
        int oldPeriod = period;
        timer ++;
        if (timer == Settings.readyTime) {
            period = 1;
        }
        if (timer == 600) {
            period = 2;
        }
        pcs.firePropertyChange("period", oldPeriod, period);
        if (period != 0) {
            pacman.move();
            for (Ghost ghost : ghosts) {
                ghost.move();
            }
        }
    }

    /**
     * Deal with player's request for moving pacman.
     * @param request the player's request.
     * @return whether it is a valid move.
     */
    public boolean pacmanMove(String request) {
        Direction moveDir = new Direction(request);
        pacman.setPlayerAction(moveDir);
        return true;
    }
}
