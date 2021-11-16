package com.example.residentialmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminMenu extends AppCompatActivity {

    Button btnComplaintAdmin, btnFacilitiesAdmin, btnLogoffAdmin, btnChangePasswordAdmin;
    TextView tvWelcomeAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        btnComplaintAdmin = (Button) findViewById(R.id.btnComplaintsAdminMenu);
        btnFacilitiesAdmin = (Button) findViewById(R.id.btnFacilitiesAdminMenu);
        btnLogoffAdmin = (Button) findViewById(R.id.btnLogoffAdminMenu);
        tvWelcomeAdmin = (TextView) findViewById(R.id.tvWelcomeAdmin);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String employeeName = prefs.getString("employeename", null);

        tvWelcomeAdmin.setText("Welcome, " + employeeName);

        btnComplaintAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminMenu.this, AdminComplaintMain.class);
                startActivity(i);
            }
        });

        btnLogoffAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor spEditor = MainActivity.mPreferences.edit();
                spEditor.clear();
                spEditor.apply();
                Intent i = new Intent(AdminMenu.this, AdminLogin.class);
                startActivity(i);
                finish();
            }
        });

        btnFacilitiesAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminMenu.this , AdminViewBooking.class);
                startActivity(i);
            }
        });
    }
}