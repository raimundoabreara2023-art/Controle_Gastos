package com.financas.modelo;

import java.io.Serializable;

/**
 * DTO que representa a tabela membros.
 * Transporta os dados das pessoas vinculadas ao núcleo familiar.
 */
public class MembroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idMembro;
    private String nome;

    public MembroDTO() {
    }

    public MembroDTO(int idMembro, String nome) {
        this.idMembro = idMembro;
        this.nome = nome;
    }

    public int getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(int idMembro) {
        this.idMembro = idMembro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Facilita a exibição em ComboBoxes e ListViewers no JavaFX.
     */
    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}