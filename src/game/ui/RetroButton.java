/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.ui;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author lilli
 */


public class RetroButton extends JButton {

    public RetroButton(String text) {
        super(text);

        setFont(new Font("Press Start 2P", Font.PLAIN, 16)); // o Pixel Emulator
        setBackground(new Color(40, 40, 70));
        setForeground(Color.WHITE);

        setFocusPainted(false);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(getBackground().darker());
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}

