package com.example.residentialmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class AdminComplaintMain extends AppCompatActivity {

    RecyclerView rv;
    ComplaintAdapter adapter;
    Button mBtnViewSolvedComplaintsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complaint_main);

        mBtnViewSolvedComplaintsAdmin = (Button) findViewById(R.id.btnViewSolvedComplaints);
        rv = (RecyclerView)findViewById(R.id.adminRecyclerView);

        UnsolvedComplaintLab complaintLab = UnsolvedComplaintLab.get(this);
        List<Complaint> complaints = complaintLab.getUnsolvedComplaints();

        //FilteredComplaintLab sComplaintLab = FilteredComplaintLab.get(this, unit);
        //List<Complaint> zcomplaints = sComplaintLab.getFilteredComplaints();

        ProgressDialog dialog = ProgressDialog.show(AdminComplaintMain.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(AdminComplaintMain.this));
                adapter = new ComplaintAdapter(complaints, AdminComplaintMain.this);
                rv.setAdapter(adapter);
            }
        }, 2000);

        mBtnViewSolvedComplaintsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminComplaintMain.this, AdminViewSolvedComplaints.class);
                startActivity(i);
                finish();
            }
        });
    }
}