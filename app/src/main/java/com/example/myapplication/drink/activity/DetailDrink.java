package com.example.myapplication.drink.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.drink.dao.DrinkDao;
import com.example.myapplication.drink.model.Drink;
import com.example.myapplication.food.activity.MenuFood;
import com.example.myapplication.food.dao.FoodDao;
import com.example.myapplication.food.model.Food;
import com.example.myapplication.order.model.Order;

import java.io.File;
import java.io.FileInputStream;

public class DetailDrink extends AppCompatActivity {
    private TextView tvName, tvPrice, tvDes;
    private EditText txtQuan;
    private Button btBack, btAddCart;
    private ImageView img;
    private DrinkDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_drink);
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
            Intent intent = new Intent(this, MenuDrink.class);
            startActivity(intent);
        });
        btAddCart = findViewById(R.id.btAddCart);
        img = findViewById(R.id.imgAddDrink);
        Intent intent = getIntent();
        dao = new DrinkDao(this);
        if (intent != null) {
            int drink_id = intent.getIntExtra("drink_id", 0);
            Drink drink = dao.getDrinkById(drink_id);
            try {
                File file = new File(drink.getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvName.setText(drink.getDrink_name());
            tvPrice.setText(drink.getPrice() + "");
            tvDes.setText(drink.getDescription());
            btAddCart.setOnClickListener(view -> {
                if (txtQuan.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Enter quantity of " + drink.getDrink_name(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int quan = Integer.parseInt(txtQuan.getText().toString().trim());
                        SharedPreferences shPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        int user_id = shPref.getInt("id", 0);
                        Order order = dao.getCurrentOrder(user_id + "");
                        if (order != null) {
                            if (dao.updateOrder(order.getOrder_id(), order.getTotal_amount() + drink.getPrice() * quan)
                                    && dao.insertOrderDetail(order.getOrder_id(), drink.getDrink_id(), "Drink", quan, drink.getPrice())) {
                                Toast.makeText(this, "Add to cart successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Add to cart fail", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Order newOrder = dao.insertOrder(user_id);
                            if (newOrder != null) {
                                if (dao.updateOrder(newOrder.getOrder_id(), newOrder.getTotal_amount() + drink.getPrice() * quan)
                                        && dao.insertOrderDetail(newOrder.getOrder_id(), drink.getDrink_id(), "Drink", quan, drink.getPrice())) {
                                    Toast.makeText(this, "Add to cart successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Add to cart fail", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Add to cart fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Enter number for quantity", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}