package com.example.residentialmanagementapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintHolder>{
    private List<Complaint> mComplaints;
    private Context context;
    public static final String EXTRA_COMPLAINT_TITLE = "complaint_title";
    public static final String EXTRA_COMPLAINT_DATETIME = "complaint_datetime";
    public static final String EXTRA_COMPLAINT_DESCRIPTION = "complaint_description";
    public static final String EXTRA_COMPLAINT_STATUS = "complaint_status";
    public static final String EXTRA_COMPLAINT_UNIT = "complaint_unit";
    public static final String EXTRA_COMPLAINT_URL = "complaint_url";
    public static final String EXTRA_COMPLAINT_ID = "complaint_id";
    public static final String EXTRA_COMPLAINT_REMARKS = "complaint_remarks";

    public ComplaintAdapter(List<Complaint>complaints, Context c){
        mComplaints = complaints;
        context = c;
    }

    @NonNull
    @Override
    public ComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater
                .inflate(/*android.R.layout.simple_list_item_1*/
                        R.layout.list_item_complaint, parent, false);
        return new ComplaintAdapter.ComplaintHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintHolder holder, int position) {
        Complaint complaint = mComplaints.get(position);
        //holder.mTitleTextView.setText(complaint.getmTitle());
        holder.bindComplaint(complaint);
    }

    @Override
    public int getItemCount() {
        return mComplaints.size();
    }


    public class ComplaintHolder extends RecyclerView.ViewHolder{

        public TextView mTitleTextView, mDateTimeTextView, mStatusTextView;
        private Complaint mComplaint;

        public void bindComplaint(Complaint complaint){
            mComplaint = complaint;
            mTitleTextView.setText(mComplaint.getmTitle());
            mDateTimeTextView.setText(mComplaint.getmDateTime());
            mStatusTextView.setText(mComplaint.getmStatus());
            if(mComplaint.getmStatus().equals("In Progress")){
                mStatusTextView.setTextColor(Color.parseColor("#FF7F27"));
            }else if (mComplaint.getmStatus().equals("Rejected")){
                mStatusTextView.setTextColor(Color.RED);
            }else if (mComplaint.getmStatus().equals("Solved")){
                mStatusTextView.setTextColor(Color.GREEN);
            }else if (mComplaint.getmStatus().equals("Pending")){
                mStatusTextView.setTextColor(Color.LTGRAY);
            }
        }

        public ComplaintHolder(@NonNull View itemView) {
            super(itemView);

            //mTitleTextView = (TextView) itemView;
            mTitleTextView = itemView.findViewById(R.id.list_item_complaint_title);
            mDateTimeTextView = itemView.findViewById(R.id.list_item_complaint_dateTime);
            mStatusTextView = itemView.findViewById(R.id.list_item_complaint_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ShowComplaintDetailsResidence.class);
                    i.putExtra(EXTRA_COMPLAINT_TITLE, mComplaint.getmTitle());
                    i.putExtra(EXTRA_COMPLAINT_DATETIME, mComplaint.getmDateTime());
                    i.putExtra(EXTRA_COMPLAINT_STATUS, mComplaint.getmStatus());
                    i.putExtra(EXTRA_COMPLAINT_DESCRIPTION, mComplaint.getmIssueDescription());
                    i.putExtra(EXTRA_COMPLAINT_UNIT, mComplaint.getmUnit());
                    i.putExtra(EXTRA_COMPLAINT_URL, mComplaint.getmUrl());
                    i.putExtra(EXTRA_COMPLAINT_ID, mComplaint.getmId());
                    i.putExtra(EXTRA_COMPLAINT_REMARKS, mComplaint.getmRemarks());

                    context.startActivity(i);
                }
            });
        }
    }
}