package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class NewCouponActivity extends AppCompatActivity {
    EditText companyName,maxPrice;
    RadioGroup chooseCategory ;
    Button buyBtn,returnBtn,filterBtn,unfilterBtn;
    CustomersFacade customersFacade;

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


        ButtonsClick buttonsClick = new ButtonsClick();
        newCouponSave.setOnClickListener(buttonsClick);
        newCouponCancel.setOnClickListener(buttonsClick);
    }
    class ButtonsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == newCouponSave.getId()) {
                if (couponId.getText().toString().isEmpty() || amount.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(NewCouponActivity.this, "Please fill all fields", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (Integer.parseInt(amount.getText().toString()) < 1) {
                    amount.setError("Amount must be positive");
                    return;
                }

                // Retrieve user input from UI elements
                String couponIdText = couponId.getText().toString();
                int companyID = 0; // Replace with the actual company ID (if available)
                String amountText = amount.getText().toString();
                String title = "Coupon Title"; // Replace with the actual title
                String description = "Coupon Description"; // Replace with the actual description
                String image = "Coupon Image URL"; // Replace with the actual image URL

                // Convert couponIdText and amountText to integers
                int couponIdInt = Integer.parseInt(couponIdText);
                int amountInt = Integer.parseInt(amountText);

                // Retrieve the selected radio button's text (category)
                int selectedRadioButtonId = chooseCategory.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String selectedCategoryText = selectedRadioButton.getText().toString();

                // Map the selected category text to your category enum
                Category selectedCategory = mapCategoryTextToEnum(selectedCategoryText);

                double price = 0; // Replace with the actual price
                Date startDate = new Date(); // Replace with the actual start date
                Date endDate = new Date(); // Replace with the actual end date

                // Create the coupon object
                Coupon newCoupon = new Coupon(couponIdInt, companyID, selectedCategory, title, description, startDate, endDate, amountInt, price, image);
                Intent intent = getIntent();
                intent.putExtra("coupon", newCoupon);
                intent.putExtra("requestCode", 1);
                setResult(RESULT_OK, intent);
                finish();
            } else if (v.getId() == newCouponCancel.getId()) {
                finish();
            }
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
    }
}