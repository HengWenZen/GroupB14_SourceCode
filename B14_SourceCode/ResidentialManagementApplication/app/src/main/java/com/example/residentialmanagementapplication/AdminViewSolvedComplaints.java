package com.example.residentialmanagementapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import java.util.List;

public class AdminViewSolvedComplaints extends AppCompatActivity {

    RecyclerView rv;
    ComplaintAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_solved_complaints);

        rv = (RecyclerView)findViewById(R.id.adminViewSolvedComplaints);

        SolvedComplaintLab complaintLab = SolvedComplaintLab.get(this);
        List<Complaint> complaints = complaintLab.getSolvedComplaints();

        //FilteredComplaintLab sComplaintLab = FilteredComplaintLab.get(this, unit);
        //List<Complaint> zcomplaints = sComplaintLab.getFilteredComplaints();

        ProgressDialog dialog = ProgressDialog.show(AdminViewSolvedComplaints.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(AdminViewSolvedComplaints.this));
                adapter = new ComplaintAdapter(complaints, AdminViewSolvedComplaints.this);
                rv.setAdapter(adapter);
            }
        }, 2000);
    }
}