package com.example.myapplication.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText txtCurrentPass, txtNewPass, txtConfirmPass;
    private Button btnBack, btnReset;

    private UserDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtCurrentPass = findViewById(R.id.txtCurrentPass);
        txtNewPass = findViewById(R.id.txtNewPass);
        txtConfirmPass = findViewById(R.id.txtConfirmPass);
        btnBack = findViewById(R.id.btnBack);
        btnReset = findViewById(R.id.btnReset);

        dao = new UserDao(this);

        btnReset.setOnClickListener(v -> {
            String currentPass = txtCurrentPass.getText().toString();
            String newPass = txtNewPass.getText().toString();
            String confirmPass = txtConfirmPass.getText().toString();


            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            int userId = sharedPreferences.getInt("id", -1);

            if (newPass.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }
hoang
            if (dao.checkCurrentPassword(userId, currentPass)) {
                if (dao.updatePassword(userId, newPass)) {
                    Toast.makeText(this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, ViewProfileActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Thay đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(this, ViewProfileActivity.class);
            startActivity(i);
        });

    }



}