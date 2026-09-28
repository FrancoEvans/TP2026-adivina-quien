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

## 2026-09-27 - parte 7: Interfaz de los dos modos

**la idea general**
- si el juego se queda esperando que el humano toque un boton, la ventana se congela.
  solucion: dos "empleados". el de la ventanilla (EDT de Swing) solo atiende clicks y
  dibuja. el de atras (un hilo aparte) corre la partida. se pasan la jugada con una caja
  (`BlockingQueue`): el boton la deja (`offer`) y el hilo del juego la agarra (`take`).
- regla de oro: la ventana solo la toca el EDT. desde el hilo del juego se usa
  `SwingUtilities.invokeLater(...)`.
- regla para decidir: si algo ESPERA (a una persona), va en otro hilo. si es una cuenta
  rapida, puede ir en el EDT.

**clases nuevas**
- `PantallaJuego` -> modo Jugador vs Maquina.
  - primer click en un personaje = tu secreto.
  - en tu turno elegis una pregunta de la lista y tocas "Preguntar", o haces click en un
    personaje para arriesgar.
  - los descartados se ponen grises y la lista de preguntas se actualiza.
  - al final muestra quien gano.
- `PantallaMaquinaVsMaquina` -> modo Maquina vs Maquina. boton "Siguiente turno" que
  juega de a un turno, y en el registro se ve el razonamiento de cada maquina.
  aca NO hace falta hilo aparte: no hay humano al que esperar, cada turno es instantaneo.
- `RegistroPartida` -> el cuadro "Partida" de abajo. desvia los `System.out.println` a la
  ventana, asi no hubo que tocar `Juego` ni `JugadorMaquina`. la usan las dos pantallas.

**cambios en clases existentes**
- `JugadorHumano` -> ya no usa `Scanner`. recibe la jugada y el secreto desde la ventana
  por dos colas. antes de esperar le avisa a la pantalla que es su turno (`teToca()`).
- `Main` -> `main()` abre `VentanaInicio` (se saco el menu por consola).
  `jugarMaquinaVsMaquina()` ya no tiene loop: arma el `Juego` y se lo pasa a la pantalla.
  `correrPartida()` devuelve el ganador.
- `VentanaInicio` -> valida nombre vacio, arranca cada modo y se cierra.
- `.gitignore` -> se agrego `.idea/`, `*.iml` y `out/`.

**pendiente**
- UML con las clases de la interfaz.
- informe: seccion de la interfaz.
- opcional: tableros en maquina vs maquina, boton "jugar de nuevo".
- 
## 2026-09-27 - parte 8: detalles de codigo + boton jugar de nuevo

`Juego`
- borre el comentario desactualizado "por hacer: - logica de turnos" del
  encabezado del archivo, ya estaba resuelta hace rato.

`Jugador`
- actualice el comentario de elegirPersonajeSecreto, ya no dice "falta armar
  el de maquina" (JugadorMaquina ya lo tiene implementado).

`Personaje`
- corregi la etiqueta "tieneBigote=" por "tieneBarba=" en toString(). el
  campo real siempre fue tieneBarba, el texto de debug decia mal.

`JugadorMaquina`
- elegirJugada(): agregue el chequeo `if (mejorPregunta == null) return
  Jugada.arriesgar(getCandidatos().get(0));` antes de usar mejorPregunta.
  cubre el caso limite de que se agoten las preguntasDisponibles antes de
  llegar a 1 solo candidato (antes devolvia Jugada.preguntar(null) y
  explotaba mas adelante con NullPointerException).

`PantallaJuego`
- en finDePartida(), agregue un dialogo "Jugar de nuevo?" al final. si dice
  que si, cierra la ventana (dispose()) y arranca Main.jugarHumanoVsMaquina()
  de nuevo en un hilo aparte (porque ese metodo se queda esperando al humano
  en cada turno, llamarlo directo congelaria la ventana).

`PantallaMaquinaVsMaquina`
- mismo dialogo "Jugar de nuevo?" al terminar la partida. si dice que si,
  cierra la ventana y llama a Main.jugarMaquinaVsMaquina() directo, sin hilo
  aparte (no espera a nadie, solo arma el tablero y programa la ventana
  nueva con invokeLater). 

**pendiente**
- UML con las clases de la interfaz.
- informe final: justificar MergeSort vs QuickSort, explicar el Greedy,
  cuando Greedy NO es optimo, Big O de la app, algoritmos no aplicados,
  y justificacion de SOLID (Open/Closed y Liskov en el diseño de Jugador). 
- agregar tableros visuales al modo Maquina vs Maquina (opcional)

---

## 2026-09-28 - parte 9: avatares visuales, reglas de atributos, preguntas por categoria, orden del tablero

**avatares (lo visual)**
- cada personaje ahora se dibuja segun sus atributos, en vez de ser solo un boton con texto.
- tecnica: composicion por capas. PNG transparentes de 256x256, todos alineados sobre el
  mismo lienzo, que se superponen de atras hacia adelante.
- `avatar_capas/` -> fuentes SVG de las capas (hechas con IA a partir de un prompt con
  anclajes fijos: centro de cabeza, ojos, boca, esquinas reservadas) + `preview.svg`.
  no se usa en tiempo de ejecucion, queda como fuente por si hay que retocar un dibujo.
- `recursos/avatar/` -> las capas convertidas a PNG (se pasaron con resvg; 24 tras sacar "Ondeado"). java lee
  PNG sin librerias externas; SVG hubiera necesitado Batik.
- `AvatarRenderer` (nueva, en UI):
  - `armar(personaje)` superpone las capas en orden: pelo atras -> remera -> cabeza ->
    collar -> arrugas -> cara -> labial -> iris -> ojos -> barba -> pelo frente -> lentes
    -> gorro -> paleta -> simbolo de genero.
  - `icono(personaje, tam)` devuelve el avatar escalado para el boton.
  - las capas de piel, pelo, ojos y remera estan en escala de grises y se tiñen por codigo
    (multiplicacion de color): el gris claro toma el color y el contorno oscuro sigue
    oscuro. asi no hace falta un PNG por color (el pelo son 10 PNG en vez de 10 x colores).
  - los colores de cada valor ("Colorado", "Miel", etc) estan en Maps al principio de la clase.
  - cache: cada PNG se lee una sola vez.
- como se representa cada atributo:
  - genero -> simbolo ♀ rosa / ♂ azul arriba a la derecha.
  - edad -> niño: paleta abajo a la izquierda; joven: nada; adulto: arrugas.
  - pelo -> largo x tipo elige la capa, el color la tiñe. el corto no tiene parte de atras.
  - pelados con barba -> barba negra (no tienen color de pelo).
- `PantallaJuego` -> cada boton muestra el avatar de 96px con el nombre abajo. cuando un
  personaje se descarta y el boton se deshabilita, swing pone la imagen en gris solo.

**reglas de atributos**
- `Personaje` / `ListaPersonajes` -> nuevo atributo `tieneLabial` (equivalente femenino de
  la barba). pregunta nueva "Tiene labial?" en `Main`, y "Labial" en el tooltip.
- barba: solo hombres, y no niños.
- labial: solo mujeres.
- pelo: las mujeres nunca son peladas. los hombres tienen 30% de ser pelados (antes 50%).
- gorro y lentes: 30% de probabilidad (antes 50%). `random.nextDouble() < 0.3`.
- largo de pelo: mujeres medio o largo (nunca corto), hombres corto o medio (nunca largo).
  el generador usa un array de largos por genero; el constructor pasa a "Medio" un largo no
  permitido (`largoSegunGenero`).
- color de pelo: los niños no pueden ser canosos. el generador usa un array sin "Canoso"
  para niños; el constructor pasa a "Negro" un niño canoso (`colorSegunEdad`).
- se elimino el color de pelo "Amarillo" (valor, pregunta y color del avatar): era casi
  igual a "Rubio". OJO: en la parte 4 se habia agregado porque la consigna lo pedia como
  filtro obligatorio. si hace falta, se vuelve a agregar.
- se elimino el tipo de pelo "Ondeado" (valor y pregunta): era casi igual a "Enrulado".
  quedan Lacio y Enrulado. se borraron sus 5 PNG de `recursos/avatar/` (los SVG siguen en
  `avatar_capas/` por si se vuelve a agregar).

**decisiones**
- las reglas se aplican en dos lugares: en el generador (para que el sorteo tenga sentido)
  y en el constructor de `Personaje`, que normaliza igual que ya hacia con el pelo (si no
  tiene pelo -> "N/A"). asi no puede existir un personaje invalido aunque se cree por fuera
  del generador.
- efecto en el juego: "Tiene barba?" = si tambien dice que es hombre y no niño. y gorro /
  lentes al 30% dividen 30/70, asi que la maquina (que busca la division mas pareja) las
  elige mas tarde.

**arriesgar**
- `Jugador.descartarPersonaje(p)` + `Juego.jugarTurno()`: si alguien arriesga y no es, ese
  personaje sale de sus candidatos. en la pantalla, el boton se apaga al empezar el
  siguiente turno (`teToca()` ya apagaba los que no estan en candidatos).
- esto tambien arreglo un bug: cuando a la maquina se le acababan las preguntas utiles,
  arriesgaba siempre `getCandidatos().get(0)` y si no era repetia el mismo personaje para
  siempre (partida infinita). ahora va descartando.
- probado con una partida simulada de peor caso (solo arriesgan, secretos al final): antes
  no terminaba nunca, ahora termina en 45 turnos.

**preguntas por categoria**
- `PantallaJuego` -> la lista de preguntas ahora esta agrupada con encabezados: Genero, Edad,
  Pelo, Cara, Ropa, Accesorios.
- la lista mezcla titulos (String) y preguntas (Pregunta). un renderer propio dibuja los
  titulos en negrita con fondo y las preguntas con sangria. los titulos no se pueden
  seleccionar.
- la categoria sale del atributo de la pregunta (switch en `categoria()`), asi no hubo que
  tocar `Pregunta` ni `Main`. las categorias que se quedan sin preguntas no se muestran.
- el orden se cambia en `ORDEN_CATEGORIAS`.

**ordenar el tablero**
- `PantallaJuego` -> selector "Ordenar tablero por:" arriba a la derecha. opciones: ID
  (original), genero, edad, color/largo/tipo de pelo, color de piel/ojos/remera, y "con X
  primero" para lentes, gorro, collar, barba, labial y pelo. usa los `Criterios` existentes.
- `Criterios.porId()` (nuevo) -> vuelve al orden original.
- ordena con `Sorter.mergeSort` partiendo del orden que se ve. como mergesort es estable se
  pueden encadenar criterios: ordenar por pelo y despues por genero deja agrupado por genero
  y, dentro de cada genero, por pelo. sirve para mostrar la estabilidad en el informe.
- decision: solo reordena los botones, NO llama a `ListaPersonajes.ordenar()`. ese metodo
  reasigna los ids, y el juego compara por id al arriesgar (en otro hilo) y el registro
  muestra "id-nombre". cambiar ids en medio de la partida podia romper eso.
- big o: O(n log n) por cada cambio de criterio.

**problemas**
- en el constructor de `Personaje`, largo/tipo/color de pelo y el mapa de atributos usaban
  el parametro `tienePelo` en vez del campo ya normalizado -> se cambio a `this.tienePelo`.
- a 96px los lentes y el collar se notan poco (los ojos grandes con borde parecen lentes).
  se verifico por pixeles que el dibujo coincide con el atributo; queda el tooltip.

**big o (para el informe)**
- armar un avatar: O(capas x pixeles) = ~15 capas x 256x256. se hace una vez por personaje
  al abrir la pantalla.
- agrupar preguntas por categoria: O(p) por turno, p = preguntas disponibles.

**pendiente**
- UML: agregar `AvatarRenderer` y el nuevo atributo `tieneLabial`.
- informe: seccion de avatares (composicion por capas + tinte).
- confirmar con la consigna si se puede sacar "Amarillo".
- informe: mostrar la estabilidad de mergesort encadenando criterios en el selector de orden.
- UML: agregar `Criterios.porId()`.
- agregar tableros visuales al modo Maquina vs Maquina (opcional, ya se puede reusar
  `AvatarRenderer`).
