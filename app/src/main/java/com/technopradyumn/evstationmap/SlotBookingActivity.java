package com.technopradyumn.evstationmap;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.technopradyumn.evstationmap.databinding.ActivitySlotBookingBinding;
import com.technopradyumn.evstationmap.model.BookedSlot;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class SlotBookingActivity extends AppCompatActivity {

    private ActivitySlotBookingBinding binding;
    private Calendar selectedDate;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Date expiryTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySlotBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupSpinners();

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        binding.datePicker.setOnClickListener(v -> showDatePicker());

        selectedDate = Calendar.getInstance();

        // Add click listener to the book slot button
        binding.slotBookBtn.setOnClickListener(v -> bookSlot());
    }

    private void setupSpinners() {

        String[] carOptions = {"Select Car", "Tata", "Hyundai", "Suzuki","MG","Nissan","Renault","BMW","Audi","Audi","Range Rover"};

        ArrayAdapter<String> carAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carOptions);
        carAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerCar.setAdapter(carAdapter);

        binding.spinnerCar.setSelection(0);

        String[] chargerOptions = {"Select Charger Type", "7.2 kW AC - Fast", "3.6 kW DC - Superfast", "60D","8.9 kW DC - Superfast"};

        ArrayAdapter<String> chargerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chargerOptions);
        chargerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerChargertype.setAdapter(chargerAdapter);
        binding.spinnerChargertype.setSelection(0);
    }

    private void showDatePicker() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    selectedDate.set(year1, monthOfYear, dayOfMonth1);

                    // Set the selected date to the 'date' variable
                    selectedDate.set(Calendar.YEAR, year1);
                    selectedDate.set(Calendar.MONTH, monthOfYear);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth1);

                    // Update the date TextView
                    updateDateTextView(year1, monthOfYear, dayOfMonth1);
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void updateDateTextView(int year, int month, int dayOfMonth) {
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.getTime());
        binding.textViewDate.setText("Selected Date: " + formattedDate);
    }

    private void bookSlot() {
        // Check if all fields are selected
        if (binding.spinnerCar.getSelectedItemPosition() != 0 &&
                binding.spinnerChargertype.getSelectedItemPosition() != 0 &&
                !binding.textViewDate.getText().toString().isEmpty()) {

            if (selectedDate != null) {

                String slotId = UUID.randomUUID().toString();

                // Calculate expiry time (set it to 12:00 PM of the selected booking date)
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate.getTime());
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Date expiryTime = calendar.getTime();

                BookedSlot bookedSlot = new BookedSlot(
                        slotId,
                        Objects.requireNonNull(mAuth.getCurrentUser()).getUid(),
                        binding.spinnerCar.getSelectedItem().toString(),
                        binding.spinnerChargertype.getSelectedItem().toString(),
                        selectedDate.getTime(),
                        expiryTime
                );

                // Add booked slot to Firestore
                db.collection("slotBooked")
                        .document(slotId)
                        .set(bookedSlot)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Slot Booked", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to book slot: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                // Schedule slot expiry check
                scheduleSlotExpiryCheck(expiryTime);

            } else {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please select all fields", Toast.LENGTH_SHORT).show();
        }
    }


    private void scheduleSlotExpiryCheck(Date expiryTime) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkAndDeleteExpiredSlots();
            }
        }, 0, 60 * 1000);
    }

    // Method to check and delete expired slots
    private void checkAndDeleteExpiredSlots() {
        Date currentDate = new Date();
        if (expiryTime != null && currentDate.after(expiryTime)) {
            // Delete the slot from Firestore
            // Replace the code below with your Firestore delete operation
            // db.collection("slotBooked").document(slotId).delete();
            // For demonstration, simply printing a message
            System.out.println("Expired slot should be deleted now.");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SlotBookingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}