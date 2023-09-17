package com.example.project;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.sql.SQLException;

public class GetCompanyActivity extends AppCompatActivity {

    private EditText companyIdEditText;
    private Button displayDetailsButton,Cancel;
    private AdminFacade ad_fe=AdminFacade.getInstance(GetCompanyActivity.this);

    public GetCompanyActivity() throws SQLException {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_company);

        companyIdEditText = findViewById(R.id.editTextCompanyId);
        displayDetailsButton = findViewById(R.id.buttonDisplayDetails);
        Cancel=findViewById(R.id.DisplaycompanyCancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        displayDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyId = companyIdEditText.getText().toString();
                // Use AdminFacade to fetch company details by ID
                Company company = null;
                company = ad_fe.companiesDAO.getOneCompany(Integer.parseInt(companyId));
                if (company != null) {
                    displayCompanyDetailsFragment(company);
                } else {
                    // Handle case where company is not found
                }
            }
        });
    }

    private void displayCompanyDetailsFragment(Company company) {
        CompanyDetailsFragment fragment = new CompanyDetailsFragment();
        fragment.setCompany(company);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null); // Allow returning to the previous fragment
        transaction.commit();
    }


}
