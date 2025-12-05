/*
 * Interfaz GameObserver
 *
 * Patrón aplicado: OBSERVER
 *   Define el método que ejecutarán los observadores cuando el estado del
 *   juego cambie.
 *
 * Code smell corregido:
 *   - El HUD antes estaba acoplado directamente al GameState.
 *     Era necesario modificar GameState para actualizar el HUD.
 *   - Se eliminó este acoplamiento usando Observer.
 *
 * SOLID:
 *   Cumple el principio OCP:
 *   Se pueden agregar nuevos observadores sin modificar GameState.
 */
package game.observer;

import game.GameState;

/**
 *
 * @author lilli
 */
public interface GameObserver {
    void onGameStateChanged(GameState state);
}