package com.financas.dados;

import com.financas.modelo.EstabelecimentoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstabelecimentoDAO {

    /**
     * Insere um novo estabelecimento no banco de dados.
     */
    public void salvar(EstabelecimentoDTO estabelecimento) throws SQLException {
        String sql = "INSERT INTO estabelecimentos (nome) VALUES (?)";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estabelecimento.getNome());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar estabelecimento: " + e.getMessage());
        }
    }

    /**
     * Retorna todos os estabelecimentos cadastrados, ordenados alfabeticamente.
     */
    public List<EstabelecimentoDTO> listarTodos() throws SQLException {
        List<EstabelecimentoDTO> lista = new ArrayList<>();
        String sql = "SELECT id_estabelecimento, nome FROM estabelecimentos ORDER BY nome ASC";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EstabelecimentoDTO dto = new EstabelecimentoDTO();
                dto.setIdEstabelecimento(rs.getInt("id_estabelecimento"));
                dto.setNome(rs.getString("nome"));
                lista.add(dto);
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao listar estabelecimentos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Remove um estabelecimento pelo seu ID.
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM estabelecimentos WHERE id_estabelecimento = ?";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Erro ao eliminar estabelecimento: " + e.getMessage());
        }
    }
}