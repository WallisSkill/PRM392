package com.example.myapplication.Login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.AssSQLiteOpenHelper;

public class UserDao {
    private AssSQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public UserDao(Context context) {
        this.context = context;
        dbHelper = new AssSQLiteOpenHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean insertUser (String username, String password, String email, String phone, String address, boolean role) {
        ContentValues value = new ContentValues();
        value.put("username", username);
        value.put("password", password);
        value.put("email", email);
        value.put("phone", phone);
        value.put("address", address);
        value.put("role", role);
        return db.insert("User", null, value) > 0;
    }

    public boolean checkUsernameExists(String username) {
        Cursor c = db.query("User", new String[]{"username"}, "username = ?",  new String[]{username}, null, null, null);
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

}
