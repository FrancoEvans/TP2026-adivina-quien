package TP.Juego;

import TP.Modelo.Personaje;
import TP.Modelo.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class Logica {
    public static List<Personaje> filtrar(List<Personaje> candidatos, Pregunta pregunta, boolean respuesta) {
        List<Personaje> resultado = new ArrayList<>();
        for (Personaje p : candidatos) {
            if (pregunta.evaluar(p) == respuesta) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}