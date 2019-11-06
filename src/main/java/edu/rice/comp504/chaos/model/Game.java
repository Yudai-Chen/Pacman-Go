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
    private boolean isTwoPlayer;
    private int[][] maze;
    private int[][] foodMap;
    private int mapid;
    private Timer timer;
    //0:ready, 1:scatter, 2:chase, 3:frightened
    private int period;
    private Pacman pacman;
    private Pacman pacman2;
    private static PropertyChangeSupport pcs;
    private Ghost[] ghosts;
    private boolean timerPause = false;
    private int timerPauseTimeOut = 0;
    private boolean gamePause = false;
    private int gamePauseTimeOut = 0;
    private boolean dying = false;
    private int dyingTimeOut = 0;
    private int currentGhostCredit = 100;
    private int life;
    private int totalDots;
    private int remainingDots;
    private int level;
    /**
     * Constructor. Initialize the map.
     */
    public Game() {
        this.level = 1;
        this.remainingDots = 0;
        this.mapid = 1;
        loadMap(mapid);
        this.totalDots = remainingDots;
        this.isTwoPlayer = false;

        pcs = new PropertyChangeSupport(this);
        pacman = new Pacman(this, new Coordinate(Settings.pacmanStartLocX, Settings.pacmanStartLocY), Settings.pacmanSpeed, Settings.pacmanSize, new Direction(Settings.pacmanStartDir));
        ghosts = new Ghost[4];
        ghosts[0] = new Ghost("red", new Coordinate(Settings.redStartLocX, Settings.redStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.redGhostStartDir), new Coordinate(Settings.redHomeCoordX, Settings.redHomeCoordY), Settings.redLockingTime);
        AGhostPersonality chase = new Chaser(pacman);
        ghosts[0].setPersonality(chase);

        ghosts[1] = new Ghost("pink", new Coordinate(Settings.pinkStartLocX, Settings.pinkStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.pinkGhostStartDir), new Coordinate(Settings.pinkHomeCoordX, Settings.pinkHomeCoordY), Settings.pinkLockingTime);
        AGhostPersonality ambusher = new Ambusher(pacman);
        ghosts[1].setPersonality(ambusher);

        ghosts[2] = new Ghost("blue", new Coordinate(Settings.blueStartLocX, Settings.blueStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.blueGhostStartDir), new Coordinate(Settings.blueHomeCoordX, Settings.blueHomeCoordY), Settings.blueLockingTime);
        AGhostPersonality bashful = new Bashful(pacman, ghosts[0]);
        ghosts[2].setPersonality(bashful);

        ghosts[3] = new Ghost("yellow", new Coordinate(Settings.yellowStartLocX, Settings.yellowStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.yellowGhostStartDir), new Coordinate(Settings.yellowHomeCoordX, Settings.yellowHomeCoordY), Settings.yellowLockingTime);
        AGhostPersonality pokey = new Pokey(pacman);
        ghosts[3].setPersonality(pokey);

        pacman.addListeners(ghosts);

        for (Ghost ghost : ghosts) {
            pcs.addPropertyChangeListener("period", ghost);
        }

        timer = new Timer();
        period = 0;
        this.life = 3;
        pcs.addPropertyChangeListener("clock", timer);
    }

    /**
     * Test whether the game is in two-player mode.
     * @return whether the game is in two-player mode.
     */
    public boolean isTwoPlayer() {
        return isTwoPlayer;
    }

    /**
     * Reset a game to a certain level with a certain map.
     * @param mapid the id of the map.
     * @param level the level.
     */
    public void reset(int mapid, int level, boolean isTwoPlayer) {
        this.level = level;
        this.timerPauseTimeOut = 0;
        this.gamePause = false;
        this.gamePauseTimeOut = 0;
        this.dying = false;
        this.dyingTimeOut = 0;
        this.currentGhostCredit = 100;
        this.remainingDots = 0;
        this.totalDots = 0;
        this.mapid = mapid;
        loadMap(mapid);
        this.totalDots = remainingDots;
        this.isTwoPlayer = isTwoPlayer;

        pcs = new PropertyChangeSupport(this);
        pacman = new Pacman(this, new Coordinate(Settings.pacmanStartLocX, Settings.pacmanStartLocY), Settings.pacmanSpeed, Settings.pacmanSize, new Direction(Settings.pacmanStartDir));
        if (isTwoPlayer) {
            pacman2 = new Pacman(this, new Coordinate(Settings.pacman2StartLocX, Settings.pacman2StartLocY), Settings.pacmanSpeed, Settings.pacmanSize, new Direction(Settings.pacman2StartDir));
        }
        ghosts = new Ghost[4];
        ghosts[0] = new Ghost("red", new Coordinate(Settings.redStartLocX, Settings.redStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.redGhostStartDir), new Coordinate(Settings.redHomeCoordX, Settings.redHomeCoordY), Settings.redLockingTime);
        AGhostPersonality chase = new Chaser(pacman);
        ghosts[0].setPersonality(chase);

        ghosts[1] = new Ghost("pink", new Coordinate(Settings.pinkStartLocX, Settings.pinkStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.pinkGhostStartDir), new Coordinate(Settings.pinkHomeCoordX, Settings.pinkHomeCoordY), Settings.pinkLockingTime);
        AGhostPersonality ambusher = new Ambusher(pacman);
        ghosts[1].setPersonality(ambusher);

        ghosts[2] = new Ghost("blue", new Coordinate(Settings.blueStartLocX, Settings.blueStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.blueGhostStartDir), new Coordinate(Settings.blueHomeCoordX, Settings.blueHomeCoordY), Settings.blueLockingTime);
        AGhostPersonality bashful = new Bashful(pacman, ghosts[0]);
        ghosts[2].setPersonality(bashful);

        ghosts[3] = new Ghost("yellow", new Coordinate(Settings.yellowStartLocX, Settings.yellowStartLocY), Settings.ghostSpeed, Settings.ghostSize, new Direction(Settings.yellowGhostStartDir), new Coordinate(Settings.yellowHomeCoordX, Settings.yellowHomeCoordY), Settings.yellowLockingTime);
        AGhostPersonality pokey = new Pokey(pacman);
        ghosts[3].setPersonality(pokey);

        pacman.addListeners(ghosts);
        if (isTwoPlayer) {
            pacman2.addListeners(ghosts);
        }
        for (Ghost ghost : ghosts) {
            pcs.addPropertyChangeListener("period", ghost);
        }

        timer = new Timer();
        period = 0;
        this.life = 3;
        pcs.addPropertyChangeListener("clock", timer);
    }

    /**
     * Load maze and food maps.
     * @param mapid map id.
     */
    private void loadMap(int mapid) {
        try {
            Utilities.loadStaticMaze(Settings.mapFileLocation + "/map" + mapid + ".txt");
            Utilities.loadStaticFoodMap(Settings.mapFileLocation + "/foodmap" + mapid + ".txt");
            maze = Utilities.getsMaze();
            foodMap = Utilities.getsFoodMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int [] row : maze) {
            for (int item : row) {
                if (item == 1) {
                    remainingDots++;
                }
            }
        }
    }

    /**
     * Add a new map.
     * @param request the map.
     */
    public void addMap(String request) {

    }

    /**
     * Update the positions of all entities in the game.
     */
    public void update() {
        if (!dying) {
            if (!gamePause) {
                if (timerPause) {
                    if (timerPauseTimeOut == 0) {
                        timerPause = false;
                    }
                    timerPauseTimeOut--;
                } else {
                    currentGhostCredit = 100;
                    pacman.setSpeed(Settings.pacmanSpeed);
                    if (isTwoPlayer) {
                        pacman2.setSpeed(Settings.pacmanSpeed);
                    }
                    int oldPeriod = period;
                    pcs.firePropertyChange("clock", true, timerPause);
                    period = timer.getPeriod();
                    pcs.firePropertyChange("period", oldPeriod, period);
                }
                if (period != 0) {
                    pacman.move();
                    if (isTwoPlayer) {
                        pacman2.move();
                    }
                    for (Ghost ghost : ghosts) {
                        ghost.move();
                    }
                }
                for (Ghost ghost : ghosts) {
                    if (pacman.getLoc().distance(ghost.getLoc()) <= 25) {
                        if (ghost.getState() == 1 || ghost.getState() == 2) {
                            dying = true;
                            dyingTimeOut = Settings.dyingTime;
                            return;
                        } else if (ghost.getState() == 3) {
                            ghost.eating();
                            gamePause = true;
                            gamePauseTimeOut = Settings.eatPauseTime;
                            currentGhostCredit *= 2;
                            pacman.addCredit(currentGhostCredit);
                        }
                    }
                }
                if (isTwoPlayer) {
                    for (Ghost ghost : ghosts) {
                        if (pacman2.getLoc().distance(ghost.getLoc()) <= 25) {
                            if (ghost.getState() == 1 || ghost.getState() == 2) {
                                dying = true;
                                dyingTimeOut = Settings.dyingTime;
                                return;
                            } else if (ghost.getState() == 3) {
                                ghost.eating();
                                gamePause = true;
                                gamePauseTimeOut = Settings.eatPauseTime;
                                currentGhostCredit *= 2;
                                pacman2.addCredit(currentGhostCredit);
                            }
                        }
                    }
                }
                remainingDots = totalDots - pacman.getEatenDots();
                if (isTwoPlayer) {
                    remainingDots -= pacman2.getEatenDots();
                }
                if (remainingDots == 0) {
                    reset(mapid, level + 1, isTwoPlayer);
                }
            } else {
                if (gamePauseTimeOut == 0) {
                    for (Ghost ghost : ghosts) {
                        if (ghost.isEating()) {
                            ghost.eaten();
                        }
                    }
                    gamePause = false;
                }
                gamePauseTimeOut--;
            }
        } else {
            if (dyingTimeOut == 0) {
                life--;
                dying = false;
                pacman.resetLoc();
                if (isTwoPlayer) {
                    pacman2.resetLoc();
                }
                timer.reset();
                for (Ghost resetGhost : ghosts) {
                    resetGhost.resetLoc();
                }
            }
            dyingTimeOut--;
        }
    }

    /**
     * Deal with player's request for moving pacman.
     * @param request the player's request.
     * @return whether it is a valid move.
     */
    public boolean pacmanMove(String request) {
        if (request.charAt(request.length() - 1) != '2') {
            Direction moveDir = new Direction(request);
            pacman.setPlayerAction(moveDir);
        } else {
            if (isTwoPlayer) {
                Direction moveDir = new Direction(request.substring(0, request.length() - 1));
                pacman2.setPlayerAction(moveDir);
            }
        }
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


    /**
     * For unit tests.
     * @return pacman's life
     */
    public int getLife() {
        return life;
    }

    /**
     * For unit tests.
     * @return Pacman object.
     */
    public Pacman getPacman(int pid) {
        return pid == 1 ? this.pacman : this.pacman2;
    }
}
