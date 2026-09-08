package TP;


// hereda de Jugador, solo que la funcion de elegirJugada() es un algoritmo que elige la mejor pregunta posible

import java.util.List;
import java.util.Random;

public class JugadorMaquina extends Jugador {
    private final Random random = new Random();

    public JugadorMaquina(String nombre, List<Personaje> tablero) {
        super(nombre, tablero);
    }
    @Override
    public Jugada elegirJugada() {
        // algoritmo para elegir
    }
    @Override
    public void elegirPersonajeSecreto(List<Personaje> tablero) { //El random genera un indice, el setter de eleccion asigna con ese indice.
        int indice = random.nextInt(tablero.size());
        setEleccion(tablero.get(indice));
    }
}
