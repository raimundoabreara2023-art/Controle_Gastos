package com.financas.dados;

import com.financas.modelo.TransacaoDetalheDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO atualizado para incluir colunas de parcelamento padrão.
 */
public class TransacaoDetalheDAO {

    public void salvar(TransacaoDetalheDTO dto) throws SQLException {
        // SQL atualizado com as colunas valor_parcela e numero_parcela
        String sql = "INSERT INTO public.transacoes_detalhe (id_compra, id_produto, id_membro, quantidade, valor_unitario, valor_parcela, numero_parcela) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dto.getIdCompra());
            stmt.setInt(2, dto.getIdProduto());
            stmt.setInt(3, dto.getIdMembro());
            stmt.setDouble(4, dto.getQuantidade());
            stmt.setDouble(5, dto.getValorUnitario());
            
            // Novos parâmetros enviados para o banco
            stmt.setDouble(6, dto.getValorParcela());
            stmt.setInt(7, dto.getNumeroParcela());

            stmt.executeUpdate();
        }
    }

    public List<TransacaoDetalheDTO> listarPorCompra(int idCompra) throws SQLException {
        List<TransacaoDetalheDTO> lista = new ArrayList<>();
        // SELECT também precisa trazer os novos campos para o DTO ficar completo
        String sql = "SELECT id_transacao, id_compra, id_produto, id_membro, quantidade, valor_unitario, valor_parcela, numero_parcela "
                   + "FROM public.transacoes_detalhe WHERE id_compra = ?";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TransacaoDetalheDTO dto = new TransacaoDetalheDTO();
                    dto.setIdDetalhe(rs.getInt("id_transacao"));
                    dto.setIdCompra(rs.getInt("id_compra"));
                    dto.setIdProduto(rs.getInt("id_produto"));
                    dto.setIdMembro(rs.getInt("id_membro"));
                    dto.setQuantidade(rs.getDouble("quantidade"));
                    dto.setValorUnitario(rs.getDouble("valor_unitario"));
                    
                    // Recuperando os novos campos do banco
                    dto.setValorParcela(rs.getDouble("valor_parcela"));
                    dto.setNumeroParcela(rs.getInt("numero_parcela"));
                    
                    lista.add(dto);
                }
            }
        }
        return lista;
    }

    public void excluirPorCompra(int idCompra) throws SQLException {
        String sql = "DELETE FROM public.transacoes_detalhe WHERE id_compra = ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCompra);
            stmt.executeUpdate();
        }
    }
}