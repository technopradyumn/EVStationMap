package com.technopradyumn.evstationmap.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technopradyumn.evstationmap.MapsActivity;
import com.technopradyumn.evstationmap.R;
import com.technopradyumn.evstationmap.SingleStationMapsActivity;
import com.technopradyumn.evstationmap.StationActivity;
import com.technopradyumn.evstationmap.model.StationModel;
import java.util.ArrayList;
import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {

    private List<StationModel> stations = new ArrayList<>();

    public void setStations(List<StationModel> stations) {
        this.stations = stations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        StationModel station = stations.get(position);
        holder.bind(station);
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    public class StationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;
        private ImageView imageView;
        private TextView availableChargingPointsTextView;
        private TextView supplierName;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            supplierName = itemView.findViewById(R.id.supplierName);
            availableChargingPointsTextView = itemView.findViewById(R.id.availableChargingPointsTextView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        public void bind(StationModel station) {
            nameTextView.setText(station.getName());
            supplierName.setText(station.getSupplierName());
            availableChargingPointsTextView.setText(String.valueOf(station.getAvailableChargingPoints()));
//            Glide.with(itemView.getContext())
//                    .load(station.getImageUrl())
//                    .placeholder(R.drawable.baseline_maps_home_work_24)
//                    .error(R.drawable.circular_marker_icon)
//                    .into(imageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                StationModel station = stations.get(position);
                Intent intent = new Intent(v.getContext(), SingleStationMapsActivity.class);
                intent.putExtra("stationName", station.getName());
                intent.putExtra("stationLatitude", station.getLatitude());
                intent.putExtra("stationLongitude", station.getLongitude());
                v.getContext().startActivity(intent);
            }
        }

    }
}
