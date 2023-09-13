package com.example.project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    RadioGroup rgUserType;
    EditText etUN, etPass;
    ClientType userType = null;
    RadioButton rbAdmin, rbCustomer, rbCompany;
    DB_Manager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DB_Manager.getInstance(this);

        rgUserType = findViewById(R.id.main_rgUserType);
        etUN = findViewById(R.id.main_etUN);
        etPass = findViewById(R.id.main_etPass);
        btnLogin = findViewById(R.id.main_btnLogin);
        rbAdmin = findViewById(R.id.main_rbAdmin);
        rbCustomer = findViewById(R.id.main_rbCustomer);
        rbCompany = findViewById(R.id.main_rbCompany);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = etUN.getText().toString();
                String pass = etPass.getText().toString();

                Object o = null;
                try {
                    o = LoginManager.getInstance(MainActivity.this).login(un, pass, userType);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(o == null) {
                    Toast.makeText(MainActivity.this, "Error User Name or Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(o instanceof AdminFacade)
                    OpenAdminActivity((AdminFacade)o);
                if(o instanceof CustomersFacade)
                    OpenCustomerActivity((CustomersFacade)o);
                if(o instanceof CompanyFacade)
                    OpenCompanyActivity((CompanyFacade)o);
            }
        });

        rgUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == rbAdmin.getId())
                    userType = ClientType.ADMINISTRATOR;
                if(checkedId == rbCustomer.getId())
                    userType = ClientType.CUSTOMER;
                if(checkedId == rbCompany.getId())
                    userType = ClientType.COMPANY;
            }
        });
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    Intent intent = result.getData();
//                    if (result.getResultCode() == RESULT_OK) {
//                        Company e = (Company) intent.getSerializableExtra("company");
//                        if (e != null) {
//                            db.addNewCompany(e);
//                        }
//                    }

                }
            }
    );
    private void OpenAdminActivity(AdminFacade adminFacade) {
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        intent.putExtra("admin",adminFacade );
        startActivity(intent);
    }
    private void OpenCustomerActivity(CustomersFacade customersFacade) {
        Intent intent = new Intent(MainActivity.this, CustomerActivity.class);
        intent.putExtra("customer", customersFacade);
        startActivity(intent);
    }
    private void OpenCompanyActivity(CompanyFacade companyFacade) {
        Intent intent = new Intent(MainActivity.this, CompanyActivity.class);
        intent.putExtra("company", companyFacade);
        startActivity(intent);
    }
}