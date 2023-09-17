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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class viewCouponsActivity extends AppCompatActivity {

    Button maxPrice, addCoupon, returnButton; // i think we should REMOVE the minPrice button and edit text from xml as well as here
    EditText maxPriceEt;

    ListView couponListView;
    RadioGroup chooseCategory;
    ArrayAdapter<Coupon> adapter;
    ArrayList<Coupon> coupons;
    CustomersFacade customersFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_coupons);

        maxPriceEt = findViewById(R.id.etMaxPrice);


        maxPrice = findViewById(R.id.maxPrice_btn);
        addCoupon = findViewById(R.id.purchaseNew_btn);
        returnButton = findViewById(R.id.returnButton);
        couponListView = findViewById(R.id.couponListView);

        chooseCategory = findViewById(R.id.chooseCate_radio);
        customersFacade = (CustomersFacade) getIntent().getSerializableExtra("customersFacade");

        coupons = new ArrayList<>();
        try {
            coupons = customersFacade.getCustomerCoupons(); //coupons arraylist has all customer coupons
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        //!!!!!!!!!!!!!!!!!!!!!!! SHOULD I SHOW ALL COUPONS THE MOMENT WE PRESS VIEW COUPONS?  I THINK YES AND I THINK SHAHD DID IT !!!!!!!!!

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


        maxPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double maximumPrice = Double.parseDouble(maxPriceEt.getText().toString());

                if (maximumPrice != Double.MIN_VALUE) {
                    ArrayList<Coupon> maxPriceCoupons;
                    try {
                        maxPriceCoupons = customersFacade.getCustomerCoupons(maximumPrice);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
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
                Intent intent = new Intent(viewCouponsActivity.this, NewCouponActivity.class);
                intent.putExtra("customerFacade", customersFacade);
                startActivity(intent);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void filterCouponsByCategory(Category selectedCategory) {
        ArrayList<Coupon> filteredCoupons;
        try {
            filteredCoupons = customersFacade.getCustomerCoupons(selectedCategory);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (filteredCoupons.isEmpty()) {
            Toast.makeText(viewCouponsActivity.this, "No coupons found for the selected category.", Toast.LENGTH_SHORT).show();
        } else {
            // Update the adapter with the filtered coupons and refresh the ListView
            adapter.clear();
            adapter.addAll(filteredCoupons);
            adapter.notifyDataSetChanged();
        }


    }



}