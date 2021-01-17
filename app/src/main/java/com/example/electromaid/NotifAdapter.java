package com.example.electromaid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {

    private ArrayList<NotifRes> notifRes = new ArrayList<>();
    private Context context;

    public NotifAdapter(Context context, ArrayList<NotifRes> notifRes){
        this.notifRes = notifRes;
        this.context = context;
    }

    @NonNull
    @Override
    public NotifAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notif,parent,false);
        return new NotifAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifAdapter.ViewHolder holder, int position) {
        holder.tv_notif_title.setText(notifRes.get(position).getTitle());
        holder.tv_notif_isi.setText(notifRes.get(position).getNotifikasi());
        holder.tv_notif_waktu.setText(notifRes.get(position).getWaktu());
    }

    @Override
    public int getItemCount() {
        return notifRes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_notif_title, tv_notif_isi, tv_notif_waktu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_notif_title = (TextView) itemView.findViewById(R.id.tv_notif_title);
            tv_notif_isi = (TextView) itemView.findViewById(R.id.tv_notif_isi);
            tv_notif_waktu = (TextView) itemView.findViewById(R.id.tv_notif_waktu);

        }
    }
}
