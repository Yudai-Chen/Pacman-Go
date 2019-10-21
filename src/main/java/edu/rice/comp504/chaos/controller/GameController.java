package edu.rice.comp504.chaos.controller;

import com.google.gson.Gson;
import edu.rice.comp504.chaos.model.Game;
import spark.servlet.SparkApplication;

import static spark.Spark.*;

/**
 * The controller. Deal with http requests.
 */
public class GameController implements SparkApplication {
    /**
     * Entry point to local server.
     */
    @Override
    public void init() {
        staticFiles.location("/public");
        Gson gson = new Gson();
        Game game = new Game();
        get("/load", (request, response) -> gson.toJson(game));
        get("/update", (request, response) -> {
            game.update();
            return gson.toJson(game);
        });
        post("/pacman-move", ((request, response) -> {
            game.pacmanMove(request.body());
            return 200;
        }));
    }
}

