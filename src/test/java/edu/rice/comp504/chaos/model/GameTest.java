package edu.rice.comp504.chaos.model;

import com.google.gson.Gson;
import edu.rice.comp504.chaos.model.Game;
import edu.rice.comp504.chaos.model.Settings;
import edu.rice.comp504.chaos.model.Utilities;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Random;

import static spark.Spark.*;

public class GameTest extends TestCase {
    public void testPartOne(){
        Game g = new Game();
        Settings.yellowStartLocX = 130;
        Settings.yellowStartLocY = 330;

        g.reset(3, 1, false);
        for(int i=0; i < 500; i++){
            g.pacmanMove("left");
            g.update();
        }

        g.reset(3, 1, true);
        for(int i=0; i < 500; i++){
            g.pacmanMove("left");
            g.pacmanMove("left2");
            g.update();
        }

        assertEquals("Pacman lost after it encounters a ghost for three times: ", 0, g.getLife());
    }

    public void testPartTwo(){
        Game g = new Game();

        Settings.yellowLockingTime = 0;
        Settings.yellowGhostStartDir = "left";
        Settings.yellowStartLocX = 130;
        Settings.yellowStartLocY = 330;

        g.reset(4, 1, false);
        for(int i=0; i < 1000; i++){
            g.update();
    }

        g.reset(4, 1, true);
        for(int i=0; i < 1000; i++){
            g.update();
            g.pacmanMove("right2");
        }
        assertEquals("Pacman gained certain credits after eating some small dots, an energizer, a fruit and a ghost: ",
                640, g.getPacman(1).getCredit());
        assertEquals("Pacman 2 gained certain credits after eating some small dots, a fruit and an energizer: ",
                320, g.getPacman(2).getCredit());


        Settings.redStartLocX = 740;
        Settings.redStartLocY = 110;
        Settings.pinkStartLocX = 740;
        Settings.pinkStartLocY = 170;
        Settings.blueStartLocX = 700;
        Settings.blueStartLocY = 170;
        Settings.yellowStartLocX = 780;
        Settings.yellowStartLocY = 170;

        Settings.blueLockingTime = 0;
        Settings.blueGhostStartDir = "left";
        Settings.blueStartLocX = 130;
        Settings.blueStartLocY = 330;

        g.reset(4, 1, false);
        for(int i=0; i < 1000; i++){
            g.update();
        }

        g.reset(4, 1, true);
        for(int i=0; i < 1000; i++){
            g.update();
            g.pacmanMove("right2");
        }
        assertEquals("Pacman gained certain credits after eating some small dots, an energizer and a ghost: ",
                640, g.getPacman(1).getCredit());
        assertEquals("Pacman 2 gained certain credits after eating some small dots and an energizer: ",
                320, g.getPacman(2).getCredit());

        Settings.redStartLocX = 740;
        Settings.redStartLocY = 110;
        Settings.pinkStartLocX = 740;
        Settings.pinkStartLocY = 170;
        Settings.blueStartLocX = 700;
        Settings.blueStartLocY = 170;
        Settings.yellowStartLocX = 780;
        Settings.yellowStartLocY = 170;

        Settings.redStartLocX = 130;
        Settings.redStartLocY = 330;

        g.reset(4, 1, false);
        for(int i=0; i < 1000; i++){
            g.update();
        }

        g.reset(4, 1, true);
        for(int i=0; i < 1000; i++){
            g.update();
            g.pacmanMove("right2");
        }
        assertEquals("Pacman gained certain credits after eating some small dots, an energizer and a ghost: ",
                640, g.getPacman(1).getCredit());
        assertEquals("Pacman 2 gained certain credits after eating some small dots and an energizer: ",
                320, g.getPacman(2).getCredit());

        Settings.redStartLocX = 740;
        Settings.redStartLocY = 110;
        Settings.pinkStartLocX = 740;
        Settings.pinkStartLocY = 170;
        Settings.blueStartLocX = 700;
        Settings.blueStartLocY = 170;
        Settings.yellowStartLocX = 780;
        Settings.yellowStartLocY = 170;

        Settings.pinkLockingTime = 0;
        Settings.pinkGhostStartDir = "left";
        Settings.pinkStartLocX = 130;
        Settings.pinkStartLocY = 330;
        g.reset(4, 1, false);
        for(int i=0; i < 1000; i++){
            g.update();
        }

        g.reset(4, 1, true);
        for(int i=0; i < 1000; i++){
            g.update();
            g.pacmanMove("right2");
        }
        assertEquals("Pacman gained certain credits after eating some small dots, an energizer and a ghost: ",
                640, g.getPacman(1).getCredit());
        assertEquals("Pacman 2 gained certain credits after eating some small dots and an energizer: ",
                320, g.getPacman(2).getCredit());
    }
}
