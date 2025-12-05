/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.entities;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author lilli
 */
public class Bullet {

    private int x, y;
    private int width = 12;
    private int height = 4;
    private int speed = 10;

    private boolean active = true;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(int screenWidth) {
        x += speed;
        if (x > screenWidth) active = false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    // -------------------------
    // PARA LAS COLISIONES
    // -------------------------
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}