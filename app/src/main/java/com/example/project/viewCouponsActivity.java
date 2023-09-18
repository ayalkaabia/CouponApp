package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.project.Coupon;
import com.example.project.CustomersFacade;
import com.example.project.R;

import java.text.ParseException;
import java.util.ArrayList;

public class viewCouponsActivity extends AppCompatActivity {

    Button  addCoupon, returnButton,filterButton,unFilterButton; // i think we should REMOVE the minPrice button and edit text from xml as well as here
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

        maxPriceEt = findViewById(R.id.view_etMaxPrice);
        filterButton = findViewById(R.id.view_filter); // Reference to the Filter button
        unFilterButton = findViewById(R.id.view_unFilter);


        returnButton = findViewById(R.id.view_returnButton);
        couponListView = findViewById(R.id.purchase_couponListView);

        chooseCategory = findViewById(R.id.chooseCate_radio);
        customersFacade = (CustomersFacade) getIntent().getSerializableExtra("customersFacade");
        try {
            loadAllCoupons(); // Load all coupons from the database
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Create an adapter for your coupon list
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, coupons);
        // Set the initial adapter for the ListView
        couponListView.setAdapter(adapter);


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedCategoryId = -1; // Initialize to -1
                int selectedRadioButtonId = -1;
                String maxPriceStr = maxPriceEt.getText().toString();

                selectedRadioButtonId = chooseCategory.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    checkedCategoryId = selectedRadioButtonId;
                }

                if (checkedCategoryId != -1) {
                    Category selectedCategory;
                    if (checkedCategoryId == R.id.purchase_choose_food_btn) {
                        selectedCategory = Category.FOOD;
                    } else if (checkedCategoryId == R.id.purchase_choose_elec_btn) {
                        selectedCategory = Category.ELECTRICITY;
                    } else if (checkedCategoryId == R.id.purchase_choose_rest_btn) {
                        selectedCategory = Category.RESTAURANT;
                    } else if (checkedCategoryId == R.id.purchase_choose_vac_btn) {
                        selectedCategory = Category.VACATION;
                    } else {
                        selectedCategory = null;
                    }

                    // Check if maximum price is also provided
                    if (!maxPriceStr.isEmpty()) {
                        try {
                            filterCouponsByCategoryAndPrice(selectedCategory, Double.parseDouble(maxPriceStr));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        filterCouponsByCategory(selectedCategory);
                    }
                } else if (!maxPriceStr.isEmpty()) {
                    try {
                        filterCouponsByPrice(Double.parseDouble(maxPriceStr));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Neither category nor maximum price provided
                    // Reset the coupon list to its original state
                    resetCouponList();
                }
            }
        });


        unFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFiltersAndDisplayAllCoupons();
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


    public void filterCouponsByCategory(Category selectedCategory) {
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


    public ArrayList<Coupon> filterCouponsByPrice(double maxPrice) throws ParseException {
        ArrayList<Coupon> allCoupons = customersFacade.getCouponsToBuy();
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();

        for (Coupon coupon : allCoupons) {
            if (coupon.getPrice() <= maxPrice) {
                filteredCoupons.add(coupon);
            }
        }

        return filteredCoupons;
    }
    public ArrayList<Coupon> filterCouponsByCategoryAndPrice(Category category, double maxPrice) throws ParseException {
        ArrayList<Coupon> allCoupons = customersFacade.getCustomerCoupons();
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();

        for (Coupon coupon : allCoupons) {
            if (coupon.getCategory() == category && coupon.getPrice() <= maxPrice) {
                filteredCoupons.add(coupon);
            }
        }

        return filteredCoupons;
    }
    private void loadAllCoupons() throws ParseException {
        coupons = customersFacade.getCustomerCoupons();
        adapter.clear();
        adapter.addAll(coupons);
        adapter.notifyDataSetChanged();
    }
    private void resetCouponList() {
        try {
            coupons = customersFacade.getCustomerCoupons();
            adapter.clear();
            adapter.addAll(coupons);
            adapter.notifyDataSetChanged();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private void resetFiltersAndDisplayAllCoupons() {
        try {
            loadAllCoupons(); // Load all coupons from the database
            maxPriceEt.setText(""); // Clear the max price EditText
            chooseCategory.clearCheck(); // Clear the selected radio button in the RadioGroup
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}