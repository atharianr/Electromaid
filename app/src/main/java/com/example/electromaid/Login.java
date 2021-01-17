package com.example.electromaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Button btn_signup, btn_login, btn_forgotpassword;
    private EditText et_email, et_password;
    String email, password;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);

        btn_signup = (Button) findViewById(R.id.buttonSignUp);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        btn_login = (Button) findViewById(R.id.loginbutton);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_forgotpassword = (Button) findViewById(R.id.btn_forgotpassword);
        btn_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPassword();
            }
        });
    }

    private void login(){
        email = et_email.getText().toString();
        password = et_password.getText().toString();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        OurLogin ourLogin = new OurLogin(email, password);
        Call<User> call = ourRetrofit.login(ourLogin);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Login Success", Toast.LENGTH_SHORT).show();
                    token = response.body().getToken();
                    openMainActivity();
                    //openNotifPage();
                }
                else if (response.errorBody() != null){
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, activity_home.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void openForgotPassword() {
        Intent intent = new Intent(this, ForgotPasswordPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    public void openNotifPage() {
        Intent intent = new Intent(this, activity_notification.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_top);
    }
}
