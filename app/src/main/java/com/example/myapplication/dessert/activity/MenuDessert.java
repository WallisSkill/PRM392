package com.example.myapplication.dessert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Homepage.CustomerHomepageActivity;
import com.example.myapplication.R;
import com.example.myapplication.dessert.adapter.MenuDessertAdapter;
import com.example.myapplication.dessert.dao.DessertDao;
import com.example.myapplication.dessert.model.Dessert;
import com.example.myapplication.drink.adapter.MenuDrinkAdapter;
import com.example.myapplication.drink.dao.DrinkDao;
import com.example.myapplication.drink.model.Drink;

import java.util.List;

public class MenuDessert extends AppCompatActivity implements  MenuDessertAdapter.DessertItemListener {
    private Button btBack, btSearch;
    private RecyclerView rView;
    private MenuDessertAdapter adapter;
    private DessertDao dao;
    private Spinner spPrice;
    private EditText txtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_dessert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btBack = findViewById(R.id.btBack);
        btSearch = findViewById(R.id.btSearch);
        rView = findViewById(R.id.rView);
        dao = new DessertDao(this);
        List<Dessert> mlist = dao.getAll();
        adapter = new MenuDessertAdapter(this, mlist);
        spPrice = findViewById(R.id.spFilter);
        txtSearch = findViewById(R.id.txtSearch);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setDessertItemListener(this);
        btBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, CustomerHomepageActivity.class);
            startActivity(intent);
        });
        btSearch.setOnClickListener(view -> {
            String filterPrice = spPrice.getSelectedItem().toString();
            String searchText = txtSearch.getText().toString();
            List<Dessert> sList = dao.searchDessert(searchText, filterPrice);
            adapter = new MenuDessertAdapter(this, sList);
            rView.setAdapter(adapter);
            adapter.setDessertItemListener(this);
        });
    }

    @Override
    public void onItemClick(int position) {
        Dessert dessert = adapter.getItem(position);
        Intent intent = new Intent(this, DetailDessert.class);
        intent.putExtra("dessert_id", dessert.getDessert_id());
        startActivity(intent);
    }
}