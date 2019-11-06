package edu.rice.comp504.chaos.model;

import java.util.Objects;

/**
 * Application global settings. All static basic types variables.
 */
public class Settings {
    public static String mapFileLocation = "/public/map";
    //static String mapFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../..")).getPath();
    public static int spawnXMin = 660;
    public static int spawnXMax = 820;
    public static int spawnYMin = 120;
    static int spawnYMax = 220;
    public static int leftGateX = 36;
    public static int gateY = 5;

    static int readyTime = 20;
    static int scatterTime1 = 90;
    static int chaseTime1 = 290;
    static int scatterTime2 = 360;
    static int chaseTime2 = 560;
    static int scatterTime3 = 610;
    static int chaseTime3 = 810;
    static int scatterTime4 = 860;

    static int pacmanStartLocX = 740;
    static int pacmanStartLocY = 330;
    static int pacman2StartLocX = 740;
    static int pacman2StartLocY = 330;
    static int pacmanSpeed = 5;
    static int pacmanSize = 30;
    public static int pacmanEnergizedSpeed = 7;
    static String pacmanStartDir = "left";
    static String pacman2StartDir = "right";

    static int redStartLocX = 740;
    static int redStartLocY = 110;
    static int redHomeCoordX = 52;
    static int redHomeCoordY = 0;
    static String redGhostStartDir = "left";
    static int redLockingTime = 0;

    static int pinkStartLocX = 740;
    static int pinkStartLocY = 170;
    static int pinkHomeCoordX = 0;
    static int pinkHomeCoordY = 0;
    static String pinkGhostStartDir = "down";
    static int pinkLockingTime = 8;

    static int blueStartLocX = 700;
    static int blueStartLocY = 170;
    static int blueHomeCoordX = 52;
    static int blueHomeCoordY = 19;
    static String blueGhostStartDir = "up";
    static int blueLockingTime = 43;

    static int yellowStartLocX = 780;
    static int yellowStartLocY = 170;
    static int yellowHomeCoordX = 0;
    static int yellowHomeCoordY = 19;
    static String yellowGhostStartDir = "up";
    static int yellowLockingTime = 85;

    public static int ghostSpeed = 5;
    public static int ghostFrightenedSpeed = 3;
    public static int ghostEatenSpeed = 20;
    static int ghostSize = 30;
    public static int frightenedLast = 80;

    static int eatPauseTime = 10;
    static int dyingTime = 11;
}
