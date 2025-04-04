package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;


public class AddNewCompany extends AppCompatActivity {

    EditText etName,etNumber,etUserName,etPassword;
    Button btnSave,btnCancel;
    AdminFacade ad_fe;


    public AddNewCompany() throws SQLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_company);
        try {
            ad_fe = AdminFacade.getInstance(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        etName=findViewById(R.id.addCompany_etName);
        etNumber=findViewById(R.id.addCompany_etNumber); //Number is the id basically
        etUserName=findViewById(R.id.addCompany_etUserName);//username is email
        etPassword=findViewById(R.id.addCompany_etPassword);

        btnSave=findViewById(R.id.addCompany_btnSave);
        btnCancel=findViewById(R.id.addCompany_btnCancel);

        ButtonsListener buttonsListener = new ButtonsListener();
        btnSave.setOnClickListener(buttonsListener);
        btnCancel.setOnClickListener(buttonsListener);
    }
    class ButtonsListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId()==btnSave.getId()){
                String name,userName,password;
                int number;
                name=etName.toString();
                number=Integer.parseInt(etNumber.toString()); //parsing the number from string to int
                userName=etUserName.toString();
                password=etPassword.toString();
                Company newCompany=new Company(number,name,userName,password,null);//when we make a new company it has no coupons yet which is why we give null
                try {
                    ad_fe.companiesDAO.addCompany(newCompany);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (DataExists e) {
                    throw new RuntimeException(e);
                }
                Intent intent=new Intent();
                intent.putExtra("newCompany",newCompany);
                setResult(RESULT_OK);
                finish();
            }
            if(view.getId()==btnCancel.getId()){
                finish();
            }
        }
    }
}