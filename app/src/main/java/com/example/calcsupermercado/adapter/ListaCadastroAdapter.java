package com.example.calcsupermercado.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calcsupermercado.R;
import com.example.calcsupermercado.model.NomeProduto;

import java.util.List;

public class ListaCadastroAdapter extends RecyclerView.Adapter<ListaCadastroAdapter.ViewHolder> {

    private List<NomeProduto> produtos;
    private LayoutInflater layoutInflater;

    // Construtor para o Adapter
    public ListaCadastroAdapter(Context context, List<NomeProduto> produtos) {
        this.produtos = produtos;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item para o RecyclerView
        View view = layoutInflater.inflate(R.layout.cadastro_produto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obter o produto da lista com base na posição
        NomeProduto produto = produtos.get(position);

        // Atribuir o nome do produto ao TextView
        holder.nomeProdutoTextView.setText(produto.getNameProduct());
    }

    @Override
    public int getItemCount() {
        // Retorna o tamanho da lista de produtos
        return produtos.size();
    }

    // Classe ViewHolder que referencia as Views do layout do item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomeProdutoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referenciar o TextView do layout item_cadastro_produto.xml
            nomeProdutoTextView = itemView.findViewById(R.id.tv_nome_produto);
        }
    }


}

