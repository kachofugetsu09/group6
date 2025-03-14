package com.group2.GUI;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        // 设置窗口基本属性
        setTitle("Forbidden Island");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建顶部面板
        JPanel topPanel = createTopPanel();

        // 创建中心面板
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createLeftPanel(), BorderLayout.WEST);
        centerPanel.add(createGamePanel(), BorderLayout.CENTER);

// 创建右侧双列面板
        JPanel rightSectionPanel = new JPanel(new BorderLayout());
        rightSectionPanel.add(createRightColumn1(), BorderLayout.EAST); // 最右侧信息区(右列1)
        rightSectionPanel.add(createRightColumn2(), BorderLayout.CENTER); // 洪水弃牌区(右列2)

// 将右侧面板添加到中心面板
        centerPanel.add(rightSectionPanel, BorderLayout.EAST);


        // 创建底部面板
        JPanel bottomPanel = createBottomPanel();

        // 将面板添加到主面板
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 添加主面板到窗口
        add(mainPanel);
    }

    // 创建顶部面板（上方两个玩家棋子展示区）
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(1024, 50));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10)); // 增加间距为20

        // 添加2个玩家区域
        for (int player = 0; player < 2; player++) {
            // 创建玩家棋子（红色方块）
            JPanel playerPiece = new JPanel();
            playerPiece.setPreferredSize(new Dimension(30, 30));
            playerPiece.setBackground(Color.RED);
            panel.add(playerPiece);

            // 在每个玩家后面添加5个白色方块（卡牌）
            for (int card = 0; card < 5; card++) {
                JPanel cardPanel = new JPanel();
                cardPanel.setPreferredSize(new Dimension(30, 30));
                cardPanel.setBackground(Color.WHITE);
                cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.add(cardPanel);
            }

            // 如果不是最后一个玩家，添加一个间隔
            if (player < 1) {
                JPanel spacer = new JPanel();
                spacer.setPreferredSize(new Dimension(30, 30));
                spacer.setBackground(Color.LIGHT_GRAY);
                panel.add(spacer);
            }
        }

        return panel;
    }

    // 创建左侧面板（宝藏弃牌区和水位指示器）
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(150, 600));

        // 创建宝藏弃牌区
        JPanel treasurePanel = new JPanel();
        treasurePanel.setPreferredSize(new Dimension(150, 300));
        treasurePanel.setBackground(new Color(219, 112, 82)); // 橙红色背景
        JLabel treasureLabel = new JLabel("Treasure Discard:");
        treasureLabel.setForeground(Color.WHITE);
        treasurePanel.add(treasureLabel);

        // 创建水位指示器
        JPanel waterPanel = new JPanel();
        waterPanel.setPreferredSize(new Dimension(150, 300));
        waterPanel.setBackground(new Color(0, 102, 153)); // 深蓝色背景

        // 水位指示标记
        waterPanel.setLayout(new GridLayout(5, 1));
        for (int i = 5; i >= 1; i--) {
            JPanel levelPanel = new JPanel();
            levelPanel.setBackground(new Color(0, 102, 153)); // 深蓝色背景
            JLabel levelLabel = new JLabel(String.valueOf(i));
            levelLabel.setForeground(Color.WHITE);
            levelPanel.add(levelLabel);
            waterPanel.add(levelPanel);
        }

        panel.add(treasurePanel, BorderLayout.NORTH);
        panel.add(waterPanel, BorderLayout.CENTER);

        return panel;
    }

    // 创建游戏主面板（岛屿区域）
    private JPanel createGamePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制背景颜色
                g.setColor(new Color(40, 122, 120)); // 蓝绿色
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // 调整游戏面板大小
        panel.setPreferredSize(new Dimension(524, 600)); // 给岛屿区域更多空间

        // 创建岛屿格子（6x6网格）
        panel.setLayout(new GridLayout(6, 6, 2, 2));

        // 添加36个岛屿格子
        for (int i = 0; i < 36; i++) {
            JPanel tilePanel = new JPanel();
            tilePanel.setLayout(new BorderLayout());

            // 为每个格子添加一个标签（实际应用中应加载图片）
            JLabel tileLabel = new JLabel("Tile " + (i+1));
            tileLabel.setHorizontalAlignment(JLabel.CENTER);
            tilePanel.add(tileLabel, BorderLayout.CENTER);

            // 添加名称标签在底部
            JLabel nameLabel = new JLabel("Island Name");
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            nameLabel.setBackground(new Color(210, 180, 140)); // 浅棕色
            nameLabel.setOpaque(true);
            tilePanel.add(nameLabel, BorderLayout.SOUTH);

            panel.add(tilePanel);
        }

        return panel;
    }

    // 创建最右侧面板(右列1：info、config、logs和按钮)
    private JPanel createRightColumn1() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 600));

        // 创建信息区
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        // 添加标题
        JLabel titleLabel = new JLabel("Info");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 添加FORBIDDEN ISLAND标题
        JLabel gameLabel = new JLabel("FORBIDDEN");
        gameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel islandLabel = new JLabel("ISLAND");
        islandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        islandLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // 添加CREATED BY标签
        JLabel createdByLabel = new JLabel("CREATED BY");
        createdByLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createdByLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        // 添加团队名称
        JLabel teamLabel = new JLabel("TEAM");
        teamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        teamLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // 添加配置区域
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setMaximumSize(new Dimension(180, 100));
        configPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        configPanel.setBorder(BorderFactory.createTitledBorder("Config"));

        // 添加难度选择 - 简化版本
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.setMaximumSize(new Dimension(180, 30));
        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyPanel.add(difficultyLabel);

        // 创建简化的难度选择下拉菜单 - 只显示1-5数字
        String[] difficultyLevels = {"1", "2", "3", "4", "5"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficultyLevels);
        difficultyComboBox.setPreferredSize(new Dimension(60, 25));
        difficultyPanel.add(difficultyComboBox);

        // 将配置选项添加到配置面板
        configPanel.add(difficultyPanel);

        // 添加Start按钮
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(100, 30));
        startButton.setBackground(new Color(0, 150, 0)); // 绿色
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));

        // 添加日志区域 - 更小的尺寸
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logPanel.setBorder(BorderFactory.createTitledBorder("Game Log"));
        logPanel.setMaximumSize(new Dimension(180, 100)); // 减小日志区域高度

        JTextArea logArea = new JTextArea(4, 15); // 减少行数
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JScrollPane scrollPane = new JScrollPane(logArea);

        logPanel.add(scrollPane, BorderLayout.CENTER);

        // 创建按钮面板 - 竖向排列
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        // 添加动作按钮 - 竖向排列
        String[] buttonLabels = {"Move To", "Shore Up", "Pass To", "Capture",
                "Lift Off", "Special Actions", "Next", "Discard","Clear"};

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(150, 30));
            button.setMargin(new Insets(2, 2, 2, 2)); // 减小按钮内边距
            buttonsPanel.add(button);
            buttonsPanel.add(Box.createVerticalStrut(5)); // 按钮之间添加间距
        }

        // 添加所有组件到信息面板
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(gameLabel);
        infoPanel.add(islandLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(createdByLabel);
        infoPanel.add(teamLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(configPanel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(startButton);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(logPanel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(buttonsPanel);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    // 创建右侧第二列面板(右列2：只包含洪水弃牌区)
    private JPanel createRightColumn2() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(150, 600));

        // 创建洪水弃牌区
        JPanel floodPanel = new JPanel();
        floodPanel.setBackground(new Color(82, 143, 219)); // 蓝色背景
        JLabel floodLabel = new JLabel("Flood Discard:");
        floodLabel.setForeground(Color.WHITE);
        floodPanel.add(floodLabel);

        panel.add(floodPanel, BorderLayout.CENTER);

        return panel;
    }

    // 创建底部面板（下方两个玩家的棋子和卡牌）
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(1024, 50));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10)); // 增加间距为20

        // 添加2个玩家区域
        for (int player = 0; player < 2; player++) {
            // 创建玩家棋子（红色方块）
            JPanel playerPiece = new JPanel();
            playerPiece.setPreferredSize(new Dimension(30, 30));
            playerPiece.setBackground(Color.RED);
            panel.add(playerPiece);

            // 在每个玩家后面添加5个白色方块（卡牌）
            for (int card = 0; card < 5; card++) {
                JPanel cardPanel = new JPanel();
                cardPanel.setPreferredSize(new Dimension(30, 30));
                cardPanel.setBackground(Color.WHITE);
                cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.add(cardPanel);
            }

            // 如果不是最后一个玩家，添加一个间隔
            if (player < 1) {
                JPanel spacer = new JPanel();
                spacer.setPreferredSize(new Dimension(30, 30));
                spacer.setBackground(Color.LIGHT_GRAY);
                panel.add(spacer);
            }
        }

        return panel;
    }
}

