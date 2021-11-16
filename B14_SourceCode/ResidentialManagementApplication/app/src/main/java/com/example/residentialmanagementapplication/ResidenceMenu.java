package com.example.residentialmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResidenceMenu extends AppCompatActivity {

    Button btnComplaintResidence, btnFacilitiesResidence, btnLogoffResidence, btnChangePassword;
    TextView tvWelcomeResidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence_menu);

        btnComplaintResidence = (Button) findViewById(R.id.btnComplaintsResidence);
        btnFacilitiesResidence = (Button) findViewById(R.id.btnFacilities);
        btnLogoffResidence = (Button) findViewById(R.id.btnLogoff);
        tvWelcomeResidence = (TextView) findViewById(R.id.tvWelcomeResidence);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String unit = prefs.getString("unit", null);

        tvWelcomeResidence.setText("Welcome, " + unit);

        btnComplaintResidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResidenceMenu.this, ResidenceComplaintMain.class);
                startActivity(i);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResidenceMenu.this, ChangePassword.class);
                startActivity(i);
            }
        });

        btnLogoffResidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor spEditor = MainActivity.mPreferences.edit();
                spEditor.clear();
                spEditor.apply();
                Intent i = new Intent(ResidenceMenu.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnFacilitiesResidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResidenceMenu.this, FacilityList.class);
                startActivity(i);
            }
        });
    }
}