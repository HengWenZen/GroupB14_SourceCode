package com.example.residentialmanagementapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewBookingAdapter extends RecyclerView.Adapter<ViewBookingAdapter.ViewBookingHolder>{
    private Context context;
    private List<Booking> bookingList;
    boolean isAdmin = false;

    public ViewBookingAdapter(Context context, List<Booking> bookingList,boolean isAdmin) {
        this.context = context;
        this.bookingList = bookingList;
        this.isAdmin = isAdmin;
    }


    @NonNull
    @Override
    public ViewBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_view_booking,parent,false);
        return new ViewBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBookingHolder holder, int position) {
        Booking  b = bookingList.get(position);
        holder.mTvFacilityName.setText(b.getFacilityName());
        holder.mTvBookingdateTime.setText(b.getDate());
        holder.mtv_BookingTime.setText(timeSlot(Integer.valueOf(b.getSlot())));

        if(isAdmin == true){
            holder.mCancelBooking.setVisibility(View.INVISIBLE);
            holder.mTvBookingUnit.setVisibility(View.VISIBLE);
            holder.mTvBookingUnit.setText("Unit: " + b.getUnit());
        }
        else{
            holder.mCancelBooking.setVisibility(View.VISIBLE);
            holder.mTvBookingUnit.setVisibility(View.INVISIBLE);
        }
        holder.bindBooking(b);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewBookingHolder extends RecyclerView.ViewHolder{
        TextView mTvFacilityName,mTvBookingdateTime,mtv_BookingTime,mTvBookingUnit;
        Button mCancelBooking;
        Booking bookingInfo;
        public ViewBookingHolder(@NonNull View itemView) {
            super(itemView);

            mTvFacilityName = itemView.findViewById(R.id.tv_bookingFacilitiesName);
            mTvBookingdateTime = itemView.findViewById(R.id.tv_BookingdateTime);
            mtv_BookingTime = itemView.findViewById(R.id.tv_BookingTime);
            mCancelBooking = itemView.findViewById(R.id.btn_cancelBooking);
            mTvBookingUnit = itemView.findViewById(R.id.tv_BookingUnit);

            mCancelBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure you want to cancel this booking?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    db.collection("userBooking").document(bookingInfo.getId()).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Cancel Booking Success", Toast.LENGTH_SHORT).show();
                                                    ((ViewBooking) context).adapterChange(getAdapterPosition());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Cancel Fail Due to Unexpected reason", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();
                }

            });
        }
        public void bindBooking(Booking b) {
            bookingInfo = b;
        }

    }

    public String timeSlot(int position) {
        switch (position) {
            case 0:
                return "8:00-9:00";
            case 1:
                return "9:00-10:00";
            case 2:
                return "10:00-11:00";
            case 3:
                return "11:00-12:00";
            case 4:
                return "12:00-13:00";
            case 5:
                return "13:00-14:00";
            case 6:
                return "14:00-15:00";
            case 7:
                return "15:00-16:00";
            case 8:
                return "16:00-17:00";
            case 9:
                return "17:00-18:00";
            case 10:
                return "18:00-19:00";
            case 11:
                return "19:00-20:00";
            case 12:
                return "20:00-21:00";
            default:
                return "Closed";
        }
    }
}
