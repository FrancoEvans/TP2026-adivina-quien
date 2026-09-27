package TP;

// hereda de Jugador, solo que la funcion de elegirJugada() escucha los eventos del UI

import java.util.List;
import java.util.Scanner;

public class JugadorHumano extends Jugador {
    private final Scanner scanner = new Scanner(System.in);

    public JugadorHumano(String nombre, List<Personaje> tablero, List<Pregunta> preguntas) {
        super(nombre, tablero, preguntas);
    }

    @Override
    public Jugada elegirJugada() {
        System.out.println("Te quedan " + getCandidatos().size() + " candidatos posibles.");
        System.out.println("Preguntas disponibles:");
        List<Pregunta> preguntas = getPreguntasDisponibles();
        for (int i = 0; i < preguntas.size(); i++) {
            System.out.println((i + 1) + ". " + preguntas.get(i).getTexto());
        }
        System.out.println("0. Arriesgar un personaje");
        System.out.print("Eleccion: ");

        int opcion = scanner.nextInt();

        if (opcion == 0) {
            System.out.println("Tus candidatos:");
            for (Personaje p : getCandidatos()) {
                System.out.println(p.getId() + "-" + p.getNombre());
            }
            int idElegido = scanner.nextInt();
            Personaje elegido = getCandidatos().get(0);
            for (Personaje p : getCandidatos()) {
                if (p.getId() == idElegido) {
                    elegido = p;
                }
            }
            return Jugada.arriesgar(elegido);
        }

        Pregunta elegida = preguntas.remove(opcion - 1);
        return Jugada.preguntar(elegida);
    }

    @Override
    public void elegirPersonajeSecreto(List<Personaje> tablero) { //Metodo que elije el personaje, cada subclase lo trabaja distinto
        System.out.println("Elegí tu personaje secreto:");
        for (Personaje p : tablero) {
            System.out.println(p.getId() + "-" + p.getNombre());
        }
        System.out.print("Eleccion: ");

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
