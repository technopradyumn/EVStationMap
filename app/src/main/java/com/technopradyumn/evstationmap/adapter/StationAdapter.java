package com.technopradyumn.evstationmap.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.technopradyumn.evstationmap.R;
import com.technopradyumn.evstationmap.StationActivity;
import com.technopradyumn.evstationmap.model.StationModel;
import java.util.ArrayList;
import java.util.List;

//public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {
//
//    private List<StationModel> stations = new ArrayList<>();
//
//    public void setStations(List<StationModel> stations) {
//        this.stations = stations;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
//        return new StationViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
//        StationModel station = stations.get(position);
//        holder.bind(station);
//    }
//
//    @Override
//    public int getItemCount() {
//        return stations.size();
//    }
//
//    public class StationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        private TextView nameTextView;
//        private TextView availableChargingPointsTextView;
//
//        public StationViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameTextView = itemView.findViewById(R.id.nameTextView);
//            availableChargingPointsTextView = itemView.findViewById(R.id.availableChargingPointsTextView);
//            itemView.setOnClickListener(this);
//        }
//
//        public void bind(StationModel station) {
//            nameTextView.setText(station.getName());
//            availableChargingPointsTextView.setText(String.valueOf(station.getAvailableChargingPoints()));
//        }
//
//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition();
//            if (position != RecyclerView.NO_POSITION) {
//                StationModel station = stations.get(position);
//                // Start StationActivity and pass station ID
//                Intent intent = new Intent(v.getContext(), StationActivity.class);
//                intent.putExtra("stationId", station.getStationId());
//                v.getContext().startActivity(intent);
//            }
//        }
//    }
//}
