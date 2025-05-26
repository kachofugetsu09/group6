package com.group6.entity.common;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Treasure {
    private String name;
    private Point position;
    private boolean captured;//这个是用来表示被拿了之后，用来消除地图上的图标用的。统计四个宝箱是否被拿全是在GameController里的hashmap

    public Treasure(String name, Point position){
        this.name = name;
        this.position = position;
        this.captured = false;
    }

    public static void initializeTreasures(List<Treasure> treasures) {
        if (treasures == null) {
            throw new IllegalArgumentException("Treasures cannot null");
        }

        List<String> treasureNames = Arrays.asList("The Earth Stone", "The Crystal of Fire", "The Statue of the Wind", "The Ocean's Chalice");
        ArrayList<Point> positions = Tile.getPositions();
        Collections.shuffle(positions); // ✅ 在循环外洗牌一次

        for (int i = 0; i < 8; i++) {
            Point pos = positions.get(i);
            Treasure treasure = new Treasure(treasureNames.get(i % 4), pos);
            treasures.add(treasure);
        }
    }


    public static List<Integer>  generateRandomNumber() {//在0-23中生成8个不重复的随机数
            // 创建一个包含0到23的列表
            List<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                numbers.add(i);
            }
    
            // 打乱列表
            Collections.shuffle(numbers);
    
            // 选择前8个数字
            List<Integer> randomNumbers = numbers.subList(0, 8);
            return randomNumbers;
        }


    
    public String getName(){
        return name;
    }
    
    public Point getPosition(){
        return position;
    }

    public void setPosition(Point position){
        this.position = position;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }
}