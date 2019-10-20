package edu.rice.comp504.chaos.controller;

import spark.servlet.SparkApplication;

import static spark.Spark.*;

public class GameController implements SparkApplication {
    /**
     * Entry point to local server.
     */
    @Override
    public void init() {
        staticFiles.location("/public");
    }
}

