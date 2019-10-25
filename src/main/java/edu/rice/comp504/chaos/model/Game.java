package edu.rice.comp504.chaos.model;

import edu.rice.comp504.chaos.model.entities.Ghost;
import edu.rice.comp504.chaos.model.entities.Pacman;
import edu.rice.comp504.chaos.model.personalities.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

/**
 * The pac-man game.
 */
public class Game implements java.io.Serializable, PropertyChangeListener{
    private int[][] maze;
    private int[][] foodMap;
    private Timer timer;
    //0:ready, 1:scatter, 2:chase
    private int period;
    private Pacman pacman;
    private static PropertyChangeSupport pcs;
    private Ghost[] ghosts;
    private boolean timerPause = false;
    private int timerPauseTimeOut = 0;
    private boolean gamePause = false;
    private int gamePauseTimeOut = 0;
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
        pacman = new Pacman(this, new Coordination(Settings.pacmanStartLocX, Settings.pacmanStartLocY), Settings.pacmanSpeed, Settings.pacmanSize);
        ghosts = new Ghost[4];
        ghosts[0] = new Ghost("red", new Coordination(Settings.redStartLocX, Settings.redStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.redGhostStartDir), new Coordination(Settings.redHomeCoordX, Settings.redHomeCoordY), Settings.redLockingTime);
        AGhostPersonality chase = new Chaser(pacman);
        ghosts[0].setPersonality(chase);

        ghosts[1] = new Ghost("pink", new Coordination(Settings.pinkStartLocX, Settings.pinkStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.pinkGhostStartDir), new Coordination(Settings.pinkHomeCoordX, Settings.pinkHomeCoordY), Settings.pinkLockingTime);
        AGhostPersonality ambusher = new Ambusher(pacman);
        ghosts[1].setPersonality(ambusher);

        ghosts[2] = new Ghost("blue", new Coordination(Settings.blueStartLocX, Settings.blueStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.blueGhostStartDir), new Coordination(Settings.blueHomeCoordX, Settings.blueHomeCoordY), Settings.blueLockingTime);
        AGhostPersonality bashful = new Bashful(pacman, ghosts[0]);
        ghosts[2].setPersonality(bashful);

        ghosts[3] = new Ghost("yellow", new Coordination(Settings.yellowStartLocX, Settings.yellowStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.yellowGhostStartDir), new Coordination(Settings.yellowHomeCoordX, Settings.yellowHomeCoordY), Settings.yellowLockingTime);
        AGhostPersonality pokey = new Pokey(pacman);
        ghosts[3].setPersonality(pokey);

        pacman.addListeners(ghosts);
        for (Ghost ghost : ghosts) {
            pcs.addPropertyChangeListener("period", ghost);
            pcs.addPropertyChangeListener("fright", ghost);
        }

        timer = new Timer();
        period = 0;
        pcs.addPropertyChangeListener("clock", timer);
    }

    /**
     * Update the positions of all entities in the game.
     */
    public void update() {
        if (!gamePause) {
            if (timerPause) {
                if (timerPauseTimeOut == 0) {
                    timerPause = false;
                }
                timerPauseTimeOut--;
            } else {
                int oldPeriod = period;
                pcs.firePropertyChange("clock", true, timerPause);
                period = timer.getPeriod();
                pcs.firePropertyChange("period", oldPeriod, period);
            }
            if (period != 0) {
                pacman.move();
                for (Ghost ghost : ghosts) {
                    ghost.move();
                }
            }
            for (Ghost ghost : ghosts) {
                if (pacman.getLoc().distance(ghost.getLoc()) <= 10) {
                    if (ghost.getState() == 1 || ghost.getState() == 2) {
                        pacman.resetLoc();
                        timer.reset();
                        for (Ghost resetGhost : ghosts) {
                            resetGhost.resetLoc();
                        }
                        return;
                    } else if (ghost.getState() == 3) {
                        ghost.eaten();
                        gamePause = true;
                        gamePauseTimeOut = Settings.eatPauseTime;
                    }
                }
            }
        } else {
            if (gamePauseTimeOut == 0) {
                gamePause = false;
            }
            gamePauseTimeOut--;
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

    /**
     * Receive a event to pause the game.
     * @param evt the property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("gamePause")) {
            gamePause = true;
        } else if (evt.getPropertyName().equals("timerPause")) {
            timerPause = true;
            timerPauseTimeOut = (int)evt.getNewValue();
            period = 3;
        }
    }
}
