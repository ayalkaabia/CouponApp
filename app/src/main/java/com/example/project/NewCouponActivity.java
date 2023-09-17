package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class NewCouponActivity extends AppCompatActivity {
    EditText companyName,maxPrice;
    ArrayList<Coupon> coupons;
    RadioGroup chooseCategory ;
    Button buyBtn,returnBtn,filterBtn,unfilterBtn;
    CustomersFacade customersFacade;
    CouponAdapter adapter;
    ListView purchaseList;
    int bgLineColor;
    LinearLayout bgLayout;
    int selectedRow=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coupon);

        customersFacade=(CustomersFacade) getIntent().getSerializableExtra("customersFacade");

        buyBtn=findViewById(R.id.purchase_buy);
        returnBtn=findViewById(R.id.purchase_return_button);
        filterBtn=findViewById(R.id.purchase_filter);
        unfilterBtn=findViewById(R.id.purchase_unFilter);
        companyName= findViewById(R.id.purchase_company_name);
        maxPrice=findViewById(R.id.purchase_maxPrice);
        purchaseList=findViewById(R.id.purchase_couponListView);




        ButtonsClick buttonsClick = new ButtonsClick();
        buyBtn.setOnClickListener(buttonsClick);
        filterBtn.setOnClickListener(buttonsClick);
        returnBtn.setOnClickListener(buttonsClick);
        unfilterBtn.setOnClickListener(buttonsClick);


        try {
            coupons = customersFacade.getCouponsToBuy();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        adapter = new CouponAdapter(this, R.layout.coupon_list_line,coupons);
        purchaseList.setAdapter(adapter);

        purchaseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedRow!=-1){
                    bgLayout.setBackgroundColor(bgLineColor);
                }
                selectedRow = position;
                purchaseList.setSelection(position);
                bgLayout=view.findViewById(R.id.coupon_bgLayout);
                bgLineColor=view.getSolidColor();
                bgLayout.setBackgroundColor(Color.rgb(50,150,150));
            }
        });


    }
    class ButtonsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == buyBtn.getId()) {
                try {
                    Coupon coupon=customersFacade.getCouponsToBuy().get(selectedRow);
                    customersFacade.purchaseCoupon(coupon);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                finish();
            }
            else if (v.getId() == filterBtn.getId()) {
                int checkedCategoryId = -1; // Initialize to -1
                int selectedRadioButtonId = -1;
//                String company=companyName.getText().toString();
                String maxPriceStr = maxPrice.getText().toString();

                selectedRadioButtonId = chooseCategory.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    checkedCategoryId = selectedRadioButtonId;
                }

                if (checkedCategoryId != -1) { // if category is selected
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

                    // Check if maximum price is also provided as well as category
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
            else if (v.getId() == unfilterBtn.getId()){
                resetCouponList();
            }
            else if (v.getId() == returnBtn.getId()){
                finish();
            }
        }



    }

    public void filterCouponsByCategory(Category selectedCategory) {
        ArrayList<Coupon> filteredCoupons;
        try {
            filteredCoupons = customersFacade.getCustomerCoupons(selectedCategory);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (filteredCoupons.isEmpty()) {
            Toast.makeText(NewCouponActivity.this, "No coupons found for the selected category.", Toast.LENGTH_SHORT).show();
        } else {
            // Update the adapter with the filtered coupons and refresh the ListView
            adapter.clear();
            adapter.addAll(filteredCoupons);
            adapter.notifyDataSetChanged();
        }


    }


    public ArrayList<Coupon> filterCouponsByPrice(double maxPrice) throws ParseException {
        ArrayList<Coupon> allCoupons = customersFacade.getCouponsToBuy(); // Assuming you have a method to fetch all coupons from the database
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();

        for (Coupon coupon : allCoupons) {
            if (coupon.getPrice() <= maxPrice) {
                filteredCoupons.add(coupon);
            }
        }

        return filteredCoupons;
    }
    public ArrayList<Coupon> filterCouponsByCategoryAndPrice(Category category, double maxPrice) throws ParseException {
        ArrayList<Coupon> allCoupons = customersFacade.getCouponsToBuy(); // Assuming you have a method to fetch all coupons from the database
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();

        for (Coupon coupon : allCoupons) {
            if (coupon.getCategory() == category && coupon.getPrice() <= maxPrice) {
                filteredCoupons.add(coupon);
            }
        }

        return filteredCoupons;
    }


    private Category mapCategoryTextToEnum(String categoryText) {
        switch (categoryText) {
            case "Food":
                return Category.FOOD;
            case "Electricity":
                return Category.ELECTRICITY;
            case "Restaurant":
                return Category.RESTAURANT;
            case "Vacation":
                return Category.VACATION;
            default:
                return Category.FOOD;
        }
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
}