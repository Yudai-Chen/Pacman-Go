'use strict';

//app to draw polymorphic shapes on canvas
var app;
//the interval ID for updating the game
var updateInterval;
var pacmanImg;
var pacmanImgSrcPrefix = "./pacman/";
var keyCode = null;
var isKeyDown = false;
var pacmanAngle = 0;
var pacmanStage = 0;

function logicToPhysical(lx,ly) {
    return {
        x: lx * 20 + 10,
        y: ly * 20 + 10
    };
}

function createApp(canvas) {
    var context = canvas.getContext("2d");

    var drawBackground = function() {
        context.fillStyle = "#000000";
        context.fillRect(0, 0, canvas.width, canvas.height);
    };

    var _COS = [1,0,-1,0];
    var _SIN = [0,1,0,-1];

    var drawMaze = function(map) {
        context.lineWidth = 2;
        for (var j = 1; j < map.length - 1; j++) {
            for (var i = 1; i < map[j].length - 1; i++) {
                var value = map[j][i];
                if (value) {
                    var code = [0, 0, 0, 0];
                    if (map[j][i + 1] && !(map[j - 1][i + 1] && map[j + 1][i + 1] && map[j - 1][i] && map[j + 1][i])) {
                        code[0] = 1;
                    }
                    if (map[j + 1][i] && !(map[j + 1][i - 1] && map[j + 1][i + 1] && map[j][i - 1] && map[j][i + 1])) {
                        code[1] = 1;
                    }
                    if (map[j][i - 1] && !(map[j - 1][i - 1] && map[j + 1][i - 1] && map[j - 1][i] && map[j + 1][i])) {
                        code[2] = 1;
                    }
                    if (map[j - 1][i] && !(map[j - 1][i - 1] && map[j - 1][i + 1] && map[j][i - 1] && map[j][i + 1])) {
                        code[3] = 1;
                    }
                    if (code.indexOf(1) > -1) {
                        switch (value) {
                            case 1:
                                context.strokeStyle = "#1414FF";
                                break;
                            case 2:
                                context.strokeStyle = "#FFAAA4";
                                break;
                            case 3:
                                context.strokeStyle = "#D60022";
                                break;
                            case 4:
                                context.strokeStyle = "#E2E200";
                                break;
                            case 5:
                                context.strokeStyle = "#00FF00";
                                break;
                            default:
                                context.strokeStyle = "#FFFFFF";
                                break;
                        }
                        var pos = logicToPhysical(i, j);
                        switch (code.join('')) {
                            case '1100':
                                context.beginPath();
                                context.arc(pos.x + 10, pos.y + 10, 10, Math.PI, 1.5 * Math.PI, false);
                                context.stroke();
                                context.closePath();
                                break;
                            case '0110':
                                context.beginPath();
                                context.arc(pos.x - 10, pos.y + 10, 10, 1.5 * Math.PI, 2 * Math.PI, false);
                                context.stroke();
                                context.closePath();
                                break;
                            case '0011':
                                context.beginPath();
                                context.arc(pos.x - 10, pos.y - 10, 10, 0, .5 * Math.PI, false);
                                context.stroke();
                                context.closePath();
                                break;
                            case '1001':
                                context.beginPath();
                                context.arc(pos.x + 10, pos.y - 10, 10, .5 * Math.PI, Math.PI, false);
                                context.stroke();
                                context.closePath();
                                break;
                            default:
                                var dist = 10;
                                code.forEach(function (v, index) {
                                    if (v) {
                                        context.beginPath();
                                        context.moveTo(pos.x, pos.y);
                                        context.lineTo(pos.x + _COS[index] * dist, pos.y + _SIN[index] * dist);
                                        context.stroke();
                                        context.closePath();
                                    }
                                });
                        }
                    }
                    context.strokeStyle = "#D60022";
                    context.beginPath();
                    context.moveTo(980, 150);
                    context.lineTo(1010, 150);
                    context.arc(1010, 160, 10, -.5 * Math.PI, .5 * Math.PI, false);
                    context.lineTo(980, 170);
                    context.arc(980, 160, 10, .5 * Math.PI, 1.5 * Math.PI, false);
                    context.stroke();
                    context.closePath();
                }
            }
        }
    };

    var drawFoodMap = function(map) {
        context.lineWidth = 2;
        for (var j = 1; j < map.length - 1; j++) {
            for (var i = 1; i < map[j].length - 1; i++) {
                var value = map[j][i];
                var pos = logicToPhysical(i, j);
                if (value === 1) {
                    context.fillStyle = "#FFAAA4";
                    context.beginPath();
                    context.arc(pos.x, pos.y, 3, 0, 2 * Math.PI, true);
                    context.fill();
                    context.closePath();
                } else if (value === 2) {
                    if (energizerAppear) {
                        context.fillStyle = "#FFAAA4";
                        context.beginPath();
                        context.arc(pos.x, pos.y, 10, 0, 2 * Math.PI, true);
                        context.fill();
                        context.closePath();
                    }
                }
            }
        }
    };

    var drawPacman = function(src, x, y, width, height, angle) {
        context.save();
        context.translate(x + width / 2, y + height / 2);
        context.rotate(angle);
        context.translate(- x - width / 2, - y - height / 2);
        context.drawImage(src, x, y, width, height);
        context.restore();
    };

    var clear = function() {
        context.clearRect(0,0, canvas.width, canvas.height);
    };

    return {
        drawBackground: drawBackground,
        drawMaze: drawMaze,
        drawFoodMap: drawFoodMap,
        drawPacman: drawPacman,
        clear: clear,
        dims: {height: canvas.height, width: canvas.width}
    }
}


window.onload = function() {
    pacmanImg = [];
    pacmanImg[0] = new Image();
    pacmanImg[0].src = pacmanImgSrcPrefix + "stage1.png";
    pacmanImg[1] = new Image();
    pacmanImg[1].src = pacmanImgSrcPrefix + "stage2.png";
    pacmanImg[2] = new Image();
    pacmanImg[2].src = pacmanImgSrcPrefix + "stage3.png";
    document.addEventListener("keydown", function(e) {
        isKeyDown = true;
        keyCode = e.keyCode;
    });
    document.addEventListener("keyup", function() {
        isKeyDown = false;
    });
    app = createApp(document.querySelector("canvas"));
    app.drawBackground();
    $.get("/PacManGo/load",function (data) {
        app.drawMaze(data.maze);
        app.drawFoodMap(data.foodMap);
        app.drawPacman(pacmanImg[0], data.pacman.loc.x - data.pacman.size / 2, data.pacman.loc.y - data.pacman.size / 2, data.pacman.size, data.pacman.size, 0);
    }, "json");
    setUpdateFreq();
};

/**
 * Determine how often the game updates occur.
 */
function setUpdateFreq() {
    updateInterval = setInterval("updateGame()", 100);
}

var energizerAppear = true;
function updateGame() {
    pacmanStage = (pacmanStage + 1) % 3;
    energizerAppear = !energizerAppear;
    if (isKeyDown) {
        var move;
        if (keyCode === 37) {
            move = "left";
        } else if (keyCode === 38) {
            move ="up";
        } else if (keyCode === 39) {
            move = "right";
        } else if (keyCode === 40) {
            move = "down";
        } else {
            return;
        }
        $.post("/PacManGo/pacman-move", move, function (data) {

        })
    }
    $.get("/PacManGo/update", function(data) {
        app.clear();
        app.drawBackground();
        app.drawMaze(data.maze);
        app.drawFoodMap(data.foodMap);
        if (data.pacman.dir.directionName === "left") {
            pacmanAngle = 0;
        } else if (data.pacman.dir.directionName === "up") {
            pacmanAngle = .5 * Math.PI;
        } else if (data.pacman.dir.directionName === "right") {
            pacmanAngle = Math.PI;
        } else if (data.pacman.dir.directionName === "down") {
            pacmanAngle = 1.5 * Math.PI;
        }
        app.drawPacman(pacmanImg[pacmanStage], data.pacman.loc.x - data.pacman.size / 2, data.pacman.loc.y - data.pacman.size / 2, data.pacman.size, data.pacman.size, pacmanAngle);
    }, "json");
}
