package com.example.residentialmanagementapplication;

import java.util.UUID;

public class Complaint {
    private String mId;
    private String mTitle;
    private String mDateTime;
    private String mStatus;
    private String mIssueDescription;
    private String mUnit;
    private String mUrl;
    private String mRemarks;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Complaint(){
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmIssueDescription() {
        return mIssueDescription;
    }

    public void setmIssueDescription(String mIssueDescription) {
        this.mIssueDescription = mIssueDescription;
    }

    public String getmUnit() {
        return mUnit;
    }

    public void setmUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmRemarks() {
        return mRemarks;
    }

    public void setmRemarks(String mRemarks) {
        this.mRemarks = mRemarks;
    }
}
