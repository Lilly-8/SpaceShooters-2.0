#ifndef PANTALLAS_H
#define PANTALLAS_H

#include "raylib.h"
#include "juego.h"

// Prototipos de las pantallas principales
void pantallaInicio(Texture2D playerTexture, Texture2D asteroideTextura[],
                     Texture2D fondoTexture, Texture2D identificacionTextures[]);

void pantallaPuntuaciones(Texture2D playerTexture, Texture2D asteroideTextura[],
                          Texture2D fondoTexture, Texture2D identificacionTextures[]);

void pantallaControles(Texture2D playerTexture, Texture2D asteroideTextura[],
                       Texture2D fondoTexture, Texture2D identificacionTextures[]);

void pantallaDificultad(Texture2D playerTexture, Texture2D asteroideTextura[],
                        Texture2D fondoTexture, Texture2D identificacionTextures[]);

void pantallaGameOver(Texture2D playerTexture, Texture2D asteroideTextura[],
                      Texture2D fondoTexture, Texture2D identificacionTextures[]);

void pantallaGuardarPuntos(void);

#endif // PANTALLAS_H
