package com.group6.utils;

import com.group6.entity.common.RoleType;
import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;

public class RoleImageManager {
    private static final Map<RoleType, ImageIcon> roleImages = new HashMap<>();
    // 移除 resources/ 前缀，因为资源会被打包到类路径根目录
    private static final String IMAGE_PATH = "player/";

    static {
        // 初始化角色图片
        loadRoleImage(RoleType.ENGINEER, "Red.png");
        loadRoleImage(RoleType.PILOT, "Green.png");
        loadRoleImage(RoleType.NAVIGATOR, "Blue.png");
        loadRoleImage(RoleType.DIVER, "Black.png");
        loadRoleImage(RoleType.EXPLORER, "Yellow.png");
        loadRoleImage(RoleType.MESSENGER, "White.png");
    }

    private static void loadRoleImage(RoleType roleType, String imageName) {
        try {
            // 构建正确的类路径资源路径
            String path = IMAGE_PATH + imageName;
            System.out.println("Attempting to load image from: " + path);

            // 使用 ClassLoader.getResource() 并确保路径不以 / 开头
            java.net.URL imageUrl = RoleImageManager.class.getClassLoader().getResource(path);

            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                roleImages.put(roleType, icon);
                System.out.println("Successfully loaded image for " + roleType);
            } else {
                System.out.println("Failed to load image for " + roleType + ": URL is null");
                // 尝试打印类路径，帮助调试
                System.out.println("Classpath: " + System.getProperty("java.class.path"));
            }
        } catch (Exception e) {
            System.out.println("Error loading image for " + roleType + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ImageIcon getRoleImage(RoleType roleType) {
        ImageIcon icon = roleImages.get(roleType);
        if (icon == null) {
            System.out.println("No image found for role: " + roleType);
            // 可以考虑返回一个默认图标
            return getDefaultIcon();
        }
        return icon;
    }

    private static ImageIcon getDefaultIcon() {
        // 返回一个默认图标，避免 UI 中出现空白
        try {
            java.net.URL defaultUrl = RoleImageManager.class.getClassLoader().getResource("default.png");
            if (defaultUrl != null) {
                return new ImageIcon(defaultUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}