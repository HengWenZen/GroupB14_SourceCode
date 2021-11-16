package com.example.residentialmanagementapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimeSlotBooking extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String FACILITY_ID = "facility_id";
    private static final String FACILITY_NAME = "facility_name" ;
    private List<TimeSlot> mTimeSlotList;
    private TimeSlotAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageButton mDatePicker;
    private TextView mDateView;
    private Button mBtnBook;
    private String dateSelected = "";

    SharedPreferences mPreferences;
    List<TimeSlot> timeSlotList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_booking);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mDatePicker = (ImageButton) findViewById(R.id.imagebtn_editDate);
        mDateView = findViewById(R.id.tvShowDate);
        mBtnBook = findViewById(R.id.btn_book);

//        mBtnBook.setEnabled(false);
        //set default date
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day  = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mDateView.setText(year +"\n " + day +" " + new DateFormatSymbols().getMonths()[month]);
        dateSelected = day + "-" + (month + 1) + "-" + year;

        Date currentdate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(currentdate);

        loadAvailableSlot(date);
//        mRecyclerView.setHasFixedSize(true);
//        mAdapter = new TimeSlotAdapter(this);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        mBtnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Book facilites code
                String facilityName = getIntent().getStringExtra(FACILITY_NAME);
                String slot = String.valueOf(mAdapter.selectedSlot);
                if (slot == "")
                    Toast.makeText(TimeSlotBooking.this, "Please select any available slot", Toast.LENGTH_SHORT).show();
                else {
                    String msg = "Are you confirm to book " + facilityName + " on " + dateSelected + " " + mAdapter.timeSlot(Integer.valueOf(slot));

                    AlertDialog.Builder alert = new AlertDialog.Builder(TimeSlotBooking.this);
                    alert.setTitle("Booking Confirmation");
                    alert.setMessage(msg);
                    alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(TimeSlotBooking.this, mAdapter.selectedSlot, Toast.LENGTH_SHORT).show();
                            SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
                            String unit = prefs.getString("unit", null);

                            Map<String, Object> data = new HashMap<>();
                            data.put("slot", slot);
                            data.put("unit", unit);
                            data.put("date" ,dateSelected);
                            data.put("facilities" , facilityName);

                            db.collection("userBooking").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(TimeSlotBooking.this, "Booking Success", Toast.LENGTH_SHORT).show();
                                    // Intent to previous page
//                                    updateUserBooking(unit,dateSelected);
//                                    Intent i = new Intent(TimeSlotBooking.this, FacilityList.class);
//                                    startActivity(i);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TimeSlotBooking.this, "Booking Failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();

                }
            }
        });


    }

    private void updateUserBooking(String unit, String dateSelected) {
        Map<String, Object> data = new HashMap<>();
        data.put("bookingDate", FieldValue.arrayUnion(dateSelected));
//    db.collection("userBooking")
        db.collection("userBooking").document(unit).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void showDatePickerDialog() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day  = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                year,
                month,
                day
        );
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, +7);
        Date newDate = calendar.getTime();
//        mDateView.setText(year +"\n " + day +" " + new DateFormatSymbols().getMonths()[month]);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.getDatePicker().setMaxDate(newDate.getTime());//set only can choose next 7 days
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Date setDate = new GregorianCalendar(year,month,day).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(setDate);

        loadAvailableSlot(date);
        mDateView.setText(year +"\n " + day +" " + new DateFormatSymbols().getMonths()[month]);
//        Toast.makeText(TimeSlotBooking.this, String.valueOf(month+1), Toast.LENGTH_SHORT).show();
        dateSelected = day + "-" + (month + 1) + "-" + year;
    }

    private void loadAvailableSlot(String date){
        timeSlotList = new ArrayList<>();
        String name = getIntent().getStringExtra(FACILITY_NAME);

        ProgressDialog dialog = ProgressDialog.show(TimeSlotBooking.this, "",
                "Loading......", true);

        db.collection("userBooking").whereEqualTo("facilities",name).whereEqualTo("date",date)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot result = task.getResult();
                    if(!result.isEmpty()){
                        Log.d("firebase result", result.toString());
                        for (QueryDocumentSnapshot document : result) {
                            TimeSlot slotlist = new TimeSlot();
                            slotlist.setSlot(document.get("slot").toString());
                            timeSlotList.add(slotlist);

                        }
                        dialog.dismiss();
                    }
                    else dialog.dismiss();

                    mRecyclerView.setHasFixedSize(true);
                    mAdapter = new TimeSlotAdapter(TimeSlotBooking.this,timeSlotList);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(TimeSlotBooking.this,3));

                }
            }
        });
    }
}