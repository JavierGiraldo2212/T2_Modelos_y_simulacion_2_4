import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;
import org.jfree.data.statistics.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class GraficosCombinados {

    public void mostrarDashboard(Simulation sim) {
        // Obtener datos de la simulación
        double[] tiempos = sim.registroEstado.obtenerTiempos();
        int[] clientesEnCola = sim.registroEstado.obtenerClientesEnCola();
        int[] llamadasEnCola = sim.registroEstado.obtenerLlamadasEnCola();
        int[] empleadoOcupado = sim.registroEstado.obtenerEmpleadoOcupado();

        // Configurar ventana principal
        JFrame ventana = new JFrame("Dashboard de Simulación de Colas");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(1200, 800);
        ventana.setLocationRelativeTo(null);

        // Crear panel con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Pestaña de Evolución de Colas
        tabbedPane.addTab("Evolución Colas", crearPanelEvolucionColas(tiempos, clientesEnCola, llamadasEnCola));

        // 2. Pestaña de Ocupación de Empleado
        tabbedPane.addTab("Ocupación Empleado", crearPanelOcupacionEmpleado(tiempos, empleadoOcupado));


        ventana.add(tabbedPane);
        ventana.setVisible(true);
    }

    private JPanel crearPanelEvolucionColas(double[] tiempos, int[] clientes, int[] llamadas) {
        XYSeries serieClientes = new XYSeries("Clientes en persona");
        XYSeries serieLlamadas = new XYSeries("Llamadas");

        for (int i = 0; i < tiempos.length; i++) {
            serieClientes.add(tiempos[i], clientes[i]);
            serieLlamadas.add(tiempos[i], llamadas[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieClientes);
        dataset.addSeries(serieLlamadas);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Evolución de las Colas en el Tiempo",
                "Tiempo (minutos)",
                "Número en Cola",
                dataset
        );

        // Personalización del gráfico
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        // Estilo para clientes en persona
        renderer.setSeriesPaint(0, new Color(46, 134, 222)); // Azul
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        // Estilo para llamadas
        renderer.setSeriesPaint(1, new Color(231, 76, 60)); // Rojo
        renderer.setSeriesStroke(1, new BasicStroke(2f));
        renderer.setSeriesShape(1, new Rectangle2D.Double(-3, -3, 6, 6));

        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1150, 700));
        return chartPanel;
    }

    private JPanel crearPanelOcupacionEmpleado(double[] tiempos, int[] ocupacion) {
        XYSeries serieOcupacion = new XYSeries("Estado del Empleado");

        for (int i = 0; i < tiempos.length; i++) {
            serieOcupacion.add(tiempos[i], ocupacion[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieOcupacion);

        JFreeChart chart = ChartFactory.createXYStepChart(
                "Ocupación del Empleado en el Tiempo",
                "Tiempo (minutos)",
                "Estado (0=Libre, 1=Ocupado)",
                dataset
        );

        // Personalización del gráfico
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(39, 174, 96)); // Verde
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShapesVisible(0, false);

        plot.setRenderer(renderer);

        // Configurar eje Y para mostrar solo 0 y 1
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setRange(0, 1.1);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1150, 700));
        return chartPanel;
    }

    private JPanel crearPanelHistogramas(double[] tiemposClientes, double[] tiemposLlamadas) {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        // Histograma para clientes en persona
        HistogramDataset datasetClientes = new HistogramDataset();
        datasetClientes.addSeries("Clientes en persona", tiemposClientes, 20);

        JFreeChart histClientes = ChartFactory.createHistogram(
                "Distribución de Tiempos de Espera - Clientes en Persona",
                "Tiempo de espera (minutos)",
                "Frecuencia",
                datasetClientes,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Histograma para llamadas
        HistogramDataset datasetLlamadas = new HistogramDataset();
        datasetLlamadas.addSeries("Llamadas", tiemposLlamadas, 20);

        JFreeChart histLlamadas = ChartFactory.createHistogram(
                "Distribución de Tiempos de Espera - Llamadas",
                "Tiempo de espera (minutos)",
                "Frecuencia",
                datasetLlamadas,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Personalizar histogramas
        styleHistogram(histClientes, new Color(46, 134, 222)); // Azul
        styleHistogram(histLlamadas, new Color(231, 76, 60)); // Rojo

        panel.add(new ChartPanel(histClientes));
        panel.add(new ChartPanel(histLlamadas));

        return panel;
    }

    private void styleHistogram(JFreeChart histogram, Color color) {
        histogram.setBackgroundPaint(Color.WHITE);

        XYPlot plot = histogram.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, color);
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
    }
}