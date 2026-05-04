package com.financas.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO que representa a tabela compras_mestre.
 * Transporta os dados principais do cabeçalho da compra.
 */
public class CompraMestreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idCompra;
    private int idEstabelecimento;
    private double valorTotal;
    private LocalDate dataEmissao;
    private int qtdParcelas;
    private String formaPagamento;

    public CompraMestreDTO() {
    }

    public CompraMestreDTO(int idCompra, int idEstabelecimento, double valorTotal, 
                           LocalDate dataEmissao, int qtdParcelas, String formaPagamento) {
        this.idCompra = idCompra;
        this.idEstabelecimento = idEstabelecimento;
        this.valorTotal = valorTotal;
        this.dataEmissao = dataEmissao;
        this.qtdParcelas = qtdParcelas;
        this.formaPagamento = formaPagamento;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(int idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public int getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(int qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    @Override
    public String toString() {
        return "Compra #" + idCompra + " - Total: " + valorTotal;
    }
}