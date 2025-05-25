package com.group6.controller;

import java.util.Collections;
import java.util.HashMap;
import com.group6.entity.common.*;
import com.group6.entity.deck.FloodDeck;
import com.group6.entity.deck.TreasureDeck;
import com.group6.entity.gameBoard.GameBoard;
import com.group6.entity.player.Player;
import com.group6.factory.DeckFactory;
import com.group6.factory.RoleFactory;
import com.group6.utils.CardEffectUtils;
import com.group6.GUI.GameFrame;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private GameBoard gameBoard;
    private Player currentPlayer;
    private Tile selectedTile;
    private Difficulty difficulty;
  //牌堆部分
    private TreasureDeck treasureDeck;
    private FloodDeck floodDeck;
    private WaterMeter waterMeter;

    private static GameController instance;
    private HashMap<String, Boolean> capturedTreasures = new HashMap<>();


    public GameController() {
        initializeGame();
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }



    private void initializeGame() {
        // 创建游戏板
        gameBoard = new GameBoard();


        // 替换为 Difficulty 初始化
        Difficulty difficulty = Difficulty.MEDIUM; // TODO: 可从 UI 读取设置
        this.difficulty = difficulty;

        int initialRiseCount = difficulty.getRiseCount();
        waterMeter = new WaterMeter(difficulty);
        treasureDeck = DeckFactory.createTreasureDeck(initialRiseCount);
        floodDeck = DeckFactory.createFloodDeck();


        // 初始洪水：抽6张并作用到对应 Tile
        List<Card> initFloods = floodDeck.getNCards(6);
        for (Card c : initFloods) {
            Tile t = findTileByName(c.getName());
            if (t != null) {
                // 替代 CardEffectUtils.useFlood()
                t.tileDescend();
            }
            floodDeck.discard(c);
        }


        // 创建24个瓷砖
        List<Tile> tiles = new ArrayList<>();
        // 预定义的瓦片名称，可以根据游戏规则自定义
        String[] tileNames = {
            "Temple of the Moon", "Temple of the Sun",
                "Coral Palace", "Tidal Palace",
                "Cave of Embers", "Cave of Shadows",
                "Whispering Garden", "Howling Garden",
                "Bronze Gate", "Silver Gate",
                "Gold Gate", "Iron Gate",
                "Fools' Landing", "Observatory",
                "Crimson Forest", "Lost Lagoon",
                "Dunes of Deception", "Phantom Rock",
                "Breakers Bridge", "Cliffs of Abandon",
                "Misty Marsh", "Watchtower",
                "Twilight Hollow", "Flooded Ruins"
        };
        for (int i = 0; i < 24; i++) {
            // 创建瓦片，初始位置为(0,0)，稍后会随机分配位置
            tiles.add(new Tile(tileNames[i], 0, 0));
        }
        gameBoard.setTiles(tiles);
        
        // 使用Tile类的方法随机初始化瓦片位置
        Tile tempTile = new Tile("临时瓦片", 0, 0);
        tempTile.initializeTiles(gameBoard.getTiles());

        //初始化8个宝藏 和 4个宝藏的全局统计
        List<Treasure> treasures = new ArrayList<>();
        Treasure.initializeTreasures(treasures);//初始化8个宝藏
        HashMap<String,Boolean> capturedTreasures = new HashMap<>();
        capturedTreasures.put("The Earth Stone", false);
        capturedTreasures.put("The Crystal of Fire", false);
        capturedTreasures.put("The Statue of the Wind", false);
        capturedTreasures.put("The Ocean's Chalice", false);


        // 创建四个角色
        List<Player> players = new ArrayList<>();
        
        // 获取一些不同的瓦片用于放置玩家，确保玩家不会重叠
        List<Tile> playerStartTiles = getRandomPlayerStartPositions(4);
        
        // 创建玩家并放置在初始位置
        // 工程师
        Player engineer = RoleFactory.createRole(RoleType.ENGINEER);
        engineer.setColor("RED");
        engineer.setCurrentPosition(playerStartTiles.get(0));
        players.add(engineer);

        // 飞行员
        Player pilot = RoleFactory.createRole(RoleType.PILOT);
        pilot.setColor("BLUE");
        pilot.setCurrentPosition(playerStartTiles.get(1));
        players.add(pilot);

        // 探险家
        Player explorer = RoleFactory.createRole(RoleType.EXPLORER);
        explorer.setColor("GREEN");
        explorer.setCurrentPosition(playerStartTiles.get(2));
        players.add(explorer);

        // 潜水员
        Player diver = RoleFactory.createRole(RoleType.DIVER);
        diver.setColor("BLACK");
        diver.setCurrentPosition(playerStartTiles.get(3));
        players.add(diver);

        gameBoard.setPlayers(players);

        // 设置当前玩家为工程师
        currentPlayer = engineer;
        currentPlayer.startTurn();
    }

    // 在类的成员变量部分添加UI更新引用
    private GameFrame gameFrame;

    // 处理回合结束
    public void endTurn() {
        // 1. 抽取宝藏卡
        drawTreasureCards();
        
        // 2. 抽取洪水卡
        drawFloodCards();
        
        // 3. 切换到下一个玩家
        switchToNextPlayer();

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

    //记录玩家是否获取了宝藏
    public HashMap<String,Boolean> getCapturedTreasures(){
        return capturedTreasures;
    }

    public void handleWaterRise() {
        waterMeter.increase();
        floodDeck.putBack2Top();
        checkGameOver();
    }

    private void drawTreasureCards() {
        // TODO: 实现宝藏牌抽卡逻辑
    }

    private void switchToNextPlayer() {
        // TODO: 切换到下一名玩家
    }


    private void drawFloodCards() {
        int cardsToDrawCount = waterMeter.getFloodCardsCount();
        List<Card> drawnCards = floodDeck.getNCards(cardsToDrawCount);
        for (Card card : drawnCards) {
            Tile tile = findTileByName(card.getName());
            if (tile != null) {
                tile.tileDescend();
            }
            floodDeck.discard(card);
        }
    }


    private boolean checkGameOver() {
        // 检查水位是否溢出
        if (waterMeter.isOverflow()) {
            return true;
        }
        return false;
    }

    private List<Tile> getRandomPlayerStartPositions(int count) {
        List<Tile> allTiles = new ArrayList<>(gameBoard.getTiles());
        Collections.shuffle(allTiles);
        return allTiles.subList(0, count);
    }


}
