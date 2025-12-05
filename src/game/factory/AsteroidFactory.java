/*
 * Clase AsteroidFactory — Implementación del Patrón FACTORY.
 *
 * Razones del refactor y documentación de diseño:
 *
 * 1) Code smell detectado: "Duplicate Code" y "Long Parameter List"
 *    Antes, la creación de asteroides ocurría directamente en GameState,
 *    repitiendo cálculos como posición, velocidad, radio, fracción, etc.
 *    Esto violaba DRY.
 *
 * 2) Solución aplicada: FACTORY PATTERN
 *    La creación de objetos complejos (Asteroid) se encapsula en esta clase.
 *    Con esto GameState deja de depender de *cómo* se construye un asteroide.
 *
 * 3) SOLID: Principio de Inversión de Dependencias (DIP)
 *    GameState ahora depende de una abstracción (FractionStrategy) y de una
 *    fábrica estática, no de los detalles internos de Asteroid.
 *    Esto hace más fácil modificar o ampliar tipos de asteroides.
 *
 * 4) SOLID: Principio de Responsabilidad Única (SRP)
 *    GameState ya no construye asteroides: solo decide CUÁNDO crearlos.
 *    AsteroidFactory se encarga de CÓMO crearlos.
 *
 * 5) Preparación para extensibilidad:
 *    Esta fábrica permite:
 *      - Cambiar proporciones de velocidad por dificultad
 *      - Crear diferentes tipos de asteroides sin modificar GameState
 *      - Incluir sprites específicos en el futuro
 *
 * 6) Cohesión mejorada:
 *    La clase se centra exclusivamente en la creación parametrizada de asteroides.
 */
package game.factory;

import game.entities.Asteroid;
import game.entities.Fraction;
import game.strategy.FractionStrategy;
import java.util.Random;

/**
 *
 * @author lilli
 */
public class AsteroidFactory {

    // Random único → evita duplicación y cumple SRP
    private static Random rand = new Random();

    /**
     * Crea un nuevo Asteroid de manera desacoplada del GameState.
     * @param strategy Estrategia usada para generar fracciones (Patrón Strategy)
     * @param screenWidth Ancho de la pantalla
     * @param screenHeight Alto de la pantalla
     * @param difficultyMode 0 = Identificación, 1 = Equivalencias
     * @return Asteroid instanciado con valores aleatorios
     */
    public static Asteroid create(FractionStrategy strategy,
                                  int screenWidth,
                                  int screenHeight,
                                  int difficultyMode) {

        /*
         * El asteroide nace justo fuera de pantalla (x > width)
         * generación aleatoria para evitar patrones repetitivos.
         */
        int x = screenWidth + rand.nextInt(200);
        int y = rand.nextInt(screenHeight - 80);

        // Velocidad proporcional a la dificultad (se puede refinar si se desea)
        int speed = 2 + rand.nextInt(3);
        int radius = 45;

        // Delegamos la generación de fracciones a la estrategia (Pattern Strategy)
        Fraction f = strategy.generateRandomFraction();

        System.out.println("[AsteroidFactory] crea frac=" + f + " modo=" + difficultyMode);

        // El asteroide no nace marcado como objetivo, GameState se encargará
        boolean isTarget = false;

        // Se encapsula la construcción del objeto asi que cumple el Factory Pattern
        return new Asteroid(x, y, speed, radius, f, isTarget, difficultyMode);
    }
}