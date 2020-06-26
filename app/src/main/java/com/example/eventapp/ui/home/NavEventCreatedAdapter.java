package com.example.eventapp.ui.home;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class NavEventCreatedAdapter extends RecyclerView.Adapter<NavEventCreatedAdapter.NavEventViewHolder> {

    private List<Event> list = new ArrayList<>();

    public void setList(List<Event> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NavEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_view_item_drawer, parent, false);
        return new NavEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavEventViewHolder holder, int position) {
        Event event = list.get(position);
        holder.onBind(event);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NavEventViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ConstraintLayout constraintLayout;
        public NavEventViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_nav_header_nama);
            constraintLayout = itemView.findViewById(R.id.constraint_nav_header_layout);
        }

        void onBind(Event event){
            textView.setText(event.getTitle());

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
