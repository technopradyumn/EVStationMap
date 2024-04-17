package com.technopradyumn.evstationmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.technopradyumn.evstationmap.R;
import com.technopradyumn.evstationmap.model.BookedSlot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyBookedSlotAdapter extends RecyclerView.Adapter<MyBookedSlotAdapter.ViewHolder> {

    private List<BookedSlot> bookedSlots;
    private Context context;

    public MyBookedSlotAdapter(List<BookedSlot> bookedSlots, Context context) {
        this.bookedSlots = bookedSlots;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return bookedSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCar;
        TextView textViewChargerType;
        TextView textViewBookingTime;
        TextView textViewExpiryTime;
        TextView textViewSlotId;
        TextView textViewUserId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCar = itemView.findViewById(R.id.textViewCar);
            textViewChargerType = itemView.findViewById(R.id.textViewChargerType);
            textViewBookingTime = itemView.findViewById(R.id.textViewBookingTime);
            textViewExpiryTime = itemView.findViewById(R.id.textViewExpiryTime);
            textViewSlotId = itemView.findViewById(R.id.textViewSlotId);
            textViewUserId = itemView.findViewById(R.id.textViewUserId);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookedSlot bookedSlot = bookedSlots.get(bookedSlots.size() - 1 - position);

        holder.textViewCar.setText(bookedSlot.getCar());
        holder.textViewChargerType.setText(bookedSlot.getChargerType());
        holder.textViewBookingTime.setText("Slot Time: "+formatDate(bookedSlot.getBookingTime()));
        holder.textViewExpiryTime.setText("Expire Time: "+formatDate(bookedSlot.getExpiryTime()));
        holder.textViewSlotId.setText("Slot ID: " + bookedSlot.getSlotId());
        holder.textViewUserId.setText("User ID: " + bookedSlot.getUserId());
    }


    // Helper method to format Date object to string
    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            return sdf.format(date);
        } else {
            return "N/A"; // or any default value you prefer
        }
    }

}