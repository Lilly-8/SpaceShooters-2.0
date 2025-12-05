/*
 * Strategy para modo de IDENTIFICACIÓN.
 *
 * Aplicación del patrón Strategy:
 *   Genera fracciones simples y valida por igualdad exacta.
 *
 * SOLID:
 *   Cumple SRP: solo maneja fracciones del modo identificación.
 *
 * Code smell eliminado:
 *   Antes, GameState tenía lógica mezclada para ambos modos.
 *   Ahora cada estrategia se separa correctamente.
 */
package game.strategy;

import game.entities.Fraction;
import java.util.Random;

/**
 *
 * @author lilli
 */
public class IdentificationFractionStrategy implements FractionStrategy {

    private Random rand = new Random();

    // Lista fija de fracciones disponibles
    private static final Fraction[] FRACTIONS = {
        new Fraction(1,1),
        new Fraction(1,2),
        new Fraction(1,3),
        new Fraction(1,4),
        new Fraction(1,8),
        new Fraction(2,3),
        new Fraction(3,4)
    };

    @Override
    public Fraction generateRandomFraction() {
        return FRACTIONS[rand.nextInt(FRACTIONS.length)];
    }

    @Override
    public boolean validate(Fraction a, Fraction target) {
        return a.getNumerator() == target.getNumerator() &&
               a.getDenominator() == target.getDenominator();
    }
}