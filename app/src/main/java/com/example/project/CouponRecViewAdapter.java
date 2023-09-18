package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CouponRecViewAdapter extends RecyclerView.Adapter<CouponRecViewAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Coupon> coupons;

    public CouponRecViewAdapter(Context context, ArrayList<Coupon> coupons) {
        this.context = context;
        this.coupons = coupons;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView id, title, Amount, Price;
        ImageView img;
        ConstraintLayout lo;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id=itemView.findViewById(R.id.CouponId);
            this.title=itemView.findViewById(R.id.coupontitle);
            this.Amount=itemView.findViewById(R.id.couponAmount);
            this.Price=itemView.findViewById(R.id.couponprice);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Bind data from the 'products' list to the views in the holder.
        Coupon coupon = coupons.get(position);
        holder.Amount.setText(coupon.getAmount());
        holder.id.setText(coupon.getId());
        holder.title.setText(coupon.getTitle());
        holder.title.setText(coupon.getPrice()+"");

    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }
}
