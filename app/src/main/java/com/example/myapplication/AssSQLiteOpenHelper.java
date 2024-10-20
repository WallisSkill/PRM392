package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AssSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String dbName = "AssPRM";
    public static final String createUser = "CREATE TABLE User(\n" +
            "    id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    username TEXT NOT NULL,\n" +
            "    password TEXT NOT NULL,\n" +
            "    email TEXT,\n" +
            "    phone TEXT,\n" +
            "    address TEXT,\n" +
            "    role  BIT\n" +
            ")\n";
    public static final String createFood = "CREATE TABLE Food (\n" +
            "    food_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    food_name TEXT NOT NULL,\n" +
            "    description TEXT,\n" +
            "    price REAL NOT NULL,\n" +
            "    image INTEGER \n" +
            ")\n";
    public static final String createDrink = "CREATE TABLE Drink (\n" +
            "    drink_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    drink_name TEXT NOT NULL,\n" +
            "    description TEXT,\n" +
            "    price REAL NOT NULL,\n" +
            "    image INTEGER \n" +
            ")\n";
    public static final String createDessert = "CREATE TABLE Dessert (\n" +
            "    dessert_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    dessert_name TEXT NOT NULL,\n" +
            "    description TEXT,\n" +
            "    price REAL NOT NULL,\n" +
            "    image INTEGER \n" +
            ")\n";
    public static final String createOrder = "CREATE TABLE Orders (\n" +
            "    order_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    customer_id INTEGER, \n" +
            "    order_date TEXT NOT NULL,\n" +
            "    total_amount REAL NOT NULL,\n" +
            "    status INTEGER NOT NULL \n" +
            ")\n";
    public static final String createOrderDetail = "CREATE TABLE Order_Detail (\n" +
            "\n" +
            " order_detail_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            " order_id INTEGER, \n" +
            " product_id INTEGER,\n" +
            " product_type TEXT NOT NULL,\n" +
            "quantity INTEGER NOT NULL,\n" +
            "price REAL NOT NULL\n" +
            ")\n";

    public AssSQLiteOpenHelper(@Nullable Context context) {
        super(context, dbName, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createUser);
        sqLiteDatabase.execSQL(createFood);
        sqLiteDatabase.execSQL(createDrink);
        sqLiteDatabase.execSQL(createDessert);
        sqLiteDatabase.execSQL(createOrder);
        sqLiteDatabase.execSQL(createOrderDetail);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Food");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Drink");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Dessert");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Orders");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Order_Detail");
        //Create
        onCreate(sqLiteDatabase);
    }
}
