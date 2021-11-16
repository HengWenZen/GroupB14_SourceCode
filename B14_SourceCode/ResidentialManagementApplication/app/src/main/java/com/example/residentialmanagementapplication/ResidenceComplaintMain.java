package com.example.residentialmanagementapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResidenceComplaintMain extends AppCompatActivity {

    Button btnViewYourComplaint, btnNewComplaint;
    RecyclerView rv;
    ComplaintAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence_complaint_main);

        btnNewComplaint = (Button) findViewById(R.id.btnNewComplaint);
        btnViewYourComplaint = (Button) findViewById(R.id.btnViewYourComplaints);
        rv =(RecyclerView)findViewById(R.id.myRecyclerView);
        //SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        //String unit = prefs.getString("unit", null);

        btnNewComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResidenceComplaintMain.this, ResidenceNewComplaint.class);
                startActivity(i);
            }
        });

        btnViewYourComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResidenceComplaintMain.this, ViewYourComplaintResidence.class);
                startActivity(i);
            }
        });

        ComplaintLab complaintLab = ComplaintLab.get(this);
        List<Complaint> complaints = complaintLab.getComplaints();

        //FilteredComplaintLab sComplaintLab = FilteredComplaintLab.get(this, unit);
        //List<Complaint> zcomplaints = sComplaintLab.getFilteredComplaints();

        ProgressDialog dialog = ProgressDialog.show(ResidenceComplaintMain.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(ResidenceComplaintMain.this));
                adapter = new ComplaintAdapter(complaints, ResidenceComplaintMain.this);
                rv.setAdapter(adapter);
            }
        }, 2000);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}