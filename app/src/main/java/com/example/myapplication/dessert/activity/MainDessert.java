package com.example.myapplication.dessert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Homepage.AdminHomepageActivity;
import com.example.myapplication.R;
import com.example.myapplication.dessert.adapter.MainDessertAdapter;
import com.example.myapplication.dessert.dao.DessertDao;
import com.example.myapplication.dessert.model.Dessert;
import com.example.myapplication.drink.activity.UpdateDrink;
import com.example.myapplication.drink.model.Drink;


import java.util.List;

public class MainDessert extends AppCompatActivity implements MainDessertAdapter.DessertItemListener {
    private Button btAdd;
    private ImageView btBack;
    private RecyclerView rView;
    private MainDessertAdapter adapter;
    private DessertDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_dessert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btAdd = findViewById(R.id.btAdd);
        btBack = findViewById(R.id.btHome);
        btBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminHomepageActivity.class);
            startActivity(intent);
        });
        rView = findViewById(R.id.rView);
        dao = new DessertDao(this);
        List<Dessert> mlist = dao.getAll();
        adapter = new MainDessertAdapter(this, mlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setDessertItemListener(this);
        btAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddDessert.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(int position) {
        Dessert dessert = adapter.getItem(position);
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra("dessert_id", dessert.getDessert_id());
        startActivity(intent);
    }

    @Override
    public void onBtDeleteClick(int dessert_id) {
        dao.deleteDessert(dessert_id + "");
    }
}