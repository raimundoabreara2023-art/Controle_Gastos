package com.financas.modelo;

import java.io.Serializable;

/**
 * DTO que representa a tabela estabelecimentos.
 * Utilizado para transportar o nome do local onde a compra foi realizada.
 */
public class EstabelecimentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEstabelecimento;
    private String nome;

    public EstabelecimentoDTO() {
    }

    public EstabelecimentoDTO(int idEstabelecimento, String nome) {
        this.idEstabelecimento = idEstabelecimento;
        this.nome = nome;
    }

    public int getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(int idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Sobrescrita estratégica para o JavaFX.
     * Quando este DTO for usado em um ComboBox, o componente exibirá 
     * automaticamente o nome do estabelecimento.
     */
    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}