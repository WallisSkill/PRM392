package com.example.myapplication.order.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Homepage.AdminHomepageActivity;
import com.example.myapplication.R;
import com.example.myapplication.order.adapter.OrderHistoryAdapter;
import com.example.myapplication.order.dao.OrderDao;
import com.example.myapplication.order.model.Order;

import java.util.List;

public class OrderHistory extends AppCompatActivity implements OrderHistoryAdapter.OrderHistoryItemListener {
    private Button btBack;
    private RecyclerView rView;
    private OrderHistoryAdapter adapter;
    private OrderDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences shPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = shPref.getInt("id", 0);
        boolean role = shPref.getBoolean("role", false);
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, Cart.class);
            startActivity(intent);
        });

        rView = findViewById(R.id.rView);
        dao = new OrderDao(this);
        List<Order> list = dao.getOrdersHistory(user_id);
        adapter = new OrderHistoryAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(manager);
        rView.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public void onItemClick(int position) {
        Order order = adapter.getItem(position);
        Intent intent = new Intent(this, OrderHistoryDetail.class);
        intent.putExtra("order_id", order.getOrder_id());
        startActivity(intent);

    }
}