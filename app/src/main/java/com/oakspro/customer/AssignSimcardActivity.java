package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class AssignSimcardActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Button assignSimcardBtn;
    EditText searchMobile;
    Button searchBtn;
    TextView customerDetails;
    ProgressDialog progressDialog, progressUpload;
    String cname, cemail, cuserid;
    String api_mobile="https://oakspro.com/projects/project35/deepu/TLS/mobile_search.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_simcard);


        searchMobile=findViewById(R.id.mobile_search_ed);
        searchBtn=findViewById(R.id.searchBtn);
        customerDetails=findViewById(R.id.customer_details);
        linearLayout=findViewById(R.id.linear22);


        progressDialog=new ProgressDialog(AssignSimcardActivity.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressUpload=new ProgressDialog(AssignSimcardActivity.this);
        progressUpload.setMessage("Assigning Simcard...");
        progressUpload.setIndeterminate(true);
        progressUpload.setCanceledOnTouchOutside(false);
        progressUpload.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobilenew=searchMobile.getText().toString();


                if (!TextUtils.isEmpty(mobilenew)){
                    //check mobile number in server

                    checkMobile(mobilenew);

                }
                else {
                    searchMobile.setError("Please Enter Valid Mobile Number");
                }


            }
        });

    }

    private void checkMobile(final String mobilenew) {

        progressDialog.show();

        StringRequest request=new StringRequest(Request.Method.POST, api_mobile, new Response.Listener<String>() {
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

                            cuserid=object.getString("userid").trim();
                            cname=object.getString("name").trim();
                            cemail=object.getString("email").trim();

                        }
                        customerDetails.setText(cname+" "+cemail);
                        customerDetails.setVisibility(View.VISIBLE);
                        customerDetails.setTextColor(Color.GREEN);
                        progressDialog.dismiss();
                        linearLayout.setVisibility(View.VISIBLE);


                    }else {
                        progressDialog.dismiss();
                        searchMobile.setError("Customer Not Found");
                        Toast.makeText(AssignSimcardActivity.this, "No Customer Found on this Mobile Number", Toast.LENGTH_SHORT).show();
                        customerDetails.setVisibility(View.VISIBLE);
                        customerDetails.setText("Customer Not Found");
                        customerDetails.setTextColor(Color.RED);
                        linearLayout.setVisibility(View.INVISIBLE);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(AssignSimcardActivity.this, "Json Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AssignSimcardActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("mobile", mobilenew);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(AssignSimcardActivity.this);
        requestQueue.add(request);

    }
}