package com.example.electromaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPasswordPage extends AppCompatActivity {
    private Button btn_resetpassword;
    private EditText et_reset_token, et_new_password;
    String reset_token, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_page);

        et_reset_token = (EditText) findViewById(R.id.reset_token);
        et_new_password = (EditText) findViewById(R.id.new_password);

        btn_resetpassword = (Button) findViewById(R.id.btn_resetpassword);
        btn_resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        reset_token = et_reset_token.getText().toString();
        password = et_new_password.getText().toString();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        OurResetPassword ourResetPassword = new OurResetPassword(password, reset_token);
        Call<ResResetPassword> call = ourRetrofit.resetPassword(ourResetPassword);
        call.enqueue(new Callback<ResResetPassword>() {
            @Override
            public void onResponse(Call<ResResetPassword> call, Response<ResResetPassword> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Password Reset Successful!", Toast.LENGTH_SHORT).show();
                    openLoginActivity();
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
            public void onFailure(Call<ResResetPassword> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_top);
    }
}
