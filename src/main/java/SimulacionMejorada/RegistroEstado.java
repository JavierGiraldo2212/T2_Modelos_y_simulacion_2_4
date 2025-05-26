package SimulacionMejorada;

import java.util.ArrayList;
import java.util.List;

public class RegistroEstado {
    public List<Double> tiempo;
    public List<Integer> clientesEnCola;
    public List<Integer> llamadasEnCola;
    public List<Integer> empleado1Ocupado; // 0 o 1
    public List<Integer> empleado2Ocupado; // 0 o 1


    public RegistroEstado() {
        this.tiempo = new ArrayList<>();
        this.clientesEnCola = new ArrayList<>();
        this.llamadasEnCola = new ArrayList<>();
        this.empleado1Ocupado = new ArrayList<>();
        this.empleado2Ocupado = new ArrayList<>();
    }


    public void Add_estado(double tiempo, int clientesEnCola, int llamadasEnCola, int empleado1Ocupado, int empleado2Ocupado) {
        this.tiempo.add(tiempo);
        this.clientesEnCola.add(clientesEnCola);
        this.llamadasEnCola.add(llamadasEnCola);
        this.empleado1Ocupado.add(empleado1Ocupado);
        this.empleado2Ocupado.add(empleado2Ocupado);
    }

    public double[] obtenerTiempos() {
        double[] tiempos = new double[tiempo.size()];
        for (int i = 0; i < tiempo.size(); i++) {
            tiempos[i] = tiempo.get(i);
        }
        return tiempos;
    }
    public int[] obtenerClientesEnCola() {
        int[] clientes = new int[clientesEnCola.size()];
        for (int i = 0; i < clientesEnCola.size(); i++) {
            clientes[i] = clientesEnCola.get(i);
        }
        return clientes;
    }
    public int[] obtenerLlamadasEnCola() {
        int[] llamadas = new int[llamadasEnCola.size()];
        for (int i = 0; i < llamadasEnCola.size(); i++) {
            llamadas[i] = llamadasEnCola.get(i);
        }
        return llamadas;
    }
    public int[] obtenerEmpleado1Ocupado() {
        int[] ocupado = new int[empleado1Ocupado.size()];
        for (int i = 0; i < empleado1Ocupado.size(); i++) {
            ocupado[i] = empleado1Ocupado.get(i);
        }
        return ocupado;
    }

    public int[] obtenerEmpleado2Ocupado() {
        int[] ocupado = new int[empleado2Ocupado.size()];
        for (int i = 0; i < empleado2Ocupado.size(); i++) {
            ocupado[i] = empleado2Ocupado.get(i);
        }
        return ocupado;
    }
}