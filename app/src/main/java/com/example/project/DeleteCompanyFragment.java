package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteCompanyFragment extends Fragment {


    Button  delete;
    TextView idTf;
    private Context context;
    CompaniesDBDAO company=CompaniesDBDAO.getInstance(context);

    public DeleteCompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_delete_company, container, false);
        delete = fragView.findViewById(R.id.Company_delete_button);
        ButtonsListener bts=new ButtonsListener();
        delete.setOnClickListener(bts);
        idTf=fragView.findViewById(R.id.company_id_delete_tf);


        return fragView;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    class ButtonsListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == delete.getId()) {
                int id=  Integer.valueOf(idTf.getText().toString()) ;
                System.out.print(id);
                idTf.setText("");
                try {
                    company.deleteCompany(id);
                } catch (DataNotExists e) {
                    Toast.makeText(context, "Something went wrong ", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}