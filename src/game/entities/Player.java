/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.entities;

import game.assets.AssetLoader;
import game.ui.GamePanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author lilli
 */
public class Player {

    private int x, y;
    private int width, height;
    private int speed = 6;

    private Image sprite;

    public Player(int screenWidth, int screenHeight) {

        width = 70;
        height = 70;

        x = 50;
        y = screenHeight / 2 - height / 2;

        // Cargar sprite
        sprite = AssetLoader.loadImage("nave.png");

        if (sprite == null) {
            System.out.println("Advertencia: nave.png no se encontro, usando fallback.");
        }
    }

    // -------------------------------
    // MOVIMIENTO DEL JUGADOR
    // -------------------------------
    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
    }

    public void moveRight() {
        x += speed;
        if (x + width > GamePanel.WIDTH)
            x = GamePanel.WIDTH - width;
    }

    public void moveUp() {
        y -= speed;
        if (y < 0) y = 0;
    }

    public void moveDown() {
        y += speed;
        if (y + height > GamePanel.HEIGHT)
            y = GamePanel.HEIGHT - height;
    }

    // -------------------------------
    // DIBUJAR JUGADOR
    // -------------------------------
    public void draw(Graphics g) {

        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(java.awt.Color.CYAN);
            g.fillRect(x, y, width, height);
        }
    }

    // -------------------------------
    // METODOS PARA COLISIONES
    // -------------------------------
    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y + height / 2;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}