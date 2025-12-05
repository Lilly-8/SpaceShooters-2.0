/*
 * Clase central del juego: administra estado, lógica, colisiones, oleadas,
 * reproducción de sonido, pausa, puntaje, etc.
 *
 * Se aplicaron varios patrones de diseño y principios SOLID:
 *
 * 1) PATTERN STRATEGY → (FractionStrategy)
 *    Antes: lógica de generación/validación de fracciones estaba mezclada dentro del estado.
 *    Code smell: "Divergent Change" + "Long Method" + violación del OCP.
 *    Solución: separar reglas por modo (Identificación / Equivalencias) en estrategias.
 *    Beneficio: el código NO cambia si se agregan nuevos modos.
 *
 * 2) OBSERVER → (GameObserver, HUDObserver)
 *    Antes: HUD se actualizaba manualmente desde GameState.
 *    Code smell: "Inappropriate Intimacy" y violación de SRP.
 *    Solución: ahora GameState solo notifica cambios sin conocer el HUD.
 *
 * 3) FACTORY → (AsteroidFactory)
 *    Antes: GameState creaba asteroides manualmente.
 *    Code smell: "God Class" y duplicación.
 *    Solución: externalizar creación en una fábrica.
 *
 * 4) SOLID — SRP + OCP + DIP
 *    - GameState ahora solo controla lógica del juego.
 *    - No crea sprites, ni carga imágenes, ni maneja UI.
 */

package game;

import game.assets.SoundManager;
import game.entities.Asteroid;
import game.entities.Bullet;
import game.entities.Fraction;
import game.entities.Player;
import game.factory.AsteroidFactory;
import game.observer.GameObserver;
import game.observer.GameSubject;
import game.strategy.EquivalentFractionStrategy;
import game.strategy.FractionStrategy;
import game.strategy.IdentificationFractionStrategy;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;
import game.ui.GamePanel;
import java.util.Random;

/**
 *
 * @author lilli
 */
public class GameState implements GameSubject {

    // -------------------------------
    // OBSERVER PATTERN
    // -------------------------------
    // Antes: HUD y GameState estaban acoplados.
    // Refactor: ahora cualquier clase puede observar cambios sin que GameState la conozca.
    private List<GameObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(GameObserver obs) { observers.add(obs); }
    @Override
    public void removeObserver(GameObserver obs) { observers.remove(obs); }

    @Override
    public void notifyObservers() {
        for (GameObserver obs : observers) obs.onGameStateChanged(this);
    }

    // -------------------------------
    // CAMPOS DEL ESTADO DEL JUEGO
    // -------------------------------
    private Player player;
    private List<Asteroid> asteroids;
    private List<Bullet> bullets;

    private int score = 0;
    private int wave = 1;
    private int targetsHit = 0;
    private int lives = 5;

    private boolean gameOver = false;
    private boolean victory = false;

    private boolean playedGameOverSound = false;

    // -------------------------------
    // STRATEGY PATTERN
    // -------------------------------
    // Antes: lógica de identificación vs equivalencias estaba mezclada.
    // Ahora: cada modo tiene su propia estrategia.
    private int difficultyMode = 0;
    private FractionStrategy strategy;
    private Fraction targetFraction;

    // -------------------------------
    // DISPARO
    // -------------------------------
    private boolean shootPressed = false;
    private int shootCooldown = 0;
    private final int SHOOT_RATE = 12;

    // -------------------------------
    // CONFIGURACIÓN DE WAVES
    // -------------------------------
    private static final int TARGETS_PER_WAVE = 5;
    private static final int MAX_WAVES = 1;

    // -------------------------------
    // SPAWNER
    // -------------------------------
    private int spawnCooldown = 0;
    private final int SPAWN_RATE = 60;
    private final int MAX_ACTIVE_ASTEROIDS = 7;

    private final int screenWidth = GamePanel.WIDTH;
    private final int screenHeight = GamePanel.HEIGHT;

    private Random rand = new Random();

    // -------------------------------
    // PAUSA DEL JUEGO
    // -------------------------------
    private boolean paused = false;

    public boolean isPaused() { return paused; }
    public void togglePause() { paused = !paused; }

    // Objetivos posibles para equivalencias
    private static final Fraction[] BASE_EQ_TARGETS = {
        new Fraction(1,2), new Fraction(1,3), new Fraction(1,4),
        new Fraction(2,3), new Fraction(3,4), new Fraction(2,5)
    };

    // -----------------------------------------
    // CONSTRUCTOR
    // -----------------------------------------
    public GameState() {
        // Por defecto identificaciones
        strategy = new IdentificationFractionStrategy();
        resetGame();
    }

    // -----------------------------------------
    // REINICIAR JUEGO
    // SRP: GameState administra el estado general del juego.
    // -----------------------------------------
    public void resetGame() {

        player = new Player(screenWidth, screenHeight);
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();

        score = 0;
        wave = 1;
        targetsHit = 0;
        lives = 5;
        paused = false;

        gameOver = false;
        victory = false;
        playedGameOverSound = false;

        // Música de fondo persistente
        SoundManager.playBackground("fondo.wav");

        spawnWave();
        notifyObservers();
    }

    // -----------------------------------------
    // CAMBIO DE MODO (STRATEGY)
    // Justificación: OCP — agregar nuevo modo NO modifica GameState.
    // -----------------------------------------
    public void setDifficulty(int mode) {

        this.difficultyMode = mode;

        if (mode == 1) {
            strategy = new EquivalentFractionStrategy();
            System.out.println("[GameState] Modo = EQUIVALENCIAS");
        } else {
            strategy = new IdentificationFractionStrategy();
            System.out.println("[GameState] Modo = IDENTIFICACIÓN");
        }

        resetGame();
    }

    // -----------------------------------------
    // NUEVA WAVE
    // -----------------------------------------
    private void spawnWave() {

        asteroids.clear();

        // STRATEGY decide cómo se generan los objetivos
        if (difficultyMode == 1) {
            targetFraction = BASE_EQ_TARGETS[rand.nextInt(BASE_EQ_TARGETS.length)];
        } else {
            targetFraction = strategy.generateRandomFraction();
        }

        notifyObservers();

        // Crear asteroides iniciales
        for (int i = 0; i < 3; i++) spawnAsteroid();

        spawnCooldown = SPAWN_RATE;
    }

    // -----------------------------------------
    // CREAR ASTEROIDE (FACTORY)
    // Justificación: Reducir duplicación y dependencia.
    // -----------------------------------------
    private void spawnAsteroid() {

        Asteroid a = AsteroidFactory.create(
            strategy, screenWidth, screenHeight, difficultyMode
        );

        boolean isTarget = strategy.validate(a.getFraction(), targetFraction);
        a.setAsTarget(isTarget);

        asteroids.add(a);
    }

    // -----------------------------------------
    // UPDATE GENERAL
    // -----------------------------------------
    public void update() {
        if (gameOver || victory || paused) return;

        updatePlayer();
        updateBullets();
        updateAsteroids();
        updateWave();
        updateSpawner();
    }

    // -----------------------------------------
    // MOVIMIENTO DEL JUGADOR
    // -----------------------------------------
    private void updatePlayer() {

        if (KeyInput.isKeyDown(java.awt.event.KeyEvent.VK_LEFT)) player.moveLeft();
        if (KeyInput.isKeyDown(java.awt.event.KeyEvent.VK_RIGHT)) player.moveRight();
        if (KeyInput.isKeyDown(java.awt.event.KeyEvent.VK_UP)) player.moveUp();
        if (KeyInput.isKeyDown(java.awt.event.KeyEvent.VK_DOWN)) player.moveDown();

        shootPressed = KeyInput.isKeyDown(java.awt.event.KeyEvent.VK_SPACE);

        if (shootPressed && shootCooldown <= 0) {
            fireBullet();
            shootCooldown = SHOOT_RATE;
        } else {
            shootCooldown--;
        }
    }

    // -----------------------------------------
    // DISPARAR BALA
    // -----------------------------------------
    private void fireBullet() {
        SoundManager.playSFX("disparo.wav");
        bullets.add(new Bullet(
            player.getX() + player.getWidth(),
            player.getY() + player.getHeight() / 2
        ));
    }

    // -----------------------------------------
    // ACTUALIZAR BALAS
    // -----------------------------------------
    private void updateBullets() {

        List<Bullet> remove = new ArrayList<>();

        synchronized (bullets) {
            for (Bullet b : bullets) {
                b.update(screenWidth);
                if (!b.isActive()) remove.add(b);
            }
        }

        bullets.removeAll(remove);
    }

    // -----------------------------------------
    // COLISIONES Y ACTUALIZACIÓN DE ASTEROIDES
    // ⭐ Con FIX de sonido de Game Over
    // -----------------------------------------
    private void updateAsteroids() {

        List<Asteroid> removeList = new ArrayList<>();

        synchronized (asteroids) {

            for (Asteroid a : asteroids) {

                a.update();
                if (!a.isActive()) {
                    removeList.add(a);
                    continue;
                }

                // ---------------------------
                // COLISIÓN JUGADOR–ASTEROIDE
                // ---------------------------
                int dx = a.getX() - player.getCenterX();
                int dy = a.getY() - player.getCenterY();
                int distSq = dx*dx + dy*dy;

                int playerRadius = Math.min(player.getWidth(), player.getHeight())/2;
                int totalRadius = a.getRadius() + playerRadius;

                if (distSq < totalRadius * totalRadius) {

                    SoundManager.playSFX("choque.wav");
                    lives--;
                    notifyObservers();

                    a.deactivate();

                    if (lives <= 0) triggerGameOver();
                    continue;
                }

                // ---------------------------
                // COLISIÓN BALA–ASTEROIDE
                // ---------------------------
                synchronized (bullets) {
                    for (Bullet b : bullets) {

                        if (a.collidesWith(b)) {

                            boolean correct =
                                strategy.validate(a.getFraction(), targetFraction);

                            if (correct) {
                                SoundManager.playSFX("correcto.wav");
                                score += 100;
                                targetsHit++;
                            } else {
                                SoundManager.playSFX("incorrecto.wav");
                                lives--;
                            }

                            notifyObservers();

                            a.deactivate();
                            b.deactivate();

                            if (lives <= 0) triggerGameOver();

                            break;
                        }
                    }
                }
            }

            asteroids.removeAll(removeList);
        }
    }

    // -----------------------------------------
    // GAME OVER — sonido corregido
    // -----------------------------------------
    private void triggerGameOver() {

        gameOver = true;

        if (!playedGameOverSound) {
            playedGameOverSound = true;
            SoundManager.playSFX("GameOver.wav");
        }
    }

    // -----------------------------------------
    // CAMBIO DE WAVE
    // -----------------------------------------
    private void updateWave() {

        if (targetsHit >= TARGETS_PER_WAVE) {

            targetsHit = 0;

            if (wave < MAX_WAVES) {
                wave++;
                notifyObservers();
                spawnWave();
            } else {
                victory = true;
                SoundManager.playSFX("victoria.wav");
            }
        }
    }

    // -----------------------------------------
    // SPAWNER AUTOMÁTICO
    // -----------------------------------------
    private void updateSpawner() {
        spawnCooldown--;

        if (spawnCooldown <= 0 && asteroids.size() < MAX_ACTIVE_ASTEROIDS) {
            spawnAsteroid();
            spawnCooldown = SPAWN_RATE;
        }
    }

    // -----------------------------------------
    // DIBUJADO DE ENTIDADES
    // -----------------------------------------
    public void drawEntities(Graphics g) {

        player.draw(g);

        for (Bullet b : bullets) b.draw(g);
        for (Asteroid a : asteroids) a.draw(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("PERDISTE", screenWidth/2 - 40, screenHeight/2);
        }

        if (victory) {
            g.setColor(Color.GREEN);
            g.drawString("GANASTE!", screenWidth/2 - 40, screenHeight/2);
        }
    }

    // -----------------------------------------
    // GETTERS
    // -----------------------------------------
    public int getScore() { return score; }
    public int getWave() { return wave; }
    public int getLives() { return lives; }
    public Fraction getTargetFraction() { return targetFraction; }
    public int getDifficulty() { return difficultyMode; }
    public boolean isGameOver() { return gameOver; }
    public boolean isVictory() { return victory; }
}