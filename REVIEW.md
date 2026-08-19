Review de diseño POO
Hallazgo 1: El sensor puede dar un estado sin haber tomado una lectura
Los sensores de humedad y temperatura guardan sus datos en variables double. El
problema es que Java les da el valor 0.0 por defecto, entonces si se llama a evaluarEstado()
antes de tomarLectura(), el programa piensa que ese 0.0 es una medición real.
Por ejemplo, un sensor de humedad recién creado podría decir que está en estado CRÍTICO solo porque tiene el valor inicial en 0.0, aunque realmente nunca haya medido nada.
Posible solución: usar una forma de saber si el sensor ya tomó una lectura o no. Por ejemplo, una variable que indique si ya se midió, o dejar el valor como null hasta que exista una lectura. Así, antes de medir, el estado podría ser SIN_DATOS y solo después evaluar si está normal, crítico, etc.
Hallazgo 2: La clase EstacionMonitoreo hace varias cosas al mismo tiempo
La clase EstacionMonitoreo en el método procesarLecturas() recorre los sensores, revisa cuáles están activos y además imprime los resultados en consola con System.out.println.
No es un error grave, pero sería mejor separar esas tareas. Así el método podría encargarse solo de procesar las lecturas y otra parte del programa mostrar los resultados.
Posible solución: hacer que procesarLecturas() devuelva los resultados y luego imprimirlos desde el main u otra clase encargada de mostrar la información.
Review
En general estos errores son corregibles facilmente, pero en si el codigo demuestra estar bastante completo y sin muchos problemas a pura lectura profunda, por lo que puedo apreciar eso esta bien, con las mejoras deberia poder funcionar de manera mas estructurada el codigo.
Por Joseph Sebastian Martínez Forero

