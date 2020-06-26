package com.example.eventapp.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventapp.R;
import com.example.eventapp.data.local.ContributorEvent;
import com.example.eventapp.data.local.Event;
import com.example.eventapp.ui.MainActivity;
import com.example.eventapp.ui.detail.DetailActivity;
import com.example.eventapp.ui.event.EventActivity;
import com.example.eventapp.ui.login.LoginFragment;
import com.example.eventapp.ui.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    MaterialToolbar toolbar;
    FloatingActionButton floatingActionButton;
    ConstraintLayout constraintHeaderDrawer;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    RecyclerView rvEventCreated;
    RecyclerView rvEventParticipant;

    private List<Event> eventList;
    private List<Event> eventCreator;
    private List<Event> eventParticipant;
    private List<String> listIDEvent;
    private List<ContributorEvent> contributorEventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        recyclerView = findViewById(R.id.content_layout).findViewById(R.id.rv_beranda);


        floatingActionButton = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar_layout);

        floatingActionButton.setOnClickListener(fabOnClickListener);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        actionBarDrawerToggle.setToolbarNavigationClickListener(actionBarToggleOnClickListener);

        View header = navigationView.getHeaderView(0);
        constraintHeaderDrawer = header.findViewById(R.id.constraint_header_container);
        CircleImageView civNavHeaderDrawer = header.findViewById(R.id.civ_nav_header_drawer);
        rvEventCreated = header.findViewById(R.id.rv_nav_header_eventdibuat);
        rvEventParticipant = header.findViewById(R.id.rv_nav_header_eventdiikuti);

        TextView tvNavHeaderNama = header.findViewById(R.id.tv_nav_header_nama);
        TextView tvNavHeaderEmail = header.findViewById(R.id.tv_nav_header_email);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        contributorEventList = new ArrayList<>();
        eventList = new ArrayList<>();
        listIDEvent = new ArrayList<>();
        eventCreator = new ArrayList<>();
        eventParticipant = new ArrayList<>();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            DocumentReference userRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
            userRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                Map<String, Object> data = snapshot.getData();
                                String nama = (String) data.get("nama");
                                String email = (String) data.get("email");
                                String photoUrl = (String) data.get("url");

                                tvNavHeaderNama.setText(nama);
                                tvNavHeaderEmail.setText(email);
                                StorageReference photoRef = firebaseStorage.getReference().child(photoUrl);
                                final long ONE_MEGABYTE = 1024 * 1024;
                                photoRef.getBytes(ONE_MEGABYTE)
                                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                Glide.with(getApplicationContext())
                                                        .load(bytes)
                                                        .into(civNavHeaderDrawer);
                                            }
                                        });
                            }
                        }
                    });

            String userEmail = firebaseAuth.getCurrentUser().getEmail();

            firebaseFirestore.collection("contributorEvent")
                    .document(userEmail)
                    .collection("event")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    String idEvent = documentSnapshot.getId();
                                    listIDEvent.add(idEvent);
                                }
                                for (int i = 0; i < listIDEvent.size(); i++) {
                                    firebaseFirestore.collection("events")
                                            .document(listIDEvent.get(i))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Event documentSnapshot = task.getResult().toObject(Event.class);
                                                        Log.d("onComplete", "onComplete: SUCCESS" + documentSnapshot.getTitle());
                                                        eventList.add(documentSnapshot);

                                                    }
                                                    EventAdapter eventAdapter = new EventAdapter();
                                                    eventAdapter.setList(eventList);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                                                    recyclerView.setAdapter(eventAdapter);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("on", "onFailure: " + e);
                                                }
                                            });
                                }

                            }
                        }
                    });

            firebaseFirestore.collection("events")
                    .whereEqualTo("owner", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Event event = doc.toObject(Event.class);
                                    eventCreator.add(event);
                                }
                                NavEventCreatedAdapter navEventCreatedAdapter = new NavEventCreatedAdapter();
                                navEventCreatedAdapter.setList(eventCreator);
                                rvEventCreated.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                                rvEventCreated.setAdapter(navEventCreatedAdapter);
                            }
                        }
                    });

            Exception exception = firebaseFirestore.collection("contributorEvent")
                    .document()
                    .collection("event")
                    .whereEqualTo("level",1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Event event = doc.toObject(Event.class);
                                    Log.d("TAG", "onComplete: "+event.getTitle());
                                    eventParticipant.add(event);
                                }
                                NavEventCreatedAdapter navEventCreatedAdapter = new NavEventCreatedAdapter();
                                navEventCreatedAdapter.setList(eventParticipant);
                                rvEventParticipant.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                                rvEventParticipant.setAdapter(navEventCreatedAdapter);
                            }
                        }
                    })
                    .getException();
            Log.d("HomeActivity", "onCreate: "+exception);
        }

        constraintHeaderDrawer.setOnClickListener(headerDrawerOnClickListener);

        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.syncState();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String ID = firebaseAuth.getCurrentUser().getUid();
        Toast.makeText(this, ID, Toast.LENGTH_SHORT)
                .show();

    }

    private View.OnClickListener headerDrawerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
    };

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.settings: {
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.logout: {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }

            return false;
        }
    };

    private View.OnClickListener fabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), EventActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener actionBarToggleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    };

}
