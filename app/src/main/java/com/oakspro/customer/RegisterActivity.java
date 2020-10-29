package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name,address,mother, email, reference, nationalid, mobile;
    Spinner center;
    Button registerBtn;
    private String url="https://oakspro.com/projects/project35/deepu/TLS/newcustomer.php";
    private int selected_gender;
    ProgressDialog progressDialog;
    OfflineDatabase offlineDatabase;
    private boolean ConnectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //set view ids

        name=findViewById(R.id.name_ed);
        address=findViewById(R.id.address_ed);
        center=findViewById(R.id.center_ed);
        mother=findViewById(R.id.mother_ed);
        email=findViewById(R.id.email_ed);
        reference=findViewById(R.id.reference_ed);
        nationalid=findViewById(R.id.nationalid_ed);
        registerBtn=findViewById(R.id.registerBtn);
        mobile=findViewById(R.id.mobile_ed);

        offlineDatabase=new OfflineDatabase(this);

        final RadioGroup radioGroup=findViewById(R.id.genderradiogroup);

        selected_gender=radioGroup.getCheckedRadioButtonId();


        final String suser=getIntent().getStringExtra("user").toString();


        final String[] centers={"A", "B", "C", "D", "E", "F","M","N","K", "R","U","O"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, centers);
        center.setAdapter(adapter);

        progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isConnected();


                String sname=name.getText().toString();
                String semail=email.getText().toString();
                String smother_name=mother.getText().toString();
                String scenter=center.getSelectedItem().toString();
                String saddress=address.getText().toString();
                String sreference=reference.getText().toString();
                String snationalid=nationalid.getText().toString();
                String smobile=mobile.getText().toString();

                RadioButton gender=(RadioButton)findViewById(selected_gender);
                String sgender=gender.getText().toString();

                if (!TextUtils.isEmpty( sname) && !TextUtils.isEmpty(semail ) && !TextUtils.isEmpty(smother_name) && !TextUtils.isEmpty(scenter) &&
                        !TextUtils.isEmpty( saddress) && !TextUtils.isEmpty(sreference) && !TextUtils.isEmpty(snationalid) && !TextUtils.isEmpty(smobile) &&
                        !TextUtils.isEmpty(sgender) ){

                    if (!TextUtils.isEmpty(suser)){
                        //register

                        if (ConnectionStatus==true){

                            RegisterNew(sname, smobile, saddress, scenter, smother_name, semail, sreference, sgender, snationalid, suser);
                        }else {
                            StoredDataLocal(sname, smobile, saddress, scenter, smother_name, semail, sreference, sgender, snationalid, suser);
                        }





                    }else {
                        Toast.makeText(RegisterActivity.this, "Authorization is invalid.. Please Login Again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                }else {
                    Toast.makeText(RegisterActivity.this, "Please Enter all details", Toast.LENGTH_SHORT).show();
                }




            }
        });



    }

    private void StoredDataLocal(final String sname, final String smobile, final String saddress, final String scenter, final String smother_name, final String semail, final String sreference, final String sgender, final String snationalid, final String suser) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("No Internet Connection. Do you want to store in local Database")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //code for insert
                        String sstaus="0";
                        boolean inserted=offlineDatabase.insertData(sname, smobile, saddress, scenter, smother_name, semail, sreference, sgender, snationalid, suser, sstaus);

                        if (inserted==true){
                            Toast.makeText(RegisterActivity.this, "Data Stores Successfully", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(RegisterActivity.this, RegisterActivity.class);
                            intent.putExtra("user", getIntent().getStringExtra("user"));
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Data Failed to Insert", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel program
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    private void RegisterNew(final String sname, final String smobile, final String saddress, final String scenter, final String smother_name, final String semail, final String sreference, final String sgender, final String snationalid, final String suser) {
    //logic to send data to server
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, "Result: " + response.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("name", sname);
                params.put("mobile", smobile);
                params.put("address", saddress);
                params.put("city", scenter);
                params.put("mother", smother_name);
                params.put("email", semail);
                params.put("reference", sreference);
                params.put("gender", sgender);
                params.put("nationalid", snationalid);
                params.put("user", suser);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(request);

    }

    private void isConnected(){

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobiledata=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi !=null && wifi.isConnected()) || (mobiledata !=null && mobiledata.isConnected())){
            ConnectionStatus=true;
        }
        else {
            ConnectionStatus=false;
        }

    }

}