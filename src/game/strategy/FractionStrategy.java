/*
 * Interfaz FractionStrategy
 *
 * Patrón aplicado: STRATEGY
 *   Esta interfaz define el comportamiento común para validar fracciones y
 *   generar fracciones según el modo de juego.
 *
 * Code smell corregido:
 *   - Condicionales extensos en GameState para decidir cómo validar/generar.
 *     → Se reemplazó por polimorfismo (Strategy Pattern).
 *
 * Beneficio:
 *   GameState no depende de detalles ni usa if/else. Solo llama a la estrategia.
 */
package game.strategy;

import game.entities.Fraction;

/**
 *
 * @author lilli
 */
public interface FractionStrategy {

    // Validate fraction based on mode (equivalence or identification)
    boolean validate(Fraction asteroidFrac, Fraction targetFrac);

    // Generate random fraction depending on difficulty
    Fraction generateRandomFraction();
}