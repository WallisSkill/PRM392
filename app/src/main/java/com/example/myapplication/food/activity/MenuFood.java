package com.example.myapplication.food.activity;

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
import com.example.myapplication.food.adapter.MainFoodAdapter;
import com.example.myapplication.food.adapter.MenuFoodAdapter;
import com.example.myapplication.food.dao.FoodDao;
import com.example.myapplication.food.model.Food;

import java.util.List;

public class MenuFood extends AppCompatActivity implements MenuFoodAdapter.FoodItemListener {
    private Button btBack;
    private RecyclerView rView;
    private MenuFoodAdapter adapter;
    private FoodDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btBack = findViewById(R.id.btBack);
        rView = findViewById(R.id.rView);
        dao = new FoodDao(this);
        List<Food> mlist = dao.getAll();
        adapter = new MenuFoodAdapter(this, mlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setFoodItemListener(this);
    }

    @Override
    public void onItemClick(int position) {
        Food food = adapter.getItem(position);
        Intent intent = new Intent(this, DetailFood.class);
        intent.putExtra("food_id", food.getFood_id());
        startActivity(intent);
    }
}