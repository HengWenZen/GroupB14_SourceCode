package com.example.residentialmanagementapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateComplaintProgressAdmin extends AppCompatActivity {

    Spinner mSpStatus;
    EditText mEtRemarks;
    Button mBtnSubmitUpdateProgress;
    public static final String EXTRA_COMPLAINT_ID = "complaint_id";
    Firebase database = new Firebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_complaint_progress_admin);

        mSpStatus = (Spinner) findViewById(R.id.spStatus);
        mEtRemarks = (EditText) findViewById(R.id.etRemarksUpdateComplaint);
        mBtnSubmitUpdateProgress = (Button) findViewById(R.id.btnSubmitUpdateProgress);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(UpdateComplaintProgressAdmin.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.status));
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpStatus.setAdapter(unitAdapter);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String id = (String) b.get(EXTRA_COMPLAINT_ID);

        mBtnSubmitUpdateProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEtRemarks.getText().toString().equals(""))
                {
                    Toast.makeText(UpdateComplaintProgressAdmin.this, "All Fields Must Be Filled...", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(UpdateComplaintProgressAdmin.this)
                            .setTitle("Update Confirmation")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.getData("complaint", null, new MyCallback() {
                                        @Override
                                        public void returnData(ArrayList<Map<String, Object>> docList) {
                                            Log.d("firebase example", docList.toString());
                                            ArrayList<String> list = new ArrayList<>();

                                            for (Map<String, Object> map : docList) {
                                                if (map.get("id").toString().equals(id)) {
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("status", mSpStatus.getSelectedItem().toString());
                                                    user.put("remarks", mEtRemarks.getText().toString());
                                                    database.updData("complaint", user, map.get("id").toString());
                                                    Toast.makeText(UpdateComplaintProgressAdmin.this, "Update Successfully...", Toast.LENGTH_SHORT).show();
                                                    Intent z = new Intent(UpdateComplaintProgressAdmin.this, ShowComplaintDetailsResidence.class);
                                                    startActivity(z);
                                                    finish();
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(UpdateComplaintProgressAdmin.this, "Update Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.show();
                }
            }
        });
    }
}