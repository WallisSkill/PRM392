package com.example.myapplication.dessert.activity;

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
import com.example.myapplication.dessert.dao.DessertDao;
import com.example.myapplication.dessert.model.Dessert;
import com.example.myapplication.order.model.Order;

import java.io.File;
import java.io.FileInputStream;


public class DetailDessert extends AppCompatActivity {
    private TextView tvName, tvPrice, tvDes;
    private EditText txtQuan;
    private Button btBack, btAddCart;
    private ImageView img;
    private DessertDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_dessert);
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
            Intent intent = new Intent(this, MenuDessert.class);
            startActivity(intent);
        });
        btAddCart = findViewById(R.id.btAddCart);
        img = findViewById(R.id.imgAddDessert);
        Intent intent = getIntent();
        dao = new DessertDao(this);
        if (intent != null) {
            int dessert_id = intent.getIntExtra("dessert_id", 0);
            Dessert dessert = dao.getDessertById(dessert_id);
            try {
                File file = new File(dessert.getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvName.setText(dessert.getDessert_name());
            tvPrice.setText(dessert.getPrice() + "");
            tvDes.setText(dessert.getDescription());
            btAddCart.setOnClickListener(view -> {
                if (txtQuan.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Enter quantity of " + dessert.getDessert_name(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int quan = Integer.parseInt(txtQuan.getText().toString().trim());
                        SharedPreferences shPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        int user_id = shPref.getInt("id", 0);
                        Order order = dao.getCurrentOrder(user_id + "");
                        if (order != null) {
                            if (dao.updateOrder(order.getOrder_id(), order.getTotal_amount() + dessert.getPrice() * quan)
                                    && dao.insertOrderDetail(order.getOrder_id(), dessert.getDessert_id(), "Dessert", quan, dessert.getPrice())) {
                                Toast.makeText(this, "Add to cart successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Add to cart fail", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Order newOrder = dao.insertOrder(user_id);
                            if (newOrder != null) {
                                if (dao.updateOrder(newOrder.getOrder_id(), newOrder.getTotal_amount() + dessert.getPrice() * quan)
                                        && dao.insertOrderDetail(newOrder.getOrder_id(), dessert.getDessert_id(), "Dessert", quan, dessert.getPrice())) {
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