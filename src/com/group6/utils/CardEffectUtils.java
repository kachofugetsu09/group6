package com.group6.utils;

import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;
import com.group6.entity.deck.FloodDeck;

import java.util.List;

public class CardEffectUtils {

    // 使用沙袋卡修复一个已淹没但未沉没的瓦片
    public static boolean useSandbag(Tile tile) {
        if (tile != null && tile.isFlooded()) {
            tile.recover();
            return true;
        }
        return false;
    }

    // 使用直升机卡移动玩家到目标瓦片）
    public static boolean useHelicopter(List<Player> players, Tile targetTile) {
        if (players == null || targetTile == null || targetTile.isSunk()) {
            return false;
        }
        for (Player player : players) {
            player.setCurrentPosition(targetTile);
        }
        return true;
    }

    // 使用直升机卡移动单个玩家到目标瓦片
    public static boolean useHelicopter(Player player, Tile targetTile) {
        if (player == null || targetTile == null || targetTile.isSunk()) {
            return false;
        }
        player.setCurrentPosition(targetTile);
        return true;
    }

    // 使用水位上升卡：将洪水弃牌堆洗回牌堆顶部
    // 水位值的增加由外部 GameController/WaterMeter 负责
    public static void useWatersRise(FloodDeck floodDeck) {
        if (floodDeck != null && gameController != null) {
            gameController.handleWaterRise();
        }
    }

    // 使用洪水牌淹没或沉没目标瓦片
    public static void useFlood(Tile tile) {
        if (tile != null && tile.isAvailable()) {
            tile.flood();
        }
    }
}
