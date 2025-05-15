package com.group6.entity.common;

import lombok.Getter;
import lombok.Setter;

import java.awt.Point;

@Getter
@Setter
public class Tile {
    private String name;
    private boolean flooded;
    private boolean sunk;
    // 添加位置信息
    private Point position;

    public Tile(String name, int x, int y) {//
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

    public void initializeTiles(){
        List<int[]> positions = List.of(
            new int[]{1, 1}, new int[]{1, 2}, new int[]{1, 3}, new int[]{1, 4},
            new int[]{2, 1}, new int[]{2, 2}, new int[]{2, 3}, new int[]{2, 4},
            new int[]{3, 1}, new int[]{3, 2}, new int[]{3, 3}, new int[]{3, 4},
            new int[]{4, 1}, new int[]{4, 2}, new int[]{4, 3}, new int[]{4, 4},
            new int[]{1, 0}, new int[]{2, 0}, new int[]{3, 5}, new int[]{4, 5},
            new int[]{0, 2}, new int[]{0, 3}, new int[]{5, 2}, new int[]{5, 3}
        );
        Collections.shuffle(positions);
        for(int[] position : positions){
            for (int i = 0; i < 24; i++){
                List<Tile> tiles = GameBoard.getTiles();
                tiles.get(i).setPosition(position[0]);
                tiles.get(i).setFlooded(false);
                tiles.get(i).setSunk(false);
                tiles.get(i).setName(tiles.get(i).getName());
            }
        }
    }

    public void tileDescend(){// 下降一个状态 没flood -> flood 或者 flood -> sink
        if(this.flooded){
            this.sunk = true;
        }else if(!this.flooded){
            this.flooded = true;
            this.sunk = false;
        }

    }
    
    //上升操作已经在player 的 shoreup实现了
    // public void tileRise(){//上升一个状态  sunk -> flood 或者 flood -> 没flood
    //     if(this.sunk){
    //         this.flooded = true;
    //         this.sunk = false;
    //     }else if(this.flooded){
    //         this.flooded = false;
    //     }
    // }

    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
    }

    public boolean isFlooded() {
        return flooded;
    }

    public boolean isSunk() {
        return sunk;
    }

    public Point getPosition() {
        return position;
    }   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }   

 
}
