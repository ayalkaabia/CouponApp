package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.ParseException;

public class updateDetailsActivity extends AppCompatActivity {

    EditText editEmailTxt;
    EditText editPassTxt;
    ImageButton btnSave;
    ImageButton btnCancel;
    int customerId = -1; // Initialize with a default value
    CustomersFacade customersFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        editEmailTxt = findViewById(R.id.editEmail_txt);
        editPassTxt = findViewById(R.id.editPass_txt);
        btnSave = findViewById(R.id.update_btnSave);
        btnCancel = findViewById(R.id.update_btnCancel);

        // Receive the customerFacade as an extra from the previous activity
        customersFacade=(CustomersFacade) getIntent().getSerializableExtra("customersFacade");


        // Set a click listener for the Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the edited email and password
                String editedEmail = editEmailTxt.getText().toString();
                String editedPassword = editPassTxt.getText().toString();

                if( !editedEmail.isEmpty()  && !editedPassword.isEmpty()) {

                    // Update the customer's details in the data source using the customers facade
                    try {
                        Customer customer=customersFacade.getCustomerDetails();
                        customer.setEmail(editedEmail);
                        customer.setPassword(editedPassword);
                        customersFacade.updateCustomer(customer);

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

//////////////////////////////////////////////should be add a code to  update the customer's details at the database///////////////////////
                    String message = "Updated details";
                    Toast.makeText(updateDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(updateDetailsActivity.this, "Please enter Correct Email and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Set a click listener for the Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity or finish this activity
                finish();
            }
        });
    }
}
