package com.example.myapplication.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Homepage.AdminHomepageActivity;
import com.example.myapplication.Homepage.CustomerHomepageActivity;
import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnRegis;
    EditText etUser, etPass;

    private UserDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegis = findViewById(R.id.btnRegis);
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        dao = new UserDao(this);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUser.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                User user = dao.loginUser(username, password);
                if (user != null) {

                    Toast.makeText(view.getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id", user.getId());
                    editor.putString("username", user.getUsername());
                    editor.putString("email", user.getEmail());
                    editor.putString("phone", user.getPhone());
                    editor.putString("address", user.getAddress());
                    editor.putBoolean("role", user.getRole());
                    editor.apply();
                    Intent i;
                    if (user.getRole()) {
                        i = new Intent(LoginActivity.this, AdminHomepageActivity.class);
                    }
                    else {
                        i = new Intent(LoginActivity.this, CustomerHomepageActivity.class);
                    }
                    startActivity(i);
                } else {
                    Toast.makeText(view.getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}