package com.financas.visao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainLayoutController {

    @FXML
    private StackPane conteudoArea; // O ID do centro do seu BorderPane no FXML

    @FXML
    public void initialize() {
        // Ao iniciar, carrega por padrão a tela de lançamentos
        exibirTelaLancamentos();
    }

    @FXML
    private void exibirTelaLancamentos() {
        carregarTela("/com/financas/visao/MainView.fxml");
    }

    @FXML
    private void exibirTelaGraficos() {
        carregarTela("/com/financas/visao/graficos/DashboardView.fxml");
    }

    /**
     * Método genérico para trocar o conteúdo do centro da tela.
     */
    private void carregarTela(String caminhoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));
            Parent tela = loader.load();
            
            // Limpa o que estava no centro e coloca a nova tela
            conteudoArea.getChildren().clear();
            conteudoArea.getChildren().add(tela);
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela: " + caminhoFxml);
            e.printStackTrace();
        }
    }
}