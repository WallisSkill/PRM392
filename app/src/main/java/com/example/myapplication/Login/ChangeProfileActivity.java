package com.example.myapplication.Login;

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
import com.example.myapplication.order.activity.Cart;

public class ChangeProfileActivity extends AppCompatActivity {
    private EditText txtCurrentPassword, txtEmail, txtPhone, txtAddress;
    private Button btnBack, btnChange;
    private UserDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences shPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        dao = new UserDao(this);

        txtAddress = findViewById(R.id.txtAddress);
        txtCurrentPassword = findViewById(R.id.txtCurrentPassword);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress.setText(shPref.getString("address", ""));
        txtEmail.setText(shPref.getString("email", ""));
        txtPhone.setText(shPref.getString("phone", ""));;
        btnBack = findViewById(R.id.btnBack);
        btnChange= findViewById(R.id.btnChange);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, ViewProfileActivity.class);
            startActivity(intent);
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int user_id = shPref.getInt("id", 0);
                boolean curRole = shPref.getBoolean("role", false);

                String password = txtCurrentPassword.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String address = txtAddress.getText().toString().trim();
                if (password.isEmpty() || password.length() < 6) {
                    Toast.makeText(view.getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!dao.checkCurrentPassword(user_id, password)) {
                    Toast.makeText(view.getContext(), "Current password is wrong", Toast.LENGTH_SHORT).show();
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


                if (dao.updateProfile(user_id, email, phone, address)) {
                    SharedPreferences.Editor editor = shPref.edit();
                    editor.putString("email", email);
                    editor.putString("phone", phone);
                    editor.putString("address", address);
                    editor.apply();


                    Toast.makeText(view.getContext(), "Change profile successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ChangeProfileActivity.this, curRole ? AdminHomepageActivity.class: CustomerHomepageActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(view.getContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}