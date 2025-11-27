#include "fondo.h"
#include "juego.h"

void MoverFondo()
{
    gameState.background.x -= gameState.background.velocidad * GetFrameTime();

    if (gameState.background.x <= -gameState.background.texture.width)
    {
        gameState.background.x = 0;
    }

    DrawTexture(gameState.background.texture, gameState.background.x, 0, WHITE);
    DrawTexture(gameState.background.texture,
                gameState.background.x + gameState.background.texture.width,
                0, WHITE);
}
