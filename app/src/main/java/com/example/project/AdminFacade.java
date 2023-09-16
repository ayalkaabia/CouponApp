package com.example.project;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminFacade extends ClientFacade implements Serializable {
    private static  final String adminEmail = "admin@admin.com";
    private static  final String adminPass = "admin";
    private static Context context;
    //...Singleton.............................
    private static AdminFacade instance = null;

    private AdminFacade(Context context) throws SQLException, ParseException {
        super(context);
      //  this.context=context;
    }

    public static AdminFacade getInstance(Context context) throws SQLException, ParseException {
        if (instance == null) instance = new AdminFacade(context);
        return instance;
    }
    //...Singleton.............................

    @Override
    boolean login(String email, String password) {
        if(adminEmail.equals(email)&&adminPass.equals((password)))
            return true;
        return false;
    }
    public void addCompany(Company company) throws DataExists, SQLException, ParseException {
        for(Company company1 : companiesDAO.getAllCompanies()){
            if(company1.getName()!=null && company1.getName().equals(company.getName()))
                throw new DataExists("company already exists");
            if(company1.getEmail()!=null&& company1.getEmail().equals(company.getEmail()))
                throw new DataExists("company's email already exists");
        }
        companiesDAO.addCompany(company);
    }
    public void updateCompany(Company company) throws DataNotExists{
        companiesDAO.updateCompany(company);
    }
    public void deleteCompany(int companyID) throws ParseException, DataNotExists {

        ArrayList<Coupon> coupons = couponsDAO.getAllCoupons();
        if(companiesDAO.getOneCompany(companyID)!=null) {
            if(coupons!=null) {
                for (Coupon coupon : coupons) {
                    if (coupon != null && coupon.getCompanyID() == companyID) {
                        deleteCouponFromCustomersCoupons(coupon.getId());
                        couponsDAO.deleteCouponsPurchaseByCouponID(coupon.getId());
                        couponsDAO.deleteCoupon(coupon.getId());
                    }
                }
            }
            companiesDAO.deleteCompany(companyID);

        }
        else
            throw new DataNotExists("company doesn't exists");
    }
    public void deleteCouponFromCustomersCoupons(int couponId) throws ParseException {
        ArrayList<Customer> customers= customersDAO.getAllCustomers();
        for(Customer customer : customers) {
            for (Coupon coupon : customer.getCoupons()){
                if(coupon.getId()==couponId)
                    customer.getCoupons().remove(coupon);
            }
        }
    }
    public  ArrayList<Company> getAllCompanies() throws DataNotExists, ParseException {
        if(companiesDAO.getAllCompanies()!=null) {
            ArrayList<Company> companies1=companiesDAO.getAllCompanies();
            System.out.println("got all companies");
            return companies1;
        }
        else
            throw new DataNotExists("There is no companies");
    }

    public Company getOneCompany(int companyID) throws DataNotExists {
        if(companiesDAO.getOneCompany(companyID)!=null) {
            System.out.println("got one of the companies");
            return companiesDAO.getOneCompany(companyID);
        }
        else
            throw new DataNotExists("company doesn't exists");
    }


    public void addCustomer(Customer customer) throws DataExists, ParseException {
        for (Customer customer1 : customersDAO.getAllCustomers()) {
            if (customer1.getEmail().equals(customer.getEmail()))
                throw new DataExists("customer Email already exists");
        }
        customersDAO.addCustomer(customer);
        System.out.println("added a customer");
    }
    public void updateCustomer(Customer customer) throws DataNotExists {
        customersDAO.updateCustomer(customer);
        System.out.println("updated a customer");

    }
    public void DeleteCustomer(int customerID) throws DataNotExists {
        if(customersDAO.getOneCustomer(customerID)!=null) {
            couponsDAO.deleteCouponsPurchaseByCustomerID(customerID);
            customersDAO.deleteCustomer(customerID);
            System.out.println("deleted a customer");

        }
        else
            throw new DataNotExists("Customer doesn't exists");
    }
    public ArrayList<Customer> getAllCustomers() throws DataNotExists, ParseException {
        if(customersDAO.getAllCustomers()!=null) {
            System.out.println("got all customers");
            return customersDAO.getAllCustomers();
        }
        else
            throw new DataNotExists("There is no customers");
    }
    public Customer getOneCustomer(int customerID) throws DataNotExists {
        if(customersDAO.getOneCustomer(customerID)!=null) {
            System.out.println("got one customer");
            return customersDAO.getOneCustomer(customerID);
        }
        else
            throw new DataNotExists("Customer doesn't exists");
    }
}