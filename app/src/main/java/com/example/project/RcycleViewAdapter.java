package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RcycleViewAdapter extends RecyclerView.Adapter<RcycleViewAdapter.myViewHolder> {
    private ArrayList<Customer>customerArrayList;

    public RcycleViewAdapter(Context context, ArrayList<Customer> customerArrayList) {
        this.customerArrayList = customerArrayList;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        private TextView id,fname,lname,email;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.CustomerItemId);
            this.fname = itemView.findViewById(R.id.CustomerItemFName);
            this.lname = itemView.findViewById(R.id.CustomerItemLName);
            this.email = itemView.findViewById(R.id.CustomerItemEmail);
        }
    }

    @NonNull
    @Override
    public RcycleViewAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item,parent,false);
        return new myViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String id=customerArrayList.get(position).getId()+"";
        String fName=customerArrayList.get(position).getfName();
        String lName=customerArrayList.get(position).getlName();
        String email=customerArrayList.get(position).getEmail();
        holder.email.setText(email);
        holder.fname.setText(fName);
        holder.id.setText(id);
        holder.lname.setText(lName);


    }



    @Override
    public int getItemCount() {
        return customerArrayList.size();
    }
}
