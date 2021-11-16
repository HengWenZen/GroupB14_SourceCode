package com.example.residentialmanagementapplication;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FilteredComplaintLab {
    private static FilteredComplaintLab sComplaintLab;
    private List<Complaint> mComplaints;
    Firebase database = new Firebase();

    public static FilteredComplaintLab get(Context context, String unit){

        sComplaintLab = new FilteredComplaintLab(context, unit);
        return sComplaintLab;
    }

    private FilteredComplaintLab(Context context, String unit) {
        mComplaints = new ArrayList<>();
        mComplaints.clear();

        database.getData("complaint", null, new com.example.residentialmanagementapplication.MyCallback() {
            @Override
            public void returnData(ArrayList<Map<String, Object>> docList) {
                Log.d("firebase example", docList.toString());
                ArrayList<String> list = new ArrayList<>();

                for (Map<String, Object> map : docList) {
                    if(map.get("unit").toString().equals(unit))
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

    public List<Complaint> getFilteredComplaints(){
        return mComplaints;
    }

    public Complaint getFilteredComplaint(UUID id){
        for (Complaint complaint : mComplaints){
            if (complaint.getmId().equals(id)){
                return complaint;
            }
        }
        return null;
    }
}
