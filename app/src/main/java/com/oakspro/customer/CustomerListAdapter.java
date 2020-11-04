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

public class CustomerListAdapter extends ArrayAdapter<Customers> {
    Context context;
    List<Customers> arrayListCustomers;


    public CustomerListAdapter(@NonNull Context context, List<Customers> arrayListCustomers) {
        super(context, R.layout.custom_list_item, arrayListCustomers);

        this.context=context;
        this.arrayListCustomers =arrayListCustomers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, null, true);

        //TextView tslId=view.findViewById(R.id.txt_id);
        TextView custname=view.findViewById(R.id.customer_name);
        TextView custmobile=view.findViewById(R.id.customer_mobile);
        TextView custcity=view.findViewById(R.id.customer_city);


        custname.setText(arrayListCustomers.get(position).getName());
        custmobile.setText(arrayListCustomers.get(position).getMobile());
        custcity.setText(arrayListCustomers.get(position).getCity());

        return view;
    }
}
