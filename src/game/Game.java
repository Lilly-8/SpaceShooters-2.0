/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import game.ui.MainMenu;
import javax.swing.JFrame;

/**
 *
 * @author lilli
 */
public class Game {

    public static void main(String[] args) {

        JFrame window = new JFrame("MathInvaders");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        MainMenu menu = new MainMenu(window);
        menu.setPreferredSize(new java.awt.Dimension(1000, 800));

        window.setContentPane(menu);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}