package com.financas.dados;

import com.financas.modelo.DashboardDTO;
import com.financas.modelo.TransacaoDetalheDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstatisticasDAO {
    private Connection conexao;

    public EstatisticasDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // 1. Método original (Categorias)
    public List<DashboardDTO> buscarGastosPorCategoria() {
        List<DashboardDTO> lista = new ArrayList<>();
        String sql = "SELECT c.nome AS categoria, SUM(td.valor_parcela) AS total " +
                    "FROM transacoes_detalhe td " +
                    "JOIN produtos p ON td.id_produto = p.id_produto " +
                    "JOIN subcategorias s ON p.id_subcategoria = s.id_subcategoria " +
                    "JOIN categorias c ON s.id_categoria = c.id_categoria " +
                    "GROUP BY c.nome ORDER BY total DESC";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new DashboardDTO(rs.getString("categoria"), rs.getDouble("total")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // 2. NOVO: Buscar Gastos por Membro (Para o Gráfico de Barras)
    public List<DashboardDTO> buscarGastosPorMembro() {
        List<DashboardDTO> lista = new ArrayList<>();
        String sql = "SELECT m.nome AS membro, SUM(td.valor_parcela) AS total " +
                    "FROM transacoes_detalhe td " +
                    "JOIN membros m ON td.id_membro = m.id_membro " +
                    "GROUP BY m.nome ORDER BY total DESC";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new DashboardDTO(rs.getString("membro"), rs.getDouble("total")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // 3. NOVO: Buscar todos os itens (Para a Tabela de Histórico)
    public List<TransacaoDetalheDTO> buscarTodosItens() {
        List<TransacaoDetalheDTO> lista = new ArrayList<>();
        String sql = "SELECT p.nome AS produto, m.nome AS membro, td.quantidade, td.valor_parcela " +
                    "FROM transacoes_detalhe td " +
                    "JOIN produtos p ON td.id_produto = p.id_produto " +
                    "JOIN membros m ON td.id_membro = m.id_membro " +
                    "ORDER BY td.id_detalhe DESC"; // Mostra os últimos primeiro

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                TransacaoDetalheDTO item = new TransacaoDetalheDTO();
                item.setNomeProduto(rs.getString("produto"));
                item.setNomeMembro(rs.getString("membro"));
                item.setQuantidade(rs.getDouble("quantidade"));
                item.setValorParcela(rs.getDouble("valor_parcela"));
                lista.add(item);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }
}