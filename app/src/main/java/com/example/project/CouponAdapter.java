package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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
        TextView tvId = view.findViewById(R.id.coupon_tvId);
        TextView tvfName = view.findViewById(R.id.coupon_tvCompanyID);
        TextView tvlName = view.findViewById(R.id.coupon_tvTitle);
        TextView tvSalary = view.findViewById(R.id.coupon_tvCategory);
        TextView tvDepartment = view.findViewById(R.id.coupon_tvStartDate);

        tvId.setText(e.getId());
        tvfName.setText(e.getfName());
        tvlName.setText(e.getlName());
        tvSalary.setText(e.getSalary() + "");
        tvDepartment.setText(e.getDepartment());

        return view;
    }

    public void refreshAllEmployees(ArrayList<Employee> employees)
    {
        clear();
        addAll(employees);
        notifyDataSetChanged();
    }

    public void refreshEmployeeAdded(Employee e)
    {
        add(e);
        notifyDataSetChanged();
    }
}
