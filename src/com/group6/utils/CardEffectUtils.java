package com.group6.utils;

import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;
import com.group6.entity.deck.FloodDeck;
import com.group6.entity.gameBoard.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class CardEffectUtils {

    // 使用沙袋卡修复一个已淹没但未沉没的瓦片
    public static boolean useSandbag(Tile tile) {
        if (tile != null && tile.isFlooded() && !tile.isSunk()) {
            tile.setFlooded(false);
            tile.setSunk(false);
            return true;
        }
        return false;
    }

    // 使用直升机卡将多个玩家移动至目标瓦片
    public static boolean useHelicopter(List<Player> players, Tile targetTile) {
        if (players == null || targetTile == null || targetTile.isSunk()) return false;
        for (Player player : players) {
            player.setCurrentPosition(targetTile);
        }
        return true;
    }

    // 使用直升机卡将单个玩家移动至目标瓦片（重载）
    public static boolean useHelicopter(Player player, Tile targetTile) {
        List<Player> singlePlayerList = new ArrayList<>();
        singlePlayerList.add(player);
        return useHelicopter(singlePlayerList, targetTile);

    }

    // 使用水位上升卡：洗混洪水弃牌堆并放回顶部，由控制器外部负责提升水位
    public static void useWatersRise(FloodDeck floodDeck) {
        if (floodDeck != null) {
            floodDeck.putBack2Top();
        }
    }

    // 使用洪水卡：根据卡牌名列表在地图中找到对应瓦片并触发 tileDescend
    public static void applyFloodByCardNames(List<String> cardNames, GameBoard board, FloodDeck floodDeck) {
        for (String name : cardNames) {
            Tile tile = findTileByName(board, name);
            if (tile != null) {
                tile.tileDescend();
            }
            // 此处假设卡对象不再使用，仅弃名而非对象（因为Card对象只在控制器中）
            // 若需弃掉实际Card，请由GameController完成
        }
    }

    // 工具方法：通过瓦片名查找瓦片对象
    private static Tile findTileByName(GameBoard board, String name) {
        for (Tile tile : board.getTiles()) {
            if (tile.getName().equals(name)) {
                return tile;
            }
        }
        return null;
    }
}
