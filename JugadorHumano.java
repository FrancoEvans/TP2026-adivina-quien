package TP;

// hereda de Jugador, solo que la funcion de elegirJugada() escucha los eventos del UI

import java.util.List;

public class JugadorHumano extends Jugador {
    public JugadorHumano(String nombre, Personaje eleccion, Estrategia estrategia, List<Personaje> tablero) {
        super(nombre, eleccion, estrategia, tablero);
    }

    public void elegirJugada() {
        //
    }
}
