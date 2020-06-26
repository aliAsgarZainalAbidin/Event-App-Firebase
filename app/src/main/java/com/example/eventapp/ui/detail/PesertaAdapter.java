package com.example.eventapp.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;

import java.util.List;

public class PesertaAdapter extends RecyclerView.Adapter<PesertaAdapter.PesertaViewHolder> {

    private List<String> list;

    public void setList(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PesertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_peserta_layout, parent, false);
        return new PesertaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PesertaViewHolder holder, int position) {
        String email = list.get(position);
        holder.onBind(email);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PesertaViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama;

        public PesertaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_rv_peserta_nama);
        }

        void onBind(String email){
            tvNama.setText(email);
        }
    }
}
