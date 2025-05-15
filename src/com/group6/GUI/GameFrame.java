package com.group6.GUI;

import com.group6.controller.GameController;
import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameFrame extends JFrame {
    private GameController gameController;
    // 存储每个格子的面板引用
    private JPanel[][] tilePanels;
    // 游戏日志区域
    private JTextArea logArea;

    public GameFrame() {
        // 初始化游戏控制器
        gameController = new GameController();

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

        // 初始化游戏界面
        updateGameBoard();

        // 居中显示
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }


    public GameController getGameController() {
        return this.gameController;
    }

    // 创建顶部面板
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1024, 30));
        panel.setBackground(new Color(70, 130, 180)); // 钢蓝色

        JLabel titleLabel = new JLabel("Forbidden Island Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel);

        return panel;
    }

    // 创建左侧面板
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(150, 600));
        panel.setBackground(new Color(222, 184, 135)); // 棕褐色
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 添加玩家信息区域
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        playersPanel.setBackground(new Color(245, 222, 179)); // 小麦色
        playersPanel.setMaximumSize(new Dimension(140, 240));
        playersPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 为每个玩家添加信息面板
        for (Player player : gameController.getGameBoard().getPlayers()) {
            JPanel playerInfo = createPlayerInfoPanel(player);
            playersPanel.add(playerInfo);
            playersPanel.add(Box.createVerticalStrut(5));
        }

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBorder(BorderFactory.createTitledBorder("Treasure Cards"));
        cardsPanel.setBackground(new Color(245, 222, 179)); // 小麦色
        cardsPanel.setMaximumSize(new Dimension(140, 280));
        cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 添加牌区占位符
        for (int i = 0; i < 5; i++) {
            JPanel cardSlot = new JPanel();
            cardSlot.setPreferredSize(new Dimension(120, 40));
            cardSlot.setMaximumSize(new Dimension(120, 40));
            cardSlot.setBackground(new Color(255, 250, 205)); // 淡黄色
            cardSlot.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cardSlot.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardSlot.add(new JLabel("Card " + (i + 1)));
            cardsPanel.add(cardSlot);
            cardsPanel.add(Box.createVerticalStrut(5));
        }

        panel.add(Box.createVerticalStrut(10));
        panel.add(playersPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cardsPanel);

        return panel;
    }

    // 创建玩家信息面板
    private JPanel createPlayerInfoPanel(Player player) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(130, 50));

        // 根据角色颜色设置背景
        Color bgColor;
        switch (player.getColor()) {
            case "RED":
                bgColor = new Color(255, 200, 200);
                break;
            case "BLUE":
                bgColor = new Color(200, 200, 255);
                break;
            case "GREEN":
                bgColor = new Color(200, 255, 200);
                break;
            case "BLACK":
                bgColor = new Color(220, 220, 220);
                break;
            default:
                bgColor = Color.WHITE;
                break;
        }
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // 添加角色名称
        JLabel nameLabel = new JLabel(player.getRoletype().name());
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(nameLabel, BorderLayout.CENTER);

        // 添加位置信息
        if (player.getCurrentPosition() != null) {
            Point pos = player.getCurrentPosition().getPosition();
            JLabel posLabel = new JLabel("(" + pos.x + "," + pos.y + ")");
            posLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(posLabel, BorderLayout.SOUTH);
        }

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

        // 初始化格子面板数组
        tilePanels = new JPanel[6][6];

        // 添加36个岛屿格子
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                final int tileX = x + 1;  // 转换为1-6的坐标
                final int tileY = y + 1;

                JPanel tilePanel = new JPanel();
                tilePanel.setLayout(new BorderLayout());
                tilePanel.setBackground(new Color(200, 200, 160)); // 岛屿默认颜色

                // 为每个格子添加一个标签
                JLabel tileLabel = new JLabel();
                tileLabel.setHorizontalAlignment(JLabel.CENTER);
                tilePanel.add(tileLabel, BorderLayout.CENTER);

                // 添加名称标签在底部
                JLabel nameLabel = new JLabel(tileX + "," + tileY);
                nameLabel.setHorizontalAlignment(JLabel.CENTER);
                nameLabel.setBackground(new Color(210, 180, 140)); // 浅棕色
                nameLabel.setOpaque(true);
                tilePanel.add(nameLabel, BorderLayout.SOUTH);

                // 添加点击事件
                tilePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Tile clickedTile = gameController.findTileAt(tileX, tileY);
                        if (clickedTile != null) {
                            gameController.selectTile(clickedTile);
                            updateGameBoard();
                            logArea.append("选择了格子: " + tileX + "," + tileY + "\n");
                        }
                    }
                });

                panel.add(tilePanel);
                tilePanels[y][x] = tilePanel;  // 存储面板引用
            }
        }

        return panel;
    }

    // 创建右侧信息面板
    private JPanel createRightColumn1() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 600));

        // 创建信息区
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(230, 230, 230));

        // 添加标题
        JLabel titleLabel = new JLabel("Game Info");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // 添加FORBIDDEN ISLAND标题
        JLabel gameLabel = new JLabel("FORBIDDEN");
        gameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel islandLabel = new JLabel("ISLAND");
        islandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        islandLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // 添加日志区域
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logPanel.setBorder(BorderFactory.createTitledBorder("Game Log"));
        logPanel.setMaximumSize(new Dimension(180, 150));

        logArea = new JTextArea(8, 15);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JScrollPane scrollPane = new JScrollPane(logArea);

        logPanel.add(scrollPane, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonsPanel.setMaximumSize(new Dimension(180, 200));
        buttonsPanel.setBackground(new Color(230, 230, 230));

        // 添加移动按钮
        JButton moveButton = new JButton("Move To");
        moveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moveButton.setMaximumSize(new Dimension(150, 30));
        moveButton.addActionListener(e -> {
            if (gameController.moveCurrentPlayer()) {
                updateGameBoard();
                logArea.append("移动成功！\n");
            } else {
                logArea.append("无法移动到该位置！\n");
            }
        });

        // 添加其他按钮
        JButton shoreUpButton = new JButton("Shore Up");
        shoreUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shoreUpButton.setMaximumSize(new Dimension(150, 30));

        JButton giveCardButton = new JButton("Give Card");
        giveCardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        giveCardButton.setMaximumSize(new Dimension(150, 30));

        JButton treasureButton = new JButton("Capture Treasure");
        treasureButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        treasureButton.setMaximumSize(new Dimension(150, 30));

        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTurnButton.setMaximumSize(new Dimension(150, 30));

        // 添加按钮到面板
        buttonsPanel.add(moveButton);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(shoreUpButton);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(giveCardButton);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(treasureButton);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(endTurnButton);

        // 添加当前玩家信息面板
        JPanel currentPlayerPanel = new JPanel();
        currentPlayerPanel.setLayout(new BoxLayout(currentPlayerPanel, BoxLayout.Y_AXIS));
        currentPlayerPanel.setBorder(BorderFactory.createTitledBorder("Current Player"));
        currentPlayerPanel.setMaximumSize(new Dimension(180, 100));
        currentPlayerPanel.setBackground(new Color(230, 230, 230));

        // 显示当前玩家信息
        Player currentPlayer = gameController.getCurrentPlayer();
        JLabel playerRoleLabel = new JLabel("Role: " + currentPlayer.getRoletype().name());
        playerRoleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel playerActionLabel = new JLabel("Actions Left: " + currentPlayer.getActions());
        playerActionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        currentPlayerPanel.add(playerRoleLabel);
        currentPlayerPanel.add(Box.createVerticalStrut(5));
        currentPlayerPanel.add(playerActionLabel);

        // 添加所有组件到信息面板
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(gameLabel);
        infoPanel.add(islandLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(currentPlayerPanel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(logPanel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(buttonsPanel);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    // 创建右侧第二列（洪水牌区）
    private JPanel createRightColumn2() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(150, 600));
        panel.setBackground(new Color(200, 230, 250)); // 淡蓝色
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 添加洪水牌标题
        JLabel titleLabel = new JLabel("Flood Cards");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // 洪水牌抽牌堆
        JPanel drawPilePanel = new JPanel();
        drawPilePanel.setBorder(BorderFactory.createTitledBorder("Draw Pile"));
        drawPilePanel.setMaximumSize(new Dimension(140, 100));
        drawPilePanel.setBackground(new Color(230, 240, 255));
        drawPilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(80, 60));
        cardPanel.setBackground(new Color(0, 102, 204));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel cardCountLabel = new JLabel("24 cards");
        cardCountLabel.setForeground(Color.WHITE);
        cardPanel.add(cardCountLabel);
        drawPilePanel.add(cardPanel);

        // 洪水牌弃牌堆
        JPanel discardPilePanel = new JPanel();
        discardPilePanel.setBorder(BorderFactory.createTitledBorder("Discard Pile"));
        discardPilePanel.setMaximumSize(new Dimension(140, 400));
        discardPilePanel.setBackground(new Color(230, 240, 255));
        discardPilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        discardPilePanel.setLayout(new BoxLayout(discardPilePanel, BoxLayout.Y_AXIS));

        // 添加几个示例弃牌
        for (int i = 0; i < 3; i++) {
            JPanel discardCard = new JPanel();
            discardCard.setMaximumSize(new Dimension(120, 40));
            discardCard.setBackground(new Color(173, 216, 230));
            discardCard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            discardCard.setAlignmentX(Component.CENTER_ALIGNMENT);
            discardCard.add(new JLabel("Flood " + (i + 1)));
            discardPilePanel.add(discardCard);
            discardPilePanel.add(Box.createVerticalStrut(5));
        }

        // 水位指示器
        JPanel waterLevelPanel = new JPanel();
        waterLevelPanel.setBorder(BorderFactory.createTitledBorder("Water Level"));
        waterLevelPanel.setMaximumSize(new Dimension(140, 60));
        waterLevelPanel.setBackground(new Color(230, 240, 255));
        waterLevelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar waterLevelBar = new JProgressBar(0, 10);
        waterLevelBar.setValue(1);
        waterLevelBar.setStringPainted(true);
        waterLevelBar.setString("Level 1");
        waterLevelBar.setPreferredSize(new Dimension(120, 20));
        waterLevelPanel.add(waterLevelBar);

        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(drawPilePanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(discardPilePanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(waterLevelPanel);

        return panel;
    }

    // 创建底部面板
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1024, 50));
        panel.setBackground(new Color(70, 130, 180)); // 钢蓝色

        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");
        JButton quitButton = new JButton("Quit Game");

        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(quitButton);

        return panel;
    }

    // 更新游戏界面
    private void updateGameBoard() {
        // 清空所有格子的玩家标记
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                JPanel tilePanel = tilePanels[y][x];
                tilePanel.removeAll();

                // 重新添加标签
                JLabel tileLabel = new JLabel();
                tileLabel.setHorizontalAlignment(JLabel.CENTER);
                tilePanel.add(tileLabel, BorderLayout.CENTER);

                // 添加名称标签
                JLabel nameLabel = new JLabel((x + 1) + "," + (y + 1));
                nameLabel.setHorizontalAlignment(JLabel.CENTER);
                nameLabel.setBackground(new Color(210, 180, 140));
                nameLabel.setOpaque(true);
                tilePanel.add(nameLabel, BorderLayout.SOUTH);

                // 设置默认颜色
                tilePanel.setBackground(new Color(200, 200, 160));

                // 检查是否有洪水
                Tile tile = gameController.findTileAt(x + 1, y + 1);
                if (tile != null && tile.isFlooded()) {
                    tilePanel.setBackground(new Color(100, 180, 255)); // 淡蓝色表示被淹没
                }
            }
        }

        // 高亮选中的格子
        Tile selectedTile = gameController.getSelectedTile();
        if (selectedTile != null) {
            int x = selectedTile.getPosition().x - 1;
            int y = selectedTile.getPosition().y - 1;
            if (x >= 0 && x < 6 && y >= 0 && y < 6) {
                tilePanels[y][x].setBackground(new Color(255, 255, 0)); // 黄色高亮
            }
        }

        // 显示玩家位置
        for (Player player : gameController.getGameBoard().getPlayers()) {
            player.setGameController(gameController);
            if (player.getCurrentPosition() != null) {
                Point pos = player.getCurrentPosition().getPosition();
                if (pos != null) {
                    int x = pos.x - 1;
                    int y = pos.y - 1;
                    if (x >= 0 && x < 6 && y >= 0 && y < 6) {
                        JPanel tilePanel = tilePanels[y][x];
                        JLabel playerLabel = (JLabel) tilePanel.getComponent(0);

                        // 设置玩家图标
                        ImageIcon playerIcon = createPlayerIcon(player.getColor());
                        if (playerIcon != null) {
                            playerLabel.setIcon(playerIcon);
                        } else {
                            // 如果没有图标，则使用文本表示
                            switch (player.getColor()) {
                                case "RED":
                                    playerLabel.setText("E"); // Engineer
                                    playerLabel.setForeground(Color.RED);
                                    break;
                                case "BLUE":
                                    playerLabel.setText("P"); // Pilot
                                    playerLabel.setForeground(Color.BLUE);
                                    break;
                                case "GREEN":
                                    playerLabel.setText("X"); // Explorer
                                    playerLabel.setForeground(Color.GREEN);
                                    break;
                                case "BLACK":
                                    playerLabel.setText("D"); // Diver
                                    playerLabel.setForeground(Color.BLACK);
                                    break;
                            }
                            playerLabel.setFont(new Font("Arial", Font.BOLD, 20));
                        }
                    }
                }
            }
        }

        // 刷新UI
        revalidate();
        repaint();
    }

    // 创建玩家图标
    private ImageIcon createPlayerIcon(String color) {
        // 这里应该返回根据角色颜色加载的图标
        return null;
    }
    }