package com.example.residentialmanagementapplication;

public class Booking {
    private String id;
    private String facilityName;
    private String date;
    private String slot;
    private String unit;


    public Booking(String id, String facilityName, String date, String slot) {
        this.id = id;
        this.facilityName = facilityName;
        this.date = date;
        this.slot = slot;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
}
