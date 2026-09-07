package TP;

import java.util.ArrayList;
import java.util.List;

// main de prueba.

// por hacer:
//  - logica de turnos

public class Juego {

    public static void main(String[] args) {
        ListaPersonajes lista = new ListaPersonajes();
        lista.generarPersonajesAleatorios(23);

        // la maquina los dispone: ordena por genero, id autoincremental
        lista.ordenar(Criterios.porGenero());
        lista.mostrarLista();

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(new Pregunta("Es de genero femenino?", "genero", "Femenino"));
        preguntas.add(new Pregunta("Usa lentes?", "tieneLentes", "true"));
        preguntas.add(new Pregunta("Es calvo?", "tienePelo", "false"));
    }
}
