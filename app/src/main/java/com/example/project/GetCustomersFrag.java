package com.example.project;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class GetCustomersFrag extends Fragment {

    private Context context;
    private ArrayList<Customer> customerlist;
    private RecyclerView recyclerView;
    private AdminFacade ad_fe=AdminFacade.getInstance(context);


    public GetCustomersFrag() throws SQLException {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_get_customers, container, false);
        customerlist=new ArrayList<>();
        try {
            customerlist=ad_fe.customersDAO.getAllCustomers();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        return myview;
    }
    public void setContext(Context context)
    {
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        recyclerView=view.findViewById(R.id.CustomerRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);
        RcycleViewAdapter adapter=new RcycleViewAdapter(this.context,customerlist);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}