package com.example.electromaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    private Button buttonBackRegister;
    private Button buttonRegister;
    EditText et_name, et_email, et_password;
    String name, email, password;
    String id, message;
    int status;
    TextView tres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        et_name = (EditText) findViewById(R.id.name);
        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);
        //tres = findViewById(R.id.response);

        /*buttonRegister = (Button) findViewById(R.id.signup);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });*/
    }

    public void PostData(View view) {
        name = et_name.getText().toString();
        email = et_email.getText().toString();
        password = et_password.getText().toString();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        OurDataSet ourDataSet = new OurDataSet(name, email, password);
        Call<RegisterResponse> call = ourRetrofit.PostData(ourDataSet);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Register Success",Toast.LENGTH_SHORT).show();
                    id = response.body().getId();
                    openLoginActivity();
                }
                else if(response.errorBody()!=null){
                    try {
                        //Toast.makeText(getApplicationContext(),"" + response.errorBody().string(),Toast.LENGTH_SHORT).show();
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
         }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void jsonParse() {
        String url = "https://elektromaid.herokuapp.com/v1/auth/signup";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            String message = jsonObject.getString("message");
                            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                            toast.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }*/

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void openLoginActivity() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        finish();
    }

    public void openLandingPage() {
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //openLandingPage();
        overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_top);
    }
}
