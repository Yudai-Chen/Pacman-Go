package edu.rice.comp504.chaos.model;

import java.util.Objects;

/**
 * Application global settings.
 */
class Settings {
    static String mazeFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../../map.txt")).getPath();
    static String foodMapFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../../foodmap.txt")).getPath();
}
