package com.example.androidauthphp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidauthphp.Common.Common;
import com.example.androidauthphp.Remote.IMyAPI;
import com.example.androidauthphp.model.APIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView txt_register;
    EditText edit_email, edit_password;
    Button btn_login;

    IMyAPI mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Service
         mService = Common.getAPI();


         //Init View
        txt_register=(TextView) findViewById(R.id.txt_register);
        edit_email=(EditText) findViewById(R.id.edit_email);
        edit_password=(EditText) findViewById(R.id.edit_password);

        btn_login=(Button) findViewById(R.id.btn_login);

        //Event
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticatedUser(edit_email.getText().toString(),edit_password.getText().toString());
            }
        });



    }

    private void authenticatedUser(String email, String password) {

        mService.loginUser(email,password)
                .enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse result = response.body();
                        if(result.isError())
                            Toast.makeText(MainActivity.this,result.getError_msg(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this,"Login success !",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,HOMEActivity.class));
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
}