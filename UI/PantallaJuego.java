package TP.UI;

import TP.Juego.Jugador;
import TP.Juego.JugadorHumano;
import TP.Main;
import TP.Modelo.Jugada;
import TP.Modelo.Personaje;
import TP.Modelo.Pregunta;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// pantalla del modo Jugador vs Maquina
public class PantallaJuego extends JFrame {

    private final JugadorHumano humano;
    private final JLabel estado;
    private final Map<Personaje, JButton> botones = new HashMap<>(); // cada personaje con su boton
    private final DefaultListModel<Pregunta> modeloPreguntas = new DefaultListModel<>();
    private final JList<Pregunta> listaPreguntas = new JList<>(modeloPreguntas);
    private final JButton botonPreguntar = new JButton("Preguntar");

    private boolean eligiendoSecreto = true; // al principio los clicks eligen el secreto
    private boolean miTurno = false;

    public PantallaJuego(JugadorHumano humano, List<Personaje> tablero) {
        super("Adivina Quien - " + humano.getNombre());
        this.humano = humano;
        humano.setPantalla(this); // asi el humano le puede avisar cuando le toca

        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // arriba: cartel que dice que hay que hacer
        estado = new JLabel("Elegí tu personaje secreto", SwingConstants.CENTER);
        estado.setFont(new Font("SansSerif", Font.BOLD, 18));
        estado.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(estado, BorderLayout.NORTH);

        // centro: el tablero, un boton por personaje
        JPanel grilla = new JPanel(new GridLayout(0, 6, 5, 5)); // 6 columnas, las filas que hagan falta
        grilla.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        for (Personaje p : tablero) {
            JButton boton = new JButton("<html><center>" + p.getId() + "<br>" + p.getNombre() + "</center></html>");
            boton.setToolTipText(descripcion(p)); // al pasar el mouse muestra sus caracteristicas
            boton.addActionListener(e -> clickPersonaje(p));
            grilla.add(boton);
            botones.put(p, boton);
        }
        add(grilla, BorderLayout.CENTER);

        // derecha: lista de preguntas + boton preguntar
        JPanel panelPreguntas = new JPanel(new BorderLayout(5, 5));
        panelPreguntas.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        panelPreguntas.setPreferredSize(new Dimension(260, 0));
        panelPreguntas.add(new JLabel("Preguntas disponibles:"), BorderLayout.NORTH);
        panelPreguntas.add(new JScrollPane(listaPreguntas), BorderLayout.CENTER);
        panelPreguntas.add(botonPreguntar, BorderLayout.SOUTH);
        add(panelPreguntas, BorderLayout.EAST);

        // abajo: el registro de la partida (lo que antes salia por consola)
        add(new RegistroPartida(), BorderLayout.SOUTH);

        listaPreguntas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        botonPreguntar.setEnabled(false); // hasta que sea mi turno
        botonPreguntar.addActionListener(e -> clickPreguntar());
    }

    // lo llama el humano (via invokeLater) cuando arranca su turno
    public void teToca() {
        miTurno = true;
        estado.setText("Tu turno: elegí una pregunta, o hacé click en un personaje para arriesgar");

        // refresca las preguntas que le quedan
        modeloPreguntas.clear();
        for (Pregunta p : humano.getPreguntasDisponibles()) {
            modeloPreguntas.addElement(p);
        }

        // apaga los personajes que ya descarto
        for (Personaje p : botones.keySet()) {
            if (!humano.getCandidatos().contains(p)) {
                botones.get(p).setEnabled(false);
            }
        }
        botonPreguntar.setEnabled(true);
    }

    private void clickPreguntar() {
        Pregunta elegida = listaPreguntas.getSelectedValue();
        if (elegida == null) {
            JOptionPane.showMessageDialog(this, "Elegí una pregunta de la lista");
            return;
        }
        terminarMiTurno();
        humano.enviarJugada(Jugada.preguntar(elegida));
    }

    private void clickPersonaje(Personaje p) {
        if (eligiendoSecreto) {
            humano.enviarPersonajeSecreto(p);
            eligiendoSecreto = false;
            estado.setText("Tu personaje secreto es " + p.getNombre() + ". Esperando a la maquina...");
            return;
        }
        if (miTurno) {
            int r = JOptionPane.showConfirmDialog(this, "¿Arriesgás a " + p.getNombre() + "?",
                    "Arriesgar", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                terminarMiTurno();
                humano.enviarJugada(Jugada.arriesgar(p));
            }
        }
    }

    private void terminarMiTurno() {
        miTurno = false;
        botonPreguntar.setEnabled(false);
        estado.setText("Turno de la maquina...");
    }

    // lo llama el humano (via invokeLater) cuando termina la partida
    public void finDePartida(Jugador ganador, Personaje secretoRival) {
        miTurno = false;
        botonPreguntar.setEnabled(false);
        for (JButton b : botones.values()) {
            b.setEnabled(false);
        }
        String mensaje;
        if (ganador == humano) {
            mensaje = "¡Ganaste! Era " + secretoRival.getNombre();
        } else {
            mensaje = "Ganó la maquina. Era " + secretoRival.getNombre();
        }
        estado.setText(mensaje);

        JOptionPane.showMessageDialog(this, mensaje);
        int r = JOptionPane.showConfirmDialog(this, "¿Jugar de nuevo?", "Jugar de nuevo", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            new Thread(() -> Main.jugarHumanoVsMaquina(humano.getNombre())).start();
        }
    }

    private String descripcion(Personaje p) {
        return "<html>"
                + p.getGenero() + ", " + p.getRangoEteareo() + "<br>"
                + "Pelo: " + p.getColorPelo() + " / " + p.getLargoPelo() + " / " + p.getTipoPelo() + "<br>"
                + "Piel: " + p.getColorPiel() + " - Ojos: " + p.getColorOjos() + "<br>"
                + "Remera: " + p.getColorRemera() + "<br>"
                + "Gorro: " + siNo(p.isTieneGorro()) + " - Lentes: " + siNo(p.isTieneLentes())
                + " - Collar: " + siNo(p.isTieneCollar()) + " - Barba: " + siNo(p.isTieneBarba())
                + "</html>";
    }

    private String siNo(boolean b) {
        return b ? "Si" : "No";
    }
}