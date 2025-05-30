package com.group6.GUI;

import com.group6.controller.GameController;
import com.group6.entity.common.Card;
import com.group6.entity.common.Tile;
import com.group6.entity.common.CardType;
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
import java.net.URL;
import javax.imageio.ImageIO;





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
        setSize(1680, 960);  // 设置初始大小
        setResizable(true);  // 允许调整大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建顶部面板
        JPanel topPanel = createTopPanel();

        // 创建中心面板，使用GridBagLayout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        
        // 创建并添加左侧面板
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.weightx = 0.1;
        leftGbc.weighty = 1.0;
        leftGbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(createLeftPanel(), leftGbc);

        // 创建地图包装面板
        JPanel mapWrapper = new JPanel(new GridBagLayout());
        mapWrapper.setBackground(new Color(40, 122, 120));

        // 添加地图面板到mapWrapper
        GridBagConstraints mapGbc = new GridBagConstraints();
        mapGbc.gridx = 0;
        mapGbc.gridy = 0;
        mapGbc.weightx = 1.1;
        mapGbc.weighty = 1.1;
        mapGbc.fill = GridBagConstraints.BOTH;
        mapWrapper.add(createGamePanel(), mapGbc);

        // 添加mapWrapper到centerPanel
        GridBagConstraints centerMapGbc = new GridBagConstraints();
        centerMapGbc.gridx = 1;
        centerMapGbc.gridy = 0;
        centerMapGbc.weightx = 0.7;
        centerMapGbc.weighty = 1.0;
        centerMapGbc.fill = GridBagConstraints.BOTH;
        centerMapGbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(mapWrapper, centerMapGbc);

        // 创建右侧面板
        JPanel rightSectionPanel = new JPanel(new BorderLayout());
        rightSectionPanel.add(createRightColumn1(), BorderLayout.EAST);
        rightSectionPanel.add(createRightColumn2(), BorderLayout.CENTER);

        // 添加右侧面板到centerPanel
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.gridx = 2;
        rightGbc.gridy = 0;
        rightGbc.weightx = 0.1;
        rightGbc.weighty = 1.0;
        rightGbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(rightSectionPanel, rightGbc);

        // 创建底部面板
        JPanel bottomPanel = createBottomPanel();

        // 将面板添加到主面板
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 添加主面板到窗口
        add(mainPanel);

        // Defer initial sizing and board update until after the frame is visible and laid out
        SwingUtilities.invokeLater(() -> {
            updateGameBoard(); // This will call updateTileSize at its end
            // setLocationRelativeTo(null); // Already called if setVisible is true
            // setVisible(true); // Already called typically before this point by user
        });
        
        setLocationRelativeTo(null);
        setVisible(true); // Make frame visible so component hierarchy is realized
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
        panel.setPreferredSize(new Dimension(180, 700));
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
        scrollPane.setPreferredSize(new Dimension(180, 600));
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
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(40, 122, 120)); // Background color for the game panel itself
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        tilePanels = new JPanel[6][6]; // Initialize the array
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2); // Spacing between tiles

        // Create and add all 36 panels (ocean or empty land placeholders)
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                gbc.gridx = x;
                gbc.gridy = y;
                gbc.weightx = 1.0; // Each cell gets equal weight
                gbc.weighty = 1.0;
                gbc.fill = GridBagConstraints.BOTH; // Each cell fills

                JPanel cellPanel;
                if (isOceanPosition(x, y)) {
                    cellPanel = createOceanPanel(x, y);
                } else {
                    // For land cells that are not ocean corners,
                    // initially they are placeholders until specific tiles are placed.
                    // Or, directly create the tile panel if the tile data is available here.
                    // Let's assume they are placeholders first or handled by updateGameBoard.
                    // For simplicity, create empty and let updateGameBoard populate.
                    cellPanel = createEmptyTilePanel(x, y); // This should be a basic panel
                }
                panel.add(cellPanel, gbc);
                tilePanels[y][x] = cellPanel; // Store reference to this cell's panel
            }
        }

        // The actual Tile objects with names and images should be placed
        // by updateGameBoard by finding the correct tilePanels[y][x]
        // and then updating its content (e.g., adding/replacing JLayeredPane).
        // The loop below that was re-adding tiles might be redundant if updateGameBoard
        // correctly populates the tilePanels created above.
        // For now, this createGamePanel will return a 6x6 grid of placeholders/ocean.

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
        oceanPanel.setPreferredSize(new Dimension(159, 159));
        
        // 创建分层面板
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(159, 159));
        oceanPanel.add(layeredPane, BorderLayout.CENTER);

        // 只添加坐标标签在最上层
        JLabel coordLabel = new JLabel(x + "," + y);
        coordLabel.setBounds(0, 0, 159, 30);
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
        // 使用相同的宽高值创建正方形
        int size = 159; // 使用与图片相同的尺寸
        emptyPanel.setPreferredSize(new Dimension(size, size));
        
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
        
        // 获取当前面板大小（如果还未设置，使用默认值159）
        int tileSize = Math.max(tilePanel.getWidth(), 159);
        
        // 创建分层面板
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(tileSize, tileSize));
        tilePanel.add(layeredPane, BorderLayout.CENTER);

        // 1. 瓦片图片（最底层）
                JLabel tileLabel = new JLabel();
        tileLabel.setBounds(0, 0, tileSize, tileSize);
                tileLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // 根据瓦片状态选择图片
        String filename = tile.getName().replaceAll("[^a-zA-Z0-9]", "_") + ".png";
        String path = tile.isFlooded() && !tile.isSunk() ? 
                     "/SubmersedTiles/" + filename : 
                     "/FloodCards/" + filename;
        
        // 使用ImageUtils加载并缩放图片，保持比例
        ImageIcon icon = ImageUtils.loadAndScaleImage(path, tileSize, tileSize);
        if (icon != null) {
            tileLabel.setIcon(icon);
        }
        layeredPane.add(tileLabel, JLayeredPane.DEFAULT_LAYER);

        // 2. 玩家图标（中间层）
        JLabel playerLabel = new JLabel();
        int iconSize = tileSize / 2;
        int offset = (tileSize - iconSize) / 2;
        playerLabel.setBounds(offset, offset, iconSize, iconSize);
        playerLabel.setHorizontalAlignment(JLabel.CENTER);
        layeredPane.add(playerLabel, JLayeredPane.PALETTE_LAYER);

        // 3. 坐标标签（顶层）
        JLabel coordLabel = new JLabel(pos.x + "," + pos.y + " " + tile.getName());
        coordLabel.setBounds(0, 0, tileSize, tileSize / 5);
        coordLabel.setHorizontalAlignment(JLabel.CENTER);
        coordLabel.setBackground(new Color(210, 180, 140, 200));
        coordLabel.setOpaque(true);
        layeredPane.add(coordLabel, JLayeredPane.DRAG_LAYER);

           // 添加点击事件
        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameController.selectTile(tile);
                updateGameBoard();

                // 显示普通瓦片点击日志
                logArea.append("选择了瓦片: " + tile.getName() + "，在" + tile.getPosition().x + "," + tile.getPosition().y + "\n");
            }
        });


        return tilePanel;
        }


    private JPanel createRightColumn1() {
        // ========== 字体与尺寸（大屏适配） ==========
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font subTitleFont = new Font("Arial", Font.BOLD, 20);
        Font textFont = new Font("Arial", Font.PLAIN, 20);
        Font buttonFont = new Font("Arial", Font.PLAIN, 20);
        Dimension buttonSize = new Dimension(200, 45);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(260, 1000));
        panel.setBackground(new Color(240, 240, 240));

        // ===== 顶部标题 =====
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 240, 240));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

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

        logArea = new JTextArea(40, 20);
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
            Card selectedCard = player.getSelectedCard();
            if (selectedCard == null) {
                logArea.append("No card selected. Cannot use.\n");
                return;
            }

            if (selectedCard.getType() == CardType.HELICOPTER) {
                // 弹窗选择玩家，等待点击瓦片
                showHelicopterUseDialog();
            } else {
                Tile tile = gameController.getSelectedTile();
                boolean success = gameController.useCard(selectedCard, tile, Arrays.asList(player));
                if (success) {
                    logArea.append("Card used successfully: " + selectedCard.getName() + "\n");
                    updateGameBoard();
                } else {
                    logArea.append("Card use failed.\n");
                }
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
        Dimension floodCardSize = new Dimension(160, 80);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(180, 800));
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
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));

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
                JPanel cellPanel = tilePanels[y][x];
                if (cellPanel == null) continue;

                cellPanel.removeAll(); // Clear previous content
                cellPanel.setLayout(new BorderLayout()); // Ensure a layout manager for adding new content

                if (isOceanPosition(x, y)) {
                    JPanel oceanContentPanel = createOceanPanel(x, y); // createOceanPanel should return a JPanel containing the JLayeredPane
                    cellPanel.add(oceanContentPanel, BorderLayout.CENTER);
                } else {
                    Tile tile = gameController.findTileAt(x, y);
                    if (tile != null && !tile.isSunk()) {
                        JPanel tileSpecificContentPanel = createTilePanel(tile); // createTilePanel returns a JPanel containing the JLayeredPane
                        cellPanel.add(tileSpecificContentPanel, BorderLayout.CENTER);
                        // Highlight selected tile
                        if (gameController.getSelectedTile() == tile) {
                            // Instead of setting cellPanel background, consider adding a border or overlay in JLayeredPane
                            cellPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                        } else {
                            cellPanel.setBorder(null); // Or a default border
                        }
                    } else { // Sunk or no tile here
                        // For sunk tiles, we still want a JLayeredPane for consistent structure if updateTileComponents expects it
                        JLayeredPane sunkLayeredPane = new JLayeredPane();
                        sunkLayeredPane.setPreferredSize(new Dimension(159, 159)); // Default size, will be resized
                        
                        JLabel sunkLabel = new JLabel("SUNK");
                        sunkLabel.setForeground(Color.RED);
                        sunkLabel.setHorizontalAlignment(JLabel.CENTER);
                        sunkLabel.setBounds(0, 0, 159, 159); // Occupy whole area
                        sunkLayeredPane.add(sunkLabel, JLayeredPane.PALETTE_LAYER); // Add label to layered pane
                        
                        JPanel sunkWrapperPanel = new JPanel(new BorderLayout()); // Wrapper to hold the layered pane
                        sunkWrapperPanel.add(sunkLayeredPane, BorderLayout.CENTER);
                        sunkWrapperPanel.setBackground(new Color(50,50,50)); // Dark background for sunk

                        cellPanel.add(sunkWrapperPanel, BorderLayout.CENTER);
                        cellPanel.setBorder(null); // Ensure no selection border for sunk tiles
                    }
                }
                cellPanel.revalidate();
                // cellPanel.repaint(); // Repaint will be handled by main repaint later
            }
        }

        // Player icon updates, etc., might need to iterate tilePanels again and add to the JLayeredPane
        // This was simplified earlier, ensure player icons are handled correctly within createTilePanel or here.
        // For example, after the main loop:
        for (Player player : gameController.getGameBoard().getPlayers()) {
            if (player.getCurrentPosition() != null) {
                Point pos = player.getCurrentPosition().getPosition();
                if (pos.x >= 0 && pos.x < 6 && pos.y >= 0 && pos.y < 6) {
                    JPanel targetCell = tilePanels[pos.y][pos.x];
                    // Assuming the first component of targetCell's content is the JLayeredPane's wrapper
                    if (targetCell.getComponentCount() > 0 && targetCell.getComponent(0) instanceof JPanel) {
                        JPanel contentWrapper = (JPanel) targetCell.getComponent(0);
                        if (contentWrapper.getComponentCount() > 0 && contentWrapper.getComponent(0) instanceof JLayeredPane) {
                            JLayeredPane layeredPane = (JLayeredPane) contentWrapper.getComponent(0);
                            
                            // Remove old player icon for this player if any
                            // Add new player icon
                            JLabel playerIconLabel = new JLabel(RoleImageManager.getRoleImage(player.getRoletype())); // Assuming this returns ImageIcon
                            // Calculate bounds for player icon, e.g., centered
                            int pSize = layeredPane.getWidth() / 3; // Example size
                            playerIconLabel.setBounds((layeredPane.getWidth() - pSize)/2, (layeredPane.getHeight() - pSize)/2, pSize, pSize);
                            playerIconLabel.setHorizontalAlignment(JLabel.CENTER);
                            layeredPane.add(playerIconLabel, JLayeredPane.MODAL_LAYER); // Higher layer for player
                        }
                    }
                }
            }
        }


        revalidate();
        repaint();
        updateCardList();
        updateFloodDiscardPile();
        updateWaterLevel();
        updateTileSize(); // Call this last
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

    // 弹出提示让当前玩家选择要弃掉的卡牌
    public void promptDiscard(Player player) {
        List<Card> hand = player.getHand();
        if (hand.isEmpty()) return;

        // 获取所有卡牌名称
        String[] cardNames = hand.stream().map(Card::getName).toArray(String[]::new);

        // 弹出选择框
        String selectedCardName = (String) JOptionPane.showInputDialog(
                this,
                "You have exceeded your hand limit. Please choose one card to discard:",
                "Discard Prompt",
                JOptionPane.PLAIN_MESSAGE,
                null,
                cardNames,
                cardNames[0]
        );

        if (selectedCardName == null) {
            // 禁止取消操作，必须选择一张卡牌
            JOptionPane.showMessageDialog(
                    this,
                    "You must discard one card to continue.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            promptDiscard(player); // 重新弹出
            return;
        }

        // 找到所选卡牌
        Card toDiscard = null;
        for (Card card : hand) {
            if (card.getName().equals(selectedCardName)) {
                toDiscard = card;
                break;
            }
        }

        if (toDiscard != null) {
            player.discardCard(toDiscard);
            updateCardList();
            logArea.append("You discarded: " + toDiscard.getName() + "\n");

            // 如果仍然超限，继续弃牌
            if (player.needsToDiscard()) {
                promptDiscard(player);
            }
        }
    }

    public void showHelicopterUseDialog() {
        List<Player> allPlayers = gameController.getGameBoard().getPlayers();

        JCheckBox[] playerBoxes = new JCheckBox[allPlayers.size()];
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Select players to move:"));
        for (int i = 0; i < allPlayers.size(); i++) {
            Player p = allPlayers.get(i);
            playerBoxes[i] = new JCheckBox(p.getRoletype().name() + " (" + p.getColor() + ")");
            panel.add(playerBoxes[i]);
        }

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Enter target tile name:"));
        JTextField tileField = new JTextField();
        panel.add(tileField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Helicopter Move", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        List<Player> selectedPlayers = new ArrayList<>();
        for (int i = 0; i < allPlayers.size(); i++) {
            if (playerBoxes[i].isSelected()) {
                selectedPlayers.add(allPlayers.get(i));
            }
        }

        if (selectedPlayers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one player.");
            return;
        }

        String targetTileName = tileField.getText().trim();
        if (targetTileName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a target tile name.");
            return;
        }

        Tile targetTile = gameController.findTileByName(targetTileName);
        if (targetTile == null) {
            JOptionPane.showMessageDialog(this, "Tile not found: " + targetTileName);
            return;
        }

        Card selectedCard = gameController.getCurrentPlayer().getSelectedCard();
        if (selectedCard == null || selectedCard.getType() != CardType.HELICOPTER) {
            JOptionPane.showMessageDialog(this, "Selected card is not a Helicopter.");
            return;
        }

        // ✅ 立即执行效果
        boolean success = gameController.useCard(selectedCard, targetTile, selectedPlayers);
        if (success) {
            logArea.append("✅ Helicopter used. Players moved to " + targetTile.getName() + "\n");
            updateGameBoard();
        } else {
            logArea.append("❌ Failed to use Helicopter card.\n");
        }
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
        int width = 160;
        int heightPerCard = 160;

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

    // 添加新方法来更新瓦片大小
    private void updateTileSize() {
        if (tilePanels == null) return;
        
        // 获取游戏面板的容器
        Container gamePanel = tilePanels[0][0].getParent();
        
        // 获取可用空间（考虑内边距）
        Insets insets = gamePanel.getInsets();
        int availableWidth = gamePanel.getWidth() - (insets.left + insets.right);
        int availableHeight = gamePanel.getHeight() - (insets.top + insets.bottom);
        
        // 计算单个地砖的大小（确保正方形）
        int tileSize = Math.min(availableWidth / 6, availableHeight / 6);
        
        // 更新所有瓦片的大小
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                JPanel tilePanel = tilePanels[y][x];
                if (tilePanel != null) {
                    tilePanel.setPreferredSize(new Dimension(tileSize, tileSize));
                    updateTileComponents(tilePanel, tileSize);
                }
            }
        }
        
        // 强制重新布局
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    // Modify updateTileComponents to be more robust
    private void updateTileComponents(JPanel tileCellPanel, int tileSize) { // tileCellPanel is tilePanels[y][x]
        if (tileCellPanel.getComponentCount() == 0) {
            // This cell panel is empty, perhaps an error or uninitialized ocean/sunk tile
            // We can either skip it or log an error.
            // For now, let's make sure it has a preferred size.
            tileCellPanel.setPreferredSize(new Dimension(tileSize, tileSize));
            return;
        }

        Component content = tileCellPanel.getComponent(0); // This should be the wrapper panel holding the JLayeredPane
        if (content instanceof JPanel) {
            JPanel contentWrapper = (JPanel) content;
            if (contentWrapper.getComponentCount() > 0 && contentWrapper.getComponent(0) instanceof JLayeredPane) {
                JLayeredPane layeredPane = (JLayeredPane) contentWrapper.getComponent(0);
                layeredPane.setPreferredSize(new Dimension(tileSize, tileSize));
                
                // Iterate through components of JLayeredPane and resize them
                // This logic needs to be specific to what you add to JLayeredPane
                for (Component comp : layeredPane.getComponents()) {
                    if (comp instanceof JLabel) {
                        JLabel label = (JLabel) comp;
                        // Example resizing logic, adjust based on your layers
                        if (JLayeredPane.getLayer(label) == JLayeredPane.DEFAULT_LAYER) { // Tile image
                            label.setBounds(0, 0, tileSize, tileSize);
                            // Rescale icon if label has one
                            if (label.getIcon() instanceof ImageIcon) {
                                ImageIcon originalIcon = (ImageIcon) label.getIcon();
                                Image scaledImg = originalIcon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
                                label.setIcon(new ImageIcon(scaledImg));
                            }
                        } else if (JLayeredPane.getLayer(label) == JLayeredPane.PALETTE_LAYER) { // Player icon or sunk label
                            // For player icons, you might want a different scaling (e.g., half size, centered)
                            // For sunk label, full size
                            if (label.getText() != null && label.getText().equals("SUNK")) {
                                label.setBounds(0, 0, tileSize, tileSize);
                            } else { // Assume player icon
                                int iconSize = tileSize / 2; // Example: player icon is half the tile size
                                int offset = (tileSize - iconSize) / 2;
                                label.setBounds(offset, offset, iconSize, iconSize);
                                if (label.getIcon() instanceof ImageIcon) {
                                    ImageIcon originalIcon = (ImageIcon) label.getIcon();
                                    Image scaledImg = originalIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                                    label.setIcon(new ImageIcon(scaledImg));
                                }
                            }
                        } else if (JLayeredPane.getLayer(label) == JLayeredPane.DRAG_LAYER) { // Coordinate/Name label
                            label.setBounds(0, 0, tileSize, tileSize / 5); // Top part for coords
                        } else if (JLayeredPane.getLayer(label) == JLayeredPane.MODAL_LAYER) { // Player Pawns layer
                             int pSize = tileSize / 3; 
                             label.setBounds((tileSize - pSize)/2, (tileSize - pSize)/2, pSize, pSize);
                             if (label.getIcon() instanceof ImageIcon) {
                                ImageIcon originalIcon = (ImageIcon) label.getIcon();
                                Image scaledImg = originalIcon.getImage().getScaledInstance(pSize, pSize, Image.SCALE_SMOOTH);
                                label.setIcon(new ImageIcon(scaledImg));
                            }
                        }
                    }
                }
                layeredPane.revalidate();
                layeredPane.repaint();
            } else {
                 // contentWrapper does not contain a JLayeredPane.
                 // Set size for the wrapper itself.
                 contentWrapper.setPreferredSize(new Dimension(tileSize, tileSize));
            }
        } else {
            // The first component of tileCellPanel is not a JPanel.
            // This is unexpected if you always add a wrapper.
            // Set size for the component directly if possible.
            content.setPreferredSize(new Dimension(tileSize, tileSize));
        }
         tileCellPanel.setPreferredSize(new Dimension(tileSize, tileSize)); // Also set size for the main cell panel
    }

}