package com.example.myapplication.order.adapter;

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
import com.example.myapplication.order.model.OrderDetailDTO;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailAdapter.CartViewHolder> {
    private List<OrderDetailDTO> mList;
    private Context context;
    private OrderHistoryDetailAdapter.OrderDetailItemListener orderDetailItemListener;

    public void setOrderDetailItemListener(OrderHistoryDetailAdapter.OrderDetailItemListener orderDetailItemListener) {
        this.orderDetailItemListener = orderDetailItemListener;
    }

    public OrderHistoryDetailAdapter(Context context, List<OrderDetailDTO> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public OrderHistoryDetailAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orderdetail_history, parent, false);
        return new OrderHistoryDetailAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryDetailAdapter.CartViewHolder holder, int position) {
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

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public OrderDetailDTO getItem(int position) {
        return mList.get(position);
    }
    public class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView tvName, tvPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDrink);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            itemView.setOnClickListener(view -> {
                if (orderDetailItemListener != null) {
                    orderDetailItemListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
    public interface OrderDetailItemListener {
        void onItemClick(int position);
    }


}
