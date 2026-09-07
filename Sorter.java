package TP;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// varios algoritmos de ordenamiento con la misma firma, para poder compararlos.
// todos ordenan una copia y no tocan la lista original.
public class Sorter {

    private Sorter() {
    }

    // MERGESORT: O(n log n), estable
    public static List<Personaje> mergeSort(List<Personaje> lista, Comparator<Personaje> criterio) {
        List<Personaje> copia = new ArrayList<>(lista);
        if (copia.size() <= 1) return copia;
        int medio = copia.size() / 2;
        List<Personaje> izq = mergeSort(copia.subList(0, medio), criterio);
        List<Personaje> der = mergeSort(copia.subList(medio, copia.size()), criterio);
        return merge(izq, der, criterio);
    }

    // combina dos mitades ordenadas. empate -> gana izquierda (estable)
    private static List<Personaje> merge(List<Personaje> izq, List<Personaje> der, Comparator<Personaje> criterio) {
        List<Personaje> res = new ArrayList<>(izq.size() + der.size());
        int i = 0, j = 0;
        while (i < izq.size() && j < der.size()) {
            if (criterio.compare(izq.get(i), der.get(j)) <= 0) res.add(izq.get(i++));
            else res.add(der.get(j++));
        }
        while (i < izq.size()) res.add(izq.get(i++));
        while (j < der.size()) res.add(der.get(j++));
        return res;
    }


    // QUICKSORT: O(n log n) promedio, O(n^2) peor caso, in-place, no estable
    public static List<Personaje> quickSort(List<Personaje> lista, Comparator<Personaje> criterio) {
        List<Personaje> copia = new ArrayList<>(lista);
        quickSort(copia, 0, copia.size() - 1, criterio);
        return copia;
    }

    private static void quickSort(List<Personaje> l, int lo, int hi, Comparator<Personaje> criterio) {
        if (lo >= hi) return;
        int p = particionar(l, lo, hi, criterio);
        quickSort(l, lo, p - 1, criterio);
        quickSort(l, p + 1, hi, criterio);
    }

    // pivote = ultimo elemento
    private static int particionar(List<Personaje> l, int lo, int hi, Comparator<Personaje> criterio) {
        Personaje pivote = l.get(hi);
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (criterio.compare(l.get(j), pivote) <= 0) {
                i++;
                intercambiar(l, i, j);
            }
        }
        intercambiar(l, i + 1, hi);
        return i + 1;
    }

    private static void intercambiar(List<Personaje> l, int a, int b) {
        Personaje tmp = l.get(a);
        l.set(a, l.get(b));
        l.set(b, tmp);
    }

}

