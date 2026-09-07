package TP;


// hereda de Jugador, solo que la funcion de elegirJugada() es un algoritmo que elige la mejor pregunta posible

import java.util.List;

public class JugadorMaquina extends Jugador {
    public JugadorMaquina(String nombre, Personaje eleccion, Estrategia estrategia, List<Personaje> tablero) {
        super(nombre, eleccion, estrategia, tablero);
    }

    public void elegirJugada() {
        // algoritmo para elegir
    }
}
