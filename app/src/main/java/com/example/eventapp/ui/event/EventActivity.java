package com.example.eventapp.ui.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.data.local.ContributorEvent;
import com.example.eventapp.data.local.Event;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity {

    private MaterialButton btnWaktuAwal;
    private MaterialButton btnWaktuAkhir;
    private MaterialButton btnTanggal;
    private MaterialButton btnBerkas;
    private TextInputEditText etPeserta;
    private TextInputEditText etTitle;
    private TextInputEditText etOverview;
    private TextInputEditText etRuangan;
    private TextInputEditText etAlamat;
    private TextInputEditText etGedung;
    private ChipGroup chipGroupEmail;
    private ChipGroup chipGroupBerkas;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Toolbar toolbar;
    private GoogleMap map;
    private Button btnCreate;
    private FirebaseFirestore db;
    public static final String EDIT_EVENT = "edit_event";
    public static final String TITLE_EVENT = "title_event";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int RC_PDF = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = findViewById(R.id.toolbar_event_layout);
        btnCreate = findViewById(R.id.btn_event_create);
        chipGroupEmail = findViewById(R.id.cg_event_email);
        chipGroupBerkas = findViewById(R.id.cg_event_berkas);
        etPeserta = findViewById(R.id.et_event_peserta);
        btnBerkas = findViewById(R.id.btn_event_berkas);
        etTitle = findViewById(R.id.et_event_title);
        etOverview = findViewById(R.id.et_event_overview);
        etRuangan = findViewById(R.id.et_event_ruangan);
        etAlamat = findViewById(R.id.et_event_alamat);
        etGedung = findViewById(R.id.et_event_gedung);
        btnWaktuAwal = findViewById(R.id.btn_event_waktu_awal);
        btnWaktuAkhir = findViewById(R.id.btn_event_waktu_akhir);
        btnTanggal = findViewById(R.id.btn_event_tanggal_awal);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        db = FirebaseFirestore.getInstance();

        if (getIntent().getBooleanExtra(EDIT_EVENT, false)) {
            toolbar.setTitle(getIntent().getStringExtra(TITLE_EVENT));
            btnCreate.setText(R.string.selesai);
        } else {
            btnCreate.setText(R.string.buat);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnWaktuAwal.setOnClickListener(waktuAwalOnClickListener);
        btnWaktuAkhir.setOnClickListener(waktuAkhirOnClickListener);
        btnTanggal.setOnClickListener(tanggalOnClickListener);
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)){
            btnCreate.setOnClickListener(createEventOnClicklistener);
        }
        etPeserta.addTextChangedListener(addChipPesertaTextWatcher);
        btnBerkas.setOnClickListener(addBerkas);

    }

    View.OnClickListener addBerkas = v -> {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 100);
    };


    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_PDF: {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri file = data.getData();
                    String path = file.getPath();
                    Cursor cursor = getContentResolver().query(file, null, null, null, null);
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    String nameFile = cursor.getString(nameIndex);
                    String newPath = path.replaceFirst(".*:", "");
                    Chip chip = createNewChip(newPath);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupBerkas.removeView(v);
                        }
                    });
                    chipGroupBerkas.addView(chip);
                }
                break;
            }
        }
    }

    TextWatcher addChipPesertaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            if (text.contains(" ")) {
                Chip chip = createNewChip(s.toString());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroupEmail.removeView(chip);
                    }
                });
                chipGroupEmail.addView(chip);
                s.clear();
            }
        }
    };

    Chip createNewChip(String text) {
        Chip chip = new Chip(EventActivity.this);
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);
        chip.setClickable(true);
        return chip;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener createEventOnClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = etTitle.getText().toString();
            String overview = etOverview.getText().toString();
            String ruangan = etRuangan.getText().toString();
            String alamat = etAlamat.getText().toString();
            String gedung = etGedung.getText().toString();
            String waktu = btnWaktuAwal.getText().toString() + " s/d " + btnWaktuAkhir.getText().toString();
            String tanggal = btnTanggal.getText().toString();
            boolean isEmpty = false;

            List<String> listEmail = new ArrayList<>();
            int indexCgEmail = chipGroupEmail.getChildCount();
            for (int i = 0; i < indexCgEmail; i++) {
                Chip chip = (Chip) chipGroupEmail.getChildAt(i);
                listEmail.add(chip.getText().toString());
            }

            if (title.isEmpty()) {
                etTitle.setError(getResources().getString(R.string.error_message));
                isEmpty = true;
            }
            if (overview.isEmpty()) {
                etOverview.setError(getResources().getString(R.string.error_message));
                isEmpty = true;
            }
            if (ruangan.isEmpty()) {
                etRuangan.setError(getResources().getString(R.string.error_message));
                isEmpty = true;
            }
            if (alamat.isEmpty()) {
                etAlamat.setError(getResources().getString(R.string.error_message));
                isEmpty = true;
            }
            if (gedung.isEmpty()) {
                etGedung.setError(getResources().getString(R.string.error_message));
                isEmpty = true;
            }

            if (tanggal.isEmpty()) {
                btnTanggal.setError(getResources().getString(R.string.error_message));
                isEmpty = true;
            } else {
                btnTanggal.setError(null);
            }

            if (!isEmpty) {
                String userEmail = firebaseAuth.getCurrentUser().getEmail();

                Event event = new Event(title, overview, ruangan, alamat, gedung, waktu, tanggal, listEmail, null, userEmail, null);

                ContributorEvent contributorEventAsCreator = new ContributorEvent(0, false);
                ContributorEvent contributorEventAsParticipant = new ContributorEvent(1, false);

                WriteBatch batch = db.batch();
                DocumentReference events = db.collection("events").document();

                events.set(event)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DocumentReference contributorAsCreator = db.collection("contributorEvent")
                                        .document(userEmail)
                                        .collection("event")
                                        .document(events.getId());

                                for (int i = 0; i < listEmail.size(); i++) {
                                    DocumentReference contributorAsParticipant = db.collection("contributorEvent")
                                            .document(listEmail.get(i))
                                            .collection("event")
                                            .document(events.getId());
                                    batch.set(contributorAsParticipant, contributorEventAsParticipant);
                                }

                                batch.set(contributorAsCreator, contributorEventAsCreator);
                                batch.commit()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(EventActivity.this, "Event Berhasil dibuat", Toast.LENGTH_SHORT)
                                                        .show();
                                                onBackPressed();
                                            }
                                        });

                                Uri QRCode = createQRCode(events.getId());
                                String path = "QRCode/" + events.getId() + ".jpg";
                                StorageReference qrRef = storageReference.child(path);
                                qrRef.putFile(QRCode)
                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    events.update("qrCode", path);
                                                }
                                            }
                                        });
                            }
                        });
            }
        }
    };

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{permission},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    Uri createQRCode(String code) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return getImageUri(EventActivity.this, bitmap);
    }

    private View.OnClickListener tanggalOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Selected Date");
            final MaterialDatePicker picker = builder.build();
            picker.show(getSupportFragmentManager(), picker.toString());

            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    btnTanggal.setText(picker.getHeaderText());
                }
            });
        }
    };

    private View.OnClickListener waktuAwalOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String text = hourOfDay + ":" + minute;
                            btnWaktuAwal.setText(text);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    };

    private View.OnClickListener waktuAkhirOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String text = hourOfDay + ":" + minute;
                            btnWaktuAkhir.setText(text);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    };

    //Use when u only have toolbar on xml, not Appbar as parent on toolbar
    private View.OnClickListener navOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

}
