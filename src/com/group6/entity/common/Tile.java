package com.group6.entity.common;

import com.group6.controller.GameController;
import lombok.Getter;
import lombok.Setter;

import java.awt.Point;

@Getter
@Setter
public class Tile {
    private String name;
    private boolean flooded;
    //Flooded 和 sunk 为不同状态，仅处于一个状态
    private boolean sunk;
    // 添加位置信息
    private Point position;

    public Tile(String name, int x, int y) {
        this.name = name;
        this.flooded = false;
        this.sunk = false;
        this.position = new Point(x, y);
    }

    public boolean isNearBy(Tile other) {
        // 检查是否相邻（上下左右）
        int dx = Math.abs(this.position.x - other.position.x);
        int dy = Math.abs(this.position.y - other.position.y);
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }


    // 判断当前 tile 是否“可用”（沉没后不可用）
    public boolean isAvailable() {
        return !sunk;
    }

    // 判断是否淹没，用于沙袋修复
    public boolean isFlooded() {
        return flooded && !sunk;
    }

    // 恢复：用于沙袋
    public void recover() {
        if (isFlooded()) {
            this.flooded = false;
        }
    }

    // 沉没：用于二次抽到同一个洪水 tile
    public void sink() {
        this.sunk = true;
        this.flooded = false;
    }

    //被洪水淹没：首次淹没标记 flooded，若已淹没则沉没
    public void flood() {
        if (!sunk) {
            if (!flooded) {
                this.flooded = true;
            } else {
                sink();
            }
        }
    }
}
