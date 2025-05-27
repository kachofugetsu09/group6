package com.group6.GUI;

import com.group6.controller.GameController;
import com.group6.entity.common.Card;
import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;
import com.group6.entity.common.GameState;
import com.group6.utils.ImageUtils;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.io.File;


public class GameFrame extends JFrame {
    private GameController gameController;
    // 存储每个格子的面板引用
    private JPanel[][] tilePanels;
    // 卡牌的面板引用
    private DefaultListModel<Card> cardListModel;
    // 游戏日志区域
    private JTextArea logArea;
    //卡牌显示区域
    private JPanel cardButtonPanel;
    private JPanel floodDiscardPanel;




    public int test;

    private ArrayList<Point> validTilePositions = Tile.getValidTilePositions();

    public GameFrame() {
        // 初始化游戏控制器
        gameController = new GameController();

        // 设置窗口基本属性
        setTitle("Forbidden Island");
        setSize(1280, 800);
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

        //每次调整窗口大小重载图片
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateGameBoard(); // 让 updateCardList 被调用，触发重绘
            }
        });

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

        // 动态行数
        cardButtonPanel = new JPanel();
        cardButtonPanel.setLayout(new GridLayout(0, 1, 3, 5)); // 动态行数
        cardButtonPanel.setBackground(new Color(245, 222, 179));

        // 滚动面板
        JScrollPane scrollPane = new JScrollPane(cardButtonPanel);
        scrollPane.setPreferredSize(new Dimension(130, 250));
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        cardsPanel.add(scrollPane);


        // 合并整体
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

    private JPanel createGamePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(40, 122, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setPreferredSize(new Dimension(500, 500));
        panel.setMaximumSize(new Dimension(600, 600));
        panel.setMinimumSize(new Dimension(300, 300));


        // 改用GridBagLayout，这样我们可以精确控制每个组件的位置
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 初始化格子面板数组
        tilePanels = new JPanel[6][6];

        // 1. 先创建所有海洋格子
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                gbc.gridx = x;
                gbc.gridy = y;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;

                JPanel emptyPanel = new JPanel(new BorderLayout());
                emptyPanel.setBackground(new Color(40,122,120));

                JLabel coordLabel = new JLabel(x + "," + y);
                coordLabel.setHorizontalAlignment(JLabel.CENTER);
                coordLabel.setForeground(Color.WHITE);
                emptyPanel.add(coordLabel, BorderLayout.SOUTH);

                panel.add(emptyPanel, gbc);
                tilePanels[y][x] = emptyPanel;
            }
        }

        // 2. 替换岛屿瓦片
        for (Tile tile : gameController.getGameBoard().getTiles()) {
            Point pos = tile.getPosition();
            final Tile finalTile = tile;

            int tileX = pos.x;  // 转换为数组索引
            int tileY = pos.y;

            if (tileX >= 0 && tileX < 6 && tileY >= 0 && tileY < 6) {
                gbc.gridx = tileX;
                gbc.gridy = tileY;

                JPanel tilePanel = new JPanel(new BorderLayout());
                tilePanel.setBackground(new Color(200, 200, 160));

                JLabel tileLabel = new JLabel();
                tileLabel.setHorizontalAlignment(JLabel.CENTER);
                tilePanel.add(tileLabel, BorderLayout.CENTER);

                JLabel nameLabel = new JLabel(pos.x + "," + pos.y + " " + tile.getName());
                nameLabel.setHorizontalAlignment(JLabel.CENTER);
                nameLabel.setBackground(new Color(210, 180, 140));
                nameLabel.setOpaque(true);
                tilePanel.add(nameLabel, BorderLayout.SOUTH);

                
                tilePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gameController.selectTile(finalTile);
                        updateGameBoard();
                        logArea.append("选择了瓦片: " + finalTile.getName() +
                                "，在" + finalTile.getPosition().x + "," +
                                finalTile.getPosition().y + "\n");
                    }
                });

                // 移除原来的面板
                panel.remove(tilePanels[tileY][tileX]);
                // 添加新面板到指定位置
                panel.add(tilePanel, gbc);
                tilePanels[tileY][tileX] = tilePanel;
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

        //使用特殊效果卡片
        JButton useCardButton = new JButton("Use Card");
        useCardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        useCardButton.setMaximumSize(new Dimension(150, 30));
        useCardButton.addActionListener(e -> {
            Player player = gameController.getCurrentPlayer();
            Tile tile = gameController.getSelectedTile();

            // 由玩家未来从手牌组件中设置
            Card selectedCard = player.getSelectedCard();

            if (selectedCard == null) {
                logArea.append("No card selected. Cannot use.\n");
                return;
            }

            boolean success = gameController.useCard(selectedCard, tile, Arrays.asList(player));
            if (success) {
                logArea.append("Card used successfully:" + selectedCard.getName() + "\n");
                updateGameBoard();
            } else {
                logArea.append("Card use failed.\n");
            }
        });

        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTurnButton.setMaximumSize(new Dimension(150, 30));
        // 给"End Turn"按钮添加点击事件
        endTurnButton.addActionListener(event -> {
            // 1. 处理游戏回合切换逻辑
            gameController.endTurn();

            // 2. 刷新界面（格子、玩家位置等）
            updateGameBoard();

            // 3. 在右侧日志中添加提示信息
            logArea.append("回合结束，切换玩家。\n");
        });


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
        buttonsPanel.add(useCardButton);
        buttonsPanel.add(Box.createVerticalStrut(5));


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

        // 洪水牌弃牌堆（真实展示）
        discardPilePanel.setLayout(new BorderLayout()); // 改为填充滚动面板

        floodDiscardPanel = new JPanel();
        floodDiscardPanel.setLayout(new BoxLayout(floodDiscardPanel, BoxLayout.Y_AXIS));
        floodDiscardPanel.setBackground(new Color(230, 240, 255));

        // 滚动展示
        JScrollPane scrollPane = new JScrollPane(floodDiscardPanel);
        scrollPane.setPreferredSize(new Dimension(130, 300));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        discardPilePanel.add(scrollPane, BorderLayout.CENTER);


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

        // 保存游戏按钮逻辑
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                gameController.saveGameToFile(file);
                JOptionPane.showMessageDialog(null, "✅ Game saved to: " + file.getAbsolutePath());
            }
        });

        // 读取游戏按钮逻辑
        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                boolean success = gameController.loadGameFromFile(file);
                if (success) {
                    updateGameBoard();
                    // 刷新卡牌
                    updateCardList();
                    // 加载存档后更新弃牌堆
                    updateFloodDiscardPile();
                    logArea.append("✅ Game loaded from: " + file.getName() + "\n");
                } else {
                    JOptionPane.showMessageDialog(null, "⚠️ Load failed. Please check the save file.");
                }
            }
        });

        // 退出按钮逻辑
        quitButton.addActionListener(e -> System.exit(0));

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
                JLabel nameLabel = new JLabel(x + "," + y);
                nameLabel.setHorizontalAlignment(JLabel.CENTER);
                nameLabel.setBackground(new Color(210, 180, 140));
                nameLabel.setOpaque(true);
                tilePanel.add(nameLabel, BorderLayout.SOUTH);

                // 设置默认颜色
                tilePanel.setBackground(new Color(200, 200, 160));

                // 检查是否有洪水
                Tile tile = gameController.findTileAt(x, y);
                if (tile != null && tile.isFlooded()) {
                    tilePanel.setBackground(new Color(100, 180, 255)); // 淡蓝色表示被淹没
                }
            }
            updateWaterLevel();
        }

        // 高亮选中的格子
        Tile selectedTile = gameController.getSelectedTile();
        if (selectedTile != null) {
            int x = selectedTile.getPosition().x;
            int y = selectedTile.getPosition().y;
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
                    int x = pos.x;
                    int y = pos.y;
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
        updateCardList();
        updateFloodDiscardPile();
    }

    private void updateCardList() {
        if (cardButtonPanel == null) return;
        cardButtonPanel.removeAll();

        java.util.List<Card> hand = gameController.getCurrentPlayer().getHand();
        Dimension panelSize = cardButtonPanel.getSize();

        int defaultWidth = 120;
        int defaultHeight = 60;

        int width = panelSize.width > 20 ? panelSize.width - 10 : defaultWidth;
        int heightPerCard = panelSize.height > 20
                ? panelSize.height / Math.max(hand.size(), 1)
                : defaultHeight;


        for (Card card : hand) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(width, heightPerCard));
            button.setEnabled(true);

            String filename = card.getName().replaceAll("[^a-zA-Z0-9]", "_") + ".png";
            String path = "/Cards/" + filename;

            ImageIcon icon = ImageUtils.loadCardImage(path, width, heightPerCard);
            if (icon != null) {
                button.setIcon(icon);
            } else {
                button.setText(card.getName());
            }

            button.addActionListener(e -> {
                gameController.getCurrentPlayer().setSelectedCard(card);
                logArea.append("Choose Card：" + card.getName() + "\n");
            });

            cardButtonPanel.add(button);
        }

        cardButtonPanel.revalidate();
        cardButtonPanel.repaint();
    }

    // 更新洪水牌弃牌堆面板
    public void updateFloodDiscardPile() {
        if (floodDiscardPanel == null) return;

        floodDiscardPanel.removeAll();

        List<Card> discardPile = gameController.getFloodDeck().getDiscardPile();
        int width = floodDiscardPanel.getWidth() > 20 ? floodDiscardPanel.getWidth() - 10 : 120;
        int heightPerCard = 50;

        for (int i = discardPile.size() - 1; i >= 0; i--) {
            Card card = discardPile.get(i);
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(width, heightPerCard));
            button.setEnabled(true);
            button.setFocusable(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setRolloverEnabled(false);


            String filename = card.getName().replaceAll("[^a-zA-Z0-9]", "_") + ".png";
            String path = "/FloodCards/" + filename;

            ImageIcon icon = ImageUtils.loadCardImage(path, width, heightPerCard);
            if (icon != null) {
                button.setIcon(icon);
            } else {
                button.setText(card.getName());
            }

            floodDiscardPanel.add(button);
        }

        floodDiscardPanel.revalidate();
        floodDiscardPanel.repaint();
    }



    // 创建玩家图标
    private ImageIcon createPlayerIcon(String color) {
        // 这里应该返回根据角色颜色加载的图标
        return null;
    }
    // 更新水位条（目前为占位实现）
    private void updateWaterLevel() {
        // TODO: 可在此更新右侧水位进度条
    }

}