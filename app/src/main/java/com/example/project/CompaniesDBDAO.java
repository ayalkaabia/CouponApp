package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

public class CompaniesDBDAO implements  CompaniesDAO {
    DB_Manager dbManager;
    ArrayList<Company> companies;
    private static Context context;
    //...Singleton.............................
    private static CompaniesDBDAO instance = null;

//
//    private CompaniesDBDAO(Context context) {
//        companies=getAllCompanies();
//        dbManager= DB_Manager.getInstance(context);
//        System.out.println("companiesDBDAO construction was a success");
//    }
    private static final String TAG = "CompaniesDBDAO";

    public CompaniesDBDAO(Context context) throws ParseException {
        try {
            dbManager= DB_Manager.getInstance(context);
            companies=getAllCompanies();

            logSystemOutMessage("CompaniesDBDAO Construction success");
        } catch (Exception e) {
            throw e;
        }
    }

    private void logSystemOutMessage(String message) {
        // Redirect System.out to Logcat
        System.setOut(new PrintStream(new CompaniesDBDAO.LogcatOutputStream()));

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

    public static CompaniesDBDAO getInstance(Context context) throws ParseException {
        if (instance == null) instance = new CompaniesDBDAO(context);
        return instance;
    }
    //...Singleton.............................


    @Override
    public boolean isCompanyExists(String email, String password) {
        for (Company company : companies ) {
            if(company.getEmail().equals(email) && company.getPassword().equals(password))
                return true;
        }
        return false;
    }

    public Company isCompanyExists2(String email, String password) {
        for (Company company : companies ) {
            if(company.getEmail().equals(email) && company.getPassword().equals(password))
                return company;
        }
        return null;
    }


    @Override
    public void addCompany(Company company) throws DataExists {

            if (!isCompanyExists(company.getEmail(),company.getPassword())) {
                companies.add(company);

                ContentValues cv = new ContentValues();
                cv.put("name", company.getName());
                cv.put("email", company.getEmail());
                cv.put("password", company.getPassword());


                SQLiteDatabase db = dbManager.getWritableDatabase();
                db.insert("companies", null, cv);
                logSystemOutMessage("companiesDBDAO addCompany success");
            }
            else
                throw new DataExists("This employee already exists !");
    }


    @Override
    public void updateCompany(Company company) throws DataNotExists {
        if (isCompanyExists(company.getEmail(),company.getPassword())) {
            for(Company company1 : companies)
            {
                if(company1.getId() == company.getId())
                {
              //      company1.setName(company.getName());
                    company1.setEmail(company.getEmail());
                //    company1.setPassword(company.getPassword());
                }
            }

            ContentValues cv = new ContentValues();
           // cv.put("name", company.getName());
            cv.put("email", company.getEmail());
          //  cv.put("password", company.getPassword());


            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.update("companies", cv,  "company_id" + "='" + company.getId() + "'", null);
            logSystemOutMessage("companiesDBDAO updateCompany success");
        }
        else
            throw new DataNotExists("Employee not exists !");

    }

    @Override
    public void deleteCompany(int companyId) throws DataNotExists {
        Company toBeDeleted = getOneCompany(companyId);
        if (toBeDeleted != null) {
            companies.remove(toBeDeleted);
            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.delete("companies", "company_id" + "='" + companyId + "'", null);
            logSystemOutMessage("companiesDBDAO deleteCompany success");
        }
        else
            throw new DataNotExists("Employee not exists !");
    }



    @Override
    public ArrayList<Company> getAllCompanies() throws ParseException {
        companies = new ArrayList<>();
        String[] fields = {dbManager.COMPANY_ID, dbManager.NAME, dbManager.EMAIL, dbManager.PASSWORD};
        String name, email, password;
        int id;
        Cursor cr = null;
        ArrayList<Coupon> allCoupons=null,companyCoupons=null;
        CouponsDBDAO couponsDBDAO=CouponsDBDAO.getInstance(context);
        try {
             cr = dbManager.getCursor(dbManager.TBL_COMPANIES, fields, null);
            if (cr.moveToFirst()) {
                do {
                    id = cr.getInt(0);
                    name = cr.getString(1);
                    email = cr.getString(2);
                    password = cr.getString(3);
                    // Adjust the last argument as needed based on the Company class constructor
                    allCoupons=couponsDBDAO.getAllCoupons(); //contains all coupons
                    for(Coupon coupon:allCoupons){
                        if(coupon.getCompanyID()==id){
                            companyCoupons.add(coupon);
                        }
                    }
                    companies.add(new Company(id, name, email, password, companyCoupons));
                } while (cr.moveToNext());
            }
            logSystemOutMessage("companiesDBDAO getAllCompanies success");
            return companies;
        } catch (Exception e) {
            // Handle the exception or log it with an appropriate message
            e.printStackTrace(); // Example: Print the stack trace for debugging
            // You may choose to return an empty list or handle the error differently
            logSystemOutMessage("companiesDBDAO getAllCompanies failure");
            return new ArrayList<>();

        }
        finally {
            // Close the cursor to release resources
            if (cr != null && !cr.isClosed()) {
                cr.close();
            }
            
        }
    }

    @Override
    public Company getOneCompany(int companyId) {
        for (Company company : companies ) {
            if(company.getId() == companyId){
                logSystemOutMessage("companiesDBDAO getOneCompany success");
                return company;
            }

        }
        return null;
    }


}