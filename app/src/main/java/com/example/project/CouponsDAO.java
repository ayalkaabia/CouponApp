package com.example.project;

import java.text.ParseException;
import java.util.ArrayList;

public interface CouponsDAO {
    void addCoupon(Coupon coupon);
    void updateCoupon(Coupon coupon);
    void deleteCoupon(int couponID);
    ArrayList<Coupon> getAllCoupons() throws ParseException;
    Coupon getOneCoupon(int couponID);
    void addCouponPurchase(int customerID,int couponID);
    void deleteCouponPurchase(int customerID,int couponID);
    void deleteCouponsPurchaseByCouponID(int couponID);
     void deleteCouponsPurchaseByCustomerID(int couponID);


}