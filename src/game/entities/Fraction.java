/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.entities;

/**
 *
 * @author lilli
 */
public class Fraction {

    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0)
            throw new IllegalArgumentException("Denominator cannot be zero.");

        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public boolean isExactlyEqual(Fraction other) {
        return this.numerator == other.numerator &&
               this.denominator == other.denominator;
    }

    public boolean isEquivalent(Fraction other) {
        return this.numerator * other.denominator ==
               this.denominator * other.numerator;
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
}