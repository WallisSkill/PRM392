package com.example.myapplication.drink.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.myapplication.drink.dao.DrinkDao;
import com.example.myapplication.drink.model.Drink;
import com.example.myapplication.food.activity.MainFood;
import com.example.myapplication.food.activity.UpdateFood;
import com.example.myapplication.food.dao.FoodDao;
import com.example.myapplication.food.model.Food;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

public class UpdateDrink extends AppCompatActivity {
    private Button btBack, btImagePicker, btUpdateDrink;
    private ImageView img;
    private EditText txtName, txtPrice, txtDes;
    private Bitmap bitmapImage;
    private DrinkDao dao;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_drink);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btBack = findViewById(R.id.btBack);
        btImagePicker = findViewById(R.id.btSelect);
        btUpdateDrink = findViewById(R.id.btUpdateDrink);
        img = findViewById(R.id.imgAddDrink);
        txtName = findViewById(R.id.txtName);
        txtDes = findViewById(R.id.txtDes);
        txtPrice = findViewById(R.id.txtPrice);
        dao = new DrinkDao(this);
        Intent intent = getIntent();
        setResultLauncher();
        if (intent != null) {
            int drink_id = intent.getIntExtra("drink_id", 0);
            Drink drink = dao.getDrinkById(drink_id);
            try {
                File file = new File(drink.getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            txtName.setText(drink.getDrink_name());
            txtPrice.setText(drink.getPrice() + "");
            txtDes.setText(drink.getDescription());
            btUpdateDrink.setOnClickListener(view -> {
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
                    Toast.makeText(this, "Enter name of drink", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (price <= 0) {
                    Toast.makeText(this, "Enter positive number for price", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (des.isEmpty()) {
                    Toast.makeText(this, "Enter description of drink", Toast.LENGTH_SHORT).show();
                    return;
                }
                String urlImage = drink.getImage();
                if (bitmapImage != null) {
                    File file = new File(drink.getImage()); // Adjust the filename as necessary
                    file.delete();
                    String imageName = "food_" + new Date().toString() + ".jpg";
                    urlImage = saveImageToInternalStorage(bitmapImage, imageName);
                }
                if (dao.updateDrink(drink.getDrink_id() + "", name, des, price, urlImage)) {
                    Toast.makeText(this, "Update food successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Update food fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
        btImagePicker.setOnClickListener(view -> {
            Intent intentSelectImg = new Intent(MediaStore.ACTION_PICK_IMAGES);
            resultLauncher.launch(intentSelectImg);
        });
        btBack.setOnClickListener(view -> {
            Intent intentBack = new Intent(this, MainDrink.class);
            startActivity(intentBack);
        });
    }

    private void setResultLauncher() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri uri = result.getData().getData();
                            bitmapImage = MediaStore.Images.Media.getBitmap(UpdateDrink.this.getContentResolver(), uri);
                            img.setImageBitmap(bitmapImage);
                        } catch (Exception e) {
                            Toast.makeText(UpdateDrink.this, "No image selected", Toast.LENGTH_SHORT).show();
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