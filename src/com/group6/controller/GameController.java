package com.group6.controller;

import com.group6.entity.common.RoleType;
import com.group6.entity.common.Tile;
import com.group6.entity.gameBoard.GameBoard;
import com.group6.entity.player.Player;
import com.group6.factory.RoleFactory;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private GameBoard gameBoard;
    private Player currentPlayer;
    private Tile selectedTile;

    public GameController() {
        initializeGame();
    }

    private void initializeGame() {
        // 创建游戏板
        gameBoard = new GameBoard();

        // 创建6x6的瓷砖
        List<Tile> tiles = new ArrayList<>();
        for (int y = 1; y <= 6; y++) {
            for (int x = 1; x <= 6; x++) {
                tiles.add(new Tile("岛屿 " + x + "," + y, x, y));
            }
        }
        gameBoard.setTiles(tiles);

        // 创建四个角色
        List<Player> players = new ArrayList<>();

        // 工程师 (3,1)
        Player engineer = RoleFactory.createRole(RoleType.ENGINEER);
        engineer.setColor("RED");
        engineer.setCurrentPosition(findTileAt(3, 1));
        players.add(engineer);

        // 飞行员 (1,3)
        Player pilot = RoleFactory.createRole(RoleType.PILOT);
        pilot.setColor("BLUE");
        pilot.setCurrentPosition(findTileAt(1, 3));
        players.add(pilot);

        // 探险家 (4,6)
        Player explorer = RoleFactory.createRole(RoleType.EXPLORER);
        explorer.setColor("GREEN");
        explorer.setCurrentPosition(findTileAt(4, 6));
        players.add(explorer);

        // 潜水员 (6,4)
        Player diver = RoleFactory.createRole(RoleType.DIVER);
        diver.setColor("BLACK");
        diver.setCurrentPosition(findTileAt(6, 4));
        players.add(diver);

        gameBoard.setPlayers(players);

        // 设置当前玩家为工程师
        currentPlayer = engineer;
        currentPlayer.startTurn();
    }

    // 根据坐标查找瓷砖
    public Tile findTileAt(int x, int y) {
        for (Tile tile : gameBoard.getTiles()) {
            if (tile.getPosition().x == x && tile.getPosition().y == y) {
                return tile;
            }
        }
        return null;
    }

    // 选择瓷砖
    public void selectTile(Tile tile) {
        selectedTile = tile;
    }

    // 移动当前玩家
    public boolean moveCurrentPlayer() {
        if (selectedTile != null && currentPlayer.isMoveAble()) {
            boolean moved = currentPlayer.move(selectedTile);
            selectedTile = null;
            return moved;
        }
        return false;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }
}
