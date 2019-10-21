package edu.rice.comp504.chaos.model;

import edu.rice.comp504.chaos.model.entities.Pacman;
import java.io.IOException;

/**
 * The pac-man game.
 */
public class Game implements java.io.Serializable{
    private int[][] maze;
    private int[][] foodMap;
    private int timer;
    private Pacman pacman;
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
        pacman = new Pacman(new Coordination(Settings.pacmanStartLocX, Settings.pacmanStartLocY), Settings.pacmanSpeed, Settings.pacmanSize);
        timer = 0;
    }

    /**
     * Update the positions of all entities in the game.
     */
    public void update() {
        pacman.move();
        timer ++;
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
