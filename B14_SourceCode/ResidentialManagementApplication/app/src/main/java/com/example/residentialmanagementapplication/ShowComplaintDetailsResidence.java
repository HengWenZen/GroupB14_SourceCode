package com.example.residentialmanagementapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ShowComplaintDetailsResidence extends AppCompatActivity {

    TextView mTvDateContent, mTvDescriptionContent, mTvStatusContent, mTvIdContent, mTvRemarksContent;
    Button mBtnDeleteComplaintResidence, mBtnUpdateProgressAdmin;
    ImageView mIvPictureEvidenceContent;
    StorageReference storageReference;
    Firebase database = new Firebase();
    public static final String EXTRA_COMPLAINT_TITLE = "complaint_title";
    public static final String EXTRA_COMPLAINT_DATETIME = "complaint_datetime";
    public static final String EXTRA_COMPLAINT_DESCRIPTION = "complaint_description";
    public static final String EXTRA_COMPLAINT_STATUS = "complaint_status";
    public static final String EXTRA_COMPLAINT_UNIT = "complaint_unit";
    public static final String EXTRA_COMPLAINT_URL = "complaint_url";
    public static final String EXTRA_COMPLAINT_ID = "complaint_id";
    public static final String EXTRA_COMPLAINT_REMARKS = "complaint_remarks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complaint_details_residence);

        mTvDateContent = (TextView) findViewById(R.id.tvDateTimeContent);
        mTvStatusContent = (TextView) findViewById(R.id.tvStatusContent);
        mTvDescriptionContent = (TextView) findViewById(R.id.tvIssueDescriptionContent);
        mTvRemarksContent = (TextView) findViewById(R.id.tvRemarksContent);
        mBtnDeleteComplaintResidence = (Button) findViewById(R.id.btnDeleteComplaintResidence);
        mBtnUpdateProgressAdmin = (Button) findViewById(R.id.btnUpdateProgressAdmin);
        mIvPictureEvidenceContent = (ImageView) findViewById(R.id.ivPictureEvidenceContent);
        mTvIdContent = (TextView) findViewById(R.id.tvIdContent);
        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String unit = prefs.getString("unit", null);
        String employeeName = prefs.getString("employeename", null);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        mTvDateContent.setText((String)b.get(EXTRA_COMPLAINT_DATETIME));
        mTvDescriptionContent.setText((String)b.get(EXTRA_COMPLAINT_DESCRIPTION));
        mTvIdContent.setText((String)b.get(EXTRA_COMPLAINT_ID));
        String id = (String) b.get(EXTRA_COMPLAINT_ID);
        String s = (String)b.get(EXTRA_COMPLAINT_STATUS);
        String v = (String)b.get(EXTRA_COMPLAINT_UNIT);
        String imageId = (String)b.get(EXTRA_COMPLAINT_URL);
        String remarks = (String)b.get(EXTRA_COMPLAINT_REMARKS);

        if (remarks.equals(""))
        {
            mTvRemarksContent.setText("No Remarks");
            mTvRemarksContent.setTextColor(Color.RED);
        }else {
            mTvRemarksContent.setText(remarks);
            mTvRemarksContent.setTextColor(Color.BLUE);
        }

        mTvStatusContent.setText(s);
        if(s.equals("In Progress")){
            mTvStatusContent.setTextColor(Color.parseColor("#FF7F27"));
        }else if (s.equals("Rejected")){
            mTvStatusContent.setTextColor(Color.RED);
        }else if (s.equals("Solved")){
            mTvStatusContent.setTextColor(Color.GREEN);
        }else if (s.equals("Pending")){
            mTvStatusContent.setTextColor(Color.LTGRAY);
        }

        if (employeeName == null || s.equals("Solved") || s.equals("Rejected"))
        {
            mBtnUpdateProgressAdmin.setVisibility(View.INVISIBLE);
            mBtnUpdateProgressAdmin.setEnabled(false);
        }
        else{
            mBtnUpdateProgressAdmin.setVisibility(View.VISIBLE);
            mBtnUpdateProgressAdmin.setEnabled(true);
        }

        if (v.equals(unit)){
            mBtnDeleteComplaintResidence.setEnabled(true);
            mBtnDeleteComplaintResidence.setVisibility(View.VISIBLE);
        }else {
            mBtnDeleteComplaintResidence.setEnabled(false);
            mBtnDeleteComplaintResidence.setVisibility(View.INVISIBLE);
        }

        mBtnUpdateProgressAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(ShowComplaintDetailsResidence.this, UpdateComplaintProgressAdmin.class);
                z.putExtra(EXTRA_COMPLAINT_ID, id);
                startActivity(z);
                finish();
            }
        });

        mBtnDeleteComplaintResidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ShowComplaintDetailsResidence.this)
                        .setTitle("Delete Confirmation")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                                        storageReference = FirebaseStorage.getInstance().getReference();
                                                        StorageReference ref
                                                                = storageReference
                                                                .child(
                                                                        "images/"
                                                                                + imageId);
                                                        ref.delete();
                                                        Toast.makeText(ShowComplaintDetailsResidence.this, "Complaint Deleted", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(ShowComplaintDetailsResidence.this, ResidenceComplaintMain.class);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ShowComplaintDetailsResidence.this, "DELETE FAILED", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ShowComplaintDetailsResidence.this, "Delete Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference ref
                = storageReference
                .child(
                        "images/"
                                + imageId);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            ref.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            mIvPictureEvidenceContent.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}