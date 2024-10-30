package com.example.myapplication.order.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Homepage.CustomerHomepageActivity;
import com.example.myapplication.R;
import com.example.myapplication.dessert.activity.DetailDessert;
import com.example.myapplication.drink.activity.DetailDrink;
import com.example.myapplication.food.activity.DetailFood;
import com.example.myapplication.order.adapter.CartAdapter;
import com.example.myapplication.order.dao.OrderDao;
import com.example.myapplication.order.model.Order;
import com.example.myapplication.order.model.OrderDetailDTO;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity implements CartAdapter.CartItemListener {
    private Button btBack,btPurchase, btOrderHistory;
    private TextView totalAmount;
    private RecyclerView rView;
    private CartAdapter adapter;
    private OrderDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, CustomerHomepageActivity.class);
            startActivity(intent);
        });
        rView = findViewById(R.id.rView);
        dao = new OrderDao(this);
        SharedPreferences shPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = shPref.getInt("id", 0);
        Order order = dao.getOrder(user_id + "",-1);
        List<OrderDetailDTO> list = new ArrayList<>();
        if(order != null) {
            list = dao.getOrderDetailsByOrderId(order.getOrder_id());
        }
        adapter = new CartAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setCartItemListener(this);
        totalAmount = findViewById(R.id.tvTotalAmount);
        totalAmount.setText(order != null ?order.getTotal_amount()+"" : 0+"");

        btPurchase = findViewById(R.id.btPurchase);
        btPurchase.setEnabled(order != null);
        btPurchase.setOnClickListener(view -> {
            if(dao.purchaseOrder(order.getOrder_id())) {
                Toast.makeText(this, "Purchase successfully", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, CustomerHomepageActivity.class);
            startActivity(intent);
        });
        btOrderHistory = findViewById(R.id.btOrderHistory);
        btOrderHistory.setOnClickListener(view -> {
            Intent intent = new Intent(this, OrderHistory.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(int position) {
        OrderDetailDTO detailDTO = adapter.getItem(position);
        Intent intent = null;
        switch (detailDTO.getProductType()) {
            case "Drink":{
                 intent = new Intent(this, DetailDrink.class);
                intent.putExtra("drink_id", detailDTO.getProductId());
                break;
            }
            case "Food":{
                 intent = new Intent(this, DetailFood.class);
                intent.putExtra("food_id", detailDTO.getProductId());
                break;
            }
            case "Dessert":{
                 intent = new Intent(this, DetailDessert.class);
                intent.putExtra("dessert_id", detailDTO.getProductId());
                break;
            }
        }

        if(intent != null) startActivity(intent);
    }

    @Override
    public void onBtDeleteClick(int orderDt_id) {
        SharedPreferences shPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = shPref.getInt("id", 0);
        Order order = dao.getOrder(user_id + "",-1);
        dao.deleteOrderDetail(orderDt_id,order.getOrder_id());
        order = dao.getOrder(user_id + "", -1);
        totalAmount = findViewById(R.id.tvTotalAmount);
        totalAmount.setText(order != null ?order.getTotal_amount()+"" : 0+"");
    }
}