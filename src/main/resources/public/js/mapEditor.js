'use strict';
//app to draw polymorphic shapes on canvas
var me;
var container;
//0 for eraser, 1 - 4 for pen
var tool;
var isMouseDown;
var startLoc;
var lastLoc;
var currentCells;
var currentMap;
var pacmanImg;
var ghostImg;

function logicToPhysical(lx,ly) {
    return {
        x: lx * 20 + 10,
        y: ly * 20 + 10
    };
}

function physicalToLogical(px,py) {
    return {
        x: Math.floor(px  / 20),
        y: Math.floor(py  / 20)
    };
}

function getPointOnCanvas(canvas, x, y) {
    var bbox = canvas.getBoundingClientRect();
    return { x: x - bbox.left * (canvas.width  / bbox.width),
        y: y - bbox.top  * (canvas.height / bbox.height)
    };
}

function createMapEditor(canvas) {

    container = document.getElementById("container");

    canvas.addEventListener("mousedown", doMouseDown, false);
    canvas.addEventListener('mousemove', doMouseMove, false);
    canvas.addEventListener('mouseup',   doMouseUp, false);
    
    var context = canvas.getContext("2d");

    function doMouseDown(event) {
        var x = event.pageX;
        var y = event.pageY;
        var canvas = event.target;
        var loc = getPointOnCanvas(canvas, x, y);
        var logical = physicalToLogical(loc.x, loc.y);
        if (tool === 0) {
            lastLoc = logical;
            currentMap[logical.y][logical.x] = tool;
            context.clearRect(0, 0, canvas.width, canvas.height);
            drawBackGround();
            drawMaze(currentMap, 0);
            isMouseDown = true;
        } else if (tool >= 1 && tool <= 5) {
            lastLoc = logical;
            context.save();
            context.lineWidth = 5;
            switch (tool) {
                case 1:
                    context.strokeStyle = "#1414FF";
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
            }
            startLoc = logicToPhysical(logical.x, logical.y);
            isMouseDown = true;
        }
    }

    function doMouseMove(event) {
        var x = event.pageX;
        var y = event.pageY;
        var loc = getPointOnCanvas(event.target, x, y);
        var currentLoc = physicalToLogical(loc.x, loc.y);
        if (tool === 0) {
            if (isMouseDown && (currentLoc.x !== lastLoc.x || currentLoc.y !== lastLoc.y)) {
                currentMap[currentLoc.y][currentLoc.x] = tool;
                context.clearRect(0, 0, canvas.width, canvas.height);
                drawBackGround();
                drawMaze(currentMap, 0);
                lastLoc = logicToPhysical(currentLoc.x, currentLoc.y);
            }
        } else if (tool >= 1 && tool <= 5) {
            if (isMouseDown && (currentLoc.x !== lastLoc.x || currentLoc.y !== lastLoc.y)) {
                loc = logicToPhysical(currentLoc.x, currentLoc.y);
                context.clearRect(0, 0, canvas.width, canvas.height);
                drawBackGround();
                drawMaze(currentMap, 0);
                switch (tool) {
                    case 1:
                        context.strokeStyle = "#1414FF";
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
                }
                context.beginPath();
                context.moveTo(startLoc.x, startLoc.y);
                context.lineTo(loc.x, loc.y);
                context.stroke();
                context.closePath();
                lastLoc = loc;
            }
        }
    }

    function doMouseUp(event) {
        if (tool === 0) {
            if (isMouseDown) {
                doMouseMove(event);
                context.restore();
                isMouseDown = false;
            }
        } else if (tool >= 1 && tool <= 5) {
            var x = event.pageX;
            var y = event.pageY;
            var loc = getPointOnCanvas(event.target, x, y);
            var currentLoc = physicalToLogical(loc.x, loc.y);
            if (isMouseDown) {
                if ((currentLoc.x === (startLoc.x - 10) / 20 && currentLoc.y === (startLoc.y - 10) / 20)) {
                    currentMap[currentLoc.y][currentLoc.x] = tool;
                    context.restore();
                    isMouseDown = false;
                    context.clearRect(0, 0, canvas.width, canvas.height);
                    drawBackGround();
                    drawMaze(currentMap, 0);
                    return;
                }
                doMouseMove(event);
                context.restore();
                isMouseDown = false;
            }
            getCellsOnLine();
            currentCells.forEach(function (element) {
                currentMap[element[1]][element[0]] = tool;
            });
            context.clearRect(0, 0, canvas.width, canvas.height);
            drawBackGround();
            drawMaze(currentMap, 0);
        }
    }

    function getCellsOnLine() {
        currentCells = [];
        var xLength = lastLoc.x - startLoc.x;
        var xCellNumber = Math.abs(xLength / 20);
        var lastY = startLoc.y;
        for (var i = 0; i < xCellNumber; i++) {
            var currentX;
            if (xLength >= 0) {
                currentX = startLoc.x + 10 + i * 20;
            } else {
                currentX = startLoc.x - 10 - i * 20;
            }
            var currentY = getYOnline(currentX);
            var currentLogLoc = physicalToLogical(currentX, currentY);
            if (currentLogLoc.y >= Math.floor(lastY / 20)) {
                for (var j = Math.floor(lastY / 20); j <= currentLogLoc.y; j++) {
                    if (xLength >= 0) {
                        currentCells.push([currentLogLoc.x - 1, j]);
                    } else {
                        currentCells.push([currentLogLoc.x, j]);
                    }
                }
            } else {
                for (var j2 = Math.floor(lastY / 20); j2 >= currentLogLoc.y; j2--) {
                    if (xLength >= 0) {
                        currentCells.push([currentLogLoc.x - 1, j2]);
                    } else {
                        currentCells.push([currentLogLoc.x, j2]);
                    }
                }
            }
            lastY = currentY;
        }
        var lastLogLoc = physicalToLogical(lastLoc.x, lastLoc.y);
        if (lastLogLoc.y >= Math.floor(lastY / 20)) {
            for (var k = Math.floor(lastY / 20); k <= lastLogLoc.y; k++) {
                currentCells.push([lastLogLoc.x, k]);
            }
        } else {
            for (var k2 = Math.floor(lastY / 20); k2 >= lastLogLoc.y; k2--) {
                currentCells.push([lastLogLoc.x, k2]);
            }
        }
    }

    function getYOnline(x) {
        if (lastLoc.x === startLoc.x) {
            return startLoc.y;
        }
        var k = (lastLoc.y - startLoc.y) / (lastLoc.x - startLoc.x);
        var b = startLoc.y - k * startLoc.x;
        return k * x + b;
    }

    var drawBackGround = function () {
        context.save();
        context.fillStyle = "#000000";
        context.fillRect(0, 0, canvas.width, canvas.height);
        for (var j = 0; j < 17; j++) {
            for (var i = 0; i < 29; i++) {
                context.fillStyle = "#323232";
                context.fillRect(20 + 20 * (j % 2) + 40 * i, 20 + 20 * j, 20, 20)
            }
        }
        context.restore();
    };

    var _COS = [1,0,-1,0];
    var _SIN = [0,1,0,-1];

    var drawMaze = function(map, id) {
        context.lineWidth = 5;
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
                    if (id === 1) {
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
        }
        context.drawImage(pacmanImg, 725, 315, 30, 30);
        context.drawImage(ghostImg[0], 725, 95, 30, 30);
        context.drawImage(ghostImg[1], 725, 155, 30, 30);
        context.drawImage(ghostImg[2], 685, 155, 30, 30);
        context.drawImage(ghostImg[3], 765, 155, 30, 30);
    };

    var clear = function() {
        context.clearRect(0,0, canvas.width, canvas.height);
    };

    return {
        drawBackGround : drawBackGround,
        drawMaze : drawMaze,
        clear : clear
    }
}

function initialBlankMap() {
    for(var i = 0; i < 19; i++) {
        currentMap[i] = [];
        for (var j = 0; j < 60; j++) {
            currentMap[i][j] = 0;
            if (i === 0 || i === 18 || j === 0 || j === 59) {
                currentMap[i][j] = 9;
            }
            if (i >= 6 && i <= 10 && j >= 33 && j <= 40) {
                currentMap[i][j] = 1;
            }
            if (i >= 7 && i <= 9 && j >= 34 && j <= 39) {
                currentMap[i][j] = 2;
            }
        }
    }
    currentMap[6][36] = 2;
    currentMap[6][37] = 2;
}

window.onload = function() {
    pacmanImg = new Image();
    pacmanImg.src = "./pacman/stage2.png";
    ghostImg = [];
    ghostImg[0] = new Image();
    ghostImg[0].src = "./ghost/red_left.png";
    ghostImg[1] = new Image();
    ghostImg[1].src = "./ghost/pink_down.png";
    ghostImg[2] = new Image();
    ghostImg[2].src = "./ghost/blue_up.png";
    ghostImg[3] = new Image();
    ghostImg[3].src = "./ghost/yellow_up.png";
    currentMap = [];

    initialBlankMap();
    tool = 9;
    me = createMapEditor(document.getElementById("canvas-editor"));
    me.drawBackGround();
    ghostImg[3].onload = function() {
        me.drawMaze(currentMap, 0);
    };
    me.drawMaze(currentMap, 0);
    $("#btn-load").click(loadMap);
    $("#eraser").click(function () {
        tool = 0;
    });
    $("#pen-blue").click(function () {
        tool = 1;
    });
    $("#pen-red").click(function () {
        tool = 3;
    });
    $("#pen-yellow").click(function () {
        tool = 4;
    });
    $("#pen-green").click(function () {
        tool = 5;
    });
    $("#reset").click(function () {
        initialBlankMap();
        me.drawBackGround();
        me.drawMaze(currentMap, 0);
    });
    $("#btn-submit").click(submitChange);
};

function submitChange() {
    $.post("/mapEditor/submit", JSON.stringify(currentMap), function (data) {

    }, "json");
}

function loadMap() {
    $.post("/mapEditor/load", $("#saved-map option:selected").val(), function (data) {
        currentMap = data;
        me.clear();
        me.drawBackGround();
        me.drawMaze(currentMap, parseInt($("#saved-map option:selected").val()));
    }, "json");
}