package com.example.myapplication.order.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.AssSQLiteOpenHelper;
import com.example.myapplication.order.model.Order;
import com.example.myapplication.order.model.OrderDetailDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDao {

    private AssSQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public OrderDao(Context context) {
        this.context = context;
        dbHelper = new AssSQLiteOpenHelper(context);
    }

    public Order getOrder(String user_id, int statusRe){
        Order order = null;
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        String query = "SELECT * from Orders where customer_id = ? and status = ?";
        Cursor cursor = db.rawQuery(query, new String[]{user_id, String.valueOf(statusRe)});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int customer_id = cursor.getInt(1);
            Date order_date = new Date(cursor.getString(2));
            Double total_amount = cursor.getDouble(3);
            int status = cursor.getInt(4);

            order = new Order(customer_id, order_date, id, status, total_amount);
        }
        db.close();
        cursor.close();
        return order;
    }

    public Order getOrderById(String orderId){
        Order order = null;
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        String query = "SELECT * from Orders where order_id = ? and (status = 0 OR status =1)";
        Cursor cursor = db.rawQuery(query, new String[]{orderId});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int customer_id = cursor.getInt(1);
            Date order_date = new Date(cursor.getString(2));
            Double total_amount = cursor.getDouble(3);
            int status = cursor.getInt(4);

            order = new Order(customer_id, order_date, id, status, total_amount);
        }
        db.close();
        cursor.close();
        return order;
    }

    public List<OrderDetailDTO> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        String query = "SELECT " +
                "    od.order_detail_id, " +
                "    od.order_id, " +
                "    od.product_id, " +
                "    od.product_type, " +
                "    od.quantity, " +
                "    od.price, " +
                "    CASE WHEN od.product_type = 'Food' THEN f.food_name " +
                "         WHEN od.product_type = 'Drink' THEN d.drink_name " +
                "         WHEN od.product_type = 'Dessert' THEN ds.dessert_name " +
                "    END AS product_name, " +
                "    CASE WHEN od.product_type = 'Food' THEN f.description " +
                "         WHEN od.product_type = 'Drink' THEN d.description " +
                "         WHEN od.product_type = 'Dessert' THEN ds.description " +
                "    END AS description, " +
                "    CASE WHEN od.product_type = 'Food' THEN f.image " +
                "         WHEN od.product_type = 'Drink' THEN d.image " +
                "         WHEN od.product_type = 'Dessert' THEN ds.image " +
                "    END AS image " +
                "FROM Order_Detail od " +
                "LEFT JOIN Food f ON od.product_id = f.food_id AND od.product_type = 'Food' " +
                "LEFT JOIN Drink d ON od.product_id = d.drink_id AND od.product_type = 'Drink' " +
                "LEFT JOIN Dessert ds ON od.product_id = ds.dessert_id AND od.product_type = 'Dessert' " +
                "WHERE od.order_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                OrderDetailDTO orderDetail = new OrderDetailDTO(
                        cursor.getInt(cursor.getColumnIndexOrThrow("order_detail_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("order_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("product_type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("product_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                );
                orderDetails.add(orderDetail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return orderDetails;
    }

    public boolean purchaseOrder(int order_id){
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        ContentValues value = new ContentValues();
        value.put("status", 1);
        value.put("order_date",new Date().toString());

        try {
            return db.update("Orders", value, "order_id=?", new String[]{String.valueOf(order_id)}) > 0;
        } catch (Exception e) {
            Log.e("OrderDao", "Error updating order status", e);
            return false;
        }
    }

    public void deleteOrderDetail(int orderDt_id, int orderId) {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }

        // Xóa sản phẩm khỏi bảng Order_Detail
        db.delete("Order_Detail", "order_detail_id=?", new String[]{String.valueOf(orderDt_id)});

        // Tính lại tổng chi phí cho đơn hàng
        Cursor cursor = db.rawQuery(
                "SELECT SUM(quantity * price) AS new_total FROM Order_Detail WHERE order_id = ?",
                new String[]{String.valueOf(orderId)}
        );

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") double newTotal = cursor.getDouble(cursor.getColumnIndex("new_total"));

            // Cập nhật total_amount trong bảng Orders
            ContentValues values = new ContentValues();
            values.put("total_amount", newTotal);
            db.update("Orders", values, "order_id = ?", new String[]{String.valueOf(orderId)});
        }
        cursor.close();

        db.close();
    }

    public int countOrderDetails(int orderId) {
        int count = 0;
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        String query = "SELECT COUNT(*) FROM Order_Detail WHERE order_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public List<Order> getOrdersHistory(int userid) {
        List<Order> list = new ArrayList<>();
        List<String> args = new ArrayList<>();
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }

        String query = "SELECT * FROM Orders WHERE 1==1 ";
        query+= "AND (status = 0 OR status = 1)";
        if(userid != -1){
            query+= "AND customer_id = ?";
            args.add(String.valueOf(userid));
        }
        Cursor cursor = db.rawQuery(query, args.toArray(new String[0]));

        while(cursor.moveToNext()) {
            int order_id = cursor.getInt(0);
            int customer_id = cursor.getInt(1);
            Date order_date = new Date(cursor.getString(2));
            Double total_amount = cursor.getDouble(3);
            int status = cursor.getInt(4);
            Order order = new Order(customer_id, order_date, order_id, status, total_amount);
            list.add(order);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Order> getOrders(int i) {
        List<Order> list = new ArrayList<>();
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }

        String query = "SELECT * FROM Orders WHERE status = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(i)});

        while(cursor.moveToNext()) {
            int order_id = cursor.getInt(0);
            int customer_id = cursor.getInt(1);
            Date order_date = new Date(cursor.getString(2));
            Double total_amount = cursor.getDouble(3);
            int status = cursor.getInt(4);
            Order order = new Order(customer_id, order_date, order_id, status, total_amount);
            list.add(order);
        }
        cursor.close();
        db.close();
        return list;
    }


    public boolean reOrder(int oldOrderId) {
        try {
            if (db == null || !db.isOpen()) {
                db = dbHelper.getWritableDatabase();
            }

            Cursor cursor = db.rawQuery("SELECT customer_id, total_amount FROM Orders WHERE order_id = ?", new String[]{String.valueOf(oldOrderId)});

            if (cursor.moveToFirst()) {
                int customerId = cursor.getInt(0);
                double totalAmount = cursor.getDouble(1);

                ContentValues newOrderValues = new ContentValues();
                newOrderValues.put("customer_id", customerId);
                newOrderValues.put("order_date", new Date().toString());
                newOrderValues.put("total_amount", totalAmount);
                newOrderValues.put("status", -1);

                long newOrderId = db.insert("Orders", null, newOrderValues);

                if (newOrderId != -1) {
                    Cursor detailCursor = db.rawQuery("SELECT product_id, product_type, quantity, price FROM Order_Detail WHERE order_id = ?", new String[]{String.valueOf(oldOrderId)});

                    while (detailCursor.moveToNext()) {
                        int productId = detailCursor.getInt(0);
                        String productType = detailCursor.getString(1);
                        int quantity = detailCursor.getInt(2);
                        double price = detailCursor.getDouble(3);

                        ContentValues newDetailValues = new ContentValues();
                        newDetailValues.put("order_id", newOrderId);
                        newDetailValues.put("product_id", productId);
                        newDetailValues.put("product_type", productType);
                        newDetailValues.put("quantity", quantity);
                        newDetailValues.put("price", price);

                        db.insert("Order_Detail", null, newDetailValues);
                    }
                    detailCursor.close();
                }
            }
            cursor.close();
            db.close();
        }catch (Exception e) {
            return false;
        }
        return true;
    }

}
