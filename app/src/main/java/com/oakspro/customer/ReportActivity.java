package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ReportActivity extends AppCompatActivity {

    Button startDateBtn, endDateBtn, getDataBtn;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        //set ids

        startDateBtn=findViewById(R.id.startdate);
        endDateBtn=findViewById(R.id.enddate);
        getDataBtn=findViewById(R.id.viewBtn);


        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                mYear=c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDateBtn.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();

            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                mYear=c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDateBtn.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdate=startDateBtn.getText().toString();
                String edate=endDateBtn.getText().toString();

                Toast.makeText(ReportActivity.this, "Start: "+sdate+"\n"+"End: "+edate, Toast.LENGTH_LONG).show();

                Intent intent=new Intent(ReportActivity.this, DataShowActivity.class);
                intent.putExtra("sdate",sdate);
                intent.putExtra("edate", edate);
                startActivity(intent);
            }
        });


    }
}