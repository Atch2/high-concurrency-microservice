# DECISIONES TÉCNICAS

## Arquitectura: Hexagonal (Puertos y Adaptadores)

Utilicé la arquitectura hexagonal, con el patrón puertos y adaptadores que nos da un gran beneficio de separar la lógica de negocio, esto ayuda a que si se cambia la infraestructura no se vea afectado, también facilita las pruebas unitarias sin levantar el contexto de Spring, con mantenibilidad y escalabilidad.

## Concurrencia: Bloqueo Granular y Ordenado

Para el caso de transferencias simultáneas, manejé de manera segura y eficiente con mecanismos de bloqueo granular y ordenado, para eliminar los Deadlocks y tener un rendimiento eficiente, para esto utilicé ReentrantLock de manera individual y por cuenta.

## Integridad de Datos: Doble Verificación

Para garantizar la integridad de los datos, implementé un mecanismo de doble verificación, sin lock para comprobar la existencia de la transacción antes de intentar bloquear los recursos y con lock para que se vuelva a comprobar, en la precisión de los datos en cálculos monetarios me generaba un error con el tipo de dato double, y utilice BigDecimal para solventar el error.

## Almacenamiento Thread-Safe

Para el almacenamiento Thread-Safe, use ConcurrentHashMap como estructura de datos en memoria.

## Análisis del Threshold K6

Se me presentó un problema, ya que el umbral threshold configurado en el script k6 era inalcanzable con la lógica de la prueba, ya que solo registraba el éxito de los invariantes (invariants_ok.add(1)) una única vez durante la fase de teardown (al final de toda la ejecución).
