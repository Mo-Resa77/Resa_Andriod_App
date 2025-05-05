package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView emailTextView, nameTextView;
    private Button logoutButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Views
        emailTextView = findViewById(R.id.profile_email);
        nameTextView = findViewById(R.id.profile_name);
        logoutButton = findViewById(R.id.profile_logout_button);

        // Get Current User
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // Display user email
            emailTextView.setText("Email: " + user.getEmail());

            // Fetch display name from Firebase Authentication
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                nameTextView.setText("Name: " + displayName);
            } else {
                // Fetch name from Firebase Realtime Database as fallback
                fetchNameFromDatabase(user.getUid());
            }
        }

        // Logout Button Action
        logoutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchNameFromDatabase(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    nameTextView.setText("Name: " + (name != null ? name : "Not Set"));
                } else {
                    nameTextView.setText("Name: Not Set");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                nameTextView.setText("Name: Error fetching name");
            }
        });
    }
}

