package com.example.project;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class CompanyFacade extends  ClientFacade implements Serializable
{
    private int companyID=13;
    private static Context context;

    public CompanyFacade(Context context) throws ParseException {
        super(context);
        this.context = context;
    }

    boolean login(String email, String password )
    {
        if( companiesDAO.isCompanyExists(email, password))
        {
            Company c= companiesDAO.isCompanyExists2 (email, password);
            companyID=c.getId();
            return  true;
        }
        return  false;
    }


    public  void  addCoupon(Coupon coupon) throws ParseException {
        Company c1=companiesDAO.getOneCompany(companyID);
        CouponsDBDAO couponsDBDAO= CouponsDBDAO.getInstance(context);
        if(c1.getCoupons()!=null) {
            for (Coupon coupons : c1.getCoupons()) {
                if (coupons.getTitle().equals(coupon.getTitle())) {
                    return;
                }
            }
        }
        c1.getCoupons().add(coupon);
        couponsDBDAO.addCoupon(coupon);
    }


    public  void  updateCoupon(Coupon coupon)
    {
        couponsDAO.updateCoupon(coupon);
    }


    public  void  deleteCoupon(int couponID) throws ParseException {
        Company company= companiesDAO.getOneCompany(companyID);
        ArrayList<Coupon> coupons= company.getCoupons();
        for(Coupon coupon : coupons)
            if(coupon.getId()==couponID)
                coupons.remove(coupon);
        deleteCouponFromCustomersCoupons(couponID);
        couponsDAO.deleteCouponsPurchaseByCouponID(couponID);
        couponsDAO.deleteCoupon(couponID); // delete coupon from coupon table
        System.out.println("companyFacade deleteCoupon");
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

    public ArrayList<Coupon> getCompanyCoupons()
    {
        Company c1=companiesDAO.getOneCompany(companyID);
        ArrayList<Coupon> coupons=c1.getCoupons();
        System.out.println("companyFacade getCompanyCoupons");
        return  coupons;
    }

    public ArrayList<Coupon> getCompanyCoupons(Category category)
    {
        ArrayList<Coupon> CompanyCouponsByCategory = new ArrayList<>();
        Company c1=companiesDAO.getOneCompany(companyID);
        for (Coupon coupons : c1.getCoupons() )
        {
            if(coupons.getCategory()==category)
            {
                CompanyCouponsByCategory.add(coupons);
            }

        }
        return  CompanyCouponsByCategory;
    }

    public ArrayList<Coupon> getCompanyCoupons(double maxPrice)
    {
        ArrayList<Coupon> companyCouponsByMaxPrice = new ArrayList<>();
        Company c1=companiesDAO.getOneCompany(companyID);
        for (Coupon coupons : c1.getCoupons() )
        {
            if(coupons.getPrice()<maxPrice)
            {
                companyCouponsByMaxPrice.add(coupons);
            }

        }
        return   companyCouponsByMaxPrice;
    }



    public  Company getCompanyDetails()
    {
        Company c1=companiesDAO.getOneCompany(companyID);
        return   c1;
    }


}