/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.utils;

import game.entities.Score;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lilli
 */
public class FileManager {

    private static final String FILE_PATH = "scores.dat";

    public static void saveScore(Score score) {
        List<Score> scores = loadScores();
        scores.add(score);
        saveScores(scores);
    }

    public static List<Score> loadScores() {
        File f = new File(FILE_PATH);

        if (!f.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Score>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static void saveScores(List<Score> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
