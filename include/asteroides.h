#ifndef ASTEROIDES_H
#define ASTEROIDES_H

#include "raylib.h"
#include "juego.h"

// Prototipos del módulo de asteroides

void inicializarAsteroides(Ts_asteroide asteroide[], Texture2D asteroideTextura[],
                           Texture2D identificacionTextures[], int maxAst, int screenWidth, int screenHeight);

void iniciaWave(Texture2D asteroideTextura[], Texture2D identificacionTextures[],
                int maxAst);

void actualiAsteroides(Ts_asteroide asteroide[], int maxAst, Rectangle playerRec,
                       Texture2D asteroideTextura[], Texture2D identificacionTextures[]);

void dibujarAsteroides(Ts_asteroide asteroide[], int maxAst,
                       Texture2D asteroideTextura[], Texture2D identificacionTextures[]);

bool asteroideCerca(Ts_asteroide ast, Ts_asteroide asts[], int index, int total);

#endif // ASTEROIDES_H
