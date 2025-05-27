package com.group6.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

//图片工具类：用于读取并缩放资源图片
public class ImageUtils {
    public static ImageIcon loadCardImage(String path, int width, int height) {
        try {
            URL imageUrl = ImageUtils.class.getResource(path);
            if (imageUrl == null) {
                System.err.println("图片资源未找到: " + path);
                return null;
            }

            BufferedImage img = ImageIO.read(imageUrl);
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
