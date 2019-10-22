package edu.rice.comp504.chaos.model;

import java.util.Objects;

/**
 * Application global settings. All static basic types variables.
 */
public class Settings {
    static String mazeFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../../map.txt")).getPath();
    static String foodMapFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../../foodmap.txt")).getPath();
    static int pacmanStartLocX = 740, pacmanStartLocY = 330;
    static int pacmanSpeed = 5, pacmanSize = 30;
    public static String pacmanStartDir = "left";
    static int redStartLocX = 740, redStartLocY = 110;
    static int pinkStartLocX = 740, pinkStartLocY = 170;
    static int blueStartLocX = 700, blueStartLocY = 170;
    static int yellowStartLocX = 780, yellowStartLocY = 170;
    static String redGhostStartDir = "left", pinkGhostStartDir = "down", blueGhostStartDir = "up", yellowGhostStartDir = "up";
    static int ghostSpeed = 5, ghostSize = 30;
}
