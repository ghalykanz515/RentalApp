package com.example.rentalapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OpeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(OpeningActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        TextView appName = findViewById(R.id.app_name);
        SpannableString spannable = new SpannableString("Library Share");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#005DB2")), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0085FF")), 8, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        appName.setText(spannable);
    }
}
