package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class UpdateCustomer extends AppCompatActivity {
    TextView id,firstname,lastname,email,Pass;
    Button UpdateBtn,CancelBtn;
    AdminFacade ad_fe=AdminFacade.getInstance(UpdateCustomer.this);

    public UpdateCustomer() throws SQLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);
        id=findViewById(R.id.AdminUpdateCustomerId);
        firstname=findViewById(R.id.AdminUpdateCustomerFN);
        lastname=findViewById(R.id.AdminUpdateCustomerlN);
        email=findViewById(R.id.UpdateCustomerEmail);
        UpdateBtn=findViewById(R.id.AdminCustomerUpdateBtn);
        CancelBtn=findViewById(R.id.updateCustomerCancel);
        Pass=findViewById(R.id.UpdateCustomerPass);

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Id;
                Customer updateValue;
                try {
                     Id = Integer.valueOf(id.getText().toString());
                }
                catch (Exception e)
                {
                    throw e;
                }
                try{
                    updateValue=ad_fe.customersDAO.getOneCustomer(Id);
                }
                catch (Exception e)
                {
                    Toast.makeText(UpdateCustomer.this, "something went wrong with finding customer", Toast.LENGTH_SHORT).show();
                    throw e;
                }
                String fname=updateValue.getfName();
                String lname=updateValue.getlName();
                String Email=updateValue.getEmail();
                String Password=updateValue.getPassword();
                if(!firstname.getText().toString().equals(null))
                {
                     fname=firstname.getText().toString();
                }
                if(!lastname.getText().toString().equals(null))
                {
                    lname=lastname.getText().toString();
                }
                if(!lastname.getText().toString().equals(null))
                {
                    Email=email.getText().toString();
                }
                if(!Pass.getText().toString().equals(null))
                {
                    Password=Pass.getText().toString();
                }
                id.setText("");
                firstname.setText("");
                lastname.setText("");
                email.setText("");


                updateValue.setEmail(Email);
                updateValue.setfName(fname);
                updateValue.setlName(lname);
                updateValue.setPassword(Password);


                ad_fe.customersDAO.updateCustomer(updateValue);
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}