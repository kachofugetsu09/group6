package com.group6.entity.common;

public class WaterMeter {
    //当前水位
    private int level;
    //最大水位，随难度变化
    private final int maxLevel = 10;
    private GameDifficulty difficulty;

    public WaterMeter(GameDifficulty difficulty){
        this.difficulty = difficulty;
        this.level = difficulty.getInitialWaterLevel();
        this.maxLevel = 10;
    }

    //水位上升操作，返回当前水位值
    public int increase() {
        if (level < maxLevel) {
            level++;
        }
        return level;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public int getFloodCardsCount() {
        if (level <= 2) return 2;
        if (level <= 5) return 3;
        if (level <= 7) return 4;
        return 5;
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

    
    public boolean isDangerous() {
        if(level >= 7){
            return true;
        }
        return false;
    }

    // 获取当前水位的描述信息（用于UI显示）
    public String getWaterLevelDescription() {
        return "Water Level: " + level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
