package TP.UI;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

// el cuadro "Partida". al crearlo, todo lo que se imprima con System.out.println aparece aca
public class RegistroPartida extends JScrollPane {

    private final JTextArea texto = new JTextArea(8, 0);

    public RegistroPartida() {
        texto.setEditable(false);
        texto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        setViewportView(texto); // lo que se ve adentro del scroll
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 10, 10),
                BorderFactory.createTitledBorder("Partida")));
        redirigirConsola();
    }

    private void redirigirConsola() {
        OutputStream haciaElTexto = new OutputStream() {
            private final ByteArrayOutputStream linea = new ByteArrayOutputStream();

            @Override
            public void write(int b) {
                linea.write(b);
                if (b == '\n') {
                    String renglon = linea.toString(StandardCharsets.UTF_8);
                    linea.reset();
                    SwingUtilities.invokeLater(() -> {
                        texto.append(renglon);
                        texto.setCaretPosition(texto.getDocument().getLength());
                    });
                }
            }
        };
        System.setOut(new PrintStream(haciaElTexto, true, StandardCharsets.UTF_8));
    }
}