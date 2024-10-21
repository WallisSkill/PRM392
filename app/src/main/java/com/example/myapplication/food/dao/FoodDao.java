package com.example.myapplication.food.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.AssSQLiteOpenHelper;
import com.example.myapplication.food.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodDao {
    private AssSQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public FoodDao(Context context) {
        this.context = context;
        dbHelper = new AssSQLiteOpenHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean insertFood(String name, String description, double price, String image){
        ContentValues value = new ContentValues();
        value.put("food_name", name);
        value.put("description", description);
        value.put("price", price);
        value.put("image", image);
        if(db.insert("Food", null, value) <= 0){
            return false;
        }
        return true;
    }

    public List<Food> getAll(){
        List<Food> list = new ArrayList<>();
        Cursor c = db.query("Food", null, null, null, null, null, null);
        c.moveToFirst();
        while(c.isAfterLast() == false){
            Food product = new Food();
            product.setFood_id(c.getInt(0));
            product.setFood_name(c.getString(1));
            product.setDescription(c.getString(2));
            product.setPrice(c.getDouble(3));
            product.setImage(c.getString(4));
            list.add(product);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public Food getFoodById(int food_id){
        Food food = new Food();
        Cursor c = db.query("Food", null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false){
            if(c.getInt(0) == food_id){
                food.setFood_id(c.getInt(0));
                food.setFood_name(c.getString(1));
                food.setDescription(c.getString(2));
                food.setPrice(c.getDouble(3));
                food.setImage(c.getString(4));
                break;
            }
        }
        return food;
    }

    public boolean deleteFood(String food_id){
        if(db.delete("Food", "food_id=?", new String[]{food_id}) <= 0){
            return false;
        }
        return true;
    }

    public boolean updateFood(String food_id, String name, String description, double price, String image){
        ContentValues value = new ContentValues();
        value.put("food_name", name);
        value.put("description", description);
        value.put("price", price);
        value.put("image", image);
        if(db.update("Food", value, "food_id=?", new String[]{food_id}) <= 0){
            return false;
        }
        return true;
    }
}
