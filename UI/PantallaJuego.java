package TP.UI;

import TP.Algortimos.Criterios;
import TP.Algortimos.Sorter;
import TP.Juego.Jugador;
import TP.Juego.JugadorHumano;
import TP.Main;
import TP.Modelo.Jugada;
import TP.Modelo.Personaje;
import TP.Modelo.Pregunta;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// pantalla del modo Jugador vs Maquina
public class PantallaJuego extends JFrame {

    private final JugadorHumano humano;
    private final JLabel estado;
    private final Map<Personaje, JButton> botones = new HashMap<>(); // cada personaje con su boton
    // la lista mezcla titulos de categoria (String) y preguntas (Pregunta)
    private final DefaultListModel<Object> modeloPreguntas = new DefaultListModel<>();
    private final JList<Object> listaPreguntas = new JList<>(modeloPreguntas);
    private final JButton botonPreguntar = new JButton("Preguntar");
    private final JPanel grilla = new JPanel(new GridLayout(0, 6, 5, 5)); // 6 columnas, las filas que hagan falta
    private List<Personaje> ordenTablero; // orden en que se ven los botones (no cambia los ids)

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

        // arriba a la derecha: selector para reordenar el tablero
        JComboBox<String> selectorOrden = new JComboBox<>(CRITERIOS.keySet().toArray(new String[0]));
        selectorOrden.addActionListener(e -> ordenarTablero(CRITERIOS.get((String) selectorOrden.getSelectedItem())));
        JPanel panelOrden = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        panelOrden.add(new JLabel("Ordenar tablero por:"));
        panelOrden.add(selectorOrden);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(estado, BorderLayout.CENTER);
        arriba.add(panelOrden, BorderLayout.EAST);
        add(arriba, BorderLayout.NORTH);

        // centro: el tablero, un boton por personaje
        ordenTablero = tablero;
        grilla.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        for (Personaje p : tablero) {
            JButton boton = new JButton(p.getId() + " - " + p.getNombre(), AvatarRenderer.icono(p, 96));
            boton.setVerticalTextPosition(SwingConstants.BOTTOM); // nombre debajo de la cara
            boton.setHorizontalTextPosition(SwingConstants.CENTER);
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
        listaPreguntas.setCellRenderer(new RendererPreguntas());
        listaPreguntas.addListSelectionListener(e -> {
            if (listaPreguntas.getSelectedValue() instanceof String) { // los titulos no se pueden elegir
                listaPreguntas.clearSelection();
            }
        });
        botonPreguntar.setEnabled(false); // hasta que sea mi turno
        botonPreguntar.addActionListener(e -> clickPreguntar());
    }

    // reordena los botones con mergesort segun el criterio elegido.
    // parte del orden actual: como mergesort es estable, se pueden encadenar criterios
    // (ej: por color de pelo y despues por genero -> agrupados por genero y, dentro, por pelo)
    // no toca los ids ni la lista del juego, solo lo que se ve
    private void ordenarTablero(Comparator<Personaje> criterio) {
        ordenTablero = Sorter.mergeSort(ordenTablero, criterio);
        grilla.removeAll();
        for (Personaje p : ordenTablero) {
            grilla.add(botones.get(p));
        }
        grilla.revalidate();
        grilla.repaint();
    }

    // lo llama el humano (via invokeLater) cuando arranca su turno
    public void teToca() {
        miTurno = true;
        estado.setText("Tu turno: elegí una pregunta, o hacé click en un personaje para arriesgar");

        // refresca las preguntas que le quedan, agrupadas por categoria
        Map<String, List<Pregunta>> porCategoria = new LinkedHashMap<>();
        for (String categoria : ORDEN_CATEGORIAS) {
            porCategoria.put(categoria, new java.util.ArrayList<>());
        }
        for (Pregunta p : humano.getPreguntasDisponibles()) {
            porCategoria.get(categoria(p)).add(p);
        }
        modeloPreguntas.clear();
        for (Map.Entry<String, List<Pregunta>> grupo : porCategoria.entrySet()) {
            if (grupo.getValue().isEmpty()) continue; // categoria sin preguntas: no se muestra
            modeloPreguntas.addElement(grupo.getKey());
            for (Pregunta p : grupo.getValue()) {
                modeloPreguntas.addElement(p);
            }
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
        Object seleccion = listaPreguntas.getSelectedValue();
        if (!(seleccion instanceof Pregunta)) {
            JOptionPane.showMessageDialog(this, "Elegí una pregunta de la lista");
            return;
        }
        Pregunta elegida = (Pregunta) seleccion;
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
                + " - Collar: " + siNo(p.isTieneCollar()) + " - Barba: " + siNo(p.isTieneBarba()) + " - Labial: " + siNo(p.isTieneLabial())
                + "</html>";
    }

    private String siNo(boolean b) {
        return b ? "Si" : "No";
    }

    // opciones del selector de orden, cada una con su criterio
    private static final Map<String, Comparator<Personaje>> CRITERIOS = new LinkedHashMap<>();
    static {
        CRITERIOS.put("ID (original)", Criterios.porId());
        CRITERIOS.put("Genero", Criterios.porGenero());
        CRITERIOS.put("Edad", Criterios.porAtributo("rangoEteareo"));
        CRITERIOS.put("Color de pelo", Criterios.porAtributo("colorPelo"));
        CRITERIOS.put("Largo de pelo", Criterios.porAtributo("largoPelo"));
        CRITERIOS.put("Tipo de pelo", Criterios.porAtributo("tipoPelo"));
        CRITERIOS.put("Color de piel", Criterios.porAtributo("colorPiel"));
        CRITERIOS.put("Color de ojos", Criterios.porAtributo("colorOjos"));
        CRITERIOS.put("Color de remera", Criterios.porAtributo("colorRemera"));
        CRITERIOS.put("Con lentes primero", Criterios.porAtributoBooleano("tieneLentes"));
        CRITERIOS.put("Con gorro primero", Criterios.porAtributoBooleano("tieneGorro"));
        CRITERIOS.put("Con collar primero", Criterios.porAtributoBooleano("tieneCollar"));
        CRITERIOS.put("Con barba primero", Criterios.porAtributoBooleano("tieneBarba"));
        CRITERIOS.put("Con labial primero", Criterios.porAtributoBooleano("tieneLabial"));
        CRITERIOS.put("Con pelo primero", Criterios.porAtributoBooleano("tienePelo"));
    }

    private static final String[] ORDEN_CATEGORIAS = {"Genero", "Edad", "Pelo", "Cara", "Ropa", "Accesorios"};

    // a que categoria pertenece cada pregunta, segun el atributo que consulta
    private static String categoria(Pregunta p) {
        switch (p.getAtributo()) {
            case "genero":
                return "Genero";
            case "rangoEteareo":
                return "Edad";
            case "colorPelo": case "largoPelo": case "tipoPelo": case "tienePelo":
                return "Pelo";
            case "colorPiel": case "colorOjos": case "tieneBarba": case "tieneLabial":
                return "Cara";
            case "colorRemera":
                return "Ropa";
            default: // gorro, lentes, collar
                return "Accesorios";
        }
    }

    // dibuja los titulos de categoria como encabezado y las preguntas con sangria
    private static class RendererPreguntas extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                      boolean seleccionado, boolean foco) {
            if (valor instanceof String) {
                JLabel titulo = (JLabel) super.getListCellRendererComponent(lista, valor, indice, false, false);
                titulo.setFont(titulo.getFont().deriveFont(Font.BOLD));
                titulo.setOpaque(true);
                titulo.setBackground(new Color(0xDCE6F2));
                titulo.setBorder(BorderFactory.createEmptyBorder(6, 6, 4, 6));
                return titulo;
            }
            JLabel item = (JLabel) super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
            item.setBorder(BorderFactory.createEmptyBorder(2, 20, 2, 6));
            return item;
        }
    }
}