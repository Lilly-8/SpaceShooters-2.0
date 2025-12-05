/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author lilli
 */
public class Asteroid {

    private int x, y;
    private int speed;
    private int radius;
    private boolean active = true;

    private boolean isTarget;
    private Fraction fraction;
    private Image sprite;

    // 0 = Identificación, 1 = Equivalencias
    private int difficultyMode;

    public Asteroid(int x, int y, int speed, int radius,
                    Fraction fraction, boolean isTarget, int difficultyMode) {

        this.x = x;
        this.y = y;
        this.speed = speed;
        this.radius = radius;
        this.fraction = fraction;
        this.isTarget = isTarget;
        this.difficultyMode = difficultyMode;

        // ---------------------------------------------------------
        // SELECCION DE SPRITE SEGÚN MODO
        // ---------------------------------------------------------
        if (difficultyMode == 0) {
            sprite = AsteroidSpriteProvider.getIdentSprite(
                    fraction.getNumerator(),
                    fraction.getDenominator()
            );
        } else {
            sprite = AsteroidSpriteProvider.getEquivalenceSprite();
        }

        if (sprite == null) {
            System.out.println("Sprite no encontrado para fraccion: " + fraction);
        }
    }

    // ---------------------------------------------------------
    // MOVIMIENTO
    // ---------------------------------------------------------
    public void update() {
        x -= speed;
        if (x < -radius * 2) active = false;
    }

    // ---------------------------------------------------------
    // DIBUJO
    // ---------------------------------------------------------
    public void draw(Graphics g) {

        // Dibujar sprite
        if (sprite != null) {
            g.drawImage(sprite, x, y, radius * 2, radius * 2, null);

            // En modo equivalencias, escribir la fracción encima
            if (difficultyMode == 1 && fraction != null) {
                drawFractionText(g);
            }

        } else {
            // Fallback sin sprite
            g.setColor(isTarget ? Color.GREEN : Color.RED);
            g.fillOval(x, y, radius * 2, radius * 2);

            if (difficultyMode == 1 && fraction != null) {
                drawFractionText(g);
            }
        }
    }

    // DIBUJAR TEXTO EN LA FRACCION (solo modo equivalencias)
    private void drawFractionText(Graphics g) {

        String txt = fraction.getNumerator() + "/" + fraction.getDenominator();

        g.setFont(new Font("Arial", Font.BOLD, 18));

        int centerX = x + radius;
        int centerY = y + radius;

        int textWidth = g.getFontMetrics().stringWidth(txt);
        int textHeight = g.getFontMetrics().getAscent();

        int textX = centerX - textWidth / 2;
        int textY = centerY + textHeight / 2 - 4;

        // Contorno negro
        g.setColor(Color.BLACK);
        g.drawString(txt, textX + 1, textY + 1);

        // Texto blanco
        g.setColor(Color.WHITE);
        g.drawString(txt, textX, textY);
    }

    // ---------------------------------------------------------
    // COLISIONES
    // ---------------------------------------------------------
    public boolean collidesWith(Bullet b) {
        return b.getX() < x + radius * 2 &&
               b.getX() + b.getWidth() > x &&
               b.getY() < y + radius * 2 &&
               b.getY() + b.getHeight() > y;
    }

    public void deactivate() { active = false; }
    public boolean isActive() { return active; }

    public void setAsTarget(boolean value) { this.isTarget = value; }
    public boolean isTarget() { return isTarget; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }
    public Fraction getFraction() { return fraction; }
}