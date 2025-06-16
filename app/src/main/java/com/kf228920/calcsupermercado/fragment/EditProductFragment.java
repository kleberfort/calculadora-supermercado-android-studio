package com.kf228920.calcsupermercado.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kf228920.calcsupermercado.R;
import com.kf228920.calcsupermercado.adapter.ListaCadastroAdapter;
import com.kf228920.calcsupermercado.databinding.FragmentEditProductBinding;
import com.kf228920.calcsupermercado.model.NomeProduto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EditProductFragment extends Fragment {

    private FragmentEditProductBinding binding;
    private RecyclerView recyclerView;
    private ListaCadastroAdapter adapter;
    private static final String PREFS_NAME = "my_prefs";
    private static final String LIST_KEY = "produtos_key";

    private ArrayList<NomeProduto> productList;
    private OnNomesListener listener;

    private HashMap<String, ArrayList<NomeProduto>> categoryMap;
    private Spinner categorySpinner;

    private List<String> categories;

    public interface OnNomesListener {
        void onEnviarCategorias(HashMap<String, ArrayList<NomeProduto>> categoryMap);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verifica se a Activity implementa a interface
        if (context instanceof OnNomesListener) {
            listener = (OnNomesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " deve implementar OnNomesListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_edit_product, container, false);

        ImageView addCategoryImageView = view.findViewById(R.id.ivAddCategoria);
        EditText categoryInput = view.findViewById(R.id.edCriarCategoria);
        categorySpinner = view.findViewById(R.id.spnCategoria);

        categoryMap = new HashMap<>();

        loadCategoriesFromPreferences();

        productList = new ArrayList<>();
        if (listener != null) {
            listener.onEnviarCategorias(categoryMap);

        }

        //Collections.sort(productList);

        // Ordena a lista após a atualização do produto
        Collections.sort(productList, Comparator.comparing(NomeProduto::getNameProduct));


        // Configurar o RecyclerView e o Adapter
        recyclerView = view.findViewById(R.id.recycler_view_edit_product);

        adapter = new ListaCadastroAdapter(getContext(), productList,
                this::showEditProductDialog, this::showDeleteConfirmationDialog);  // Passa o clique longo para deletar
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar o FloatingActionButton
        FloatingActionButton fab = view.findViewById(R.id.fab_edit_product);
        fab.setOnClickListener(v -> showAddProductDialog());

        EditText searchEditText= view.findViewById(R.id.edit_text_search);
        View imageSeach = view.findViewById(R.id.image_search);


        addCategoryImageView.setOnClickListener(v -> {
            String newCategory = categoryInput.getText().toString().trim();
            if (!newCategory.isEmpty()) {
                if (!categoryMap.containsKey(newCategory)) {
                    categoryMap.put(newCategory, new ArrayList<>());
                    updateSpinner();
                    saveCategoriesToPreferences();
                    Toast.makeText(getContext(), "Categoria adicionada!", Toast.LENGTH_SHORT).show();
                   // categoryInput.setText("");
                    categoryInput.setText(""); // Limpa o campo de Criar categoria
                } else {
                    Toast.makeText(getContext(), "Categoria já existe!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        imageSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Alterna a visibilidade do EditText
                if (searchEditText.getVisibility() == View.GONE) {
                    searchEditText.setVisibility(View.VISIBLE);
                } else if(searchEditText.getVisibility() == View.VISIBLE) {
                    searchEditText.setVisibility(View.GONE);
                }
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                loadProductsForSelectedCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                productList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        categorySpinner.setOnLongClickListener(v -> {
            String selectedCategory = (String) categorySpinner.getSelectedItem();
            showEditCategoryDialog(selectedCategory);
            return true;
        });

        // Configurar o campo de busca e o TextWatcher

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                filterProductList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    private void loadProductsForSelectedCategory(String selectedCategory) {
        productList = categoryMap.get(selectedCategory);
        if (productList != null) {
            Collections.sort(productList, Comparator.comparing(NomeProduto::getNameProduct));
        }
        adapter.updateList(productList != null ? productList : new ArrayList<>());
    }


    private void updateSpinner() {
        // Ordena as categorias (as chaves do categoryMap)
        List<String> sortedCategories = new ArrayList<>(categoryMap.keySet());
        Collections.sort(sortedCategories);  // Ordena as categorias em ordem alfabética

        // Atualiza o ArrayAdapter com a lista ordenada
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sortedCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Atualiza o Spinner
        categorySpinner.setAdapter(adapter);
    }

    private void filterProductList(String query) {
        ArrayList<NomeProduto> filteredList = new ArrayList<>();
        for (NomeProduto produto : productList) {
            if (produto.getNameProduct().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(produto);
            }
        }
        adapter.updateList(filteredList);
    }

    private void showEditCategoryDialog(String category) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.edit_text_category_name, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(dialogView);

        EditText categoryNameInput = dialogView.findViewById(R.id.edit_text_category_name);
        categoryNameInput.setText(category);

        // Referência ao ImageView de lixeira para excluir a categoria
        ImageView trashIcon = dialogView.findViewById(R.id.image_trash);

        // Configurar o botão de confirmação de edição
        dialogBuilder.setTitle("Editar Categoria");
        dialogBuilder.setPositiveButton("Salvar", (dialog, which) -> {
            String newCategoryName = categoryNameInput.getText().toString().trim();

            if (!newCategoryName.isEmpty() && !newCategoryName.equals(category)) {
                // Atualizar o nome da categoria
                ArrayList<NomeProduto> products = categoryMap.remove(category);  // Remove a categoria antiga
                categoryMap.put(newCategoryName, products);  // Adiciona a categoria com o novo nome

                // Chama o método para atualizar e ordenar as categorias no Spinner
                updateSpinner();  // Garante que a lista de categorias seja reordenada

                // Salva as mudanças
                saveCategoriesToPreferences();

                Toast.makeText(getContext(), "Categoria atualizada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "O nome da categoria não pode estar vazio!", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = dialogBuilder.create();

        // Definir o clique no ícone de lixeira para excluir a categoria
        trashIcon.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Excluir Categoria")
                    .setMessage("Tem certeza de que deseja excluir esta categoria e todos os produtos dentro dela?")
                    .setPositiveButton("Excluir", (deleteDialog, which) -> {
                        // Remover a categoria e atualizar o Spinner
                        categoryMap.remove(category);
                        updateSpinner();
                        saveCategoriesToPreferences();

                        Toast.makeText(getContext(), "Categoria e produtos excluídos com sucesso!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss(); // Fechar o diálogo de edição após a exclusão
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        dialog.show();
    }


    // Método para abrir o dialog de editar produto
    private void showEditProductDialog(int position) {
        NomeProduto product = productList.get(position);

        // Inflar o layout do dialog
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(dialogView);

        // Referências ao EditText para editar o produto
        EditText productNameInput = dialogView.findViewById(R.id.edit_text_product_name);
        productNameInput.setText(product.getNameProduct());

        // Configurar os botões do dialog
        dialogBuilder.setTitle("Editar Produto");
        dialogBuilder.setPositiveButton("Salvar", (dialog, which) -> {
            String novoNome = productNameInput.getText().toString().trim();

            if (!novoNome.isEmpty()) {
                novoNome = novoNome.substring(0, 1).toUpperCase() + novoNome.substring(1).toLowerCase();

                // Atualizar o produto
                product.setNameProduct(novoNome);
                adapter.notifyItemChanged(position);

                // Atualizar o Spinner, ordenar as categorias e salvar as mudanças
                updateSpinner(); // Chamada para ordenar e atualizar o Spinner

                // Salvar a lista de produtos atualizada
                saveCategoriesToPreferences();

                Toast.makeText(getContext(), "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "O nome do produto não pode estar vazio!", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        // Mostrar o dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }




    // Método para mostrar o diálogo de confirmação de exclusão
    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Excluir Produto")
                .setMessage("Tem certeza de que deseja excluir este produto?")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    // Remover o produto da lista
                    productList.remove(position);

                    // Notificar o adapter sobre a remoção
                    adapter.notifyItemRemoved(position);

                    // Salvar a lista de produtos atualizada
                    saveCategoriesToPreferences();

                    Toast.makeText(getContext(), "Produto excluído", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Método para abrir o dialog de adicionar produto
    private void showAddProductDialog() {
        // Inflar o layout do dialog
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);

        // Capturar a referência do EditText do nome do produto
        EditText productNameInput = dialogView.findViewById(R.id.edit_text_product_name);
        String selectedCategory = (String) categorySpinner.getSelectedItem();

        // Criar o AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(dialogView);

        // Configurar botões do dialog
        dialogBuilder.setTitle("Adicionar Produto");
        dialogBuilder.setPositiveButton("Adicionar", (dialog, which) -> {
            String nomeProduto = productNameInput.getText().toString().trim();

            if (!nomeProduto.isEmpty()) {
                // Capitalizar o nome do produto
                nomeProduto = nomeProduto.substring(0, 1).toUpperCase() + nomeProduto.substring(1).toLowerCase();
                NomeProduto novoProduto = new NomeProduto(nomeProduto);

                if (selectedCategory != null) {
                    // Adiciona o produto na lista da categoria selecionada
                    ArrayList<NomeProduto> produtosCategoria = categoryMap.get(selectedCategory);
                    if (produtosCategoria != null) {
                        produtosCategoria.add(novoProduto);
                    } else {
                        produtosCategoria = new ArrayList<>();
                        produtosCategoria.add(novoProduto);
                        categoryMap.put(selectedCategory, produtosCategoria);
                    }

                    // Ordena a lista de produtos da categoria após adicionar o novo produto
                    Collections.sort(produtosCategoria, Comparator.comparing(NomeProduto::getNameProduct));

                    // Salva a estrutura atualizada de categorias e produtos
                    saveCategoriesToPreferences();
                    Toast.makeText(getContext(), "Produto adicionado na categoria " + selectedCategory, Toast.LENGTH_SHORT).show();

                    // Atualiza o RecyclerView
                    adapter.updateList(produtosCategoria);
                }
            } else {
                Toast.makeText(getContext(), "O nome do produto não pode estar vazio!", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }


    private void saveCategoriesToPreferences() {
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(categoryMap);
        editor.putString(LIST_KEY, json);
        editor.apply();
    }

    private void loadCategoriesFromPreferences() {
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(LIST_KEY, null);
        if (json != null) {
            Type type = new TypeToken<HashMap<String, ArrayList<NomeProduto>>>() {}.getType();
            categoryMap = gson.fromJson(json, type);
            updateSpinner();
        }
    }

}
