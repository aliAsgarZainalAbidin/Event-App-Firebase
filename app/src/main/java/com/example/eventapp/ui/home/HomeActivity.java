package com.example.eventapp.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.ui.detail.DetailActivity;
import com.example.eventapp.ui.event.EventActivity;
import com.example.eventapp.ui.profile.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    ConstraintLayout constraintHeaderDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        floatingActionButton = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_mdi_menu);
        floatingActionButton.setOnClickListener(fabOnClickListener);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        actionBarDrawerToggle.setToolbarNavigationClickListener(actionBarToggleOnClickListener);

        View header = navigationView.getHeaderView(0);
        constraintHeaderDrawer = header.findViewById(R.id.constraint_header_container);
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
