package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class MainActivity extends AppCompatActivity {

    TextView login_txt;
    EditText username, password, otp;
    Button login_btn, verify_btn;
    LinearLayout linear1, linear2;
    ProgressDialog progressDialog;
    private String url="https://oakspro.com/projects/project35/deepu/TLS/syncdata.php";
    OfflineDatabase offlineDatabase;
    private boolean ConnectionStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set view ids

        login_txt=findViewById(R.id.login_txt);
        username=findViewById(R.id.username_ed);
        password=findViewById(R.id.password_ed);
        otp=findViewById(R.id.otp_ed);
        login_btn=findViewById(R.id.loginBtn);
        verify_btn=findViewById(R.id.verifyBtn);
        linear1=findViewById(R.id.linear1);
        linear2=findViewById(R.id.linear2);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Syncing Data....");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        offlineDatabase=new OfflineDatabase(this);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //program of login
                isConnected();

                String uname=username.getText().toString();
                String upass=password.getText().toString();

                if (TextUtils.isEmpty(uname)){
                    Toast.makeText(MainActivity.this, "Enter Username Please", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (TextUtils.isEmpty(upass)){
                        Toast.makeText(MainActivity.this, "Enter Password Please", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //logic of authentication

                        if (uname.equals("admin")){
                            if (upass.equals("12%%K")){
                               //logic 3
                                linear1.setVisibility(View.INVISIBLE);
                                login_btn.setVisibility(View.INVISIBLE);
                                linear2.setVisibility(View.VISIBLE);
                                verify_btn.setVisibility(View.VISIBLE);

                                if (ConnectionStatus==true) {
                                       syncDataCheck();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Wrong Password..try again", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Please Enter Valid Username", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }
        });

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String uotp=otp.getText().toString();
               if (uotp.equals("123456") && uotp.length()==6){
                   //logic for next process
                   //intent is used to move from one activity to other activity(communication, data exchange) types=explict, implicit

                   Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                   intent.putExtra("user", username.getText().toString());
                   startActivity(intent);


               }else {
                   Toast.makeText(MainActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                   otp.setError("Enter Valid OTP");
               }
            }
        });

    }

    private void syncDataCheck() {

        progressDialog.show();

        Cursor res=offlineDatabase.viewAll();
        if (res.getCount() ==0){

            progressDialog.dismiss();
            return;
        }
        else {


            while (res.moveToNext()){

                String name=res.getString(1);
                String mobile=res.getString(2);
                String address=res.getString(3);
                String city=res.getString(4);
                String mother=res.getString(5);
                String email=res.getString(6);
                String reference=res.getString(7);
                String gender=res.getString(8);
                String nationalid=res.getString(9);
                String user=res.getString(10);
                
                uploadSync(name, mobile, address, city, mother, email, reference, gender, nationalid, user);
            }
        }
    }

    private void uploadSync(final String name, final String mobile, final String address, final String city, final String mother, final String email, final String reference, final String gender, final String nationalid, final String user) {

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Toast.makeText(MainActivity.this, "Result: " + response.toString(), Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");

                    if (success.equals("1")){

                        Integer deletedItem=offlineDatabase.deleteData(mobile);
                        if (deletedItem>0){
                            Toast.makeText(MainActivity.this, "One Record Deleted from local", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Record Not Deleted", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        progressDialog.dismiss();
                        syncDataCheck();

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Json Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("name", name);
                params.put("mobile", mobile);
                params.put("address", address);
                params.put("city", city);
                params.put("mother", mother);
                params.put("email", email);
                params.put("reference", reference);
                params.put("gender", gender);
                params.put("nationalid", nationalid);
                params.put("user", user);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
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