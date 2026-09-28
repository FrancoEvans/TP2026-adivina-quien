package TP.Juego;


// hereda de Jugador, solo que la funcion de elegirJugada() es un algoritmo que elige la mejor pregunta posible

import TP.Modelo.Jugada;
import TP.Modelo.Personaje;
import TP.Modelo.Pregunta;

import java.util.List;
import java.util.Random;

public class JugadorMaquina extends Jugador {
    private final Random random = new Random();
    private boolean mostrarRazonamiento = false; //se activa solo en el maquina vs maquina

    public JugadorMaquina(String nombre, List<Personaje> tablero, List<Pregunta> preguntas) {
        super(nombre, tablero, preguntas);
    }
    public void setMostrarRazonamiento(boolean mostrarRazonamiento) {
        this.mostrarRazonamiento = mostrarRazonamiento;
    }
    @Override
    public Jugada elegirJugada() {
        if (identificado()) {
            return Jugada.arriesgar(getCandidatos().get(0));
        }
        Pregunta mejorPregunta = null;
        int mejorDiferencia = getCandidatos().size();

        for (Pregunta pregunta : getPreguntasDisponibles()) {
            int cantidadSi = 0;
            for (Personaje candidato : getCandidatos()) {
                if (pregunta.evaluar(candidato)) {
                    cantidadSi++;
                }
            }
            int cantidadNo = getCandidatos().size() - cantidadSi;
            int diferencia = Math.abs(cantidadSi - cantidadNo);

            if (mostrarRazonamiento) {
                System.out.println(pregunta.getTexto() + " " + cantidadSi + " si / "
                        + cantidadNo + " no (diferencia " + diferencia + ")");
            }

            if (diferencia < mejorDiferencia) {
                mejorDiferencia = diferencia;
                mejorPregunta = pregunta;
            }
        }

        if (mejorPregunta == null) {
            return Jugada.arriesgar(getCandidatos().get(0));
        }

        if (mostrarRazonamiento) {
            System.out.println(getNombre() + " elige: " + mejorPregunta.getTexto()
                    + " (diferencia " + mejorDiferencia + ")");
        }
        getPreguntasDisponibles().remove(mejorPregunta);
        return Jugada.preguntar(mejorPregunta);
    }
    @Override
    public void elegirPersonajeSecreto(List<Personaje> tablero) { //El random genera un indice, el setter de eleccion asigna con ese indice.
        int indice = random.nextInt(tablero.size());
        setEleccion(tablero.get(indice));
    }
}
