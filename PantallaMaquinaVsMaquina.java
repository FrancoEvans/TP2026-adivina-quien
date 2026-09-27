package TP;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// pantalla del modo Maquina vs Maquina: se mira la partida turno a turno
public class PantallaMaquinaVsMaquina extends JFrame {

    private final Juego juego;
    private final JButton botonSiguiente = new JButton("Siguiente turno");
    private int turno = 0;

    public PantallaMaquinaVsMaquina(Juego juego, List<Personaje> personajes) {
        super("Adivina Quien - Maquina vs Maquina");
        this.juego = juego;

        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // arriba: los controles
        JPanel controles = new JPanel();
        controles.add(botonSiguiente);
        add(controles, BorderLayout.NORTH);

        // centro: el registro, donde se ve el razonamiento de cada maquina
        add(new RegistroPartida(), BorderLayout.CENTER);

        // cada maquina elige su secreto (al azar, instantaneo)
        juego.iniciar(personajes);

        botonSiguiente.addActionListener(e -> siguienteTurno());
    }

    private void siguienteTurno() {
        if (juego.termino()) return;

        turno++;
        System.out.println("---------- Turno " + turno + " ----------");
        juego.jugarTurno();

        if (juego.termino()) {
            botonSiguiente.setEnabled(false);
            System.out.println("Ganó " + juego.getGanador().getNombre() + " en " + turno + " turnos");
        }
    }
}