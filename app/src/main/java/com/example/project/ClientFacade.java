package com.example.project;

import android.content.Context;

import java.sql.SQLException;


public abstract class ClientFacade {

    CompaniesDAO companiesDAO;
    CouponsDAO couponsDAO;
    CustomersDAO customersDAO;

    public ClientFacade(Context context) {
        this.customersDAO = CustomersDBDAO.getInstance(context);
        this.couponsDAO = CouponsDBDAO.getInstance(context);
        this.companiesDAO = companiesDAO.getInstance(context);


    }


    abstract boolean login(String email, String password) ;



}
