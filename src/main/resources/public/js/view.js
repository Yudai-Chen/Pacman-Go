"use strict";

//app to draw polymorphic shapes on canvas
var app;
//the interval ID for updating the game
var updateInterval;
var pacmanImg;
var pacmanAngle = 0;
var pacmanStage = 0;
var pacmanImg2;
var pacmanAngle2 = 0;
var pacmanStage2 = 0;
var pacmanImgSrcPrefix = "./pacman/";
var ghostImgSrcPrefix = "./ghost/";
var creditImgSrcPrefix = "./credit/";
var otherImgSrcPrefix = "./other/";
var keyCode = null;
var isKeyDown = false;
var redGhostImg;
var pinkGhostImg;
var blueGhostImg;
var yellowGhostImg;
var eyeGhostImg;
var frightenBlueGhostImg;
var frightenWhiteGhostImg;
var creditImg;
var readyImg;
var overImg;
var dyingImg;
var displayTarget;
var playerNameImg;
var iconImg;
var fruitImg;
var currentFruitType;

function logicToPhysical(lx, ly) {
  return {
    x: lx * 20 + 10,
    y: ly * 20 + 10,
  };
}

function createApp(canvas) {
  var context = canvas.getContext("2d");

  var drawBackground = function () {
    context.fillStyle = "#000000";
    context.fillRect(0, 0, canvas.width, canvas.height);
  };

  var _COS = [1, 0, -1, 0];
  var _SIN = [0, 1, 0, -1];

  var drawMaze = function (map, id) {
    context.lineWidth = 2;
    for (var j = 1; j < map.length - 1; j++) {
      for (var i = 1; i < map[j].length - 1; i++) {
        var value = map[j][i];
        if (value) {
          var code = [0, 0, 0, 0];
          if (
            map[j][i + 1] &&
            !(
              map[j - 1][i + 1] &&
              map[j + 1][i + 1] &&
              map[j - 1][i] &&
              map[j + 1][i]
            )
          ) {
            code[0] = 1;
          }
          if (
            map[j + 1][i] &&
            !(
              map[j + 1][i - 1] &&
              map[j + 1][i + 1] &&
              map[j][i - 1] &&
              map[j][i + 1]
            )
          ) {
            code[1] = 1;
          }
          if (
            map[j][i - 1] &&
            !(
              map[j - 1][i - 1] &&
              map[j + 1][i - 1] &&
              map[j - 1][i] &&
              map[j + 1][i]
            )
          ) {
            code[2] = 1;
          }
          if (
            map[j - 1][i] &&
            !(
              map[j - 1][i - 1] &&
              map[j - 1][i + 1] &&
              map[j][i - 1] &&
              map[j][i + 1]
            )
          ) {
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
            switch (code.join("")) {
              case "1100":
                context.beginPath();
                context.arc(
                  pos.x + 10,
                  pos.y + 10,
                  10,
                  Math.PI,
                  1.5 * Math.PI,
                  false
                );
                context.stroke();
                context.closePath();
                break;
              case "0110":
                context.beginPath();
                context.arc(
                  pos.x - 10,
                  pos.y + 10,
                  10,
                  1.5 * Math.PI,
                  2 * Math.PI,
                  false
                );
                context.stroke();
                context.closePath();
                break;
              case "0011":
                context.beginPath();
                context.arc(
                  pos.x - 10,
                  pos.y - 10,
                  10,
                  0,
                  0.5 * Math.PI,
                  false
                );
                context.stroke();
                context.closePath();
                break;
              case "1001":
                context.beginPath();
                context.arc(
                  pos.x + 10,
                  pos.y - 10,
                  10,
                  0.5 * Math.PI,
                  Math.PI,
                  false
                );
                context.stroke();
                context.closePath();
                break;
              default:
                var dist = 10;
                code.forEach(function (v, index) {
                  if (v) {
                    context.beginPath();
                    context.moveTo(pos.x, pos.y);
                    context.lineTo(
                      pos.x + _COS[index] * dist,
                      pos.y + _SIN[index] * dist
                    );
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
            context.arc(1010, 160, 10, -0.5 * Math.PI, 0.5 * Math.PI, false);
            context.lineTo(980, 170);
            context.arc(980, 160, 10, 0.5 * Math.PI, 1.5 * Math.PI, false);
            context.stroke();
            context.closePath();
          } else if (id === 2) {
            drawPacManIcon();
          }
        }
      }
    }
  };

  var drawFoodMap = function (map) {
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
        } else if (value === 3) {
          drawFruit(currentFruitType, pos.x, pos.y - 15, 30, 30);
        }
      }
    }
  };

  var drawPacman = function (src, x, y, width, height, angle) {
    context.save();
    context.translate(x + width / 2, y + height / 2);
    context.rotate(angle);
    context.translate(-x - width / 2, -y - height / 2);
    context.drawImage(src, x, y, width, height);
    context.restore();
  };

  var drawDying = function (timeOut, x, y, width, height) {
    if (timeOut === 0) {
      timeOut = 1;
    }
    context.save();
    context.drawImage(dyingImg[11 - timeOut], x, y, width, height);
    context.restore();
  };

  var drawGhost = function (
    state,
    name,
    direction,
    x,
    y,
    width,
    height,
    frightenTimeOut
  ) {
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
    if (state === 4) {
      imgSet = eyeGhostImg;
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

  var drawPlayerName = function (playerName) {
    context.save();
    if (playerName === 1) {
      context.drawImage(playerNameImg[0], 20, 5, 40, 20);
    } else if (playerName === 2) {
      context.drawImage(playerNameImg[1], 150, 5, 40, 20);
    }
    context.restore();
  };

  var drawOver = function (x, y, width, height) {
    context.save();
    context.drawImage(overImg, x, y, width, height);
    context.restore();
  };

  var drawCredit = function (credit, x, y, width, height) {
    var src;
    switch (credit) {
      case 200:
        src = creditImg[0];
        break;
      case 400:
        src = creditImg[1];
        break;
      case 800:
        src = creditImg[2];
        break;
      case 1600:
        src = creditImg[3];
        break;
    }
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
    context.closePath();
    if (color === "yellow") {
      context.beginPath();
      context.arc(x, y, 160, 0, 2 * Math.PI, false);
      context.closePath();
    }
    context.stroke();
    context.restore();
  };

  var drawPacManIcon = function () {
    context.save();
    context.drawImage(iconImg, 228, 148, 84, 24);
    context.restore();
  };

  var drawText = function (text, x, y) {
    context.save();
    context.fillStyle = "#FFFFFF";
    context.font = "20px Airal";
    context.fillText(text, x, y);
    context.restore();
  };

  var drawFruit = function (fruitId, x, y, width, height) {
    context.save();
    context.drawImage(fruitImg[fruitId], x, y, width, height);
    context.restore();
  };

  var clear = function () {
    context.clearRect(0, 0, canvas.width, canvas.height);
  };

  return {
    drawBackground: drawBackground,
    drawMaze: drawMaze,
    drawFoodMap: drawFoodMap,
    drawPacman: drawPacman,
    drawDying: drawDying,
    drawGhost: drawGhost,
    drawReady: drawReady,
    drawOver: drawOver,
    drawCredit: drawCredit,
    drawPlayerName: drawPlayerName,
    drawTarget: drawTarget,
    drawText: drawText,
    drawPacManIcon: drawPacManIcon,
    drawFruit: drawFruit,
    clear: clear,
    dims: { height: canvas.height, width: canvas.width },
  };
}

function loadImages() {
  pacmanImg = [];
  pacmanImg[0] = new Image();
  pacmanImg[0].src = pacmanImgSrcPrefix + "stage1.png";
  pacmanImg[1] = new Image();
  pacmanImg[1].src = pacmanImgSrcPrefix + "stage2.png";
  pacmanImg[2] = new Image();
  pacmanImg[2].src = pacmanImgSrcPrefix + "stage3.png";
  pacmanImg2 = [];
  pacmanImg2[0] = new Image();
  pacmanImg2[0].src = pacmanImgSrcPrefix + "stage1_2.png";
  pacmanImg2[1] = new Image();
  pacmanImg2[1].src = pacmanImgSrcPrefix + "stage2_2.png";
  pacmanImg2[2] = new Image();
  pacmanImg2[2].src = pacmanImgSrcPrefix + "stage3_2.png";
  redGhostImg = [];
  pinkGhostImg = [];
  blueGhostImg = [];
  yellowGhostImg = [];
  eyeGhostImg = [];
  creditImg = [];
  var credit = 100;
  for (var i = 0; i < 4; i++) {
    var suffix;
    credit = credit * 2;
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
    creditImg[i] = new Image();
    creditImg[i].src = creditImgSrcPrefix + credit + ".png";
    redGhostImg[i] = new Image();
    redGhostImg[i].src = ghostImgSrcPrefix + "red_" + suffix;
    pinkGhostImg[i] = new Image();
    pinkGhostImg[i].src = ghostImgSrcPrefix + "pink_" + suffix;
    blueGhostImg[i] = new Image();
    blueGhostImg[i].src = ghostImgSrcPrefix + "blue_" + suffix;
    yellowGhostImg[i] = new Image();
    yellowGhostImg[i].src = ghostImgSrcPrefix + "yellow_" + suffix;
    eyeGhostImg[i] = new Image();
    eyeGhostImg[i].src = ghostImgSrcPrefix + "eye_" + suffix;
  }
  dyingImg = [];
  for (var j = 0; j < 11; j++) {
    var code = j + 1;
    dyingImg[j] = new Image();
    dyingImg[j].src = pacmanImgSrcPrefix + "dying_" + code + ".png";
  }
  readyImg = new Image();
  readyImg.src = otherImgSrcPrefix + "ready.png";
  overImg = new Image();
  overImg.src = otherImgSrcPrefix + "over.png";
  frightenBlueGhostImg = new Image();
  frightenBlueGhostImg.src = ghostImgSrcPrefix + "frighten_blue.png";
  frightenWhiteGhostImg = new Image();
  frightenWhiteGhostImg.src = ghostImgSrcPrefix + "frighten_white.png";
  playerNameImg = [];
  playerNameImg[0] = new Image();
  playerNameImg[0].src = otherImgSrcPrefix + "1UP.png";
  playerNameImg[1] = new Image();
  playerNameImg[1].src = otherImgSrcPrefix + "2UP.png";
  iconImg = new Image();
  iconImg.src = otherImgSrcPrefix + "icon.png";
  fruitImg = [];

  for (var fi = 0; fi < 4; fi++) {
    var id = fi + 1;
    fruitImg[fi] = new Image();
    fruitImg[fi].src = otherImgSrcPrefix + "fruit" + id + ".png";
  }
}

window.onload = function () {
  displayTarget = false;
  loadImages();
  document.addEventListener("keydown", function (e) {
    isKeyDown = true;
    keyCode = e.keyCode;
  });
  document.addEventListener("keyup", function () {
    isKeyDown = false;
  });
  app = createApp(document.querySelector("canvas"));
  app.drawBackground();

  app.drawText("Powered by Team Chaos.", 500, 20);
  $.post(
    "/load",
    $("#map option:selected").val(),
    function (data) {
      currentFruitType = (data.level - 1) % 4;
      app.drawMaze(data.maze, data.mapid);
      app.drawFoodMap(data.foodMap);
      app.drawPacman(
        pacmanImg[0],
        data.pacman.loc.x - data.pacman.size / 2,
        data.pacman.loc.y - data.pacman.size / 2,
        data.pacman.size,
        data.pacman.size,
        0
      );
      if (data.isTwoPlayer) {
        app.drawPacman(
          pacmanImg2[0],
          data.pacman2.loc.x - data.pacman2.size / 2,
          data.pacman2.loc.y - data.pacman2.size / 2,
          data.pacman2.size,
          data.pacman2.size,
          0
        );
      }
      data.ghosts.forEach(function (element) {
        app.drawGhost(
          element.state,
          element.name,
          element.dir.directionName,
          element.loc.x - element.size / 2,
          element.loc.y - element.size / 2,
          element.size,
          element.size,
          element.frightenTimeOut
        );
        if (displayTarget) {
          app.drawTarget(
            element.targetForDebug.x,
            element.targetForDebug.y,
            element.name
          );
        }
      });
      app.drawText(data.pacman.credit, 70, 20);
      app.drawPlayerName(1);
      if (data.isTwoPlayer) {
        app.drawText(data.pacman2.credit, 200, 20);
        app.drawPlayerName(2);
      }
    },
    "json"
  );
  app.drawReady(readyImg, 720, 200, 45, 7);
  setUpdateFreq();
  $("#btn-pause").click(pause);
  $("#btn-resume").click(resume);
  $("#btn-display").click(displayTargetOrNot);
  $("#btn-player").click(addPlayer);
  $("#btn-editor").click(mapEditor);
  $("#btn-map").click(restart);
  $("#btn-restart").click(restart);
};

function restart() {
  $.post(
    "/load",
    $("#map option:selected").val(),
    function (data) {
      currentFruitType = (data.level - 1) % 4;
      app.drawMaze(data.maze, data.mapid);
      app.drawFoodMap(data.foodMap);
      app.drawPacman(
        pacmanImg[0],
        data.pacman.loc.x - data.pacman.size / 2,
        data.pacman.loc.y - data.pacman.size / 2,
        data.pacman.size,
        data.pacman.size,
        0
      );
      if (data.isTwoPlayer) {
        app.drawPacman(
          pacmanImg2[0],
          data.pacman2.loc.x - data.pacman2.size / 2,
          data.pacman2.loc.y - data.pacman2.size / 2,
          data.pacman2.size,
          data.pacman2.size,
          0
        );
      }
      data.ghosts.forEach(function (element) {
        app.drawGhost(
          element.state,
          element.name,
          element.dir.directionName,
          element.loc.x - element.size / 2,
          element.loc.y - element.size / 2,
          element.size,
          element.size,
          element.frightenTimeOut
        );
        if (displayTarget) {
          app.drawTarget(
            element.targetForDebug.x,
            element.targetForDebug.y,
            element.name
          );
        }
      });
      app.drawText(data.pacman.credit, 70, 20);
      app.drawPlayerName(1);
      if (data.isTwoPlayer) {
        app.drawText(data.pacman2.credit, 200, 20);
        app.drawPlayerName(2);
      }
    },
    "json"
  );
}

function mapEditor() {
  window.location.href = "mapEditor.html";
}

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

function addPlayer() {
  $.post("/add-player", $("#map option:selected").val(), function (data) {});
}

function displayTargetOrNot() {
  displayTarget = !displayTarget;
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
      move = "up";
    } else if (keyCode === 39) {
      move = "right";
    } else if (keyCode === 40) {
      move = "down";
    } else if (keyCode === 65) {
      move = "left2";
    } else if (keyCode === 87) {
      move = "up2";
    } else if (keyCode === 68) {
      move = "right2";
    } else if (keyCode === 83) {
      move = "down2";
    } else {
      return;
    }
    $.post("/pacman-move", move, function (data) {});
  }
  $.get(
    "/update",
    function (data) {
      app.clear();
      app.drawBackground();
      app.drawText("Powered by Yudai Chen.", 500, 20);
      currentFruitType = (data.level - 1) % 4;
      app.drawMaze(data.maze, data.mapid);
      app.drawFoodMap(data.foodMap);
      app.drawText(data.pacman.credit, 70, 20);
      app.drawPlayerName(1);
      if (data.isTwoPlayer) {
        app.drawText(data.pacman2.credit, 200, 20);
        app.drawPlayerName(2);
      }
      for (var i = 1; i <= data.level; i++) {
        app.drawFruit((i - 1) % 4, 1180 - i * 40, 360, 30, 30);
      }

      if (data.life > 0) {
        for (var t = 0; t < data.life - 1; t++) {
          app.drawPacman(
            pacmanImg[1],
            20 + t * 40,
            360,
            data.pacman.size,
            data.pacman.size,
            0
          );
        }
      } else {
        app.drawOver(680, 220, 120, 20);
        return;
      }

      if (data.period === 0) {
        app.drawReady(readyImg, 695, 225, 90, 15);
      }

      if (data.pacman.dir.directionName === "left") {
        pacmanAngle = 0;
      } else if (data.pacman.dir.directionName === "up") {
        pacmanAngle = 0.5 * Math.PI;
      } else if (data.pacman.dir.directionName === "right") {
        pacmanAngle = Math.PI;
      } else if (data.pacman.dir.directionName === "down") {
        pacmanAngle = 1.5 * Math.PI;
      }
      if (data.isTwoPlayer) {
        if (data.pacman2.dir.directionName === "left") {
          pacmanAngle2 = 0;
        } else if (data.pacman2.dir.directionName === "up") {
          pacmanAngle2 = 0.5 * Math.PI;
        } else if (data.pacman2.dir.directionName === "right") {
          pacmanAngle2 = Math.PI;
        } else if (data.pacman2.dir.directionName === "down") {
          pacmanAngle2 = 1.5 * Math.PI;
        }
      }

      if (data.dying) {
        app.drawDying(
          data.dyingTimeOut,
          data.pacman.loc.x - data.pacman.size / 2,
          data.pacman.loc.y - data.pacman.size / 2,
          data.pacman.size,
          data.pacman.size
        );
        if (data.isTwoPlayer) {
          app.drawDying(
            data.dyingTimeOut,
            data.pacman2.loc.x - data.pacman2.size / 2,
            data.pacman2.loc.y - data.pacman2.size / 2,
            data.pacman2.size,
            data.pacman2.size
          );
        }
      } else {
        if (data.gamePause !== true) {
          app.drawPacman(
            pacmanImg[pacmanStage],
            data.pacman.loc.x - data.pacman.size / 2,
            data.pacman.loc.y - data.pacman.size / 2,
            data.pacman.size,
            data.pacman.size,
            pacmanAngle
          );
          if (data.isTwoPlayer) {
            app.drawPacman(
              pacmanImg2[pacmanStage],
              data.pacman2.loc.x - data.pacman2.size / 2,
              data.pacman2.loc.y - data.pacman2.size / 2,
              data.pacman2.size,
              data.pacman2.size,
              pacmanAngle2
            );
          }
        }
        if (!data.dying) {
          data.ghosts.forEach(function (element) {
            if (element.eating) {
              app.drawCredit(
                data.currentGhostCredit,
                element.loc.x - element.size / 2,
                element.loc.y - element.size / 2,
                element.size,
                element.size
              );
            } else {
              app.drawGhost(
                element.state,
                element.name,
                element.dir.directionName,
                element.loc.x - element.size / 2,
                element.loc.y - element.size / 2,
                element.size,
                element.size,
                element.frightenTimeOut
              );
            }
            if (element.state !== 3 && displayTarget) {
              app.drawTarget(
                element.targetForDebug.x,
                element.targetForDebug.y,
                element.name
              );
            }
          });
        }
      }
    },
    "json"
  );
}
