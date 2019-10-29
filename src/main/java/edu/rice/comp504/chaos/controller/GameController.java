package edu.rice.comp504.chaos.controller;

import com.google.gson.Gson;
import edu.rice.comp504.chaos.model.Game;

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

        staticFiles.location("/public");
        Gson gson = new Gson();
        Game game = new Game();
        post("/load", (request, response) -> {
            game.reset(Integer.parseInt(request.body()), 1);
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

