#include "botones.h"

void dibujarBoton(Ts_boton boton)
{
    DrawRectangleRec(boton.rec, boton.color);
    DrawText(boton.text,
             boton.rec.x + boton.rec.width / 2 - MeasureText(boton.text, TAM_TEXT_BOTON) / 2,
             boton.rec.y + boton.rec.height / 2 - TAM_TEXT_BOTON / 2,
             TAM_TEXT_BOTON,
             WHITE);
}

bool botonClick(Ts_boton boton)
{
    Vector2 mouse = GetMousePosition();
    if (CheckCollisionPointRec(mouse, boton.rec))
    {
        return true;
    }
    return false;
}
