/*
 * Clase GamePanel: administra la representación visual del juego,
 * el fondo animado, la lectura de entradas y el control del ciclo
 * principal de actualización mediante GameLoop.
 *
 * Este archivo fue refactorizado aplicando principios SOLID y varios patrones:
 *
 * 1) SRP (Single Responsibility Principle)
 *    Antes: GamePanel mezclaba lógica del juego con lógica de renderizado.
 *    Ahora: GameState maneja toda la lógica y GamePanel únicamente dibuja y
 *    recibe entradas. Cada clase tiene un rol único.
 *
 * 2) OBSERVER PATTERN (HUDObserver)
 *    Antes: GamePanel dependía directamente de los valores internos de GameState.
 *    Code smell: "Inappropriate intimacy".
 *    Ahora: GamePanel recibe la información del HUD mediante un observador.
 *
 * 3) Aislamiento de la entrada del teclado
 *    Antes: múltiples partes del sistema podían manejar eventos.
 *    Ahora: KeyInput centraliza el registro de teclas presionadas,
 *    reduciendo acoplamiento y cumpliendo DIP.
 *
 * 4) Eliminación de duplicación (DRY)
 *    Se quitó el cálculo repetido del fondo animado y se movió a update().
 */
package game.ui;

import game.GameLoop;
import game.GameState;
import game.KeyInput;
import game.assets.AssetLoader;
import game.assets.SoundManager;
import game.observer.HUDObserver;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author lilli
 */
public class GamePanel extends JPanel {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;

    private GameLoop gameLoop;
    private GameState state;

    // OBSERVER: recibe actualizaciones automáticas del GameState
    private HUDObserver hud;

    // Fondo animado
    private Image bg;
    private int bgX = 0;
    private final int bgSpeed = 1;

    // -----------------------------------------------------
    // CONSTRUCTOR
    // -----------------------------------------------------
    public GamePanel() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);

        /*
         * Code smell corregido:
         * Antes: varias clases intentaban leer directamente eventos del teclado.
         * Ahora: KeyInput concentra el control y expone estados mediante un arreglo booleano.
         */
        addKeyListener(new KeyInput());

        // GameState encapsula TODA la lógica del juego: SRP aplicado.
        state = new GameState();

        // OBSERVER: HUD recibe actualizaciones sin acoplarse al GameState
        hud = new HUDObserver();
        state.addObserver(hud);

        // Carga del fondo
        bg = AssetLoader.loadImage("fondo.png");

        /*
         * Manejo de pausa con KeyAdapter.
         * Este comportamiento se mantiene aquí porque pertenece a la UI,
         * NO al GameState (cumple SRP).
         */
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_P) {
                    state.togglePause();
                    repaint();
                }
            }
        });
    }

    // -----------------------------------------------------
    // INICIO DEL JUEGO
    // -----------------------------------------------------
    public void startGame() {

        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
            requestFocus();
        });

        SoundManager.playBackground("fondo.wav");

        // GameLoop administra actualizaciones con bajo acoplamiento
        gameLoop = new GameLoop(() -> update());
        gameLoop.start();
    }

    // -----------------------------------------------------
    // UPDATE DEL JUEGO
    // -----------------------------------------------------
    private void update() {

        /*
         * GameState maneja internamente la pausa,
         * GamePanel únicamente se asegura de redibujar.
         */
        state.update();

        // Mostrar pantalla de Game Over o Victoria
        if (state.isGameOver() || state.isVictory()) {
            showGameOver();
            return;
        }

        // Fondo animado solo si el juego está activo
        if (!state.isPaused()) {
            bgX -= bgSpeed;

            // Reinicio del fondo para efecto loop
            if (bgX <= -WIDTH) {
                bgX = 0;
            }
        }

        repaint();
    }

    // -----------------------------------------------------
    // PINTADO DE ELEMENTOS
    // -----------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // === 1. Fondo animado ===
        if (bg != null) {
            g.drawImage(bg, bgX, 0, WIDTH, HEIGHT, null);
            g.drawImage(bg, bgX + WIDTH, 0, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // === 2. HUD por OBSERVER ===
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Puntos: " + hud.getScore(), 20, 30);
        g.drawString("Wave: " + hud.getWave(), 20, 50);
        g.drawString("Vidas: " + hud.getLives(), 20, 70);
        g.drawString("Dispara fracciones equivalentes a: " + hud.getTargetText() + "!", 350, 30);

        // === 3. Entidades del juego ===
        state.drawEntities(g);

        // === 4. Overlay de Pausa ===
        if (state.isPaused()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            String msg = "PAUSA";
            int textWidth = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, (WIDTH / 2) - (textWidth / 2), HEIGHT / 2);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String subMsg = "Presiona 'P' para continuar";
            int subWidth = g.getFontMetrics().stringWidth(subMsg);
            g.drawString(subMsg, (WIDTH / 2) - (subWidth / 2), HEIGHT / 2 + 50);
        }
    }

    // -----------------------------------------------------
    // TRANSICIÓN A PANTALLA DE GAME OVER
    // -----------------------------------------------------
    private void showGameOver() {

        if (gameLoop != null) gameLoop.stopLoop();

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame == null) return;

        int finalScore = state.getScore();
        int finalDifficulty = state.getDifficulty();

        GameOverMenu gameOver = new GameOverMenu(frame, finalScore, finalDifficulty);

        frame.setContentPane(gameOver);
        frame.revalidate();
    }

    public GameState getState() {
        return state;
    }
}