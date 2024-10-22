package com.example.myapplication.food.activity;

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
import com.example.myapplication.food.dao.FoodDao;
import com.example.myapplication.food.model.Food;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

public class UpdateFood extends AppCompatActivity {
    private Button btBack, btImagePicker, btUpdateFood;
    private ImageView img;
    private EditText txtName, txtPrice, txtDes;
    private Bitmap bitmapImage;
    private FoodDao dao;
    ActivityResultLauncher<Intent> resultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btBack=findViewById(R.id.btBack);
        btImagePicker=findViewById(R.id.btSelect);
        btUpdateFood=findViewById(R.id.btUpdateFood);
        img = findViewById(R.id.imgAddFood);
        txtName = findViewById(R.id.txtName);
        txtDes = findViewById(R.id.txtDes);
        txtPrice = findViewById(R.id.txtPrice);
        dao = new FoodDao(this);
        Intent intent = getIntent();
        setResultLauncher();
        if(intent != null){
            int food_id = intent.getIntExtra("food_id", 0);
            Food food = dao.getFoodById(food_id);
            try{
                File file = new File(food.getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                img.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
            txtName.setText(food.getFood_name());
            txtPrice.setText(food.getPrice()+"");
            txtDes.setText(food.getDescription());
            btUpdateFood.setOnClickListener(view -> {
                String name = txtName.getText().toString().trim();
                String des = txtDes.getText().toString().trim();
                Double price = -1d;
                try {
                    price = Double.parseDouble(txtPrice.getText().toString().trim());
                }catch (Exception e){
                    Toast.makeText(this, "Enter number for price", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.isEmpty()){
                    Toast.makeText(this, "Enter name of food", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (price == -1){
                    Toast.makeText(this, "Enter price of food", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (des.isEmpty()){
                    Toast.makeText(this, "Enter description of food", Toast.LENGTH_SHORT).show();
                    return;
                }
                String urlImage = food.getImage();
                if(bitmapImage != null){
                    File file = new File(food.getImage()); // Adjust the filename as necessary
                    file.delete();
                    String imageName = "food_" + new Date().toString()+".jpg";
                    urlImage = saveImageToInternalStorage(bitmapImage, imageName);
                }
                if(dao.updateFood(food.getFood_id()+"", name, des, price, urlImage)){
                    Toast.makeText(this, "Update food successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Update food fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
        btImagePicker.setOnClickListener(view -> {
            Intent intentSelectImg = new Intent(MediaStore.ACTION_PICK_IMAGES);
            resultLauncher.launch(intentSelectImg);
        });
        btBack.setOnClickListener(view -> {
            Intent intentBack = new Intent(this, MainFood.class);
            startActivity(intentBack);
        });
    }

    private void setResultLauncher(){
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            Uri uri = result.getData().getData();
                            bitmapImage = MediaStore.Images.Media.getBitmap(UpdateFood.this.getContentResolver(), uri);
                            img.setImageBitmap(bitmapImage);
                        }catch (Exception e){
                            Toast.makeText(UpdateFood.this, "No image selected", Toast.LENGTH_SHORT).show();
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