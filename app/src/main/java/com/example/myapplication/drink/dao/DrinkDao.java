package com.example.myapplication.drink.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.AssSQLiteOpenHelper;
import com.example.myapplication.drink.model.Drink;
import com.example.myapplication.food.model.Food;

import java.util.ArrayList;
import java.util.List;

public class DrinkDao {
    private AssSQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public DrinkDao(Context context) {
        this.context = context;
        dbHelper = new AssSQLiteOpenHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean insertDrink(String name, String description, double price, String image){
        ContentValues value = new ContentValues();
        value.put("drink_name", name);
        value.put("description", description);
        value.put("price", price);
        value.put("image", image);
        if(db.insert("Drink", null, value) <= 0){
            return false;
        }
        return true;
    }

    public List<Drink> getAll(){
        List<Drink> list = new ArrayList<>();
        Cursor c = db.query("Drink", null, null, null, null, null, null);
        c.moveToFirst();
        while(c.isAfterLast() == false){
            Drink product = new Drink();
            product.setDrink_id(c.getInt(0));
            product.setDrink_name(c.getString(1));
            product.setDescription(c.getString(2));
            product.setPrice(c.getDouble(3));
            product.setImage(c.getString(4));
            list.add(product);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public Drink getDrinkById(int drink_id){
        Drink drink = new Drink();
        Cursor c = db.query("Drink", null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false){
            if(c.getInt(0) == drink_id){
                drink.setDrink_id(c.getInt(0));
                drink.setDrink_name(c.getString(1));
                drink.setDescription(c.getString(2));
                drink.setPrice(c.getDouble(3));
                drink.setImage(c.getString(4));
                break;
            }
        }
        return drink;
    }

    public boolean deleteDrink(String drink_id){
        if(db.delete("Drink", "drink_id=?", new String[]{drink_id}) <= 0){
            return false;
        }
        return true;
    }

    public boolean updateDrink(String drink_id, String name, String description, double price, String image){
        ContentValues value = new ContentValues();
        value.put("drink_name", name);
        value.put("description", description);
        value.put("price", price);
        value.put("image", image);
        if(db.update("Drink", value, "drink_id=?", new String[]{drink_id}) <= 0){
            return false;
        }
        return true;
    }
}
