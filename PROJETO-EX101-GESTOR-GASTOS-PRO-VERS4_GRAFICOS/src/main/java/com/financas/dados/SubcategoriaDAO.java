package com.financas.dados;

import com.financas.modelo.SubcategoriaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubcategoriaDAO {

    /**
     * Insere uma nova subcategoria vinculada a uma categoria pai.
     */
    public void salvar(SubcategoriaDTO subcategoria) throws SQLException {
        String sql = "INSERT INTO subcategorias (id_categoria, nome) VALUES (?, ?)";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subcategoria.getIdCategoria());
            stmt.setString(2, subcategoria.getNome());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar subcategoria no banco: " + e.getMessage());
        }
    }

    /**
     * Lista todas as subcategorias de uma categoria específica.
     * Muito útil para atualizar ComboBoxes dependentes na interface.
     */
    public List<SubcategoriaDTO> listarPorCategoria(int idCategoria) throws SQLException {
        List<SubcategoriaDTO> lista = new ArrayList<>();
        String sql = "SELECT id_subcategoria, id_categoria, nome FROM subcategorias " +
                     "WHERE id_categoria = ? ORDER BY nome ASC";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SubcategoriaDTO dto = new SubcategoriaDTO();
                    dto.setIdSubcategoria(rs.getInt("id_subcategoria"));
                    dto.setIdCategoria(rs.getInt("id_categoria"));
                    dto.setNome(rs.getString("nome"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao filtrar subcategorias: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Retorna a lista completa de subcategorias.
     */
    public List<SubcategoriaDTO> listarTodas() throws SQLException {
        List<SubcategoriaDTO> lista = new ArrayList<>();
        String sql = "SELECT id_subcategoria, id_categoria, nome FROM subcategorias ORDER BY nome ASC";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SubcategoriaDTO dto = new SubcategoriaDTO();
                dto.setIdSubcategoria(rs.getInt("id_subcategoria"));
                dto.setIdCategoria(rs.getInt("id_categoria"));
                dto.setNome(rs.getString("nome"));
                lista.add(dto);
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao listar todas as subcategorias: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Remove uma subcategoria pelo ID.
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM subcategorias WHERE id_subcategoria = ?";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir subcategoria: " + e.getMessage());
        }
    }
}