package SimulacionInicial;

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

        JFrame ventana = new JFrame("Dashboard de Simulación de Colas");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(1200, 800);
        ventana.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Paneles separados
        tabbedPane.addTab("Cola Clientes", crearPanelClientesEnCola(tiempos, clientesEnCola));
        tabbedPane.addTab("Cola Llamadas", crearPanelLlamadasEnCola(tiempos, llamadasEnCola));
        tabbedPane.addTab("Ocupación Empleado", crearPanelOcupacionEmpleado(tiempos, empleadoOcupado));

        ventana.add(tabbedPane);
        ventana.setVisible(true);
    }

    private JPanel crearPanelClientesEnCola(double[] tiempos, int[] clientes) {
        XYSeries serieClientes = new XYSeries("Clientes en persona");

        for (int i = 0; i < tiempos.length; i++) {
            serieClientes.add(tiempos[i], clientes[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieClientes);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Evolución de Clientes en Cola",
                "Tiempo (minutos)",
                "Número de Clientes",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        // Establecer el rango del eje X (0 a 480)
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setRange(0, 480);
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());


        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(46, 134, 222)); // Azul
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));

        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1150, 700));
        return chartPanel;
    }

    private JPanel crearPanelLlamadasEnCola(double[] tiempos, int[] llamadas) {
        XYSeries serieLlamadas = new XYSeries("Llamadas");

        for (int i = 0; i < tiempos.length; i++) {
            serieLlamadas.add(tiempos[i], llamadas[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieLlamadas);


        JFreeChart chart = ChartFactory.createXYLineChart(
                "Evolución de Llamadas en Cola",
                "Tiempo (minutos)",
                "Número de Llamadas",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        // Establecer el rango del eje X (0 a 480)
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setRange(0, 480);
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());


        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(231, 76, 60)); // Rojo
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShape(0, new Rectangle2D.Double(-3, -3, 6, 6));

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

        // Crear ejes explícitamente como NumberAxis
        NumberAxis domainAxis = new NumberAxis("Tiempo (minutos)");
        domainAxis.setRange(0, 480);
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        NumberAxis rangeAxis = new NumberAxis("Estado (0=Libre, 1=Ocupado)");
        rangeAxis.setRange(0, 1.1);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Crear renderer para la gráfica tipo step
        XYStepRenderer renderer = new XYStepRenderer();
        renderer.setSeriesPaint(0, new Color(39, 174, 96)); // Verde
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShapesVisible(0, false);

        // Crear el plot manualmente
        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        // Crear el chart con el plot
        JFreeChart chart = new JFreeChart("Ocupación del Empleado en el Tiempo", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

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