package com.group6;

import com.group6.GUI.GameFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 故意引入编译错误来测试防护机制
        int syntaxError = "这是一个编译错误"; // 类型不匹配
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        });
    }
}
