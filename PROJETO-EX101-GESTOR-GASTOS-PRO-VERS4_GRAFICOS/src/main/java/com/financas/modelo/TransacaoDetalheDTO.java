package com.financas.modelo;

import java.io.Serializable;

/**
 * DTO que representa a tabela transacoes_detalhe.
 * Transporta os dados de cada item individual dentro de uma compra mestre.
 */
public class TransacaoDetalheDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idDetalhe;
    private int idCompra;
    private int idProduto;
    private int idMembro;
    private int idMesRef; // Campo obrigatório para o banco de dados
    private double quantidade;
    private double valor_unitario;
    
    // Suporte para parcelamento padrão
    private double valor_parcela;
    private int numero_parcela;

    // Campos para exibição na interface JavaFX (Joins de consulta)
    private String nomeProduto;
    private String nomeMembro;
    private String nomeCategoria;    
    private String nomeSubcategoria; 

    public TransacaoDetalheDTO() {
    }

    public TransacaoDetalheDTO(int idDetalhe, int idCompra, int idProduto, 
                                int idMembro, int idMesRef, double quantidade, double valorUnitario) {
        this.idDetalhe = idDetalhe;
        this.idCompra = idCompra;
        this.idProduto = idProduto;
        this.idMembro = idMembro;
        this.idMesRef = idMesRef;
        this.quantidade = quantidade;
        this.valor_unitario = valorUnitario;
    }

    // --- GETTERS E SETTERS ---

    public int getIdDetalhe() { return idDetalhe; }
    public void setIdDetalhe(int idDetalhe) { this.idDetalhe = idDetalhe; }

    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public int getIdMembro() { return idMembro; }
    public void setIdMembro(int idMembro) { this.idMembro = idMembro; }

    public int getIdMesRef() { return idMesRef; }
    public void setIdMesRef(int idMesRef) { this.idMesRef = idMesRef; }

    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }

    public double getValorUnitario() { return valor_unitario; }
    public void setValorUnitario(double valorUnitario) { this.valor_unitario = valorUnitario; }

    public double getValorParcela() { return valor_parcela; }
    public void setValorParcela(double valorParcela) { this.valor_parcela = valorParcela; }

    public int getNumeroParcela() { return numero_parcela; }
    public void setNumeroParcela(int numeroParcela) { this.numero_parcela = numeroParcela; }

    // --- CAMPOS DE EXIBIÇÃO ---

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public String getNomeMembro() { return nomeMembro; }
    public void setNomeMembro(String nomeMembro) { this.nomeMembro = nomeMembro; }

    public String getNomeCategoria() { return nomeCategoria; }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }

    public String getNomeSubcategoria() { return nomeSubcategoria; }
    public void setNomeSubcategoria(String nomeSubcategoria) { this.nomeSubcategoria = nomeSubcategoria; }

    /**
     * Cálculo rápido de subtotal
     */
    public double getSubtotal() {
        return this.quantidade * this.valor_unitario;
    }

    @Override
    public String toString() {
        return (nomeProduto != null ? nomeProduto : "Item #" + idProduto) + " - Qtd: " + quantidade;
    }
}