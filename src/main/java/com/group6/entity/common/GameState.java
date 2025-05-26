package com.group6.entity.common;

import com.group6.entity.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

//游戏状态数据类，用于保存和恢复整个游戏的关键状态。
@Getter
@Setter
public class GameState {

    // 当前玩家索引（0~3）
    private int currentPlayerIndex;
    // 所有玩家信息
    private List<Player> players;
    // 所有瓦片状态（是否被淹、沉没、宝藏等）
    private List<Tile> tiles;

    // 所有宝藏对象（是否已被获取）
    private List<Treasure> treasures;
    // 宝藏牌堆和弃牌堆
    private List<Card> treasureDeck;
    private List<Card> treasureDiscard;

    // 洪水牌堆和弃牌堆
    private List<Card> floodDeck;
    private List<Card> floodDiscard;

    // 当前水位等级（1~5）
    private int waterLevel;

    // 四种宝藏是否已获取
    private HashMap<String, Boolean> capturedTreasures;

    private List<Point> playerPositions;

}
