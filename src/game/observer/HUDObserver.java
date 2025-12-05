/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.observer;

import game.GameState;

/**
 *
 * @author lilli
 */
public class HUDObserver implements GameObserver {

    private int score;
    private int wave;
    private int lives;
    private String targetText;

    @Override
    public void onGameStateChanged(GameState state) {
        this.score = state.getScore();
        this.wave = state.getWave();
        this.lives = state.getLives();
        this.targetText = state.getTargetFraction().toString();
    }

    // Getters
    public int getScore() { return score; }
    public int getWave() { return wave; }
    public int getLives() { return lives; }
    public String getTargetText() { return targetText; }
}
