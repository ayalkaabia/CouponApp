package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class DeleteCustomerFragment extends Fragment {

    DB_Manager db = DB_Manager.getInstance(getContext());

    Button  delete;
    TextView id_tf;
    private Context context;
    AdminFacade ad_fe=AdminFacade.getInstance(context);

    public DeleteCustomerFragment() throws SQLException {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_delete_customer, container, false);
        delete = fragView.findViewById(R.id.confirm_delete_button);
        id_tf=fragView.findViewById(R.id.deleteCustomerTf);


        return fragView;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    class ButtonsListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == delete.getId()) {
                try {
                    int id = Integer.valueOf(id_tf.getText().toString());
                    try {
                        ad_fe.customersDAO.deleteCustomer(id);
                        
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context,"something went wrong ", Toast.LENGTH_SHORT).show() ;// in Activity

                    }
                  id_tf.setText("");
                }
                catch (Exception e)
                {
                    throw e;
                }
            }
        }
    }
}