package com.example.residentialmanagementapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FacilityListAdapter extends RecyclerView.Adapter<FacilityListAdapter.FacilityViewHolder> {
    private static final String FACILITY_ID = "facility_id";
    private static final String FACILITY_NAME = "facility_name" ;
    private Context context;
    private List<Facility> facilityList;

    public FacilityListAdapter(Context context, List<Facility> facilityList) {
        this.context = context;
        this.facilityList = facilityList;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_facilities,parent,false);
        return new FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        Facility facility = facilityList.get(position);
        holder.mfacilitiesTitle.setText(facility.getName());

        if(facility.getName().equalsIgnoreCase("Gym Room")) holder.imageView_facilities.setImageResource(R.drawable.gym);
        else if(facility.getName().equalsIgnoreCase("Swimming Pool")) holder.imageView_facilities.setImageResource(R.drawable.swimmingpool);
//        holder.mfacilitiesDescription.setText(facility.getDescription());
        holder.bindFacility(facility);
//        holder.imageView_facilities.set
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public class FacilityViewHolder extends RecyclerView.ViewHolder{
        TextView mfacilitiesTitle, mfacilitiesDescription;
        ImageView imageView_facilities;
        CardView mCardView;
        Facility mfacility;

        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);

            mfacilitiesTitle = (TextView) itemView.findViewById(R.id.list_item_facilitiesTitle);
//            mfacilitiesDescription = (TextView) itemView.findViewById(R.id.list_item_facilitiesDescription);
            imageView_facilities = (ImageView) itemView.findViewById(R.id.imageView_facilities);
            mCardView = (CardView) itemView.findViewById(R.id.cv_facilities);

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(context, "Your click "+ mfacility.getId().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,TimeSlotBooking.class);
                    intent.putExtra(FACILITY_ID,mfacility.getId());
                    intent.putExtra(FACILITY_NAME,mfacility.getName());

                    context.startActivity(intent);
                }
            });

        }

        public void bindFacility(Facility facility) {
            mfacility = facility;
        }
    }
}
