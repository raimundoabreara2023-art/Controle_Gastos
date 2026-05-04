package com.financas.modelo;

import java.io.Serializable;

/**
 * DTO para representar a categoria principal de gastos.
 * Segue o padrão JavaBean para transporte de dados entre camadas.
 */
public class CategoriaDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int idCategoria;
    private String nome;

    // Construtor padrão necessário para frameworks e instanciacão manual
    public CategoriaDTO() {
    }

    // Construtor de conveniência
    public CategoriaDTO(int idCategoria, String nome) {
        this.idCategoria = idCategoria;
        this.nome = nome;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Sobrescrita do toString para facilitar o debug e exibição em ComboBoxes, se necessário
    @Override
    public String toString() {
        return this.nome;
    }
}