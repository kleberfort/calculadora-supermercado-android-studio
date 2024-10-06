package com.example.calcsupermercado.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListaComprasSupermecado extends NomeProduto implements Parcelable {

    private Integer qtdUnidadeProduto;
    private Double valorUnidadeProduto;
    private Double valorQdtUnidadeProduto;
    private Double valorQtdTotalProdutos;

    // Construtor
    public ListaComprasSupermecado(String nameProduct, Integer qtdUnidadeProduto, Double valorUnidadeProduto, Double valorQdtUnidadeProduto ) {
        super(nameProduct); // Chama o construtor da superclasse
        this.qtdUnidadeProduto = qtdUnidadeProduto;
        this.valorUnidadeProduto = valorUnidadeProduto;
        this.valorQdtUnidadeProduto = calcularValorQdtUnidadeProduto(); // Supondo que você tenha um método para calcular
        this.valorQtdTotalProdutos = calcularValorQtdTotalProdutos(); // Supondo que você tenha um método para calcular
    }

    public ListaComprasSupermecado(String nameProduct, Integer qtdUnidadeProduto, Double valorUnidadeProduto, Double valorQdtUnidadeProduto, Double valorQtdTotalProdutos) {
        super(nameProduct);
        this.qtdUnidadeProduto = qtdUnidadeProduto;
        this.valorUnidadeProduto = valorUnidadeProduto;
        this.valorQdtUnidadeProduto = valorQdtUnidadeProduto;
        this.valorQtdTotalProdutos = valorQtdTotalProdutos;
    }

    // Métodos de cálculo (caso você queira implementar)
    private Double calcularValorQdtUnidadeProduto() {
        return valorUnidadeProduto * qtdUnidadeProduto;
    }

    private Double calcularValorQtdTotalProdutos() {
        return valorQdtUnidadeProduto; // Ajuste conforme necessário
    }

    // Getters e Setters
    public Integer getQtdUnidadeProduto() {
        return qtdUnidadeProduto;
    }

    public void setQtdUnidadeProduto(Integer qtdUnidadeProduto) {
        this.qtdUnidadeProduto = qtdUnidadeProduto;
    }

    public Double getValorUnidadeProduto() {
        return valorUnidadeProduto;
    }

    public void setValorUnidadeProduto(Double valorUnidadeProduto) {
        this.valorUnidadeProduto = valorUnidadeProduto;
        this.valorQdtUnidadeProduto = calcularValorQdtUnidadeProduto(); // Atualiza ao modificar
    }

    public Double getValorQdtUnidadeProduto() {
        return valorQdtUnidadeProduto;
    }

    public Double getValorQtdTotalProdutos() {
        return valorQtdTotalProdutos;
    }

    // Métodos para implementar Parcelable
    protected ListaComprasSupermecado(Parcel in) {
        super(in.readString()); // Chama o construtor da superclasse
        qtdUnidadeProduto = (Integer) in.readValue(Integer.class.getClassLoader());
        valorUnidadeProduto = in.readDouble();
        valorQdtUnidadeProduto = in.readDouble();
        valorQtdTotalProdutos = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getNameProduct()); // Escreve o nome do produto
        dest.writeValue(qtdUnidadeProduto);
        dest.writeDouble(valorUnidadeProduto);
        dest.writeDouble(valorQdtUnidadeProduto);
        dest.writeDouble(valorQtdTotalProdutos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListaComprasSupermecado> CREATOR = new Creator<ListaComprasSupermecado>() {
        @Override
        public ListaComprasSupermecado createFromParcel(Parcel in) {
            return new ListaComprasSupermecado(in);
        }

        @Override
        public ListaComprasSupermecado[] newArray(int size) {
            return new ListaComprasSupermecado[size];
        }
    };
}

