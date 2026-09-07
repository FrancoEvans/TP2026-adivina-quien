package TP;

import java.util.ArrayList;
import java.util.List;


// por hacer:
//  - logica de turnos

public class Juego {
    private Jugador jugadorA;
    private Jugador jugadorB;
    private Jugador turnoActual;
    private Jugador ganador;
    private int turno;

    public void iniciar() {
        // verificar que ambos jugadores tienen eleccion y candidatos
        // asigna turnoActual a un jugador aleatorio
        // define que turno = 1
    }

    public Jugador rivalDe(Jugador jugador) {
        if (jugador == jugadorA) {
            return jugadorB;
        } else {
            return jugadorA;
        }
    }

    public void jugarTurno() {

        Jugador rival = rivalDe(turnoActual);
        Jugada jugada = turnoActual.elegirJugada();

        if (termino()) return;

        if (jugada.esPregunta()) {
            rival.responderPregunta(jugada.getPregunta());
        } else {
            // el jugador arriesgo

        }

        turno ++;
        turnoActual = rival;
    }

    public boolean termino() {
        // devuelve si la partida ya termino
    }

}
