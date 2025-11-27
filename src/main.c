#include "raylib.h"
#include <stdio.h>
#include <unistd.h>

#include "juego.h"
#include "pantallas.h"
#include "fondo.h"
#include "puntuaciones.h"
#include "botones.h"


const int screenWidth = 1000;
const int screenHeight = 800;


Ts_sonidos sonido = {0};

// bandera para sonido GameOver
bool sonidoReproducido = false;

int main()
{
    InitAudioDevice();
    InitWindow(screenWidth, screenHeight, "Math Shooter: fracciones espaciales");

    if (access("scores.dll", F_OK) == -1)
    {
        FILE *scoresFile = fopen("scores.dll", "w");
        if (scoresFile != NULL)
            fclose(scoresFile);
        else
            printf("Error al crear el archivo scores.dll\n");
    }

    // Cargar texturas
    Texture2D playerTexture = LoadTexture("resources/nave.png");

    Texture2D asteroideTextura[1];
    asteroideTextura[0] = LoadTexture("resources/asteroidesEq/asteroide.png");

    Texture2D fondoTexture = LoadTexture("resources/fondo.png");

    Texture2D identificacionTextures[] = {
        LoadTexture("resources/asteroidesIdent/asteroide_1_2.png"),
        LoadTexture("resources/asteroidesIdent/asteroide_1_3.png"),
        LoadTexture("resources/asteroidesIdent/asteroide_1_4.png"),
        LoadTexture("resources/asteroidesIdent/asteroide_1_8.png"),
        LoadTexture("resources/asteroidesIdent/asteroide_2_3.png"),
        LoadTexture("resources/asteroidesIdent/asteroide_3_4.png"),
        LoadTexture("resources/asteroidesIdent/asteroide_1_1.png")};

    // Cargar sonidos
    sonido.fondoSound = LoadSound("resources/sonidos/fondo.wav");
    sonido.gameOverSound = LoadSound("resources/sonidos/GameOver.wav");
    sonido.victoriaSound = LoadSound("resources/sonidos/victoria.mp3");
    sonido.disparoSound = LoadSound("resources/sonidos/disparo.wav");
    sonido.choqueSound = LoadSound("resources/sonidos/choque.mp3");
    sonido.correctoSound = LoadSound("resources/sonidos/correcto.mp3");
    sonido.incorrectoSound = LoadSound("resources/sonidos/incorrecto.wav");

    SetTargetFPS(60);

    // Reproducir sonido de fondo
    PlaySound(sonido.fondoSound);

    // Inicializar juego y mostrar la pantalla inicial
    inicializarJuego(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);

    pantallaInicio(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);

    while (!WindowShouldClose())
    {
        // Actualizar el juego
        actualiFramePantalla(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
    }

    // Descargar texturas
    UnloadTexture(playerTexture);
    UnloadTexture(asteroideTextura[0]);
    UnloadTexture(fondoTexture);

    for (int i = 0; i < 7; i++)
        UnloadTexture(identificacionTextures[i]);

    // Descargar sonidos
    UnloadSound(sonido.fondoSound);
    UnloadSound(sonido.gameOverSound);
    UnloadSound(sonido.victoriaSound);
    UnloadSound(sonido.disparoSound);
    UnloadSound(sonido.choqueSound);
    UnloadSound(sonido.correctoSound);
    UnloadSound(sonido.incorrectoSound);

    CloseAudioDevice();
    CloseWindow();

    return 0;
}
