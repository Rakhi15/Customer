package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

                        RegisterNew(sname, smobile, saddress, scenter, smother_name, semail, sreference, sgender, snationalid, suser);



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


}