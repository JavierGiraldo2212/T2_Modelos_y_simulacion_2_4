package SimulacionInicial;

import java.util.ArrayList;
import java.util.List;

public class RegistroEstado {
    public List<Double> tiempo;
    public List<Integer> clientesEnCola;
    public List<Integer> llamadasEnCola;
    public List<Integer> empleadoOcupado; // 0 o 1

    public RegistroEstado() {
        this.tiempo = new ArrayList<>();
        this.clientesEnCola = new ArrayList<>();
        this.llamadasEnCola = new ArrayList<>();
        this.empleadoOcupado = new ArrayList<>();
    }


    public void Add_estado(double tiempo, int clientesEnCola, int llamadasEnCola, int empleadoOcupado) {
        this.tiempo.add(tiempo);
        this.clientesEnCola.add(clientesEnCola);
        this.llamadasEnCola.add(llamadasEnCola);
        this.empleadoOcupado.add(empleadoOcupado);
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
    public int[] obtenerEmpleadoOcupado() {
        int[] ocupado = new int[empleadoOcupado.size()];
        for (int i = 0; i < empleadoOcupado.size(); i++) {
            ocupado[i] = empleadoOcupado.get(i);
        }
        return ocupado;
    }
}

