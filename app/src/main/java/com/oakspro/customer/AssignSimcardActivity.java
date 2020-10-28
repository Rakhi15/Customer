package com.oakspro.customer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
    Spinner simslot;
    Button assignSimcardBtn;
    EditText searchMobile;
    Button searchBtn;
    TextView customerDetails;
    ProgressDialog progressDialog, progressUpload;
    String cname, cemail, cuserid;
    String api_mobile = "https://oakspro.com/projects/project35/deepu/TLS/mobile_search.php";
    String assign_sim_api = "https://oakspro.com/projects/project35/deepu/TLS/assign_simcard.php";


    Integer sim_slot_number;
    String iccid_s, imsi_s, simselected_s;
    EditText iccid_ed, imsi_ed;
    String imei_number, phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_simcard);


        searchMobile = findViewById(R.id.mobile_search_ed);
        searchBtn = findViewById(R.id.searchBtn);
        customerDetails = findViewById(R.id.customer_details);
        linearLayout = findViewById(R.id.linear22);
        simslot = findViewById(R.id.simslot_ed);
        assignSimcardBtn = findViewById(R.id.assignBtn);
        iccid_ed = findViewById(R.id.iccid_ed);
        imsi_ed = findViewById(R.id.imsi_ed);


        progressDialog = new ProgressDialog(AssignSimcardActivity.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressUpload = new ProgressDialog(AssignSimcardActivity.this);
        progressUpload.setMessage("Assigning Simcard...");
        progressUpload.setIndeterminate(true);
        progressUpload.setCanceledOnTouchOutside(false);
        progressUpload.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        imei_number=manager.getDeviceId();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        phoneNumber = manager.getLine1Number();


        final String[] slots = {"SIM 1", "SIM 2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, slots);
        simslot.setAdapter(adapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobilenew = searchMobile.getText().toString();


                if (!TextUtils.isEmpty(mobilenew)) {
                    //check mobile number in server

                    checkMobile(mobilenew);

                } else {
                    searchMobile.setError("Please Enter Valid Mobile Number");
                }


            }
        });

        assignSimcardBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                iccid_s = iccid_ed.getText().toString();
                imsi_s = imsi_ed.getText().toString();
                simselected_s = simslot.getSelectedItem().toString();

                if (!TextUtils.isEmpty(iccid_s) && !TextUtils.isEmpty(imsi_s) && !TextUtils.isEmpty(simselected_s)) {

                    AssignSimProgram(iccid_s, imsi_s, simselected_s);

                } else {
                    Toast.makeText(AssignSimcardActivity.this, "Please Enter Valid Data for Assign SIM Card", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void AssignSimProgram(final String iccid_s, final String imsi_s, String simselected_s) {
        progressUpload.show();

        if (simselected_s.equals("SIM 1")) {
            sim_slot_number = 0;
        } else if (simselected_s.equals("SIM 2")) {
            sim_slot_number = 1;
        } else {
            Toast.makeText(this, "SIM SIM SLOTS NOT AVAILABLE IN DEVICE", Toast.LENGTH_SHORT).show();
        }





        StringRequest assignSimRequest=new StringRequest(Request.Method.POST, assign_sim_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressUpload.dismiss();
                Toast.makeText(AssignSimcardActivity.this, "Upload Status: "+response.toString(), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(AssignSimcardActivity.this, HomeActivity.class);
                intent.putExtra("user", getIntent().getStringExtra("user"));
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AssignSimcardActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressUpload.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> assignSimData=new HashMap<>();
                assignSimData.put("iccid", iccid_s);
                assignSimData.put("imsi", imsi_s);
                assignSimData.put("imei", imei_number);
                assignSimData.put("phoneNumber", phoneNumber);
                assignSimData.put("userid", cuserid);

                return assignSimData;
            }
        };
        RequestQueue assign_requestQueue=Volley.newRequestQueue(AssignSimcardActivity.this);
        assign_requestQueue.add(assignSimRequest);

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
                        customerDetails.setTextColor(Color.CYAN);
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