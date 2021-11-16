package com.example.residentialmanagementapplication;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SolvedComplaintLab {
    private static SolvedComplaintLab sComplaintLab;
    private List<Complaint> mComplaints;
    Firebase database = new Firebase();

    public static SolvedComplaintLab get(Context context){

        sComplaintLab = new SolvedComplaintLab(context);
        return sComplaintLab;
    }

    private SolvedComplaintLab(Context context) {
        mComplaints = new ArrayList<>();
        mComplaints.clear();

        database.getData("complaint", null, new com.example.residentialmanagementapplication.MyCallback() {
            @Override
            public void returnData(ArrayList<Map<String, Object>> docList) {
                Log.d("firebase example", docList.toString());
                ArrayList<String> list = new ArrayList<>();

                for (Map<String, Object> map : docList) {
                    if(!(map.get("status").toString().equals("In Progress")||map.get("status").toString().equals("Pending")))
                    {
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
                    }
//                            Log.d("firebase 12",map.toString());
//                            Log.d("firebase 12", (String) map.get("firstName"));
                    //list.add(map.get("firstName").toString() + " " + map.get("lastName").toString());
                }
            }
        });
    }

    public List<Complaint> getSolvedComplaints(){
        return mComplaints;
    }

    public Complaint getSolvedComplaint(UUID id){
        for (Complaint complaint : mComplaints){
            if (complaint.getmId().equals(id)){
                return complaint;
            }
        }
        return null;
    }
}
