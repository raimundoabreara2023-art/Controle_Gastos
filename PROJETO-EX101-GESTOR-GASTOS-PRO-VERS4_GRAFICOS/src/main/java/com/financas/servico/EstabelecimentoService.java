package com.financas.servico;

import com.financas.dados.EstabelecimentoDAO;
import com.financas.modelo.EstabelecimentoDTO;
import java.util.List;

public class EstabelecimentoService {

    private EstabelecimentoDAO estabelecimentoDAO;

    public EstabelecimentoService() {
        this.estabelecimentoDAO = new EstabelecimentoDAO();
    }

    /**
     * Regra de Negócio: Salvar Estabelecimento
     * Garante a padronização do nome e evita valores nulos.
     */
    public void salvar(EstabelecimentoDTO estabelecimento) throws Exception {
        if (estabelecimento.getNome() == null || estabelecimento.getNome().trim().isEmpty()) {
            throw new Exception("O nome do estabelecimento não pode estar vazio.");
        }

        // Padronização: Remove espaços extras e coloca em Maiúsculas
        estabelecimento.setNome(estabelecimento.getNome().trim().toUpperCase());

        try {
            estabelecimentoDAO.salvar(estabelecimento);
        } catch (Exception e) {
            // Tratamento para o UNIQUE CONSTRAINT que definimos no PostgreSQL
            if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                throw new Exception("Este estabelecimento já está cadastrado.");
            }
            throw new Exception("Erro ao salvar estabelecimento: " + e.getMessage());
        }
    }

    /**
     * Retorna a lista de todos os estabelecimentos cadastrados.
     */
    public List<EstabelecimentoDTO> listarTodos() throws Exception {
        try {
            return estabelecimentoDAO.listarTodos();
        } catch (Exception e) {
            throw new Exception("Erro ao carregar estabelecimentos: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Exclusão
     * O banco impedirá se houver compras vinculadas (Integridade Referencial).
     */
    public void excluir(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID de estabelecimento inválido.");
        }
        try {
            estabelecimentoDAO.excluir(id);
        } catch (Exception e) {
            throw new Exception("Não é possível remover: existem compras registadas para este local.");
        }
    }
}