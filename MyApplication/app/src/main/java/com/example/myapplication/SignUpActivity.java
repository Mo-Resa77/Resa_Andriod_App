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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstName, email, password, confirmPassword;
    private Button signUpButton, goToLoginButton;

    // Firebase instances
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Authentication and Database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI elements
        firstName = findViewById(R.id.sign_tv_first_name);
        email = findViewById(R.id.sign_tv_email);
        password = findViewById(R.id.sign_tv_password);
        confirmPassword = findViewById(R.id.et_confirm_password);
        signUpButton = findViewById(R.id.btn_sign_up);
        goToLoginButton = findViewById(R.id.btn_go_to_login);

        // Set click listener for the sign-up button
        signUpButton.setOnClickListener(view -> validateInputs());

        // Set click listener for the "Go to Login" button
        goToLoginButton.setOnClickListener(view -> navigateToLogin());
    }

    private void validateInputs() {
        String userName = firstName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConfirmPassword = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            showToast("Name is required.");
            return;
        } else if (!userName.matches("^[a-zA-Z ]{3,}$")) {
            showToast("Name must contain only letters and be at least 3 characters long.");
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            showToast("Email is required.");
            return;
        } else if (!userEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showToast("Invalid email format. Example: user@example.com");
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            showToast("Password is required.");
            return;
        } else if (!userPassword.equals(userConfirmPassword)) {
            showToast("Passwords do not match.");
            return;
        }

        signUpUser(userName, userEmail, userPassword);
    }

    private void signUpUser(String userName, String userEmail, String userPassword) {
        Log.d(TAG, "Starting sign-up process...");
        signUpButton.setEnabled(false); // Disable button to prevent multiple clicks

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User created successfully in Firebase Authentication.");

                        // Update Firebase user profile with display name
                        firebaseAuth.getCurrentUser()
                                .updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userName)
                                        .build())
                                .addOnCompleteListener(profileTask -> {
                                    if (profileTask.isSuccessful()) {
                                        Log.d(TAG, "Display name updated successfully.");
                                        saveUserData(userName, userEmail);
                                    } else {
                                        Log.e(TAG, "Failed to update display name.", profileTask.getException());
                                        showToast("Failed to update profile: " + profileTask.getException().getMessage());
                                        signUpButton.setEnabled(true); // Re-enable button
                                    }
                                });
                    } else {
                        Log.e(TAG, "User creation failed.", task.getException());
                        showToast("Sign-Up Failed: " + task.getException().getMessage());
                        signUpButton.setEnabled(true); // Re-enable button
                    }
                });
    }

    private void saveUserData(String userName, String userEmail) {
        Log.d(TAG, "Saving user data to Firebase Realtime Database...");
        String userId = firebaseAuth.getCurrentUser().getUid();
        User user = new User(userName, userEmail);

        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User data saved successfully.");
                        showToast("Sign-Up Successful!");
                        navigateToLogin();
                    } else {
                        Log.e(TAG, "Failed to save user data.", task.getException());
                        showToast("Failed to save user data: " + task.getException().getMessage());
                        signUpButton.setEnabled(true); // Re-enable button
                    }
                });
    }

    private void navigateToLogin() {
        Log.d(TAG, "Navigating to LoginActivity...");
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close SignUpActivity
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

// Define the User class to store user details
class User {
    public String name;
    public String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}


