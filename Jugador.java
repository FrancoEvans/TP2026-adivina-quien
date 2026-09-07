package TP;

import java.util.ArrayList;
import java.util.List;

// un jugador (humano o maquina). la logica comun vive aca; como elige la jugada
// se delega en una Estrategia.
public class Jugador {

    private final String nombre;
    private final Personaje eleccion;         // su personaje secreto
    private final Estrategia estrategia;
    private final List<Personaje> candidatos; // los que le quedan del rival

    public Jugador(String nombre, Personaje eleccion, Estrategia estrategia, List<Personaje> tablero) {
        this.nombre = nombre;
        this.eleccion = eleccion;
        this.estrategia = estrategia;
        this.candidatos = new ArrayList<>(tablero);
    }

    // el rival consulta por aca, no accede a eleccion
    public boolean responderPregunta(Pregunta pregunta) {
        return pregunta.evaluar(eleccion);
    }

    // pide la jugada del turno a la estrategia
    public Jugada jugar(List<Pregunta> preguntasDisponibles) {
        return estrategia.elegirJugada(candidatos, preguntasDisponibles);
    }

    // descarta candidatos segun la respuesta del rival a una pregunta
    public void descartar(Pregunta pregunta, boolean respuesta) {
        List<Personaje> quedan = Logica.filtrar(candidatos, pregunta, respuesta);
        candidatos.clear();
        candidatos.addAll(quedan);
    }

    // ya lo tiene identificado
    public boolean identificado() {
        return candidatos.size() == 1;
    }

    public String getNombre() { return nombre; }
    public Personaje getEleccion() { return eleccion; }
    public List<Personaje> getCandidatos() { return candidatos; }
}
