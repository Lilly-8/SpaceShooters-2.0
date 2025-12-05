/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.ui;

import game.assets.AssetLoader;
import game.entities.Score;
import game.utils.FileManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author lilli
 */
public class ScoresMenu extends BackgroundPanelAnimado {

    private Image titleImage;
    private List<Score> scores;

    public ScoresMenu(JFrame frame) {

        setLayout(null);

        titleImage = AssetLoader.loadImage("titulopuntuaciones.png");

        // Cargar scores del archivo
        scores = FileManager.loadScores();

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(30, 700, 150, 40);
        backBtn.addActionListener(e -> {
            frame.setContentPane(new MainMenu(frame));
            frame.revalidate();
        });

        add(backBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (titleImage != null) {
            int w = 600, h = 150;
            g.drawImage(titleImage, (getWidth() - w)/2, 40, w, h, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        int y = 220;
        int rank = 1;

        if (scores.isEmpty()) {
            g.drawString("No scores yet!", 420, y);
            return;
        }

        for (Score s : scores) {
            g.drawString(rank + ".  " + s.getName() + " — " + s.getPoints(), 380, y);
            y += 40;
            rank++;
        }
    }
}