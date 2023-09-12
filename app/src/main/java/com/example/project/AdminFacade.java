package com.example.project;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminFacade extends ClientFacade {

    //...Singleton.............................
    private static AdminFacade instance = null;

    private AdminFacade(CompaniesDAO companiesDAO,CouponsDAO couponsDAO,CustomersDAO customersDAO) {
        super(companiesDAO,couponsDAO,customersDAO);
    }

    public static AdminFacade getInstance(CompaniesDAO companiesDAO,CouponsDAO couponsDAO,CustomersDAO customersDAO) {
        if (instance == null) instance = new AdminFacade(companiesDAO,couponsDAO,customersDAO);
        return instance;
    }
    //...Singleton.............................
    @Override
    boolean login(String email, String password) {
        return false;
    }
    public void addCompany(Company company) throws DataExists, SQLException {
        for(Company company1 : companiesDAO.getAllCompanies()){
            if(company1.getName().equals(company.getName()))
                throw new DataExists("company already exists");
            if(company1.getEmail().equals(company.getEmail()))
                throw new DataExists("company's email already exists");
        }
        companiesDAO.addCompany(company);
    }
    public void updateCompany(Company company) throws DataNotExists{
        companiesDAO.updateCompany(company);
    }
    public void deleteCompany(int companyID) throws ParseException, DataNotExists {
        ArrayList<Company> companies = companiesDAO.getAllCompanies();
        ArrayList<Coupon> coupons = couponsDAO.getAllCoupons();
        ArrayList<Customer> customers = customersDAO.getAllCustomers();
        if(companiesDAO.getOneCompany(companyID)!=null){
                for (Coupon coupon : coupons) {
                    if (coupon.getCompanyID() == companyID)
                        couponsDAO.deleteCouponsPurchaseByCouponID(coupon.getId());
                        couponsDAO.deleteCoupon(coupon.getId());
                }
            }
      else
          throw new DataNotExists("company doesn't exists");
    }
    public  ArrayList<Company> getAllCompanies() throws DataNotExists {
        if(companiesDAO.getAllCompanies()!=null) {
            return companiesDAO.getAllCompanies();
        }
        else
            throw new DataNotExists("There is no companies");
    }

    public Company getOneCompany(int companyID) throws DataNotExists {
        if(companiesDAO.getOneCompany(companyID)!=null) {
            return companiesDAO.getOneCompany(companyID);
        }
       else
           throw new DataNotExists("company doesn't exists");
    }


    public void addCustomer(Customer customer) throws DataExists {
        for (Customer customer1 : customersDAO.getAllCustomers()) {
            if (customer1.getEmail().equals(customer.getEmail()))
                throw new DataExists("customer Email already exists");
        }
        customersDAO.addCustomer(customer);
    }
    public void updateCustomer(Customer customer){
        customersDAO.updateCustomer(customer);
    }
    public void DeleteCustomer(int customerID) throws DataNotExists {
        if(customersDAO.getOneCustomer(customerID)!=null) {
            couponsDAO.deleteCouponsPurchaseByCustomerID(customerID);
            customersDAO.deleteCustomer(customerID);
        }
        else
            throw new DataNotExists("Customer doesn't exists");
    }
    public ArrayList<Customer> getAllCustomers() throws DataNotExists {
        if(customersDAO.getAllCustomers()!=null) {
            return customersDAO.getAllCustomers();
        }
        else
            throw new DataNotExists("There is no customers");
    }
    public Customer getOneCustomer(int customerID) throws DataNotExists {
        if(customersDAO.getOneCustomer(customerID)!=null) {
            return customersDAO.getOneCustomer(customerID);
        }
        else
            throw new DataNotExists("Customer doesn't exists");
    }
}
