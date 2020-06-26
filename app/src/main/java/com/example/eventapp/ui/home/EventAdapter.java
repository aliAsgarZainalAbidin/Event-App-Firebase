package com.example.eventapp.ui.home;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;
import com.example.eventapp.data.local.Event;
import com.example.eventapp.ui.detail.DetailActivity;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> list;

    public void setList(List<Event> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = list.get(position);
        Log.d("Adapter", "onBindViewHolder: "+event.getTitle());
        holder.onBind(event);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvLocation;
        TextView tvWaktu;
        TextView tvTanggal;
        ConstraintLayout constraintLayout;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraint_list_container);
            tvTitle = itemView.findViewById(R.id.tv_list_title);
            tvLocation = itemView.findViewById(R.id.tv_list_location);
            tvWaktu = itemView.findViewById(R.id.tv_list_time);
            tvTanggal = itemView.findViewById(R.id.tv_list_date);
        }

        void onBind(Event event){
            String title = event.getTitle();
            String location = event.getRuangan();
            String waktu = event.getWaktu();
            String tanggal = event.getTanggal();

            tvTitle.setText(title);
            tvWaktu.setText(waktu);
            tvLocation.setText(location);
            tvTanggal.setText(tanggal);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.DETAIL, event);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
