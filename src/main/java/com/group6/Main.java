package com.group6;

import com.group6.GUI.StartMenuFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartMenuFrame menu = new StartMenuFrame();
            menu.setVisible(true);
        });
    }
}
