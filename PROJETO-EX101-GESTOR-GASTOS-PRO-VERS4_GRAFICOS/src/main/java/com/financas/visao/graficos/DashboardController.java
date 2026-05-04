package com.financas.visao.graficos;

import com.financas.dados.ConexaoDAO;
import com.financas.modelo.DashboardDTO;
import com.financas.modelo.TransacaoDetalheDTO;
import com.financas.servico.DashBoardService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.util.List;

public class DashboardController {

    @FXML private VBox containerGraficoPizza; 
    @FXML private VBox containerGraficoBarras; 
    
    // Itens da Tabela
    @FXML private TableView<TransacaoDetalheDTO> tabelaHistorico;
    @FXML private TableColumn<TransacaoDetalheDTO, String> colProduto;
    @FXML private TableColumn<TransacaoDetalheDTO, String> colMembro;
    @FXML private TableColumn<TransacaoDetalheDTO, Double> colQuantidade; // ADICIONADO: Para não dar erro
    @FXML private TableColumn<TransacaoDetalheDTO, Double> colValor;

    private DashBoardService dashBoardService;

    @FXML
    public void initialize() {
        try {
            Connection conexao = ConexaoDAO.conectar();
            dashBoardService = new DashBoardService(conexao);
            
            configurarColunasTabela();
            atualizarDashboard();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarColunasTabela() {
        // Vinculação com os atributos do seu TransacaoDetalheDTO
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colMembro.setCellValueFactory(new PropertyValueFactory<>("nomeMembro"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade")); // CONFIGURADO
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorParcela"));
    }

    public void atualizarDashboard() {
        // 1. Gráfico de Pizza
        List<DashboardDTO> dadosPizza = dashBoardService.carregarDadosPizza();
        containerGraficoPizza.getChildren().clear();
        containerGraficoPizza.getChildren().add(GeradorGraficos.criarGraficoPizza(dadosPizza, "Gastos por Categoria"));

        // 2. Gráfico de Barras
        List<DashboardDTO> dadosMembros = dashBoardService.carregarDadosMembros(); 
        containerGraficoBarras.getChildren().clear();
        containerGraficoBarras.getChildren().add(GeradorGraficos.criarGraficoBarras(dadosMembros, "Gastos por Membro"));

        // 3. VINCULAR À TABELA
        List<TransacaoDetalheDTO> itensDoBanco = dashBoardService.carregarHistoricoItens();
        
        if (itensDoBanco != null) {
            tabelaHistorico.setItems(FXCollections.observableArrayList(itensDoBanco));
            tabelaHistorico.refresh();
        }
    }
}