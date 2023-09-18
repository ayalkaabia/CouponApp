package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentCompanyDetails extends Fragment {

    private TextView companyNameTextView;
    private TextView companyDetailsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_details, container, false);

        companyNameTextView = view.findViewById(R.id.textViewCompanyName);
        companyDetailsTextView = view.findViewById(R.id.textViewCompanyDetails);

        return view;
    }

    // Method to update the company details in the fragment
    public void setCompany(Company company) {
        if (company != null) {
            companyNameTextView.setText("Company Name: " + company.getName());
            companyDetailsTextView.setText("Company Details: " + company.toString());
        }
    }


}
