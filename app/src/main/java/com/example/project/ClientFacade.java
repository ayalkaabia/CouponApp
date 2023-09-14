package com.example.project;

import android.content.Context;

import java.sql.SQLException;
import java.text.ParseException;


public abstract class ClientFacade {

    CompaniesDAO companiesDAO;
    CouponsDAO couponsDAO;
    CustomersDAO customersDAO;

    public ClientFacade(Context context) throws ParseException {
        this.customersDAO = CustomersDBDAO.getInstance(context);
        this.couponsDAO = CouponsDBDAO.getInstance(context);
        this.companiesDAO = CompaniesDBDAO.getInstance(context);


    }


    abstract boolean login(String email, String password) ;



}
