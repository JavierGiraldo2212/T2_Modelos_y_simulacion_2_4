package SimulacionMejorada;

import SimLib.*;
import java.util.PriorityQueue;
import java.util.Comparator;

public class NewSimulation {
    RegistroEstado registroEstado = new RegistroEstado();
    private static final float SIM_TIME = 480; // 8 horas en minutos

    private Timer timer = new Timer();
    private boolean employee1Busy = false;
    private boolean employee2Busy = false;

    private List clientQueue = new List("Client Queue");
    private List callQueue = new List("Call Queue");

    private DiscreteStat clientWaitStat = new DiscreteStat();
    private DiscreteStat callWaitStat = new DiscreteStat();

    private float totalBusyTime1 = 0f;
    private float totalBusyTime2 = 0f;
    private float serviceStartTime1 = 0f;
    private float serviceStartTime2 = 0f;

    private PriorityQueue<Event> eventList;

    public NewSimulation() {
        eventList = new PriorityQueue<>(Comparator.comparingDouble(e -> e.time));
        scheduleEvent(new Event(EventType.CLIENT_ARRIVAL, 2));
        scheduleEvent(new Event(EventType.CALL_ARRIVAL, 3));
    }

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
                    processServiceComplete(e.employeeId);
                    break;
            }

            registroEstado.Add_estado(timer.getTime(), clientQueue.size(), callQueue.size(),
                    employee1Busy ? 1 : 0, employee2Busy ? 1 : 0);
        }
        printStats();
    }

    private void processClientArrival() {
        SimObject client = new SimObject();
        client.setTime(timer.getTime());
        clientQueue.insertAtBack(client);

        scheduleNextClient();
        assignNextService();
    }

    private void processCallArrival() {
        SimObject call = new SimObject();
        call.setTime(timer.getTime());
        callQueue.insertAtBack(call);

        scheduleNextCall();
        assignNextService();
    }

    private void scheduleNextClient() {
        float nextArrival = timer.getTime() + Random.expon(12, 1);
        if (nextArrival < SIM_TIME) {
            scheduleEvent(new Event(EventType.CLIENT_ARRIVAL, nextArrival));
        }
    }

    private void scheduleNextCall() {
        float nextCall = timer.getTime() + Random.expon(10, 2);
        if (nextCall < SIM_TIME) {
            scheduleEvent(new Event(EventType.CALL_ARRIVAL, nextCall));
        }
    }

    private void assignNextService() {
        if (!employee1Busy) {
            if (!clientQueue.isEmpty()) {
                startServiceClient(1);
            } else if (!callQueue.isEmpty()) {
                startServiceCall(1);
            }
        }

        if (!employee2Busy) {
            if (!callQueue.isEmpty()) {
                startServiceCall(2);
            } else if (!clientQueue.isEmpty()) {
                startServiceClient(2);
            }
        }
    }

    private void startServiceClient(int employeeId) {
        SimObject client = clientQueue.removeFromFront();
        float waitTime = timer.getTime() - client.getTime();
        clientWaitStat.recordDiscrete(waitTime);

        if (employeeId == 1) {
            employee1Busy = true;
            serviceStartTime1 = timer.getTime();
        } else {
            employee2Busy = true;
            serviceStartTime2 = timer.getTime();
        }

        float serviceTime = Random.expon(6, 3);
        scheduleEvent(new Event(EventType.SERVICE_COMPLETE, timer.getTime() + serviceTime, employeeId));
    }

    private void startServiceCall(int employeeId) {
        SimObject call = callQueue.removeFromFront();
        float waitTime = timer.getTime() - call.getTime();
        callWaitStat.recordDiscrete(waitTime);

        if (employeeId == 1) {
            employee1Busy = true;
            serviceStartTime1 = timer.getTime();
        } else {
            employee2Busy = true;
            serviceStartTime2 = timer.getTime();
        }

        float serviceTime = Random.expon(5, 4);
        scheduleEvent(new Event(EventType.SERVICE_COMPLETE, timer.getTime() + serviceTime, employeeId));
    }

    private void processServiceComplete(int employeeId) {
        float busyPeriod = timer.getTime() - (employeeId == 1 ? serviceStartTime1 : serviceStartTime2);
        if (employeeId == 1) {
            totalBusyTime1 += busyPeriod;
            employee1Busy = false;
        } else {
            totalBusyTime2 += busyPeriod;
            employee2Busy = false;
        }
        assignNextService();
    }

    private void printStats() {
        System.out.printf("Tiempo promedio espera clientes en persona: %.2f minutos\n", clientWaitStat.getDiscreteAverage());
        System.out.printf("Tiempo promedio espera llamadas telefónicas: %.2f minutos\n", callWaitStat.getDiscreteAverage());
        float totalBusy = totalBusyTime1 + totalBusyTime2;
        System.out.printf("Tiempo total ocupado empleado 1: %.2f %%\n", totalBusyTime1/SIM_TIME *100);
        System.out.printf("Tiempo total ocupado empleado 2: %.2f %%\n", totalBusyTime2/SIM_TIME *100);
        System.out.printf("Porcentaje de ocupación del sistema: %.2f %%\n", (totalBusy / (2 * SIM_TIME)) * 100);
    }

    private class Event {
        EventType type;
        float time;
        int employeeId;

        Event(EventType type, float time) {
            this(type, time, -1);
        }

        Event(EventType type, float time, int employeeId) {
            this.type = type;
            this.time = time;
            this.employeeId = employeeId;
        }
    }

    private enum EventType {
        CLIENT_ARRIVAL,
        CALL_ARRIVAL,
        SERVICE_COMPLETE
    }

    public static void main(String[] args) {
        NewSimulation sim = new NewSimulation();
        sim.run();
        System.out.println("Simulación finalizada.");
        System.out.println("Estado final del sistema:");

        double[] tiempos = sim.registroEstado.obtenerTiempos();
        int[] clientesEnCola = sim.registroEstado.obtenerClientesEnCola();
        int[] llamadasEnCola = sim.registroEstado.obtenerLlamadasEnCola();
        int[] empleado1Ocupado = sim.registroEstado.obtenerEmpleado1Ocupado();
        int[] empleado2Ocupado = sim.registroEstado.obtenerEmpleado2Ocupado();

        new GraficosCombinados().mostrarDashboard(sim);
    }
}
