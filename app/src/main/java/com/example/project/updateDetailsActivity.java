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

public class updateDetailsActivity extends AppCompatActivity {

    EditText editEmailTxt;
    EditText editPassTxt;
    ImageButton btnSave;
    ImageButton btnCancel;
    int customerId = -1; // Initialize with a default value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        editEmailTxt = findViewById(R.id.editEmail_txt);
        editPassTxt = findViewById(R.id.editPass_txt);
        btnSave = findViewById(R.id.update_btnSave);
        btnCancel = findViewById(R.id.update_btnCancel);

        // Receive the customer ID as an extra from the previous activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("customerId")) {
            customerId = intent.getIntExtra("customerId", -1); // Store the received customer ID
        }

        // Set a click listener for the Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the edited email and password
                String editedEmail = editEmailTxt.getText().toString();
                String editedPassword = editPassTxt.getText().toString();

                // Update the customer's details in the data source using the received customer ID
                if (customerId != -1) {
//////////////////////////////////////////////should be add a code to  update the customer's details at the database///////////////////////
                    String message = "Updated details";
                    Toast.makeText(updateDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    // Handle the case where there's no valid customer ID
                    Toast.makeText(updateDetailsActivity.this, "Invalid customer ID", Toast.LENGTH_SHORT).show();
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
