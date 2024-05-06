package main;

import javax.swing.*;

import user.UserGUI;

public class Main {

    public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
            new UserGUI();
        });
    }
}