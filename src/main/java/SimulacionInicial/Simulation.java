package SimulacionInicial;

import SimLib.*;

import java.util.PriorityQueue;
import java.util.Comparator;

public class Simulation {
    RegistroEstado registroEstado = new RegistroEstado();
    private static final float SIM_TIME = 480; // 8 horas en minutos

    // Estado del sistema
    private Timer timer = new Timer();
    private boolean employeeBusy = false;

    private List clientQueue = new List("Client Queue");
    private List callQueue = new List("Call Queue");

    private DiscreteStat clientWaitStat = new DiscreteStat();
    private DiscreteStat callWaitStat = new DiscreteStat();

    private float totalBusyTime = 0f;
    private float serviceStartTime = 0f;

    // Eventos en una cola priorizada por tiempo
    private PriorityQueue<Event> eventList;



    // Constructor
    public Simulation() {
        eventList = new PriorityQueue<>(Comparator.comparingDouble(e -> e.time));

        // Inicializar generadores aleatorios con semillas basadas en el tiempo actual

        /*long semillaBase = System.currentTimeMillis();
        Random.randst(semillaBase % 2147483647, 1);          // Para llegadas de clientes
        Random.randst((semillaBase + 123) % 2147483647, 2);  // Para llegadas de llamadas
        Random.randst((semillaBase + 456) % 2147483647, 3);  // Para servicios de clientes
        Random.randst((semillaBase + 789) % 2147483647, 4);  // Para servicios de llamadas
        */
        // Programar primeras llegadas
        scheduleEvent(new Event(EventType.CLIENT_ARRIVAL, 2));
        scheduleEvent(new Event(EventType.CALL_ARRIVAL, 3));
    }

    // Agrega evento a la lista
    private void scheduleEvent(Event e) {
        eventList.add(e);
    }

    public void run() {
        while (timer.getTime() < SIM_TIME && !eventList.isEmpty()) {
            Event e = eventList.poll();
            timer.setTime(e.time);

            switch (e.type) {
                case CLIENT_ARRIVAL:
                    processClientArrival();
                    break;
                case CALL_ARRIVAL:
                    processCallArrival();
                    break;
                case SERVICE_COMPLETE:
                    processServiceComplete();
                    break;
            }
            // Registrar estado del sistema
            registroEstado.Add_estado(timer.getTime(), clientQueue.size(), callQueue.size(), employeeBusy ? 1 : 0);
        }
        printStats();
    }

    private void processClientArrival() {
        SimObject client = new SimObject();
        client.setTime(timer.getTime());  // Arrival time
        clientQueue.insertAtBack(client);

        // Si empleado está libre, comenzar servicio
        if (!employeeBusy) {
            startServiceClient();
        }

        // Programar próxima llegada cliente
        float nextArrival = timer.getTime() + Random.expon(12, 1);
        if (nextArrival < SIM_TIME) {
            scheduleEvent(new Event(EventType.CLIENT_ARRIVAL, nextArrival));
        }
    }

    private void processCallArrival() {
        SimObject call = new SimObject();
        call.setTime(timer.getTime());
        callQueue.insertAtBack(call);

        if (!employeeBusy && clientQueue.isEmpty()) {
            startServiceCall();
        }

        // Programar próxima llegada llamada
        float nextCall = timer.getTime() + Random.expon(10, 2);
        if (nextCall < SIM_TIME) {
            scheduleEvent(new Event(EventType.CALL_ARRIVAL, nextCall));
        }
    }

    private void startServiceClient() {
        SimObject client = clientQueue.removeFromFront();
        employeeBusy = true;

        // Calcular tiempo de espera
        float waitTime = timer.getTime() - client.getTime();
        clientWaitStat.recordDiscrete(waitTime);

        // Guardar tiempo inicio servicio
        serviceStartTime = timer.getTime();

        // Generar tiempo de servicio
        float serviceTime = Random.expon(6, 3);
        scheduleEvent(new Event(EventType.SERVICE_COMPLETE, timer.getTime() + serviceTime));
    }

    private void startServiceCall() {
        SimObject call = callQueue.removeFromFront();
        employeeBusy = true;

        float waitTime = timer.getTime() - call.getTime();
        callWaitStat.recordDiscrete(waitTime);

        serviceStartTime = timer.getTime();

        float serviceTime = Random.expon(5, 4);
        scheduleEvent(new Event(EventType.SERVICE_COMPLETE, timer.getTime() + serviceTime));
    }

    private void processServiceComplete() {
        float busyPeriod = timer.getTime() - serviceStartTime;
        totalBusyTime += busyPeriod;

        employeeBusy = false;
        if (!clientQueue.isEmpty()) {
            startServiceClient();
        } else if (!callQueue.isEmpty()) {
            startServiceCall();
        }
    }

    private void printStats() {
        System.out.printf("Tiempo promedio espera clientes en persona: %.2f minutos\n", clientWaitStat.getDiscreteAverage());
        System.out.printf("Tiempo promedio espera llamadas telefónicas: %.2f minutos\n", callWaitStat.getDiscreteAverage());
        System.out.printf("Porcentaje de ocupación del empleado: %.2f %%\n", (totalBusyTime / SIM_TIME) * 100);
    }

    // Clase interna para eventos
    private class Event {
        EventType type;
        float time;

        Event(EventType type, float time) {
            this.type = type;
            this.time = time;
        }
    }

    private enum EventType {
        CLIENT_ARRIVAL,
        CALL_ARRIVAL,
        SERVICE_COMPLETE
    }

    public static void main(String[] args) {
        Simulation sim = new Simulation();
        sim.run();
        System.out.println("Simulación finalizada.");
        System.out.println("Estado final del sistema:");

        // Almacenamiento de resultados para su graficación
        double [] tiempos = sim.registroEstado.obtenerTiempos();
        int [] clientesEnCola = sim.registroEstado.obtenerClientesEnCola();
        int [] llamadasEnCola = sim.registroEstado.obtenerLlamadasEnCola();
        int [] empleadoOcupado = sim.registroEstado.obtenerEmpleadoOcupado();
        // Mostrar dashboard con gráficos
        new GraficosCombinados().mostrarDashboard(sim);
    }
}
