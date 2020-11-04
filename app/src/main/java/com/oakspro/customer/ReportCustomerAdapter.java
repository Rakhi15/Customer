package com.oakspro.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ReportCustomerAdapter extends ArrayAdapter<ReportCustomer> {
    Context context;
    List<ReportCustomer> arrayListReport;

    public ReportCustomerAdapter(Context context, List<ReportCustomer> arrayListReport){
        super(context, R.layout.custom_list_table, arrayListReport);
        this.context=context;
        this.arrayListReport=arrayListReport;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_table, null, true);

        //TextView tslId=view.findViewById(R.id.txt_id);
        TextView mobile=view.findViewById(R.id.mobile_txt);
        TextView name=view.findViewById(R.id.name_txt);
        TextView city=view.findViewById(R.id.city_txt);
        TextView regdate=view.findViewById(R.id.regdate_txt);


        name.setText(arrayListReport.get(position).getName());
        mobile.setText(arrayListReport.get(position).getMobile());
        city.setText(arrayListReport.get(position).getCity());
        regdate.setText(arrayListReport.get(position).getRegdate());

        return view;
    }
}
