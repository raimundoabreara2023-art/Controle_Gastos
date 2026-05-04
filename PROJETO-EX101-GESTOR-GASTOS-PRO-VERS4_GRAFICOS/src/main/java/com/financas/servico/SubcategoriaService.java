package com.financas.servico;

import com.financas.dados.SubcategoriaDAO;
import com.financas.modelo.SubcategoriaDTO;
import java.util.List;

public class SubcategoriaService {

    private SubcategoriaDAO subcategoriaDAO;

    public SubcategoriaService() {
        // Inicializa o DAO para comunicação com o banco de dados
        this.subcategoriaDAO = new SubcategoriaDAO();
    }

    /**
     * Regra de Negócio: Salvar Subcategoria
     * Valida se o nome está preenchido e se há vínculo com uma categoria pai.
     */
    public void salvar(SubcategoriaDTO subcategoria) throws Exception {
        if (subcategoria.getNome() == null || subcategoria.getNome().trim().isEmpty()) {
            throw new Exception("O nome da subcategoria é obrigatório.");
        }

        if (subcategoria.getIdCategoria() <= 0) {
            throw new Exception("A subcategoria deve estar vinculada a uma categoria principal.");
        }

        // Padronização para evitar duplicidade no banco (converte para MAIÚSCULAS)
        subcategoria.setNome(subcategoria.getNome().trim().toUpperCase());

        try {
            subcategoriaDAO.salvar(subcategoria);
        } catch (Exception e) {
            // Tratamento para restrições de unicidade no banco de dados
            if (e.getMessage().toLowerCase().contains("unique") || e.getMessage().toLowerCase().contains("duplicate")) {
                throw new Exception("Já existe uma subcategoria com este nome para a categoria selecionada.");
            }
            throw new Exception("Erro ao salvar subcategoria: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Listagem por Categoria (Filtro Dinâmico)
     * Essencial para filtrar as subcategorias na interface quando uma categoria é selecionada.
     */
    public List<SubcategoriaDTO> listarPorCategoria(int idCategoria) throws Exception {
        if (idCategoria <= 0) {
            throw new Exception("ID de categoria inválido para filtragem.");
        }
        try {
            return subcategoriaDAO.listarPorCategoria(idCategoria);
        } catch (Exception e) {
            throw new Exception("Erro ao filtrar subcategorias no banco: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Listar todas as subcategorias cadastradas.
     */
    public List<SubcategoriaDTO> listarTodas() throws Exception {
        try {
            return subcategoriaDAO.listarTodas();
        } catch (Exception e) {
            throw new Exception("Erro ao carregar todas as subcategorias: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Exclusão
     * Verifica o ID e trata possíveis impedimentos por integridade referencial.
     */
    public void excluir(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido para exclusão.");
        }
        
        try {
            subcategoriaDAO.excluir(id);
        } catch (Exception e) {
            // Caso existam produtos vinculados e a FK não permita a exclusão
            throw new Exception("Não foi possível excluir a subcategoria. Verifique se existem produtos vinculados.");
        }
    }
}