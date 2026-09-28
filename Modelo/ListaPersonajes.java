package TP.Modelo;

import TP.Algortimos.Sorter;

import java.util.*;

public class ListaPersonajes {
    private final List<Personaje> personajes;
    private final Random random;

    private static final String[] nombresFemeninos = {
            "Sofia", "Valentina", "Emma", "Isabella", "Catalina", "Martina", "Julieta",
            "Delfina", "Renata", "Mia", "Agustina", "Camila", "Lucia", "Zoe", "Pilar",
            "Guadalupe", "Josefina", "Malena", "Emilia", "Priscila", "Antonella",
            "Constanza", "Milagros", "Abril", "Morena", "Ambar", "Luana", "Olivia",
            "Francesca", "Selena"
    };
    private static final String[] nombresMasculinos = {
            "Juan", "Mateo", "Santiago", "Benjamin", "Lautaro", "Tomas", "Facundo",
            "Bautista", "Franco", "Thiago", "Ignacio", "Bruno", "Joaquin", "Nicolas",
            "Agustin", "Valentino", "Ciro", "Gael", "Santino", "Dante", "Pedro",
            "Martin", "Federico", "Rodrigo", "Maximo", "Ramiro", "Tobias", "Lorenzo",
            "Simon", "Lucas"
    };
    private static final String [] generos = {"Masculino", "Femenino"};
    private static final String [] rangosEtareos = {"Niño", "Joven", "Adulto"};
    private static final String [] coloresPelo = {"Rubio", "Negro", "Colorado", "Canoso"};
    private static final String [] coloresPiel ={"Blanco", "Negro", "Morocho"};
    private static final String [] coloresOjo = {"Azul", "Celeste", "Marron", "Miel", "Verde", "Negro"};
    private static final String [] coloresRemera = {"Negro", "Blanco", "Violeta", "Rosa"};
    private static final String [] largosPelo ={"Corto", "Medio", "Largo"};
    private static final String [] tiposPelo ={"Lacio", "Ondeado", "Enrulado"};

    public ListaPersonajes() {
        this.personajes = new ArrayList<>();
        this.random = new Random();

    }
    private String elegirRandom(String[] lista) {
        return lista[random.nextInt(lista.length)];
    }

    public void generarPersonajesAleatorios(int cantidad) {
        List<String> masculinosDisponibles = new ArrayList<>(Arrays.asList(nombresMasculinos));
        List<String> femeninosDisponibles = new ArrayList<>(Arrays.asList(nombresFemeninos));
        Collections.shuffle(masculinosDisponibles, random);
        Collections.shuffle(femeninosDisponibles, random);

        int indiceMasculino = 0;
        int indiceFemenino = 0;
        int generados = 0;

        while (generados < cantidad) {
            String genero = elegirRandom(generos);
            String nombre;
            if (genero.equals("Masculino")) {
                nombre = masculinosDisponibles.get(indiceMasculino);
            } else {
                nombre = femeninosDisponibles.get(indiceFemenino);
            }
            String rangoEtareo = elegirRandom(rangosEtareos);
            String colorPelo = elegirRandom(coloresPelo);
            String colorPiel = elegirRandom(coloresPiel);
            String colorOjo = elegirRandom(coloresOjo);
            String colorRemera = elegirRandom(coloresRemera);
            String largoPelo = elegirRandom(largosPelo);
            String tipoPelo = elegirRandom(tiposPelo);
            boolean tieneGorro = random.nextDouble() < 0.3; // 30% de probabilidad
            boolean tieneLentes = random.nextDouble() < 0.3; // 30% de probabilidad
            boolean tieneCollar = random.nextBoolean();
            boolean tieneBarba = genero.equals("Masculino") && !rangoEtareo.equals("Niño") && random.nextBoolean();
            boolean tieneLabial = genero.equals("Femenino") && random.nextBoolean();
            boolean tienePelo = genero.equals("Femenino") || random.nextDouble() >= 0.3; // las mujeres siempre tienen pelo, los hombres 30% pelados

            Personaje candidato = new Personaje(0, nombre, genero, rangoEtareo, colorPelo,
                    tieneGorro, colorPiel, tieneLentes, colorOjo,
                    tieneCollar, colorRemera, tieneBarba, tieneLabial, tienePelo,
                    largoPelo, tipoPelo
            );

            if (!existeDuplicadoDeCaracteristicas(candidato)) {
                this.personajes.add(candidato);
                if (genero.equals("Masculino")) {
                    indiceMasculino++;
                } else {
                    indiceFemenino++;
                }
                generados++;
            }
        }
    }

    private boolean existeDuplicadoDeCaracteristicas(Personaje candidato) {
        for (Personaje p : this.personajes) {
            if (p.tieneMismasCaracteristicas(candidato)) {
                return true;
            }
        }
        return false;
    }

    // reordena con mergesort y reasigna id autoincremental. se puede llamar varias veces
    public void ordenar(Comparator<Personaje> criterio) {
        List<Personaje> ordenados = Sorter.mergeSort(personajes, criterio);
        personajes.clear();
        personajes.addAll(ordenados);
        int id = 1;
        for (Personaje p : personajes) {
            p.setId(id++);
        }
    }

    public List<Personaje> getPersonajes() {
        return personajes;
    }

    public void mostrarLista() {
        for (Personaje p : personajes) {
            System.out.println(p);
        }
    }
}
