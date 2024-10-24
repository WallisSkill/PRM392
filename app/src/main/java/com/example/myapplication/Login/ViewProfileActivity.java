package com.example.myapplication.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Homepage.AdminHomepageActivity;
import com.example.myapplication.Homepage.CustomerHomepageActivity;
import com.example.myapplication.R;

public class ViewProfileActivity extends AppCompatActivity {
    TextView txtName, txtEmail, txtPhone, txtAddress;
    Button btnBack, btnResetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        btnBack = findViewById(R.id.btnBack);
        btnResetPass = findViewById(R.id.btnResetPass);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1);
        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);
        String phone = sharedPreferences.getString("phone", null);
        String address = sharedPreferences.getString("address", null);
        boolean role = sharedPreferences.getBoolean("role", false);
        txtName.setText(username);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        txtAddress.setText(address);


        btnBack.setOnClickListener(v -> {
            Intent i;
            if (role) {
                i = new Intent(this, AdminHomepageActivity.class);
                startActivity(i);
            } else {
                i = new Intent(this, CustomerHomepageActivity.class);
                startActivity(i);
            }
        });

        btnResetPass.setOnClickListener(v -> {
            Intent i = new Intent(this, ResetPasswordActivity.class);
            startActivity(i);
        });



    }
}