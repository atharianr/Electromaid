package com.example.electromaid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.core.annotations.Line;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_notification extends AppCompatActivity {
    Button btn_home_page;
    TextView tv_empty_notif;
    ImageView img_no_notif;

    ArrayList<NotifRes> notifRes = new ArrayList<>();
    private NotifAdapter notifAdapter;
    private RecyclerView notif_recyclerview;

    int array_length;
    String[] title = new String[50]; // Buat nyimpen title
    String[] notifikasi = new String[50]; // Buat nyimpen notif
    String[] waktu = new String[50];

    String[] titlecontoh = {"facebook", "instagram", "whatsapp"};
    String[] notifikasicontoh = {"facebook notif", "instagram notif", "whatsapp notif"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_norification);

        img_no_notif = (ImageView) findViewById(R.id.no_notification_icon);
        tv_empty_notif = (TextView) findViewById(R.id.tv_empty_notif);

        btn_home_page = (Button) findViewById(R.id.btn_home_page);
        btn_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        notif_recyclerview = (RecyclerView) findViewById(R.id.notif_recyclerview);
        notif_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        getNotif();
    }

    private void getNotif() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);
        Call<List<NotifRes>> call = ourRetrofit.getNotif();
        call.enqueue(new Callback<List<NotifRes>>() {
            @Override
            public void onResponse(Call<List<NotifRes>> call, Response<List<NotifRes>> response) {

                //List<NotifRes> notifRes = response.body();
                notifRes = new ArrayList<>(response.body());
                notifAdapter = new NotifAdapter(activity_notification.this,notifRes);
                notif_recyclerview.setAdapter(notifAdapter);
                if (notifRes.size() > 0){
                    img_no_notif.setImageResource(0);
                    tv_empty_notif.setText("");
                }
            }

            @Override
            public void onFailure(Call<List<NotifRes>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openHomePage() {
        Intent intent = new Intent(this, activity_home.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
