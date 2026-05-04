package com.financas.servico;

import com.financas.dados.ProdutoDAO;
import com.financas.modelo.ProdutoDTO;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoService {

    private ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    /**
     * Regra de Negócio: Salvar Produto
     * Valida se o produto tem nome e se está vinculado a uma subcategoria.
     */
    public void salvar(ProdutoDTO produto) throws Exception {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new Exception("O nome do produto é obrigatório.");
        }
        
        if (produto.getIdSubcategoria() <= 0) {
            throw new Exception("O produto deve estar vinculado a uma subcategoria.");
        }

        // Padronização: Nome em maiúsculas para facilitar a busca dinâmica
        produto.setNome(produto.getNome().trim().toUpperCase());

        try {
            produtoDAO.salvar(produto);
        } catch (Exception e) {
            if (e.getMessage().contains("unique")) {
                throw new Exception("Este produto já está cadastrado nesta subcategoria.");
            }
            throw new Exception("Erro ao salvar produto: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Busca Dinâmica (Auto-complete)
     * Filtra a lista de produtos com base no que o usuário digita na interface.
     */
    public List<ProdutoDTO> buscarPorNome(String termo) throws Exception {
        try {
            List<ProdutoDTO> todos = produtoDAO.listarTodos();
            String termoBusca = termo.toUpperCase();
            
            return todos.stream()
                        .filter(p -> p.getNome().contains(termoBusca))
                        .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao pesquisar produtos: " + e.getMessage());
        }
    }

    /**
     * Lista todos os produtos cadastrados.
     */
    public List<ProdutoDTO> listarTodos() throws Exception {
        try {
            return produtoDAO.listarTodos();
        } catch (Exception e) {
            throw new Exception("Erro ao carregar catálogo de produtos: " + e.getMessage());
        }
    }

    /**
     * Exclui um produto, desde que não esteja em uso em transações.
     */
    public void excluir(int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        
        try {
            produtoDAO.excluir(id);
        } catch (Exception e) {
            throw new Exception("Não é possível excluir o produto: ele já foi utilizado em compras registadas.");
        }
    }
}