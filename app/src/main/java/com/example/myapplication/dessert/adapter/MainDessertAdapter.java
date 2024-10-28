package com.example.myapplication.dessert.adapter;

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
import com.example.myapplication.dessert.model.Dessert;


import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MainDessertAdapter extends RecyclerView.Adapter<MainDessertAdapter.DessertViewHolder> {

    private List<Dessert> mList;
    private Context context;
    private DessertItemListener dessertItemListener;

    public void setDessertItemListener(DessertItemListener dessertItemListener) {
        this.dessertItemListener = dessertItemListener;
    }

    public MainDessertAdapter(Context context, List<Dessert> mlist) {
        this.context = context;
        this.mList = mlist;
    }

    public Dessert getItem(int position){
        return mList.get(position);
    }

    @NonNull
    @Override
    public DessertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dessert, parent, false);
        return new DessertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DessertViewHolder holder, int position) {
        Dessert dessert = mList.get(position);
        if(dessert == null) return;
        try{
            File file = new File(dessert.getImage());
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            holder.img.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        String name = dessert.getDessert_name();
        holder.tvName.setText(dessert.getDessert_name());
        holder.tvPrice.setText(dessert.getPrice()+"");
        holder.btDelete.setOnClickListener(view -> {
            if(dessertItemListener != null){
                dessertItemListener.onBtDeleteClick(dessert.getDessert_id());
            }
            mList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class DessertViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView tvName, tvPrice;
        private Button btDelete;
        public DessertViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDessert);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btDelete = itemView.findViewById(R.id.btDelete);
            itemView.setOnClickListener(view -> {
                if(dessertItemListener != null){
                    dessertItemListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }



    public interface DessertItemListener{
        void onItemClick(int position);
        void onBtDeleteClick(int dessert_id);
    }
}
