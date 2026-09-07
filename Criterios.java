package TP;

import java.util.Comparator;

// comparators reutilizables para el sort
public class Criterios {

    private Criterios() { }

    // criterio por defecto
    public static Comparator<Personaje> porGenero() {
        return Comparator.comparing(Personaje::getGenero);
    }

    // los que cumplen el atributo booleano van primero (base viz futura)
    public static Comparator<Personaje> porAtributoBooleano(String atributo) {
        return (a, b) -> {
            boolean cumpleA = "true".equals(a.getAtributo(atributo));
            boolean cumpleB = "true".equals(b.getAtributo(atributo));
            if (cumpleA == cumpleB) return 0;
            if (cumpleA) {
                return -1;
            } else {
                return 1;
            }
        };
    }

    // orden alfabetico por el valor de cualquier atributo del map
    public static Comparator<Personaje> porAtributo(String atributo) {
        return Comparator.comparing(p -> p.getAtributo(atributo));
    }
}
