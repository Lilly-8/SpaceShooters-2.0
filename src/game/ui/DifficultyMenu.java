/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.ui;

import game.assets.AssetLoader;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author lilli
 */
public class DifficultyMenu extends BackgroundPanelAnimado {

    private Image titleImage;

    public DifficultyMenu(JFrame frame) {

        setLayout(null);
        setOpaque(false);
        titleImage = AssetLoader.loadImage("titulodificultades.png");

        // Modo Identificación
        JButton modeIdent = new RetroButton("Nivel de Identificación");
        modeIdent.setBounds(360, 250, 280, 50);
        modeIdent.addActionListener(e -> {
            GamePanel panel = new GamePanel();
            panel.getState().setDifficulty(0);
            frame.setContentPane(panel);
            frame.revalidate();
            panel.startGame();
        });

        // Modo Equivalencia
        JButton modeEq = new RetroButton("Nivel de Equivalencia");
        modeEq.setBounds(360, 320, 280, 50);
        modeEq.addActionListener(e -> {
            GamePanel panel = new GamePanel();
            panel.getState().setDifficulty(1);
            frame.setContentPane(panel);
            frame.revalidate();
            panel.startGame();
        });

        // Botón volver
        JButton backBtn = new RetroButton("Volver al menú");
        backBtn.setBounds(50, 700, 200, 40);
        backBtn.addActionListener(e -> {
            frame.setContentPane(new MainMenu(frame));
            frame.revalidate();
            frame.repaint();
        });
        
       
        add(modeIdent);
        add(modeEq);
        add(backBtn);
        setComponentZOrder(backBtn, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (titleImage != null) {
            int w = 600, h = 140;
            g.drawImage(titleImage, (getWidth() - w)/2, 40, w, h, null);
        }
    }
}