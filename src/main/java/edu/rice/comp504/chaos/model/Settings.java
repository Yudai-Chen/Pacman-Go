package edu.rice.comp504.chaos.model;

import java.util.Objects;

/**
 * Application global settings. All static basic types variables.
 */
public class Settings {
    static String mapFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../..")).getPath();

    public static int spawnXMin = 660, spawnXMax = 820, spawnYMin = 120, spawnYMax = 220;

    static int readyTime = 20, scatterTime1 = 90, chaseTime1 = 290, scatterTime2 = 360, chaseTime2 = 560, scatterTime3 = 610, chaseTime3 = 810, scatterTime4 = 860;

    static int pacmanStartLocX = 740, pacmanStartLocY = 330;
    static int pacmanSpeed = 5, pacmanSize = 30;
    public static int pacmanEnergizedSpeed = 7;
    public static String pacmanStartDir = "left";

    static int redStartLocX = 740, redStartLocY = 110, redHomeCoordX = 52, redHomeCoordY = 0;
    static int pinkStartLocX = 740, pinkStartLocY = 170, pinkHomeCoordX = 0, pinkHomeCoordY = 0;
    static int blueStartLocX = 700, blueStartLocY = 170, blueHomeCoordX = 52, blueHomeCoordY = 19;
    static int yellowStartLocX = 780, yellowStartLocY = 170, yellowHomeCoordX = 0, yellowHomeCoordY = 19;
    static String redGhostStartDir = "left", pinkGhostStartDir = "down", blueGhostStartDir = "up", yellowGhostStartDir = "up";
    static int redLockingTime = 0, pinkLockingTime = 8, blueLockingTime = 43, yellowLockingTime = 85;
    public static int ghostSpeed = 5, ghostFrightenedSpeed = 3, ghostEatenSpeed = 20, ghostSize = 30;
    public static int frightenedLast = 80;

    public static int leftGateX = 36, gateY = 5;

    static int eatPauseTime = 10, dyingTime = 11;
}
