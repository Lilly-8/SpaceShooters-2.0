/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.assets;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

/**
 *
 * @author lilli
 */
public class AssetLoader {

    // -------------------------
    // CARGA DE IMÁGENES
    // -------------------------
    // Asume que las imágenes están en src/resources/
    public static Image loadImage(String filename) {
        String path = "/resources/" + filename;
        try {
            InputStream is = AssetLoader.class.getResourceAsStream(path);
            if (is == null) {
                System.err.println("ERROR: No se encontró la imagen: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO cargando imagen: " + path);
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------
    // CARGA DE SONIDOS
    // -------------------------
    // Asume que los sonidos están en src/resources/sonidos/
    public static void playSound(String filename) {
        // Importante: Java usa "/" incluso en Windows para recursos internos
        String path = "/resources/sonidos/" + filename;
        
        try {
            // 1. Obtener el archivo como stream
            InputStream audioSrc = AssetLoader.class.getResourceAsStream(path);

            if (audioSrc == null) {
                System.err.println("ERROR: No se encontró el sonido: " + path);
                return;
            }

            // 2. Buffer para evitar problemas de marca/reinicio en algunos sistemas
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            
            // 3. Obtener el stream de audio
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);

            // 4. Abrir el clip y reproducir
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();

        } catch (Exception e) {
            // Imprime el error pero no detiene el juego
            System.err.println("ERROR reproduciendo sonido (" + path + "): " + e.getMessage());
            // Nota: Java base solo soporta .wav, .aiff, .au (NO soporta .mp3 directamente)
        }
    }

    public static ImageIcon loadIcon(String resourcescontroles_flechaspng) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}