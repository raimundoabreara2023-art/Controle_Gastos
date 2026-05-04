package com.financas.modelo;

import java.io.Serializable;

/**
 * DTO que representa a tabela produtos.
 */
public class ProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idProduto;
    private int idSubcategoria;
    private String nome;
    
    // --- ADICIONE ESTE ATRIBUTO ---
    private SubcategoriaDTO subcategoria; 

    public ProdutoDTO() {
    }

    public ProdutoDTO(int idProduto, int idSubcategoria, String nome) {
        this.idProduto = idProduto;
        this.idSubcategoria = idSubcategoria;
        this.nome = nome;
    }

    // --- ADICIONE ESTES MÉTODOS GETTER E SETTER ---
    public SubcategoriaDTO getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(SubcategoriaDTO subcategoria) {
        this.subcategoria = subcategoria;
    }

    // --- MANTENHA OS OUTROS MÉTODOS ORIGINAIS ---
    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(int idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
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