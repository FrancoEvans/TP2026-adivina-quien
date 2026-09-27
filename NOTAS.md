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
- 
## 2026-09-07 - parte 4: cambios y mejoras en la logica

`ListaPersonajes`
- agregue "Amarillo" al pool de coloresPelo. la consigna pide como filtros
  obligatorios 3 colores de pelo especificos (colorado, negro, amarillo) 
  faltaba amarillo en el pool.
- agregue el metodo existeDuplicadoDeCaracteristicas(candidato), que recorre
  los personajes ya generados y devuelve true si alguno tiene exactamente
  las mismas caracteristicas que el candidato.
- generarPersonajesAleatorios ahora chequea existeDuplicadoDeCaracteristicas
  antes de agregar un personaje. si es duplicado, no se agrega y el while
  vuelve a intentar (sin gastar el nombre ni el contador).
  
`Personaje`
- agregue el metodo tieneMismasCaracteristicas(otro), que compara todos los
  atributos del personaje excepto id y nombre.

`Main`
- complete la lista de preguntas jugables, una para cada atributo de Personaje

`Jugador`
- saque el objeto estrategia, que va a ser reemplazado por el metodo elegirJugada.
  con esto cambie el constructor.
- borre el metodo jugar que derivaba a estrategia.
- Personaje eleccion dejo de ser final y de recibirse por constructor, ahora se
  asigna con un setter.
- agregue el metodo elegirPersonajeSecreto, y cada subclase define como elige su
  personaje cuando arranca la partida.

`JugadorHumano`
- actualice el constructor acorde a Jugador.
- elegirJugada() corregido el tipo de retorno de void a Jugada.
- elegirPersonajeSecreto implementado. permite elegir por id o por nombre.

`JugadorMaquina`
- lo mismo que JugadorHumano.
- la implementacion de elegirPersonajeSecreto es sorteando un indice con random,
  y asignandolo con un setter.

`Juego`
- constructor actualizado.
- en iniciar, agregue bucle while que pide que se elija el personaje secreto, si
  este es null.
- termino() devuelve true cuando hay un ganador.
- arme la logica de jugarTurno. si es pregunta, se descartan candidatos con la
  respuesta; si es arriesgue, se chequea si acerto y se define el ganador.

**pendiente**
`JugadorHumano` y `JugadorMaquina`
- implementar elegirJugada() en ambas subclases

## 2026-09-26 - parte 5: Correccion de errores, implementacion de interfaz


- `Main` -> llamaba a `lista.generarPersonajes(23)`, que no existe. el metodo se llama
  `generarPersonajesAleatorios(23)`. corregido el nombre.
- `JugadorHumano` -> `elegirJugada()` declara que devuelve `Jugada` pero no tenia `return`.
  se agrego `return null;` provisorio hasta conectarlo con la interfaz.
- `JugadorMaquina` -> mismo problema que `JugadorHumano`. `return null;` provisorio hasta
  que este el algoritmo greedy.
- `VentanaInicio`-> Nueva interfaz grafica, es la pantalla que aparece apenas abris el juego.
ventana comun, hereda de `JFrame`
- Tiene el titulo del juego, y abajo un espacio para poner el nombre del jugador, mas 2 botones
para elegir la modalidad del juego

**pendiente**
-Terminar la logica el juego
- Implementar las interfaces que ya debern utilizar la logica


## 2026-09-27 - parte 6: logica terminada, maquina vs maquina hecho

`Juego`
- jugarTurno() ahora imprime la pregunta/arriesgue y la respuesta (Si/No), y
  los candidatos restantes despues de cada pregunta (para los dos jugadores).
- agregue comentario explicito con la regla: arriesgar mal no tiene penalidad
  extra, solo pierde el turno, igual que si hubiera preguntado. puede arriesgar
  las veces que quiera

`Jugador`
- descartar(pregunta, respuesta): cuando la respuesta es "si", ademas de
  filtrar candidatos, saco de preguntasDisponibles todas las demas preguntas
  sobre el mismo atributo (mismo getAtributo())

`JugadorMaquina`
- agregue mostrarRazonamiento (boolean, default false) + setter. adentro de
  elegirJugada(), si esta activado, imprime por cada pregunta evaluada
  cantidadSi/cantidadNo/diferencia, y al final cual eligio y por que.
- en false por defecto: el modo Jugador vs Maquina no cambia, solo se
  activa el detalle en el modo que la consigna pide poder presenciar.

`Main`
- Saque armarTablero() y armarPreguntas() a metodos compartidos, para no
  repetir ese codigo en los dos modos de juego.
- arme jugarHumanoVsMaquina(), jugarMaquinaVsMaquina() (con dos JugadorMaquina
  y mostrarRazonamiento en true) y correrPartida() (compartido: crear Juego,
  iniciar, loop de turnos, mostrar ganador).
- agregue un menu por consola en main() que pregunta que modo jugar (1 o 2)
- agregue el while (!juego.termino()) que llama a jugarTurno() hasta terminar.


**pendiente**
- decidir que hacer con VentanaInicio.java: sigue siendo un esqueleto Swing
  desconectado de la logica real, con su propio main() aparte.
- UML desactualizado (cambio mucho con el pase a herencia).
- informe final: justificar MergeSort vs QuickSort, explicar el Greedy,
  cuando Greedy NO es optimo, Big O de la app, algoritmos no aplicados.
- caso limite sin cubrir: si se agotan las preguntas antes de llegar a 1 solo
  candidato, JugadorMaquina.elegirJugada() devolveria una pregunta null.