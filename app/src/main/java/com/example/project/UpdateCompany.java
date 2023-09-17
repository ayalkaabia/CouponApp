package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateCompany extends AppCompatActivity {
Button Cancel,find,Update;
TextView name,email,id,Password;
AdminFacade ad_fe=AdminFacade.getInstance(UpdateCompany.this);
    private RecyclerView recyclerView;

    public UpdateCompany() throws SQLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_company);
        Cancel=findViewById(R.id.updateCompanyCancel);
        find=findViewById(R.id.updateCompanyFind);
        Update=findViewById(R.id.updateCompanyButton);
        name=findViewById(R.id.UpdateCompanyName);
        email=findViewById(R.id.UpdateCompanyEmail);
        id=findViewById(R.id.UpdateCompanyId);
        Password=findViewById(R.id.UpdateCompanyPassword);
    }

    class ButtonsListener implements View.OnClickListener
    {


        @Override
        public void onClick(View view) {
            Company company=null;
            if(view.getId()==Cancel.getId())
            {
                finish();
            }
            else if(view.getId()==find.getId())
            {
              company= ad_fe.companiesDAO.getOneCompany(Integer.parseInt(id.getText().toString()));
              name.setText(company.getName());
              email.setText(company.getEmail());
              Password.setText(company.getPassword());
              ArrayList<Coupon>coupons=new ArrayList<>();
                try {
                    coupons= ad_fe.getOneCompany(Integer.parseInt(id.getText().toString())).getCoupons();
                } catch (DataNotExists e) {
                    throw new RuntimeException(e);
                }

                recyclerView=view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(UpdateCompany.this));
                recyclerView.setHasFixedSize(false);
                CouponRecViewAdapter adapter=new CouponRecViewAdapter(UpdateCompany.this,coupons);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else if(view.getId()==Update.getId())
            {
                String cname=name.getText().toString();
                String cemail=email.getText().toString();
                String Cpassword=Password.getText().toString();
                name.setText("");
                email.setText("");
                Password.setText("");
                company.setEmail(cemail);
                company.setName(cname);
                company.setPassword(Cpassword);
                try {
                    ad_fe.companiesDAO.updateCompany(company);
                } catch (DataNotExists e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}