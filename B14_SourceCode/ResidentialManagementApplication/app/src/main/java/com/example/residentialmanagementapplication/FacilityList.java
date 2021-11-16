package com.example.residentialmanagementapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FacilityList extends AppCompatActivity {
    RecyclerView mRecyclerView_facility;
    FacilityListAdapter mFacilityListAdapter;
    Button mViewBooking;

    SharedPreferences mPreferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Facility> mFacilityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_list);

        mPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);

        mViewBooking = findViewById(R.id.btn_viewBooking);
        mRecyclerView_facility = findViewById(R.id.rvFacilities);

        loadFacilities();

        mViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FacilityList.this,ViewBooking.class);
                startActivity(i);
            }
        });
    }

    private void loadFacilities() {
        mFacilityList = new ArrayList<>();
        ProgressDialog dialog = ProgressDialog.show(FacilityList.this, "",
                "Loading......", true);

//        SharedPreferences.Editor editor = mPreferences.edit();
//        Set<String> set = new HashSet<String>();

        db.collection("bookingFacilities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot result = task.getResult();

                    if(!result.isEmpty()){
                        for (QueryDocumentSnapshot document : result) {
                            Facility temp = new Facility(document.getId().toString(),document.get("name").toString(),document.get("description").toString());
                            mFacilityList.add(temp);
//                            set.add(document.getId().toString());
                        }
                        dialog.dismiss();
                    }
                    else dialog.dismiss();

//                    editor.putStringSet("facilityList",set);
//                    editor.apply();

                    mFacilityListAdapter = new FacilityListAdapter(FacilityList.this,mFacilityList);
                    mRecyclerView_facility.setLayoutManager(new LinearLayoutManager(FacilityList.this));
                    mRecyclerView_facility.setAdapter(mFacilityListAdapter);

                }
            }
        });
    }
}