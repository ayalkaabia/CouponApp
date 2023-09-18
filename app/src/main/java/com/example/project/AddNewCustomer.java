package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class AddNewCustomer extends AppCompatActivity {
TextView Id,firstName,lastName,Email,Password;
Button Cancel,Add;
AdminFacade ad_fe=AdminFacade.getInstance(AddNewCustomer.this);

    public AddNewCustomer() throws SQLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        Id =findViewById(R.id.AddCustomerIDtf);
        firstName=findViewById(R.id.AddCustomerFirstNametf);
        lastName=findViewById(R.id.AddCustomerLastNametf);
        Email=findViewById(R.id.AddCustomerEmailtf);
        Password=findViewById(R.id.AddCustomerPasswordtf);
        Cancel=findViewById(R.id.AddCustomerCancelButton);
        Add=findViewById(R.id.AdddCustomerAddButton);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=Id.getText().toString();
                int integer_id=Integer.parseInt(id);
                String fName=firstName.getText().toString();
                String lName=lastName.getText().toString();
                String password=Password.getText().toString();
                String email=Email.getText().toString();
                Customer cs=new Customer(integer_id,fName,lName,email,password,null);
                Id.setText("");
                firstName.setText("");
                lastName.setText("");
                Password.setText("");
                Email.setText("");
               try {
                   ad_fe.customersDAO.addCustomer(cs);
               }
               catch (Exception e)
               {
                   Toast.makeText(AddNewCustomer.this, "something failed check values or if id already exists", Toast.LENGTH_SHORT).show();
                   throw e;
               }
            }
        });
    }
}