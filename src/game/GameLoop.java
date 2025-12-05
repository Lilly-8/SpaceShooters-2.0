/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

/**
 *
 * @author lilli
 */
public class GameLoop extends Thread {

    private boolean running = true;
    private final Runnable updateCallback;

    public GameLoop(Runnable updateCallback) {
        this.updateCallback = updateCallback;
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long frameTime = 1000 / FPS;

        while (running) {
            long start = System.currentTimeMillis();

            updateCallback.run();

            long elapsed = System.currentTimeMillis() - start;
            long sleep = frameTime - elapsed;

            if (sleep < 0) sleep = 2;

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void stopLoop() {
        running = false;
    }
}
