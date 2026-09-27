package TP;

import javax.swing.*;
import java.awt.*;


public class VentanaInicio extends JFrame {

    private final JTextField campoNombre;
    private final JButton botonVsMaquina;
    private final JButton botonMaqVsMaq;

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
        botonVsMaquina = new JButton("Jugador vs Maquina");
        botonMaqVsMaq = new JButton("Maquina vs Maquina");

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
        if (nombre.isEmpty()){
            JOptionPane.showMessageDialog(this, "Ingresa tu nombre para jugar");
            return;
        }
        botonVsMaquina.setEnabled(false);
        botonMaqVsMaq.setEnabled(false);
        System.out.println("Arranca " + nombre + " vs Maquina");
        new Thread(()-> Main.jugarHumanoVsMaquina(nombre)).start();

    }

    private void iniciarMaquinaVsMaquina() {
        System.out.println("Arranca Maquina vs Maquina");
        botonVsMaquina.setEnabled(false);
        botonMaqVsMaq.setEnabled(false);
        new Thread(()-> Main.jugarMaquinaVsMaquina()).start();

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaInicio().setVisible(true));
    }
}