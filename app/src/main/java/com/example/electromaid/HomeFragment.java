package com.example.electromaid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.core.view.ViewCompat.getTransitionName;

public class HomeFragment extends Fragment {

    ListView list_view;
    TextView tv_device_count, tv_usage_wh, tv_usage_price, tv_test,
            tv_device_id0, tv_device_id1, tv_device_id2, tv_device_id3, tv_device_id4, tv_device_id5,
            tv_daya_device0, tv_daya_device1, tv_daya_device2, tv_daya_device3, tv_daya_device4, tv_daya_device5;
    ToggleButton btn_expand;
    ExpandableRelativeLayout expand_layout;
    AnyChartView pie_chart;
    SwipeRefreshLayout swipe_refresh;

    String on = "on";
    String off = "off";
    String master_id, watt_total, ididid;
    String[] device_id = new String[10]; // Buat nyimpen Device ID
    String[] status = new String[10]; // Buat nyimpen Device status
    int[] daya = new int[10]; // Buat nyimpen Device Daya
    int device_array_length;
    double usage_wh = 0.0;
    double usage_kwh = 0.0;
    double usage_price = 0.0;
    private static String token;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_home, container, false);

        // TOKEN AUTH
        //getSecret();

        // DEVICE INGPO
        tv_device_id0 = (TextView) rootView.findViewById(R.id.device_id0);
        tv_device_id1 = (TextView) rootView.findViewById(R.id.device_id1);
        tv_device_id2 = (TextView) rootView.findViewById(R.id.device_id2);
        tv_device_id3 = (TextView) rootView.findViewById(R.id.device_id3);
        tv_device_id4 = (TextView) rootView.findViewById(R.id.device_id4);
        tv_device_id5 = (TextView) rootView.findViewById(R.id.device_id5);

        tv_daya_device0 = (TextView) rootView.findViewById(R.id.daya_device0);
        tv_daya_device1 = (TextView) rootView.findViewById(R.id.daya_device1);
        tv_daya_device2 = (TextView) rootView.findViewById(R.id.daya_device2);
        tv_daya_device3 = (TextView) rootView.findViewById(R.id.daya_device3);
        tv_daya_device4 = (TextView) rootView.findViewById(R.id.daya_device4);
        tv_daya_device5 = (TextView) rootView.findViewById(R.id.daya_device5);


        // TEST DOANG
        list_view = (ListView) rootView.findViewById(R.id.list_view);
        tv_test = (TextView) rootView.findViewById(R.id.tv_test);

        // DEVICE COUNT
        tv_device_count = (TextView) rootView.findViewById(R.id.device_count);

        // USAGE KWH and PRICE
        tv_usage_wh = (TextView) rootView.findViewById(R.id.usage_wh);
        tv_usage_price = (TextView) rootView.findViewById(R.id.usage_price);

        /*final Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DecimalFormat df = new DecimalFormat("0.00");

                                usage_wh += 0.1;
                                String usage_wh_df = df.format(usage_wh);
                                //String usage_kwh_str = Double.toString(usage_wh);
                                //tv_usage_wh.setText(usage_wh_df);

                                usage_kwh = usage_wh / 1000;
                                usage_price = usage_kwh * 1444.70;
                                String usage_price_df = df.format(usage_price);
                                tv_usage_price.setText(usage_price_df);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();*/

        // REFRESH
        /*swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });*/

        // EXPAND BUTTON
        btn_expand = (ToggleButton) rootView.findViewById(R.id.btn_expand);
        btn_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand_layout = (ExpandableRelativeLayout) rootView.findViewById(R.id.expand_layout);
                expand_layout.toggle();
                expand_layout.setExpanded(false);
            }
        });

        // PIE CHART
        pie_chart = (AnyChartView) rootView.findViewById(R.id.pie_chart);

        Fade fade = new Fade();
        View decor = getActivity().getWindow().getDecorView();
        //fade.excludeTarget(decor.findViewById(R.id.action_bar_container));
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(R.id.info_box, true);
        fade.excludeTarget(R.id.top_bar, true);

        getActivity().getWindow().setEnterTransition(fade);
        getActivity().getWindow().setExitTransition(fade);


        final CardView cardview = rootView.findViewById(R.id.card_device_1);
        Button buttonMonitoringPage = rootView.findViewById(R.id.device_1);
        buttonMonitoringPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), com.example.electromaid.MonitoringPage.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), cardview, getTransitionName(cardview));
                startActivity(intent, options.toBundle());
            }
        });


        getSensorData();
        //setupPieChart();
        //updatePost();

        return rootView;
    }

    /*private void getSecret() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        Call<ResponseBody> call = ourRetrofit.getSercret(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(getActivity().getApplicationContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Token Invalid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

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

                /* device id */
                for (int i = 0; i < device_array_length; i++) {
                    device_id[i] = sensor_data.get(0).getDevice().get(i).getId_device();
                }
                tv_device_id0.setText("Device\n" + device_id[0]);
                tv_device_id1.setText("Device\n" + device_id[1]);
                tv_device_id2.setText("Device\n" + device_id[2]);
                tv_device_id3.setText("Device\n" + device_id[3]);
                tv_device_id4.setText("Device\n" + device_id[4]);
                tv_device_id5.setText("Device\n" + device_id[5]);

                /* device status */
                for (int i = 0; i < device_array_length; i++) {
                    status[i] = sensor_data.get(0).getDevice().get(i).getStatus();
                }
                if (status[0].equals(off)){
                    tv_device_id0.setTextColor(Color.parseColor("#A9A9A9"));
                }
                else if (status[0].equals(on)){
                    tv_device_id0.setTextColor(Color.parseColor("#000000"));
                }


                /* device daya */
                for (int i = 0; i < device_array_length; i++) {
                    daya[i] = Integer.parseInt(sensor_data.get(0).getDevice().get(i).getDaya());
                }
                tv_daya_device0.setText(daya[0] + " Wh");
                tv_daya_device1.setText(daya[1] + " Wh");
                tv_daya_device2.setText(daya[2] + " Wh");
                tv_daya_device3.setText(daya[3] + " Wh");
                tv_daya_device4.setText(daya[4] + " Wh");
                tv_daya_device5.setText(daya[5] + " Wh");

                // SHOW USAGE WH
                watt_total = sensor_data.get(0).getWatt_total();
                tv_usage_wh.setText(watt_total);

                // SHOW USAGE PRICE
                usage_wh = Double.parseDouble(watt_total);
                usage_kwh = usage_wh / 1000;
                usage_price = usage_kwh * 1444.70;
                String usage_price_df = df.format(usage_price);
                tv_usage_price.setText(usage_price_df);

                // PIE CHART
                Pie pie = AnyChart.pie();
                List<DataEntry> dataEntries = new ArrayList<>();
                for (int i = 0; i < device_array_length; i++) {
                    dataEntries.add(new ValueDataEntry(device_id[i], daya[i]));
                }
                pie.data(dataEntries);
                pie_chart.setChart(pie);

                // TEST DOANG
                String[] masterId = new String[sensor_data.size()];
                //String masterId = "";
                for (int i = 0; i < sensor_data.size(); i++) {
                    masterId[i] = sensor_data.get(i).getMaster_id();
                    //tv_device_count.setText(masterId);
                }
                list_view.setAdapter(
                        new ArrayAdapter<String>(
                                getActivity().getApplicationContext(),
                                android.R.layout.simple_list_item_1,
                                masterId
                        )
                );
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        
        refresh(1000);
    }

    /*private void updatePost(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://elektromaid.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        SensorDataPut sensorDataPut = new SensorDataPut("off");
        Call<SensorDataPutRes> sensorDataPutResCall = ourRetrofit.putPost(sensorDataPut);
        sensorDataPutResCall.enqueue(new Callback<SensorDataPutRes>() {
            @Override
            public void onResponse(Call<SensorDataPutRes> call, Response<SensorDataPutRes> response) {
                if (response.isSuccessful()){
                    tv_test.setText(String.valueOf(response.body()));
                }
                else if(response.errorBody() != null){
                    tv_test.setText(String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<SensorDataPutRes> call, Throwable t) {
                tv_test.setText("Connection Error");
            }
        });
    }*/

    public void openMonitoringPage() {
        Intent intent = new Intent(getActivity().getApplication(), MonitoringPage.class);
        startActivity(intent);
    }

    private void refresh(int millisecond){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSensorData();
            }
        };
    }

}
