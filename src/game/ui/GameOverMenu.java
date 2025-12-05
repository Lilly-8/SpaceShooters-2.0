package game.ui;

import game.assets.AssetLoader;
import game.entities.Score;
import game.utils.FileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author lilli
 */
public class GameOverMenu extends JPanel {

    private JFrame parentFrame;
    private int finalScore;
    private int finalDifficulty; // 0=Identification, 1=Equivalence

    // Fondo
    private Image bgImage;

    public GameOverMenu(JFrame frame, int score, int difficulty) {
        this.parentFrame = frame;
        this.finalScore = score;
        this.finalDifficulty = difficulty;

        setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
        setLayout(null);

        // Cargar fondo del menú
        bgImage = AssetLoader.loadImage("fondo.png");

        addTitle();
        addScoreText();
        addNameEntry();
        addButtons();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar fondo
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void addTitle() {
        JLabel title = new JLabel("GAME OVER", SwingConstants.CENTER);
        title.setBounds(0, 70, GamePanel.WIDTH, 80);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.RED);
        add(title);
    }

    // -----------------------------
    // Score Display
    // -----------------------------
    private void addScoreText() {
        JLabel scoreLabel = new JLabel("Tu puntaje: " + finalScore, SwingConstants.CENTER);
        scoreLabel.setBounds(0, 180, GamePanel.WIDTH, 50);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 32));
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel);
    }

    // -----------------------------
    // Name Entry
    // -----------------------------
    private JTextField nameField;

    private void addNameEntry() {
        JLabel label = new JLabel("Registra tu nombre:", SwingConstants.CENTER);
        label.setBounds(0, 260, GamePanel.WIDTH, 40);
        label.setFont(new Font("Arial", Font.PLAIN, 26));
        label.setForeground(Color.LIGHT_GRAY);

        nameField = new JTextField();
        nameField.setBounds(GamePanel.WIDTH / 2 - 150, 310, 300, 40);
        nameField.setFont(new Font("Arial", Font.PLAIN, 22));

        add(label);
        add(nameField);
    }

    // -----------------------------
    // Buttons
    // -----------------------------
    private void addButtons() {
        JButton saveBtn = createButton("Guardar puntaje", 390);
        JButton retryBtn = createButton("Jugar de nuevo", 460);
        JButton menuBtn = createButton("Menú principal", 530);

        saveBtn.addActionListener(e -> saveScore());
        retryBtn.addActionListener(e -> retry());
        menuBtn.addActionListener(e -> backToMenu());

        add(saveBtn);
        add(retryBtn);
        add(menuBtn);
    }

    private JButton createButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(GamePanel.WIDTH / 2 - 160, y, 320, 50);
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setFocusPainted(false);
        return btn;
    }

    // -----------------------------
    // ACTIONS
    // -----------------------------
    private void saveScore() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registra tu nombre.");
            return;
        }

        // Crear objeto Score
        Score score = new Score(name, finalScore, finalDifficulty);

        // Guardarlo en archivo binario
        FileManager.saveScore(score);

        JOptionPane.showMessageDialog(this, "¡Puntaje guardado con éxito!");
    }

    private void retry() {
        GamePanel panel = new GamePanel();
        panel.getState().setDifficulty(finalDifficulty);

        parentFrame.setContentPane(panel);
        parentFrame.revalidate();
        panel.startGame();
    }

    private void backToMenu() {
        MainMenu menu = new MainMenu(parentFrame);
        parentFrame.setContentPane(menu);
        parentFrame.revalidate();
    }
}
