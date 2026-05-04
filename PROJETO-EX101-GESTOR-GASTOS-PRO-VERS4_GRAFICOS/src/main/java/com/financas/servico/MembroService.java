package com.financas.servico;

import com.financas.dados.MembroDAO;
import com.financas.modelo.MembroDTO;
import java.util.List;

public class MembroService {

    private MembroDAO membroDAO;

    public MembroService() {
        this.membroDAO = new MembroDAO();
    }

    /**
     * Regra de Negócio: Salvar Membro
     * Garante que o nome seja válido e padronizado.
     */
    public void salvar(MembroDTO membro) throws Exception {
        if (membro.getNome() == null || membro.getNome().trim().isEmpty()) {
            throw new Exception("O nome do membro da família é obrigatório.");
        }

        // Padronização para evitar duplicidade visual (ex: "João" e "joão")
        membro.setNome(membro.getNome().trim().toUpperCase());

        try {
            membroDAO.salvar(membro);
        } catch (Exception e) {
            // Tratamento para nomes duplicados caso tenha UNIQUE no banco
            if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                throw new Exception("Este membro já está cadastrado no sistema.");
            }
            throw new Exception("Erro ao salvar membro: " + e.getMessage());
        }
    }

    /**
     * Retorna a lista de todos os membros da família.
     */
    public List<MembroDTO> listarTodos() throws Exception {
        try {
            return membroDAO.listarTodos();
        } catch (Exception e) {
            throw new Exception("Erro ao carregar a lista de membros: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Exclusão
     * Verifica se o membro pode ser removido sem quebrar o histórico de gastos.
     */
    public void excluir(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID de membro inválido.");
        }

        try {
            membroDAO.excluir(id);
        } catch (Exception e) {
            // O PostgreSQL impedirá a exclusão se este ID estiver em 'transacoes_detalhe'
            throw new Exception("Não é possível remover este membro, pois existem gastos registados no nome dele.");
        }
    }
}