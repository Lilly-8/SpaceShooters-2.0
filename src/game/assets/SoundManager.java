/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.assets;

/**
 *
 * @author lilli
 */
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

public class SoundManager {

    private static Clip backgroundClip; // música de fondo única

    // Reproduce efectos de sonido (disparo, correcto, incorrecto, etc.)
    public static void playSFX(String fileName) {
        try {
            InputStream audioSrc = SoundManager.class.getResourceAsStream("/resources/sonidos/" + fileName);
            if (audioSrc == null) {
                System.err.println("SFX no encontrado: " + fileName);
                return;
            }
            InputStream buffered = new BufferedInputStream(audioSrc);
            AudioInputStream ais = AudioSystem.getAudioInputStream(buffered);

            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Música de fondo, NO se duplica
    public static void playBackground(String fileName) {

        try {
            // Si ya está sonando, NO volver a iniciar
            if (backgroundClip != null && backgroundClip.isActive()) {
                return; // evita duplicación
            }

            InputStream audioSrc = SoundManager.class.getResourceAsStream("/resources/sonidos/" + fileName);
            if (audioSrc == null) {
                System.err.println("Música no encontrada: " + fileName);
                return;
            }

            InputStream buffered = new BufferedInputStream(audioSrc);
            AudioInputStream ais = AudioSystem.getAudioInputStream(buffered);

            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(ais);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Detener música (por ejemplo, al volver al menú)
    public static void stopBackground() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            //backgroundClip.close();
            //backgroundClip = null;
        }
    }
}
