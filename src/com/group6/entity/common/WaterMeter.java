package com.group6.entity.common;

public class WaterMeter {
    //当前水位
    private int level;
    //最大水位，随难度变化
    private  int maxLevel;

    public WaterMeter(int level,int max_level){
        this.level = level;
        this.maxLevel = max_level;
    }

    //水位上升操作，返回当前水位值
    public int increase() {
        if (level < maxLevel) {
            level++;
        }
        return level;
    }

    public boolean isOverflow() {
        return level > maxLevel;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
