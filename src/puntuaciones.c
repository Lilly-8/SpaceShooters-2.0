#include <stdio.h>
#include <stdlib.h>
#include "puntuaciones.h"

void guardarPuntos(Ts_puntos score)
{
    FILE *file = fopen("scores.dll", "a+b");
    if (file == NULL)
    {
        printf("Error al abrir archivo para guardar\n");
        return;
    }

    fwrite(&score, sizeof(Ts_puntos), 1, file);

    fclose(file);
}
