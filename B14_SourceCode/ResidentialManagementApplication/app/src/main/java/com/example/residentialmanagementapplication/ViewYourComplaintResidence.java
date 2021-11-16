package com.example.residentialmanagementapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewYourComplaintResidence extends AppCompatActivity {

    RecyclerView rv;
    ComplaintAdapter adapter;
    Firebase database = new Firebase();
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_your_complaint_residence);

        rv = (RecyclerView)findViewById(R.id.rvViewYourComplaint);
        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String unit = prefs.getString("unit", null);

        FilteredComplaintLab filteredComplaintLab = FilteredComplaintLab.get(this, unit);
        List<Complaint> complaints = filteredComplaintLab.getFilteredComplaints();

        ProgressDialog dialog = ProgressDialog.show(ViewYourComplaintResidence.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(ViewYourComplaintResidence.this));
                adapter = new ComplaintAdapter(complaints, ViewYourComplaintResidence.this);
                rv.setAdapter(adapter);
            }
        }, 2000);

        ItemTouchHelper helper = new ItemTouchHelper((new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("Are you sure you want to delete this complaint?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = complaints.get(viewHolder.getAdapterPosition()).getmId();
                                String url = complaints.get(viewHolder.getAdapterPosition()).getmUrl();

                                storageReference = FirebaseStorage.getInstance().getReference();
                                StorageReference ref
                                        = storageReference
                                        .child(
                                                "images/"
                                                        + url);
                                ref.delete();

                                database.getData("complaint", null, new MyCallback() {
                                    @Override
                                    public void returnData(ArrayList<Map<String, Object>> docList) {
                                        Log.d("firebase example", docList.toString());
                                        ArrayList<String> list = new ArrayList<>();

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("complaint").document(id)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(ViewYourComplaintResidence.this, "DELETED", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ViewYourComplaintResidence.this, "DELETE FAILED", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });

                                complaints.remove(viewHolder.getAdapterPosition());
                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ViewYourComplaintResidence.this, "DELETE CANCEL", Toast.LENGTH_SHORT).show();
                                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        }).create().show();
            }
        }));
        helper.attachToRecyclerView(rv);
    }
}