package com.example.electromaid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonitoringPage extends AppCompatActivity {

    TextView tv_device_id, tv_nyobak1;
    String ID, AKTIF;
    Button btn_notif, btn_publish;
    ToggleButton tb_status;

    List<Device> deviceList;
    int device_array_length;

    String[] device_id = new String[10]; // Buat nyimpen Device ID
    String[] status = new String[10]; // Buat nyimpen Device status
    String[] daya = new String[10]; // Buat nyimpen Device Daya
    int[] dayaInt = new int[10]; // Buat nyimpen Device Daya
    String[] aktif = new String[10];

    static String MQTTHOST = "tcp://broker.mqtt-dashboard.com";
    static String USERNAME = "";
    static String PASSWORD = "";
    String topicStr = "elektromaid/1/";
    MqttAndroidClient client;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring_page);

        // TextView
        tv_device_id = (TextView) findViewById(R.id.device_id);
        tv_nyobak1 = (TextView) findViewById(R.id.tv_nyobak1);

        //getSensorData();
        //putSensorData();
        recieveData();
        mqtt();

        tb_status = (ToggleButton) findViewById(R.id.tb_status);
        tb_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tb_status.isChecked()) {
                    pubOn();
                } else {
                    pubOff();
                }
            }
        });

        /*if (AKTIF.equals("AKTIF")){

        }*/

        btn_publish = (Button) findViewById(R.id.btn_publish);
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pubOn();
            }
        });
    }

    public void recieveData() {
        // recieve data
        Intent intent = getIntent();
        ID = intent.getExtras().getString("ID");
        AKTIF = intent.getExtras().getString("AKTIF");

        // setting value
        tv_device_id.setText("Device " + ID);
        tv_nyobak1.setText("Aktif: " + AKTIF);

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
                device_array_length = sensor_data.get(0).getDevice().size();

                deviceList = new ArrayList<>();
                for (int i = 0; i < device_array_length; i++) {
                    device_id[i] = sensor_data.get(0).getDevice().get(i).getId_device();
                    status[i] = sensor_data.get(0).getDevice().get(i).getStatus();
                    //daya[i] = Integer.parseInt(sensor_data.get(0).getDevice().get(i).getDaya();
                    daya[i] = sensor_data.get(0).getDevice().get(i).getDaya();
                }
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void putSensorData() {
        String value = "eiWee8ep9due4deeshoa8Peichai8Eih";

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        //devices.add();
        /*SensorDataPut sensorDataPut = new SensorDataPut(
                devices.get(0).setId_device("10"),
                devices.get(0).setDaya("10"),
                devices.get(0).setStatus("10"),
                devices.get(0).setAktif("10")
        );*/
        Device device = new Device("1.1", "0", "on", "false");
        /*sensorDataPut.getDevices().get(0).setId_device("1.1");
        sensorDataPut.getDevices().get(0).setDaya("100");
        sensorDataPut.getDevices().get(0).setStatus("off");
        sensorDataPut.getDevices().get(0).setAktif(true);*/
        /*ourRetrofit.putPost(sensorDataPut, value);
        Call<List<Device>> call = ourRetrofit.putPost(sensorDataPut, value);
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
                } else if (response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void mqtt() {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void pubOn() {
        String topic = "elektromaid/1/" + ID;
        String status = "on";
        String aktif = AKTIF;
        Boolean aktifbool = true;
        try {
            client.publish(topic, status.getBytes(), 0, false);
            client.publish(topic, aktif.getBytes(), 0, false);
            Toast.makeText(getApplicationContext(), "ON" + topic, Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void pubOff() {
        String topic = "elektromaid/1/" + ID;
        String status = "off";
        String aktif = AKTIF;
        Boolean aktifbool = true;
        try {
            client.publish(topic, status.getBytes(), 0, false);
            client.publish(topic, aktif.getBytes(), 0, false);
            Toast.makeText(getApplicationContext(), "ON" + topic, Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
