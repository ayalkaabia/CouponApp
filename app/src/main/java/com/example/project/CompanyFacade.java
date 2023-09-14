package com.example.project;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class CompanyFacade extends  ClientFacade implements Serializable
{
    private int companyID;

    public CompanyFacade(Context context) throws ParseException {
        super(context);
    }

    boolean login(String email, String password )
    {
        if( companiesDAO.isCompanyExists(email, password))
        {
            Company c= companiesDAO.isCompanyExists2 (email, password);
            companyID=c.getId();
            System.out.println("company log in");
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
        System.out.println("companyFacade addCoupon");
    }


    public  void  updateCoupon(Coupon coupon)
    {
        couponsDAO.updateCoupon(coupon);
        System.out.println("companyFacade updateCoupon");
    }


    public  void  deleteCoupon(int couponID)
    {
        couponsDAO. deleteCoupon(couponID); // delete coupon from coupon table
        System.out.println("companyFacade deleteCoupon");
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
        ArrayList<Coupon> CompanyCategory = null;
        Company c1=companiesDAO.getOneCompany(companyID);
        for (Coupon coupons : c1.getCoupons() )
        {
            if(coupons.getCategory()==category)
            {
                CompanyCategory.add(coupons);
            }

        }
        System.out.println("companyFacade getCompanyCoupons by category");
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
        System.out.println("companyFacade getCompanyCoupons by maxprice");
        return  CompanyMaxPrice;
    }



    public  Company getCompanyDetails()
    {
        Company c1=companiesDAO.getOneCompany(companyID);
        System.out.println("companyFacade getCompanyDetails");
        return   c1;
    }


}