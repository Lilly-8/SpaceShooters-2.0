/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.entities;

import game.assets.AssetLoader;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lilli
 */
public class AsteroidSpriteProvider {

    private static final Map<String, Image> identSprites = new HashMap<>();
    private static Image equivalenceSprite;

    static {

        equivalenceSprite = AssetLoader.loadImage("asteroidesEq/asteroide.png");

        // Cargar todos los sprites disponibles para identificacion
        loadIdentSprite(1,1);
        loadIdentSprite(1,2);
        loadIdentSprite(1,3);
        loadIdentSprite(1,4);
        loadIdentSprite(1,8);
        loadIdentSprite(2,3);
        loadIdentSprite(3,4);
    }

    private static void loadIdentSprite(int num, int den) {
        String key = num + "_" + den;
        String path = "asteroidesIdent/asteroide_" + key + ".png";

        Image img = AssetLoader.loadImage(path);
        if (img != null) {
            identSprites.put(key, img);
        } else {
            System.out.println("Sprite faltante: " + path);
        }
    }

    public static Image getIdentSprite(int num, int den) {
        return identSprites.get(num + "_" + den);
    }

    public static Image getEquivalenceSprite() {
        return equivalenceSprite;
    }
}
