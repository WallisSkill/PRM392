package com.example.myapplication.food.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.food.model.Food;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MenuFoodAdapter extends RecyclerView.Adapter<MenuFoodAdapter.FoodViewHolder>{
    private List<Food> mList;
    private Context context;
    private MenuFoodAdapter.FoodItemListener foodItemListener;

    public void setFoodItemListener(MenuFoodAdapter.FoodItemListener foodItemListener) {
        this.foodItemListener = foodItemListener;
    }

    public MenuFoodAdapter(Context context, List<Food> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_cus, parent, false);
        return new MenuFoodAdapter.FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = mList.get(position);
        if(food == null) return;
        try{
            File file = new File(food.getImage());
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            holder.img.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.tvName.setText(food.getFood_name());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Food getItem(int position){
        return mList.get(position);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView tvName;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(view -> {
                if(foodItemListener != null){
                    foodItemListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface FoodItemListener{
        void onItemClick(int position);
    }
}
