package com.group6.entity.gameBoard;

import com.group6.entity.common.Tile;
import com.group6.entity.common.Treasure;
import com.group6.entity.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class GameBoard {
    private List<Tile> tiles;
    private List<Treasure> treasures;
    private List<Player> players;

    public GameBoard() {
        tiles = new ArrayList<>();
        treasures = new ArrayList<>();
        players = new ArrayList<>();
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Player> getPlayers() {
        return players;
    }
    
    public List<Treasure> getTreasures() {
        return treasures;
    }
}