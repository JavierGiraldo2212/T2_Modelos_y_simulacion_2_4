

# Simulación de Atención en un Teatro – SimLib Java (OOP)

Este proyecto simula el sistema de atención al cliente en un teatro que opera desde las 9:00 a. m. hasta las 5:00 p. m. utilizando la biblioteca **SimLib** orientada a objetos en Java. El objetivo es analizar el tiempo promedio de espera de los clientes en persona y las llamadas telefónicas, así como el porcentaje de ocupación del empleado.

## Enunciado del Problema

El teatro cuenta con un **único empleado** que debe:
- Vender tiquetes y responder consultas a **clientes en persona**
- Responder **llamadas telefónicas**

### Reglas del sistema:
- **Clientes en persona tienen prioridad** sobre las llamadas.
- **Llamadas esperan indefinidamente** hasta ser atendidas (FIFO).
- El **empleado atiende solo un cliente a la vez**.
- La **jornada dura 480 minutos (8 horas)**.

## Parámetros de Simulación

| Evento                   | Distribución         | Media (minutos) |
|--------------------------|----------------------|-----------------|
| Llegada clientes         | Exponencial          | 12              |
| Llegada llamadas         | Exponencial          | 10              |
| Servicio clientes        | Exponencial          | 6               |
| Servicio llamadas        | Exponencial          | 5               |
| Primera llegada cliente  | Fija                 | 2               |
| Primera llegada llamada  | Fija                 | 3               |

## Estructura del Proyecto

- `Simulation.java`: Lógica principal de simulación
- `SimLib/`: Librería utilizada para estructuras de eventos, listas, estadísticas, etc.
- `RegistroEstado.java`: Registra el estado del sistema en el tiempo
- `GraficosCombinados.java`: Genera gráficos (opcional si se incluye visualización)

## Variables y Estructuras Clave

| Variable              | Propósito                                              |
|-----------------------|--------------------------------------------------------|
| `clientQueue`         | Cola FIFO de clientes presenciales                     |
| `callQueue`           | Cola FIFO de llamadas telefónicas                      |
| `eventList`           | Cola de prioridad para eventos futuros                 |
| `clientWaitStat`      | Estadísticas de espera de clientes                     |
| `callWaitStat`        | Estadísticas de espera de llamadas                     |
| `totalBusyTime`       | Tiempo total que el empleado estuvo ocupado            |
| `employeeBusy`        | Estado del empleado (ocupado/libre)                    |
| `timer`               | Tiempo actual de simulación                            |

## Subprogramas Clave

| Método                  | Función principal                                                |
|--------------------------|------------------------------------------------------------------|
| `scheduleEvent(Event e)` | Programa un nuevo evento en la cola                             |
| `processClientArrival()` | Maneja la llegada de un cliente                                 |
| `processCallArrival()`   | Maneja la llegada de una llamada                                |
| `startServiceClient()`   | Inicia atención de un cliente                                   |
| `startServiceCall()`     | Inicia atención de una llamada                                  |
| `processServiceComplete()`| Finaliza el servicio actual y selecciona el siguiente           |
| `printStats()`           | Muestra estadísticas al final de la simulación                  |

## Métricas de Desempeño

- **Tiempo promedio de espera** de:
  - Clientes en persona
  - Llamadas telefónicas
- **Porcentaje de ocupación del empleado**


## Ejemplo de salida

```
Tiempo promedio espera clientes en persona: 3.24 minutos
Tiempo promedio espera llamadas telefónicas: 6.75 minutos
Porcentaje de ocupación del empleado: 82.63 %
```

## Visualización

El sistema puede generar gráficos que muestran:

* Evolución de la cola de clientes
* Evolución de la cola de llamadas
* Estado del empleado (ocupado/libre)

(Esto requiere una clase adicional: `GraficosCombinados`)

## Bibliotecas

* **SimLib (OOP)**: Librería de simulación discreta usada para eventos, listas, estadísticas, etc.
* **Java Standard Libraries**: Para estructura de datos y control de flujo
