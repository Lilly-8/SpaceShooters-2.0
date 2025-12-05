/*
 * Strategy para modo de EQUIVALENCIAS.
 *
 * Patrón Strategy aplicado:
 *   Genera fracciones equivalentes y valida con multiplicación cruzada.
 *
 * SOLID:
 *   SRP: solo se encarga del modo equivalencia.
 *
 * Code smell corregido:
 *   - Lógica compleja incrustada en GameState.
 *   - Validación duplicada.
 *
 *   Ahora GameState solo invoca strategy.validate() sin saber cómo funciona.
 */
package game.strategy;

import game.entities.Fraction;
import java.util.Random;

/**
 *
 * @author lilli
 */
public class EquivalentFractionStrategy implements FractionStrategy {

    private Random rand = new Random();

    private static final Fraction[] EQUIVALENT_FRACTIONS = {

        // Equivalentes a 1/2
        new Fraction(2,4),
        new Fraction(3,6),
        new Fraction(4,8),
        new Fraction(5,10),

        // Equivalentes a 2/3
        new Fraction(4,6),
        new Fraction(6,9),
        new Fraction(8,12),

        // Equivalentes a 1/4
        new Fraction(2,8),
        new Fraction(3,12),
        new Fraction(4,16),

        // Equivalentes a 3/4
        new Fraction(6,8),
        new Fraction(9,12),
        new Fraction(12,16),

        // Equivalentes a 1/3
        new Fraction(2,6),
        new Fraction(3,9),
        new Fraction(4,12),

        // Equivalentes a 2/5
        new Fraction(4,10),
        new Fraction(6,15),
        new Fraction(8,20)
    };

    @Override
    public Fraction generateRandomFraction() {
        Fraction f = EQUIVALENT_FRACTIONS[rand.nextInt(EQUIVALENT_FRACTIONS.length)];
        System.out.println("[EquivalentStrategy DEBUG] genera: " + f);
        return f;
    }

    @Override
    public boolean validate(Fraction a, Fraction target) {
        return a.getNumerator() * target.getDenominator() ==
               a.getDenominator() * target.getNumerator();
    }
}