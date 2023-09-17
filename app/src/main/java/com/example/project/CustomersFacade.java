package com.example.project;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class CustomersFacade extends ClientFacade implements Serializable {
    private int customerID;

    public CustomersFacade(Context context) throws ParseException {
        super(context);
    }

    @Override
    boolean login(String email, String password) {
        Customer customer=null;
        //customer is performing a login
        customer=customersDAO.getCustomer(email,password);
        if(customer==null) return false;
        //if customer logged in then the customerID variable is the id of the costumer that logged in
        this.customerID=customer.getId();
        System.out.println("customer logged in");
        return true;
    }

    void purchaseCoupon(Coupon coupon) throws Exception {
        ArrayList<Coupon> userCoupons=getCustomerCoupons();
        if(userCoupons.contains(coupon)) throw new Exception("you cant purchase same coupon more than once");
        //checking if coupon quantity is 0

        if(coupon.getAmount()==0){ //if coupon quantity = 0
            throw new Exception("Coupon quantity is 0");
        }
//        //CHECKING IF EXPIRATION DATE  HAS ALREADY ARRIVED
//        if(couponsDAO.checkExpired(coupon.getId())){
//            throw new Exception("expiration date has already arrived");
//        }
        //purchase the coupon and link the coupon id with the customer id + reduce quantity by 1

        couponsDAO.addCouponPurchase(customerID, coupon.getId());
        coupon.setAmount(coupon.getAmount() - 1);
        userCoupons = customersDAO.getOneCustomer(customerID).getCoupons();
        userCoupons.add(coupon);
        couponsDAO.updateCoupon(coupon);
        Company company = companiesDAO.getOneCompany(coupon.getCompanyID());
        for(Coupon coupon1 : company.getCoupons())
            if(coupon1.getId()==coupon.getId())
                coupon1.setAmount(coupon1.getAmount() - 1);

    }
    ArrayList<Coupon> getCustomerCoupons() throws ParseException {

        return customersDAO.getOneCustomer(customerID).getCoupons();
    }
    ArrayList<Coupon> getCustomerCoupons(Category category) throws ParseException { //getting all the customer coupons that are under this Category

        ArrayList<Coupon> returnedCoupons=new ArrayList<>();
        ArrayList<Coupon> coupons1=getCustomerCoupons();//getting customer coupons
        for(Coupon coupon:coupons1){
            if(coupon.getCategory()==category){
                returnedCoupons.add(coupon);
            }
        }
        return returnedCoupons;
    }
    ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws ParseException {
        ArrayList<Coupon> returnedCoupons=new ArrayList<>();
        ArrayList<Coupon> coupons1=getCustomerCoupons();//getting customer coupons
        for(Coupon coupon:coupons1){
            if(coupon.getPrice()<=maxPrice){
                returnedCoupons.add(coupon);
            }
        }
        return returnedCoupons;

    }
    Customer getCustomerDetails() throws ParseException {
        ArrayList<Customer> customers1=customersDAO.getAllCustomers();
        for(Customer customer:customers1){
            if(customer.getId()==customerID){
                return customer;
            }
        }
        return null;
    }


    public void updateCustomer(Customer customer) {

        customersDAO.updateCustomer(customer);
    }
}
