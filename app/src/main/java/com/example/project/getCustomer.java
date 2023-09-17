package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class getCustomer extends AppCompatActivity {
    Button ShowFields;
    TextView lName,fName,Email,cancel,id;
    ArrayList<Customer> customers=new ArrayList<>();
    AdminFacade ad_fe=AdminFacade.getInstance(getCustomer.this);

    public getCustomer() throws SQLException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_customer);

        ShowFields=findViewById(R.id.GetCustomerShow);
        lName=findViewById(R.id.GetCustomerLname);
        fName=findViewById(R.id.GetCustomerFname);
        Email=findViewById(R.id.GetCustomerEmail);
        cancel=findViewById(R.id.GetCustomerCancel);
        id=findViewById(R.id.GetCustomerId);
//        customers.add()
        cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ShowFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Customer myCustomer=  ad_fe.customersDAO.getOneCustomer(Integer.parseInt(id.getText().toString()));
                lName.setVisibility(View.VISIBLE);
                lName.setText(myCustomer.getlName());
                fName.setVisibility(View.VISIBLE);
                fName.setText(myCustomer.getfName());
                Email.setVisibility(View.VISIBLE);
                Email.setText(myCustomer.getEmail());
            }
        });


    }
}