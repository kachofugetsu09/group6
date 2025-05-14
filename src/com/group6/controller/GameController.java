package com.group6.controller;

import com.group6.entity.common.Card;
import com.group6.entity.common.RoleType;
import com.group6.entity.common.Tile;
import com.group6.entity.common.WaterMeter;
import com.group6.entity.deck.FloodDeck;
import com.group6.entity.deck.TreasureDeck;
import com.group6.entity.gameBoard.GameBoard;
import com.group6.entity.player.Player;
import com.group6.factory.DeckFactory;
import com.group6.factory.RoleFactory;
import com.group6.utils.CardEffectUtils;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private GameBoard gameBoard;
    private Player currentPlayer;
    private Tile selectedTile;

    //牌堆部分
    private TreasureDeck treasureDeck;
    private FloodDeck floodDeck;
    private WaterMeter waterMeter;


    public GameController() {
        initializeGame();
    }

    private void initializeGame() {
        // 创建游戏板
        gameBoard = new GameBoard();

        int initialRiseCount = 3;  // 宝藏牌里水位上升卡张数
        int startWaterLevel  = 1;  // 初始水位等级
        int maxWaterLevel    = 5;  // 最大水位等级（超出即失败）

    // 初始化水位计和牌堆
        waterMeter   = new WaterMeter(startWaterLevel, maxWaterLevel);
        treasureDeck = DeckFactory.createTreasureDeck(initialRiseCount);
        floodDeck    = DeckFactory.createFloodDeck();

    // 初始洪水：抽6张并作用到对应 Tile
        List<Card> initFloods = floodDeck.getNCards(6);
        for (Card c : initFloods) {
            Tile t = findTileByName(c.getName());
            CardEffectUtils.useFlood(t);
            floodDeck.discard(c);
        }




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

    private Tile findTileByName(String name) {
        for (Tile tile : gameBoard.getTiles()) {
            if (tile.getName().equals(name)) {
                return tile;
            }
        }
        return null; // 如果找不到，可以返回 null 或抛异常，根据你的容错策略
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
