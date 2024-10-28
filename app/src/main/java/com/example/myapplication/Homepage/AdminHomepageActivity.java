package com.example.myapplication.Homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Login.LoginActivity;
import com.example.myapplication.Login.ViewProfileActivity;
import com.example.myapplication.R;
import com.example.myapplication.dessert.activity.MainDessert;
import com.example.myapplication.drink.activity.MainDrink;
import com.example.myapplication.food.activity.MainFood;

public class AdminHomepageActivity extends AppCompatActivity {
    LinearLayout lnFood, lnDrink, lnDessert;
    Button btnLogout, btnProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lnFood = findViewById(R.id.lnFood);
        lnDrink = findViewById(R.id.lnDrink);
        lnDessert = findViewById(R.id.lnDessert);
        btnLogout = findViewById(R.id.btnLogout);
        btnProfile = findViewById(R.id.btnProfile);

        lnFood.setOnClickListener(view -> {
            Intent i = new Intent(AdminHomepageActivity.this, MainFood.class);
            startActivity(i);

        });

        lnDrink.setOnClickListener(view -> {
            Intent i = new Intent(AdminHomepageActivity.this, MainDrink.class);
            startActivity(i);
        });

        lnDessert.setOnClickListener(view -> {

            Intent i = new Intent(this, MainDessert.class);
            startActivity(i);
        });

        btnLogout.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.remove("id");
            editor.remove("username");
            editor.remove("email");
            editor.remove("phone");
            editor.remove("address");
            editor.remove("role");
            editor.apply();
            Intent i = new Intent(AdminHomepageActivity.this, LoginActivity.class);
            startActivity(i);
        });
        btnProfile.setOnClickListener(view -> {
            Intent i = new Intent(AdminHomepageActivity.this, ViewProfileActivity.class);
            startActivity(i);
        });


    }
}