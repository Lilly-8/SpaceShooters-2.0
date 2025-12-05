/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.entities;

import java.io.Serializable;

/**
 *
 * @author lilli
 */
public class Score implements Serializable {

    private String playerName;
    private int points;
    private int difficulty; // 0 o 1

    public Score(String name, int points, int difficulty) {
        this.playerName = name;
        this.points = points;
        this.difficulty = difficulty;
    }

    public String getName() { return playerName; }
    public int getPoints() { return points; }
    public int getDifficulty() { return difficulty; }
}

