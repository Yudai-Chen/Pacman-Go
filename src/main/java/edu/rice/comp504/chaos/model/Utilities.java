package edu.rice.comp504.chaos.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Some useful methods.
 */
public class Utilities {
    private static int[][] sMaze;
    private static int[][] sFoodMap;

    /**
     * Load static maze from external file.
     * @param mapFileLocation the location of the maze map file.
     * @throws IOException file not found.
     */
    static void loadStaticMaze(String mapFileLocation) throws IOException {
        sMaze = loadMap(mapFileLocation);
    }

    /**
     * Load static food map from external file.
     * @param mapFileLocation the location of the food map file.
     * @throws IOException file not found.
     */
    static void loadStaticFoodMap(String mapFileLocation) throws IOException {
        sFoodMap = loadMap(mapFileLocation);
    }

    /**
     * Load map from map file. (The map can be maze map or food map.)
     * @param mapFileLocation the map file.
     * @return map array.
     * @throws IOException map file not found.
     */
    private static int[][] loadMap(String mapFileLocation) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(Game.class.getResourceAsStream(mapFileLocation)));
        String str;
        List<int[]> list = new ArrayList<>();
        while ((str = br.readLine()) != null) {
            int lineIndex = 0;
            String[] lineBuffer = str.split(", ");
            int[] dArr = new int[lineBuffer.length];
            for (String ss : lineBuffer) {
                if (ss != null) {
                    dArr[lineIndex++] = Integer.parseInt(ss);
                }

            }
            list.add(dArr);
        }
        int max = 0;
        for (int[] ints : list) {
            if (max < ints.length) {
                max = ints.length;
            }
        }
        int[][] array = new int[list.size()][max];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(list.get(i), 0, array[i], 0, list.get(i).length);
        }
        return array;
    }

    /**
     * Convert the Coordinate in the map array (the logical address) to the location on the canvas (the physical address).
     * @param coord the Coordinate in the map array (the logical address).
     * @return the location on the canvas (the physical address).
     */
    public static Coordinate coord2Loc(Coordinate coord) {
        return new Coordinate(coord.x * 20 + 10, coord.y * 20 + 10);
    }

    /**
     * Convert the location on the canvas (the physical address) to the Coordinate in the map array (the logical address).
     * @param loc the location on the canvas (the physical address).
     * @return the Coordinate in the map array (the logical address).
     */
    public static Coordinate loc2Coord(Coordinate loc) {
        return new Coordinate((loc.x - 10) / 20, (loc.y - 10) / 20);
    }


    /**
     * Get the item(x, y) in the static maze. ATTENTION: In a 2-dimension array, we access the raw first and column next.
     * However in the canvas, the column is the y-dimension and the raw is the x-dimension, so we should reverse the Coordinate
     * to access to the maze array.
     * @return the item.
     */
    public static int getMazeItem(int x, int y) {
        return sMaze[y][x];
    }

    /**
     * Get the item(x, y) in the static food map. ATTENTION: In a 2-dimension array, we access the raw first and column next.
     * However in the canvas, the column is the y-dimension and the raw is the x-dimension, so we should reverse the Coordinate
     * to access to the maze array.
     * @return the item.
     */
    public static int getFoodMapItem(int x, int y) {
        return sFoodMap[y][x];
    }

    /**
     * Set the item on a certain Coordinate in the food map to a new item.
     * @param x the x-Coordinate.
     * @param y the y-Coordinate.
     * @param newItem the new item.
     */
    public static void setFoodMapItem(int x, int y, int newItem) {
        sFoodMap[y][x] = newItem;
    }

    /**
     * Get the static maze.
     * @return the static maze.
     */
    static int[][] getsMaze() {
        return sMaze;
    }

    /**
     * Get the static food map.
     * @return the static food map.
     */
    static int[][] getsFoodMap() {
        return sFoodMap;
    }
}
