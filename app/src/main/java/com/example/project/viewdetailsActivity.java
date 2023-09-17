package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;

public class viewdetailsActivity extends AppCompatActivity {
    Button EditDetails;
    ListView CustomersDetails;
    ArrayAdapter<String> adapter;
    int selectedCustomerId = -1;
   CustomersFacade customersFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdetails);
        EditDetails = findViewById(R.id.detail_btnEdit);
        CustomersDetails = findViewById(R.id.lv_customerDetails);
        customersFacade=(CustomersFacade) getIntent().getSerializableExtra("customersFacade");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        CustomersDetails.setAdapter(adapter);

        CustomersDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                if (selectedItem != null) {
                    String[] parts = selectedItem.split("ID: ");
                    if (parts.length == 2) {
                        selectedCustomerId = Integer.parseInt(parts[1]);

////////////////////////// Fetch customer details from your data source based on selectedCustomerId///////////////////////
                        Customer customer = null;
                        try {
                            customer = customersFacade.getCustomerDetails();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        // Create a Bundle to pass customer details to the fragment
                        Bundle args = new Bundle();
                        args.putString("firstName", customer.getfName());
                        args.putString("lastName", customer.getlName());
                        args.putInt("customerId", customer.getId());
                        args.putString("email", customer.getEmail());

                        // Create an instance of the CustomerDetailsFragment
                        CustomerDetailsFragment fragment = new CustomerDetailsFragment();
                        fragment.setArguments(args);

                        // Replace fragment_container with your fragment container ID
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });

        EditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(viewdetailsActivity.this, updateDetailsActivity.class);
                    intent.putExtra("customerFacade", customersFacade);
                    startActivity(intent);

            }
        });
    }
}
