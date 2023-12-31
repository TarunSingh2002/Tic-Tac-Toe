package com.example.texter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ForthActivity extends AppCompatActivity {

    TextView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forth);
        next =findViewById(R.id.next);
        Intent intent = new Intent(ForthActivity.this,login.class);
        startActivity(intent);
        finish();
    }
}