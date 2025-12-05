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

public class BackgroundPanelAnimado extends JPanel {

    private Image background;
    private int x1 = 0;
    private int x2;
    private final int velocidad = 2;
    private boolean inicializado = false; // Evita el hueco blanco inicial

    public BackgroundPanelAnimado() {
        try {
            background = new ImageIcon(getClass().getResource("/resources/fondo.png")).getImage();
        } catch (Exception e) {
            System.out.println("No se pudo cargar fondo.png");
        }

        setLayout(null);

        // Timer (animación)
        new Timer(20, e -> mover()).start();
    }

    private void inicializarSiEsNecesario() {
        if (!inicializado && getWidth() > 0) {
            x1 = 0;
            x2 = getWidth();   // ← AQUÍ se arregla el problema
            inicializado = true;
        }
    }

    private void mover() {
        inicializarSiEsNecesario();
        if (!inicializado) return;

        x1 -= velocidad;
        x2 -= velocidad;

        int width = getWidth();

        // Cuando una imagen sale completamente, se reposiciona
        if (x1 <= -width) {
            x1 = x2 + width;
        }
        if (x2 <= -width) {
            x2 = x1 + width;
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        inicializarSiEsNecesario();
        if (!inicializado) return;

        int width = getWidth();
        int height = getHeight();

        g.drawImage(background, x1, 0, width, height, null);
        g.drawImage(background, x2, 0, width, height, null);
    }
}

