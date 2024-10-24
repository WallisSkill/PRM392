package com.example.myapplication.Login;

import android.content.Intent;
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

import com.example.myapplication.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtName, txtPassword, txtEmail, txtPhone, txtAddress, txtConfirmPassword;
    private Button btnBack, btnRegis;
    private UserDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         dao = new UserDao(this);
        setContentView(R.layout.register_screen);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtPassword = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnBack = findViewById(R.id.btnBack);
        btnRegis = findViewById(R.id.btnRegis);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtName.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String address = txtAddress.getText().toString().trim();
                boolean role = false;
                if (username.isEmpty()) {
                    Toast.makeText(view.getContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dao.checkUsernameExists(username)) {
                    Toast.makeText(view.getContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty() || password.length() < 6) {
                    Toast.makeText(view.getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!confirmPassword.equals(password)) {
                    Toast.makeText(view.getContext(), "Confirm password is wrong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(view.getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.length() != 10) {
                    Toast.makeText(view.getContext(), "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (address.isEmpty()) {
                    Toast.makeText(view.getContext(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dao.insertUser(username, password, email, phone, address, role)) {
                    Toast.makeText(view.getContext(), "Sign up successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(view.getContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}