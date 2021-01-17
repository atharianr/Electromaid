package com.example.electromaid;

import android.content.Context;
import android.content.Intent;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {

    private Context mContext;
    private List<Device> mData;

    public DeviceAdapter(Context mContext, List<Device> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_device, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tv_device_id.setText("Device\n" + mData.get(position).getId_device());
        holder.tv_device_daya.setText(mData.get(position).getDaya() + " Wh");

        // set click listener
        holder.cardview_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing data to MonitoringPage
                Intent intent = new Intent(mContext, MonitoringPage.class);
                intent.putExtra("ID", mData.get(position).getId_device());
                intent.putExtra("DAYA", mData.get(position).getDaya());
                intent.putExtra("STATUS", mData.get(position).getStatus());
                intent.putExtra("AKTIF", mData.get(position).getAktif());
                //intent.putExtra("daya", mData.get(position).getDaya());
                // start activity
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_device_id, tv_device_daya;
        CardView cardview_device;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_device_id = (TextView) itemView.findViewById(R.id.tv_device_id);
            tv_device_daya = (TextView) itemView.findViewById(R.id.tv_device_daya);
            cardview_device = (CardView) itemView.findViewById(R.id.cardview_device);

        }
    }


}
