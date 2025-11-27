#ifndef JUEGO_H
#define JUEGO_H

#include "raylib.h"
#include <stdbool.h>

// Constantes 
#define NUM_DISPAROS 50
#define NUM_MAX_ASTEROIDES 10
#define OBJETIVOS_WAVE 5
#define MAX_WAVE 3
#define PUNTOS_OBJETIVO 100
#define PUNTOS_ERROR -10
#define ASTE_MIN_DISTANCIA 100

#define EQUIVALENCIA 1
#define IDENTIFICACION 0

#define ANCHO_BOTON 450
#define ALTO_BOTON 60

#define TAM_TEXT_BOTON 35

// Declaración de tipos y estructuras 
typedef struct _fraccion
{
    int numerador;
    int denominador;
} Ts_fraccion;

typedef struct _fraccionIdent
{
    Ts_fraccion fraccion;
    const char *rutaImagen; 
} Ts_fraccionIdent;

typedef struct _fraccionEquiv
{
    Ts_fraccion objetivo;
    Ts_fraccion equivalentes[3];
} Ts_fraccionEquiv;

typedef struct Ts_nave
{
    Rectangle rec;
    Vector2 velocidad;
    Color color;
    Texture2D texture;
} Ts_nave;

typedef struct _fondo
{
    Texture2D texture;
    float x;
    float velocidad;
} Ts_fondo;

typedef struct _circulo
{
    float x;
    float y;
    float radio;
} Ts_circulo;

typedef struct _asteroide
{
    Ts_circulo circulo;
    Vector2 velocidad;
    bool active;
    Color color;
    Ts_fraccion valor;
    char textoFrac[10];
    bool esObjetivo;
    Texture2D asteroideTextura; // Texturas para el asteroide
} Ts_asteroide;

typedef struct _disparo
{
    Rectangle rec;
    Vector2 velocidad;
    bool active;
    Color color;
} Ts_disparo;

typedef struct _boton
{
    Rectangle rec;
    const char *text;
    Color color;
} Ts_boton;

typedef struct _sonidos
{
    Sound disparoSound;
    Sound choqueSound;
    Sound correctoSound;
    Sound incorrectoSound;
    Sound victoriaSound;
    Sound gameOverSound;
    Sound fondoSound;
} Ts_sonidos;

typedef struct _estadoJuego
{
    int currentWave;
    int score;
    int targetsHit;
    bool gameOver;
    bool pause;
    bool victory;
    Ts_fraccion targetFraction;
    int currentDifficulty;
    char waveMessage[50];
    float messageAlpha;
    bool shouldFadeMessage;
    Ts_fondo background;
    int playerLives;
    bool lostLife;
    float lostLifeMessageTime;
    bool AudioReproducido;
} Ts_estadoJuego;

typedef struct _puntos
{
    char username[10];
    int score;
    int nivel; // nivel EQUIVALENCIA o IDENTIFICACION
} Ts_puntos;

// Variables globales 
extern const int screenWidth;
extern const int screenHeight;

extern Ts_nave player;
extern Ts_asteroide asteroide[NUM_MAX_ASTEROIDES];
extern Ts_disparo disparo[NUM_DISPAROS];
extern Ts_estadoJuego gameState;
extern Ts_puntos scores[2][20];
extern Ts_sonidos sonido;
extern bool sonidoReproducido;

// Prototipos de funciones principales del módulo juego
void inicializarJuego(Texture2D playerTexture, Texture2D asteroideTextura[],
                      Texture2D fondoTexture, Texture2D identificacionTextures[]);

void actualiJuego(Texture2D playerTexture, Texture2D asteroideTextura[],
                  Texture2D fondoTexture, Texture2D identificacionTextures[]);

void actualiFramePantalla(Texture2D playerTexture, Texture2D asteroideTextura[],
                          Texture2D fondoTexture, Texture2D identificacionTextures[]);

void dibujarJuego(Texture2D playerTexture, Texture2D asteroideTextura[],
                  Texture2D fondoTexture, Texture2D identificacionTextures[]);

#endif // JUEGO_H
