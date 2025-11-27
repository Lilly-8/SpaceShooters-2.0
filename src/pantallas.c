#include "pantallas.h"
#include "botones.h"
#include "fondo.h"
#include "puntuaciones.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// pantallaInicio
void pantallaInicio(Texture2D playerTexture, Texture2D asteroideTextura[],
                    Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    Texture2D titulo = LoadTexture("resources/titulo.png");

    // Definir el ancho de los botones
    const int buttonWidth = ANCHO_BOTON; // Ancho de los botones
    const int buttonHeight = ALTO_BOTON; // Alto de los botones

    // Calcular la posición X centrada
    int buttonX = (screenWidth - buttonWidth) / 2;

    // Crear botones
    Ts_boton difficultyButton = {
        .rec = {buttonX, 300, buttonWidth, buttonHeight},
        .text = "JUGAR",
        .color = PURPLE};

    Ts_boton scoresButton = {
        .rec = {buttonX, 390, buttonWidth, buttonHeight},
        .text = "Puntuaciones",
        .color = PURPLE};

    Ts_boton controlsButton = {
        .rec = {buttonX, 480, buttonWidth, buttonHeight},
        .text = "Controles",
        .color = PURPLE};

    Ts_boton exitButton = {
        .rec = {buttonX, 570, buttonWidth, buttonHeight},
        .text = "Salir",
        .color = PURPLE};

    bool salir = false;

    while (true)
    {

        BeginDrawing();
        ClearBackground(RAYWHITE);

        // Dibujar y mover fondo
        MoverFondo();

        // Dibujar título del menú
        DrawTexture(titulo, (screenWidth - titulo.width) / 2, 100, WHITE);
        // Dibujar botones
        dibujarBoton(difficultyButton);
        dibujarBoton(scoresButton);
        dibujarBoton(controlsButton);
        dibujarBoton(exitButton);

        const char *nombre1 = "Universidad Autonoma de Baja California";
        int textoIzquierdaX = 10;
        int textoIzquierdaY = screenHeight - 30;
        DrawText(nombre1, textoIzquierdaX, textoIzquierdaY, 20, DARKGRAY);

        const char *nombre2 = "Programacion estructurada";
        int textoIzquierdaY2 = textoIzquierdaY - 25;
        DrawText(nombre2, textoIzquierdaX, textoIzquierdaY2, 20, DARKGRAY);

        const char *nombre3 = "Mykytuk Ayvar Fanny Lillian";
        int textoDerechaX = screenWidth - MeasureText(nombre3, 20) - 10;
        int textoDerechaY = screenHeight - 30;
        DrawText(nombre3, textoDerechaX, textoDerechaY, 20, DARKGRAY);

        const char *nombre4 = "Lopez Cisneros Isis Vanesa";
        int textoDerechaY2 = textoDerechaY - 25;
        DrawText(nombre4, textoDerechaX, textoDerechaY2, 20, DARKGRAY);

        EndDrawing();

        // Comprobar si se hace clic en los botones
        if (IsMouseButtonPressed(MOUSE_LEFT_BUTTON))
        {
            // Botón "Selección de dificultad"
            if (botonClick(difficultyButton))
            {
                pantallaDificultad(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                break;
            }
            else
            {
                if (botonClick(scoresButton))
                {
                    pantallaPuntuaciones(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                    break;
                }
                else
                {
                    if (botonClick(controlsButton))
                    {
                        pantallaControles(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                        break;
                    }
                    else
                    {
                        if (botonClick(exitButton))
                        {
                            CloseWindow();
                            exit(0);
                        }
                    }
                }
            }
        }
    }
    UnloadTexture(titulo);
}

// pantallaPuntuaciones
void pantallaPuntuaciones(Texture2D playerTexture, Texture2D asteroideTextura[],
                          Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    Texture2D titulo = LoadTexture("resources/titulopuntuaciones.png");

    const int buttonWidth = ANCHO_BOTON; // Ancho de los botones
    const int buttonHeight = ALTO_BOTON; // Alto de los botones

    // Calcular la posición X centrada
    int buttonX = (screenWidth - buttonWidth) / 2;

    // Crear botón
    Ts_boton backButton = {
        .rec = {buttonX, 650, buttonWidth, buttonHeight},
        .text = "Volver al menu",
        .color = PURPLE};

    // Abrir el archivo binario
    FILE *file = fopen("scores.dll", "r+b");
    if (file == NULL)
    {
        printf("Error al abrir el archivo\n");
        // Aún así muestra la pantalla con "Sin puntuaciones"
    }

    // Leer las puntuaciones del archivo
    Ts_puntos fileScores[20];
    int numScores = 0;
    if (file != NULL)
    {
        for (int i = 0; i < 20; i++)
        {
            if (fread(&fileScores[i], sizeof(Ts_puntos), 1, file) != 1)
            {
                break;
            }
            numScores++;
        }
        fclose(file);
    }

    // Ordenar las puntuaciones
    for (int i = 0; i < numScores; i++)
    {
        for (int j = i + 1; j < numScores; j++)
        {
            if (fileScores[i].score < fileScores[j].score)
            {
                Ts_puntos temp = fileScores[i];
                fileScores[i] = fileScores[j];
                fileScores[j] = temp;
            }
        }
    }

    while (true)
    {
        BeginDrawing();
        ClearBackground(RAYWHITE);

        MoverFondo();

        // Dibujar título
        DrawTexture(titulo, (screenWidth - titulo.width) / 2, 100, WHITE);

        // Dibujar encabezado de la tabla
        DrawText("NIVEL", 200, 250, 20, PINK);
        DrawText("NOMBRE", 400, 250, 20, PINK);
        DrawText("PUNTUACION", 600, 250, 20, PINK);

        // Dibujar puntuaciones
        if (numScores > 0)
        {
            for (int i = 0; i < numScores; i++)
            {
                char *nivelStr = (fileScores[i].nivel == 0) ? "Identificacion" : "Equivalencia";

                DrawText(nivelStr, 200, 300 + (i * 20), 20, PINK);
                DrawText(fileScores[i].username, 400, 300 + (i * 20), 20, PINK);
                DrawText(TextFormat("%d", fileScores[i].score), 600, 300 + (i * 20), 20, PINK);
            }
        }
        else
        {
            DrawText("Sin puntuaciones", 300, 300, 20, PINK);
        }

        dibujarBoton(backButton);

        EndDrawing();

        if (IsMouseButtonPressed(MOUSE_LEFT_BUTTON))
        {
            if (botonClick(backButton))
            {
                pantallaInicio(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                break;
            }
        }
    }
    UnloadTexture(titulo);
}

// pantallaControles
void pantallaControles(Texture2D playerTexture, Texture2D asteroideTextura[],
                       Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    Texture2D titulo = LoadTexture("resources/titulocontroles.png");

    const int buttonWidth = ANCHO_BOTON;
    const int buttonHeight = ALTO_BOTON;

    int buttonX = (screenWidth - buttonWidth) / 2;

    Ts_boton backButton = {
        .rec = {buttonX, 650, buttonWidth, buttonHeight},
        .text = "Volver al menu",
        .color = PURPLE};

    // Cargar imágenes de flecha
    Texture2D flechaAbajo = LoadTexture("resources/teclas/abajo.png");
    Texture2D flechaArriba = LoadTexture("resources/teclas/arriba.png");
    Texture2D flechaIzquierda = LoadTexture("resources/teclas/izquierda.png");
    Texture2D flechaDerecha = LoadTexture("resources/teclas/derecha.png");

    while (true)
    {
        BeginDrawing();
        ClearBackground(RAYWHITE);

        MoverFondo();

        // Dibujar título de la pantalla de controles
        DrawTexture(titulo, (screenWidth - titulo.width) / 2, 100, WHITE);

        // Dibujar controles
        DrawText("Mover la nave:", 100, 300, 30, PINK);
        DrawText("Teclas de control", 400, 300, 30, PURPLE);
        DrawTexture(flechaDerecha, 750, 300, WHITE);
        DrawTexture(flechaIzquierda, 700, 300, WHITE);
        DrawTexture(flechaArriba, 725, 270, WHITE);
        DrawTexture(flechaAbajo, 725, 330, WHITE);

        DrawText("Disparar:", 100, 430, 30, PINK);
        DrawText("Tecla spacio", 400, 430, 30, PURPLE);

        DrawText("Pausa y reanudar:", 100, 530, 30, PINK);
        DrawText("Tecla 'P'", 400, 530, 30, PURPLE);

        dibujarBoton(backButton);

        EndDrawing();

        if (IsMouseButtonPressed(MOUSE_LEFT_BUTTON))
        {
            if (botonClick(backButton))
            {
                // Descargar imágenes de flecha
                UnloadTexture(flechaAbajo);
                UnloadTexture(flechaArriba);
                UnloadTexture(flechaIzquierda);
                UnloadTexture(flechaDerecha);

                pantallaInicio(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                break;
            }
        }
    }
    UnloadTexture(titulo);
}

// pantallaDificultad
void pantallaDificultad(Texture2D playerTexture, Texture2D asteroideTextura[],
                        Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    Texture2D titulo = LoadTexture("resources/titulodificultades.png");

    const int buttonWidth = ANCHO_BOTON;
    const int buttonHeight = ALTO_BOTON;

    int buttonX = (screenWidth - buttonWidth) / 2;

    Ts_boton identificationButton = {
        .rec = {buttonX, 300, buttonWidth, buttonHeight},
        .text = "Identificacion",
        .color = PURPLE};

    Ts_boton equivalenceButton = {
        .rec = {buttonX, 390, buttonWidth, buttonHeight},
        .text = "Equivalencia",
        .color = PURPLE};

    Ts_boton backButton = {
        .rec = {buttonX, 480, buttonWidth, buttonHeight},
        .text = "Inicio",
        .color = PURPLE};

    while (true)
    {
        BeginDrawing();
        ClearBackground(RAYWHITE);

        MoverFondo();

        DrawTexture(titulo, (screenWidth - titulo.width) / 2, 100, WHITE);

        dibujarBoton(identificationButton);
        dibujarBoton(equivalenceButton);
        dibujarBoton(backButton);

        EndDrawing();

        if (IsMouseButtonPressed(MOUSE_LEFT_BUTTON))
        {
            // Botón "Identificación"
            if (botonClick(identificationButton))
            {
                gameState.currentDifficulty = IDENTIFICACION;
                inicializarJuego(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                break;
            }
            else
            {
                // Botón "Equivalencia"
                if (botonClick(equivalenceButton))
                {
                    gameState.currentDifficulty = EQUIVALENCIA;
                    inicializarJuego(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                    break;
                }
                else
                {
                    // Botón "Volver al menu principal"
                    if (botonClick(backButton))
                    {
                        pantallaInicio(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                        break;
                    }
                }
            }
        }
    }
    UnloadTexture(titulo);
}

// pantallaGameOver
void pantallaGameOver(Texture2D playerTexture, Texture2D asteroideTextura[],
                      Texture2D fondoTexture, Texture2D identificacionTextures[])
{
    const int buttonWidth = ANCHO_BOTON;
    const int buttonHeight = ALTO_BOTON;

    // Calcular la posición X centrada
    int buttonX = (screenWidth - buttonWidth) / 2;

    // Crear botones
    Ts_boton playAgainButton = {
        .rec = {buttonX, 400, buttonWidth, buttonHeight},
        .text = "JUGAR DENUEVO",
        .color = PURPLE};

    Ts_boton difficultyMenuButton = {
        .rec = {buttonX, 490, buttonWidth, buttonHeight},
        .text = "Menu de Dificultad",
        .color = PURPLE};

    Ts_boton saveScoreButton = {
        .rec = {buttonX, 580, buttonWidth, buttonHeight},
        .text = "Guardar Puntuacion",
        .color = PURPLE};

    if (!sonidoReproducido)
    {
        if (gameState.gameOver)
        {
            PlaySound(sonido.gameOverSound);
            sonidoReproducido = true;
        }
        else
        {
            if (gameState.victory)
            {
                PlaySound(sonido.victoriaSound);
                sonidoReproducido = true;
            }
        }
    }

    while (true)
    {
        BeginDrawing();
        ClearBackground(RAYWHITE);

        DrawTexture(gameState.background.texture, gameState.background.x, 0, WHITE);
        DrawTexture(gameState.background.texture, (gameState.background.x + gameState.background.texture.width), 0, WHITE);

        DrawText(gameState.victory ? "¡GANASTE!" : "GAME OVER", (screenWidth - MeasureText(gameState.victory ? "¡GANASTE!" : "GAME OVER", 40)) / 2, 200, 40, gameState.victory ? GREEN : RED);

        DrawText(TextFormat("Puntuacion Final: %04i", gameState.score), (screenWidth - MeasureText("Puntuacion Final: 0000", 30)) / 2, 300, 30, GRAY);

        dibujarBoton(playAgainButton);
        dibujarBoton(difficultyMenuButton);
        dibujarBoton(saveScoreButton);

        EndDrawing();

        // Comprobar si se hace clic en los botones
        if (IsMouseButtonPressed(MOUSE_LEFT_BUTTON))
        {
            sonidoReproducido = false;

            // Botón "Jugar de Nuevo"
            if (botonClick(playAgainButton))
            {
                inicializarJuego(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                break;
            }
            else
            {
                if (botonClick(difficultyMenuButton))
                {
                    pantallaDificultad(playerTexture, asteroideTextura, fondoTexture, identificacionTextures);
                    break;
                }
                else
                {
                    if (botonClick(saveScoreButton))
                    {
                        pantallaGuardarPuntos();
                    }
                }
            }
        }
    }
}

// pantallaGuardarPuntos
void pantallaGuardarPuntos()
{
    // Pedir el nombre del usuario
    Ts_puntos score;

    char inputTexto[10];
    int inputTextoLargo = 0;
    bool textoVacio = true; // Variable para verificar si el campo está vacío

    while (true)
    {
        BeginDrawing();
        ClearBackground(RAYWHITE);

        DrawTexture(gameState.background.texture, gameState.background.x, 0, WHITE);
        DrawTexture(gameState.background.texture, (gameState.background.x + gameState.background.texture.width), 0, WHITE);

        DrawText("Ingrese su nombre:", screenWidth / 2 - 100, screenHeight / 2 - 50, 20, PINK);

        // Mostrar '?' si el campo está vacío y el usuario no ha escrito nada
        if (textoVacio)
        {
            DrawText("?", screenWidth / 2 - 100, screenHeight / 2 - 20, 30, PURPLE);
        }
        else
        {
            DrawText(inputTexto, screenWidth / 2 - 100, screenHeight / 2 - 20, 30, PURPLE); // Mostrar el texto ingresado
        }

        if (IsKeyPressed(KEY_ENTER))
        {
            break;
        }
        if (IsKeyPressed(KEY_BACKSPACE))
        {
            if (inputTextoLargo > 0)
            {
                inputTextoLargo--;
                inputTexto[inputTextoLargo] = '\0';
                if (inputTextoLargo == 0) // Si se borra todo, marcar como vacío
                {
                    textoVacio = true;
                }
            }
        }
        else
        {
            if (IsKeyPressed(KEY_SPACE))
            {
                if (inputTextoLargo < 9)
                {
                    inputTexto[inputTextoLargo] = ' ';
                    inputTextoLargo++;
                    inputTexto[inputTextoLargo] = '\0';
                    textoVacio = false; // Cambiar a no vacío
                }
            }
            else
            {
                char c = GetCharPressed();
                if (c != '\0')
                {
                    if (inputTextoLargo < 9)
                    {
                        inputTexto[inputTextoLargo] = c;
                        inputTextoLargo++;
                        inputTexto[inputTextoLargo] = '\0';
                        textoVacio = false; // Cambiar a no vacío
                    }
                }
            }
        }

        EndDrawing();
    }

    strcpy(score.username, inputTexto);

    // Guardar la puntuación con el nivel
    score.score = gameState.score;
    score.nivel = gameState.currentDifficulty; // Agregamos el nivel

    // Buscamos la posición para guardar la puntuación
    int pos = 0;
    for (int i = 0; i < 10; i++)
    {
        if (scores[score.nivel][i].score < score.score)
        {
            pos = i;
            break;
        }
    }

    // Guardamos la puntuación
    scores[score.nivel][pos] = score;

    guardarPuntos(score);
}
