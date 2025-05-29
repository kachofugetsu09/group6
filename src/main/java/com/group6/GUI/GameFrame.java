package com.group6.GUI;

import com.group6.controller.GameController;
import com.group6.entity.common.Card;
import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;
import com.group6.entity.common.GameState;
import com.group6.utils.ImageUtils;
import com.group6.utils.RoleImageManager;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    private JLabel cardCountLabel;




    public int test;

    private ArrayList<Point> validTilePositions = Tile.getValidTilePositions();

    public GameFrame(GameController gameController) {
        // 初始化游戏控制器
        this.gameController = gameController;

        // 设置窗口基本属性
        setTitle("Forbidden Island");
        setSize(1920, 1080);
        setMinimumSize(new Dimension(1920, 1080));
        setMaximumSize(new Dimension(1920, 1080));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建顶部面板
        JPanel topPanel = createTopPanel();

        // 创建中心面板
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createLeftPanel(), BorderLayout.WEST);
        // 地图区域：放入一个包裹层，居中展示
        JPanel mapWrapper = new JPanel(new GridBagLayout());
        mapWrapper.setPreferredSize(new Dimension(600, 600));
        mapWrapper.setBackground(new Color(40, 122, 120));
        mapWrapper.add(createGamePanel()); // 把地图放入中间
        centerPanel.add(mapWrapper, BorderLayout.CENTER);


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
        panel.setPreferredSize(new Dimension(1024, 40));
        panel.setBackground(new Color(70, 130, 180)); // 钢蓝色

        JLabel titleLabel = new JLabel("Forbidden Island Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        panel.add(titleLabel);

        return panel;
    }

    // 创建左侧面板
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(260, 700));
        panel.setBackground(new Color(222, 184, 135)); // 棕褐色
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Font sectionFont = new Font("Arial", Font.BOLD, 25);
        Font playerFont = new Font("Arial", Font.PLAIN, 25);

        // 添加玩家信息区域
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        TitledBorder playerBorder = BorderFactory.createTitledBorder("Players");
        playerBorder.setTitleFont(new Font("Arial", Font.BOLD, 25));
        playersPanel.setBorder(playerBorder);
        playersPanel.setBackground(new Color(245, 222, 179)); // 小麦色
        playersPanel.setMaximumSize(new Dimension(240, 260));
        playersPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 为每个玩家添加信息面板
        for (Player player : gameController.getGameBoard().getPlayers()) {
            JPanel playerInfo = new JPanel(new BorderLayout());
            playerInfo.setMaximumSize(new Dimension(220, 60));
            playerInfo.setBackground(Color.WHITE);
            playerInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel nameLabel = new JLabel(player.getRoletype().name(), JLabel.CENTER);
            nameLabel.setFont(playerFont);

            JLabel posLabel = new JLabel("(" + player.getCurrentPosition().getPosition().x + "," + player.getCurrentPosition().getPosition().y + ")", JLabel.CENTER);
            posLabel.setFont(playerFont);

            playerInfo.add(nameLabel, BorderLayout.CENTER);
            playerInfo.add(posLabel, BorderLayout.SOUTH);

            playersPanel.add(playerInfo);
            playersPanel.add(Box.createVerticalStrut(8));
        }

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        TitledBorder cardsBorder = BorderFactory.createTitledBorder("Treasure Cards");
        cardsBorder.setTitleFont(new Font("Arial", Font.BOLD, 25));
        cardsPanel.setBorder(cardsBorder);
        cardsPanel.setBackground(new Color(245, 222, 179));
        cardsPanel.setMaximumSize(null);
        cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 动态行数
        cardButtonPanel = new JPanel();
        cardButtonPanel.setLayout(new GridLayout(0, 1, 5, 10));
        cardButtonPanel.setBackground(new Color(245, 222, 179));

        // 滚动面板
        JScrollPane scrollPane = new JScrollPane(cardButtonPanel);
        scrollPane.setPreferredSize(new Dimension(220, 600));
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        cardsPanel.add(scrollPane);

        // 合并整体
        panel.add(Box.createVerticalStrut(10));
        panel.add(playersPanel);
        panel.add(Box.createVerticalStrut(15));
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

        panel.setPreferredSize(new Dimension(1000, 900));
        panel.setMaximumSize(new Dimension(1000, 900));
        panel.setMinimumSize(new Dimension(1000, 900));
        panel.setLayout(new GridLayout(6, 6, 4, 4));

        // 初始化格子面板数组
        tilePanels = new JPanel[6][6];

        // 创建一个辅助方法来判断位置是否应该是海洋
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                JPanel tilePanel;
                
                // 判断是否是角落区域（应该保持为海洋的位置）
                if (isOceanPosition(x, y)) {
                    // 创建海洋面板
                    tilePanel = createOceanPanel(x, y);
                } else {
                    // 创建可放置瓦片的面板
                    tilePanel = createEmptyTilePanel(x, y);
                }

                panel.add(tilePanel);
                tilePanels[y][x] = tilePanel;
            }
        }

        // 放置瓦片（只在非海洋区域）
        for (Tile tile : gameController.getGameBoard().getTiles()) {
            Point pos = tile.getPosition();
            final Tile finalTile = tile;

            int tileX = pos.x;
            int tileY = pos.y;

            if (!isOceanPosition(tileX, tileY)) {
                JPanel tilePanel = createTilePanel(tile);
                panel.remove(tilePanels[tileY][tileX]);
                panel.add(tilePanel, tileY * 6 + tileX);
                tilePanels[tileY][tileX] = tilePanel;
            }
        }

        return panel;
    }

    // 判断是否是应该保持为海洋的位置
    private boolean isOceanPosition(int x, int y) {
        // 左上角
        if ((x == 0 && y == 0) || (x == 0 && y == 1) || (x == 1 && y == 0)) return true;
        
        // 右上角
        if ((x == 4 && y == 0) || (x == 5 && y == 0) || (x == 5 && y == 1)) return true;
        
        // 左下角
        if ((x == 0 && y == 4) || (x == 0 && y == 5) || (x == 1 && y == 5)) return true;
        
        // 右下角
        if ((x == 4 && y == 5) || (x == 5 && y == 4) || (x == 5 && y == 5)) return true;
        
        return false;
    }

    // 创建海洋面板
    private JPanel createOceanPanel(int x, int y) {
        JPanel oceanPanel = new JPanel();
        oceanPanel.setLayout(new BorderLayout());
        oceanPanel.setBackground(new Color(40, 122, 120));
        oceanPanel.setPreferredSize(new Dimension(150, 150));
        
        // 创建分层面板
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(150, 150));
        oceanPanel.add(layeredPane, BorderLayout.CENTER);

        // 只添加坐标标签在最上层
        JLabel coordLabel = new JLabel(x + "," + y);
        coordLabel.setBounds(0, 0, 150, 30);
        coordLabel.setHorizontalAlignment(JLabel.CENTER);
        coordLabel.setForeground(Color.WHITE);
        coordLabel.setBackground(new Color(40, 122, 120, 200));
        coordLabel.setOpaque(true);
        layeredPane.add(coordLabel, JLayeredPane.DRAG_LAYER);

        return oceanPanel;
    }

    // 创建空的可放置瓦片的面板
    private JPanel createEmptyTilePanel(int x, int y) {
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setBackground(new Color(200, 200, 160));
        emptyPanel.setPreferredSize(new Dimension(150, 150));
        
        JLabel coordLabel = new JLabel(x + "," + y);
        coordLabel.setHorizontalAlignment(JLabel.CENTER);
        coordLabel.setBackground(new Color(210, 180, 140));
        coordLabel.setOpaque(true);
        emptyPanel.add(coordLabel, BorderLayout.NORTH);
        
        return emptyPanel;
    }

    // 创建瓦片面板
    private JPanel createTilePanel(Tile tile) {
        Point pos = tile.getPosition();
        JPanel tilePanel = new JPanel();
        tilePanel.setLayout(new BorderLayout());
        tilePanel.setBackground(new Color(200, 200, 160));
        tilePanel.setPreferredSize(new Dimension(150, 150));

        // 创建分层面板
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(150, 150));
        tilePanel.add(layeredPane, BorderLayout.CENTER);

        // 1. 最底层：瓦片图片（层次0）
        JLabel tileLabel = new JLabel();
        tileLabel.setBounds(0, 0, 150, 150);
        tileLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // 根据瓦片状态选择对应的图片路径
        String filename = tile.getName().replaceAll("[^a-zA-Z0-9]", "_") + ".png";
        String path;
        if (tile.isFlooded() && !tile.isSunk()) {
            // 如果瓦片被淹没但未沉没，使用SubmersedTiles目录下的图片
            path = "/SubmersedTiles/" + filename;
        } else {
            // 正常状态使用FloodCards目录下的图片
            path = "/FloodCards/" + filename;
        }
        
        ImageIcon icon = ImageUtils.loadCardImage(path, 150, 150);
        if (icon != null) {
            tileLabel.setIcon(icon);
        } else {
            // 如果加载失败，可以添加一些视觉提示
            System.out.println("无法加载图片: " + path);
            tileLabel.setText(tile.getName());
        }
        layeredPane.add(tileLabel, JLayeredPane.DEFAULT_LAYER); // 最底层

        // 2. 中间层：玩家图标（层次1）
        JLabel playerLabel = new JLabel();
        playerLabel.setBounds(45, 45, 60, 60); // 居中且稍小一些
        playerLabel.setHorizontalAlignment(JLabel.CENTER);
        layeredPane.add(playerLabel, JLayeredPane.PALETTE_LAYER); // 中间层

        // 3. 最上层：坐标标签（层次2）
        JLabel coordLabel = new JLabel(pos.x + "," + pos.y + " " + tile.getName());
        coordLabel.setBounds(0, 0, 150, 30); // 顶部30像素高度
        coordLabel.setHorizontalAlignment(JLabel.CENTER);
        coordLabel.setBackground(new Color(210, 180, 140, 200)); // 添加一点透明度
        coordLabel.setOpaque(true);
        layeredPane.add(coordLabel, JLayeredPane.DRAG_LAYER); // 最上层

        // 添加点击事件
        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameController.selectTile(tile);
                updateGameBoard();
                logArea.append("选择了瓦片: " + tile.getName() +
                        "，在" + tile.getPosition().x + "," +
                        tile.getPosition().y + "\n");
            }
        });

        return tilePanel;
    }


    private JPanel createRightColumn1() {
        // ========== 字体与尺寸（大屏适配） ==========
        Font titleFont = new Font("Arial", Font.BOLD, 32);
        Font subTitleFont = new Font("Arial", Font.BOLD, 26);
        Font textFont = new Font("Arial", Font.PLAIN, 20);
        Font buttonFont = new Font("Arial", Font.PLAIN, 20);
        Dimension buttonSize = new Dimension(240, 60);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(320, 1000));
        panel.setBackground(new Color(240, 240, 240));

        // ===== 顶部标题 =====
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 240, 240));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel infoLabel = new JLabel("Game Info", SwingConstants.CENTER);
        infoLabel.setFont(titleFont);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel gameLabel = new JLabel("FORBIDDEN", SwingConstants.CENTER);
        gameLabel.setFont(subTitleFont);
        gameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel islandLabel = new JLabel("ISLAND", SwingConstants.CENTER);
        islandLabel.setFont(subTitleFont);
        islandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(infoLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(gameLabel);
        titlePanel.add(islandLabel);

        // ===== 当前玩家区域 =====
        JPanel currentPlayerPanel = new JPanel();
        currentPlayerPanel.setLayout(new BoxLayout(currentPlayerPanel, BoxLayout.Y_AXIS));
        currentPlayerPanel.setBackground(new Color(240, 240, 240));
        TitledBorder playerBorder = BorderFactory.createTitledBorder("Current Player");
        playerBorder.setTitleFont(new Font("Arial", Font.BOLD, 25));
        currentPlayerPanel.setBorder(playerBorder);

        Player currentPlayer = gameController.getCurrentPlayer();
        JLabel roleLabel = new JLabel("Role: " + currentPlayer.getRoletype().name());
        roleLabel.setFont(textFont);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel actionsLabel = new JLabel("Actions Left: " + currentPlayer.getActions());
        actionsLabel.setFont(textFont);
        actionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        currentPlayerPanel.add(roleLabel);
        currentPlayerPanel.add(Box.createVerticalStrut(10));
        currentPlayerPanel.add(actionsLabel);

        // ===== 游戏日志区域 =====
        JPanel logPanel = new JPanel(new BorderLayout());
        TitledBorder logBorder = BorderFactory.createTitledBorder("Game Log");
        logBorder.setTitleFont(new Font("Arial", Font.BOLD, 25));
        logPanel.setBorder(logBorder);
        logPanel.setBackground(new Color(255, 255, 255));

        logArea = new JTextArea(10, 20);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JScrollPane logScroll = new JScrollPane(logArea);
        logPanel.add(logScroll, BorderLayout.CENTER);

        // ===== 按钮操作区域 =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 20));
        TitledBorder actionBorder = BorderFactory.createTitledBorder("Actions");
        actionBorder.setTitleFont(new Font("Arial", Font.BOLD, 25));
        buttonPanel.setBorder(actionBorder);
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton moveButton = new JButton("Move To");
        moveButton.setFont(buttonFont);
        moveButton.setPreferredSize(buttonSize);
        moveButton.addActionListener(e -> {
            if (gameController.moveCurrentPlayer()) {
                updateGameBoard();
                logArea.append("移动成功！\n");
            } else {
                logArea.append("无法移动到该位置！\n");
            }
        });

        JButton shoreButton = new JButton("Shore Up");
        shoreButton.setFont(buttonFont);
        shoreButton.setPreferredSize(buttonSize);

        JButton giveCardButton = new JButton("Give Card");
        giveCardButton.setFont(buttonFont);
        giveCardButton.setPreferredSize(buttonSize);

        JButton treasureButton = new JButton("Capture Treasure");
        treasureButton.setFont(buttonFont);
        treasureButton.setPreferredSize(buttonSize);

        JButton useCardButton = new JButton("Use Card");
        useCardButton.setFont(buttonFont);
        useCardButton.setPreferredSize(buttonSize);
        useCardButton.addActionListener(e -> {
            Player player = gameController.getCurrentPlayer();
            Tile tile = gameController.getSelectedTile();
            Card selectedCard = player.getSelectedCard();
            if (selectedCard == null) {
                logArea.append("No card selected. Cannot use.\n");
                return;
            }
            boolean success = gameController.useCard(selectedCard, tile, Arrays.asList(player));
            if (success) {
                logArea.append("Card used successfully: " + selectedCard.getName() + "\n");
                updateGameBoard();
            } else {
                logArea.append("Card use failed.\n");
            }
        });

        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setFont(buttonFont);
        endTurnButton.setPreferredSize(buttonSize);
        endTurnButton.addActionListener(e -> {
            gameController.endTurn();
            updateGameBoard();
            logArea.append("回合结束，切换玩家。\n");
        });

        // 添加按钮
        buttonPanel.add(moveButton);
        buttonPanel.add(shoreButton);
        buttonPanel.add(giveCardButton);
        buttonPanel.add(treasureButton);
        buttonPanel.add(useCardButton);
        buttonPanel.add(endTurnButton);

        // ===== 合并所有区域 =====
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentPanel.add(titlePanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(currentPlayerPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(logPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(buttonPanel);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }


    // 创建右侧第二列（洪水牌区）
    private JPanel createRightColumn2() {
        // 字体与大小（统一风格）
        Font sectionTitleFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 18);
        Dimension pilePanelSize = new Dimension(180, 120);
        Dimension floodCardSize = new Dimension(200, 80);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(260, 800));
        panel.setBackground(new Color(220, 235, 250));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== 标题 =====
        JLabel titleLabel = new JLabel("Flood Cards");
        titleLabel.setFont(sectionTitleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // ===== 抽牌堆区域 =====
        JPanel drawPilePanel = new JPanel();
        drawPilePanel.setPreferredSize(pilePanelSize);
        drawPilePanel.setBackground(new Color(200, 220, 255));
        drawPilePanel.setLayout(new BorderLayout());
        TitledBorder drawBorder = BorderFactory.createTitledBorder("Draw Pile");
        drawBorder.setTitleFont(new Font("Arial", Font.BOLD, 22));
        drawPilePanel.setBorder(drawBorder);

        JPanel drawCard = new JPanel();
        drawCard.setPreferredSize(new Dimension(40, 40));
        drawCard.setBackground(new Color(0, 102, 204));
        drawCard.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        cardCountLabel = new JLabel("0 cards");
        cardCountLabel.setForeground(Color.WHITE);
        cardCountLabel.setFont(labelFont);
        drawCard.add(cardCountLabel);
        drawPilePanel.add(drawCard, BorderLayout.CENTER);
        panel.add(drawPilePanel);
        panel.add(Box.createVerticalStrut(20));

        // ===== 弃牌堆区域 =====
        JPanel discardPanel = new JPanel();
        discardPanel.setLayout(new BorderLayout());
        discardPanel.setBackground(new Color(230, 240, 255));
        TitledBorder discardBorder = BorderFactory.createTitledBorder("Discard Pile");
        discardBorder.setTitleFont(new Font("Arial", Font.BOLD, 22));
        discardPanel.setBorder(discardBorder);
        discardPanel.setPreferredSize(new Dimension(240, 400));

        floodDiscardPanel = new JPanel();
        floodDiscardPanel.setLayout(new BoxLayout(floodDiscardPanel, BoxLayout.Y_AXIS));
        floodDiscardPanel.setBackground(new Color(230, 240, 255));

        JScrollPane scrollPane = new JScrollPane(floodDiscardPanel);
        scrollPane.setPreferredSize(new Dimension(220, 360));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        discardPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(discardPanel);
        panel.add(Box.createVerticalStrut(20));

        // ===== 水位指示器 =====
        JPanel waterLevelPanel = new JPanel();
        waterLevelPanel.setPreferredSize(new Dimension(240, 80));
        waterLevelPanel.setBackground(new Color(200, 230, 255));
        TitledBorder waterBorder = BorderFactory.createTitledBorder("Water Level");
        waterBorder.setTitleFont(new Font("Arial", Font.BOLD, 22));
        waterLevelPanel.setBorder(waterBorder);

        JProgressBar waterLevelBar = new JProgressBar(0, 10);
        waterLevelBar.setValue(1);
        waterLevelBar.setString("Level 1");
        waterLevelBar.setFont(labelFont);
        waterLevelBar.setStringPainted(true);
        waterLevelBar.setPreferredSize(new Dimension(200, 30));

        waterLevelPanel.add(waterLevelBar);
        panel.add(waterLevelPanel);

        return panel;
    }



    // 创建底部面板
    private JPanel createBottomPanel() {
        // 字体与按钮尺寸
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Dimension buttonSize = new Dimension(180, 50);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1920, 80));
        panel.setBackground(new Color(70, 130, 180)); // Steel blue
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));

        JButton saveButton = new JButton("Save Game");
        saveButton.setFont(buttonFont);
        saveButton.setPreferredSize(buttonSize);

        JButton loadButton = new JButton("Load Game");
        loadButton.setFont(buttonFont);
        loadButton.setPreferredSize(buttonSize);

        JButton quitButton = new JButton("Quit Game");
        quitButton.setFont(buttonFont);
        quitButton.setPreferredSize(buttonSize);

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
                    updateCardList();
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
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                if (isOceanPosition(x, y)) {
                    // 海洋格子保持原样
                    continue;
                }

                JPanel tilePanel = tilePanels[y][x];
                JLayeredPane layeredPane = (JLayeredPane) tilePanel.getComponent(0);
                
                // 更新瓦片状态
                Tile tile = gameController.findTileAt(x, y);
                if (tile != null) {
                    // 根据瓦片状态选择对应的图片路径
                    String filename = tile.getName().replaceAll("[^a-zA-Z0-9]", "_") + ".png";
                    String path;
                    if (tile.isFlooded() && !tile.isSunk()) {
                        path = "/SubmersedTiles/" + filename;
                    } else {
                        path = "/FloodCards/" + filename;
                    }
                    
                    ImageIcon icon = ImageUtils.loadCardImage(path, 150, 150);
                    if (icon != null) {
                        JLabel tileLabel = (JLabel) layeredPane.getComponent(0);
                        tileLabel.setIcon(icon);
                    }

                    // 设置背景颜色（可选，因为已经有图片区分了）
                    if (tile.isFlooded()) {
                        tilePanel.setBackground(new Color(100, 180, 255, 128)); // 半透明的蓝色
                    } else {
                        tilePanel.setBackground(new Color(200, 200, 160));
                    }

                    // 更新玩家图标
                    JLabel playerLabel = (JLabel) layeredPane.getComponent(1);
                    playerLabel.setIcon(null); // 清除原有图标
                    
                    // 检查是否有玩家在此位置
        for (Player player : gameController.getGameBoard().getPlayers()) {
                        if (player.getCurrentPosition() != null &&
                            player.getCurrentPosition().getPosition().equals(new Point(x, y))) {
                        ImageIcon playerIcon = createPlayerIcon(player.getColor());
                        if (playerIcon != null) {
                                // 调整图标大小
                                Image scaledImage = playerIcon.getImage()
                                    .getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                                playerLabel.setIcon(new ImageIcon(scaledImage));
                            }
                            break;
                        }
                    }
                }
            }
        }

        // 高亮选中的格子
        Tile selectedTile = gameController.getSelectedTile();
        if (selectedTile != null) {
            Point pos = selectedTile.getPosition();
            if (!isOceanPosition(pos.x, pos.y)) {
                tilePanels[pos.y][pos.x].setBackground(new Color(255, 255, 0));
            }
        }

        revalidate();
        repaint();
        updateCardList();
        updateFloodDiscardPile();
        updateWaterLevel();
    }

    private void updateCardList() {
        if (cardButtonPanel == null) return;
        cardButtonPanel.removeAll();

        java.util.List<Card> hand = gameController.getCurrentPlayer().getHand();
        Dimension panelSize = cardButtonPanel.getSize();

        int width = panelSize.width > 20 ? panelSize.width - 10 : 220;
        int heightPerCard = panelSize.height > 20 ? panelSize.height / Math.max(hand.size(), 1) : 80;

        for (Card card : hand) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(width, heightPerCard));
            button.setFont(new Font("Arial", Font.PLAIN, 14));
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

        if (cardCountLabel != null) {
            int total = gameController.getFloodDeck().getDeck().size() + gameController.getFloodDeck().getDiscardPile().size();
            int discard = gameController.getFloodDeck().getDiscardPile().size();
            int remaining = total - discard;
            cardCountLabel.setText(remaining + " cards");
        }

        floodDiscardPanel.removeAll();

        List<Card> discardPile = gameController.getFloodDeck().getDiscardPile();
        int width = 210;
        int heightPerCard = 210;

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
        for (Player player : gameController.getGameBoard().getPlayers()) {
            if (player.getColor().equals(color)) {
                ImageIcon icon = RoleImageManager.getRoleImage(player.getRoletype());
                if (icon != null) {
                    System.out.println("Successfully created icon for player: " + player.getRoletype());
                } else {
                    System.out.println("Failed to create icon for player: " + player.getRoletype());
                }
                return icon;
            }
        }
        return null;
    }

    // 更新水位条（目前为占位实现）
    private void updateWaterLevel() {
        // 获取当前水位（假设范围是1-10）
        int currentLevel = gameController.getWaterMeter().getLevel();
        
        // 构建水位计图片路径
        String waterMeterImagePath = "/WaterMeter/water_level_" + currentLevel + ".png";
        
        // 在右侧面板中找到水位计面板
        JPanel waterLevelPanel = null;
        Component[] components = getRootPane().getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JPanel) {
                        Component[] rightPanels = ((JPanel) subComp).getComponents();
                        for (Component rightPanel : rightPanels) {
                            if (rightPanel instanceof JPanel && 
                                ((JPanel) rightPanel).getBorder() != null && 
                                ((JPanel) rightPanel).getBorder().toString().contains("Water Level")) {
                                waterLevelPanel = (JPanel) rightPanel;
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        if (waterLevelPanel != null) {
            // 清除现有内容
            waterLevelPanel.removeAll();
            
            // 加载并显示水位计图片
            ImageIcon waterMeterIcon = ImageUtils.loadCardImage(waterMeterImagePath, 200, 30);
            if (waterMeterIcon != null) {
                JLabel waterMeterLabel = new JLabel(waterMeterIcon);
                waterLevelPanel.add(waterMeterLabel);
            } else {
                // 如果图片加载失败，使用进度条作为备选显示
                JProgressBar waterLevelBar = new JProgressBar(0, 10);
                waterLevelBar.setValue(currentLevel);
                waterLevelBar.setString("Level " + currentLevel);
                waterLevelBar.setStringPainted(true);
                waterLevelBar.setFont(new Font("Arial", Font.PLAIN, 18));
                waterLevelBar.setPreferredSize(new Dimension(200, 30));
                
                // 根据水位设置颜色
                if (currentLevel <= 2) {
                    waterLevelBar.setForeground(new Color(0, 255, 0)); // 绿色
                } else if (currentLevel <= 5) {
                    waterLevelBar.setForeground(new Color(255, 255, 0)); // 黄色
                } else if (currentLevel <= 7) {
                    waterLevelBar.setForeground(new Color(255, 165, 0)); // 橙色
                } else {
                    waterLevelBar.setForeground(new Color(255, 0, 0)); // 红色
                }
                
                waterLevelPanel.add(waterLevelBar);
            }
            
            // 刷新面板
            waterLevelPanel.revalidate();
            waterLevelPanel.repaint();
        }
    }

}