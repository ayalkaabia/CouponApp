package com.example.project;

import java.sql.SQLException;


public abstract class ClientFacade {

    CompaniesDAO companiesDAO;
    CouponsDAO couponsDAO;
    CustomersDAO customersDAO;

    abstract boolean login(String email, String password) ;



}
