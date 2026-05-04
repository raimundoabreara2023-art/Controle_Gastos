package com.financas.dados;

import com.financas.modelo.CategoriaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    /**
     * Insere uma nova categoria no PostgreSQL.
     */
    public void salvar(CategoriaDTO categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nome) VALUES (?)";
        
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar no banco de dados: " + e.getMessage());
        }
    }

    /**
     * Retorna a lista de todas as categorias ordenadas por nome.
     */
    public List<CategoriaDTO> listarTodas() throws SQLException {
        List<CategoriaDTO> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nome FROM categorias ORDER BY nome ASC";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CategoriaDTO dto = new CategoriaDTO();
                dto.setIdCategoria(rs.getInt("id_categoria"));
                dto.setNome(rs.getString("nome"));
                lista.add(dto);
            }
            
        } catch (SQLException e) {
            throw new SQLException("Erro ao listar categorias: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Exclui uma categoria pelo ID.
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir do banco de dados: " + e.getMessage());
        }
    }
}