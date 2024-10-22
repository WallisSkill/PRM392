package com.example.myapplication.drink.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.drink.model.Drink;
import com.example.myapplication.food.adapter.MainFoodAdapter;
import com.example.myapplication.food.model.Food;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MainDrinkAdapter extends RecyclerView.Adapter<MainDrinkAdapter.DrinkViewHolder>{
    private List<Drink> mList;
    private Context context;
    private DrinkItemListener drinkItemListener;

    public void setDrinkItemListener(DrinkItemListener drinkItemListener) {
        this.drinkItemListener = drinkItemListener;
    }

    public MainDrinkAdapter(Context context, List<Drink> mlist) {
        this.context = context;
        this.mList = mlist;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drink, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = mList.get(position);
        if(drink == null) return;
        try{
            File file = new File(drink.getImage());
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            holder.img.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.tvName.setText(drink.getDrink_name());
        holder.tvPrice.setText(drink.getPrice()+"");
        holder.btDelete.setOnClickListener(view -> {
            if(drinkItemListener != null){
                drinkItemListener.onBtDeleteClick(drink.getDrink_id());
            }
            mList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Drink getItem(int position){
        return mList.get(position);
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView tvName, tvPrice;
        private Button btDelete;
        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDrink);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btDelete = itemView.findViewById(R.id.btDelete);
            itemView.setOnClickListener(view -> {
                if(drinkItemListener != null){
                    drinkItemListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface DrinkItemListener{
        void onItemClick(int position);
        void onBtDeleteClick(int drink_id);
    }
}
