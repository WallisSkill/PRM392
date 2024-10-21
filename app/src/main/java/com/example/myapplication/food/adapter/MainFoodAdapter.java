package com.example.myapplication.food.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.food.model.Food;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainFoodAdapter extends RecyclerView.Adapter<MainFoodAdapter.FoodViewHolder> {
    private List<Food> mList;
    private Context context;
    private FoodItemListener foodItemListener;

    public void setFoodItemListener(FoodItemListener foodItemListener) {
        this.foodItemListener = foodItemListener;
    }

    public MainFoodAdapter(Context context, List<Food> mlist) {
        this.context = context;
        this.mList = mlist;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
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
        holder.tvPrice.setText(food.getPrice()+"");
        holder.btDelete.setOnClickListener(view -> {
            if(foodItemListener != null){
                foodItemListener.onBtDeleteClick(food.getFood_id());
            }
            mList.remove(position);
            notifyDataSetChanged();
        });
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
        private TextView tvName, tvPrice;
        private Button btDelete;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btDelete = itemView.findViewById(R.id.btDelete);
            itemView.setOnClickListener(view -> {
                if(foodItemListener != null){
                    foodItemListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface FoodItemListener{
        void onItemClick(int position);
        void onBtDeleteClick(int food_id);
    }
}
