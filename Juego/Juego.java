package TP.Juego;

import TP.Modelo.Jugada;
import TP.Modelo.Personaje;
import TP.Modelo.Pregunta;

import java.util.List;
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
            Pregunta pregunta = jugada.getPregunta();
            boolean respuesta = rival.responderPregunta(pregunta);
            System.out.println(turnoActual.getNombre() + " pregunta: " + pregunta.getTexto()
                    + " -> " + (respuesta ? "Si" : "No"));
            turnoActual.descartar(pregunta, respuesta);

            System.out.println("Le quedan " + turnoActual.getCandidatos().size() + " candidatos:");
            for (Personaje p : turnoActual.getCandidatos()) {
                System.out.println("  " + p.getId() + "-" + p.getNombre());
            }
        } else {
            //Cuando el jugador arriesgo
            Personaje arriesgado = jugada.getPersonaje();
            boolean acierto = rival.responderSuposicion(arriesgado);
            System.out.println(turnoActual.getNombre() + " arriesga a " + arriesgado.getNombre()
                    + " -> " + (acierto ? "Si" : "No"));
            if (acierto) {
                ganador = turnoActual;
            }
            // puede arriesgar las veces que quiera, solo pierde el turno, igual que si hubiera preguntado
        }
        turno++;
        turnoActual = rival;
    }

    public boolean termino() {
        // devuelve si la partida ya termino
        return ganador != null;
    }
    public Jugador getGanador() {
        return ganador;
    }
}
