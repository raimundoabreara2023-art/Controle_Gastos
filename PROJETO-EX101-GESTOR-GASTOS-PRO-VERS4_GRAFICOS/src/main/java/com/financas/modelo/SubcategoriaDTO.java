package com.financas.modelo;

import java.io.Serializable;

/**
 * DTO que representa a tabela subcategorias.
 */
public class SubcategoriaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idSubcategoria;
    private int idCategoria;
    private String nome;
    
    // --- ADICIONE ESTE ATRIBUTO PARA COMPLETAR O VÍNCULO ---
    private CategoriaDTO categoria;

    public SubcategoriaDTO() {
    }

    public SubcategoriaDTO(int idSubcategoria, int idCategoria, String nome) {
        this.idSubcategoria = idSubcategoria;
        this.idCategoria = idCategoria;
        this.nome = nome;
    }

    // --- ADICIONE OS MÉTODOS GETTER E SETTER PARA A CATEGORIA ---
    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    // --- MANTENHA OS MÉTODOS ORIGINAIS ABAIXO ---
    public int getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(int idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
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

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}