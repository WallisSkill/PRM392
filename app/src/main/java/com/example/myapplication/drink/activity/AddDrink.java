package com.example.myapplication.drink.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.food.activity.AddFood;
import com.example.myapplication.food.activity.MainFood;

public class AddDrink extends AppCompatActivity {
    private Button btBack, btImagePicker, btAddDrink;
    private ImageView img;
    private EditText txtName, txtPrice, txtDes;
    private Bitmap bitmapImage;

    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_drink);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btBack = findViewById(R.id.btBack);
        btImagePicker = findViewById(R.id.btSelect);
        btAddDrink = findViewById(R.id.btAddDrink);
        img = findViewById(R.id.imgAddDrink);
        txtName = findViewById(R.id.txtName);
        txtDes = findViewById(R.id.txtDes);
        txtPrice = findViewById(R.id.txtPrice);

        btBack.setOnClickListener(view -> {
//            Intent intent = new Intent(this, MainDrink.class);
//            startActivity(intent);
        });
        setResultLauncher();
        btImagePicker.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            resultLauncher.launch(intent);
        });
    }

    private void setResultLauncher() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri uri = result.getData().getData();
                            bitmapImage = MediaStore.Images.Media.getBitmap(AddDrink.this.getContentResolver(), uri);
                            img.setImageBitmap(bitmapImage);
                        } catch (Exception e) {
                            Toast.makeText(AddDrink.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}