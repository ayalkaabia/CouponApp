package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import com.example.project.Category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class viewCouponsActivity extends AppCompatActivity {

    Button  maxPrice,minPrice,addCoupon,returnButton;
    EditText couponIdEt ;
    EditText companyIdEt;
    ListView couponListView;
    RadioGroup chooseCategory;
    ArrayAdapter<Coupon> adapter;
    ArrayList<Coupon> coupons = new ArrayList<>();
    CustomersFacade customersFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_coupons);

        couponIdEt = findViewById(R.id.etCouponId);
        companyIdEt=findViewById(R.id.etcompanyID);
        minPrice =findViewById(R.id.minPrice_btn);
        maxPrice=findViewById(R.id.maxPrice_btn);
        addCoupon =findViewById(R.id.purchaseNew_btn);
        returnButton=findViewById(R.id.returnButton);
        couponListView = findViewById(R.id.couponListView);

        chooseCategory = findViewById(R.id.chooseCate_radio);
        customersFacade=(CustomersFacade) getIntent().getSerializableExtra("customersFacade");

        ArrayList<Coupon> coupons = new ArrayList<>();
        // Create an adapter for your coupon list
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, coupons);
        // Set the initial adapter for the ListView
        couponListView.setAdapter(adapter);

        chooseCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle category selection here using if statements
                if (checkedId == R.id.choose_food_btn) {
                    filterCouponsByCategory(Category.FOOD);
                } else if (checkedId == R.id.choose_elec_btn) {
                    filterCouponsByCategory(Category.ELECTRICITY);
                } else if (checkedId == R.id.choose_rest_btn) {
                    filterCouponsByCategory(Category.RESTAURANT);
                } else if (checkedId == R.id.choose_vac_btn) {
                    filterCouponsByCategory(Category.VACATION);
                } else {
                }
            }
        });

        minPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double minimumPrice = findMinPrice(coupons); // Get the minimum price

                if (minimumPrice != Double.MAX_VALUE) {
                    ArrayList<Coupon> minPriceCoupons = filterdCouponsByMinPrice(coupons, minimumPrice);

                    // Create an adapter to display the filtered coupons in the ListView
                    ArrayAdapter<Coupon> adapter = new ArrayAdapter<>(viewCouponsActivity.this, android.R.layout.simple_list_item_1, minPriceCoupons);

                    // Set the adapter on the ListView
                    couponListView.setAdapter(adapter);
                } else {
                    Toast.makeText(viewCouponsActivity.this, "No coupons found with minimum price.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        maxPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double maximumPrice = findMaxPrice(coupons); // Get the maximum price

                if (maximumPrice != Double.MIN_VALUE) {
                    ArrayList<Coupon> maxPriceCoupons = filterdCouponsByMaxPrice(coupons, maximumPrice);

                    // Create an adapter to display the filtered coupons in the ListView
                    ArrayAdapter<Coupon> adapter = new ArrayAdapter<>(viewCouponsActivity.this, android.R.layout.simple_list_item_1, maxPriceCoupons);

                    // Set the adapter on the ListView
                    couponListView.setAdapter(adapter);
                } else {
                    Toast.makeText(viewCouponsActivity.this, "No coupons found with maximum price.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(viewCouponsActivity.this, NewCouponActivity.class);
                startActivity(intent);            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void filterCouponsByCategory(Category selectedCategory) {
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();

        for (Coupon c : coupons) {
            if (c.getCategory() == selectedCategory) {
                filteredCoupons.add(c);
            }
        }

        // Update the adapter with the filtered coupons and refresh the ListView
        adapter.clear();
        adapter.addAll(filteredCoupons);
        adapter.notifyDataSetChanged();

        if (filteredCoupons.isEmpty()) {
            Toast.makeText(viewCouponsActivity.this, "No coupons found for the selected category.", Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<Coupon> filterdCouponsBycouponId(ArrayList<Coupon> coupons) {
        ArrayList<Coupon> filteredList = new ArrayList<>();

        for (Coupon c : coupons) {
            // Check if couponIdEt is not empty and matches the coupon's ID
            if (!couponIdEt.getText().toString().isEmpty() &&
                    c.getId() == Integer.parseInt(couponIdEt.getText().toString()))
            {
                filteredList.add(c);
            }

        }
        return filteredList;
    }
    public ArrayList<Coupon> filterdCouponsBycompanyId(ArrayList<Coupon> coupons) {
        ArrayList<Coupon> filteredList = new ArrayList<>();

        for (Coupon c : coupons) {
            // Check if couponIdEt is not empty and matches the coupon's ID
            if (!companyIdEt.getText().toString().isEmpty() &&
                    c.getId() == Integer.parseInt(companyIdEt.getText().toString()))
            {
                filteredList.add(c);
            }

        }
        return filteredList;
    }

    public double findMaxPrice(ArrayList<Coupon> coupons) {
        double maxPrice = Double.MIN_VALUE; // Initialize with the smallest possible value

        for (Coupon c : coupons) {
            if (c.getPrice() > maxPrice) {
                maxPrice = c.getPrice();
            }
        }

        return maxPrice;
    }

    public double findMinPrice(ArrayList<Coupon> coupons) {
        double minPrice = Double.MAX_VALUE; // Initialize with the largest possible value

        for (Coupon c : coupons) {
            if (c.getPrice() < minPrice) {
                minPrice = c.getPrice();
            }
        }

        return minPrice;
    }


    public ArrayList<Coupon> filterdCouponsByMinPrice(ArrayList<Coupon> coupons, double minPrice) {
        ArrayList<Coupon> filteredList = new ArrayList<>();

        for (Coupon c : coupons) {
            if (c.getPrice() == minPrice) {
                filteredList.add(c);
            }
        }

        return filteredList;
    }
    public ArrayList<Coupon> filterdCouponsByMaxPrice(ArrayList<Coupon> coupons, double maxPrice) {
        ArrayList<Coupon> filteredList = new ArrayList<>();

        for (Coupon c : coupons) {
            if (c.getPrice() == maxPrice) {
                filteredList.add(c);
            }
        }

        return filteredList;
    }


}