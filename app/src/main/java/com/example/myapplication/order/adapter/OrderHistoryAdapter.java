package com.example.myapplication.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.order.dao.OrderDao;
import com.example.myapplication.order.model.Order;
import com.example.myapplication.order.model.OrderDetailDTO;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    private List<Order> mList;
    private Context context;
    private OrderHistoryItemListener listener;
    private OrderDao dao;

    public OrderHistoryAdapter(Context context, List<Order> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setListener(OrderHistoryItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orderhistory, parent, false);
        return new OrderHistoryAdapter.OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = mList.get(position);
        dao = new OrderDao(context);
        if (order == null) return;
        holder.tvName.setText("orderId#"+order.getOrder_id()+"");
        Date orderDate = order.getOrder_date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        holder.tvDate.setText(sdf.format(orderDate));
        holder.tvOrderDetailCount.setText("Items" +": "+dao.countOrderDetails(order.getOrder_id()));
        holder.tvTotalMoney.setText("Total: "+order.getTotal_amount()+"");
        if (order.getStatus() == 1) {
            holder.tvStatus.setText("Paid");
            holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        } else if(order.getStatus() == 0) {
            holder.tvStatus.setText("Reject");
            holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        }
        else {
            holder.tvStatus.setText("Pending");
            holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Order getItem(int position) {
        return mList.get(position);
    }
    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvDate,tvOrderDetailCount,tvTotalMoney,tvStatus;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvOrderDetailCount = itemView.findViewById(R.id.tvOrderDetailCount);
            tvTotalMoney = itemView.findViewById(R.id.tvTotalMoney);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OrderHistoryItemListener {
        void onItemClick(int position);
    }
}
