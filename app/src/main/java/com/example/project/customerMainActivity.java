package com.example.project;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class customerMainActivity extends AppCompatActivity {

    Button detailsBtn, coustomerCoupons; // login button
    CustomersFacade customersFacade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        customersFacade=(CustomersFacade) getIntent().getSerializableExtra("customersFacade");

        detailsBtn = findViewById(R.id.details_btn);
        coustomerCoupons = findViewById(R.id.coupons_btn);

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the viewdetailsActivity
                Intent intent = new Intent(customerMainActivity.this, viewdetailsActivity.class);
                intent.putExtra("customersFacade", customersFacade);
                startActivity(intent);
            }
        });

        coustomerCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the viewCouponsActivity
                Intent intent = new Intent(customerMainActivity.this, viewCouponsActivity.class);
                intent.putExtra("customersFacade", customersFacade);
                startActivity(intent);
            }
        });
    }
}





