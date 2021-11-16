package com.example.residentialmanagementapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AdminViewBooking extends AppCompatActivity {
    private ViewBookingAdapter mAdapter;
    private RecyclerView rv_AdminViewBooking;
    Button mbtn_search;
    Spinner mSpinner_facilityList,mSpinner_unitList;

    List<String> list = new ArrayList<>();
    List<Booking> mBookingList;
    ArrayList<Booking> allBookingList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_booking);

        rv_AdminViewBooking = findViewById(R.id.rv_AdminViewBooking);
        mSpinner_facilityList = findViewById(R.id.spinner_facilityList);
//        mSpinner_unitList = findViewById(R.id.spinner_unitList);
        mbtn_search = findViewById(R.id.btn_search);


//        ArrayList<String> s = new ArrayList<>();
//        s.add("All");
//        s.addAll(Arrays.asList(getResources().getStringArray(R.array.unit)));
//        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(AdminViewBooking.this, android.R.layout.simple_list_item_1, s);
//        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpinner_unitList.setAdapter(unitAdapter);

        loadFacilityList();
        loadBookingList();

        mbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBookingList.clear();
                String selectedItem = mSpinner_facilityList.getSelectedItem().toString();
                if(selectedItem == "All") {
                    mBookingList.addAll(allBookingList);
                }
                else{
                    for(Booking a :allBookingList){
                        if(a.getFacilityName().equals(selectedItem))
                            mBookingList.add(a);
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    private void loadFacilityList() {
        list.add("All");

        db.collection("bookingFacilities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    if (!result.isEmpty()) {
                        for (QueryDocumentSnapshot document : result) {
                            list.add(document.get("name").toString());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AdminViewBooking.this, android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinner_facilityList.setAdapter(dataAdapter);
                    }
                }
            }
        });
    }

    private void loadBookingList() {
        mBookingList = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(AdminViewBooking.this, "",
                "Loading....", true);

        db.collection("userBooking").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    if (!result.isEmpty()) {
                        for (QueryDocumentSnapshot document : result) {
                            boolean check = checkDate(document.get("date").toString(),document.get("slot").toString());
                            if(check) {
//                            Log.d("data",document.getData().toString());
                                Booking b = new Booking(document.getId().toString(),
                                        document.get("facilities").toString(),
                                        document.get("date").toString(),
                                        document.get("slot").toString());
                                b.setUnit(document.get("unit").toString());
                                mBookingList.add(b);
                            }
                        }
                        if(mBookingList.size() == 0) {
                            dialog.dismiss();
                            alertDataEmpty();
                        }
                        else {
                            dialog.dismiss();
                            allBookingList.addAll(mBookingList);
                            mAdapter = new ViewBookingAdapter(AdminViewBooking.this, mBookingList,true);
                            rv_AdminViewBooking.setLayoutManager(new LinearLayoutManager(AdminViewBooking.this));
                            rv_AdminViewBooking.setAdapter(mAdapter);
                        }
                    }
                }
            }
        });
    }
    private boolean checkDate(String result,String slot) {
        slot = timeSlot(Integer.valueOf(slot));
        result = result.concat(" " + slot);
        Date strDate = null;
        try {
            strDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Date().getTime() > strDate.getTime()) {
            return false;
        }
        else{
            return true;
        }
    }
    public String timeSlot(int position) {
        switch (position) {
            case 0:
                return "09:00";
            case 1:
                return "10:00";
            case 2:
                return "11:00";
            case 3:
                return "12:00";
            case 4:
                return "13:00";
            case 5:
                return "14:00";
            case 6:
                return "15:00";
            case 7:
                return "16:00";
            case 8:
                return "17:00";
            case 9:
                return "18:00";
            case 10:
                return "19:00";
            case 11:
                return "20:00";
            case 12:
                return "21:00";

            default:
                return "Closed";
        }
    }
    private void alertDataEmpty() {
        new AlertDialog.Builder(this)
                .setMessage("No Booking Existing!")
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create().show();
    }

}