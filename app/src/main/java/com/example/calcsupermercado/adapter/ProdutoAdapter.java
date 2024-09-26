package com.example.calcsupermercado.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calcsupermercado.R;
import com.example.calcsupermercado.model.ListaComprasSupermecado;

import java.util.List;

// Adapter para o RecyclerView
public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {


    private List<ListaComprasSupermecado> listaProdutos;

    // Construtor do Adapter
    public ProdutoAdapter( List<ListaComprasSupermecado> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflando o layout do item (item_produto.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_produtos, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        // Obtém o produto da lista
        ListaComprasSupermecado ListaComprasSupermecado = listaProdutos.get(position);

        // Configura os valores dos TextView com os dados do produto
        holder.textViewNomeProduto.setText(ListaComprasSupermecado.getNameProduct());
        holder.textViewQuantidade.setText(String.valueOf(ListaComprasSupermecado.getQtdUnidadePruduto()));
        holder.textViewValorUnidade.setText(String.format("R$ %.2f", ListaComprasSupermecado.getValorUnidadeProduto()));
        holder.textViewTotal.setText(String.format("R$ %.2f", ListaComprasSupermecado.getValorQdtUnidadeProduto()));
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public void addProduto(ListaComprasSupermecado produto) {
        //listaProdutos.add(produto);
        notifyItemInserted(listaProdutos.size() - 1); // Notifica que um novo item foi inserido




    }

    // Classe ViewHolder para associar os componentes do layout com o Adapter
    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNomeProduto;
        TextView textViewQuantidade;
        TextView textViewValorUnidade;
        TextView textViewValorTotal;
        TextView textViewTotal;



        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Referenciando os TextView do item_produto.xml
            textViewNomeProduto = itemView.findViewById(R.id.textViewNomeProduto);
            textViewQuantidade = itemView.findViewById(R.id.textViewQuantidade);
            textViewValorUnidade = itemView.findViewById(R.id.textViewValorUnidade);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            textViewValorTotal = itemView.findViewById(R.id.textViewTotalValue);
        }
    }
}
