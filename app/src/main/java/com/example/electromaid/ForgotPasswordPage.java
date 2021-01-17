package com.example.electromaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordPage extends AppCompatActivity {
    private Button btn_sendrequest;
    private EditText et_email;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        et_email = (EditText) findViewById(R.id.email);

        btn_sendrequest = (Button) findViewById(R.id.btn_forgotpassword);
        btn_sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }

    private void forgotPassword(){
        email = et_email.getText().toString();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        OurForgotPassword ourForgotPassword = new OurForgotPassword(email);
        Call<ReqForgotPassword> call = ourRetrofit.forgotPassword(ourForgotPassword);
        call.enqueue(new Callback<ReqForgotPassword>() {
            @Override
            public void onResponse(Call<ReqForgotPassword> call, Response<ReqForgotPassword> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Request Link Sent", Toast.LENGTH_SHORT).show();
                    openResetPasswordPage();
                }
                else if(response.errorBody() != null){
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReqForgotPassword> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openResetPasswordPage(){
        Intent intent = new Intent(this, ResetPasswordPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_top);
    }
}
