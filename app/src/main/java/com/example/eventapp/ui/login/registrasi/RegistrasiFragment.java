package com.example.eventapp.ui.login.registrasi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.ui.login.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrasiFragment extends Fragment {

    private Button btnRegistrasi;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etNama;
    private MaterialButton btnTanggal;
    private TextInputEditText etAlumni;
    private TextInputEditText etAlamat;
    private TextInputEditText etPhone;
    private TextInputEditText etNIP;
    private TextInputEditText etKantor;
    private TextInputEditText etJob;
    private TextInputEditText etKewarganegaraan;
    private TextInputEditText etOverview;
    private FirebaseAuth mFirebaseAuth;
    private ImageView ivPhoto;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri selectedImage;

    public static final int RC_PICK_IMAGE_FROM_GALLERY = 1;

    public RegistrasiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrasi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnRegistrasi = view.findViewById(R.id.btn_regis);
        etEmail = view.findViewById(R.id.et_regis_email);
        etPassword = view.findViewById(R.id.et_regist_pass);
        etNama = view.findViewById(R.id.et_regis_nama);
        btnTanggal = view.findViewById(R.id.btn_regis_tanggal);
        etAlumni = view.findViewById(R.id.et_regis_alumni);
        etAlamat = view.findViewById(R.id.et_regis_alamat);
        etPhone = view.findViewById(R.id.et_regis_phone);
        etNIP = view.findViewById(R.id.et_regis_nip);
        etKantor = view.findViewById(R.id.et_regis_kantor);
        etJob = view.findViewById(R.id.et_regis_job);
        etKewarganegaraan = view.findViewById(R.id.et_regis_kewarganegaraan);
        etOverview = view.findViewById(R.id.et_regis_overview);
        ivPhoto = view.findViewById(R.id.iv_regis_photo);
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnRegistrasi.setOnClickListener(regisAccount);
        btnTanggal.setOnClickListener(setBirthDate);
        ivPhoto.setOnClickListener(setPhotoProfile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_PICK_IMAGE_FROM_GALLERY: {
                if (data != null) {
                    selectedImage = data.getData();
                    ivPhoto.setImageURI(selectedImage);
                }
                break;
            }
        }
    }

    View.OnClickListener setBirthDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Selected Date");
            final MaterialDatePicker picker = builder.build();
            picker.show(getChildFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selection -> {
                btnTanggal.setText(picker.getHeaderText());
                btnTanggal.setTextColor(getResources().getColor(R.color.colorBlack));
            });
        }
    };

    View.OnClickListener setPhotoProfile = v -> {
        String[] mimeType = {"image/jpeg", "image/png"};

        Intent intentPickPhotoGallery = new Intent(Intent.ACTION_PICK);
        intentPickPhotoGallery.setType("image/*");
        intentPickPhotoGallery.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
        startActivityForResult(intentPickPhotoGallery, RC_PICK_IMAGE_FROM_GALLERY);
    };

    View.OnClickListener regisAccount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String nama = etNama.getText().toString().trim();
            String tanggal = btnTanggal.getText().toString().trim();
            String alumni = etAlumni.getText().toString().trim();
            String alamat = etAlamat.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String nip = etNIP.getText().toString().trim();
            String kantor = etKantor.getText().toString().trim();
            String job = etJob.getText().toString().trim();
            String kewarganegaraan = etKewarganegaraan.getText().toString().trim();
            String overview = etOverview.getText().toString().trim();
            boolean isEmpty = false;

            if (email.isEmpty()) {
                etEmail.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (password.isEmpty()) {
                etPassword.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (nama.isEmpty()) {
                etNama.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (tanggal.isEmpty()) {
                btnTanggal.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (alumni.isEmpty()) {
                etAlumni.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (alamat.isEmpty()) {
                etAlamat.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (phone.isEmpty()) {
                etPhone.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (nip.isEmpty()) {
                etNIP.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (kantor.isEmpty()) {
                etKantor.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (job.isEmpty()) {
                etJob.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (kewarganegaraan.isEmpty()) {
                etKewarganegaraan.setError(getString(R.string.error_message));
                isEmpty = true;
            }

            if (overview.isEmpty()) {
                etOverview.setError(getString(R.string.error_message));
                isEmpty = true;
            }


            if (!isEmpty) {
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final FirebaseUser mUser = mFirebaseAuth.getCurrentUser();
                                    if (mUser != null) {
                                        mUser.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "Verification email sent to " + mUser.getEmail(), Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                                        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(getActivity(), task1 -> {
                                                    String userUID = mFirebaseAuth.getCurrentUser().getUid();
                                                    uploadData(userUID);
                                                    String photoProfileUrl = "photo/" + userUID + ".jpg";
                                                    createUserData(nama, email, tanggal, alumni, alamat, phone, nip, kantor, job, kewarganegaraan, overview, photoProfileUrl);
                                                    replaceFragmentToLoginFragment();
                                                });
                                    }
                                } else {
                                    Log.w(RegistrasiFragment.class.getSimpleName(), "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    };

    private void uploadData(String userUID) {
        if (selectedImage != null) {
            StorageReference photoProfile = storageReference.child("photo/" + userUID + ".jpg");
            photoProfile.putFile(selectedImage);
        }
    }

    private void createUserData(String nama, String email, String tanggal, String alumni, String alamat, String phone,
                                String nip, String kantor, String job, String kewarganegaraan, String overview, String url) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("nama", nama);
        userData.put("tanggal", tanggal);
        userData.put("alamat", alamat);
        userData.put("alumni", alumni);
        userData.put("phone", phone);
        userData.put("email", email);
        userData.put("nip", nip);
        userData.put("kantor", kantor);
        userData.put("job", job);
        userData.put("kewarganegaraan", kewarganegaraan);
        userData.put("overview", overview);
        userData.put("url", url);

        CollectionReference userRef = FirebaseFirestore.getInstance().collection("users");
        String userUID = mFirebaseAuth.getCurrentUser().getUid();
        userRef.document(userUID)
                .set(userData);
    }

    private void replaceFragmentToLoginFragment() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_main_container, new LoginFragment())
                        .commit();
            }
        }, 2000);
    }
}
