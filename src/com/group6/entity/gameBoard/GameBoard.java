package com.group6.entity.gameBoard;

import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GameBoard {
    private List<Tile> tiles;
    private List<Player> players;

}
