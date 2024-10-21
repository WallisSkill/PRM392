package com.example.myapplication.food.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.food.adapter.MainFoodAdapter;
import com.example.myapplication.food.dao.FoodDao;
import com.example.myapplication.food.model.Food;

import java.util.List;

public class MainFood extends AppCompatActivity implements MainFoodAdapter.FoodItemListener{
    private Button btAdd;
    private RecyclerView rView;
    private MainFoodAdapter adapter;
    private FoodDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btAdd = findViewById(R.id.btAdd);
        rView = findViewById(R.id.rView);
        dao = new FoodDao(this);
        List<Food> mlist = dao.getAll();
        adapter = new MainFoodAdapter(this, mlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setFoodItemListener(this);
        btAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddFood.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(int position) {
        Food food = adapter.getItem(position);
        Toast.makeText(this, food.getFood_name(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBtDeleteClick(int food_id) {
        dao.deleteFood(food_id+"");
    }
}