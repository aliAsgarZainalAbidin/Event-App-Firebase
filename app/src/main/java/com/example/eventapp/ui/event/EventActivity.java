package com.example.eventapp.ui.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MaterialButton btnWaktuAwal;
    private MaterialButton btnWaktuAkhir;
    private MaterialButton btnTanggal;
    Toolbar toolbar;
    private GoogleMap map;
    private Button btnCreate;
    private FirebaseFirestore db;
    public static final String EDIT_EVENT = "edit_event";
    public static final String TITLE_EVENT = "title_event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = findViewById(R.id.toolbar_event_layout);
        btnWaktuAwal = findViewById(R.id.btn_event_waktu_awal);
        btnWaktuAkhir = findViewById(R.id.btn_event_waktu_akhir);
        btnTanggal = findViewById(R.id.btn_event_tanggal_awal);
        btnCreate = findViewById(R.id.btn_event_create);
        db = FirebaseFirestore.getInstance();

        if (getIntent().getBooleanExtra(EDIT_EVENT, false)){
            toolbar.setTitle(getIntent().getStringExtra(TITLE_EVENT));
            btnCreate.setText(R.string.selesai);
        } else {
            btnCreate.setText(R.string.buat);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnWaktuAwal.setOnClickListener(waktuAwalOnClickListener);
        btnWaktuAkhir.setOnClickListener(waktuAkhirOnClickListener);
        btnTanggal.setOnClickListener(tanggalOnClickListener);
        btnCreate.setOnClickListener(createEventOnClicklistener);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener createEventOnClicklistener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, Object> user = new HashMap<>();
            user.put("nama","Ali");
            user.put("umur","Asgar");

            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),"Success added with ID", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed added with ID", Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("EventActivity", "onFailure: "+e);
                        }
                    });
            
        }
    };

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

    //Used when u only have toolbar on xml, not Appbar as parent on toolbar
    private View.OnClickListener navOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
