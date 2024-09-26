package com.example.calcsupermercado.model;

import java.io.Serializable;

public class NomeProduto implements Serializable  {

    private String nameProduct;

    public NomeProduto() {
    }

    public NomeProduto(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }
}
