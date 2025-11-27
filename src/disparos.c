#include "disparos.h"
#include "juego.h"
#include "asteroides.h"
#include <stdio.h>
#include <math.h>
#include <fracciones.h>

void actualiDisparos(Ts_disparo disparo[], int maxDisp,
                     Ts_asteroide asteroide[], int maxAst)
{
    for (int i = 0; i < maxDisp; i++)
    {
        if (!disparo[i].active)
            continue;

        disparo[i].rec.x += disparo[i].velocidad.x;

        if (disparo[i].rec.x > screenWidth || disparo[i].rec.x < 0)
        {
            disparo[i].active = false;
            continue;
        }

        for (int j = 0; j < maxAst; j++)
        {
            if (!asteroide[j].active)
                continue;

            float dx = disparo[i].rec.x - asteroide[j].circulo.x;
            float dy = disparo[i].rec.y - asteroide[j].circulo.y;
            float dist = sqrtf(dx * dx + dy * dy);

            if (dist < asteroide[j].circulo.radio)
            {
                bool correcto = false;

                if (gameState.currentDifficulty == EQUIVALENCIA)
                {
                    correcto = sonFraccionesEquiv(
                        asteroide[j].valor,
                        gameState.targetFraction);
                }
                else
                {
                    correcto = sonFraccionesIguales(
                        asteroide[j].valor,
                        gameState.targetFraction);
                }

                if (correcto)
                {
                    PlaySound(sonido.correctoSound);
                    gameState.score += PUNTOS_OBJETIVO;
                    asteroide[j].active = false;
                    gameState.targetsHit++;
                }
                else
                {
                    PlaySound(sonido.incorrectoSound);
                    gameState.score += PUNTOS_ERROR;

                    gameState.playerLives--;
                    gameState.lostLife = true;
                    gameState.lostLifeMessageTime = 1.5f;

                    if (gameState.playerLives <= 0)
                    {
                        gameState.gameOver = true;
                    }
                }

                disparo[i].active = false;
                break;
            }
        }
    }
}

void dibujarDisparos(Ts_disparo disparo[], int maxDisp)
{
    for (int i = 0; i < maxDisp; i++)
    {
        if (disparo[i].active)
        {
            DrawRectangleRec(disparo[i].rec, disparo[i].color);
        }
    }
}
