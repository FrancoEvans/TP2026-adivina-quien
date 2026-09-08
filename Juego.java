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
    // El constructor juego hace que al main le pueda pasar new Juego(a,b)

    public Juego(Jugador jugadorA, Jugador jugadorB) {
        this.jugadorA = jugadorA;
        this.jugadorB = jugadorB;
    }

    public void iniciar(List<Personaje> tablero) {
        // pide a cada jugador que elija su personaje secreto hasta que lo haga
        while (jugadorA.getEleccion() == null) {
            jugadorA.elegirPersonajeSecreto(tablero);
        }
        while (jugadorB.getEleccion() == null) {
            jugadorB.elegirPersonajeSecreto(tablero);
        }

        // asigna turnoActual a un jugador aleatorio
        turnoActual = new java.util.Random().nextBoolean() ? jugadorA : jugadorB;
        // define que turno = 1
        turno = 1;
    }

    public Jugador rivalDe(Jugador jugador) {
        if (jugador == jugadorA) {
            return jugadorB;
        } else {
            return jugadorA;
        }
    }

    public void jugarTurno() {
        if (termino()) return;

        Jugador rival = rivalDe(turnoActual);
        Jugada jugada = turnoActual.elegirJugada();

        if (jugada.esPregunta()) {
            boolean respuesta = rival.responderPregunta(jugada.getPregunta());
            turnoActual.descartar(jugada.getPregunta(), respuesta);
        } else {
            //Cuando el jugador arriesgo
            boolean acierto = rival.responderSuposicion(jugada.getPersonaje());
            if (acierto) {
                ganador = turnoActual;
            }
        }

        turno ++;
        turnoActual = rival;
    }

    public boolean termino() {
        // devuelve si la partida ya termino
        return ganador != null;
    }

}
