package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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
import java.util.Map;

public class DataShowActivity extends AppCompatActivity {

    String sdate, edate;
    ProgressDialog progressDialog;
    String report_api="https://oakspro.com/projects/project35/deepu/TLS/report_api.php";

    ListView listView;
    ReportCustomerAdapter adapter;
    public static ArrayList<ReportCustomer> reportList =new ArrayList<>();

    ReportCustomer reportCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_show);

        sdate=getIntent().getStringExtra("sdate").toString();
        edate=getIntent().getStringExtra("edate").toString();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        listView=findViewById(R.id.list);

        reportList.clear();
        adapter=new ReportCustomerAdapter(this, reportList);
        listView.setAdapter(adapter);



        getDataFromServer(sdate, edate);
    }

    private void getDataFromServer(final String sdate, final String edate){

        progressDialog.show();
        reportList.clear();

        StringRequest dataRequest=new StringRequest(Request.Method.POST, report_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");

                    if (success.equals("1")){
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object=jsonArray.getJSONObject(i);

                            String name=object.getString("name");
                            String mobile=object.getString("mobile");
                            String city=object.getString("center");
                            String regdate=object.getString("regdate");


                            reportCustomer=new ReportCustomer(name, mobile, city, regdate);
                            reportList.add(reportCustomer);
                            Log.i("TAG", reportCustomer.getName());
                            adapter.notifyDataSetChanged();

                        }
                        progressDialog.dismiss();



                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(DataShowActivity.this, "No Customer Found", Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(DataShowActivity.this, "Json Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DataShowActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("sdate", sdate);
                params.put("edate", edate);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(dataRequest);


    }
}