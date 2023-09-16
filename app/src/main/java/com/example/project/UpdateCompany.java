package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpdateCompany extends AppCompatActivity {
Button Cancel,find,Update;
TextView name,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_company);
        Cancel=findViewById(R.id.updateCompanyCancel);
        find=findViewById(R.id.updateCompanyFind);
        Update=findViewById(R.id.updateCompanyButton);
        name=findViewById(R.id.UpdateCompanyName);
        email=findViewById(R.id.UpdateCompanyEmail);
    }

    class ButtonsListener implements View.OnClickListener
    {


        @Override
        public void onClick(View view) {
            if(view.getId()==Cancel.getId())
            {
                finish();
            }
            else if(view.getId()==find.getId())
            {
                //TODO search in database and updae all other fields
            }
            else if(view.getId()==Update.getId())
            {
                String cname=name.getText().toString();
                String cemail=email.getText().toString();
                name.setText("");
                email.setText("");
                // TODO get company that has the id of the inserted value and insert the new values in the fields
            }
        }
    }
}