# Bitácora de Aprendizaje y Evolución Arquitectónica
Curso / Contexto: Laboratorio POO Java — Universidad de los Llanos
Dominio: Sistemas de Escala Agrícola e Industrial (Orinoquía)
Objeto de Desarrollo: Sistema de Monitoreo de Sensores (Sensor, SensorHumedadSuelo,
SensorTemperatura, EstacionMonitoreo)
Resumen Ejecutivo de la Interacción
La interacción se estructuró bajo la modalidad de un Code Review Socrático de Nivel Senior. A
través de tres hitos iterativos, la relación con la IA operó bajo la restricción de cero código
autogenerado por la IA, forzando la deducción, el análisis de diseño, la identificación de code
smells y la fundamentación teórica (Java vs. C++, concurrencia, SOLID) antes de cada
refactorización.
EVOLUCIÓN DEL SISTEMA
HITO 1: Encapsulamiento Base
• Abstracción Sensor
• Principio DRY
• Visibilidad Hilos
HITO 2: Herencia y Polimorfismo
• Subclases concretas
• Eliminación Setters
• Defensas de Estado

HITO 3: Colecciones y Diseño
• EstacionMonitoreo
• Filtro de Estado
• Fronteras de OCP

Cronología del Proceso de Ingenierización
Hito 1 — Encapsulamiento y Abstracción Base
1. Diagnóstico Inicial y Desafíos Planteados
La entrega inicial de la clase abstracta Sensor presentaba un contrato incompleto y redundancias
en las validaciones de las invariantes de clase:

• Omisión de Contrato: Faltaba definir el método abstracto evaluarEstado() especificado en el
diagrama de clases.
• Violación de DRY (Don't Repeat Yourself): Las reglas de validación para la variable ubicacion
estaban duplicadas tanto en el constructor como en el método setUbicacion().
• Duda Teórica sobre Concurrencia: Incertidumbre sobre el uso de la palabra clave volatile en
atributos modificables (ubicacion, activo) frente a atributos final (id).
2. Proceso Socrático y Razonamiento
• Análisis de volatile: Se diferenció la visibilidad en memoria principal (evitar cachés locales por
hilo) de la atomicidad en operaciones compuestas (read-modify-write). Se justificó que id no
requiere volatile por ser final (su publicación segura ocurre durante la construcción del objeto).
• Decisión de Diseño de Interfaz: Se optó por métodos explícitos e intencionales (activar() /
desactivar()) sobre un setter genérico (setActivo(boolean)), protegiendo la semántica del dominio
de negocio.
3. Resultados y Refactorización
• Centralización de la validación mediante el método privado validarUbicacion(String ubicacion).
• Adición del método abstracto public abstract String evaluarEstado().
• Corrección de convenciones del estándar JavaBeans / Reflexión: cambio de getActivo() a
isActivo().
Hito 2 — Herencia y Especialización Polimórfica
1. Diagnóstico Inicial y Desafíos Planteados
Se implementaron las especializaciones SensorHumedadSuelo y SensorTemperatura,
evidenciando fallas severas de encapsulamiento y sintaxis:
• Falta de Constructores Explícitos: Error de compilación por ausencia de constructor implícito sin
argumentos en la superclase Sensor.
• Corrupción de Estado: Presencia de setters públicos sin validación para humedadPct y celsius,
permitiendo modificar los valores medidos externamente sin pasar por los sensores físicos.
• Inconsistencia de Nombres y Sintaxis: Violación de convenciones camelCase (gethumedadPct) y
errores sintácticos.
2. Proceso Socrático y Razonamiento
• Protección del Encapsulamiento: Se argumentó que la única fuente de verdad para la alteración
de variables de estado de lectura debe ser el método tomarLectura(). Los setters públicos rompían
el encapsulamiento de los sensores.
• El Problema del Estado "Sin Datos": Se identificó que las variables primitivas double se
inicializan en 0.0. Esto provocaba que llamar a evaluarEstado() antes de invocar tomarLectura()
generara un falso positivo de estado "CRITICO" (ej. humedad a 0%).

• Estrategias planteadas: Uso de banderas de control (boolean lecturaTomada) o empaquetadores
de objetos (wrappers nullables como Double) para representar la ausencia de datos antes de la
primera medición.
3. Resultados y Refactorización
• Eliminación total de setters mutables externos en las subclases.
• Implementación estricta de constructores delegados mediante super(id, ubicacion, activo).
• Normalización de getters bajo estándar camelCase (getHumedadPct(), getCelsius()).
Hito 3 — Procesamiento Polimórfico en Colecciones y Principios SOLID
1. Diagnóstico Inicial y Desafíos Planteados
Se diseñó la clase gestora EstacionMonitoreo utilizando List para procesar colecciones
heterogéneas. La revisión detectó dos problemas críticos:
• Asimetría de Filtrado (Bug de Dominio): Mientras que procesarLecturas() comprobaba
sensor.isActivo(), el método obtenerSensoresCriticos() iteraba sobre toda la lista sin verificar la
actividad del sensor. Un sensor inactivo con lectura por defecto (0.0) era erróneamente clasificado
como "CRITICO".
• Debate sobre OCP (Open/Closed Principle): La necesidad de definir con precisión si la inclusión
de un nuevo tipo de sensor (SensorCalidadAire) requería modificar clases existentes y dónde se
situaba el límite de la extensión.
2. Proceso Socrático y Razonamiento
• Defensa contra NullPointerException: En la evaluación de cadenas, se determinó usar
"CRITICO".equals(sensor.evaluarEstado()) en lugar de
sensor.evaluarEstado().equals("CRITICO"), garantizando un código defensivo ante posibles
retornos nulos.
• Fronteras del Principio Abierto/Cerrado (OCP):
- ¿Crear subclases viola OCP? No, es extensión por polimorfismo.
- ¿Modificar EstacionMonitoreo viola OCP? Sí, si requiere alterar su código para soportar el nuevo
sensor.
- ¿Instanciar el objeto en Main viola OCP? No. El punto de entrada o fábrica (Wiring / Composition
Root) es el lugar legítimo donde se configura la aplicación. Crear código nuevo para conectar
abstracciones no es una modificación del comportamiento core del sistema.
3. Resultados y Refactorización
• Corrección en obtenerSensoresCriticos() para evaluar de forma coordinada la condición de
actividad e inocuidad ante nulos:
if (sensor.isActivo() && "CRITICO".equals(sensor.evaluarEstado()))
• Implementación de copia defensiva en getSensores() retornando new ArrayList<>(sensores)
para evitar la mutación de la colección interna desde el exterior.

Para dar por concluida la fase académica e integrar los requisitos del análisis comparativo C++ vs.
Java, se registran los siguientes puntos de análisis:
1. Polimorfismo y Dispatch Dinámico:
• C++: Resolución en tiempo de ejecución a través de la tabla de métodos virtuales (vtable) y
punteros de vtable (vptr). Requiere el uso explícito de la palabra clave virtual.
• Java: Todos los métodos no estáticos, no finales y no privados son virtuales por defecto. La
Máquina Virtual de Java (JVM) maneja la invocación mediante las instrucciones de bytecode
invokevirtual o invokeinterface.

2. Concurrencia en la Estación de Monitoreo:
• ArrayList no es una colección segura para entornos multihilo (thread-safe).
• Para escenarios de alta concurrencia en entornos industriales, se debe documentar el uso de
alternativas de java.util.concurrent, como CopyOnWriteArrayList (ideal para lecturas frecuentes y
pocas escrituras) o sincronización explícita sobre el bloque del recurso compartido.
3. Gestión de Memoria:
• C++: Control explícito del ciclo de vida del objeto (asignación en heap con new y liberación con
delete o mediante punteros inteligentes como std::unique_ptr / std::shared_ptr). Riesgo alto de
memory leaks o dangling pointers.
• Java: Gestión automática de memoria delegada al Garbage Collector (GC), basándose en
algoritmos de alcanzabilidad de objetos (Mark and Sweep, G1GC, ZGC).
Estado Final del Entregable
El diseño orientable a objetos ha alcanzado el nivel de madurez requerido para producción:
cumple con las invariantes de encapsulamiento, respeta el principio OCP en la jerarquía y
manipulación de colecciones, aplica programación defensiva y queda preparado para la fase de
documentación y evaluación entre pares (Peer Review).
