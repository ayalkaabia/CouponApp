package com.example.project;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class CustomersFacade extends ClientFacade implements Serializable {
    private int customerID;

    public CustomersFacade(Context context) {
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


    }
    ArrayList<Coupon> getCustomerCoupons() throws ParseException {

        ArrayList<Customer> customers1=customersDAO.getAllCustomers();
        for(Customer customer:customers1){
            if(customer.getId()==customerID){
                return customer.getCoupons();
            }
        }
        return null;
    }
    ArrayList<Coupon> getCustomerCoupons(Category category) throws ParseException { //getting all the customer coupons that are under this Category

        ArrayList<Coupon> returnedCoupons=null;
        ArrayList<Coupon> coupons1=getCustomerCoupons();//getting customer coupons
        for(Coupon coupon:coupons1){
            if(coupon.getCategory()==category){
                returnedCoupons.add(coupon);
            }
        }
        return returnedCoupons;
    }
    ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws ParseException {
        ArrayList<Coupon> returnedCoupons=null;
        ArrayList<Coupon> coupons1=getCustomerCoupons();//getting customer coupons
        for(Coupon coupon:coupons1){
            if(coupon.getPrice()<=maxPrice){
                returnedCoupons.add(coupon);
            }
        }
        return returnedCoupons;

    }
    Customer getCustomerDetails(){
        ArrayList<Customer> customers1=customersDAO.getAllCustomers();
        for(Customer customer:customers1){
            if(customer.getId()==customerID){
                return customer;
            }
        }
        return null;
    }


}
