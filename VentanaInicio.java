package TP;

import javax.swing.*;
import java.awt.*;


public class VentanaInicio extends JFrame {

    private final JTextField campoNombre;

    public VentanaInicio() {
        super("Adivina Quien");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrada en la pantalla
        setLayout(new BorderLayout(10, 10));


        JLabel titulo = new JLabel("Adivina Quien", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // aire arriba
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(4, 1, 10, 10));
        centro.setBorder(BorderFactory.createEmptyBorder(10, 60, 20, 60)); // margen

        campoNombre = new JTextField("Jugador");
        JButton botonVsMaquina = new JButton("Jugador vs Maquina");
        JButton botonMaqVsMaq = new JButton("Maquina vs Maquina");

        centro.add(new JLabel("Tu nombre:"));
        centro.add(campoNombre);
        centro.add(botonVsMaquina);
        centro.add(botonMaqVsMaq);
        add(centro, BorderLayout.CENTER);


        botonVsMaquina.addActionListener(e -> iniciarVsMaquina());
        botonMaqVsMaq.addActionListener(e -> iniciarMaquinaVsMaquina());
    }

    private void iniciarVsMaquina() {
        String nombre = campoNombre.getText().trim();
        System.out.println("Arranca " + nombre + " vs Maquina");

    }

    private void iniciarMaquinaVsMaquina() {
        System.out.println("Arranca Maquina vs Maquina");

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaInicio().setVisible(true));
    }
}