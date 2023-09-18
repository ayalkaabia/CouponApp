package com.example.project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project.CustomersFacade;
import com.example.project.Customer;
import java.text.ParseException;


public class viewdetailsActivity extends AppCompatActivity {
    private TextView idTextView, fNameTextView, lNameTextView, emailTextView;
    private CustomersFacade customersFacade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdetails);

        // Initialize TextViews
        idTextView = findViewById(R.id.customerId_txt);
        fNameTextView = findViewById(R.id.fName_txt);
        lNameTextView = findViewById(R.id.lName_txt);
        emailTextView = findViewById(R.id.customerEmail_txt);

        // Initialize your CustomersFacade
        try {
            customersFacade = new CustomersFacade(this);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            // Retrieve the customer details
            Customer customer = customersFacade.getCustomerDetails();

            if (customer != null) {
                // Display customer details in TextViews
                idTextView.setText("ID: " + customer.getId());
                fNameTextView.setText("First Name: " + customer.getfName());
                lNameTextView.setText("Last Name: " + customer.getlName());
                emailTextView.setText("Email: " + customer.getEmail());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}