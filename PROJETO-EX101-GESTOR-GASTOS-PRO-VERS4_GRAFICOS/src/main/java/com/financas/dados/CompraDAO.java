package com.financas.dados;

import com.financas.modelo.CompraMestreDTO;
import com.financas.modelo.TransacaoDetalheDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    /**
     * Salva a Compra Mestre e retorna o ID gerado automaticamente.
     * Ajustado para enviar o valor padrão 1 em qtd_parcelas.
     */
    public int salvar(CompraMestreDTO compra) throws SQLException {
        String sql = "INSERT INTO public.compras_mestre (id_estabelecimento, valor_total, data_emissao, qtd_parcelas, forma_pagamento) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, compra.getIdEstabelecimento());
            stmt.setDouble(2, compra.getValorTotal());
            
            if (compra.getDataEmissao() != null) {
                stmt.setDate(3, java.sql.Date.valueOf(compra.getDataEmissao()));
            } else {
                stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }

            // Enviando valor padrão 1 para evitar erro de interface não implementada
            stmt.setInt(4, 1); 
            
            stmt.setString(5, compra.getFormaPagamento());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar mestre no banco: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Salva um item individual vinculado a uma compra.
     * Ajustado para usar 'valor_unitario' conforme a tabela do banco.
     */
    public void salvarItem(TransacaoDetalheDTO item) throws SQLException {
        // SQL atualizado com todas as colunas obrigatórias da sua tabela
        String sql = "INSERT INTO public.transacoes_detalhe " + 
                    "(id_compra, id_produto, id_membro, id_mes_ref, quantidade, valor_unitario, valor_parcela, numero_parcela) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDAO.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, item.getIdCompra());
            stmt.setInt(2, item.getIdProduto());
            stmt.setInt(3, item.getIdMembro());
            
            // Se o seu DTO ainda não tem idMesRef, você pode fixar um ID de teste 
            // ou garantir que o DTO o carregue. Vou usar o do item por enquanto:
            stmt.setInt(4, item.getIdMesRef()); 
            
            stmt.setDouble(5, item.getQuantidade());
            stmt.setDouble(6, item.getValorUnitario());
            
            // Em compras à vista, o valor da parcela é o valor unitário * quantidade
            stmt.setDouble(7, item.getValorUnitario() * item.getQuantidade());
            stmt.setInt(8, 1); // Número da parcela (1 de 1)
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Isso vai imprimir o erro real no console do VS Code
            System.err.println("ERRO NO POSTGRES: " + e.getMessage());
            throw new SQLException("Erro ao salvar item do detalhe: " + e.getMessage());
        }
    }

    public List<CompraMestreDTO> listarTodas() throws SQLException {
        List<CompraMestreDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM public.compras_mestre ORDER BY data_emissao DESC";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CompraMestreDTO dto = new CompraMestreDTO();
                dto.setIdCompra(rs.getInt("id_compra"));
                dto.setIdEstabelecimento(rs.getInt("id_estabelecimento"));
                dto.setValorTotal(rs.getDouble("valor_total"));
                
                java.sql.Date dbDate = rs.getDate("data_emissao");
                if (dbDate != null) {
                    dto.setDataEmissao(dbDate.toLocalDate());
                }
                
                dto.setQtdParcelas(rs.getInt("qtd_parcelas"));
                dto.setFormaPagamento(rs.getString("forma_pagamento"));
                lista.add(dto);
            }
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM public.compras_mestre WHERE id_compra = ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}