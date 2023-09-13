package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CompaniesDBDAO implements  CompaniesDAO {
    DB_Manager dbManager;
    ArrayList<Company> companies;
    //...Singleton.............................
    private static CompaniesDBDAO instance = null;


    private CompaniesDBDAO(Context context) {
        companies=getAllCompanies();
        dbManager= DB_Manager.getInstance(context);
    }
    public CompaniesDBDAO getInstance(Context context) {
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
        }
        else
            throw new DataNotExists("Employee not exists !");
    }



    @Override
    public ArrayList<Company> getAllCompanies() {

        ArrayList<Company> companies1 = new ArrayList<>();

        String[] fields = {dbManager.COMPANY_ID, dbManager.NAME, dbManager.EMAIL, dbManager.PASSWORD};
        String  name, email, password;
        int id;
        try {
            Cursor cr = dbManager.getCursor(dbManager.TBL_COMPANIES, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getInt(0);
                    name = cr.getString(1);
                    email = cr.getString(2);
                    password = cr.getString(3);
                    companies.add(new Company(id, name, email, password, null));
                } while (cr.moveToNext());
            return companies1;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public Company getOneCompany(int companyId) {
        for (Company company : companies ) {
            if(company.getId() == companyId)
                return company;
        }
        return null;
    }
}