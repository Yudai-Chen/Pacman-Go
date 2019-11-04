package edu.rice.comp504.chaos.controller;

import com.google.gson.Gson;
import edu.rice.comp504.chaos.model.Game;
import edu.rice.comp504.chaos.model.Settings;
import edu.rice.comp504.chaos.model.Utilities;

import static spark.Spark.*;

/**
 * The controller. Deal with http requests.
 */
public class GameController {
    /**
     * Entry point to local server.
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        int maxThreads = 8;
        threadPool(maxThreads);

        staticFiles.location("/public");
        Gson gson = new Gson();
        Game game = new Game();
        post("/mapEditor/load", (request, response) -> {
            Utilities.loadStaticMaze(Settings.mapFileLocation + "/map" + request.body() + ".txt");
            return gson.toJson(Utilities.getsMaze());
        });
        post("/mapEditor/submit", (request, response) -> {
            game.addMap(request.body());
            return gson.toJson(Utilities.getsMaze());
        });
        post("/load", (request, response) -> {
            game.reset(Integer.parseInt(request.body()), 1, false);
            return gson.toJson(game);
        });
        get("/update", (request, response) -> {
            game.update();
            return gson.toJson(game);
        });
        post("/pacman-move", ((request, response) -> {
            game.pacmanMove(request.body());
            return 200;
        }));
        post("/add-player", (request, response) -> {
            if (!game.isTwoPlayer()) {
                game.reset(Integer.parseInt(request.body()), 1, true);
            }
            return 200;
        });
    }

    /**
     * Get the assigned port when deployed onto Heroku.
     * @return the assigned port.
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}

