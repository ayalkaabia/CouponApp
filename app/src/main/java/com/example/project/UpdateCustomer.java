package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateCustomer extends AppCompatActivity {
    TextView id,firstname,lastname,email;
    Button UpdateBtn,CancelBtn;
    CustomersDBDAO CustomerDataBase=CustomersDBDAO.getInstance(UpdateCustomer.this);

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
                    updateValue=CustomerDataBase.getOneCustomer(Id);
                }
                catch (Exception e)
                {
                    Toast.makeText(UpdateCustomer.this, "something went wrong with finding customer", Toast.LENGTH_SHORT).show();
                    throw e;
                }
                String fname=updateValue.getfName();
                String lname=updateValue.getlName();
                String Email=updateValue.getEmail();
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
                id.setText("");
                firstname.setText("");
                lastname.setText("");
                email.setText("");


                updateValue.setEmail(Email);
                updateValue.setfName(fname);
                updateValue.setlName(lname);


                CustomerDataBase.updateCustomer(updateValue);
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