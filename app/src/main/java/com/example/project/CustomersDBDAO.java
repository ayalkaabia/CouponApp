package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class CustomersDBDAO implements CustomersDAO {
    ArrayList<Customer> customers;
    //    Context context;
    DB_Manager dbManager;


    //...Singleton.............................
    private static CustomersDBDAO instance = null;
    private static final String TAG = "CustomersDBDAO";

    public CustomersDBDAO(Context context) {
        try {
            dbManager = DB_Manager.getInstance(context);
            this.customers = getAllCustomers();
            logSystemOutMessage("CustomersDBDAO Construction success"); // i can use this function to show all messages i want in the logcat file under the tag : CustomersDBDAO
        } catch (Exception e) {
            throw e;
        }
    }

    private void logSystemOutMessage(String message) {
        // Redirect System.out to Logcat
        System.setOut(new PrintStream(new LogcatOutputStream()));

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

//
//    CustomersDBDAO(Context context) {
//        try {
//            dbManager=DB_Manager.getInstance(context);
//            this.customers = getAllCustomers();
//            System.out.println("CustomersDBDAO Construction success");
//        } catch (Exception e) {
//            throw e;
//        }
//    }

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
    public Customer getCustomer(String email, String password) {
        for(Customer customer:customers){
            if(customer.getEmail().equals(email) && customer.getPassword().equals(password)){
                return customer;
            }
        }
        return null;
    }

    @Override
    public void addCustomer(Customer customer)  {
        int affectedRows;
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
            affectedRows = (int) db.insert(dbManager.TBL_CUSTOMERS, null, cv);
            logSystemOutMessage("CustomersDBDAO addCustomer success" + affectedRows);
        }
        else {
            try {
                throw new DataExists("This customer already exists !");
            } catch (DataExists e) {
                throw new RuntimeException(e);
            }
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
                    customer1.setPassword(customer.getPassword());
                }
            }


            ContentValues cv = new ContentValues();
            cv.put(dbManager.F_NAME, customer.getfName());
            cv.put(dbManager.L_NAME, customer.getlName());
            cv.put(dbManager.EMAIL, customer.getEmail());
            cv.put(dbManager.PASSWORD, customer.getPassword());

            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.update(dbManager.TBL_CUSTOMERS, cv, dbManager.CUSTOMER_ID + "=" + customer.getId(), null);
            logSystemOutMessage("CustomersDBDAO updateCustomer success");
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
            logSystemOutMessage("CustomersDBDAO deleteCustomer success");
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
         customers = new ArrayList<>();
        String[] fields = {dbManager.CUSTOMER_ID, dbManager.F_NAME,dbManager.L_NAME,dbManager.EMAIL,dbManager.PASSWORD};
        String  fName,lName, email, password;
        int id;
        Cursor cr = null;
        try {
             cr = dbManager.getCursor(dbManager.TBL_CUSTOMERS, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getInt(0);
                    fName = cr.getString(1);
                    lName=cr.getString(2);
                    email = cr.getString(3);
                    password = cr.getString(4);
                    customers.add(new Customer(id,fName,lName,email,password));
                } while (cr.moveToNext());
            logSystemOutMessage("CustomersDBDAO getAllCustomers success");
            return customers;
        }catch(Exception e){
            throw e;
        }finally {
            if(cr!=null){
                cr.close();
            }
        }
    }

    @Override
    public Customer getOneCustomer(int customerID) {
        for (Customer customer : customers ) {
            if(customer.getId() == customerID) {
                logSystemOutMessage("CustomersDBDAO getOneCustomer success");
                return customer;
            }
        }
        return null;
    }

}
