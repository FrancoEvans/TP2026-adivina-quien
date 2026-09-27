package TP;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaInicio().setVisible(true));
    }

    static void jugarHumanoVsMaquina(String nombre) {
        ListaPersonajes lista = armarTablero();
        List<Pregunta> preguntas = armarPreguntas();

        Jugador a = new JugadorHumano(nombre, lista.getPersonajes(), preguntas);
        Jugador b = new JugadorMaquina("Maquina", lista.getPersonajes(), preguntas);

        correrPartida(a, b, lista);
    }

    static void jugarMaquinaVsMaquina() {
        ListaPersonajes lista = armarTablero();
        List<Pregunta> preguntas = armarPreguntas();

        JugadorMaquina a = new JugadorMaquina("Maquina A", lista.getPersonajes(), preguntas);
        JugadorMaquina b = new JugadorMaquina("Maquina B", lista.getPersonajes(), preguntas);
        a.setMostrarRazonamiento(true);
        b.setMostrarRazonamiento(true);

        correrPartida(a, b, lista);
    }

    private static void correrPartida(Jugador a, Jugador b, ListaPersonajes lista) {
        Juego juego = new Juego(a, b);
        juego.iniciar(lista.getPersonajes());

        while (!juego.termino()) {
            juego.jugarTurno();
        }

        System.out.println("Ganó: " + juego.getGanador().getNombre());
    }

    private static ListaPersonajes armarTablero() {
        ListaPersonajes lista = new ListaPersonajes();
        lista.generarPersonajesAleatorios(23);
        // la maquina los dispone: ordena por genero, id autoincremental
        lista.ordenar(Criterios.porGenero());
        lista.mostrarLista();
        return lista;
    }

    private static List<Pregunta> armarPreguntas() {
        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(new Pregunta("Es de genero femenino?", "genero", "Femenino"));

        preguntas.add(new Pregunta("Es niño?", "rangoEteareo", "Niño"));
        preguntas.add(new Pregunta("Es joven?", "rangoEteareo", "Joven"));
        preguntas.add(new Pregunta("Es adulto?", "rangoEteareo", "Adulto"));

        preguntas.add(new Pregunta("Tiene el pelo colorado?", "colorPelo", "Colorado"));
        preguntas.add(new Pregunta("Tiene el pelo negro?", "colorPelo", "Negro"));
        preguntas.add(new Pregunta("Tiene el pelo amarillo?", "colorPelo", "Amarillo"));
        preguntas.add(new Pregunta("Tiene el pelo rubio?", "colorPelo", "Rubio"));
        preguntas.add(new Pregunta("Tiene el pelo canoso?", "colorPelo", "Canoso"));

        preguntas.add(new Pregunta("Tiene el pelo corto?", "largoPelo", "Corto"));
        preguntas.add(new Pregunta("Tiene el pelo medio?", "largoPelo", "Medio"));
        preguntas.add(new Pregunta("Tiene el pelo largo?", "largoPelo", "Largo"));

        preguntas.add(new Pregunta("Tiene el pelo lacio?", "tipoPelo", "Lacio"));
        preguntas.add(new Pregunta("Tiene el pelo ondeado?", "tipoPelo", "Ondeado"));
        preguntas.add(new Pregunta("Tiene el pelo enrulado?", "tipoPelo", "Enrulado"));

        preguntas.add(new Pregunta("Tiene la piel blanca?", "colorPiel", "Blanco"));
        preguntas.add(new Pregunta("Tiene la piel negra?", "colorPiel", "Negro"));
        preguntas.add(new Pregunta("Tiene la piel morocha?", "colorPiel", "Morocho"));

        preguntas.add(new Pregunta("Tiene ojos azules?", "colorOjos", "Azul"));
        preguntas.add(new Pregunta("Tiene ojos celestes?", "colorOjos", "Celeste"));
        preguntas.add(new Pregunta("Tiene ojos marrones?", "colorOjos", "Marron"));
        preguntas.add(new Pregunta("Tiene ojos color miel?", "colorOjos", "Miel"));
        preguntas.add(new Pregunta("Tiene ojos verdes?", "colorOjos", "Verde"));
        preguntas.add(new Pregunta("Tiene ojos negros?", "colorOjos", "Negro"));

        preguntas.add(new Pregunta("Tiene remera negra?", "colorRemera", "Negro"));
        preguntas.add(new Pregunta("Tiene remera blanca?", "colorRemera", "Blanco"));
        preguntas.add(new Pregunta("Tiene remera violeta?", "colorRemera", "Violeta"));
        preguntas.add(new Pregunta("Tiene remera rosa?", "colorRemera", "Rosa"));

        preguntas.add(new Pregunta("Usa lentes?", "tieneLentes", "true"));

        preguntas.add(new Pregunta("Es pelado?", "tienePelo", "false"));

        preguntas.add(new Pregunta("Usa gorro?", "tieneGorro", "true"));

        preguntas.add(new Pregunta("Usa collar?", "tieneCollar", "true"));

        preguntas.add(new Pregunta("Tiene barba?", "tieneBarba", "true"));

        return preguntas;
    }
}