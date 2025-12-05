/*
 * Interfaz GameSubject
 *
 * Patrón OBSERVER:
 *   Define cómo un sujeto (en este caso GameState) gestiona a sus observadores.
 *
 * Code smell corregido:
 *   - Notificaciones manuales y repetidas desde GameState al HUD.
 *   - Dependencia directa entre GameState y HUD.
 *   - Ahora el HUD solo se registra y recibe notificaciones.
 *
 * SOLID:
 *   DIP: GameState NO depende de una implementación concreta.
 */
package game.observer;

/**
 *
 * @author lilli
 */
public interface GameSubject {
    void addObserver(GameObserver obs);
    void removeObserver(GameObserver obs);
    void notifyObservers();
}
