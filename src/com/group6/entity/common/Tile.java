package com.group6.entity.common;

import lombok.Getter;
import lombok.Setter;

import java.awt.Point;

@Getter
@Setter
public class Tile {
    private String name;
    private boolean flooded;
    // 添加位置信息
    private Point position;

    public Tile(String name, int x, int y) {
        this.name = name;
        this.flooded = false;
        this.position = new Point(x, y);
    }

    public boolean isNearBy(Tile other) {
        // 检查是否相邻（上下左右）
        int dx = Math.abs(this.position.x - other.position.x);
        int dy = Math.abs(this.position.y - other.position.y);
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }
}
