package TP;

import java.util.List;

// decide la jugada de un jugador en su turno.
// implementaciones: humano por consola, maquina greedy, etc.
public interface Estrategia {
    Jugada elegirJugada(List<Personaje> candidatos, List<Pregunta> preguntasDisponibles);
}
