package com.group6.controller;

import com.group6.entity.common.Card;
import com.group6.entity.common.RoleType;
import com.group6.entity.common.Tile;
import com.group6.entity.common.Treasure;
import com.group6.entity.common.WaterMeter;
import com.group6.entity.deck.FloodDeck;
import com.group6.entity.deck.TreasureDeck;
import com.group6.entity.gameBoard.GameBoard;
import com.group6.entity.player.Player;
import com.group6.factory.DeckFactory;
import com.group6.factory.RoleFactory;
import com.group6.utils.CardEffectUtils;

import java.awt.Point;
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

    private static GameController instance;


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


        // 创建24个瓦片，使用Tile中定义的位置
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
        // 预定义的瓦片名称，可以根据游戏规则自定义
        String[] tileNames = {
            "失落之桥", "黄昏花园", "破碎神殿", "珊瑚宫殿", "珊瑚宫殿", "幽灵岛",
            "银色门户", "青铜门", "铜门", "愚者降落点", "金门", "铁门",
            "狮子门", "迷雾沼泽", "神秘沙丘", "海洋之眼", "鹦鹉通道", "发现地",
            "旋风岛", "珊瑚礁", "日出之地", "守望高地", "旋涡花园", "啸风悬崖"
        };
        for (int i = 0; i < 24; i++) {
            // 创建瓦片，初始位置为(0,0)，稍后会随机分配位置
            tiles.add(new Tile(tileNames[i], 0, 0));
        }
        gameBoard.setTiles(tiles);
        
        // 使用Tile类的方法随机初始化瓦片位置
        Tile tempTile = new Tile("临时瓦片", 0, 0);
        tempTile.initializeTiles();

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

        // 初始化宝藏
        initializeTreasures();

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
