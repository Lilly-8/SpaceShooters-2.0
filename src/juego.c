#include "juego.h"
#include "fracciones.h"    // funciones de fracciones (vendrán en tandas siguientes)
#include "asteroides.h"   // inicializarAsteroides, actualiAsteroides, dibujarAsteroides
#include "disparos.h"     // actualiDisparos, dibujarDisparos
#include "pantallas.h"    // pantallaGameOver, etc.
#include "fondo.h"        // MoverFondo
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>


extern const int screenWidth;
extern const int screenHeight;

// Definición de variables globales
Ts_nave player = {0};
Ts_asteroide asteroide[NUM_MAX_ASTEROIDES] = {0};
Ts_disparo disparo[NUM_DISPAROS] = {0};
Ts_estadoJuego gameState = {0};
Ts_puntos scores[2][20] = {0};
extern Ts_sonidos sonido;
extern bool sonidoReproducido;

// Contador para la tasa de disparo (se usaba globalmente)
int shootRate = 0;



void inicializarJuego(Texture2D playerTexture, Texture2D asteroideTextura[],
                      Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    // Inicializar variables del juego
    gameState.currentWave = 1;
    gameState.score = 0;
    gameState.targetsHit = 0;
    gameState.gameOver = false;
    gameState.pause = false;
    gameState.victory = false;

    // Inicializar nave
    player.rec.x = screenWidth / 2;
    player.rec.y = screenHeight - 50;
    player.rec.width = 50;
    player.rec.height = 50;
    player.velocidad.x = 5; // Velocidad horizontal
    player.velocidad.y = 5; // Velocidad vertical
    player.color = WHITE;
    player.texture = playerTexture;

    // Inicializar vidas
    gameState.playerLives = 5;
    gameState.lostLife = false;
    gameState.lostLifeMessageTime = 0.0f;

    // Inicializar asteroides (esta función está en asteroides.c)
    inicializarAsteroides(asteroide, asteroideTextura, identificacionTextures, NUM_MAX_ASTEROIDES, screenWidth, screenHeight);

    // Inicializar disparos
    for (int i = 0; i < NUM_DISPAROS; i++)
    {
        disparo[i].rec.x = 0;
        disparo[i].rec.y = 0;
        disparo[i].rec.width = 5;   // Ancho del disparo
        disparo[i].rec.height = 10; // Alto del disparo
        disparo[i].velocidad.x = 5; // Velocidad horizontal del disparo
        disparo[i].velocidad.y = 0; // Velocidad vertical del disparo
        disparo[i].active = false;  // El disparo no está activo al principio
        disparo[i].color = MAROON;  // Color del disparo
    }

    // Inicializar fondo
    gameState.background.texture = fondoTexture;
    gameState.background.x = 0;
    gameState.background.velocidad = 100.0f;

    gameState.messageAlpha = 1.0f;
    gameState.shouldFadeMessage = false;

    // Iniciar la primera wave
    iniciaWave(asteroideTextura, identificacionTextures, NUM_MAX_ASTEROIDES);
}

void actualiJuego(Texture2D playerTexture, Texture2D asteroideTextura[],
                  Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    if (!gameState.gameOver && !gameState.victory)
    {
        // Actualizar solo la posición del fondo (NO dibujar aquí)
        gameState.background.x -= gameState.background.velocidad * GetFrameTime();
        if (gameState.background.x <= -gameState.background.texture.width)
        {
            gameState.background.x = 0;
        }


        if (IsKeyPressed('P'))
            gameState.pause = !gameState.pause;

        if (!gameState.pause)
        {
            if (gameState.lostLife)
            {
                gameState.lostLifeMessageTime -= GetFrameTime();
                if (gameState.lostLifeMessageTime <= 0.0f)
                {
                    gameState.lostLife = false;
                }
            }

            // Update message alpha
            if (gameState.shouldFadeMessage)
            {
                gameState.messageAlpha -= 0.02f;
                if (gameState.messageAlpha <= 0.0f)
                {
                    gameState.messageAlpha = 0.0f;
                    gameState.shouldFadeMessage = false;
                }
            }

            // Movimiento de la nave
            if (IsKeyDown(KEY_RIGHT))
            {
                player.rec.x += player.velocidad.x;
            }
            if (IsKeyDown(KEY_LEFT))
            {
                player.rec.x -= player.velocidad.x;
            }
            if (IsKeyDown(KEY_UP))
            {
                player.rec.y -= player.velocidad.y;
            }
            if (IsKeyDown(KEY_DOWN))
            {
                player.rec.y += player.velocidad.y;
            }

            // Evitar que la nave salga de la ventana
            if (player.rec.x < 0)
                player.rec.x = 0;
            if (player.rec.x + player.rec.width > screenWidth)
                player.rec.x = screenWidth - player.rec.width;
            if (player.rec.y < 0)
                player.rec.y = 0;
            if (player.rec.y + player.rec.height > screenHeight)
                player.rec.y = screenHeight - player.rec.height;

            // Disparos
            if (IsKeyDown(KEY_SPACE))
            {
                PlaySound(sonido.disparoSound);
                shootRate += 5;
                for (int i = 0; i < NUM_DISPAROS; i++)
                {
                    if (!disparo[i].active && shootRate % 20 == 0)
                    {
                        disparo[i].rec.x = player.rec.x + player.rec.width;
                        disparo[i].rec.y = player.rec.y + player.rec.height / 2;
                        disparo[i].active = true;
                        break;
                    }
                }
            }

            // Actualizar asteroides (función en asteroides.c)
            if (gameState.currentDifficulty == EQUIVALENCIA)
            {
                actualiAsteroides(asteroide, NUM_MAX_ASTEROIDES, player.rec, asteroideTextura, identificacionTextures);
            }
            else
            {
                if (gameState.currentDifficulty == IDENTIFICACION)
                {
                    actualiAsteroides(asteroide, NUM_MAX_ASTEROIDES, player.rec, asteroideTextura, identificacionTextures);
                }
            }

            // Actualizar disparos y checar colisiones (función en disparos.c)
            actualiDisparos(disparo, NUM_DISPAROS, asteroide, NUM_MAX_ASTEROIDES);

            // Verificar si el wave ha terminado
            if (gameState.targetsHit >= OBJETIVOS_WAVE)
            {
                if (gameState.currentWave < MAX_WAVE)
                {
                    gameState.currentWave++;
                    gameState.targetsHit = 0;
                    // Start new wave (usa iniciaWave)
                    if (gameState.currentDifficulty == EQUIVALENCIA)
                    {
                        iniciaWave(asteroideTextura, asteroideTextura, NUM_MAX_ASTEROIDES);
                    }
                    else
                    {
                        if (gameState.currentDifficulty == IDENTIFICACION)
                        {
                            iniciaWave(asteroideTextura, identificacionTextures, NUM_MAX_ASTEROIDES);
                        }
                    }
                }
                else
                {
                    gameState.victory = true;
                }
            }
        }
        else
        {
            if (gameState.gameOver || gameState.victory)
            {
                pantallaGameOver(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
            }
        }
    }
}

void actualiFramePantalla(Texture2D playerTexture, Texture2D asteroideTextura[],
                          Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    actualiJuego(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
    dibujarJuego(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
}

void dibujarJuego(Texture2D playerTexture, Texture2D asteroideTextura[],
                  Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    BeginDrawing();
    ClearBackground(RAYWHITE);

    if (!gameState.gameOver && !gameState.victory)
    {
        MoverFondo();

        if (gameState.currentDifficulty == IDENTIFICACION)
        {
            DrawText(
                TextFormat("Encuentra la fraccion igual a: %d/%d",
                        gameState.targetFraction.numerador,
                        gameState.targetFraction.denominador),
                20, 80, 35, WHITE);
        }
        else if (gameState.currentDifficulty == EQUIVALENCIA)
        {
            DrawText(
                TextFormat("Encuentra una fraccion equivalente a: %d/%d",
                        gameState.targetFraction.numerador,
                        gameState.targetFraction.denominador),
                20, 80, 35, WHITE);
        }


        // Draw player
        DrawTexture(playerTexture, player.rec.x, player.rec.y, WHITE);

        // Draw enemies (función en asteroides.c)
        dibujarAsteroides(asteroide, NUM_MAX_ASTEROIDES, asteroideTextura, identificacionTextures);

        // Draw shoots (función en disparos.c)
        dibujarDisparos(disparo, NUM_DISPAROS);

        // Draw HUD
        DrawText(TextFormat("Puntuación: %04i", gameState.score), 20, 20, 20, GRAY);
        DrawText(TextFormat("Wave: %d", gameState.currentWave), 20, 50, 20, GRAY);
        DrawText(TextFormat("Objetivos disparados: %d/%d", gameState.targetsHit, OBJETIVOS_WAVE), 20, 110, 20, GRAY);
        DrawText(TextFormat("Vidas: %d", gameState.playerLives), 20, 140, 20, GRAY);

        if (gameState.lostLife)
        {
            DrawText("¡Te equivocaste!", screenWidth / 2 - MeasureText("¡Te equivocaste!", 20) / 2,
                     screenHeight / 2, 20, RED);
        }

        if (gameState.messageAlpha > 0.0f)
        {
            DrawText(gameState.waveMessage,
                     screenWidth / 2 - MeasureText(gameState.waveMessage, 30) / 2,
                     50, 30, Fade(RAYWHITE, gameState.messageAlpha));
        }

        if (gameState.pause)
        {
            DrawText("PAUSA", screenWidth / 2 - MeasureText("PAUSA", 40) / 2,
                     screenHeight / 2 - 40, 40, GRAY);
        }
    }
    else
    {
        pantallaGameOver(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
    }

    EndDrawing();
}
