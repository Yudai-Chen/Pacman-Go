package edu.rice.comp504.chaos.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Some useful methods.
 */
public class Utilities {
    private static int[][] sMaze;

    static void loadStaticMaze(String mapFileLocation) throws IOException {
        sMaze = loadMap(mapFileLocation);
    }
    /**
     * Load map from map file. (The map can be maze map or food map.)
     * @param mapFileLocation the map file.
     * @return map array.
     * @throws IOException map file not found.
     */
    static int[][] loadMap(String mapFileLocation) throws IOException {
        File file = new File(mapFileLocation);
        if (!file.exists()) {
            System.out.println(111);
            throw new RuntimeException("Map file not found.");
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
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
            if (max < ints.length)
                max = ints.length;
        }
        int[][] array = new int[list.size()][max];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(list.get(i), 0, array[i], 0, list.get(i).length);
        }
        return array;
    }

    /**
     * Convert the coordination in the map array (the logical address) to the location on the canvas (the physical address).
     * @param coord the coordination in the map array (the logical address).
     * @return the location on the canvas (the physical address).
     */
    public static Coordination coord2Loc(Coordination coord) {
        return new Coordination(coord.x * 20 + 10, coord.y * 20 + 10);
    }

    /**
     * Convert the location on the canvas (the physical address) to the coordination in the map array (the logical address).
     * @param loc the location on the canvas (the physical address).
     * @return the coordination in the map array (the logical address).
     */
    public static Coordination loc2Coord (Coordination loc) {
        return new Coordination((loc.x - 10) / 20, (loc.y - 10) / 20);
    }


    /**
     * Get the item(x, y) in the static maze. ATTENTION: In a 2-dimension array, we access the raw first and column next.
     * However in the canvas, the column is the y-dimension and the raw is the x-dimension, so we should reverse the coordination
     * to access to the maze array.
     * @return the item.
     */
    public static int getMazeItem(int x, int y) {
        return sMaze[y][x];
    }

    /**
     * Get the static maze.
     * @return the static maze.
     */
    static int[][] getsMaze() {
        return sMaze;
    }
}
