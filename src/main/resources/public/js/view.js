'use strict';

//app to draw polymorphic shapes on canvas
var app;
//the interval ID for updating the game
var updateInterval;
var pacmanImg;
var pacmanImgSrcPrefix = "./pacman/";
var ghostImgSrcPrefix = "./ghost/";
var otherImgSrcPrefix = "./other/";
var keyCode = null;
var isKeyDown = false;
var pacmanAngle = 0;
var pacmanStage = 0;
var redGhostImg;
var pinkGhostImg;
var blueGhostImg;
var yellowGhostImg;
var frightenBlueGhostImg;
var frightenWhiteGhostImg;
var readyImg;

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

    var drawGhost = function(state, name, direction, x, y, width, height, frightenTimeOut) {
        if (state === 3) {
            context.save();
            if (frightenTimeOut >= 20) {
                context.drawImage(frightenBlueGhostImg, x, y, width, height);
            } else if (frightenTimeOut >= 16) {
                    context.drawImage(frightenWhiteGhostImg, x, y, width, height);
            } else if (frightenTimeOut >= 12) {
                context.drawImage(frightenBlueGhostImg, x, y, width, height);
            } else if (frightenTimeOut >= 8) {
                context.drawImage(frightenWhiteGhostImg, x, y, width, height);
            } else if (frightenTimeOut >= 4) {
                context.drawImage(frightenBlueGhostImg, x, y, width, height);
            } else {
                context.drawImage(frightenWhiteGhostImg, x, y, width, height);
            }
            context.restore();
            return;
        }
        var imgSet;
        switch (name) {
            case "red":
               imgSet = redGhostImg;
               break;
            case "pink":
                imgSet = pinkGhostImg;
                break;
            case "blue":
                imgSet = blueGhostImg;
                break;
            case "yellow":
                imgSet = yellowGhostImg;
                break;
        }
        var index;
        switch (direction) {
            case "left":
                index = 0;
                break;
            case "up":
                index = 1;
                break;
            case "right":
                index = 2;
                break;
            case "down":
                index = 3;
                break;
        }
        context.save();
        context.drawImage(imgSet[index], x, y, width, height);
        context.restore();
    };

    var drawReady = function (src, x, y, width, height) {
        context.save();
        context.drawImage(src, x, y, width, height);
        context.restore();
    };

    var drawTarget = function (x, y, color) {
        context.save();
        switch (color) {
            case "red":
                context.strokeStyle = "#FF0000";
                break;
            case "pink":
                context.strokeStyle = "#FF99CC";
                break;
            case "blue":
                context.strokeStyle = "#33FFFF";
                break;
            case "yellow":
                context.strokeStyle = "#FFCC33";
                break;
        }
        context.beginPath();
        context.moveTo(x - 10, y - 10);
        context.lineTo(x + 10, y + 10);
        context.moveTo(x + 10, y - 10);
        context.lineTo(x - 10, y + 10);
        context.stroke();
        context.restore();
        context.closePath();
        if (color === "yellow") {
            context.beginPath();
            context.arc(x, y, 160, 0, 2 * Math.PI, false);
            context.stroke();
            context.closePath();
        }
    };

    var clear = function() {
        context.clearRect(0,0, canvas.width, canvas.height);
    };

    return {
        drawBackground: drawBackground,
        drawMaze: drawMaze,
        drawFoodMap: drawFoodMap,
        drawPacman: drawPacman,
        drawGhost: drawGhost,
        drawReady: drawReady,
        drawTarget: drawTarget,
        clear: clear,
        dims: {height: canvas.height, width: canvas.width}
    }
}

function loadImages() {
    pacmanImg = [];
    pacmanImg[0] = new Image();
    pacmanImg[0].src = pacmanImgSrcPrefix + "stage1.png";
    pacmanImg[1] = new Image();
    pacmanImg[1].src = pacmanImgSrcPrefix + "stage2.png";
    pacmanImg[2] = new Image();
    pacmanImg[2].src = pacmanImgSrcPrefix + "stage3.png";
    redGhostImg = [];
    pinkGhostImg = [];
    blueGhostImg = [];
    yellowGhostImg = [];
    for (var i = 0; i < 4; i++) {
        var suffix;
        switch (i) {
            case 0:
                suffix = "left.png";
                break;
            case 1:
                suffix = "up.png";
                break;
            case 2:
                suffix = "right.png";
                break;
            default:
                suffix = "down.png";
                break;
        }
        redGhostImg[i] = new Image();
        redGhostImg[i].src = ghostImgSrcPrefix + "red_" + suffix;
        pinkGhostImg[i] = new Image();
        pinkGhostImg[i].src = ghostImgSrcPrefix + "pink_" + suffix;
        blueGhostImg[i] = new Image();
        blueGhostImg[i].src = ghostImgSrcPrefix + "blue_" + suffix;
        yellowGhostImg[i] = new Image();
        yellowGhostImg[i].src = ghostImgSrcPrefix + "yellow_" + suffix;
    }
    readyImg = new Image();
    readyImg.src = otherImgSrcPrefix + "ready.png";
    frightenBlueGhostImg = new Image();
    frightenBlueGhostImg.src = ghostImgSrcPrefix + "frighten_blue.png";
    frightenWhiteGhostImg = new Image();
    frightenWhiteGhostImg.src = ghostImgSrcPrefix + "frighten_white.png";
}

window.onload = function() {
    loadImages();
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
        data.ghosts.forEach(function(element) {
            app.drawGhost(element.state, element.name, element.dir.directionName, element.loc.x - element.size / 2, element.loc.y - element.size / 2, element.size, element.size, element.frightenTimeOut);
            app.drawTarget(element._TARGET.x, element._TARGET.y, element.name);
        });
    }, "json");
    app.drawReady(readyImg, 720, 200, 45, 7);
    setUpdateFreq();
    $("#btn-pause").click(pause);
    $("#btn-resume").click(resume);
};

/**
 * Determine how often the game updates occur.
 */
function setUpdateFreq() {
    updateInterval = setInterval("updateGame()", 100);
}

function pause() {
    clearInterval(updateInterval);
}

function resume() {
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
        if (data.period === 0) {
            app.drawReady(readyImg, 695, 225, 90, 15);
        }
        app.drawPacman(pacmanImg[pacmanStage], data.pacman.loc.x - data.pacman.size / 2, data.pacman.loc.y - data.pacman.size / 2, data.pacman.size, data.pacman.size, pacmanAngle);
        data.ghosts.forEach(function(element) {
            app.drawGhost(element.state, element.name, element.dir.directionName, element.loc.x - element.size / 2, element.loc.y - element.size / 2, element.size, element.size, element.frightenTimeOut);
            if (element.state !== 3) {
                app.drawTarget(element._TARGET.x, element._TARGET.y, element.name);
            }
        });
    }, "json");
}
