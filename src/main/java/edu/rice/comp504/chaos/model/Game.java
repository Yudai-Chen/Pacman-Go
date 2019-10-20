package edu.rice.comp504.chaos.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The pac-man game.
 */
public class Game implements java.io.Serializable{
    private static int[][] map;

    /**
     * Constructor. Initialize the map.
     */
    public Game() {
        try {
            map = loadMap(Settings.mapFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load map from map file.
     * @param mapFileLocation the map file
     * @return map array.
     * @throws IOException map file not found.
     */
    private int[][] loadMap(String mapFileLocation) throws IOException {
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
     * Get the map.
     * @return the map.
     */
    public int[][] getMap() {
        return map;
    }
}
