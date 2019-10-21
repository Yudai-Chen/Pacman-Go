package edu.rice.comp504.chaos.model;

import java.util.Objects;

/**
 * Application global settings.
 */
public class Settings {
    static String mazeFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../../map.txt")).getPath();
    static String foodMapFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../../foodmap.txt")).getPath();
    static int pacmanStartLocX = 740, pacmanStartLocY = 330;
    static int pacmanSpeed = 5, pacmanSize = 30;
    public static String pacmanStartDir = "left";
}
