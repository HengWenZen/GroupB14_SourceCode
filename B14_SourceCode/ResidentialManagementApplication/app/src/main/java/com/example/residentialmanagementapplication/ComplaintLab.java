package com.example.residentialmanagementapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ComplaintLab {
    private static ComplaintLab sComplaintLab;
    private List<Complaint> mComplaints;
    Firebase database = new Firebase();

    public static ComplaintLab get(Context context){

        sComplaintLab = new ComplaintLab(context);
        return sComplaintLab;
    }

    private ComplaintLab(Context context) {
        mComplaints = new ArrayList<>();
        mComplaints.clear();

        database.getData("complaint", null, new com.example.residentialmanagementapplication.MyCallback() {
            @Override
            public void returnData(ArrayList<Map<String, Object>> docList) {
                Log.d("firebase example", docList.toString());
                ArrayList<String> list = new ArrayList<>();

                for (Map<String, Object> map : docList) {
                    Complaint complaint = new Complaint();
                    complaint.setmTitle(map.get("issueTitle").toString());
                    complaint.setmDateTime(map.get("dateTime").toString());
                    complaint.setmStatus(map.get("status").toString());
                    complaint.setmIssueDescription(map.get("issueDescription").toString());
                    complaint.setmUnit(map.get("unit").toString());
                    complaint.setmUrl(map.get("pictureEvidenceUrl").toString());
                    complaint.setmId(map.get("id").toString());
                    complaint.setmRemarks(map.get("remarks").toString());
                    mComplaints.add(complaint);

//                            Log.d("firebase 12",map.toString());
//                            Log.d("firebase 12", (String) map.get("firstName"));
                    //list.add(map.get("firstName").toString() + " " + map.get("lastName").toString());
                }
            }
        });
    }

    public List<Complaint> getComplaints(){
        return mComplaints;
    }

    public Complaint getComplaint(UUID id){
        for (Complaint complaint : mComplaints){
            if (complaint.getmId().equals(id)){
                return complaint;
            }
        }
        return null;
    }
}
