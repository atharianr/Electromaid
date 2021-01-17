package com.example.electromaid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.core.view.ViewCompat.getTransitionName;

public class activity_home extends AppCompatActivity {

    SwipeRefreshLayout swipe_layout;
    ListView list_view;
    TextView tv_device_count, tv_usage_wh, tv_usage_price, tv_test,
            tv_device_id0, tv_device_id1, tv_device_id2, tv_device_id3, tv_device_id4, tv_device_id5,
            tv_daya_device0, tv_daya_device1, tv_daya_device2, tv_daya_device3, tv_daya_device4, tv_daya_device5;
    ToggleButton btn_expand;
    Button btn_notif_page, btn_setting_page, btn_notif;
    ExpandableRelativeLayout expand_layout;
    AnyChartView pie_chart;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String currentDate;

    String on = "on";
    String off = "off";
    String master_id, watt_total, ididid;
    String[] device_id = new String[10]; // Buat nyimpen Device ID
    String[] status = new String[10]; // Buat nyimpen Device status
    String[] daya = new String[10]; // Buat nyimpen Device Daya
    int[] dayaInt = new int[10]; // Buat nyimpen Device Daya
    String[] aktif = new String[10];
    int device_array_length;
    double usage_wh = 0.0;
    double usage_kwh = 0.0;
    double usage_price = 0.0;
    private static String token;

    List<Device> deviceList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // DEVICE COUNT
        tv_device_count = (TextView) findViewById(R.id.device_count);

        // USAGE KWH and PRICE
        tv_usage_wh = (TextView) findViewById(R.id.usage_wh);
        tv_usage_price = (TextView) findViewById(R.id.usage_price);

        // EXPAND BUTTON
        btn_expand = (ToggleButton) findViewById(R.id.btn_expand);
        btn_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand_layout = (ExpandableRelativeLayout) findViewById(R.id.expand_layout);
                expand_layout.toggle();
                expand_layout.setExpanded(true);
            }
        });

        // PIE CHART
        pie_chart = (AnyChartView) findViewById(R.id.pie_chart);

        // BUTTON BOTTOM
        btn_notif_page = (Button) findViewById(R.id.btn_notif_page);
        btn_notif_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotifPage();
            }
        });
        btn_setting_page = (Button) findViewById(R.id.btn_setting_page);
        btn_setting_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingPage();
            }
        });

        // GET SENSOR DATA
        getSensorData();
        listDevice();

        // SWIPE
        swipe_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // GET SENSOR DATA
                getSensorData();
                listDevice();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_layout.setRefreshing(false);
                    }
                }, 4000);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void listDevice() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);
        Call<List<SensorData>> call = ourRetrofit.getSensorData();
        call.enqueue(new Callback<List<SensorData>>() {
            @Override
            public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                List<SensorData> sensor_data = response.body();
                device_array_length = sensor_data.get(0).getDevice().size();

                deviceList = new ArrayList<>();
                for (int i = 0; i < device_array_length; i++) {
                    deviceList.add(new Device(device_id[i], daya[i], status[i], aktif[i]));
                }

                RecyclerView device_recyclerview = (RecyclerView) findViewById(R.id.device_recyclerview);
                DeviceAdapter myAdapter = new DeviceAdapter(getApplicationContext(), deviceList);
                device_recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                device_recyclerview.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSensorData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);
        Call<List<SensorData>> call = ourRetrofit.getSensorData();
        call.enqueue(new Callback<List<SensorData>>() {
            @Override
            public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {

                List<SensorData> sensor_data = response.body();

                // DECIMAL FORMAT
                DecimalFormat df = new DecimalFormat("0.00");

                // GET ID
                ididid = sensor_data.get(0).get_id().getOid();

                // GET DEVICE INGPO
                device_array_length = sensor_data.get(0).getDevice().size();
                tv_device_count.setText(String.valueOf(device_array_length));

                /* device id, status, daya */
                for (int i = 0; i < device_array_length; i++) {
                    device_id[i] = sensor_data.get(0).getDevice().get(i).getId_device();
                    status[i] = sensor_data.get(0).getDevice().get(i).getStatus();
                    //daya[i] = Integer.parseInt(sensor_data.get(0).getDevice().get(i).getDaya();
                    daya[i] = sensor_data.get(0).getDevice().get(i).getDaya();
                }

                // SHOW USAGE WH
                watt_total = sensor_data.get(0).getWatt_total();
                tv_usage_wh.setText(watt_total);
                tv_usage_wh.setBackgroundColor(Color.TRANSPARENT);

                // SHOW USAGE PRICE
                usage_wh = Double.parseDouble(watt_total);
                usage_kwh = usage_wh / 1000;
                usage_price = usage_kwh * 1444.70;
                String usage_price_df = df.format(usage_price);
                tv_usage_price.setText(usage_price_df);
                tv_usage_price.setBackgroundColor(Color.TRANSPARENT);

                double usage_wh_temp = usage_wh % 200;
                if (usage_wh_temp == 0) {
                    notif();
                }

                // PIE CHART
                Pie pie = AnyChart.pie();
                List<DataEntry> dataEntries = new ArrayList<>();
                for (int i = 0; i < device_array_length; i++) {
                    dayaInt[i] = Integer.parseInt(daya[i]);
                    dataEntries.add(new ValueDataEntry(device_id[i], dayaInt[i]));
                }
                pie.data(dataEntries);
                pie_chart.setChart(pie);
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void notif() {
        String message = "Hello, your electricity usage is too high.";
        String title = "Alert!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                activity_home.this, "My Notification"
        );
        builder.setSmallIcon(R.drawable.ic_message);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);

        // CURRENT TIME
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        String dateTime = simpleDateFormat.format(calendar.getTime());

        Intent intent = new Intent(activity_home.this, activity_notification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity_home.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(activity_home.this);
        notificationManagerCompat.notify(1, builder.build());

        // CALENDAR
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        currentDate = simpleDateFormat.format(calendar.getTime());

        // POST
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        NotifRes notifRes = new NotifRes(title, message, currentDate);
        Call<List<NotifRes>> call = ourRetrofit.postNotif(notifRes);
        call.enqueue(new Callback<List<NotifRes>>() {
            @Override
            public void onResponse(Call<List<NotifRes>> call, Response<List<NotifRes>> response) {
            }

            @Override
            public void onFailure(Call<List<NotifRes>> call, Throwable t) {
            }
        });
    }

    public void openNotifPage() {
        Intent intent = new Intent(this, activity_notification.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void openSettingPage() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

}
