package TP;

// hereda de Jugador, solo que la funcion de elegirJugada() escucha los eventos del UI

import java.util.List;
import java.util.Scanner;

public class JugadorHumano extends Jugador {
    private final Scanner scanner = new Scanner(System.in);

    public JugadorHumano(String nombre, List<Personaje> tablero) {
        super(nombre, tablero);
    }

    @Override //Agreguen los override cuando sobreescriban algo q es buena practica wachines vamos a sacarnos el 10
    public Jugada elegirJugada() {
        return null;
    }

    @Override
    public void elegirPersonajeSecreto(List<Personaje> tablero) { //Metodo que elije el personaje, cada subclase lo trabaja distinto
        System.out.println("Elegí tu personaje secreto:");
        for (Personaje p : tablero) {
            System.out.println(p.getId() + "-" + p.getNombre());
        }

        if (scanner.hasNextInt()) {
            int idElegido = scanner.nextInt();
            for (Personaje p : tablero) {
                if (p.getId() == idElegido) {
                    setEleccion(p);
                }
            }
        } else if (scanner.hasNext()) {
            String nombreElegido = scanner.next();
            boolean encontrado = false;
            while (!encontrado) {
                for (Personaje p : tablero) {
                    if (p.getNombre().equalsIgnoreCase(nombreElegido)) {
                        setEleccion(p);
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    System.out.println("Nombre no encontrado, intentá de nuevo:");
                    nombreElegido = scanner.next();
                }
            }
        } else {
            System.out.println("Ingrese un numero para elegir el personaje por id, o una cadena de texto para asignarlo por nombre");
        }
    }
}
