package edu.rice.comp504.chaos.model;

import java.util.Objects;

/**
 * Application global settings.
 */
class Settings {
    static String mapFileLocation = Objects.requireNonNull(Game.class.getClassLoader().getResource("../../map.txt")).getPath();
}
