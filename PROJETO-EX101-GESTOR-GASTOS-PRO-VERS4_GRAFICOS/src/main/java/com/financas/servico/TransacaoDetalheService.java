package com.financas.servico;

import com.financas.dados.TransacaoDetalheDAO;
import com.financas.modelo.TransacaoDetalheDTO;
import java.util.List;

public class TransacaoDetalheService {

    // Agora utilizamos o DAO específico para detalhes
    private final TransacaoDetalheDAO detalheDAO;

    public TransacaoDetalheService() {
        this.detalheDAO = new TransacaoDetalheDAO();
    }

    /**
     * Regra de Negócio: Salvar Item da Transação
     * Valida os dados técnicos e financeiros de cada item individual.
     * Atualizado para usar getValorUnitario().
     */
    public void salvar(TransacaoDetalheDTO detalhe) throws Exception {
        
        // Validação de Valor Unitário (Ajustado para a nova nomenclatura)
        if (detalhe.getValorUnitario() <= 0) {
            throw new Exception("O valor unitário do item deve ser maior que zero.");
        }

        // Validação de Quantidade (Suporta decimais para itens pesáveis)
        if (detalhe.getQuantidade() <= 0) {
            throw new Exception("A quantidade deve ser maior que zero.");
        }

        // Validação de Vínculos (Foreign Keys)
        if (detalhe.getIdProduto() <= 0) {
            throw new Exception("O item deve estar vinculado a um produto válido.");
        }
        
        if (detalhe.getIdMembro() <= 0) {
            throw new Exception("É necessário atribuir este gasto a um membro da família.");
        }

        try {
            // Chama o método salvar no DAO que já está usando valor_unitario
            detalheDAO.salvar(detalhe);
        } catch (Exception e) {
            throw new Exception("Erro ao inserir item no banco de dados: " + e.getMessage());
        }
    }

    /**
     * Lista todos os itens de uma compra específica.
     */
    public List<TransacaoDetalheDTO> listarPorCompra(int idCompra) throws Exception {
        if (idCompra <= 0) {
            throw new Exception("ID de compra inválido para consulta de detalhes.");
        }
        try {
            return detalheDAO.listarPorCompra(idCompra);
        } catch (Exception e) {
            throw new Exception("Erro ao carregar os itens da compra: " + e.getMessage());
        }
    }
}