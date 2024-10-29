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

    public User loginUser (String username, String password ) {
        String query = "SELECT * FROM User WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        User user = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            boolean role = cursor.getInt(cursor.getColumnIndex("role")) > 0;   // Chuyển đổi sang boolean

            user = new User(id, username, password, email, phone, address, role);
        }
        cursor.close();

        return user;
    }

    public boolean checkCurrentPassword (int id, String password) {
        String query = "SELECT * FROM User WHERE id = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id), password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public boolean updatePassword(int id, String newPassword) {
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("User", values, "id = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }



}
