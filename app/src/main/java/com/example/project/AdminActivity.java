package com.example.project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {


    Button btnBack,addCompany,updateCompany,deleteCompany,addCustomer,updateCustomer,deleteCustomer,getCompany,getCustomers,getCustomer;
    DeleteCustomerFragment DelCustomerFragment = new DeleteCustomerFragment();
    GetCustomersFrag getcustomersfrag= new GetCustomersFrag();
    DeleteCompanyFragment DeleteCompanyfragment=new DeleteCompanyFragment();
    Fragment currentFragment = null;
    ConstraintLayout fragContainer;
    // create adapter


    DB_Manager db=DB_Manager.getInstance(this);

    AdminFacade adminFacade;

    public AdminActivity() throws SQLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        addCompany=findViewById(R.id.admin_btnAddCompany);
        updateCompany=findViewById(R.id.admin_btnUpdateCompany);
        deleteCompany=findViewById(R.id.admin_btnDeleteCompany);
        addCustomer=findViewById(R.id.admin_btnAddCustomer);
        updateCustomer=findViewById(R.id.admin_btnUpdateCustomer);
        deleteCustomer=findViewById(R.id.admin_btnDeleteCustomer);
        btnBack=findViewById(R.id.admin_btnBack);
        getCompany=findViewById(R.id.admin_btnGetCompany);
        fragContainer = findViewById(R.id.fragmentContainerAdmin);
        getCustomers=findViewById(R.id.admin_btnGetCustomers);
        getCustomer = findViewById(R.id.admin_btnGetCustomer);



        ButtonsListener buttonsListener = new ButtonsListener();
        addCustomer.setOnClickListener(buttonsListener);
        addCompany.setOnClickListener(buttonsListener);
        deleteCustomer.setOnClickListener(buttonsListener);
        deleteCompany.setOnClickListener(buttonsListener);
        updateCustomer.setOnClickListener(buttonsListener);
        updateCompany.setOnClickListener(buttonsListener);
        btnBack.setOnClickListener(buttonsListener);
        getCompany.setOnClickListener(buttonsListener);
        getCustomers.setOnClickListener(buttonsListener);
        getCustomer.setOnClickListener(buttonsListener);
        //we should get the admin facade instance from the intent or maybe we dont need the intent at all??
        try {
            adminFacade=AdminFacade.getInstance(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if(intent != null) {
                        int requestCode = intent.getIntExtra("requestCode", 0);
//                        if (requestCode == 3) {   //update customer
//                            if (result.getResultCode() == RESULT_OK) {
//                                Customer e = (Customer) intent.getSerializableExtra("Customer");
//                                if (e != null) {
//                                    try {
//                                        db.updateCustomer(e);
//                                    } catch (Exception ex) {
//                                        Toast.makeText(AdminActivity.this, "Employee : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }
//                        if (requestCode == 4) {   //update company
//                            if (result.getResultCode() == RESULT_OK) {
//                                Company e = (Company) intent.getSerializableExtra("Company");
//                                if (e != null) {
//                                    try {
//                                        db.UpdateCompany(e);
//                                    } catch (Exception ex) {
//                                        Toast.makeText(AdminActivity.this, "Employee : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }
//                        if (requestCode == 5) {   //delete company
//                            if (result.getResultCode() == RESULT_OK) {
//                                Company e = (Company) intent.getSerializableExtra("Company");
//                                if (e != null) {
//                                    try {
//                                        db.DeleteCompany(e);
//                                    } catch (Exception ex) {
//                                        Toast.makeText(AdminActivity.this, "Employee : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }
//                        if (requestCode == 6) {   //delete customer
//                            if (result.getResultCode() == RESULT_OK) {
//                                Company e = (Company) intent.getSerializableExtra("Company");
//                                if (e != null) {
//                                    try {
//                                        db.DeleteCustomer(e);
//                                    } catch (Exception ex) {
//                                        Toast.makeText(AdminActivity.this, "Employee : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }
//

                    }
                }
            }
    );


    class ButtonsListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            if(view.getId() == addCompany.getId())
            {
                Intent intent = new Intent(AdminActivity.this, AddNewCompanyActivity.class);
                startActivityForResult(intent,1);
            }

            else if(view.getId()==getCompany.getId()) {
                Intent intent = new Intent(AdminActivity.this, GetCompanyActivity.class);
                startActivity(intent);
            }
            else if(view.getId()==getCustomers.getId())
            {
                getcustomersfrag.setContext(AdminActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerAdmin, getcustomersfrag).commit();
                currentFragment = getcustomersfrag;
            }
            else if(view.getId()==getCustomer.getId())
            {
                Intent intent = new Intent(AdminActivity.this, getCustomer.class);
                startActivity(intent);
            }
            else if(view.getId() == addCustomer.getId())
            {
                  Intent intent = new Intent(AdminActivity.this, AddNewCustomer.class);
                  startActivityForResult(intent,2);
            }
            else if(view.getId() == updateCustomer.getId())
            {
                Intent intent = new Intent(AdminActivity.this, UpdateCustomer.class);
                startActivity(intent);
            }
            else if(view.getId() == updateCompany.getId())
            {
                Intent intent = new Intent(AdminActivity.this, UpdateCompany.class);
                startActivity(intent);
//                ArrayList<Coupon >abc = new ArrayList<>();

            }
            else if(view.getId() == deleteCompany.getId())
            {
                DeleteCompanyfragment.setContext(AdminActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerAdmin, DeleteCompanyfragment).commit();
                currentFragment = DeleteCompanyfragment;
            }
            else if(view.getId() == deleteCustomer.getId())
            {
                DelCustomerFragment.setContext(AdminActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerAdmin, DelCustomerFragment).commit();
                currentFragment = DelCustomerFragment;
            }
            if(view.getId() == btnBack.getId()){
                finish();
            }


        }
    }
//
//    private void startActivityDeleteCustomer() {
//        Intent intent = new Intent(AdminActivity.this, DeleteCustomerActivity.class);
//        intent.putExtra("requestCode", 6);
//        launcher.launch(intent);
//    }
//
//    private void startActivityDeleteCompany() {
//        Intent intent = new Intent(AdminActivity.this, DeleteCompanyActivity.class);
//        intent.putExtra("requestCode", 5);
//        launcher.launch(intent);
//    }
//
//    private void startActivityEditCompany() {
//        Intent intent = new Intent(AdminActivity.this, UpdateCompanyActivity.class);
//        intent.putExtra("requestCode", 4);
//        launcher.launch(intent);
//    }
//
//    private void startActivityEditCustomer() {
//        Intent intent = new Intent(AdminActivity.this, UpdateCustomerActivity.class);
//        intent.putExtra("requestCode", 3);
//        launcher.launch(intent);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1) {//returned from addNewcompany
            if (data != null) {//there is a company returned
                if (resultCode == RESULT_OK) {
                    Company c = (Company) data.getSerializableExtra("newCompany");//should the name here be the same name as the one in addNewCompany activity?
                    if (c == null)
                        Toast.makeText(AdminActivity.this, "No company returned", Toast.LENGTH_SHORT).show();
                    try {
                        adminFacade.addCompany(c);
                    } catch (DataExists e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


//        }else if(requestCode==2){//returned from addNewCustomer
//            if(data!=null){//there is a customer returned
//                if(resultCode==RESULT_OK){
//                    Customer c=(Customer)data.getSerializableExtra("customer");
//                    if(c==null) Toast.makeText(AdminActivity.this,"No Customer returned",Toast.LENGTH_SHORT).show();
//                    db.addNewCustomer(c);
//                }
//            }


        super.onActivityResult(requestCode, resultCode, data);
    }
}