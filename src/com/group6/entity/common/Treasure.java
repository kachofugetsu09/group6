package com.group6.entity.common;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Treasure {
    private String name;
    private Point position;
    private boolean isFound;
    private boolean captured;

    public Treasure(String name, Point position){
        this.name = name;
        this.position = position;
        this.isFound = false;
        this.captured = false;
    }

    public <Treasure> treasures initializeTreasures(){
        List<Treasure> treasures = new ArrayList<>();
        List<String> treasureNames = Arrays.asList("Crystal Skull", "Golden Idol", "Ancient Coin");

        for(int i = 0; i < 8; i++){
            Treasure treasure = new Treasure(treasureNames.get(i%4), get);
            treasures.add(treasure);
        }
    }

        public int generateRandomNumber() {//在0-23中生成8个不重复的随机数
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

    public boolean isFound(){
        return isFound;
    }

    public void setFound(boolean isFound){
        this.isFound = isFound;
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