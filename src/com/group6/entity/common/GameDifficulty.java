public enum GameDifficulty {
    NOVICE(1, 8),      // 新手模式：初始水位1，最大水位8
    NORMAL(2, 9),      // 普通模式：初始水位2，最大水位9
    ELITE(3, 10),      // 精英模式：初始水位3，最大水位10
    LEGENDARY(4, 10);  // 传奇模式：初始水位4，最大水位10

    private final int initialWaterLevel;
    private final int maxWaterLevel = 10;

    GameDifficulty(int initialWaterLevel) {
        this.initialWaterLevel = initialWaterLevel;
    }

    public int getInitialWaterLevel() {
        return initialWaterLevel;
    }

    public int getMaxWaterLevel() {
        return maxWaterLevel;
    }
}
