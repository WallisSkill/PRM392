package com.example.myapplication.drink.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.AssSQLiteOpenHelper;
import com.example.myapplication.drink.model.Drink;
import com.example.myapplication.food.model.Food;
import com.example.myapplication.order.model.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        return db.insert("Drink", null, value) > 0;
    }

    public List<Drink> getAll(){
        List<Drink> list = new ArrayList<>();
        Cursor c = db.query("Drink", null, null, null, null, null, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
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
        while(!c.isAfterLast()){
            if(c.getInt(0) == drink_id){
                drink.setDrink_id(c.getInt(0));
                drink.setDrink_name(c.getString(1));
                drink.setDescription(c.getString(2));
                drink.setPrice(c.getDouble(3));
                drink.setImage(c.getString(4));
                break;
            }
            c.moveToNext();
        }
        return drink;
    }

    public boolean deleteDrink(String drink_id){
        return db.delete("Drink", "drink_id=?", new String[]{drink_id}) > 0;
    }

    public boolean updateDrink(String drink_id, String name, String description, double price, String image){
        ContentValues value = new ContentValues();
        value.put("drink_name", name);
        value.put("description", description);
        value.put("price", price);
        value.put("image", image);
        return db.update("Drink", value, "drink_id=?", new String[]{drink_id}) > 0;
    }

    public Order getCurrentOrder(String user_id){
        Order order = null;
        String query = "SELECT * from Orders where customer_id = ? and status = -1";
        Cursor cursor = db.rawQuery(query, new String[]{user_id});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int customer_id = cursor.getInt(1);
            String order_date = cursor.getString(2);
            Double total_amount = cursor.getDouble(3);
            int status = cursor.getInt(4);

            order = new Order(customer_id, order_date, id, status, total_amount);
        }
        cursor.close();
        return order;
    }

    public boolean updateOrder(int order_id, Double totalAmount){
        ContentValues value = new ContentValues();
        value.put("total_amount", totalAmount);
        return db.update("Orders", value, "order_id=?", new String[]{order_id + ""}) > 0;
    }

    public boolean insertOrderDetail(int order_id, int product_id, String type, int quantity, double price){
        ContentValues value = new ContentValues();
        value.put("order_id", order_id);
        value.put("product_id", product_id);
        value.put("product_type", type);
        value.put("quantity", quantity);
        value.put("price", price);
        return db.insert("Order_Detail", null, value) > 0;
    }

    public Order insertOrder(int customer_id){
        ContentValues value = new ContentValues();
        value.put("customer_id", customer_id);
        value.put("order_date", new Date().toString());
        value.put("total_amount", 0);
        value.put("status", -1);
        if(db.insert("Orders", null, value) > 0){
            return getCurrentOrder(customer_id+"");
        }else{
            return null;
        }
    }

    public List<Drink> searchDrink(String txtSearch, String filterPrice){
        List<Drink> mlist = new ArrayList<Drink>();
        String query = "Select * from Drink where drink_name like ? order by price";
        if(Objects.equals(filterPrice, "Price Up")){
            query += " asc";
        }else{
            query += " desc";
        }
        Cursor c = db.rawQuery(query, new String[]{"%"+txtSearch+"%"});
        c.moveToFirst();
        while(!c.isAfterLast()){
            Drink product = new Drink();
            product.setDrink_id(c.getInt(0));
            product.setDrink_name(c.getString(1));
            product.setDescription(c.getString(2));
            product.setPrice(c.getDouble(3));
            product.setImage(c.getString(4));
            mlist.add(product);
            c.moveToNext();
        }
        c.close();
        return mlist;
    }
}
