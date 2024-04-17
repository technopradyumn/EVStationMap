package com.technopradyumn.evstationmap;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.technopradyumn.evstationmap.adapter.MyBookedSlotAdapter;
import com.technopradyumn.evstationmap.databinding.ActivityMyBookedSlotBinding;
import com.technopradyumn.evstationmap.model.BookedSlot;
import java.util.ArrayList;
import java.util.List;

public class MyBookedSlotActivity extends AppCompatActivity {
    ActivityMyBookedSlotBinding binding;
    private MyBookedSlotAdapter adapter;
    private List<BookedSlot> bookedSlots;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBookedSlotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        bookedSlots = new ArrayList<>();
        adapter = new MyBookedSlotAdapter(bookedSlots, this);

        binding.recyclerViewBookedSlots.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewBookedSlots.setAdapter(adapter);

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        loadBookedSlots();
    }

    private void loadBookedSlots() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = db.collection("slotBooked").whereEqualTo("userId", currentUserId);

        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle errors
                return;
            }

            for (DocumentChange dc : value.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        // New booking added
                        BookedSlot bookedSlot = dc.getDocument().toObject(BookedSlot.class);
                        bookedSlots.add(bookedSlot);
                        adapter.notifyDataSetChanged();
                        break;
                    case MODIFIED:
                        // Handle modified document
                        break;
                    case REMOVED:
                        // Handle removed document
                        break;
                }
            }
        });
    }
}