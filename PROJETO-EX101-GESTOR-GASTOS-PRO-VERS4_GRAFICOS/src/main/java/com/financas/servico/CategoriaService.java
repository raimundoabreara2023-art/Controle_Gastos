package com.financas.servico;

import com.financas.dados.CategoriaDAO;
import com.financas.modelo.CategoriaDTO;
import java.util.List;

public class CategoriaService {

    private CategoriaDAO categoriaDAO;

    public CategoriaService() {
        // Inicializa o DAO para comunicação com o banco
        this.categoriaDAO = new CategoriaDAO();
    }

    /**
     * Regra de Negócio: Salvar Categoria
     * Valida se o nome não é nulo ou vazio antes de enviar ao DAO.
     */
    public void salvar(CategoriaDTO categoria) throws Exception {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new Exception("O nome da categoria é obrigatório.");
        }

        // Você pode adicionar outras regras, como converter para MAIÚSCULO
        categoria.setNome(categoria.getNome().toUpperCase().trim());

        try {
            categoriaDAO.salvar(categoria);
        } catch (Exception e) {
            // Aqui você trata erros de banco, como nomes duplicados (Unique Constraint)
            if (e.getMessage().contains("duplicate key")) {
                throw new Exception("Esta categoria já está cadastrada.");
            }
            throw new Exception("Erro ao salvar categoria: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Listar Categorias
     * Retorna todas as categorias cadastradas.
     */
    public List<CategoriaDTO> listarTodos() throws Exception {
        try {
            return categoriaDAO.listarTodas();
        } catch (Exception e) {
            throw new Exception("Erro ao listar categorias: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Exclusão
     * Verifica se a categoria pode ser excluída.
     */
    public void excluir(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido para exclusão.");
        }
        
        try {
            categoriaDAO.excluir(id);
        } catch (Exception e) {
            // O banco de dados vai impedir a exclusão se houver subcategorias vinculadas
            // devido à integridade referencial (Foreign Key).
            throw new Exception("Não é possível excluir: existem subcategorias ou produtos vinculados.");
        }
    }
}