/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.ui;

import game.assets.AssetLoader;
import game.assets.SoundManager;
import java.awt.Image;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 *
 * @author lilli
 */

public class MainMenu extends BackgroundPanelAnimado {

    private Image titleImage;

    public MainMenu(JFrame frame) {

        setLayout(null);  

        titleImage = AssetLoader.loadImage("titulo.png");
        SoundManager.playBackground("fondo.wav");


        JButton playBtn = new RetroButton("JUGAR");
        playBtn.setBounds(400, 250, 200, 50);
        playBtn.addActionListener(e -> {
            frame.setContentPane(new DifficultyMenu(frame));
            frame.revalidate();
        });

        JButton controlsBtn = new RetroButton("Controles");
        controlsBtn.setBounds(400, 320, 200, 50);
        controlsBtn.addActionListener(e -> {
            frame.setContentPane(new ControlsMenu(frame));
            frame.revalidate();
        });

        JButton scoresBtn = new RetroButton("Puntuaciones");
        scoresBtn.setBounds(400, 390, 200, 50);
        scoresBtn.addActionListener(e -> {
            frame.setContentPane(new ScoresMenu(frame));
            frame.revalidate();
        });
        
        JButton salirBtn = new RetroButton("Salir");
        salirBtn.setBounds(400, 500, 200, 50); // posición
        salirBtn.addActionListener(e -> System.exit(0));


        add(playBtn);
        add(controlsBtn);
        add(scoresBtn);
        add(salirBtn);
        
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (titleImage != null) {
            int w = 500, h = 150;
            g.drawImage(titleImage, (getWidth() - w) / 2, 40, w, h, null);
        }
    }
}