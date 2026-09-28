package TP.Juego;

import TP.Modelo.Jugada;
import TP.UI.PantallaJuego;
import TP.Modelo.Personaje;
import TP.Modelo.Pregunta;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.swing.*;

public class JugadorHumano extends Jugador {

    private final BlockingQueue<Jugada> colaJugadas = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Personaje> colaSecreto = new ArrayBlockingQueue<>(1);
    private PantallaJuego pantalla;

    public JugadorHumano(String nombre, List<Personaje> tablero, List<Pregunta> preguntas) {
        super(nombre, tablero, preguntas);
    }

    public void setPantalla(PantallaJuego pantalla) {
        this.pantalla = pantalla;
    }

    public void enviarJugada(Jugada jugada) {
        colaJugadas.offer(jugada);
    }

    public void enviarPersonajeSecreto(Personaje personaje) {
        colaSecreto.offer(personaje);
    }
    public void avisarFin(Jugador ganador, Personaje secretoRival){
        if (pantalla!=null){
            SwingUtilities.invokeLater(() -> pantalla.finDePartida(ganador, secretoRival));

        }
    }


    @Override
    public Jugada elegirJugada() {
        if(pantalla!=null){
            SwingUtilities.invokeLater(() -> pantalla.teToca());
        }
        try {
            Jugada jugada = colaJugadas.take();
            if (jugada.esPregunta()) {
                getPreguntasDisponibles().remove(jugada.getPregunta());
            }
            return jugada;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void elegirPersonajeSecreto(List<Personaje> tablero) {
        try {
            setEleccion(colaSecreto.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}