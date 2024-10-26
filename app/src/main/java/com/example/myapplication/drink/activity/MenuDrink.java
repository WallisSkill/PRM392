package com.example.myapplication.drink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.drink.adapter.MenuDrinkAdapter;
import com.example.myapplication.drink.dao.DrinkDao;
import com.example.myapplication.drink.model.Drink;
import com.example.myapplication.food.activity.DetailFood;
import com.example.myapplication.food.adapter.MenuFoodAdapter;
import com.example.myapplication.food.dao.FoodDao;
import com.example.myapplication.food.model.Food;

import java.util.List;

public class MenuDrink extends AppCompatActivity implements MenuDrinkAdapter.DrinkItemListener {
    private Button btBack;
    private RecyclerView rView;
    private MenuDrinkAdapter adapter;
    private DrinkDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_drink);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btBack = findViewById(R.id.btBack);
        rView = findViewById(R.id.rView);
        dao = new DrinkDao(this);
        List<Drink> mlist = dao.getAll();
        adapter = new MenuDrinkAdapter(this, mlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setDrinkItemListener(this);
    }

    @Override
    public void onItemClick(int position) {
        Drink drink = adapter.getItem(position);
        Intent intent = new Intent(this, DetailDrink.class);
        intent.putExtra("drink_id", drink.getDrink_id());
        startActivity(intent);
    }
}