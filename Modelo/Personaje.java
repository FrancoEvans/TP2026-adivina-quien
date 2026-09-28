package TP.Modelo;
import java.util.LinkedHashMap;
import java.util.Map;

public class Personaje {
    private int id;  // se asigna luego de cada sort
    private final String nombre;
    private final String genero;
    private final String rangoEteareo;
    private final String colorPelo;
    private final boolean tieneGorro;
    private final String colorPiel;
    private final boolean tieneLentes;
    private final String colorOjos;
    private final boolean tieneCollar;
    private final String colorRemera;
    private final boolean tieneBarba;
    private final boolean tieneLabial;
    private final boolean tienePelo;
    private final String largoPelo;
    private final String tipoPelo;
    private final Map<String, String> atributos = new LinkedHashMap<>();


    public Personaje( int id,String nombre, String genero, String rangoEteareo, String colorPelo, boolean tieneGorro, String colorPiel, boolean tieneLentes, String colorOjos, boolean tieneCollar, String colorRemera, boolean tieneBarba, boolean tieneLabial, boolean tienePelo, String largoPelo, String tipoPelo) {
        this.tienePelo = genero.equals("Femenino") || tienePelo; // no hay mujeres peladas
        this.largoPelo = this.tienePelo ? largoPelo : "N/A";
        this.tipoPelo = this.tienePelo ? tipoPelo : "N/A";
        this.colorPelo = this.tienePelo ? colorPelo : "N/A";
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.rangoEteareo = rangoEteareo;
        this.tieneGorro = tieneGorro;
        this.colorPiel = colorPiel;
        this.tieneLentes = tieneLentes;
        this.colorOjos = colorOjos;
        this.tieneCollar = tieneCollar;
        this.colorRemera = colorRemera;
        this.tieneBarba = genero.equals("Masculino") && !rangoEteareo.equals("Niño") && tieneBarba;
        this.tieneLabial = genero.equals("Femenino") && tieneLabial;

        atributos.put("genero", genero);
        atributos.put("rangoEteareo", rangoEteareo);
        atributos.put("colorPelo", this.colorPelo);
        atributos.put("largoPelo", this.largoPelo);
        atributos.put("tipoPelo", this.tipoPelo);
        atributos.put("colorPiel", colorPiel);
        atributos.put("colorOjos", colorOjos);
        atributos.put("colorRemera", colorRemera);
        atributos.put("tienePelo", String.valueOf(this.tienePelo));
        atributos.put("tieneGorro", String.valueOf(tieneGorro));
        atributos.put("tieneLentes", String.valueOf(tieneLentes));
        atributos.put("tieneCollar", String.valueOf(tieneCollar));
        atributos.put("tieneBarba", String.valueOf(this.tieneBarba));
        atributos.put("tieneLabial", String.valueOf(this.tieneLabial));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public String getRangoEteareo() {
        return rangoEteareo;
    }

    public String getColorPelo() {
        return colorPelo;
    }

    public boolean isTieneGorro() {
        return tieneGorro;
    }

    public String getColorPiel() {
        return colorPiel;
    }

    public boolean isTieneLentes() {
        return tieneLentes;
    }

    public String getColorOjos() {
        return colorOjos;
    }

    public boolean isTieneCollar() {
        return tieneCollar;
    }

    public String getColorRemera() {
        return colorRemera;
    }

    public boolean isTieneBarba() {
        return tieneBarba;
    }

    public boolean isTieneLabial() {
        return tieneLabial;
    }

    public boolean isTienePelo() {
        return tienePelo;
    }

    public String getLargoPelo() {
        return largoPelo;
    }

    public String getAtributo(String clave) {
        return atributos.getOrDefault(clave, "N/A");
    }

    public String getTipoPelo() {
        return tipoPelo;
    }

    public boolean tieneMismasCaracteristicas(Personaje otro) {
        return this.genero.equals(otro.genero)
                && this.rangoEteareo.equals(otro.rangoEteareo)
                && this.colorPelo.equals(otro.colorPelo)
                && this.tieneGorro == otro.tieneGorro
                && this.colorPiel.equals(otro.colorPiel)
                && this.tieneLentes == otro.tieneLentes
                && this.colorOjos.equals(otro.colorOjos)
                && this.tieneCollar == otro.tieneCollar
                && this.colorRemera.equals(otro.colorRemera)
                && this.tieneBarba == otro.tieneBarba
                && this.tieneLabial == otro.tieneLabial
                && this.tienePelo == otro.tienePelo
                && this.largoPelo.equals(otro.largoPelo)
                && this.tipoPelo.equals(otro.tipoPelo);
    }
    @Override
    public String toString() {
        return "Personajes{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", rangoEteareo='" + rangoEteareo + '\'' +
                ", colorPelo='" + colorPelo + '\'' +
                ", tieneGorro=" + tieneGorro +
                ", colorPiel='" + colorPiel + '\'' +
                ", tieneLentes=" + tieneLentes +
                ", colorOjos='" + colorOjos + '\'' +
                ", tieneCollar=" + tieneCollar +
                ", colorRemera='" + colorRemera + '\'' +
                ", tieneBarba=" + tieneBarba +
                ", tieneLabial=" + tieneLabial +
                ", tienePelo=" + tienePelo +
                ", largoPelo='" + largoPelo + '\'' +
                ", tipoPelo='" + tipoPelo + '\'' +
                '}';
    }
}

