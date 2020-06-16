package com.example.eventapp.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.eventapp.R;

public class ProfileActivity extends AppCompatActivity {

    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageButton = findViewById(R.id.btn_profile_back);

        imageButton.setOnClickListener(onBackPressedButton);
    }

    private View.OnClickListener onBackPressedButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
}
