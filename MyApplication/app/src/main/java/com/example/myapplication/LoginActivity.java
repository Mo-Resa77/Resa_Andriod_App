package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signUpButton;
    private Button forgotPasswordButton;

    // Firebase Authentication and Database instances
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication and Database Reference
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize views
        emailEditText = findViewById(R.id.login_tv_email);
        passwordEditText = findViewById(R.id.login_tv_pass);
        loginButton = findViewById(R.id.login_btn_login);
        signUpButton = findViewById(R.id.login_btn_sign);
        forgotPasswordButton = findViewById(R.id.login_btn_forgot_password);

        // Set up login button click listener
        loginButton.setOnClickListener(view -> validateInputs());

        // Set up sign-up button click listener
        signUpButton.setOnClickListener(view -> {
            // Navigate to SignUpActivity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Set up forgot password button click listener
        forgotPasswordButton.setOnClickListener(view -> {
            // Navigate to ForgotPasswordActivity
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate Email
        if (TextUtils.isEmpty(email)) {
            showToast("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Password is required.");
            return;
        }

        // Proceed with Firebase login
        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Try to fetch the user's name from Realtime Database
                            String userId = user.getUid();
                            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String name = dataSnapshot.child("name").getValue(String.class);
                                        showToast("Welcome back, " + (name != null ? name : "User") + "!");
                                    } else {
                                        showToast("Welcome back, User!");
                                    }

                                    // Navigate to HomeActivity
                                    navigateToHome();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, "Failed to fetch user data: " + databaseError.getMessage());
                                    showToast("Login successful, but failed to fetch user data.");
                                    navigateToHome();
                                }
                            });
                        }
                    } else {
                        // Login failed
                        showToast("Login failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
