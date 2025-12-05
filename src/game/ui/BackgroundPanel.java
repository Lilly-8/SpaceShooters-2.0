/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.ui;

/**
 *
 * @author lilli
 */

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    private Image background;

    public BackgroundPanel() {
        try {
            background = new ImageIcon(getClass().getResource("/resources/fondo.png")).getImage();
        } catch (Exception e) {
            System.out.println("No se pudo cargar fondo.png");
        }

        setLayout(null); // Para colocar botones de forma manual
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
    }
}

