package com.example.calcsupermercado.model;

public class ListaComprasSupermecado extends NomeProduto {

    private Integer qtdUnidadePruduto;
    private Double valorUnidadeProduto;
    private Double valorQdtUnidadeProduto;
    //valor total de todos os produtos da lista
    private Double valorQtdTotalPrudutos;


    public ListaComprasSupermecado() {
    }

    public ListaComprasSupermecado(String nameProduct, Integer qtdUnidadePruduto, Double valorUnidadeProduto, Double valorQdtUnidadeProduto, Double valorQtdTotalPrudutos) {
        super(nameProduct);
        this.qtdUnidadePruduto = qtdUnidadePruduto;
        this.valorUnidadeProduto = valorUnidadeProduto;
        this.valorQdtUnidadeProduto = valorQdtUnidadeProduto;
        this.valorQtdTotalPrudutos = valorQtdTotalPrudutos;
    }

    public ListaComprasSupermecado(Integer qtdUnidadePruduto, Double valorUnidadeProduto, Double valorQdtUnidadeProduto) {
        this.qtdUnidadePruduto = qtdUnidadePruduto;
        this.valorUnidadeProduto = valorUnidadeProduto;
        this.valorQdtUnidadeProduto = valorQdtUnidadeProduto;
    }

    public ListaComprasSupermecado(String nameProduct, Integer qtdUnidadePruduto, Double valorUnidadeProduto, Double valorQdtUnidadeProduto) {
        super(nameProduct);
        this.qtdUnidadePruduto = qtdUnidadePruduto;
        this.valorUnidadeProduto = valorUnidadeProduto;
        this.valorQdtUnidadeProduto = valorQdtUnidadeProduto;
    }

    public Integer getQtdUnidadePruduto() {
        return qtdUnidadePruduto;
    }

    public void setQtdUnidadePruduto(Integer qtdUnidadePruduto) {
        this.qtdUnidadePruduto = qtdUnidadePruduto;
    }

    public Double getValorUnidadeProduto() {
        return valorUnidadeProduto;
    }

    public void setValorUnidadeProduto(Double valorUnidadeProduto) {
        this.valorUnidadeProduto = valorUnidadeProduto;
    }

    public Double getValorQdtUnidadeProduto() {
        return valorQdtUnidadeProduto;
    }

    public void setValorQdtUnidadeProduto(Double valorQdtUnidadeProduto) {
        this.valorQdtUnidadeProduto = valorQdtUnidadeProduto;
    }

    public Double getValorQtdTotalPrudutos() {
        return valorQtdTotalPrudutos;
    }

    public void setValorQtdTotalPrudutos(Double valorQtdTotalPrudutos) {
        this.valorQtdTotalPrudutos = valorQtdTotalPrudutos;
    }
}