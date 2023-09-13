package com.example.project;


import android.content.Context;

import java.util.ArrayList;

public class CompanyFacade extends  ClientFacade
{
    private int companyID;

    public CompanyFacade(Context context) {
        super(context);
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


    public  void  addCoupon(Coupon coupon)
    {
        Company c1=companiesDAO.getOneCompany(companyID);
        for (Coupon coupons : c1.getCoupons() )
        {
            if (coupons.getTitle().equals(coupon.getTitle()))
            {
                return;
            }

        }
        c1.getCoupons().add(coupon);
    }


    public  void  updateCoupon(Coupon coupon)
    {
        couponsDAO.updateCoupon(coupon);
    }


    public  void  deleteCoupon(int couponID)
    {
        couponsDAO. deleteCoupon(couponID); // delete coupon from coupon table
    }


    public ArrayList<Coupon> getCompanyCoupons()
    {
        Company c1=companiesDAO.getOneCompany(companyID);
        return  c1.getCoupons();
    }

    public ArrayList<Coupon> getCompanyCoupons(Category category)
    {
        ArrayList<Coupon> CompanyCategory = null;
        Company c1=companiesDAO.getOneCompany(companyID);
        for (Coupon coupons : c1.getCoupons() )
        {
            if(coupons.getCategory()==category)
            {
                CompanyCategory.add(coupons);
            }

        }
        return  CompanyCategory;
    }

    public ArrayList<Coupon> getCompanyCoupons(double maxPrice)
    {
        ArrayList<Coupon> CompanyMaxPrice = null;
        Company c1=companiesDAO.getOneCompany(companyID);
        for (Coupon coupons : c1.getCoupons() )
        {
            if(coupons.getPrice()<maxPrice)
            {
                CompanyMaxPrice.add(coupons);
            }

        }

        return  CompanyMaxPrice;
    }



    public  Company getCompanyDetails()
    {
        Company c1=companiesDAO.getOneCompany(companyID);
        return   c1;
    }


}

