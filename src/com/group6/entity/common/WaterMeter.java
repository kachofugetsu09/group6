package com.group6.entity.common;

import com.group6.entity.common.Difficulty; // 确保路径正确

public class WaterMeter {
    // 当前水位
    private int level;
    // 最大水位（由 Difficulty 提供）
    private final int maxLevel;
    private Difficulty difficulty;

    // 构造方法：根据难度设置初始水位与最大水位
    public WaterMeter(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.level = difficulty.getStartLevel();
        this.maxLevel = difficulty.getMaxLevel();
    }

    // 水位上升操作，返回当前水位值
    public int increase() {
        if (level < maxLevel) {
            level++;
        }
        return level;
    }

    public Difficulty getDifficulty() {
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
        return level >= 7;
    }

    // 获取当前水位的描述信息（用于 UI 显示）
    public String getWaterLevelDescription() {
        return "Water Level: " + level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
