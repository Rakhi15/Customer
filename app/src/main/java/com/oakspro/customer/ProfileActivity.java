package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView customername, customeremail, customermobile, customercity;
    ImageView profile_img;
    String api_profile_img="https://oakspro.com/projects/project35/deepu/TLS/profile_image.php";
    ProgressDialog progressDialog;
    String imagename;
    String main_dir_http="https://oakspro.com/projects/project35/deepu/TLS/uploads/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        customername=findViewById(R.id.customer_name);
        customeremail=findViewById(R.id.customer_email);
        customermobile=findViewById(R.id.customer_mobile);
        customercity=findViewById(R.id.customer_city);
        profile_img=findViewById(R.id.profile_image);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        String cuserid=getIntent().getStringExtra("userid").toString();
        String cname=getIntent().getStringExtra("name").toString();
        String cemail=getIntent().getStringExtra("email").toString();
        String cmobile=getIntent().getStringExtra("mobile").toString();
        String ccity=getIntent().getStringExtra("city").toString();


        Log.i("TAG", cuserid.toString());
        profileProcess(cuserid);


        customername.setText(cname);
        customermobile.setText(cmobile);
        customeremail.setText(cemail);
        customercity.setText(ccity);

    }

    private void profileProcess(final String cuserid) {

        progressDialog.show();
        StringRequest imagerequest=new StringRequest(Request.Method.POST, api_profile_img, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");

                    if (success.equals("1")){
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            imagename=object.getString("imagename").trim();
                        }
                        String image_url=main_dir_http.concat(imagename);
                        Picasso.get().load(image_url).into(profile_img);
                        progressDialog.dismiss();

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "No profile found", Toast.LENGTH_SHORT).show();

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Json Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, "Volley Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> imageparam=new HashMap<>();
                imageparam.put("userid", cuserid);
                return imageparam;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(imagerequest);



    }
}