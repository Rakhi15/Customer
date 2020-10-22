package com.oakspro.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView login_txt;
    EditText username, password, otp;
    Button login_btn, verify_btn;
    LinearLayout linear1, linear2;
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


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //program of login

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
}