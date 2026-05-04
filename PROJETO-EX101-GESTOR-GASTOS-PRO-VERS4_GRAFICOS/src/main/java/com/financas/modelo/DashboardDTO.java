package com.financas.modelo;

/**
 * DTO para transportar os totais agrupados para os gráficos.
 */
public class DashboardDTO {
    private String nome;
    private Double valor;

    public DashboardDTO(String nome, Double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}