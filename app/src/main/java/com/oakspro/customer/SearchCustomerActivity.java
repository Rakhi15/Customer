package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCustomerActivity extends AppCompatActivity {
    ProgressDialog progressDialog, progressUpload;
    LinearLayout relayout1;
    EditText searchName;
    Button searchBtn;
    String api_search_name="https://oakspro.com/projects/project35/deepu/TLS/search_customer_name.php";

    ListView listView;
    CustomerListAdapter adapter;
    public static ArrayList<Customers> customersList =new ArrayList<>();

    Customers customers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);

        searchName=findViewById(R.id.mobile_search_ed);
        searchBtn=findViewById(R.id.searchBtn);
        relayout1=findViewById(R.id.relLayout1);

        customersList.clear();


        listView=findViewById(R.id.listview_search);
        adapter=new CustomerListAdapter(this, customersList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int itemPosition=position;

                Customers Itemdata=customersList.get(position);
                String cname=Itemdata.getName();
                String cemail=Itemdata.getEmail();
                String cmobile=Itemdata.getMobile();
                String ccity=Itemdata.getCity();
                String cuserid=Itemdata.getUserid();

                Intent intent=new Intent(SearchCustomerActivity.this, ProfileActivity.class);
                intent.putExtra("name", cname);
                intent.putExtra("email", cemail);
                intent.putExtra("mobile",cmobile);
                intent.putExtra("city", ccity);
                intent.putExtra("userid", cuserid);
                startActivity(intent);
            }
        });

        progressDialog=new ProgressDialog(SearchCustomerActivity.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namesearch=searchName.getText().toString();


                if (!TextUtils.isEmpty(namesearch)){
                    //check mobile number in server

                    checkName(namesearch);

                }
                else {
                    searchName.setError("Please Enter Valid Name");
                }


            }
        });

    }


    private void checkName(final String name) {
        progressDialog.show();
        customersList.clear();

        StringRequest request=new StringRequest(Request.Method.POST, api_search_name, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //logic to get response from server

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");

                    if (success.equals("1")){
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object=jsonArray.getJSONObject(i);

                            String name=object.getString("name");
                            String mobile=object.getString("mobile");
                            String city=object.getString("city");
                            String gender=object.getString("gender");
                            String mother=object.getString("mother_name");
                            String email=object.getString("email");
                            String nationalid=object.getString("nationalid");
                            String address=object.getString("address");
                            String userid=object.getString("userid");

                            customers=new Customers(name, mobile, address, city, mother, gender, email, nationalid,userid);
                            customersList.add(customers);
                            Log.i("TAG", customers.getName());
                            adapter.notifyDataSetChanged();

                        }
                        progressDialog.dismiss();
                        relayout1.setVisibility(View.VISIBLE);


                    }else {
                        progressDialog.dismiss();
                        searchName.setError("Customer Not Found");
                        Toast.makeText(SearchCustomerActivity.this, "No Customer Found on this Name", Toast.LENGTH_SHORT).show();


                        relayout1.setVisibility(View.INVISIBLE);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(SearchCustomerActivity.this, "Json Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchCustomerActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("name", name);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(SearchCustomerActivity.this);
        requestQueue.add(request);

    }

}