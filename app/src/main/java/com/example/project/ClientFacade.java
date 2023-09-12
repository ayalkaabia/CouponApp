package com.example.project;

import java.sql.SQLException;


public abstract class ClientFacade {

    CompaniesDAO companiesDAO;
    CouponsDAO couponsDAO;
    CustomersDAO customersDAO;

    public ClientFacade(CompaniesDAO companiesDAO, CouponsDAO couponsDAO, CustomersDAO customersDAO) {
        this.companiesDAO = companiesDAO;
        this.couponsDAO = couponsDAO;
        this.customersDAO = customersDAO;
    }

    abstract boolean login(String email, String password) ;



}
