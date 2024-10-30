package com.example.myapplication.order.adapter;

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
import com.example.myapplication.drink.adapter.MenuDrinkAdapter;
import com.example.myapplication.drink.model.Drink;
import com.example.myapplication.order.model.Order;
import com.example.myapplication.order.model.OrderDetailDTO;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<OrderDetailDTO> mList;
    private Context context;
    private CartAdapter.CartItemListener cartItemListener;

    public void setCartItemListener(CartAdapter.CartItemListener cartItemListener) {
        this.cartItemListener = cartItemListener;
    }

    public CartAdapter(Context context, List<OrderDetailDTO> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
            OrderDetailDTO orderDetailDTO = mList.get(position);
        if (orderDetailDTO == null) return;
        try {
            File file = new File(orderDetailDTO.getImage());
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            holder.img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvName.setText(orderDetailDTO.getProductName());
        holder.tvPrice.setText(orderDetailDTO.getPrice() + " x " + orderDetailDTO.getQuantity());
        holder.btnDelete.setOnClickListener(view -> {
            if(cartItemListener != null){
                cartItemListener.onBtDeleteClick(orderDetailDTO.getOrderDetailId());
            }
            mList.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public OrderDetailDTO getItem(int position) {
        return mList.get(position);
    }
    public class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView img,btnDelete;
        private TextView tvName, tvPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDrink);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDelete = itemView.findViewById(R.id.imgRemove);
            itemView.setOnClickListener(view -> {
                if (cartItemListener != null) {
                    cartItemListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
    public interface CartItemListener {
        void onItemClick(int position);
        void onBtDeleteClick(int orderDt_id);
    }


}
