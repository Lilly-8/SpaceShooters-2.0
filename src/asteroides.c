#include "asteroides.h"
#include "fracciones.h"
#include "juego.h"
#include "raylib.h"
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

static float getRandomVelocity()
{
    return (float)(rand() % 3 + 2);
}

bool asteroideCerca(Ts_asteroide ast, Ts_asteroide asts[], int index, int total)
{
    for (int i = 0; i < total; i++)
    {
        if (i != index && asts[i].active)
        {
            float dx = ast.circulo.x - asts[i].circulo.x;
            float dy = ast.circulo.y - asts[i].circulo.y;
            float dist = sqrtf(dx * dx + dy * dy);

            if (dist < ASTE_MIN_DISTANCIA)
                return true;
        }
    }

    return false;
}

void inicializarAsteroides(Ts_asteroide asteroide[], Texture2D asteroideTextura[],
                           Texture2D identificacionTextures[], int maxAst, int screenWidth, int screenHeight)
{
    for (int i = 0; i < maxAst; i++)
    {
        asteroide[i].circulo.x = rand() % screenWidth;
        asteroide[i].circulo.y = -(rand() % screenHeight);
        asteroide[i].circulo.radio = 30;

        asteroide[i].velocidad.x = getRandomVelocity();
        asteroide[i].velocidad.y = getRandomVelocity();

        asteroide[i].active = true;

        asteroide[i].asteroideTextura = asteroideTextura[0];

        asteroide[i].valor.numerador = 1;
        asteroide[i].valor.denominador = 2;

        sprintf(asteroide[i].textoFrac, "%d/%d",
                asteroide[i].valor.numerador, asteroide[i].valor.denominador);
    }
}

void iniciaWave(Texture2D asteroideTextura[], Texture2D identificacionTextures[],
                int maxAst)
{
    gameState.messageAlpha = 1.0f;
    gameState.shouldFadeMessage = true;
    snprintf(gameState.waveMessage, 50, "Wave %d", gameState.currentWave);

    // ---- ESTABLECER LA FRACCION OBJETIVO ----
    Ts_fraccionEquiv waveFrac;

    if (gameState.currentDifficulty == IDENTIFICACION)
    {
        gameState.targetFraction = fraccionIdentRandom();
    }
    else // EQUIVALENCIA
    {
        waveFrac = fraccionEquivRandom();
        gameState.targetFraction = waveFrac.objetivo;
    }

    // ---- GENERAR ASTEROIDES ----
    for (int i = 0; i < maxAst; i++)
    {
        asteroide[i].circulo.x = rand() % screenWidth;
        asteroide[i].circulo.y = -(rand() % 800);

        asteroide[i].velocidad.x = getRandomVelocity();
        asteroide[i].velocidad.y = getRandomVelocity();
        asteroide[i].active = true;

        if (gameState.currentDifficulty == IDENTIFICACION)
        {
            int r = rand() % 7;
            asteroide[i].asteroideTextura = identificacionTextures[r];
            asteroide[i].esObjetivo = sonFraccionesIguales(asteroide[i].valor, gameState.targetFraction); 
            
        }
        else
        {
            asteroide[i].asteroideTextura = asteroideTextura[0];

            
            asteroide[i].valor = waveFrac.equivalentes[rand() % 3];

            sprintf(asteroide[i].textoFrac, "%d/%d",
                    asteroide[i].valor.numerador,
                    asteroide[i].valor.denominador);

            asteroide[i].esObjetivo = sonFraccionesEquiv(asteroide[i].valor, gameState.targetFraction) || sonFraccionesIguales(asteroide[i].valor, gameState.targetFraction);
        }

        // Evitar asteroides muy cerca
        while (asteroideCerca(asteroide[i], asteroide, i, maxAst))
        {
            asteroide[i].circulo.x = rand() % screenWidth;
            asteroide[i].circulo.y = -(rand() % 800);
        }
    }
}



void actualiAsteroides(Ts_asteroide asteroide[], int maxAst, Rectangle playerRec,
                       Texture2D asteroideTextura[], Texture2D identificacionTextures[])
{
    for (int i = 0; i < maxAst; i++)
    {
        if (!asteroide[i].active)
            continue;

        asteroide[i].circulo.x += asteroide[i].velocidad.x;
        asteroide[i].circulo.y += asteroide[i].velocidad.y;

        if (asteroide[i].circulo.x > screenWidth + 50 ||
            asteroide[i].circulo.y > screenHeight + 50)
        {
            asteroide[i].circulo.x = rand() % screenWidth;
            asteroide[i].circulo.y = -(rand() % 800);

            asteroide[i].velocidad.x = getRandomVelocity();
            asteroide[i].velocidad.y = getRandomVelocity();
        }

        Rectangle astRect = {
            .x = asteroide[i].circulo.x - asteroide[i].circulo.radio,
            .y = asteroide[i].circulo.y - asteroide[i].circulo.radio,
            .width = asteroide[i].circulo.radio * 2,
            .height = asteroide[i].circulo.radio * 2};

        if (CheckCollisionRecs(astRect, playerRec))
        {
            if (!gameState.gameOver)
            {
                PlaySound(sonido.choqueSound);

                gameState.playerLives--;

                if (gameState.playerLives <= 0)
                {
                    gameState.gameOver = true;
                }
                else
                {
                    gameState.lostLife = true;
                    gameState.lostLifeMessageTime = 1.5f;
                }

                asteroide[i].circulo.x = rand() % screenWidth;
                asteroide[i].circulo.y = -(rand() % 800);
            }
        }
    }
}

void dibujarAsteroides(Ts_asteroide asteroide[], int maxAst,
                       Texture2D asteroideTextura[], Texture2D identificacionTextures[])
{
    for (int i = 0; i < maxAst; i++)
    {
        if (!asteroide[i].active)
            continue;

        DrawTexture(
            asteroide[i].asteroideTextura,
            asteroide[i].circulo.x - asteroide[i].circulo.radio,
            asteroide[i].circulo.y - asteroide[i].circulo.radio,
            WHITE);

        if (gameState.currentDifficulty == EQUIVALENCIA)
        {
            DrawText(asteroide[i].textoFrac,
                     asteroide[i].circulo.x - 10, asteroide[i].circulo.y,
                     20, YELLOW);
        }
    }
}
