package com.group6.GUI;

import com.group6.controller.GameController;
import com.group6.entity.common.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class StartMenuFrame extends JFrame {
    private Difficulty selectedDifficulty = Difficulty.MEDIUM;
    private final HashMap<String, Difficulty> difficultyMap = new HashMap<>();

    public StartMenuFrame() {
        setTitle("Choose Difficulty");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 背景图作为主面板
        JPanel backgroundPanel = new JPanel() {
            Image background = new ImageIcon(getClass().getClassLoader().getResource("background/Background.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // 标题
        JLabel titleLabel = new JLabel("Forbidden Island", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // 难度按钮面板
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setOpaque(false);
        difficultyPanel.setLayout(new BoxLayout(difficultyPanel, BoxLayout.Y_AXIS));
        difficultyPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 30, 100));

        // 设置难度映射
        difficultyMap.put("Easy", Difficulty.EASY);
        difficultyMap.put("Medium", Difficulty.MEDIUM);
        difficultyMap.put("Hard", Difficulty.HARD);

        // 统一按钮样式
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 36);

        for (String label : new String[]{"Easy", "Medium", "Hard"}) {
            JButton diffButton = new JButton(label);
            diffButton.setFont(buttonFont);
            diffButton.setPreferredSize(new Dimension(400, 70));
            diffButton.setMaximumSize(new Dimension(400, 70));
            diffButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            diffButton.addActionListener(e -> {
                selectedDifficulty = difficultyMap.get(label);
                System.out.println("选中难度: " + selectedDifficulty);
            });

            difficultyPanel.add(diffButton);
            difficultyPanel.add(Box.createVerticalStrut(30));
        }

        backgroundPanel.add(difficultyPanel, BorderLayout.CENTER);

        // 开始按钮
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("微软雅黑", Font.BOLD, 30));
        startButton.setPreferredSize(new Dimension(200, 60));
        startButton.addActionListener(e -> {
            GameController controller = new GameController(selectedDifficulty);
            GameFrame frame = new GameFrame(controller);
            // 补丁：将 UI 注入 controller
            controller.setGameFrame(frame);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
