package com.example.project;

import java.util.ArrayList;

public interface CustomersDAO {
    Boolean isCustomerExists(String email,String password);
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerID);
    ArrayList<Customer> getAllCustomers();
    Customer getOneCustomer(int customerID);
}