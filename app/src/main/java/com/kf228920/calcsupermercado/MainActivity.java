package com.kf228920.calcsupermercado;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kf228920.calcsupermercado.fragment.EditProductFragment;
import com.kf228920.calcsupermercado.fragment.HomeFragment;
import com.kf228920.calcsupermercado.model.NomeProduto;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements EditProductFragment.OnNomesListener {

    // Lista que você quer passar para o HomeFragment
    private ArrayList<NomeProduto> minhaLista = new ArrayList<>();
    private HashMap<String, ArrayList<NomeProduto>> categoryMap; // Para armazenar o HashMap
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Definir o BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Carregar o fragmento inicial (HomeFragment) quando a Activity for criada
        //antes era replace
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new EditProductFragment())
                    .commit();
        }

        // Configurar o listener para troca de fragmentos
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    // Cria o HomeFragment
                    selectedFragment = new HomeFragment();

                    // Verifica se já recebeu o categoryMap ou a lista de produtos
                    if (categoryMap != null) {
                        // Passa o categoryMap para o HomeFragment
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("categoryMap", categoryMap); // Passa o HashMap
                        selectedFragment.setArguments(bundle);
                    } else {
                        // Caso não tenha o categoryMap, passa a minhaLista, ou outra estrutura
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("listaProdutos", minhaLista); // Passa a lista
                        selectedFragment.setArguments(bundle);
                    }
                } else if (id == R.id.navigation_edit_product) {
                    // Se for para o fragmento de edição, apenas troca para o EditProductFragment
                    selectedFragment = new EditProductFragment();
                }

                // Troca o fragmento
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();

                return true;
            }
        });

    }


    @Override
    public void onEnviarCategorias(HashMap<String, ArrayList<NomeProduto>> categoryMap) {
        // Salva o categoryMap para que ele possa ser passado para o HomeFragment
        this.categoryMap = categoryMap;
    }
}