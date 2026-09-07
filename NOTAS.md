# notas de desarrollo - adivina quien

bitacora raw. cada entrada = un avance o decision. despues alimenta la bitacora del informe.

---

## 2026-09-06 - paso 1: sort

**hecho**
- `Sorter.java`: mergesort estable, `mergeSort(lista, Comparator)`. no muta la entrada.
- `Criterios.java`: comparators reutilizables (`porGenero`, `porAtributoBooleano`, `porAtributo`).
- `ListaPersonajes.ordenar(Comparator)`: reordena con mergesort + reasigna id autoincremental 1..N.
- `ListaPersonajes.getPersonajes()`.

**decisiones**
- sorter aparte, no metodo de ListaPersonajes: el sort se usa muchas veces con criterios
  distintos (genero al inicio, despues por atributos). algoritmo separado de datos. queda
  lugar para meter quicksort/insertionsort para la tabla comparativa del informe.
- mergesort principal: O(n log n) garantizado, estable (permite encadenar sorts), D&C puro.
- criterio como Comparator, no hardcodeado.
- id de Personaje dejo de ser final: el enunciado pide asignarlo despues de ordenar. se
  agrego setId().

**problemas**
- Personaje no compilaba: `tieneBarba` era `static final` sin init y se usaba como campo de
  instancia -> pasado a `private final boolean` normal, getter `isTieneBarba()`.
- Juego.java tenia la clase declarada 2 veces, la segunda solo firmas -> dejada una sola con
  el main de prueba.
- Jugada / Jugador eran esqueletos sin cuerpo -> stubs minimos compilables.

**big o (para el informe)**
- mergeSort: T(n) = 2T(n/2) + O(n) -> O(n log n), O(n) memoria.
- ordenar(): O(n log n). se corre una vez al preparar el tablero.

**pendiente / futuro**
- swing: viz con colores del resultado de un filtro por atributo.

---

## 2026-09-06 - paso 1b: mas sorts

**hecho**
- `Sorter` ahora tiene 3 algoritmos, misma firma `(List, Comparator)`:
  - `mergeSort`: D&C, O(n log n) garantizado, estable.
  - `quickSort`: D&C, O(n log n) promedio, O(n^2) peor caso, in-place, no estable. pivote lomuto (ultimo elem).
  - `insertionSort`: O(n^2), estable. es el cuadratico de referencia para la tabla comparativa del informe.

**decisiones**
- tener varias sirve para: (a) tabla comparativa mergesort/quicksort vs cuadratico que pide el
  informe, (b) mostrar que con n=23 la diferencia es despreciable (los personajes ya vienen
  casi ordenados por genero -> insertion queda casi O(n)).
- quicksort NO se usa en el juego real (mergesort es mejor por estable + sin peor caso). queda
  para el informe.

**big o**
- mergeSort O(n log n) 
- quickSort O(n log n) prom, O(n^2) peor 

---

## 2026-09-06 - paso 2: Jugador

**hecho**
- `Estrategia.java`: interfaz `elegirJugada(candidatos, preguntasDisponibles) -> Jugada`.
- `Jugador.java` completo:
  - guarda `nombre`, `eleccion` (secreto), `estrategia`, `candidatos` (copia del tablero).
  - `responderPregunta(p)`: unica via para que el rival lo consulte, no expone `eleccion`.
  - `jugar(preguntas)`: delega la decision a la estrategia.
  - `descartar(pregunta, respuesta)`: filtra `candidatos` con `Logica.filtrar`.
  - `identificado()`: queda 1 candidato.

**decisiones**
- una sola clase Jugador con Estrategia inyectable (no herencia humano/maquina). asi se pueden
  sumar varias IAs distintas sin tocar Jugador. patron strategy.
- `candidatos` es copia, no referencia al tablero: cada jugador descarta lo suyo sin afectar al otro.
- encapsulamiento del enunciado: la maquina no puede "ver" `eleccion` del humano, solo llamar
  `responderPregunta`.

**problemas**
- primera version de `descartar` hacia `candidatos.clear()` antes de leerlos -> se filtraba sobre
  lista vacia. corregido: filtrar primero, despues reemplazar.

**big o**
- `descartar`: O(n) recorre candidatos una vez.
- `jugar`: depende de la estrategia (la greedy sera O(P * n)).

**pendiente**
- implementaciones de Estrategia: humano por consola, maquina greedy.
- logica de turnos en Juego.


## 2026-09-06 - parte 3 - algunos cambios

`JugadorHumano` y `JugadorMaquina`
  - queriamos que el cirterio para `elegirJugada` para un `Jugador` sea diferente dependiendo si es la maquina o el humano.
  - se crearon las clases que heredan de `Jugador`: `JugadorHumano` y `JugadorMaquina`. solo cambia la funcion `elegirJugada()` y para eso la clase `Jugador` ahora es abstracta y tiene una funcion abstracta `elegirJugada()`.

  **proximos pasos**
  
- crear un algoritmo para que la maquina elija una jugada eficientemente teniendo en cuenta la lista de personajes y los atributos
- crear una UI con event listeners para que el humano pueda elegir su jugada clikeando opciones 

- `Juego.jugarTurno()` que esta incompleto
- una opcion para que, cuando un jugador arriesga y no acierta, se descarte de la lista tablero el personaje que eligio arriesgar

**cambios**

-`Juego` ya no es el main, ahora `Main` lo es
- `Juego` representa el flujo del juego turno a turno