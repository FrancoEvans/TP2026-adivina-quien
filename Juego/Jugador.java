package TP.Juego;

import TP.Modelo.Jugada;
import TP.Modelo.Personaje;
import TP.Modelo.Pregunta;

import java.util.ArrayList;
import java.util.List;

// un jugador (humano o maquina). la logica comun vive aca; como elige la jugada
// La estrategia ya no se define con un objeto, ahora llaman al metodo  elegirJugada(), y cada subclase lo define de una forma..
public abstract class Jugador {

    private final String nombre;
    private Personaje eleccion;  // su personaje secreto
    private final List<Personaje> candidatos; // los que le quedan del rival
    private final List<Pregunta> preguntasDisponibles; // las que todavia no gasto


    // la eleccion del personaje secreto todavia no se conoce al crear el jugador, se agrega despues con un setter
    public Jugador(String nombre, List<Personaje> tablero, List<Pregunta> preguntas) {
        this.nombre = nombre;
        this.candidatos = new ArrayList<>(tablero);
        this.preguntasDisponibles = new ArrayList<>(preguntas);
    }

    // el rival consulta pregunta y arriesga a traves de aca
    public boolean responderPregunta(Pregunta pregunta) {
        return pregunta.evaluar(eleccion);
    }

    public boolean responderSuposicion(Personaje suposicion) {
        return suposicion.getId() == eleccion.getId();
    }

    // descarta candidatos segun la respuesta del rival a una pregunta
    public void descartar(Pregunta pregunta, boolean respuesta) {
        List<Personaje> quedan = Logica.filtrar(candidatos, pregunta, respuesta);
        candidatos.clear();
        candidatos.addAll(quedan);

        if (respuesta) {
            List<Pregunta> aEliminar = new ArrayList<>();
            for (Pregunta p : preguntasDisponibles) {
                if (p.getAtributo().equals(pregunta.getAtributo())) {
                    aEliminar.add(p);
                }
            }
            preguntasDisponibles.removeAll(aEliminar);
        }
    }

    // saca de los candidatos a un personaje que arriesgo y no era
    public void descartarPersonaje(Personaje personaje) {
        candidatos.remove(personaje);
    }

    // ya lo tiene identificado
    public boolean identificado() {
        return candidatos.size() == 1;
    }
    // Se llama cuando arranca la partida, cuando el jugador o la maquina elije el personaje secreto
    public void setEleccion(Personaje eleccion) {
        this.eleccion = eleccion;
    }
    //Cada subclase (persona o jugador) decide como arrma la jugada
    public abstract Jugada elegirJugada();

    // cada subclase decide como elegir el personaje, y llama al setter de eleccion
    public abstract void elegirPersonajeSecreto(List<Personaje> tablero);

    public String getNombre() { return nombre; }
    public Personaje getEleccion() { return eleccion; }
    public List<Personaje> getCandidatos() { return candidatos; }
    public List<Pregunta> getPreguntasDisponibles() { return preguntasDisponibles; }
}
