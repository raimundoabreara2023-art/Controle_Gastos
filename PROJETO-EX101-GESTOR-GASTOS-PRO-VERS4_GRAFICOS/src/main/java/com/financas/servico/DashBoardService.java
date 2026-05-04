package com.financas.servico;

import com.financas.dados.EstatisticasDAO;
import com.financas.modelo.DashboardDTO;
import com.financas.modelo.TransacaoDetalheDTO;
import java.sql.Connection;
import java.util.List;

public class DashBoardService {
    
    private EstatisticasDAO estatisticasDAO;

    public DashBoardService(Connection conexao) {
        this.estatisticasDAO = new EstatisticasDAO(conexao);
    }

    /**
     * Retorna a lista de gastos por categoria para o gráfico de pizza.
     */
    public List<DashboardDTO> carregarDadosPizza() {
        return estatisticasDAO.buscarGastosPorCategoria();
    }

    /**
     * NOVO: Retorna a lista de gastos por membro para o gráfico de barras.
     */
    public List<DashboardDTO> carregarDadosMembros() {
        return estatisticasDAO.buscarGastosPorMembro();
    }

    /**
     * NOVO: Retorna todos os itens salvos para preencher a TableView de histórico.
     */
    public List<TransacaoDetalheDTO> carregarHistoricoItens() {
        return estatisticasDAO.buscarTodosItens();
    }

    /**
     * Calcula o total geral dos dados carregados.
     */
    public double calcularTotalGeral(List<DashboardDTO> dados) {
        return dados.stream()
                    .mapToDouble(DashboardDTO::getValor)
                    .sum();
    }
}