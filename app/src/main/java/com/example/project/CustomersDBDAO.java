package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

public class CustomersDBDAO implements CustomersDAO {
    ArrayList<Customer> customers;
    //    Context context;
    DB_Manager dbManager;


    //...Singleton.............................
    private static CustomersDBDAO instance = null;

    CustomersDBDAO(Context context) {

        try {

            dbManager=DB_Manager.getInstance(context);
            this.customers = getAllCustomers();
        } catch (Exception e) {
            throw e;
        }
    }

    public static CustomersDBDAO getInstance(Context context) {
        if (instance == null) instance = new CustomersDBDAO(context);
        return instance;
    }
    //...Singleton.............................



    @Override
    public Boolean isCustomerExists(String email, String password) {
        for(Customer customer:customers){
            if(customer.getEmail().equals(email) && customer.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addCustomer(Customer customer)  {
        Customer newCustomer = getOneCustomer(customer.getId());
        if (newCustomer==null) {
            customers.add(customer);
            ContentValues cv = new ContentValues();
            cv.put(dbManager.CUSTOMER_ID, customer.getId());
            cv.put(dbManager.F_NAME, customer.getfName());
            cv.put(dbManager.L_NAME, customer.getlName());
            cv.put(dbManager.EMAIL, customer.getEmail());
            cv.put(dbManager.PASSWORD, customer.getPassword());

            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.insert(dbManager.TBL_CUSTOMERS, null, cv);
        }
        else
            try {
                throw new DataExists("This customer already exists !");
            } catch (DataExists e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    public void updateCustomer(Customer customer) {
        Customer updatedCustomer = getOneCustomer(customer.getId());
        if (updatedCustomer!=null) {

            for(Customer customer1:customers){
                if(customer1.getId()==customer.getId()){
                    customer1.setfName(customer.getfName());
                    customer1.setlName(customer.getlName());
                    customer1.setEmail(customer.getEmail());
                  //  customer1.setPassword(customer.getPassword());
                }
            }


            ContentValues cv = new ContentValues();
            cv.put(dbManager.F_NAME, customer.getfName());
            cv.put(dbManager.L_NAME, customer.getlName());
            cv.put(dbManager.EMAIL, customer.getEmail());
          //  cv.put(dbManager.PASSWORD, customer.getPassword());

            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.update(dbManager.TBL_CUSTOMERS, cv, dbManager.CUSTOMER_ID + "=" + customer.getId(), null);
        }
        else
            try {
                throw new DataNotExists("Employee not exists !");
            } catch (DataNotExists e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    public void deleteCustomer(int customerID) {
        Customer toBeDeleted = getOneCustomer(customerID);
        if(toBeDeleted!=null){
            customers.remove(toBeDeleted);
            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.delete(dbManager.TBL_CUSTOMERS, dbManager.CUSTOMER_ID + "=" + customerID, null);
        } else {
            try {
                throw new DataNotExists("Employee does not exist!");
            } catch (DataNotExists e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers1 = new ArrayList<>();

        String[] fields = {"id", "fName","lName","email", "password"};
        String  fName,lName, email, password;
        int id;
        double salary;
        try {
            Cursor cr = dbManager.getCursor(dbManager.TBL_CUSTOMERS, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getInt(0);
                    fName = cr.getString(1);
                    lName=cr.getString(2);
                    email = cr.getString(3);
                    password = cr.getString(4);
                    customers1.add(new Customer(id,fName,lName,email,password));
                } while (cr.moveToNext());
            return customers1;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public Customer getOneCustomer(int customerID) {
        for (Customer customer : customers ) {
            if(customer.getId() == customerID) {
                return customer;
            }
        }
        return null;
    }

}
