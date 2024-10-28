package com.example.myapplication.dessert.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dessert.model.Dessert;



import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MenuDessertAdapter extends RecyclerView.Adapter<MenuDessertAdapter.DessertViewHolder> {
    private List<Dessert> mList;
    private Context context;
    private DessertItemListener dessertItemListener;

    public void setDessertItemListener(DessertItemListener dessertItemListener) {
        this.dessertItemListener = dessertItemListener;
    }

    public MenuDessertAdapter(Context context, List<Dessert> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public DessertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dessert_cus, parent, false);
        return new MenuDessertAdapter.DessertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DessertViewHolder holder, int position) {
        Dessert dessert = mList.get(position);
        if (dessert == null) return;
        try {
            File file = new File(dessert.getImage());
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            holder.img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvName.setText(dessert.getDessert_name());
        holder.tvPrice.setText(dessert.getPrice() + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public Dessert getItem(int position) {
        return mList.get(position);
    }

    public class DessertViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvName, tvPrice;

        public DessertViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDessert);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            itemView.setOnClickListener(view -> {
                if (dessertItemListener != null) {
                    dessertItemListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
    public interface DessertItemListener {
        void onItemClick(int position);
    }
}
