package com.example.calcsupermercado.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calcsupermercado.adapter.ProdutoAdapter;
import com.example.calcsupermercado.databinding.FragmentHomeBinding;
import com.example.calcsupermercado.model.ListaComprasSupermecado;
import com.example.calcsupermercado.model.NomeProduto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Handler;
import android.os.Looper;


public class HomeFragment extends Fragment    {

    private FragmentHomeBinding binding;

    private String nomeProduto;
    private Integer quantidadeProtudo = 0;
    private double valorUnidadeProduto = 0.0;
    private double valorQdtProduto = 0.0;
    private double valorQtdTotalPrudutos = 0.0;

    private List<ListaComprasSupermecado> produtos = new ArrayList<>(); // Inicializando a lista de produtos
    private ProdutoAdapter produtoAdapter; // Adapter para o RecyclerView


    private ArrayList<NomeProduto> productList = new ArrayList<NomeProduto>();

    ArrayList<String> listaSpiner;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflando o layout usando DataBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        listaSpiner = new ArrayList<>();

        if (getArguments() != null) {
            productList = getArguments().getParcelableArrayList("listaProdutos");
            Log.d("BundleCheck", "Product list size: " + (productList != null ? productList.size() : "null"));
        }


////            // Adicionar itens à lista do Spinner
        if (productList != null && !productList.isEmpty()) {
            for (NomeProduto produto : productList) {
                String nomeProduto = produto.getNameProduct();
                if (nomeProduto != null) {
                    listaSpiner.add(nomeProduto);
                    Log.d("Spinner", "Produto adicionado: " + nomeProduto);
                }
            }
        }

// Certifique-se de que o adapter está sendo atualizado corretamente.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, listaSpiner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerNomes.setAdapter(adapter);
        adapter.notifyDataSetChanged();  // Chame depois de vincular o adapter.

        binding.editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d("beforeTextChanged", "Texto após a mudança: " + charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d("onTextChanged", "Texto após a mudança: " + charSequence);

                String oquevem = charSequence.toString();
                // Este método é chamado enquanto o texto está sendo alterado
                if (!charSequence.toString().isEmpty()) {
                    try {
                        // Converta o texto para Integer
                        quantidadeProtudo = Integer.parseInt(charSequence.toString());

                        // Exibir a quantidade capturada
                        Log.d("Quantidade", "Valor capturado: " + quantidadeProtudo);
                    } catch (NumberFormatException e) {
                        // Tratar erros de conversão
                        Log.e("Erro", "Erro ao converter para Integer: " + e.getMessage());
                    }
                } else {
                    // Se o campo estiver vazio, defina como null ou zero
                    quantidadeProtudo = null; // ou 0 se preferir
                    Log.d("Quantidade", "Campo vazio");
                }

                updateTotalValue();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("afterTextChanged", "Texto após a mudança: " + editable);
            }
        });

        // Adicionar EditText para captura do valor unitário do produto
        binding.editTextUnitValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().isEmpty()) {
                    try {
                        // Converta o texto para Integer
                        valorUnidadeProduto = Double.parseDouble(charSequence.toString());

                        // Exibir a quantidade capturada
                        Log.d("ValorUnidadeProduto", "Valor capturado: " + valorUnidadeProduto);
                    } catch (NumberFormatException e) {
                        // Tratar erros de conversão
                        Log.e("Erro", "Erro ao converter para Integer: " + e.getMessage());
                    }
                } else {
                    // Se o campo estiver vazio, defina como null ou zero
                    valorUnidadeProduto = 0.0; // ou 0 se preferir
                    Log.d("ValorUnidadeProduto", "Campo vazio");
                }

                updateTotalValue();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Configurar o botão de adicionar
        binding.buttonAdd.setOnClickListener(v -> adicionarProduto());


        // Configurar o RecyclerView
        RecyclerView recyclerView = binding.rvListaProdutos; // Certifique-se de que o RecyclerView está no seu layout
        produtoAdapter = new ProdutoAdapter(produtos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(produtoAdapter);


        // Retornando a raiz do layout
        return binding.getRoot();
    }//fim do método oncreate






    // Método para atualizar o valor total
    private void updateTotalValue() {
        if (quantidadeProtudo != null && valorUnidadeProduto > 0) {
            valorQdtProduto = quantidadeProtudo * valorUnidadeProduto;
            binding.textViewTotalValue.setText(String.format("R$ %.2f", valorQdtProduto));
        } else {
            binding.textViewTotalValue.setText("R$ 0,00");
        }
    }

    private void adicionarProduto() {

        // Verificar se a quantidade foi preenchida e é maior que 0
        if (quantidadeProtudo == null || quantidadeProtudo <= 0) {
            Toast.makeText(getContext(), "Por favor, informe uma quantidade válida", Toast.LENGTH_SHORT).show();
            binding.editTextQuantity.requestFocus(); // Define o foco no campo de quantidade
            return; // Interrompe a execução se a quantidade não for válida
        }

        // Verificar se o valor unitário foi preenchido e é maior que 0
        if (valorUnidadeProduto <= 0) {
            Toast.makeText(getContext(), "Por favor, informe o valor da unidade do produto ", Toast.LENGTH_SHORT).show();
            binding.editTextUnitValue.requestFocus(); // Define o foco no campo de valor unitário
            return; // Interrompe a execução se o valor unitário não for válido
        }


            // Adicionar um novo produto à lista
            ListaComprasSupermecado novoProduto = new ListaComprasSupermecado(nomeProduto, quantidadeProtudo, valorUnidadeProduto, valorQdtProduto, valorQtdTotalPrudutos);
            produtos.add(novoProduto);

            // Passar o novo produto para o adapter
            produtoAdapter.addProduto(novoProduto);

           //  Atualizar o TextView da quantidade de produtos e valor total
            binding.tvQantidadeProdutos.setText("QTD Produtos: " + produtos.size());
            valorQtdTotalPrudutos += valorQdtProduto;
            binding.textViewTotal.setText(String.format("Total: R$ %.2f", valorQtdTotalPrudutos));


        // Limpar os campos de quantidade, valor da unidade e o total do produto
        binding.editTextQuantity.setText(""); // Limpa o campo de quantidade
        binding.editTextUnitValue.setText(""); // Limpa o campo de valor unitário
        binding.textViewTotalValue.setText("R$ 0,00"); // Reseta o valor total do produto

        quantidadeProtudo = 0;
        valorUnidadeProduto = 0.0;
        valorQdtProduto = 0.0;

    }


}
