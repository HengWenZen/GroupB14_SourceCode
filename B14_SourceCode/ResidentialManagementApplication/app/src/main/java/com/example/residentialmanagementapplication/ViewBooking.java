package com.example.residentialmanagementapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewBooking extends AppCompatActivity {
    private ViewBookingAdapter mAdapter;
    private RecyclerView rv_viewBooking;
    Button mCancelBooking;

    List<String> dateList;
    List<Booking> bookingList;
    SharedPreferences prefs;
//    ArrayList<Object> dateList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        rv_viewBooking = findViewById(R.id.rv_viewBooking);
        mCancelBooking = findViewById(R.id.btn_cancelBooking);

        prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String unit = prefs.getString("unit", null);
        loadSpecificBooking(unit);

    }

    private void loadSpecificBooking(String unit) {
        bookingList = new ArrayList<>();
        ProgressDialog dialog = ProgressDialog.show(ViewBooking.this, "",
                "Loading....", true);

        db.collection("userBooking").whereEqualTo("unit",unit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    boolean check = checkDate(document.get("date").toString(),document.get("slot").toString());
                                    if(check) {
                                        Booking b = new Booking(document.getId().toString(),
                                                document.get("facilities").toString(),
                                                document.get("date").toString(),
                                                document.get("slot").toString());
                                        bookingList.add(b);
                                    }
                                }
                                Log.d("data",bookingList.toString());
                            }
                        }
                    }
        });



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                if(bookingList.size() == 0) {
                    alertDataEmpty();
                }
                else {
                    mAdapter = new ViewBookingAdapter(ViewBooking.this, bookingList,false);
                    rv_viewBooking.setLayoutManager(new LinearLayoutManager(ViewBooking.this));
                    rv_viewBooking.setAdapter(mAdapter);
                }
            }
        }, 2000);

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
    public void adapterChange(int position){
        bookingList.remove(position);
        mAdapter.notifyItemRemoved(position);
        if(bookingList.size() == 0) {
            finish();
        }
    }
}