package com.financas.dados;

import com.financas.modelo.ProdutoDTO;
import com.financas.modelo.SubcategoriaDTO;
import com.financas.modelo.CategoriaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void salvar(ProdutoDTO produto) throws SQLException {
        String sql = "INSERT INTO produtos (id_subcategoria, nome) VALUES (?, ?)";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produto.getIdSubcategoria());
            stmt.setString(2, produto.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar produto: " + e.getMessage());
        }
    }

    public List<ProdutoDTO> listarTodos() throws SQLException {
        List<ProdutoDTO> lista = new ArrayList<>();
        
        // SQL com JOIN para trazer a árvore completa: Produto -> Subcategoria -> Categoria
        String sql = "SELECT p.id_produto, p.nome AS nome_produto, " +
                     "s.id_subcategoria, s.nome AS nome_subcategoria, " +
                     "c.id_categoria, c.nome AS nome_categoria " +
                     "FROM produtos p " +
                     "INNER JOIN subcategorias s ON p.id_subcategoria = s.id_subcategoria " +
                     "INNER JOIN categorias c ON s.id_categoria = c.id_categoria " +
                     "ORDER BY p.nome ASC";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // 1. Monta a Categoria
                CategoriaDTO cat = new CategoriaDTO();
                cat.setIdCategoria(rs.getInt("id_categoria"));
                cat.setNome(rs.getString("nome_categoria"));

                // 2. Monta a Subcategoria e vincula a Categoria
                SubcategoriaDTO sub = new SubcategoriaDTO();
                sub.setIdSubcategoria(rs.getInt("id_subcategoria"));
                sub.setNome(rs.getString("nome_subcategoria"));
                sub.setCategoria(cat); // Importante para o Controller!

                // 3. Monta o Produto e vincula a Subcategoria
                ProdutoDTO p = new ProdutoDTO();
                p.setIdProduto(rs.getInt("id_produto"));
                p.setNome(rs.getString("nome_produto"));
                p.setIdSubcategoria(rs.getInt("id_subcategoria"));
                p.setSubcategoria(sub); // Importante para o Controller!

                lista.add(p);
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao listar produtos detalhados: " + e.getMessage());
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir produto: " + e.getMessage());
        }
    }
}