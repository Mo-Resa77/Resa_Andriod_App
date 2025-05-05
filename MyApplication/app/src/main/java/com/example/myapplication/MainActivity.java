package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    private Button btnGoToLogin, btnGoToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize LottieAnimationView
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie_animation);
        lottieAnimationView.setAnimation(R.raw.lotties);  // Replace with your animation file
        lottieAnimationView.playAnimation();

        // Initialize navigation buttons
        btnGoToLogin = findViewById(R.id.main_btn_login);
        btnGoToSignUp = findViewById(R.id.main_btn_sign);

        // Set click listeners for navigation
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
    }
}
