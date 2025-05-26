package com.group6;

import com.group6.GUI.GameFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int syntaxError = "这是一个编译错误"; // 类型不匹配
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        });
    }
}
