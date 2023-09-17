package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CouponAdapter extends ArrayAdapter<Coupon> {
    Context ctx;
    int res;
    ArrayList<Coupon> coupons;
    public CouponAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Coupon> coupons) {
        super(context, resource, coupons);
        ctx = context;
        res= resource;
        this.coupons = coupons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        view = inflater.inflate(res, parent, false);

        Coupon e = coupons.get(position);
        TextView tvId = view.findViewById(R.id.coupon_tvID);
        TextView tvCompanyID = view.findViewById(R.id.coupon_tvCompanyID);
        TextView tvTitle= view.findViewById(R.id.coupon_tvTitle);
        TextView tvCategory = view.findViewById(R.id.coupon_tvCategory);
        TextView tvStartDate = view.findViewById(R.id.coupon_tvStartDate);
        TextView tvEndDate = view.findViewById(R.id.coupon_tvEndDate);
        TextView tvDescription = view.findViewById(R.id.coupon_tvDescription);
        TextView tvAmount = view.findViewById(R.id.coupon_tvAmount);
        TextView tvPrice = view.findViewById(R.id.coupon_tvPrice);
        TextView tvImage = view.findViewById(R.id.coupon_tvImage);



        tvId.setText(e.getId());
        tvCompanyID.setText(e.getCompanyID());
        tvTitle.setText(e.getTitle());
        //Getting category string
        String categoryType = null;
        Category category=e.getCategory();
        if (category == Category.FOOD)
            categoryType = "FOOD";
        if (category == Category.ELECTRICITY)
            categoryType = "ELECTRICITY";
        if (category == Category.RESTAURANT)
            categoryType = "RESTAURANT";
        if (category == Category.VACATION)
            categoryType = "VACATION";
        tvCategory.setText(categoryType);// MAYBE I SHOULD SWITCH CATEGORY TO STRING

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = e.getStartDate();
        String formattedStartDate = sdf.format(startDate);
        Date endDate = e.getEndDate();
        String formattedEndDate = sdf.format(endDate);

        tvStartDate.setText(formattedStartDate);
        tvEndDate.setText(formattedEndDate);

        tvDescription.setText(e.getDescription());
        tvAmount.setText(e.getAmount());
        tvPrice.setText(e.getPrice()+"");
        tvImage.setText(e.getImage());



        return view;
    }

    public void refreshAllCoupons(ArrayList<Coupon> employees)
    {
        clear();
        addAll(employees);
        notifyDataSetChanged();
    }

    public void refreshCouponAdded(Coupon e)
    {
        add(e);
        notifyDataSetChanged();
    }
}
