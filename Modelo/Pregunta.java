package TP.Modelo;

public class Pregunta {

    private final String texto;          // lo que ve el jugador
    private final String atributo;       // sobre qué pregunta
    private final String valorEsperado;  // contra qué compara

    public Pregunta(String texto, String atributo, String valorEsperado) {
        this.texto = texto;
        this.atributo = atributo;
        this.valorEsperado = valorEsperado;
    }

    public boolean evaluar(Personaje personaje) {
        return valorEsperado.equals(personaje.getAtributo(atributo));
    }

    public String getTexto() { return texto; }
    public String getAtributo() { return atributo; }
    public String getValorEsperado() { return valorEsperado; }

    @Override
    public String toString() { return texto; }
}