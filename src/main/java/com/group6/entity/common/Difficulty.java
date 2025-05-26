package com.group6.entity.common;

public enum Difficulty {
    EASY   (2, 1, 5),
    MEDIUM (3, 2, 6),
    HARD   (4, 3, 7);

    private final int riseCount;   // 宝藏牌中水位上升卡数量
    private final int startLevel;  // 初始水位
    private final int maxLevel;    // 最大水位（超过即失败）

    Difficulty(int riseCount, int startLevel, int maxLevel) {
        this.riseCount  = riseCount;
        this.startLevel = startLevel;
        this.maxLevel   = maxLevel;
    }

    public int getRiseCount()  { return riseCount; }
    public int getStartLevel() { return startLevel; }
    public int getMaxLevel()   { return maxLevel;   }
}
