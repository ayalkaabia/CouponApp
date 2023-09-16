package com.example.project;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CouponsDBDAO implements CouponsDAO {
    private ArrayList<Coupon> coupons;
    private static CouponsDBDAO instance = null;
    DB_Manager dbManager;
    private static Context context;
   // private CustomersDAO customersDAO;


    //...Singleton.............................
//    private CouponsDBDAO(Context context) throws ParseException {
//        coupons=getAllCoupons();
//        dbManager = DB_Manager.getInstance(context);
//       // coupons = new ArrayList<Coupon>();
//        customersDAO = new CustomersDBDAO(context);
//        System.out.println("couponsDBDAO construction was a success");
//    }

    private static final String TAG = "CouponsDBDAO";

    public CouponsDBDAO(Context context) throws ParseException {
        try {
            dbManager = DB_Manager.getInstance(context);
          //  customersDAO = CustomersDBDAO.getInstance(context);
            coupons=getAllCoupons();
            // coupons = new ArrayList<Coupon>();

            this.context=context;
            logSystemOutMessage("CouponsDBDAO Construction success");
        } catch (Exception e) {
            throw e;
        }
    }

    private void logSystemOutMessage(String message) {
        // Redirect System.out to Logcat
        System.setOut(new PrintStream(new CouponsDBDAO.LogcatOutputStream()));

        // Print the message
        System.out.println(message);

        // Restore the standard output
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // This is needed to prevent further System.out usage from crashing
            }
        }));
    }

    private static class LogcatOutputStream extends OutputStream {
        private StringBuilder buffer = new StringBuilder();

        @Override
        public void write(int b) throws IOException {
            if (b == '\n') {
                Log.d(TAG, buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }
    }





    public static CouponsDBDAO getInstance(Context context) throws SQLException, ParseException {
        if (instance == null) instance = new CouponsDBDAO(context);
        return instance;
    }

    //...Singleton.............................
    @Override
    public void addCoupon(Coupon coupon) {
        int insertRow;
        Coupon newCoupon = getOneCoupon(coupon.getId());
        String[] fields = {dbManager.CATEGORY_ID, dbManager.CATEGORY_NAME};
        if (newCoupon == null) {
            ContentValues cv = new ContentValues();
            cv.put(dbManager.COUPON_ID, coupon.getId());
            cv.put(dbManager.KEY_COMPANY_ID_FK, coupon.getCompanyID());

            int category_id=getCategoryID(coupon.getCategory());//getting category id by the category type using function we built

            cv.put(dbManager.KEY_CATEGORY_ID_FK, category_id);
            cv.put(dbManager.COUPONS_TITLE, coupon.getTitle());
            cv.put(dbManager.COUPONS_DESCRIPTION, coupon.getDescription());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = coupon.getStartDate();
            String formattedStartDate = sdf.format(startDate);

            Date endDate = coupon.getEndDate();
            String formattedEndDate = sdf.format(endDate);

            cv.put(dbManager.COUPONS_START_DATE, formattedStartDate);
            cv.put(dbManager.COUPONS_END_DATE, formattedEndDate);
            cv.put(dbManager.COUPONS_AMOUNT, coupon.getAmount());
            cv.put(dbManager.COUPONS_PRICE, coupon.getPrice());
            cv.put(dbManager.COUPONS_IMAGE, coupon.getImage());
            try {

                insertRow= (int) dbManager.getWritableDatabase().insert(dbManager.TBL_COUPONS, null, cv);
                logSystemOutMessage("CouponsDBDAO addCoupon success  " + insertRow);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else
            try {
                throw new DataExists("This coupon already exists !");
            } catch (DataExists e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public void updateCoupon(Coupon coupon) {
        int updatedRow;
        Coupon updatedCoupon = getOneCoupon(coupon.getId());
        if (updatedCoupon != null) {

            for (Coupon coupon1 : coupons) {
                if (coupon1.getId() == coupon.getId()) {         //updating the coupon in the array list
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

            ContentValues cv = new ContentValues();

            int category_id=getCategoryID(coupon.getCategory());

            cv.put(dbManager.CATEGORY_ID, category_id);

            //preparing the start and end date from Dates to String
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = coupon.getStartDate();
            String formattedStartDate = sdf.format(startDate);
            Date endDate = coupon.getEndDate();
            String formattedEndDate = sdf.format(endDate);

            cv.put(dbManager.COUPONS_START_DATE, formattedStartDate);
            cv.put(dbManager.COUPONS_END_DATE, formattedEndDate);

            //preparing the rest of the cv
            cv.put(dbManager.COMPANY_ID, coupon.getCompanyID());
            cv.put(dbManager.COUPONS_TITLE, coupon.getTitle());
            cv.put(dbManager.COUPONS_DESCRIPTION, coupon.getDescription());
            cv.put(dbManager.COUPONS_AMOUNT, coupon.getAmount());
            cv.put(dbManager.COUPONS_PRICE, coupon.getPrice());
            cv.put(dbManager.COUPONS_IMAGE, coupon.getImage());

            SQLiteDatabase db = dbManager.getWritableDatabase();
            try {
                updatedRow= db.update(dbManager.TBL_COUPONS, cv,  " WHERE " + dbManager.COUPON_ID + "= '" + coupon.getId() + "'", null);
                logSystemOutMessage("CouponsDBDAO updateCoupon success     "+ updatedRow);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else
            try {
                throw new DataNotExists("coupon not exists !");
            } catch (DataNotExists e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    public void deleteCoupon(int CouponID) {
        int deletedRow;
        Coupon toBeDeleted = getOneCoupon(CouponID);
        if (toBeDeleted != null) {
            coupons.remove(toBeDeleted);
            SQLiteDatabase db = dbManager.getWritableDatabase();
            try {
                deletedRow= db.delete(dbManager.TBL_COUPONS,   dbManager.COUPON_ID + "= '" + CouponID + "'", null);
                logSystemOutMessage("CouponsDBDAO deleteCoupon success     " + deletedRow);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else {
            try {
                throw new DataNotExists("Coupon does not exist!");
            } catch (DataNotExists e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public ArrayList<Coupon> getAllCoupons() throws ParseException {
        coupons = new ArrayList<>();
        String[] fields = {dbManager.COUPON_ID, dbManager.KEY_COMPANY_ID_FK, dbManager.KEY_CATEGORY_ID_FK, dbManager.COUPONS_TITLE,
                dbManager.COUPONS_DESCRIPTION, dbManager.COUPONS_START_DATE, dbManager.COUPONS_END_DATE, dbManager.COUPONS_AMOUNT, dbManager.COUPONS_PRICE, dbManager.COUPONS_IMAGE};
        String title, description, couponsImage;
        int amount;
        Date startDate, endDate;
        String category;
        Double price;
        int couponsId, companyId, categoryId;
        Category categoryType = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] fields1 ={dbManager.CATEGORY_ID,dbManager.CATEGORY_NAME};
        Cursor cr=null,cr2=null;
        try {
            cr = dbManager.getCursor(dbManager.TBL_COUPONS, fields, null);
            if (cr!=null && cr.moveToPosition(0))
                do {
                    couponsId = cr.getInt(0);
                    companyId = cr.getInt(1);
                    categoryId = cr.getInt(2);
                    //to find categoryType from categoryID
                    cr2 = dbManager.getCursor(dbManager.TBL_CATEGORIES, fields1,  " WHERE " + dbManager.CATEGORY_ID + "= '" + categoryId + "'");
                    cr2.moveToPosition(0);
                    category = cr2.getString(1);  // we should be wary that cr2 is a new cursor ..
                    if (category.equals("FOOD"))
                        categoryType = Category.FOOD;
                    if (category.equals("ELECTRICITY"))
                        categoryType = Category.ELECTRICITY;
                    if (category.equals("RESTAURANT"))
                        categoryType = Category.RESTAURANT;
                    if (category.equals("VACATION"))
                        categoryType = Category.VACATION;

                    title = cr.getString(3);
                    description = cr.getString(4);
                    startDate = sdf.parse(cr.getString(5));
                    endDate = sdf.parse(cr.getString(6));
                    amount = cr.getInt(7);
                    price = cr.getDouble(8);
                    couponsImage = cr.getString(9);

                    coupons.add(new Coupon(couponsId, companyId, categoryType, title, description, startDate, endDate, amount, price, couponsImage));
                } while (cr.moveToNext());
            logSystemOutMessage("CouponsDBDAO getAllCoupons success");
            return coupons;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }




    @Override
    public Coupon getOneCoupon(int couponID) {
        for (Coupon coupon : coupons) {
            if (coupon.getId() == couponID){
                logSystemOutMessage("CouponsDBDAO getOneCoupon success");
                return coupon;
            }
        }
        return null;

    }

    @Override
    public void addCouponPurchase(int customerID, int couponID) {
        ArrayList<Coupon> customerCoupons;
        ContentValues cv = new ContentValues();
        cv.put(dbManager.CUSTOMER_ID, customerID);
        cv.put(dbManager.COUPON_ID, couponID);

        SQLiteDatabase db = dbManager.getWritableDatabase();
        try {
            db.insert(dbManager.TBL_CUSTOMERS_VS_COUPONS, null, cv);
            logSystemOutMessage("CouponsDBDAO addCouponPurchase success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//      //  customerCoupons = customersDAO.getOneCustomer(customerID).getCoupons();
//        for (Coupon coupon : coupons)
//            if (coupon.getId() == couponID) { //ADDING THE COUPON TO THE CUSTOMER COUPONS???
//                customerCoupons.add(coupon);
//                coupon.setAmount(coupon.getAmount() - 1);
//
//            }

    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponID) {

        int deletedRow;
        ArrayList<Coupon> customerCoupons = null;
        SQLiteDatabase db = dbManager.getWritableDatabase();
        String whereClause = dbManager.COUPON_ID + " = ? AND " + dbManager.CUSTOMER_ID + " = ?";

        // Define the values for the placeholders in the WHERE clause
        String[] whereArgs = { String.valueOf(couponID), String.valueOf(customerID) };

        // Perform the delete operation with the updated WHERE clause
        try {
            deletedRow= db.delete(dbManager.TBL_CUSTOMERS_VS_COUPONS, whereClause, whereArgs);
            logSystemOutMessage("CouponsDBDAO deleteCouponPurchase success      " + deletedRow);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    public void deleteCouponsPurchaseByCouponID(int couponID) {
        int customerId;
        String[] fields = {dbManager.CUSTOMER_ID, dbManager.COUPON_ID};
        Cursor cr =null;
        try {
            cr = dbManager.getCursor(dbManager.TBL_CUSTOMERS_VS_COUPONS, fields, " WHERE " + dbManager.COUPON_ID + "= '" + couponID + "'");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (cr.moveToFirst())
            do {
                customerId = cr.getInt(0);
                deleteCouponPurchase(customerId, couponID);

            } while (cr.moveToNext());
        logSystemOutMessage("CouponsDBDAO deleteCouponPurchase by couponID success");

    }

    public void deleteCouponsPurchaseByCustomerID(int customerId) {
        int couponID;
        String[] fields = {dbManager.CUSTOMER_ID, dbManager.COUPON_ID};
        Cursor cr=null;
        try {
            cr = dbManager.getCursor(dbManager.TBL_CUSTOMERS_VS_COUPONS, fields,   dbManager.CUSTOMER_ID + "= '" + customerId + "'");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (cr.moveToFirst())
            do {
                couponID = cr.getInt(1);
                deleteCouponPurchase(customerId, couponID);

            } while (cr.moveToNext());
        logSystemOutMessage("CouponsDBDAO deleteCouponPurchase by customerID success");

    }
    public ArrayList<Coupon> getCouponsPurchaseByCustomerID(int customerId) {
        int couponID;
        ArrayList<Coupon> customerCoupons= new ArrayList<>();
        Coupon coupon;
        String[] fields = {dbManager.CUSTOMER_ID, dbManager.COUPON_ID};
        Cursor cr=null;
        try {
            cr = dbManager.getCursor(dbManager.TBL_CUSTOMERS_VS_COUPONS, fields,   " WHERE " + dbManager.CUSTOMER_ID + "= '" + customerId + "'");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (cr.moveToFirst())
            do {
                couponID = cr.getInt(1);
                coupon=getOneCoupon(couponID);
                customerCoupons.add(coupon);

            } while (cr.moveToNext());


        return customerCoupons;
    }

    int getCategoryID(Category category){
        //Preparing the categoryID by its type
        String[] fields = {dbManager.CATEGORY_ID,dbManager.CATEGORY_NAME}; // we only need the category id
        String categoryType = null;
        if (category == Category.FOOD)
            categoryType = "FOOD";
        if (category == Category.ELECTRICITY)
            categoryType = "ELECTRICITY";
        if (category == Category.RESTAURANT)
            categoryType = "RESTAURANT";
        if (category == Category.VACATION)
            categoryType = "VACATION";
        Cursor cr=null;
        try {
            cr = dbManager.getCursor(dbManager.TBL_CATEGORIES, fields,  " WHERE " + dbManager.CATEGORY_NAME + "='" + categoryType + "'");
           logSystemOutMessage(String.valueOf(cr.getCount())+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int category_id=-1;
        cr.moveToFirst();
        category_id = cr.getInt(0);
        return category_id;
    }

}
