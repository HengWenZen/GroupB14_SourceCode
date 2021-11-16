package com.example.residentialmanagementapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.effect.Effect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResidenceNewComplaint extends AppCompatActivity {

    private ImageView pictureEvidence;
    private Button btnChoosePicture, btnSubmitNewComplaint;
    public Uri imageUri;
    private StorageReference storageReference;
    private EditText etIssueDescription, etIssueTitle;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    String generatedFilePath;
    String randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence_new_complaint);

        pictureEvidence = findViewById(R.id.ivPictureEvidence);
        btnChoosePicture = (Button) findViewById(R.id.btnChoosePicture);
        btnSubmitNewComplaint = (Button) findViewById(R.id.btnSubmitNewComplaint);
        etIssueTitle = (EditText) findViewById(R.id.etIssueTitle);
        etIssueDescription = (EditText) findViewById(R.id.etIssueDescription);
        storageReference = FirebaseStorage.getInstance().getReference();

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String unit = prefs.getString("unit", null);

        btnChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        btnSubmitNewComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null && (!etIssueTitle.getText().toString().equals("")) && (!etIssueDescription.getText().toString().equals("")))
                {
                    uploadPicture();
                    String issueTitle = etIssueTitle.getText().toString();
                    String issueDescription = etIssueDescription.getText().toString();
                    Calendar currentTime = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                    String strDate = dateFormat.format(currentTime.getTime());
                    Map<String, Object> user = new HashMap<>();
                    user.put("issueTitle", issueTitle);
                    user.put("issueDescription", issueDescription);
                    user.put("pictureEvidenceUrl", randomNumber);
                    user.put("dateTime", strDate);
                    user.put("unit", unit);
                    user.put("status", "Pending");
                    user.put("remarks", "");
                    database.collection("complaint")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Intent i = new Intent(ResidenceNewComplaint.this, ResidenceComplaintMain.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                }
                            });
                }else{
                    Toast.makeText(ResidenceNewComplaint.this, "Please Fill In All Field !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadPicture() {

        if (imageUri != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            randomNumber = UUID.randomUUID().toString();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + randomNumber);

            // adding listeners on upload
            // or failure of image
            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(ResidenceNewComplaint.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                                    if(downloadUri.isSuccessful()) {
                                        generatedFilePath = downloadUri.getResult().toString();
                                    }
                                    else
                                    {
                                        generatedFilePath = "wow";
                                    }
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(ResidenceNewComplaint.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    private void choosePicture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null&&data.getData()!=null){
            imageUri = data.getData();
            pictureEvidence.setImageURI(imageUri);
        }
    }
}