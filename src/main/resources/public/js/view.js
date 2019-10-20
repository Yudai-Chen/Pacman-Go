'use strict';

//app to draw polymorphic shapes on canvas
var app;

function createApp(canvas) {
    var context = canvas.getContext("2d");

    var logicToPhysical = function(lx,ly) {
        return {
            x: lx * 20 + 10,
            y: ly * 20 + 10
        };
    };

    var _COS = [1,0,-1,0];
    var _SIN = [0,1,0,-1];

    var drawMap = function(map) {
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
                                context.strokeStyle = "#FFFFFF";
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
                } else {
                    
                }
            }
        }

    };

    return {
        drawMap: drawMap
    }
}


window.onload = function() {
    app = createApp(document.querySelector("canvas"));

    $.get("/PacManGo/map",function (data) {
        app.drawMap(data);
    }, "json");

};