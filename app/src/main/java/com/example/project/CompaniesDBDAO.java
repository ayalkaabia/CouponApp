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
            this.context=context;
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

    static class LogcatOutputStream extends OutputStream {
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

            if (getOneCompany(company.getId())==null) {
                companies.add(company);
                int newRow;

                ContentValues cv = new ContentValues();
                cv.put(dbManager.NAME, company.getName());
                cv.put(dbManager.EMAIL, company.getEmail());
                cv.put(dbManager.PASSWORD, company.getPassword());

            try {
                  SQLiteDatabase db = dbManager.getWritableDatabase();
                  newRow= (int) db.insert(dbManager.TBL_COMPANIES, null, cv);
                  logSystemOutMessage("companiesDBDAO addCompany success    "+ newRow);
                } catch (Exception e) {
                logSystemOutMessage("companiesDBDAO addCompany failure");
                throw new RuntimeException(e);
            }
            }
            else
                throw new DataExists("This Company already exists !");
    }


    @Override
    public void updateCompany(Company company) throws DataNotExists {
        int rowsUpdated;
        if (getOneCompany(company.getId())!=null) {
            for(Company company1 : companies)
            {
                if(company1.getId() == company.getId())
                {
                    company1.setEmail(company.getEmail());
                    company1.setPassword(company.getPassword());
                }
            }

            ContentValues cv = new ContentValues();
            cv.put(dbManager.EMAIL, company.getEmail());
            cv.put(dbManager.PASSWORD, company.getPassword());

            try {
                    SQLiteDatabase db = dbManager.getWritableDatabase();
                    rowsUpdated=db.update(dbManager.TBL_COMPANIES, cv, dbManager.COMPANY_ID + "='" + company.getId() + "'", null);
                    logSystemOutMessage("companiesDBDAO updateCompany success      " + rowsUpdated);
                } catch (Exception e) {
                      throw new RuntimeException(e);
                }

        }
        else
            throw new DataNotExists("Company not exists !");

    }

    @Override
    public void deleteCompany(int companyId) throws DataNotExists {
        Company toBeDeleted = getOneCompany(companyId);
        int deletedRows;
        if (toBeDeleted != null) {
            companies.remove(toBeDeleted);
            try {
                SQLiteDatabase db = dbManager.getWritableDatabase();
                deletedRows=db.delete(dbManager.TBL_COMPANIES,  dbManager.COMPANY_ID + "='" + companyId + "'", null);
                logSystemOutMessage("companiesDBDAO deleteCompany success      " + deletedRows);
            } catch (Exception e) {
                logSystemOutMessage("companiesDBDAO deleteCompany FAILURE");
                throw new RuntimeException(e);
            }
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
        ArrayList<Coupon> allCoupons,companyCoupons;
        companyCoupons= new ArrayList<>();
        CouponsDBDAO couponsDBDAO=CouponsDBDAO.getInstance(context);
        try {
             cr = dbManager.getCursor(dbManager.TBL_COMPANIES, fields, null);
            if (cr!=null && cr.moveToFirst()) {
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
//        finally {
//            // Close the cursor to release resources
//            if (cr != null && !cr.isClosed()) {
//                cr.close();
//            }
//
//        }
    }

    @Override
    public Company getOneCompany(int companyId) {
        if(companies!=null) {
            for (Company company : companies) {
                if (company.getId() == companyId) {
                    logSystemOutMessage("companiesDBDAO getOneCompany success");
                    return company;
                }
            }
        }
        return null;
    }


}