package com.example.myapplication.dessert.activity;

import android.content.Context;
import android.content.ContextWrapper;
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
import com.example.myapplication.dessert.dao.DessertDao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class AddDessert extends AppCompatActivity {
    private Button btBack, btImagePicker, btAddDessert;
    private ImageView img;
    private EditText txtName, txtPrice, txtDes;
    private Bitmap bitmapImage;
    private DessertDao dao;

    ActivityResultLauncher<Intent> resultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_dessert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dao = new DessertDao(this);
        btBack = findViewById(R.id.btBack);
        btImagePicker = findViewById(R.id.btSelect);
        btAddDessert = findViewById(R.id.btAddDessert);
        img = findViewById(R.id.imgAddDessert);
        txtName = findViewById(R.id.txtName);
        txtDes = findViewById(R.id.txtDes);
        txtPrice = findViewById(R.id.txtPrice);
        btBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainDessert.class);
            startActivity(intent);
        });
        setResultLauncher();
        btImagePicker.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            resultLauncher.launch(intent);
        });
        btAddDessert.setOnClickListener(view -> {
            String name = txtName.getText().toString().trim();
            String des = txtDes.getText().toString().trim();
            Double price = -1d;
            try {
                price = Double.parseDouble(txtPrice.getText().toString().trim());
            } catch (Exception e) {
                Toast.makeText(this, "Enter number for price", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter name of dessert", Toast.LENGTH_SHORT).show();
                return;
            }
            if (price == -1) {
                Toast.makeText(this, "Enter price of dessert", Toast.LENGTH_SHORT).show();
                return;
            }
            if (des.isEmpty()) {
                Toast.makeText(this, "Enter description of dessert", Toast.LENGTH_SHORT).show();
                return;
            }
            if (bitmapImage == null) {
                Toast.makeText(this, "Select image of dessert", Toast.LENGTH_SHORT).show();
                return;
            }
            String imageName = "dessert_" + new Date().toString() + ".jpg";
            String urlImage = saveImageToInternalStorage(bitmapImage, imageName);
            if (dao.insertDessert(name, des, price, urlImage)) {
                Toast.makeText(this, "Add dessert successfully", Toast.LENGTH_SHORT).show();
                txtName.setText("");
                txtDes.setText("");
                txtPrice.setText("");
            } else {
                Toast.makeText(this, "Add dessert fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setResultLauncher() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri uri = result.getData().getData();
                            bitmapImage = MediaStore.Images.Media.getBitmap(AddDessert.this.getContentResolver(), uri);
                            img.setImageBitmap(bitmapImage);
                        } catch (Exception e) {
                            Toast.makeText(AddDessert.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String saveImageToInternalStorage(Bitmap bitmap, String food_name) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, food_name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}