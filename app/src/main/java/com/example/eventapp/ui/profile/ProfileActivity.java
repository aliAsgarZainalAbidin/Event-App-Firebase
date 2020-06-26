package com.example.eventapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eventapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    ImageButton imageButton;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageButton = findViewById(R.id.btn_profile_back);
        imageButton.setOnClickListener(onBackPressedButton);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        ShimmerFrameLayout shimmerFrameLayout = findViewById(R.id.shimmer_profile_container);
        ConstraintLayout constraintLayout = findViewById(R.id.constraint_profile_container);

        TextView tvOnContainerNama = findViewById(R.id.tv_profile_container_nama);
        TextView tvOnContainerJob = findViewById(R.id.tv_profile_container_jobs);
        TextView tvNama = findViewById(R.id.tv_profile_nama);
        TextView tvTanggal = findViewById(R.id.tv_profile_tanggal);
        TextView tvEducation = findViewById(R.id.tv_profile_education);
        TextView tvAlamat = findViewById(R.id.tv_profile_alamat);
        TextView tvPhone = findViewById(R.id.tv_profile_phone);
        TextView tvEmail = findViewById(R.id.tv_profile_email);
        TextView tvNip = findViewById(R.id.tv_profile_nip);
        TextView tvKantor = findViewById(R.id.tv_profile_kantor);
        TextView tvJob = findViewById(R.id.tv_profile_jobs);
        TextView tvKewaganegaraan = findViewById(R.id.tv_profile_kewarganegaraan);
        TextView tvOverViews = findViewById(R.id.tv_profile_overview);
        ImageView ivProfile = findViewById(R.id.iv_profile_image_photo);

        shimmerFrameLayout.startShimmer();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            DocumentReference userRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            Map<String, Object> data = snapshot.getData();
                            String nama = (String) data.get("nama");
                            String jobs = (String) data.get("job");
                            String tanggal = (String) data.get("tanggal");
                            String education = (String) data.get("alumni");
                            String alamat = (String) data.get("alamat");
                            String phone = (String) data.get("phone");
                            String email = (String) data.get("email");
                            String nip = (String) data.get("nip");
                            String kantor = (String) data.get("kantor");
                            String kewarganegaraan = (String) data.get("kewarganegaraan");
                            String overviews = (String) data.get("overview");
                            String url = (String) data.get("url");

                            StorageReference photoRef = firebaseStorage.getReference().child(url);
                            final long ONE_MEGABYTE = 1024 * 1024;
                            photoRef.getBytes(ONE_MEGABYTE)
                                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            Glide.with(getApplicationContext())
                                                    .load(bytes)
                                                    .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_account_circle_24).error(R.drawable.ic_baseline_account_circle_24))
                                                    .into(ivProfile);
                                        }
                                    });

                            tvOnContainerNama.setText(nama);
                            tvOnContainerJob.setText(jobs);
                            tvNama.setText(nama);
                            tvJob.setText(jobs);
                            tvTanggal.setText(tanggal);
                            tvEducation.setText(education);
                            tvAlamat.setText(alamat);
                            tvPhone.setText(phone);
                            tvEmail.setText(email);
                            tvNip.setText(nip);
                            tvKantor.setText(kantor);
                            tvKewaganegaraan.setText(kewarganegaraan);
                            tvOverViews.setText(overviews);

                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);

                            constraintLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
    }

    private View.OnClickListener onBackPressedButton = v -> onBackPressed();
}
