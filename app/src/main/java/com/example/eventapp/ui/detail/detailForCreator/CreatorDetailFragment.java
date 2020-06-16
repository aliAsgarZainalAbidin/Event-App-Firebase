package com.example.eventapp.ui.detail.detailForCreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventapp.R;
import com.example.eventapp.ui.event.EventActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatorDetailFragment extends Fragment implements View.OnClickListener{

    private Button btnEdit;
    private Button btnDelete;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
