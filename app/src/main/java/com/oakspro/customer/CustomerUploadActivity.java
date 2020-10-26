package com.oakspro.customer;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CustomerUploadActivity extends AppCompatActivity {

    RelativeLayout relayout1;
    EditText searchMobile;
    Spinner documenttype;
    Button searchBtn, browseBtn, cameraBtn, uploadBtn;
    ImageView loadedImage;
    TextView customerDetails;
    String api_mobile="https://oakspro.com/projects/project35/deepu/TLS/mobile_search.php";
    ProgressDialog progressDialog;
    String cname, cemail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_upload);
        //set ids

        searchMobile=findViewById(R.id.mobile_search_ed);
        searchBtn=findViewById(R.id.searchBtn);
        customerDetails=findViewById(R.id.customer_details);
        documenttype=findViewById(R.id.documenttype_spinner);
        browseBtn=findViewById(R.id.btn1);
        cameraBtn=findViewById(R.id.btn2);
        loadedImage=findViewById(R.id.imageView1);
        uploadBtn=findViewById(R.id.uploadBtn);
        relayout1=findViewById(R.id.relLayout1);

        progressDialog=new ProgressDialog(CustomerUploadActivity.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final String[] doctypes={"Passport", "National ID", "File"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, doctypes);
        documenttype.setAdapter(adapter);


        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(CustomerUploadActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                permissionToken.continuePermissionRequest();
                            }
                        }).check();


            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, 2);
            }
        });





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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data !=null){
            Uri filepath=data.getData();

            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                loadedImage.setImageBitmap(bitmap);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        if (requestCode == 2){
            Bitmap photo=(Bitmap)data.getExtras().get("data");
            loadedImage.setImageBitmap(photo);
        }

        super.onActivityResult(requestCode, resultCode, data);
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

                                    cname=object.getString("name").trim();
                                    cemail=object.getString("email").trim();

                                }
                                customerDetails.setText(cname+" "+cemail);
                                customerDetails.setVisibility(View.VISIBLE);
                                customerDetails.setTextColor(Color.GREEN);
                                progressDialog.dismiss();
                                relayout1.setVisibility(View.VISIBLE);


                            }else {
                                progressDialog.dismiss();
                                searchMobile.setError("Customer Not Found");
                                Toast.makeText(CustomerUploadActivity.this, "No Customer Found on this Mobile Number", Toast.LENGTH_SHORT).show();
                                customerDetails.setVisibility(View.VISIBLE);
                                customerDetails.setText("Customer Not Found");
                                customerDetails.setTextColor(Color.RED);
                                relayout1.setVisibility(View.INVISIBLE);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(CustomerUploadActivity.this, "Json Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerUploadActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(CustomerUploadActivity.this);
        requestQueue.add(request);

    }
}