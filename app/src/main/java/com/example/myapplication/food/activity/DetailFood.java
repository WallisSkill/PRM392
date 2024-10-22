package com.example.myapplication.food.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.food.dao.FoodDao;
import com.example.myapplication.food.model.Food;

import java.io.File;
import java.io.FileInputStream;

public class DetailFood extends AppCompatActivity {
    private TextView tvName, tvPrice, tvDes;
    private EditText txtQuan;
    private Button btBack, btAddCart;
    private ImageView img;
    private FoodDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvName = findViewById(R.id.txtName);
        tvPrice = findViewById(R.id.txtPrice);
        tvDes = findViewById(R.id.txtDes);
        txtQuan = findViewById(R.id.txtQuan);
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, MenuFood.class);
            startActivity(intent);
        });
        btAddCart = findViewById(R.id.btAddCart);
        img = findViewById(R.id.imgAddFood);
        Intent intent = getIntent();
        dao = new FoodDao(this);
        if(intent != null) {
            int food_id = intent.getIntExtra("food_id", 0);
            Food food = dao.getFoodById(food_id);
            try {
                File file = new File(food.getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvName.setText(food.getFood_name());
            tvPrice.setText(food.getPrice() + "");
            tvDes.setText(food.getDescription());
        }
    }
}