package com.financas.visao.graficos;

import com.financas.modelo.DashboardDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import java.util.List;
import java.util.stream.Collectors;

public class GeradorGraficos {

    /**
     * Gráfico de Pizza (Categorias)
     */
    public static PieChart criarGraficoPizza(List<DashboardDTO> dados, String titulo) {
        ObservableList<PieChart.Data> dadosGrafico = dados.stream()
            .map(dto -> new PieChart.Data(dto.getNome(), dto.getValor()))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));

        PieChart grafico = new PieChart(dadosGrafico);
        grafico.setTitle(titulo);
        grafico.setLegendSide(Side.RIGHT);
        grafico.setLabelsVisible(true);
        
        aplicarCss(grafico);
        return grafico;
    }

    /**
     * NOVO: Gráfico de Barras (Membros da Família)
     */
    public static BarChart<String, Number> criarGraficoBarras(List<DashboardDTO> dados, String titulo) {
        // 1. Configura os eixos (X = Nomes, Y = Valores)
        CategoryAxis eixoX = new CategoryAxis();
        NumberAxis eixoY = new NumberAxis();
        eixoX.setLabel("Membros");
        eixoY.setLabel("Valor (R$)");

        // 2. Cria o gráfico de barras
        BarChart<String, Number> grafico = new BarChart<>(eixoX, eixoY);
        grafico.setTitle(titulo);
        grafico.setLegendVisible(false); // Geralmente barras não precisam de legenda se o eixo X já tem nomes

        // 3. Converte os dados para uma Série (Series)
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (DashboardDTO dto : dados) {
            serie.getData().add(new XYChart.Data<>(dto.getNome(), dto.getValor()));
        }

        grafico.getData().add(serie);
        
        aplicarCss(grafico);
        return grafico;
    }

    /**
     * Método auxiliar para não repetir código de CSS
     */
    private static void aplicarCss(javafx.scene.layout.Region grafico) {
        try {
            String css = GeradorGraficos.class.getResource("/com/financas/estilo/estilo-graficos.css").toExternalForm();
            grafico.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Aviso: CSS de gráficos não encontrado.");
        }
    }
}