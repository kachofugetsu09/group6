package com.group6.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverDialog extends JDialog {

    private GameFrame ownerFrame; // 用来引用主GameFrame

    public GameOverDialog(GameFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.ownerFrame = owner;
        initComponents();
        pack(); // 根据内容调整对话框大小
        setLocationRelativeTo(owner); // 相对于父窗口居中
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 关闭对话框时的操作
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // 主布局，带边距

        // "Game Over" 文本标签
        JLabel gameOverLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setForeground(Color.RED);
        add(gameOverLabel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // "返回菜单" 按钮
        JButton returnToMenuButton = new JButton("返回主菜单");
        returnToMenuButton.setFont(new Font("Arial", Font.PLAIN, 18));
        returnToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 当按钮被点击时执行的操作
                returnToMainMenu();
            }
        });
        buttonPanel.add(returnToMenuButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 设置对话框的内边距
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void returnToMainMenu() {
        // 关闭当前 Game Over 对话框
        this.dispose();

        // 关闭主游戏窗口
        if (ownerFrame != null) {
            ownerFrame.dispose();
        }

        // 返回主菜单的逻辑
        // 创建并显示 StartMenuFrame 的新实例
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartMenuFrame().setVisible(true);
            }
        });
        System.out.println("返回主菜单...");
    }


    // 可以添加一个静态方法来方便地显示这个对话框
    public static void showGameOver(GameFrame owner) {
        GameOverDialog dialog = new GameOverDialog(owner, "游戏结束", true);
        dialog.setVisible(true);
    }
}