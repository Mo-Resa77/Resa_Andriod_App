package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.animation.ValueAnimator;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Define Java objects from XML objects
    ImageView appname, splashimg;
    TextView typingMessage;
    ProgressBar downloadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Set fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize views
        appname = findViewById(R.id.splash_logo);
        splashimg = findViewById(R.id.img);
        typingMessage = findViewById(R.id.typing_message);
        downloadProgress = findViewById(R.id.download_progress);

        // Set visibility
        typingMessage.setVisibility(TextView.GONE);
        downloadProgress.setVisibility(ProgressBar.GONE);

        // Start typing animation
        new Handler().postDelayed(this::startTypingAnimation, 2000);

        // Start progress bar after typing animation
        new Handler().postDelayed(this::startDownloadProgress, 4000);

        // Transition to MainActivity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish(); // Prevent going back to splash
        }, 10000); // Total splash duration: 10 seconds
    }

    private void startTypingAnimation() {
        typingMessage.setVisibility(TextView.VISIBLE);
        String message = typingMessage.getText().toString();
        int messageLength = message.length();

        ValueAnimator animator = ValueAnimator.ofInt(0, messageLength);
        animator.setDuration(2000); // Typing animation duration
        animator.addUpdateListener(animation -> {
            int index = (int) animation.getAnimatedValue();
            typingMessage.setText(message.substring(0, index));
        });
        animator.start();
    }

    private void startDownloadProgress() {
        downloadProgress.setVisibility(ProgressBar.VISIBLE);

        ValueAnimator downloadAnimator = ValueAnimator.ofInt(0, 100);
        downloadAnimator.setDuration(6000); // 6 seconds download time
        downloadAnimator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            downloadProgress.setProgress(progress);
        });
        downloadAnimator.start();
    }
}
