package com.group6.entity.common;

import com.group6.controller.GameController;
import lombok.Getter;
import lombok.Setter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Tile {
    private String name;
    private boolean flooded;
    private boolean sunk;
    // 添加位置信息
    private Point position;
    private boolean isFoolsLanding;

    private boolean hasTreasure;
    private Treasure treasure;
    
    // 改为 ArrayList<Point> 以便能够被 shuffle
    public static ArrayList<Point> positions = new ArrayList<>();
    
    // 静态初始化块，初始化 positions
    static {
        // 添加所有可能的位置
        positions.add(new Point(1, 1)); positions.add(new Point(1, 2)); 
        positions.add(new Point(1, 3)); positions.add(new Point(1, 4));
        positions.add(new Point(2, 1)); positions.add(new Point(2, 2)); 
        positions.add(new Point(2, 3)); positions.add(new Point(2, 4));
        positions.add(new Point(3, 1)); positions.add(new Point(3, 2)); 
        positions.add(new Point(3, 3)); positions.add(new Point(3, 4));
        positions.add(new Point(4, 1)); positions.add(new Point(4, 2)); 
        positions.add(new Point(4, 3)); positions.add(new Point(4, 4));
        positions.add(new Point(1, 0)); positions.add(new Point(2, 0)); 
        positions.add(new Point(3, 5)); positions.add(new Point(4, 5));
        positions.add(new Point(0, 2)); positions.add(new Point(0, 3)); 
        positions.add(new Point(5, 2)); positions.add(new Point(5, 3));
    }

    public Tile(String name, int x, int y) {//
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


    public void initializeTiles(){
        // 使用 Collections.shuffle 来打乱位置顺序
        Collections.shuffle(positions);
        
        // 获取游戏板上的所有瓦片
        List<Tile> tiles = GameBoard.getTiles();
        tiles.get(0).setFoolsLanding(true);//随便找一个，后面随机放位置
        
        // 为每个瓦片分配一个随机位置
        for(int i = 0; i < Math.min(tiles.size(), positions.size()); i++) {
            Point position = positions.get(i);
            tiles.get(i).setPosition(position.x, position.y);
            tiles.get(i).setFlooded(false);
            tiles.get(i).setSunk(false);
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

    public void setTreasure(Treasure treasure){
        this.hasTreasure = true;
        this.treasure = treasure;
    }

    public void removeTreasure(){
        this.hasTreasure = false;
        this.treasure = null;
    }

    public boolean hahasTreasure(){
        return hasTreasure;
    }

    public Treasure getTreasure(){
        return treasure;
    }

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

    public ArrayList<Point> getPositions(){
        return ArrayList<Point> positions;
    }

    public boolean getIsFoolsLanding(){
        return isFoolsLanding;
    }

    public void setFoolsLanding(boolean isFoolsLanding){
        this.isFoolsLanding = isFoolsLanding;
    }
}
