package com.group6.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.entity.common.GameState;

import java.io.File;
import java.io.IOException;

//工具类：负责游戏状态的保存与读取

public class GameSaveUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    // 保存游戏状态到指定文件
    public static void saveGame(GameState gameState, File file) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从指定文件读取游戏状态
    public static GameState loadGame(File file) {
        try {
            return mapper.readValue(file, GameState.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
