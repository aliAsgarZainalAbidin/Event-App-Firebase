package com.example.eventapp.ui.detail.detailForUser;

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
import com.example.eventapp.ui.detail.DetailActivity;
import com.example.eventapp.ui.detail.SuccessDialogFragment;
import com.example.eventapp.ui.detail.scanner.ScannerActivity;

import static com.example.eventapp.ui.detail.DetailActivity.REQUEST_CODE_SCANNER;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailFragment extends Fragment {

    private Button btnKehadiran;

    public UserDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnKehadiran = view.findViewById(R.id.btn_detail_user_kehadiran);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fullscreen();

        btnKehadiran.setOnClickListener(onClickKehadiran);
    }

    private View.OnClickListener onClickKehadiran = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ScannerActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCANNER);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCANNER) {
            if (resultCode == ScannerActivity.RESULT_CODE_SCANNER) {
                SuccessDialogFragment successDialogFragment = new SuccessDialogFragment();
                successDialogFragment.show(getChildFragmentManager(), successDialogFragment.getTag());
                btnKehadiran.setEnabled(false);
            }
        }
    }


    public void fullscreen() {
        View v = getActivity().getWindow().getDecorView();
        int options = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        v.setSystemUiVisibility(options);
    }
}
