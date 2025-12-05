/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.ui;

import game.assets.AssetLoader;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author lilli
 */
public class ControlsMenu extends BackgroundPanelAnimado {

    private Image titleImage;

    private Image upArrow;
    private Image downArrow;
    private Image leftArrow;
    private Image rightArrow;

    public ControlsMenu(JFrame frame) {

        setLayout(null);

        titleImage = AssetLoader.loadImage("titulocontroles.png");

        // Cargar imágenes de flechas
        upArrow = AssetLoader.loadImage("teclas/arriba.png");
        downArrow = AssetLoader.loadImage("teclas/abajo.png");
        leftArrow = AssetLoader.loadImage("teclas/izquierda.png");
        rightArrow = AssetLoader.loadImage("teclas/derecha.png");

        // Botón volver
        JButton backBtn = new JButton("Volver al menú");
        backBtn.setBounds(50, 700, 200, 40);
        backBtn.addActionListener(e -> {
            frame.setContentPane(new MainMenu(frame));
            frame.revalidate();
        });
        add(backBtn);

        // Botón salir
        JButton exitBtn = new JButton("Salir");
        exitBtn.setBounds(750, 700, 200, 40);
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Título
        if (titleImage != null) {
            int w = 600, h = 140;
            g.drawImage(titleImage, (getWidth() - w)/2, 40, w, h, null);
        }

        g.setColor(Color.PINK);
        g.setFont(new Font("Dialog", Font.BOLD, 30));
        g.drawString("Mover nave:", 100, 300);

        // Dibuja las flechas
        g.drawImage(upArrow,     500, 260, 70, 70, null);
        g.drawImage(downArrow,   500, 340, 70, 70, null);
        g.drawImage(leftArrow,   430, 300, 70, 70, null);
        g.drawImage(rightArrow,  570, 300, 70, 70, null);

        g.drawString("Disparar: barra espaciadora", 100, 450);
        g.drawString("Pausa / Reanudar: tecla 'P'", 100, 520);
    }
}
