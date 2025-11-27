#ifndef DISPAROS_H
#define DISPAROS_H

#include "raylib.h"
#include "juego.h"

// Prototipos del módulo de disparos
void actualiDisparos(Ts_disparo disparo[], int maxDisp,
                     Ts_asteroide asteroide[], int maxAst);

void dibujarDisparos(Ts_disparo disparo[], int maxDisp);

#endif // DISPAROS_H
