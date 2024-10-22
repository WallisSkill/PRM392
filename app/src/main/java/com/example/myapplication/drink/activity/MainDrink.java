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
import com.example.myapplication.drink.adapter.MainDrinkAdapter;
import com.example.myapplication.drink.dao.DrinkDao;
import com.example.myapplication.drink.model.Drink;
import com.example.myapplication.food.activity.AddFood;
import com.example.myapplication.food.activity.UpdateFood;
import com.example.myapplication.food.adapter.MainFoodAdapter;
import com.example.myapplication.food.dao.FoodDao;
import com.example.myapplication.food.model.Food;

import java.util.List;

public class MainDrink extends AppCompatActivity implements MainDrinkAdapter.DrinkItemListener {
    private Button btAdd;
    private RecyclerView rView;
    private MainDrinkAdapter adapter;
    private DrinkDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_drink);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btAdd = findViewById(R.id.btAdd);
        rView = findViewById(R.id.rView);
        dao = new DrinkDao(this);
        List<Drink> mlist = dao.getAll();
        adapter = new MainDrinkAdapter(this, mlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setDrinkItemListener(this);
        btAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddDrink.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(int position) {
        Drink drink = adapter.getItem(position);
        Intent intent = new Intent(this, UpdateDrink.class);
        intent.putExtra("drink_id", drink.getDrink_id());
        startActivity(intent);
    }

    @Override
    public void onBtDeleteClick(int drink_id) {
        dao.deleteDrink(drink_id + "");
    }
}