package com.example.texter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    TextView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        next =findViewById(R.id.next);
        Intent intent = new Intent(ThirdActivity.this,ForthActivity.class);
        startActivity(intent);
        finish();
    }
}