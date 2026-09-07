package TP;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ListaPersonajes lista = new ListaPersonajes();
        lista.generarPersonajes(23);

        Jugador a = new JugadorHumano("Jugador", lista.getPersonajes());
        Jugador b = new JugadorMaquina("Maquina", lista.getPersonajes());
        Jugador c = new JugadorMaquina("Maquina", lista.getPersonajes());

        Juego juego = new Juego(a, b);
        juego.iniciar();

        // la maquina los dispone: ordena por genero, id autoincremental
        lista.ordenar(Criterios.porGenero());
        lista.mostrarLista();

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(new Pregunta("Es de genero femenino?", "genero", "Femenino"));
        preguntas.add(new Pregunta("Usa lentes?", "tieneLentes", "true"));
        preguntas.add(new Pregunta("Es calvo?", "tienePelo", "false"));

    }
}
