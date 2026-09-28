package TP.UI;

import TP.Modelo.Personaje;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// arma la imagen de cada personaje superponiendo capas PNG (recursos/avatar)
// las capas en escala de grises se tiñen con el color del atributo (piel, pelo, ojos, remera)
public class AvatarRenderer {

    private static final String CARPETA = "/TP/recursos/avatar/";
    private static final int TAM = 256; // tamaño de las capas

    private static final Map<String, BufferedImage> capas = new HashMap<>(); // cache: cada PNG se lee una sola vez

    private static final Map<String, Color> PIEL = Map.of(
            "Blanco", new Color(0xF2CFAE),
            "Morocho", new Color(0xC08A5E),
            "Negro", new Color(0x7A4E30));
    private static final Map<String, Color> PELO = Map.of(
            "Rubio", new Color(0xD9B26A),
            "Negro", new Color(0x2E2A28),
            "Colorado", new Color(0xC4532B),
            "Canoso", new Color(0xBDBDBD));
    private static final Map<String, Color> OJOS = Map.of(
            "Azul", new Color(0x2F6DB5),
            "Celeste", new Color(0x7EC8E3),
            "Marron", new Color(0x7A4E30),
            "Miel", new Color(0xC99440),
            "Verde", new Color(0x4E9A48),
            "Negro", new Color(0x2A2A2A));
    private static final Map<String, Color> REMERA = Map.of(
            "Negro", new Color(0x3A3A3A),
            "Blanco", new Color(0xFFFFFF),
            "Violeta", new Color(0x8A5CC2),
            "Rosa", new Color(0xF29BB8));

    // devuelve el avatar del personaje escalado a tam x tam
    public static ImageIcon icono(Personaje p, int tam) {
        BufferedImage chico = new BufferedImage(tam, tam, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = chico.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(armar(p), 0, 0, tam, tam, null);
        g.dispose();
        return new ImageIcon(chico);
    }

    // superpone las capas de atras hacia adelante segun los atributos
    public static BufferedImage armar(Personaje p) {
        BufferedImage img = new BufferedImage(TAM, TAM, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        Color pelo = PELO.get(p.getColorPelo());
        String peinado = p.getLargoPelo().toLowerCase() + "_" + p.getTipoPelo().toLowerCase();

        if (p.isTienePelo() && !p.getLargoPelo().equals("Corto")) { // el corto no tiene parte de atras
            dibujar(g, "pelo_" + peinado + "_atras", pelo);
        }
        dibujar(g, "remera", REMERA.get(p.getColorRemera()));
        dibujar(g, "cabeza", PIEL.get(p.getColorPiel()));
        if (p.isTieneCollar()) dibujar(g, "collar", null);
        if (p.getRangoEteareo().equals("Adulto")) dibujar(g, "edad_adulto", null);
        dibujar(g, "cara", null);
        if (p.isTieneLabial()) dibujar(g, "labial", null);
        dibujar(g, "iris", OJOS.get(p.getColorOjos()));
        dibujar(g, "ojos", null);
        if (p.isTieneBarba()) dibujar(g, "barba", pelo != null ? pelo : PELO.get("Negro")); // los pelados tienen barba negra
        if (p.isTienePelo()) dibujar(g, "pelo_" + peinado + "_frente", pelo);
        if (p.isTieneLentes()) dibujar(g, "lentes", null);
        if (p.isTieneGorro()) dibujar(g, "gorro", null);
        if (p.getRangoEteareo().equals("Niño")) dibujar(g, "edad_nino", null);
        dibujar(g, p.getGenero().equals("Femenino") ? "genero_femenino" : "genero_masculino", null);

        g.dispose();
        return img;
    }

    // dibuja una capa; si viene color, la tiñe antes
    private static void dibujar(Graphics2D g, String nombre, Color color) {
        BufferedImage capa = cargar(nombre);
        if (capa == null) return;
        g.drawImage(color == null ? capa : tenir(capa, color), 0, 0, null);
    }

    // tinte por multiplicacion: el gris claro queda del color, el contorno oscuro sigue oscuro
    private static BufferedImage tenir(BufferedImage capa, Color color) {
        int w = capa.getWidth(), h = capa.getHeight();
        int[] px = capa.getRGB(0, 0, w, h, null, 0, w);
        for (int i = 0; i < px.length; i++) {
            int a = px[i] >>> 24;
            if (a == 0) continue;
            int r = ((px[i] >> 16) & 0xFF) * color.getRed() / 255;
            int gr = ((px[i] >> 8) & 0xFF) * color.getGreen() / 255;
            int b = (px[i] & 0xFF) * color.getBlue() / 255;
            px[i] = (a << 24) | (r << 16) | (gr << 8) | b;
        }
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        out.setRGB(0, 0, w, h, px, 0, w);
        return out;
    }

    private static BufferedImage cargar(String nombre) {
        if (capas.containsKey(nombre)) return capas.get(nombre);
        BufferedImage img = null;
        try (InputStream in = AvatarRenderer.class.getResourceAsStream(CARPETA + nombre + ".png")) {
            if (in != null) {
                img = ImageIO.read(in);
            } else {
                System.out.println("Falta la capa " + nombre + ".png");
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer la capa " + nombre + ": " + e.getMessage());
        }
        capas.put(nombre, img);
        return img;
    }
}
