#include "fracciones.h"
#include <stdlib.h>

bool sonFraccionesIguales(Ts_fraccion a, Ts_fraccion b)
{
    return (a.numerador == b.numerador &&
            a.denominador == b.denominador);
}

bool sonFraccionesEquiv(Ts_fraccion a, Ts_fraccion b)
{
    return (a.numerador * b.denominador ==
            a.denominador * b.numerador);
}

Ts_fraccion fraccionIdentRandom()
{
    Ts_fraccion f;
    int opciones[][2] = {
        {1, 2}, {1, 3}, {1, 4}, {1, 8},
        {2, 3}, {3, 4}, {1, 1}
    };
    int r = rand() % 7;

    f.numerador = opciones[r][0];
    f.denominador = opciones[r][1];
    return f;
}

Ts_fraccionEquiv fraccionEquivRandom()
{
    Ts_fraccionEquiv fe;

    Ts_fraccion baseOpc[] = {
        {1, 2}, {1, 3}, {1, 4}, {2, 5}
    };

    Ts_fraccion base = baseOpc[rand() % 4];
    fe.objetivo = base;

    for (int i = 0; i < 3; i++)
    {
        int k = rand() % 5 + 1;
        fe.equivalentes[i].numerador = base.numerador * k;
        fe.equivalentes[i].denominador = base.denominador * k;
    }

    return fe;
}
