package com.example.eventapp.ui.detail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventapp.R;
import com.example.eventapp.data.local.Event;
import com.example.eventapp.ui.detail.detailForCreator.CreatorDetailFragment;
import com.example.eventapp.ui.detail.detailForUser.UserDetailFragment;
import com.example.eventapp.ui.detail.scanner.ScannerActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    public static final int REQUEST_CODE_SCANNER = 101;
    public static final int USER_DETAIL = 0;
    public static final int CREATOR_DETAIL = 1;
    public static final String DETAIL = "detail";
    public static final String DETAIL_TYPE = "detail_type";
    final long ONE_MEGABYTE = 1024 * 1024;
    private String urlQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        collapsingToolbarLayout = findViewById(R.id.collaps);
        toolbar = findViewById(R.id.toolbar_detail);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        toolbar.setNavigationOnClickListener(onNavToolbarOnClick);
        toolbar.setOnMenuItemClickListener(onMenuToolbarClick);
        collapsingToolbarLayout.setExpandedTitleColor(getColor(R.color.colorAccent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.colorAccent));

        fullscreen();

        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        Event event = getIntent().getParcelableExtra(DETAIL);
        String owner = event.getOwner();
        String title = event.getTitle();
        urlQR = event.getQrCode();

        toolbar.setTitle(title);

        if (userEmail.equals(owner)) {
            CreatorDetailFragment creatorDetailFragment = new CreatorDetailFragment();
            setFragment(creatorDetailFragment);
        } else {
            UserDetailFragment userDetailFragment = new UserDetailFragment();
            setFragment(userDetailFragment);
        }

    }

    Toolbar.OnMenuItemClickListener onMenuToolbarClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_detail_qr: {

                    StorageReference qrCode = firebaseStorage.getReference().child(urlQR);
                    qrCode.getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Scan QR Code");
                                    View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.dialog_qr_code, null);
                                    ImageView imageView = view.findViewById(R.id.iv_dialog_qr);
                                    Glide.with(DetailActivity.this)
                                            .load(bytes)
                                            .into(imageView);

                                    builder.setView(view);
                                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                    break;
                }
            }

            return true;
        }
    };

    void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_detail_layout, fragment)
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
