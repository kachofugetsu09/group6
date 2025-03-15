package com.group6.entity.common;

import com.group6.entity.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Tile {
    private int row;
    private int col;
    private boolean flooded;
    private Player occupyingPlayer;

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.flooded = false;
        this.occupyingPlayer = null;
    }

    public boolean isNearBy(Tile destination) {
        return (Math.abs(this.row - destination.row) + Math.abs(this.col - destination.col)) == 1;
    }

    public boolean isFlooded() {
        return flooded;
    }
}