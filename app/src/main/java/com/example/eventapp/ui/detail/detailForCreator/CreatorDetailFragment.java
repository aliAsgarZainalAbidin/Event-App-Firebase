package com.example.eventapp.ui.detail.detailForCreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventapp.R;
import com.example.eventapp.data.local.Event;
import com.example.eventapp.ui.detail.DetailActivity;
import com.example.eventapp.ui.detail.PesertaAdapter;
import com.example.eventapp.ui.event.EventActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatorDetailFragment extends Fragment implements View.OnClickListener{

    private Button btnEdit;
    private Button btnDelete;
    private TextView tvOverview;
    private TextView tvTanggal;
    private TextView tvRuanganDanLokasi;
    private TextView tvAlamat;
    private TextView tvWaktu;
    private List<String> listPeserta;
    private RecyclerView recyclerView;
    private TextView tvBerkas;

    public CreatorDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creator_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnEdit = view.findViewById(R.id.btn_detail_creator_edit);
        btnDelete = view.findViewById(R.id.btn_detail_creator_deleted);
        tvOverview = view.findViewById(R.id.tv_detail_creator_overview);
        tvTanggal = view.findViewById(R.id.tv_detail_creator_tanggal);
        tvRuanganDanLokasi = view.findViewById(R.id.tv_detail_creator_tempat);
        tvAlamat = view.findViewById(R.id.tv_detail_creator_jalan);
        tvWaktu = view.findViewById(R.id.tv_detail_creator_waktu);
        recyclerView = view.findViewById(R.id.rv_detail_creator_peserta);
        listPeserta = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity()!=null){
            Event event = getActivity().getIntent().getParcelableExtra(DetailActivity.DETAIL);
            String overview = event.getOverview();
            String tanggal = event.getTanggal();
            String ruangan = event.getRuangan()+", "+event.getGedung();
            String alamat = event.getAlamat();
            String waktu = event.getWaktu();
            listPeserta.addAll(event.getListEmail());

            tvOverview.setText(overview);
            tvTanggal.setText(tanggal);
            tvRuanganDanLokasi.setText(ruangan);
            tvAlamat.setText(alamat);
            tvWaktu.setText(waktu);

            PesertaAdapter pesertaAdapter = new PesertaAdapter();
            pesertaAdapter.setList(listPeserta);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(pesertaAdapter);
            
        }

        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_detail_creator_edit : {
                Intent intent = new Intent(getContext(), EventActivity.class);
                intent.putExtra(EventActivity.TITLE_EVENT, "Nama Event");
                intent.putExtra(EventActivity.EDIT_EVENT, true);
                startActivity(intent);
                break;
            }

            case R.id.btn_detail_creator_deleted : {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(getContext());
                aBuilder.setTitle("Warning");
                aBuilder.setMessage("Hapus event ini ?");
                aBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete event
                        getActivity().finish();
                    }
                });
                aBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                aBuilder.show();

                break;
            }
        }
    }
}
