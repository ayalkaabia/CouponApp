package com.example.project;

import java.text.ParseException;
import java.util.ArrayList;

public interface CustomersDAO {
    Boolean isCustomerExists(String email,String password);
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerID);
    ArrayList<Customer> getAllCustomers() throws ParseException;
    Customer getOneCustomer(int customerID);

    Customer getCustomer(String email, String password);
}