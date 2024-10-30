package com.example.myapplication.order.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.example.myapplication.Login.User;
import com.example.myapplication.Login.UserDao;
import com.example.myapplication.R;
import com.example.myapplication.dessert.activity.DetailDessert;
import com.example.myapplication.dessert.activity.UpdateActivity;
import com.example.myapplication.drink.activity.DetailDrink;
import com.example.myapplication.drink.activity.UpdateDrink;
import com.example.myapplication.food.activity.DetailFood;
import com.example.myapplication.food.activity.UpdateFood;
import com.example.myapplication.order.adapter.CartAdapter;
import com.example.myapplication.order.adapter.OrderHistoryDetailAdapter;
import com.example.myapplication.order.dao.OrderDao;
import com.example.myapplication.order.model.Order;
import com.example.myapplication.order.model.OrderDetailDTO;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDetail extends AppCompatActivity implements OrderHistoryDetailAdapter.OrderDetailItemListener {
    private Button btBack,btPurchase;
    private TextView totalAmount, tvAddressLabel, tvPhoneNumberLabel;
    private RecyclerView rView;
    private OrderHistoryDetailAdapter adapter;
    private OrderDao dao;
    private UserDao dao2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvAddressLabel = findViewById(R.id.tvAddressLabel);
        tvPhoneNumberLabel = findViewById(R.id.tvPhoneNumberLabel);
        btBack = findViewById(R.id.btBack);

        rView = findViewById(R.id.rView);
        dao = new OrderDao(this);
        dao2 = new UserDao(this);
        SharedPreferences shPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = shPref.getInt("id", 0);

        boolean role = shPref.getBoolean("role", false);
        Intent intents = getIntent();
        int orderId = intents.getIntExtra("order_id",0);
        btBack.setOnClickListener(view -> {
            Intent intent = new Intent(this,role ? MainOrderHistory.class : OrderHistory.class);
            startActivity(intent);
        });
        Order order = dao.getOrderById(String.valueOf(orderId));

        User user = dao2.getUserById(order.getCustomer_id());
        tvAddressLabel.setText("Address: "+ user.getAddress());
        tvPhoneNumberLabel.setText("Phone: "+ user.getPhone());
        List<OrderDetailDTO> list = new ArrayList<>();
        if(order != null) {
            list = dao.getOrderDetailsByOrderId(order.getOrder_id());
        }
        adapter = new OrderHistoryDetailAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setOrderDetailItemListener(this);
        totalAmount = findViewById(R.id.tvTotalAmount);
        totalAmount.setText(order != null ?order.getTotal_amount()+"" : 0+"");

        btPurchase = findViewById(R.id.btPurchase);
        btPurchase.setEnabled(order != null);
        btPurchase.setVisibility(!role ? View.VISIBLE : View.GONE);
        btPurchase.setOnClickListener(view -> {
            if(dao.reOrder(order.getOrder_id())) {
                Toast.makeText(this, "Re-order successfully", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, Cart.class);
            startActivity(intent);
        });
    }
    @Override
    public void onItemClick(int position) {
        SharedPreferences shPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean role= shPref.getBoolean("role", false);
        OrderDetailDTO detailDTO = adapter.getItem(position);
        Intent intent = null;
        switch (detailDTO.getProductType()) {
            case "Drink":{
                intent = new Intent(this, role? UpdateDrink.class : DetailDrink.class);
                intent.putExtra("drink_id", detailDTO.getProductId());
                break;
            }
            case "Food":{
                intent = new Intent(this, role ? UpdateFood.class : DetailFood.class);
                intent.putExtra("food_id", detailDTO.getProductId());
                break;
            }
            case "Dessert":{
                intent = new Intent(this, role ? UpdateActivity.class : DetailDessert.class);
                intent.putExtra("dessert_id", detailDTO.getProductId());
                break;
            }
        }

        if(intent != null) startActivity(intent);
    }

}