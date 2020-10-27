package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    CardView newcustomerBtn, assignsimcardBtn, customerdocBtn, searchCustomerBtn, reportBtn, profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //set ids

        newcustomerBtn=findViewById(R.id.card1);
        assignsimcardBtn=findViewById(R.id.card2);
        customerdocBtn=findViewById(R.id.card3);
        searchCustomerBtn=findViewById(R.id.card4);
        reportBtn=findViewById(R.id.card5);
        profileBtn=findViewById(R.id.card6);

        final String user=getIntent().getStringExtra("user").toString();


        newcustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newcustomerintent=new Intent(HomeActivity.this, RegisterActivity.class);
                newcustomerintent.putExtra("user", user);
                startActivity(newcustomerintent);
            }
        });

        customerdocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, CustomerUploadActivity.class);
                startActivity(intent);
            }
        });

        assignsimcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeActivity.this, AssignSimcardActivity.class);
                startActivity(intent);
            }
        });


    }
}