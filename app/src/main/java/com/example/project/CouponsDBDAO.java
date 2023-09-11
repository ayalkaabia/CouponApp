package com.example.project;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CouponsDBDAO implements CouponsDAO
{
    ArrayList<Coupon> coupons;
    private static CouponsDBDAO instance = null;
    DB_Manager dbManager;


//...Singleton.............................
    private CouponsDBDAO(Context context) {
        //companies=getAllCompanies();
        dbManager = DB_Manager.getInstance(context);
        coupons=new ArrayList<Coupon>();
    }


    public static CouponsDBDAO getInstance(Context context) throws SQLException {
        if (instance == null) instance = new CouponsDBDAO(context);
        return instance;
    }
//...Singleton.............................
    @Override
    public void addCoupon(Coupon coupon) {
        Coupon newCoupon=getOneCoupon(coupon.getId());
        String[] fields={dbManager.CATEGORY_ID, dbManager.CATEGORY_NAME};
        if(newCoupon==null)
        {
            ContentValues cv = new ContentValues();
            cv.put(dbManager.COUPONS_ID, coupon.getId());
            cv.put(dbManager.KEY_COMPANY_ID_FK, coupon.getCompanyID());
            String categoryType=null;
            if (coupon.getCategory() == Category.FOOD)
                categoryType = "FOOD";
            if (coupon.getCategory() == Category.ELECTRICITY)
                categoryType = "ELECTRICITY";
            if (coupon.getCategory() == Category.RESTAURANT)
                categoryType = "RESTAURANT";
            if (coupon.getCategory() == Category.VACATION)
                categoryType = "VACATION";
            Cursor cr = dbManager.getCursor(dbManager.TBL_CATEGORIES, fields, dbManager.CATEGORY_NAME +"= '" + categoryType + "'");            int category_id = cr.getInt(0);

            cv.put(dbManager.KEY_CATEGORY_ID_FK, category_id);
            cv.put(dbManager.COUPONS_TITLE, coupon.getTitle());
            cv.put(dbManager.COUPONS_DESCRIPTION, coupon.getDescription());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate =coupon.getStartDate();
            String formattedStartDate = sdf.format(startDate);

            Date endDate =coupon.getEndDate();
            String formattedEndDate = sdf.format(endDate);

            cv.put(dbManager.COUPONS_START_DATE, formattedStartDate);
            cv.put(dbManager.COUPONS_END_DATE, formattedEndDate);
            cv.put(dbManager.COUPONS_AMOUNT, coupon.getAmount());
            cv.put(dbManager.COUPONS_PRICE, coupon.getPrice());
            cv.put(dbManager.COUPONS_IMAGE, coupon.getImage());

            dbManager.getWritableDatabase().insert(dbManager.TBL_COUPONS, null, cv);
        }
        else
            try {
                throw new DataExists("This employee already exists !");
            } catch (DataExists e) {
                throw new RuntimeException(e);
            }
        }
    @Override
    public void updateCoupon(Coupon coupon) {
        Coupon updatedCoupon = getOneCoupon(coupon.getId());
        if (updatedCoupon!=null) {

            for(Coupon coupon1:coupons){
                if(coupon1.getId()==coupon.getId()){         //updating the coupon in the array list
                    coupon1.setCategory(coupon.getCategory());
                    coupon1.setCompanyID(coupon1.getCompanyID());
                    coupon1.setTitle(coupon1.getTitle());
                    coupon1.setDescription(coupon1.getDescription());
                    coupon1.setStartDate(coupon1.getStartDate());
                    coupon1.setEndDate(coupon1.getEndDate());
                    coupon1.setAmount(coupon1.getAmount());
                    coupon1.setPrice(coupon1.getPrice());
                    coupon1.setImage(coupon1.getImage());
                }
            }
            String []fields={dbManager.CATEGORY_ID}; // we only need the category id
            ContentValues cv = new ContentValues();

            //Preparing the categoryID by its type
            String categoryType=null;
            if (coupon.getCategory() == Category.FOOD)
                categoryType = "FOOD";
            if (coupon.getCategory() == Category.ELECTRICITY)
                categoryType = "ELECTRICITY";
            if (coupon.getCategory() == Category.RESTAURANT)
                categoryType = "RESTAURANT";
            if (coupon.getCategory() == Category.VACATION)
                categoryType = "VACATION";
            Cursor cr = dbManager.getCursor(dbManager.TBL_CATEGORIES, fields, dbManager.CATEGORY_NAME +"= '" + categoryType + "'");
            int category_id = cr.getInt(0);
            cv.put(dbManager.CATEGORY_ID,category_id );

            //preparing the start and end date from Dates to String
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate =coupon.getStartDate();
            String formattedStartDate = sdf.format(startDate);
            Date endDate =coupon.getEndDate();
            String formattedEndDate = sdf.format(endDate);

            cv.put(dbManager.COUPONS_START_DATE, formattedStartDate);
            cv.put(dbManager.COUPONS_END_DATE, formattedEndDate);

            //preparing the rest of the cv
            cv.put(dbManager.COMPANY_ID, coupon.getCompanyID());
            cv.put(dbManager.COUPONS_TITLE, coupon.getTitle());
            cv.put(dbManager.COUPONS_DESCRIPTION, coupon.getDescription());
            cv.put(dbManager.COUPONS_AMOUNT,coupon.getAmount());
            cv.put(dbManager.COUPONS_PRICE,coupon.getPrice());
            cv.put(dbManager.COUPONS_IMAGE,coupon.getImage());

            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.update(dbManager.TBL_COUPONS, cv, dbManager.COUPONS_ID +"= '" + coupon.getId() + "'", null);
        }
        else
            try {
                throw new DataNotExists("Employee not exists !");
            } catch (DataNotExists e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    public void deleteCoupon(int CouponID) {
        Coupon toBeDeleted = getOneCoupon(CouponID);
        if (toBeDeleted != null) {
            coupons.remove(toBeDeleted);
            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.delete(dbManager.TBL_COUPONS, dbManager.COUPONS_ID + "= '" + CouponID + "'", null);
        } else {
            try {
                throw new DataNotExists("Employee does not exist!");
            } catch (DataNotExists e) {
                throw new RuntimeException(e);
         }
    }


    }

    @Override
    public ArrayList<Coupon> getAllCoupons() throws ParseException {
        ArrayList<Coupon> coupons1 = new ArrayList<>();
        String[] fields = {dbManager.COUPONS_ID, dbManager.KEY_COMPANY_ID_FK, dbManager.KEY_CATEGORY_ID_FK, dbManager.COUPONS_TITLE,
                dbManager.COUPONS_DESCRIPTION,dbManager.COUPONS_START_DATE,dbManager.COUPONS_END_DATE,dbManager.COUPONS_AMOUNT,dbManager.COUPONS_PRICE,dbManager.COUPONS_IMAGE};
        String  title,description,couponsImage,amount;
        Date startDate,endDate;
        String category;
        Double price;
        int couponsId, companyId,categoryId;
        int id;
        Category categoryType = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Cursor cr = dbManager.getCursor(dbManager.TBL_COMPANIES, fields, null);
            if (cr.moveToFirst())
                do {
                    couponsId = cr.getInt(0);
                    companyId = cr.getInt(1);
                    categoryId = cr.getInt(2);
                    Cursor cr2 = dbManager.getCursor(dbManager.TBL_CATEGORIES, fields, dbManager.CATEGORY_ID +"= '" + categoryId + "'");
                    category = cr.getString(3);
                    if (category.equals("FOOD"))
                        categoryType = Category.FOOD;
                    if (category.equals("ELECTRICITY"))
                        categoryType = Category.ELECTRICITY;
                    if (category.equals("RESTAURANT"))
                        categoryType =Category.RESTAURANT;
                    if (category.equals("VACATION"))
                        categoryType = Category.VACATION;
                    title = cr.getString(4);
                    description=cr.getString(5);
                    startDate= sdf.parse(cr.getString(6));
                    endDate= sdf.parse(cr.getString(7));
                    amount=cr.getString(8);
                    price=cr.getDouble(9);
                    couponsImage=cr.getString(10);


                    coupons1.add(new Coupon(couponsId, companyId, categoryType, title, description,startDate,endDate,amount,price,couponsImage));
                } while (cr.moveToNext());
            return coupons1;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public Coupon getOneCoupon(int couponID) {
        for (Coupon coupon : coupons ) {
            if(coupon.getId() == couponID)
                return coupon;
        }
        return null;

    }

    @Override
    public void addCouponPurchase(int customerID, int couponID) {

            ContentValues cv = new ContentValues();
            cv.put(dbManager.CUSTOMER_ID, customerID);
            cv.put(dbManager.COUPON_ID, couponID);

            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.insert(dbManager.TBL_CUSTOMERS_VS_COUPONS, null, cv);

    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponID) {


    }
}
