package TP;

// accion de turno: preguntar o arriesgar. stub, se completa en el paso de turnos

public class Jugada {
    private Pregunta pregunta;
    private Personaje personaje;

    public static Jugada preguntar(Pregunta pregunta) {
        Jugada j = new Jugada();
        j.pregunta = pregunta;
        return j;
    }

    public static Jugada arriesgar(Personaje personaje) {
        Jugada j = new Jugada();
        j.personaje = personaje;
        return j;
    }

    public boolean esPregunta() { return pregunta != null; }
    public Pregunta getPregunta() { return pregunta; }
    public Personaje getPersonaje() { return personaje; }
}
