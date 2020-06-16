package com.example.eventapp.ui.detail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.ui.detail.detailForCreator.CreatorDetailFragment;
import com.example.eventapp.ui.detail.detailForUser.UserDetailFragment;
import com.example.eventapp.ui.detail.scanner.ScannerActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

public class DetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    public static final int REQUEST_CODE_SCANNER = 101;
    public static final int USER_DETAIL = 0;
    public static final int CREATOR_DETAIL = 1;
    public static final String DETAIL_TYPE = "detail_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        collapsingToolbarLayout = findViewById(R.id.collaps);
        toolbar = findViewById(R.id.toolbar_detail);

        toolbar.setNavigationOnClickListener(onNavToolbarOnClick);
        collapsingToolbarLayout.setExpandedTitleColor(getColor(R.color.colorAccent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.colorAccent));

        fullscreen();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_detail_layout, new UserDetailFragment())
                .commit();
    }


    private View.OnClickListener onNavToolbarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public void fullscreen() {
        View v = getWindow().getDecorView();
        int options = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        v.setSystemUiVisibility(options);
    }
}
