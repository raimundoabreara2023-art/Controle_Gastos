package com.financas.dados;

import com.financas.modelo.MembroDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pela comunicação com a tabela 'membros' no esquema 'public'.
 */
public class MembroDAO {

    /**
     * Insere um novo membro da família.
     * Usa o caminho explícito 'public.membros' para evitar erros de visibilidade.
     */
    public void salvar(MembroDTO membro) throws SQLException {
        String sql = "INSERT INTO public.membros (nome) VALUES (?)";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, membro.getNome());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar membro no banco: " + e.getMessage());
        }
    }

    /**
     * Lista todos os membros para preencher os ComboBoxes.
     * Ordenado por nome para facilitar a seleção na interface.
     */
    public List<MembroDTO> listarTodos() throws SQLException {
        List<MembroDTO> lista = new ArrayList<>();
        // Adicionado 'public.' antes do nome da tabela para garantir a conexão
        String sql = "SELECT id_membro, nome FROM public.membros ORDER BY nome ASC";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MembroDTO dto = new MembroDTO();
                // Verificado:setIdMembro (CamelCase) para alinhar com o DTO
                dto.setIdMembro(rs.getInt("id_membro"));
                dto.setNome(rs.getString("nome"));
                lista.add(dto);
            }

        } catch (SQLException e) {
            // Este erro será capturado pelo MembroService e exibido na MainView
            throw new SQLException("Falha ao listar membros no banco de dados: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Remove um membro pelo seu ID.
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM public.membros WHERE id_membro = ?";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Não foi possível excluir o membro. Verifique se ele possui gastos registrados.");
        }
    }
}