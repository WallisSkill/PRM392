package com.example.myapplication.food.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.AssSQLiteOpenHelper;
import com.example.myapplication.Login.User;
import com.example.myapplication.food.model.Food;
import com.example.myapplication.order.model.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        return db.insert("Food", null, value) > 0;
    }

    public List<Food> getAll(){
        List<Food> list = new ArrayList<>();
        Cursor c = db.query("Food", null, null, null, null, null, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
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
        Cursor c = db.query("Food", null, "food_id=?", new String[]{food_id+""}, null, null, null);
        if(c.moveToFirst()){
            food.setFood_id(c.getInt(0));
            food.setFood_name(c.getString(1));
            food.setDescription(c.getString(2));
            food.setPrice(c.getDouble(3));
            food.setImage(c.getString(4));
        }
        c.close();
        return food;
    }

    public boolean deleteFood(String food_id){
        return db.delete("Food", "food_id=?", new String[]{food_id}) > 0;
    }

    public boolean updateFood(String food_id, String name, String description, double price, String image){
        ContentValues value = new ContentValues();
        value.put("food_name", name);
        value.put("description", description);
        value.put("price", price);
        value.put("image", image);
        return db.update("Food", value, "food_id=?", new String[]{food_id}) > 0;
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

            order = new Order(id, order_date ,customer_id, status, total_amount);
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

    public List<Food> searchFood(String txtSearch, String filterPrice){
        List<Food> mlist = new ArrayList<Food>();
        String query = "Select * from Food where food_name like ? order by price";
        if(Objects.equals(filterPrice, "Price Up")){
            query += " asc";
        }else{
            query += " desc";
        }
        Cursor c = db.rawQuery(query, new String[]{"%"+txtSearch+"%"});
        c.moveToFirst();
        while(!c.isAfterLast()){
            Food product = new Food();
            product.setFood_id(c.getInt(0));
            product.setFood_name(c.getString(1));
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
