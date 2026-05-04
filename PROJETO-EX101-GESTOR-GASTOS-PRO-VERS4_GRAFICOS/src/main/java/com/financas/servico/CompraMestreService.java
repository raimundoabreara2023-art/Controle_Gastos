package com.financas.servico;

import com.financas.dados.CompraDAO;
import com.financas.modelo.CompraMestreDTO;
import com.financas.modelo.TransacaoDetalheDTO;
import java.util.List;

public class CompraMestreService {

    private CompraDAO compraDAO;
    private TransacaoDetalheService detalheService;

    public CompraMestreService() {
        this.compraDAO = new CompraDAO();
        this.detalheService = new TransacaoDetalheService();
    }

    /**
     * Regra de Negócio: Salvar Compra Completa
     * Vincula os itens à compra mestre e define valores padrão para parcelamento.
     */
    public void salvarCompraCompleta(CompraMestreDTO compra, List<TransacaoDetalheDTO> itens) throws Exception {
        // 1. Validações básicas da compra
        if (compra.getValorTotal() <= 0) {
            throw new Exception("O valor total da compra deve ser maior que zero.");
        }
        if (itens == null || itens.isEmpty()) {
            throw new Exception("Uma compra não pode ser salva sem itens.");
        }
        if (compra.getIdEstabelecimento() <= 0) {
            throw new Exception("É necessário selecionar um estabelecimento.");
        }

        try {
            // 2. Salva a Compra Mestre e recupera o ID gerado pelo PostgreSQL
            int idCompraGerado = compraDAO.salvar(compra);
            
            // 3. Percorre os itens para vincular e definir valores de parcela
            for (TransacaoDetalheDTO item : itens) {
                item.setIdCompra(idCompraGerado);
                
                /* * AJUSTE TEMPORÁRIO (Parcelamento):
                 * Como não implementamos a interface de parcelas, definimos 
                 * que o valor da parcela é o valor total da nota e o número é 1.
                 */
                item.setValorParcela(compra.getValorTotal());
                item.setNumeroParcela(1);
                
                detalheService.salvar(item);
            }
            
        } catch (Exception e) {
            throw new Exception("Erro ao processar a compra: " + e.getMessage());
        }
    }

    /**
     * Lista o histórico de compras
     */
    public List<CompraMestreDTO> listarHistorico() throws Exception {
        try {
            return compraDAO.listarTodas();
        } catch (Exception e) {
            throw new Exception("Erro ao obter histórico de compras: " + e.getMessage());
        }
    }

    /**
     * Regra de Negócio: Exclusão de Compra
     */
    public void excluirCompra(int idCompra) throws Exception {
        if (idCompra <= 0) {
            throw new Exception("ID de compra inválido.");
        }
        try {
            compraDAO.excluir(idCompra);
        } catch (Exception e) {
            throw new Exception("Falha ao eliminar a compra: " + e.getMessage());
        }
    }
}